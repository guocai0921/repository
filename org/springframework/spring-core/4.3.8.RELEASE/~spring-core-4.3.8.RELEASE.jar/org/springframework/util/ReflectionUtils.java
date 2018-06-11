// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ReflectionUtils.java

package org.springframework.util;

import java.lang.reflect.*;
import java.sql.SQLException;
import java.util.*;

// Referenced classes of package org.springframework.util:
//			ConcurrentReferenceHashMap, Assert

public abstract class ReflectionUtils
{
	public static interface FieldFilter
	{

		public abstract boolean matches(Field field);
	}

	public static interface FieldCallback
	{

		public abstract void doWith(Field field)
			throws IllegalArgumentException, IllegalAccessException;
	}

	public static interface MethodFilter
	{

		public abstract boolean matches(Method method);
	}

	public static interface MethodCallback
	{

		public abstract void doWith(Method method)
			throws IllegalArgumentException, IllegalAccessException;
	}


	private static final String CGLIB_RENAMED_METHOD_PREFIX = "CGLIB$";
	private static final Method NO_METHODS[] = new Method[0];
	private static final Field NO_FIELDS[] = new Field[0];
	private static final Map declaredMethodsCache = new ConcurrentReferenceHashMap(256);
	private static final Map declaredFieldsCache = new ConcurrentReferenceHashMap(256);
	public static final FieldFilter COPYABLE_FIELDS = new FieldFilter() {

		public boolean matches(Field field)
		{
			return !Modifier.isStatic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers());
		}

	};
	public static final MethodFilter NON_BRIDGED_METHODS = new MethodFilter() {

		public boolean matches(Method method)
		{
			return !method.isBridge();
		}

	};
	public static final MethodFilter USER_DECLARED_METHODS = new MethodFilter() {

		public boolean matches(Method method)
		{
			return !method.isBridge() && method.getDeclaringClass() != java/lang/Object;
		}

	};

	public ReflectionUtils()
	{
	}

	public static Field findField(Class clazz, String name)
	{
		return findField(clazz, name, null);
	}

	public static Field findField(Class clazz, String name, Class type)
	{
		Assert.notNull(clazz, "Class must not be null");
		Assert.isTrue(name != null || type != null, "Either name or type of the field must be specified");
		for (Class searchType = clazz; java/lang/Object != searchType && searchType != null; searchType = searchType.getSuperclass())
		{
			Field fields[] = getDeclaredFields(searchType);
			Field afield[] = fields;
			int i = afield.length;
			for (int j = 0; j < i; j++)
			{
				Field field = afield[j];
				if ((name == null || name.equals(field.getName())) && (type == null || type.equals(field.getType())))
					return field;
			}

		}

		return null;
	}

	public static void setField(Field field, Object target, Object value)
	{
		try
		{
			field.set(target, value);
		}
		catch (IllegalAccessException ex)
		{
			handleReflectionException(ex);
			throw new IllegalStateException((new StringBuilder()).append("Unexpected reflection exception - ").append(ex.getClass().getName()).append(": ").append(ex.getMessage()).toString());
		}
	}

	public static Object getField(Field field, Object target)
	{
		return field.get(target);
		IllegalAccessException ex;
		ex;
		handleReflectionException(ex);
		throw new IllegalStateException((new StringBuilder()).append("Unexpected reflection exception - ").append(ex.getClass().getName()).append(": ").append(ex.getMessage()).toString());
	}

	public static Method findMethod(Class clazz, String name)
	{
		return findMethod(clazz, name, new Class[0]);
	}

	public static transient Method findMethod(Class clazz, String name, Class paramTypes[])
	{
		Assert.notNull(clazz, "Class must not be null");
		Assert.notNull(name, "Method name must not be null");
		for (Class searchType = clazz; searchType != null; searchType = searchType.getSuperclass())
		{
			Method methods[] = searchType.isInterface() ? searchType.getMethods() : getDeclaredMethods(searchType);
			Method amethod[] = methods;
			int i = amethod.length;
			for (int j = 0; j < i; j++)
			{
				Method method = amethod[j];
				if (name.equals(method.getName()) && (paramTypes == null || Arrays.equals(paramTypes, method.getParameterTypes())))
					return method;
			}

		}

		return null;
	}

	public static Object invokeMethod(Method method, Object target)
	{
		return invokeMethod(method, target, new Object[0]);
	}

	public static transient Object invokeMethod(Method method, Object target, Object args[])
	{
		return method.invoke(target, args);
		Exception ex;
		ex;
		handleReflectionException(ex);
		throw new IllegalStateException("Should never get here");
	}

	public static Object invokeJdbcMethod(Method method, Object target)
		throws SQLException
	{
		return invokeJdbcMethod(method, target, new Object[0]);
	}

	public static transient Object invokeJdbcMethod(Method method, Object target, Object args[])
		throws SQLException
	{
		return method.invoke(target, args);
		IllegalAccessException ex;
		ex;
		handleReflectionException(ex);
		break MISSING_BLOCK_LABEL_38;
		ex;
		if (ex.getTargetException() instanceof SQLException)
			throw (SQLException)ex.getTargetException();
		handleInvocationTargetException(ex);
		throw new IllegalStateException("Should never get here");
	}

	public static void handleReflectionException(Exception ex)
	{
		if (ex instanceof NoSuchMethodException)
			throw new IllegalStateException((new StringBuilder()).append("Method not found: ").append(ex.getMessage()).toString());
		if (ex instanceof IllegalAccessException)
			throw new IllegalStateException((new StringBuilder()).append("Could not access method: ").append(ex.getMessage()).toString());
		if (ex instanceof InvocationTargetException)
			handleInvocationTargetException((InvocationTargetException)ex);
		if (ex instanceof RuntimeException)
			throw (RuntimeException)ex;
		else
			throw new UndeclaredThrowableException(ex);
	}

	public static void handleInvocationTargetException(InvocationTargetException ex)
	{
		rethrowRuntimeException(ex.getTargetException());
	}

	public static void rethrowRuntimeException(Throwable ex)
	{
		if (ex instanceof RuntimeException)
			throw (RuntimeException)ex;
		if (ex instanceof Error)
			throw (Error)ex;
		else
			throw new UndeclaredThrowableException(ex);
	}

	public static void rethrowException(Throwable ex)
		throws Exception
	{
		if (ex instanceof Exception)
			throw (Exception)ex;
		if (ex instanceof Error)
			throw (Error)ex;
		else
			throw new UndeclaredThrowableException(ex);
	}

	public static boolean declaresException(Method method, Class exceptionType)
	{
		Assert.notNull(method, "Method must not be null");
		Class declaredExceptions[] = method.getExceptionTypes();
		Class aclass[] = declaredExceptions;
		int i = aclass.length;
		for (int j = 0; j < i; j++)
		{
			Class declaredException = aclass[j];
			if (declaredException.isAssignableFrom(exceptionType))
				return true;
		}

		return false;
	}

	public static boolean isPublicStaticFinal(Field field)
	{
		int modifiers = field.getModifiers();
		return Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers);
	}

	public static boolean isEqualsMethod(Method method)
	{
		if (method == null || !method.getName().equals("equals"))
		{
			return false;
		} else
		{
			Class paramTypes[] = method.getParameterTypes();
			return paramTypes.length == 1 && paramTypes[0] == java/lang/Object;
		}
	}

	public static boolean isHashCodeMethod(Method method)
	{
		return method != null && method.getName().equals("hashCode") && method.getParameterTypes().length == 0;
	}

	public static boolean isToStringMethod(Method method)
	{
		return method != null && method.getName().equals("toString") && method.getParameterTypes().length == 0;
	}

	public static boolean isObjectMethod(Method method)
	{
		if (method == null)
			return false;
		java/lang/Object.getDeclaredMethod(method.getName(), method.getParameterTypes());
		return true;
		Exception ex;
		ex;
		return false;
	}

	public static boolean isCglibRenamedMethod(Method renamedMethod)
	{
		String name = renamedMethod.getName();
		if (name.startsWith("CGLIB$"))
		{
			int i;
			for (i = name.length() - 1; i >= 0 && Character.isDigit(name.charAt(i)); i--);
			return i > "CGLIB$".length() && i < name.length() - 1 && name.charAt(i) == '$';
		} else
		{
			return false;
		}
	}

	public static void makeAccessible(Field field)
	{
		if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers()) || Modifier.isFinal(field.getModifiers())) && !field.isAccessible())
			field.setAccessible(true);
	}

	public static void makeAccessible(Method method)
	{
		if ((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers())) && !method.isAccessible())
			method.setAccessible(true);
	}

	public static void makeAccessible(Constructor ctor)
	{
		if ((!Modifier.isPublic(ctor.getModifiers()) || !Modifier.isPublic(ctor.getDeclaringClass().getModifiers())) && !ctor.isAccessible())
			ctor.setAccessible(true);
	}

	public static void doWithLocalMethods(Class clazz, MethodCallback mc)
	{
		Method methods[] = getDeclaredMethods(clazz);
		Method amethod[] = methods;
		int i = amethod.length;
		for (int j = 0; j < i; j++)
		{
			Method method = amethod[j];
			try
			{
				mc.doWith(method);
			}
			catch (IllegalAccessException ex)
			{
				throw new IllegalStateException((new StringBuilder()).append("Not allowed to access method '").append(method.getName()).append("': ").append(ex).toString());
			}
		}

	}

	public static void doWithMethods(Class clazz, MethodCallback mc)
	{
		doWithMethods(clazz, mc, null);
	}

	public static void doWithMethods(Class clazz, MethodCallback mc, MethodFilter mf)
	{
		Method methods[] = getDeclaredMethods(clazz);
		Method amethod[] = methods;
		int i = amethod.length;
		for (int k = 0; k < i; k++)
		{
			Method method = amethod[k];
			if (mf != null && !mf.matches(method))
				continue;
			try
			{
				mc.doWith(method);
			}
			catch (IllegalAccessException ex)
			{
				throw new IllegalStateException((new StringBuilder()).append("Not allowed to access method '").append(method.getName()).append("': ").append(ex).toString());
			}
		}

		if (clazz.getSuperclass() != null)
			doWithMethods(clazz.getSuperclass(), mc, mf);
		else
		if (clazz.isInterface())
		{
			Class aclass[] = clazz.getInterfaces();
			int j = aclass.length;
			for (int l = 0; l < j; l++)
			{
				Class superIfc = aclass[l];
				doWithMethods(superIfc, mc, mf);
			}

		}
	}

	public static Method[] getAllDeclaredMethods(Class leafClass)
	{
		List methods = new ArrayList(32);
		doWithMethods(leafClass, new MethodCallback(methods) {

			final List val$methods;

			public void doWith(Method method)
			{
				methods.add(method);
			}

			
			{
				methods = list;
				super();
			}
		});
		return (Method[])methods.toArray(new Method[methods.size()]);
	}

	public static Method[] getUniqueDeclaredMethods(Class leafClass)
	{
		List methods = new ArrayList(32);
		doWithMethods(leafClass, new MethodCallback(methods) {

			final List val$methods;

			public void doWith(Method method)
			{
				boolean knownSignature = false;
				Method methodBeingOverriddenWithCovariantReturnType = null;
				Iterator iterator = methods.iterator();
				do
				{
					if (!iterator.hasNext())
						break;
					Method existingMethod = (Method)iterator.next();
					if (!method.getName().equals(existingMethod.getName()) || !Arrays.equals(method.getParameterTypes(), existingMethod.getParameterTypes()))
						continue;
					if (existingMethod.getReturnType() != method.getReturnType() && existingMethod.getReturnType().isAssignableFrom(method.getReturnType()))
						methodBeingOverriddenWithCovariantReturnType = existingMethod;
					else
						knownSignature = true;
					break;
				} while (true);
				if (methodBeingOverriddenWithCovariantReturnType != null)
					methods.remove(methodBeingOverriddenWithCovariantReturnType);
				if (!knownSignature && !ReflectionUtils.isCglibRenamedMethod(method))
					methods.add(method);
			}

			
			{
				methods = list;
				super();
			}
		});
		return (Method[])methods.toArray(new Method[methods.size()]);
	}

	private static Method[] getDeclaredMethods(Class clazz)
	{
		Assert.notNull(clazz, "Class must not be null");
		Method result[] = (Method[])declaredMethodsCache.get(clazz);
		if (result == null)
		{
			Method declaredMethods[] = clazz.getDeclaredMethods();
			List defaultMethods = findConcreteMethodsOnInterfaces(clazz);
			if (defaultMethods != null)
			{
				result = new Method[declaredMethods.length + defaultMethods.size()];
				System.arraycopy(declaredMethods, 0, result, 0, declaredMethods.length);
				int index = declaredMethods.length;
				for (Iterator iterator = defaultMethods.iterator(); iterator.hasNext();)
				{
					Method defaultMethod = (Method)iterator.next();
					result[index] = defaultMethod;
					index++;
				}

			} else
			{
				result = declaredMethods;
			}
			declaredMethodsCache.put(clazz, result.length != 0 ? ((Object) (result)) : ((Object) (NO_METHODS)));
		}
		return result;
	}

	private static List findConcreteMethodsOnInterfaces(Class clazz)
	{
		List result = null;
		Class aclass[] = clazz.getInterfaces();
		int i = aclass.length;
		for (int j = 0; j < i; j++)
		{
			Class ifc = aclass[j];
			Method amethod[] = ifc.getMethods();
			int k = amethod.length;
			for (int l = 0; l < k; l++)
			{
				Method ifcMethod = amethod[l];
				if (Modifier.isAbstract(ifcMethod.getModifiers()))
					continue;
				if (result == null)
					result = new LinkedList();
				result.add(ifcMethod);
			}

		}

		return result;
	}

	public static void doWithLocalFields(Class clazz, FieldCallback fc)
	{
		Field afield[] = getDeclaredFields(clazz);
		int i = afield.length;
		for (int j = 0; j < i; j++)
		{
			Field field = afield[j];
			try
			{
				fc.doWith(field);
			}
			catch (IllegalAccessException ex)
			{
				throw new IllegalStateException((new StringBuilder()).append("Not allowed to access field '").append(field.getName()).append("': ").append(ex).toString());
			}
		}

	}

	public static void doWithFields(Class clazz, FieldCallback fc)
	{
		doWithFields(clazz, fc, null);
	}

	public static void doWithFields(Class clazz, FieldCallback fc, FieldFilter ff)
	{
		Class targetClass = clazz;
		do
		{
			Field fields[] = getDeclaredFields(targetClass);
			Field afield[] = fields;
			int i = afield.length;
			for (int j = 0; j < i; j++)
			{
				Field field = afield[j];
				if (ff != null && !ff.matches(field))
					continue;
				try
				{
					fc.doWith(field);
				}
				catch (IllegalAccessException ex)
				{
					throw new IllegalStateException((new StringBuilder()).append("Not allowed to access field '").append(field.getName()).append("': ").append(ex).toString());
				}
			}

			targetClass = targetClass.getSuperclass();
		} while (targetClass != null && targetClass != java/lang/Object);
	}

	private static Field[] getDeclaredFields(Class clazz)
	{
		Assert.notNull(clazz, "Class must not be null");
		Field result[] = (Field[])declaredFieldsCache.get(clazz);
		if (result == null)
		{
			result = clazz.getDeclaredFields();
			declaredFieldsCache.put(clazz, result.length != 0 ? ((Object) (result)) : ((Object) (NO_FIELDS)));
		}
		return result;
	}

	public static void shallowCopyFieldState(Object src, Object dest)
	{
		if (src == null)
			throw new IllegalArgumentException("Source for field copy cannot be null");
		if (dest == null)
			throw new IllegalArgumentException("Destination for field copy cannot be null");
		if (!src.getClass().isAssignableFrom(dest.getClass()))
		{
			throw new IllegalArgumentException((new StringBuilder()).append("Destination class [").append(dest.getClass().getName()).append("] must be same or subclass as source class [").append(src.getClass().getName()).append("]").toString());
		} else
		{
			doWithFields(src.getClass(), new FieldCallback(src, dest) {

				final Object val$src;
				final Object val$dest;

				public void doWith(Field field)
					throws IllegalArgumentException, IllegalAccessException
				{
					ReflectionUtils.makeAccessible(field);
					Object srcValue = field.get(src);
					field.set(dest, srcValue);
				}

			
			{
				src = obj;
				dest = obj1;
				super();
			}
			}, COPYABLE_FIELDS);
			return;
		}
	}

	public static void clearCache()
	{
		declaredMethodsCache.clear();
		declaredFieldsCache.clear();
	}

}
