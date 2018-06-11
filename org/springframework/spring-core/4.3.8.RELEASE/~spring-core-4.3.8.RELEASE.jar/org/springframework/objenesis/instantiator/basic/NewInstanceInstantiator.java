// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   NewInstanceInstantiator.java

package org.springframework.objenesis.instantiator.basic;

import org.springframework.objenesis.ObjenesisException;
import org.springframework.objenesis.instantiator.ObjectInstantiator;

public class NewInstanceInstantiator
	implements ObjectInstantiator
{

	private final Class type;

	public NewInstanceInstantiator(Class type)
	{
		this.type = type;
	}

	public Object newInstance()
	{
		return type.newInstance();
		Exception e;
		e;
		throw new ObjenesisException(e);
	}
}
