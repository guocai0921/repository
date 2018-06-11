// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ObjectStreamClassInstantiator.java

package org.springframework.objenesis.instantiator.basic;

import java.io.ObjectStreamClass;
import java.lang.reflect.Method;
import org.springframework.objenesis.ObjenesisException;
import org.springframework.objenesis.instantiator.ObjectInstantiator;

public class ObjectStreamClassInstantiator
	implements ObjectInstantiator
{

	private static Method newInstanceMethod;
	private final ObjectStreamClass objStreamClass;

	private static void initialize()
	{
		if (newInstanceMethod == null)
			try
			{
				newInstanceMethod = java/io/ObjectStreamClass.getDeclaredMethod("newInstance", new Class[0]);
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

	public ObjectStreamClassInstantiator(Class type)
	{
		initialize();
		objStreamClass = ObjectStreamClass.lookup(type);
	}

	public Object newInstance()
	{
		return newInstanceMethod.invoke(objStreamClass, new Object[0]);
		Exception e;
		e;
		throw new ObjenesisException(e);
	}
}
