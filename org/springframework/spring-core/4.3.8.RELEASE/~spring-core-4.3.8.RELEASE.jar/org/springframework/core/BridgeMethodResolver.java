// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   BridgeMethodResolver.java

package org.springframework.core;

import java.lang.reflect.Method;
import java.util.*;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

// Referenced classes of package org.springframework.core:
//			ResolvableType

public abstract class BridgeMethodResolver
{

	public BridgeMethodResolver()
	{
	}

	public static Method findBridgedMethod(Method bridgeMethod)
	{
		if (bridgeMethod == null || !bridgeMethod.isBridge())
			return bridgeMethod;
		List candidateMethods = new ArrayList();
		Method methods[] = ReflectionUtils.getAllDeclaredMethods(bridgeMethod.getDeclaringClass());
		Method amethod[] = methods;
		int i = amethod.length;
		for (int j = 0; j < i; j++)
		{
			Method candidateMethod = amethod[j];
			if (isBridgedCandidateFor(candidateMethod, bridgeMethod))
				candidateMethods.add(candidateMethod);
		}

		if (candidateMethods.size() == 1)
			return (Method)candidateMethods.get(0);
		Method bridgedMethod = searchCandidates(candidateMethods, bridgeMethod);
		if (bridgedMethod != null)
			return bridgedMethod;
		else
			return bridgeMethod;
	}

	private static boolean isBridgedCandidateFor(Method candidateMethod, Method bridgeMethod)
	{
		return !candidateMethod.isBridge() && !candidateMethod.equals(bridgeMethod) && candidateMethod.getName().equals(bridgeMethod.getName()) && candidateMethod.getParameterTypes().length == bridgeMethod.getParameterTypes().length;
	}

	private static Method searchCandidates(List candidateMethods, Method bridgeMethod)
	{
		if (candidateMethods.isEmpty())
			return null;
		Method previousMethod = null;
		boolean sameSig = true;
		for (Iterator iterator = candidateMethods.iterator(); iterator.hasNext();)
		{
			Method candidateMethod = (Method)iterator.next();
			if (isBridgeMethodFor(bridgeMethod, candidateMethod, bridgeMethod.getDeclaringClass()))
				return candidateMethod;
			if (previousMethod != null)
				sameSig = sameSig && Arrays.equals(candidateMethod.getGenericParameterTypes(), previousMethod.getGenericParameterTypes());
			previousMethod = candidateMethod;
		}

		return sameSig ? (Method)candidateMethods.get(0) : null;
	}

	static boolean isBridgeMethodFor(Method bridgeMethod, Method candidateMethod, Class declaringClass)
	{
		if (isResolvedTypeMatch(candidateMethod, bridgeMethod, declaringClass))
		{
			return true;
		} else
		{
			Method method = findGenericDeclaration(bridgeMethod);
			return method != null && isResolvedTypeMatch(method, candidateMethod, declaringClass);
		}
	}

	private static Method findGenericDeclaration(Method bridgeMethod)
	{
		for (Class superclass = bridgeMethod.getDeclaringClass().getSuperclass(); superclass != null && java/lang/Object != superclass; superclass = superclass.getSuperclass())
		{
			Method method = searchForMatch(superclass, bridgeMethod);
			if (method != null && !method.isBridge())
				return method;
		}

		Class interfaces[] = ClassUtils.getAllInterfacesForClass(bridgeMethod.getDeclaringClass());
		Class aclass[] = interfaces;
		int i = aclass.length;
		for (int j = 0; j < i; j++)
		{
			Class ifc = aclass[j];
			Method method = searchForMatch(ifc, bridgeMethod);
			if (method != null && !method.isBridge())
				return method;
		}

		return null;
	}

	private static boolean isResolvedTypeMatch(Method genericMethod, Method candidateMethod, Class declaringClass)
	{
		java.lang.reflect.Type genericParameters[] = genericMethod.getGenericParameterTypes();
		Class candidateParameters[] = candidateMethod.getParameterTypes();
		if (genericParameters.length != candidateParameters.length)
			return false;
		for (int i = 0; i < candidateParameters.length; i++)
		{
			ResolvableType genericParameter = ResolvableType.forMethodParameter(genericMethod, i, declaringClass);
			Class candidateParameter = candidateParameters[i];
			if (candidateParameter.isArray() && !candidateParameter.getComponentType().equals(genericParameter.getComponentType().resolve(java/lang/Object)))
				return false;
			if (!candidateParameter.equals(genericParameter.resolve(java/lang/Object)))
				return false;
		}

		return true;
	}

	private static Method searchForMatch(Class type, Method bridgeMethod)
	{
		return ReflectionUtils.findMethod(type, bridgeMethod.getName(), bridgeMethod.getParameterTypes());
	}

	public static boolean isVisibilityBridgeMethodPair(Method bridgeMethod, Method bridgedMethod)
	{
		if (bridgeMethod == bridgedMethod)
			return true;
		else
			return Arrays.equals(bridgeMethod.getParameterTypes(), bridgedMethod.getParameterTypes()) && bridgeMethod.getReturnType().equals(bridgedMethod.getReturnType());
	}
}
