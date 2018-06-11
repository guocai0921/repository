// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   NullInstantiator.java

package org.springframework.objenesis.instantiator.basic;

import org.springframework.objenesis.instantiator.ObjectInstantiator;

public class NullInstantiator
	implements ObjectInstantiator
{

	public NullInstantiator(Class type)
	{
	}

	public Object newInstance()
	{
		return null;
	}
}
