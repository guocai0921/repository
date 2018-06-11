// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SerializableTypeWrapper.java

package org.springframework.core;

import java.io.*;
import java.lang.reflect.*;
import org.springframework.util.*;

// Referenced classes of package org.springframework.core:
//			MethodParameter

abstract class SerializableTypeWrapper
{
	static class MethodInvokeTypeProvider
		implements TypeProvider
	{

		private final TypeProvider provider;
		private final String methodName;
		private final Class declaringClass;
		private final int index;
		private transient Method method;
		private volatile transient Object result;

		public Type getType()
		{
			Object result = this.result;
			if (result == null)
			{
				result = ReflectionUtils.invokeMethod(method, provider.getType());
				this.result = result;
			}
			return (result instanceof Type[]) ? ((Type[])(Type[])result)[index] : (Type)result;
		}

		public Object getSource()
		{
			return null;
		}

		private void readObject(ObjectInputStream inputStream)
			throws IOException, ClassNotFoundException
		{
			inputStream.defaultReadObject();
			method = ReflectionUtils.findMethod(declaringClass, methodName);
			if (method.getReturnType() != java/lang/reflect/Type && method.getReturnType() != [Ljava/lang/reflect/Type;)
				throw new IllegalStateException((new StringBuilder()).append("Invalid return type on deserialized method - needs to be Type or Type[]: ").append(method).toString());
			else
				return;
		}

		public MethodInvokeTypeProvider(TypeProvider provider, Method method, int index)
		{
			this.provider = provider;
			methodName = method.getName();
			declaringClass = method.getDeclaringClass();
			this.index = index;
			this.method = method;
		}
	}

	static class MethodParameterTypeProvider
		implements TypeProvider
	{

		private final String methodName;
		private final Class parameterTypes[];
		private final Class declaringClass;
		private final int parameterIndex;
		private transient MethodParameter methodParameter;

		public Type getType()
		{
			return methodParameter.getGenericParameterType();
		}

		public Object getSource()
		{
			return methodParameter;
		}

		private void readObject(ObjectInputStream inputStream)
			throws IOException, ClassNotFoundException
		{
			inputStream.defaultReadObject();
			try
			{
				if (methodName != null)
					methodParameter = new MethodParameter(declaringClass.getDeclaredMethod(methodName, parameterTypes), parameterIndex);
				else
					methodParameter = new MethodParameter(declaringClass.getDeclaredConstructor(parameterTypes), parameterIndex);
			}
			catch (Throwable ex)
			{
				throw new IllegalStateException("Could not find original class structure", ex);
			}
		}

		public MethodParameterTypeProvider(MethodParameter methodParameter)
		{
			if (methodParameter.getMethod() != null)
			{
				methodName = methodParameter.getMethod().getName();
				parameterTypes = methodParameter.getMethod().getParameterTypes();
			} else
			{
				methodName = null;
				parameterTypes = methodParameter.getConstructor().getParameterTypes();
			}
			declaringClass = methodParameter.getDeclaringClass();
			parameterIndex = methodParameter.getParameterIndex();
			this.methodParameter = methodParameter;
		}
	}

	static class FieldTypeProvider
		implements TypeProvider
	{

		private final String fieldName;
		private final Class declaringClass;
		private transient Field field;

		public Type getType()
		{
			return field.getGenericType();
		}

		public Object getSource()
		{
			return field;
		}

		private void readObject(ObjectInputStream inputStream)
			throws IOException, ClassNotFoundException
		{
			inputStream.defaultReadObject();
			try
			{
				field = declaringClass.getDeclaredField(fieldName);
			}
			catch (Throwable ex)
			{
				throw new IllegalStateException("Could not find original class structure", ex);
			}
		}

		public FieldTypeProvider(Field field)
		{
			fieldName = field.getName();
			declaringClass = field.getDeclaringClass();
			this.field = field;
		}
	}

	private static class TypeProxyInvocationHandler
		implements InvocationHandler, Serializable
	{

		private final TypeProvider provider;

		public Object invoke(Object proxy, Method method, Object args[])
			throws Throwable
		{
			if (method.getName().equals("equals"))
			{
				Object other = args[0];
				if (other instanceof Type)
					other = SerializableTypeWrapper.unwrap((Type)other);
				return Boolean.valueOf(provider.getType().equals(other));
			}
			if (method.getName().equals("hashCode"))
				return Integer.valueOf(provider.getType().hashCode());
			if (method.getName().equals("getTypeProvider"))
				return provider;
			if (java/lang/reflect/Type == method.getReturnType() && args == null)
				return SerializableTypeWrapper.forTypeProvider(new MethodInvokeTypeProvider(provider, method, -1));
			if ([Ljava/lang/reflect/Type; == method.getReturnType() && args == null)
			{
				Type result[] = new Type[((Type[])(Type[])method.invoke(provider.getType(), args)).length];
				for (int i = 0; i < result.length; i++)
					result[i] = SerializableTypeWrapper.forTypeProvider(new MethodInvokeTypeProvider(provider, method, i));

				return result;
			}
			return method.invoke(provider.getType(), args);
			InvocationTargetException ex;
			ex;
			throw ex.getTargetException();
		}

		public TypeProxyInvocationHandler(TypeProvider provider)
		{
			this.provider = provider;
		}
	}

	private static abstract class DefaultTypeProvider
		implements TypeProvider
	{

		public Object getSource()
		{
			return null;
		}

		private DefaultTypeProvider()
		{
		}

	}

	static interface TypeProvider
		extends Serializable
	{

		public abstract Type getType();

		public abstract Object getSource();
	}

	static interface SerializableTypeProxy
	{

		public abstract TypeProvider getTypeProvider();
	}


	private static final Class SUPPORTED_SERIALIZABLE_TYPES[] = {
		java/lang/reflect/GenericArrayType, java/lang/reflect/ParameterizedType, java/lang/reflect/TypeVariable, java/lang/reflect/WildcardType
	};
	private static final ConcurrentReferenceHashMap cache = new ConcurrentReferenceHashMap(256);

	SerializableTypeWrapper()
	{
	}

	public static Type forField(Field field)
	{
		Assert.notNull(field, "Field must not be null");
		return forTypeProvider(new FieldTypeProvider(field));
	}

	public static Type forMethodParameter(MethodParameter methodParameter)
	{
		return forTypeProvider(new MethodParameterTypeProvider(methodParameter));
	}

	public static Type forGenericSuperclass(Class type)
	{
		return forTypeProvider(new DefaultTypeProvider(type) {

			final Class val$type;

			public Type getType()
			{
				return type.getGenericSuperclass();
			}

			
			{
				type = class1;
				super();
			}
		});
	}

	public static Type[] forGenericInterfaces(Class type)
	{
		Type result[] = new Type[type.getGenericInterfaces().length];
		for (int i = 0; i < result.length; i++)
		{
			int index = i;
			result[i] = forTypeProvider(new DefaultTypeProvider(type, index) {

				final Class val$type;
				final int val$index;

				public Type getType()
				{
					return type.getGenericInterfaces()[index];
				}

			
			{
				type = class1;
				index = i;
				super();
			}
			});
		}

		return result;
	}

	public static Type[] forTypeParameters(Class type)
	{
		Type result[] = new Type[type.getTypeParameters().length];
		for (int i = 0; i < result.length; i++)
		{
			int index = i;
			result[i] = forTypeProvider(new DefaultTypeProvider(type, index) {

				final Class val$type;
				final int val$index;

				public Type getType()
				{
					return type.getTypeParameters()[index];
				}

			
			{
				type = class1;
				index = i;
				super();
			}
			});
		}

		return result;
	}

	public static Type unwrap(Type type)
	{
		Type unwrapped;
		for (unwrapped = type; unwrapped instanceof SerializableTypeProxy; unwrapped = ((SerializableTypeProxy)type).getTypeProvider().getType());
		return unwrapped;
	}

	static Type forTypeProvider(TypeProvider provider)
	{
		Assert.notNull(provider, "Provider must not be null");
		if ((provider.getType() instanceof Serializable) || provider.getType() == null)
			return provider.getType();
		Type cached = (Type)cache.get(provider.getType());
		if (cached != null)
			return cached;
		Class aclass[] = SUPPORTED_SERIALIZABLE_TYPES;
		int i = aclass.length;
		for (int j = 0; j < i; j++)
		{
			Class type = aclass[j];
			if (type.isAssignableFrom(provider.getType().getClass()))
			{
				ClassLoader classLoader = provider.getClass().getClassLoader();
				Class interfaces[] = {
					type, org/springframework/core/SerializableTypeWrapper$SerializableTypeProxy, java/io/Serializable
				};
				InvocationHandler handler = new TypeProxyInvocationHandler(provider);
				cached = (Type)Proxy.newProxyInstance(classLoader, interfaces, handler);
				cache.put(provider.getType(), cached);
				return cached;
			}
		}

		throw new IllegalArgumentException((new StringBuilder()).append("Unsupported Type class: ").append(provider.getType().getClass().getName()).toString());
	}

}
