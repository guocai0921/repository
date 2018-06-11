// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   EnumerablePropertySource.java

package org.springframework.core.env;

import org.springframework.util.ObjectUtils;

// Referenced classes of package org.springframework.core.env:
//			PropertySource

public abstract class EnumerablePropertySource extends PropertySource
{

	public EnumerablePropertySource(String name, Object source)
	{
		super(name, source);
	}

	protected EnumerablePropertySource(String name)
	{
		super(name);
	}

	public boolean containsProperty(String name)
	{
		return ObjectUtils.containsElement(getPropertyNames(), name);
	}

	public abstract String[] getPropertyNames();
}
