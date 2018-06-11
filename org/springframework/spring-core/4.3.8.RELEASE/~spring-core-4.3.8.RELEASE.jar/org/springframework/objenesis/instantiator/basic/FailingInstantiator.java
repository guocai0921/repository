// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   FailingInstantiator.java

package org.springframework.objenesis.instantiator.basic;

import org.springframework.objenesis.ObjenesisException;
import org.springframework.objenesis.instantiator.ObjectInstantiator;

public class FailingInstantiator
	implements ObjectInstantiator
{

	public FailingInstantiator(Class type)
	{
	}

	public Object newInstance()
	{
		throw new ObjenesisException("Always failing");
	}
}
