// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   PrioritizedParameterNameDiscoverer.java

package org.springframework.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;

// Referenced classes of package org.springframework.core:
//			ParameterNameDiscoverer

public class PrioritizedParameterNameDiscoverer
	implements ParameterNameDiscoverer
{

	private final List parameterNameDiscoverers = new LinkedList();

	public PrioritizedParameterNameDiscoverer()
	{
	}

	public void addDiscoverer(ParameterNameDiscoverer pnd)
	{
		parameterNameDiscoverers.add(pnd);
	}

	public String[] getParameterNames(Method method)
	{
		for (Iterator iterator = parameterNameDiscoverers.iterator(); iterator.hasNext();)
		{
			ParameterNameDiscoverer pnd = (ParameterNameDiscoverer)iterator.next();
			String result[] = pnd.getParameterNames(method);
			if (result != null)
				return result;
		}

		return null;
	}

	public String[] getParameterNames(Constructor ctor)
	{
		for (Iterator iterator = parameterNameDiscoverers.iterator(); iterator.hasNext();)
		{
			ParameterNameDiscoverer pnd = (ParameterNameDiscoverer)iterator.next();
			String result[] = pnd.getParameterNames(ctor);
			if (result != null)
				return result;
		}

		return null;
	}
}
