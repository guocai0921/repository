// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DefaultParameterNameDiscoverer.java

package org.springframework.core;

import org.springframework.util.ClassUtils;

// Referenced classes of package org.springframework.core:
//			PrioritizedParameterNameDiscoverer, StandardReflectionParameterNameDiscoverer, LocalVariableTableParameterNameDiscoverer

public class DefaultParameterNameDiscoverer extends PrioritizedParameterNameDiscoverer
{

	private static final boolean standardReflectionAvailable = ClassUtils.isPresent("java.lang.reflect.Executable", org/springframework/core/DefaultParameterNameDiscoverer.getClassLoader());

	public DefaultParameterNameDiscoverer()
	{
		if (standardReflectionAvailable)
			addDiscoverer(new StandardReflectionParameterNameDiscoverer());
		addDiscoverer(new LocalVariableTableParameterNameDiscoverer());
	}

}
