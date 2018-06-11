// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   PercInstantiator.java

package org.springframework.objenesis.instantiator.perc;

import java.io.ObjectInputStream;
import java.lang.reflect.Method;
import org.springframework.objenesis.ObjenesisException;
import org.springframework.objenesis.instantiator.ObjectInstantiator;

public class PercInstantiator
	implements ObjectInstantiator
{

	private final Method newInstanceMethod;
	private final Object typeArgs[];

	public PercInstantiator(Class type)
	{
		typeArgs = (new Object[] {
			null, Boolean.FALSE
		});
		typeArgs[0] = type;
		try
		{
			newInstanceMethod = java/io/ObjectInputStream.getDeclaredMethod("newInstance", new Class[] {
				java/lang/Class, Boolean.TYPE
			});
			newInstanceMethod.setAccessible(true);
		}
		catch (RuntimeException e)
		{
			throw new ObjenesisException(e);
		}
		catch (NoSuchMethodException e)
		{
			throw new ObjenesisException(e);
		}
	}

	public Object newInstance()
	{
		return newInstanceMethod.invoke(null, typeArgs);
		Exception e;
		e;
		throw new ObjenesisException(e);
	}
}
