// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ConstructorInstantiator.java

package org.springframework.objenesis.instantiator.basic;

import java.lang.reflect.Constructor;
import org.springframework.objenesis.ObjenesisException;
import org.springframework.objenesis.instantiator.ObjectInstantiator;

public class ConstructorInstantiator
	implements ObjectInstantiator
{

	protected Constructor constructor;

	public ConstructorInstantiator(Class type)
	{
		try
		{
			constructor = type.getDeclaredConstructor((Class[])null);
		}
		catch (Exception e)
		{
			throw new ObjenesisException(e);
		}
	}

	public Object newInstance()
	{
		return constructor.newInstance((Object[])null);
		Exception e;
		e;
		throw new ObjenesisException(e);
	}
}
