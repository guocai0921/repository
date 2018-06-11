// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   UnsafeFactoryInstantiator.java

package org.springframework.objenesis.instantiator.sun;

import java.lang.reflect.Field;
import org.springframework.objenesis.ObjenesisException;
import org.springframework.objenesis.instantiator.ObjectInstantiator;
import sun.misc.Unsafe;

public class UnsafeFactoryInstantiator
	implements ObjectInstantiator
{

	private static Unsafe unsafe;
	private final Class type;

	public UnsafeFactoryInstantiator(Class type)
	{
		if (unsafe == null)
		{
			Field f;
			try
			{
				f = sun/misc/Unsafe.getDeclaredField("theUnsafe");
			}
			catch (NoSuchFieldException e)
			{
				throw new ObjenesisException(e);
			}
			f.setAccessible(true);
			try
			{
				unsafe = (Unsafe)f.get(null);
			}
			catch (IllegalAccessException e)
			{
				throw new ObjenesisException(e);
			}
		}
		this.type = type;
	}

	public Object newInstance()
	{
		return type.cast(unsafe.allocateInstance(type));
		InstantiationException e;
		e;
		throw new ObjenesisException(e);
	}
}
