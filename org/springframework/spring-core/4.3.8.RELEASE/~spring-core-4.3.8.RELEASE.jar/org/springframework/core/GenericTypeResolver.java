// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   GenericTypeResolver.java

package org.springframework.core;

import java.lang.reflect.*;
import java.util.*;
import org.springframework.util.Assert;
import org.springframework.util.ConcurrentReferenceHashMap;

// Referenced classes of package org.springframework.core:
//			ResolvableType, MethodParameter

public abstract class GenericTypeResolver
{
	private static class TypeVariableMapVariableResolver
		implements ResolvableType.VariableResolver
	{

		private final Map typeVariableMap;

		public ResolvableType resolveVariable(TypeVariable variable)
		{
			Type type = (Type)typeVariableMap.get(variable);
			return type == null ? null : ResolvableType.forType(type);
		}

		public Object getSource()
		{
			return typeVariableMap;
		}

		public TypeVariableMapVariableResolver(Map typeVariableMap)
		{
			this.typeVariableMap = typeVariableMap;
		}
	}


	private static final Map typeVariableCache = new ConcurrentReferenceHashMap();

	public GenericTypeResolver()
	{
	}

	/**
	 * @deprecated Method getTargetType is deprecated
	 */

	public static Type getTargetType(MethodParameter methodParameter)
	{
		Assert.notNull(methodParameter, "MethodParameter must not be null");
		return methodParameter.getGenericParameterType();
	}

	public static Class resolveParameterType(MethodParameter methodParameter, Class implementationClass)
	{
		Assert.notNull(methodParameter, "MethodParameter must not be null");
		Assert.notNull(implementationClass, "Class must not be null");
		methodParameter.setContainingClass(implementationClass);
		ResolvableType.resolveMethodParameter(methodParameter);
		return methodParameter.getParameterType();
	}

	public static Class resolveReturnType(Method method, Class clazz)
	{
		Assert.notNull(method, "Method must not be null");
		Assert.notNull(clazz, "Class must not be null");
		return ResolvableType.forMethodReturnType(method, clazz).resolve(method.getReturnType());
	}

	/**
	 * @deprecated Method resolveReturnTypeForGenericMethod is deprecated
	 */

	public static Class resolveReturnTypeForGenericMethod(Method method, Object args[], ClassLoader classLoader)
	{
		Type genericReturnType;
		Type methodArgumentTypes[];
		int i;
		Assert.notNull(method, "Method must not be null");
		Assert.notNull(((Object) (args)), "Argument array must not be null");
		TypeVariable declaredTypeVariables[] = method.getTypeParameters();
		genericReturnType = method.getGenericReturnType();
		methodArgumentTypes = method.getGenericParameterTypes();
		if (declaredTypeVariables.length == 0)
			return method.getReturnType();
		if (args.length < methodArgumentTypes.length)
			return null;
		boolean locallyDeclaredTypeVariableMatchesReturnType = false;
		TypeVariable atypevariable[] = declaredTypeVariables;
		int j = atypevariable.length;
		int k = 0;
		do
		{
			if (k >= j)
				break;
			TypeVariable currentTypeVariable = atypevariable[k];
			if (currentTypeVariable.equals(genericReturnType))
			{
				locallyDeclaredTypeVariableMatchesReturnType = true;
				break;
			}
			k++;
		} while (true);
		if (!locallyDeclaredTypeVariableMatchesReturnType)
			break MISSING_BLOCK_LABEL_296;
		i = 0;
_L5:
		if (i >= methodArgumentTypes.length) goto _L2; else goto _L1
_L1:
		Type atype[];
		int l;
		int i1;
		Type currentMethodArgumentType = methodArgumentTypes[i];
		if (currentMethodArgumentType.equals(genericReturnType))
			return args[i].getClass();
		if (!(currentMethodArgumentType instanceof ParameterizedType))
			continue; /* Loop/switch isn't completed */
		ParameterizedType parameterizedType = (ParameterizedType)currentMethodArgumentType;
		Type actualTypeArguments[] = parameterizedType.getActualTypeArguments();
		atype = actualTypeArguments;
		l = atype.length;
		i1 = 0;
_L4:
		Object arg;
		if (i1 >= l)
			continue; /* Loop/switch isn't completed */
		Type typeArg = atype[i1];
		if (!typeArg.equals(genericReturnType))
			break MISSING_BLOCK_LABEL_284;
		arg = args[i];
		if (arg instanceof Class)
			return (Class)arg;
		if (!(arg instanceof String) || classLoader == null)
			break MISSING_BLOCK_LABEL_279;
		return classLoader.loadClass((String)arg);
		ClassNotFoundException ex;
		ex;
		throw new IllegalStateException((new StringBuilder()).append("Could not resolve specific class name argument [").append(arg).append("]").toString(), ex);
		return method.getReturnType();
		i1++;
		if (true) goto _L4; else goto _L3
_L3:
		i++;
		  goto _L5
_L2:
		return method.getReturnType();
	}

	public static Class resolveReturnTypeArgument(Method method, Class genericIfc)
	{
		Assert.notNull(method, "method must not be null");
		ResolvableType resolvableType = ResolvableType.forMethodReturnType(method).as(genericIfc);
		if (!resolvableType.hasGenerics() || (resolvableType.getType() instanceof WildcardType))
			return null;
		else
			return getSingleGeneric(resolvableType);
	}

	public static Class resolveTypeArgument(Class clazz, Class genericIfc)
	{
		ResolvableType resolvableType = ResolvableType.forClass(clazz).as(genericIfc);
		if (!resolvableType.hasGenerics())
			return null;
		else
			return getSingleGeneric(resolvableType);
	}

	private static Class getSingleGeneric(ResolvableType resolvableType)
	{
		if (resolvableType.getGenerics().length > 1)
			throw new IllegalArgumentException((new StringBuilder()).append("Expected 1 type argument on generic interface [").append(resolvableType).append("] but found ").append(resolvableType.getGenerics().length).toString());
		else
			return resolvableType.getGeneric(new int[0]).resolve();
	}

	public static Class[] resolveTypeArguments(Class clazz, Class genericIfc)
	{
		ResolvableType type = ResolvableType.forClass(clazz).as(genericIfc);
		if (!type.hasGenerics() || type.isEntirelyUnresolvable())
			return null;
		else
			return type.resolveGenerics(java/lang/Object);
	}

	public static Class resolveType(Type genericType, Map map)
	{
		return ResolvableType.forType(genericType, new TypeVariableMapVariableResolver(map)).resolve(java/lang/Object);
	}

	public static Map getTypeVariableMap(Class clazz)
	{
		Map typeVariableMap = (Map)typeVariableCache.get(clazz);
		if (typeVariableMap == null)
		{
			typeVariableMap = new HashMap();
			buildTypeVariableMap(ResolvableType.forClass(clazz), typeVariableMap);
			typeVariableCache.put(clazz, Collections.unmodifiableMap(typeVariableMap));
		}
		return typeVariableMap;
	}

	private static void buildTypeVariableMap(ResolvableType type, Map typeVariableMap)
	{
		if (type != ResolvableType.NONE)
		{
			if (type.getType() instanceof ParameterizedType)
			{
				TypeVariable variables[] = type.resolve().getTypeParameters();
				for (int i = 0; i < variables.length; i++)
				{
					ResolvableType generic;
					for (generic = type.getGeneric(new int[] {
	i
}); generic.getType() instanceof TypeVariable; generic = generic.resolveType());
					if (generic != ResolvableType.NONE)
						typeVariableMap.put(variables[i], generic.getType());
				}

			}
			buildTypeVariableMap(type.getSuperType(), typeVariableMap);
			ResolvableType aresolvabletype[] = type.getInterfaces();
			int j = aresolvabletype.length;
			for (int k = 0; k < j; k++)
			{
				ResolvableType interfaceType = aresolvabletype[k];
				buildTypeVariableMap(interfaceType, typeVariableMap);
			}

			if (type.resolve().isMemberClass())
				buildTypeVariableMap(ResolvableType.forClass(type.resolve().getEnclosingClass()), typeVariableMap);
		}
	}

}
