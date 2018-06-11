// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   StandardReflectionParameterNameDiscoverer.java

package org.springframework.core;

import java.lang.reflect.*;

// Referenced classes of package org.springframework.core:
//			ParameterNameDiscoverer

public class StandardReflectionParameterNameDiscoverer
	implements ParameterNameDiscoverer
{

	public StandardReflectionParameterNameDiscoverer()
	{
	}

	public String[] getParameterNames(Method method)
	{
		Parameter parameters[] = method.getParameters();
		String parameterNames[] = new String[parameters.length];
		for (int i = 0; i < parameters.length; i++)
		{
			Parameter param = parameters[i];
			if (!param.isNamePresent())
				return null;
			parameterNames[i] = param.getName();
		}

		return parameterNames;
	}

	public String[] getParameterNames(Constructor ctor)
	{
		Parameter parameters[] = ctor.getParameters();
		String parameterNames[] = new String[parameters.length];
		for (int i = 0; i < parameters.length; i++)
		{
			Parameter param = parameters[i];
			if (!param.isNamePresent())
				return null;
			parameterNames[i] = param.getName();
		}

		return parameterNames;
	}
}
