// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   NamedThreadLocal.java

package org.springframework.core;

import org.springframework.util.Assert;

public class NamedThreadLocal extends ThreadLocal
{

	private final String name;

	public NamedThreadLocal(String name)
	{
		Assert.hasText(name, "Name must not be empty");
		this.name = name;
	}

	public String toString()
	{
		return name;
	}
}
