// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   MethodInvoker.java

package org.springframework.util;

import java.lang.reflect.*;

// Referenced classes of package org.springframework.util:
//			ClassUtils, ReflectionUtils

public class MethodInvoker
{

	private Class targetClass;
	private Object targetObject;
	private String targetMethod;
	private String staticMethod;
	private Object arguments[];
	private Method methodObject;

	public MethodInvoker()
	{
		arguments = new Object[0];
	}

	public void setTargetClass(Class targetClass)
	{
		this.targetClass = targetClass;
	}

	public Class getTargetClass()
	{
		return targetClass;
	}

	public void setTargetObject(Object targetObject)
	{
		this.targetObject = targetObject;
		if (targetObject != null)
			targetClass = targetObject.getClass();
	}

	public Object getTargetObject()
	{
		return targetObject;
	}

	public void setTargetMethod(String targetMethod)
	{
		this.targetMethod = targetMethod;
	}

	public String getTargetMethod()
	{
		return targetMethod;
	}

	public void setStaticMethod(String staticMethod)
	{
		this.staticMethod = staticMethod;
	}

	public void setArguments(Object arguments[])
	{
		this.arguments = arguments == null ? new Object[0] : arguments;
	}

	public Object[] getArguments()
	{
		return arguments;
	}

	public void prepare()
		throws ClassNotFoundException, NoSuchMethodException
	{
		if (staticMethod != null)
		{
			int lastDotIndex = staticMethod.lastIndexOf('.');
			if (lastDotIndex == -1 || lastDotIndex == staticMethod.length())
				throw new IllegalArgumentException("staticMethod must be a fully qualified class plus method name: e.g. 'example.MyExampleClass.myExampleMethod'");
			String className = staticMethod.substring(0, lastDotIndex);
			String methodName = staticMethod.substring(lastDotIndex + 1);
			this.targetClass = resolveClassName(className);
			this.targetMethod = methodName;
		}
		Class targetClass = getTargetClass();
		String targetMethod = getTargetMethod();
		if (targetClass == null)
			throw new IllegalArgumentException("Either 'targetClass' or 'targetObject' is required");
		if (targetMethod == null)
			throw new IllegalArgumentException("Property 'targetMethod' is required");
		Object arguments[] = getArguments();
		Class argTypes[] = new Class[arguments.length];
		for (int i = 0; i < arguments.length; i++)
			argTypes[i] = arguments[i] == null ? java/lang/Object : arguments[i].getClass();

		try
		{
			methodObject = targetClass.getMethod(targetMethod, argTypes);
		}
		catch (NoSuchMethodException ex)
		{
			methodObject = findMatchingMethod();
			if (methodObject == null)
				throw ex;
		}
	}

	protected Class resolveClassName(String className)
		throws ClassNotFoundException
	{
		return ClassUtils.forName(className, ClassUtils.getDefaultClassLoader());
	}

	protected Method findMatchingMethod()
	{
		String targetMethod = getTargetMethod();
		Object arguments[] = getArguments();
		int argCount = arguments.length;
		Method candidates[] = ReflectionUtils.getAllDeclaredMethods(getTargetClass());
		int minTypeDiffWeight = 0x7fffffff;
		Method matchingMethod = null;
		Method amethod[] = candidates;
		int i = amethod.length;
		for (int j = 0; j < i; j++)
		{
			Method candidate = amethod[j];
			if (!candidate.getName().equals(targetMethod))
				continue;
			Class paramTypes[] = candidate.getParameterTypes();
			if (paramTypes.length != argCount)
				continue;
			int typeDiffWeight = getTypeDifferenceWeight(paramTypes, arguments);
			if (typeDiffWeight < minTypeDiffWeight)
			{
				minTypeDiffWeight = typeDiffWeight;
				matchingMethod = candidate;
			}
		}

		return matchingMethod;
	}

	public Method getPreparedMethod()
		throws IllegalStateException
	{
		if (methodObject == null)
			throw new IllegalStateException("prepare() must be called prior to invoke() on MethodInvoker");
		else
			return methodObject;
	}

	public boolean isPrepared()
	{
		return methodObject != null;
	}

	public Object invoke()
		throws InvocationTargetException, IllegalAccessException
	{
		Object targetObject = getTargetObject();
		Method preparedMethod = getPreparedMethod();
		if (targetObject == null && !Modifier.isStatic(preparedMethod.getModifiers()))
		{
			throw new IllegalArgumentException("Target method must not be non-static without a target");
		} else
		{
			ReflectionUtils.makeAccessible(preparedMethod);
			return preparedMethod.invoke(targetObject, getArguments());
		}
	}

	public static int getTypeDifferenceWeight(Class paramTypes[], Object args[])
	{
		int result = 0;
		for (int i = 0; i < paramTypes.length; i++)
		{
			if (!ClassUtils.isAssignableValue(paramTypes[i], args[i]))
				return 0x7fffffff;
			if (args[i] == null)
				continue;
			Class paramType = paramTypes[i];
			for (Class superClass = args[i].getClass().getSuperclass(); superClass != null;)
				if (paramType.equals(superClass))
				{
					result += 2;
					superClass = null;
				} else
				if (ClassUtils.isAssignable(paramType, superClass))
				{
					result += 2;
					superClass = superClass.getSuperclass();
				} else
				{
					superClass = null;
				}

			if (paramType.isInterface())
				result++;
		}

		return result;
	}
}
