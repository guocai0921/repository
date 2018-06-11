// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AndroidSerializationInstantiator.java

package org.springframework.objenesis.instantiator.android;

import java.io.ObjectStreamClass;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.springframework.objenesis.ObjenesisException;
import org.springframework.objenesis.instantiator.ObjectInstantiator;

public class AndroidSerializationInstantiator
	implements ObjectInstantiator
{

	private final Class type;
	private final ObjectStreamClass objectStreamClass;
	private final Method newInstanceMethod = getNewInstanceMethod();

	public AndroidSerializationInstantiator(Class type)
	{
		this.type = type;
		Method m = null;
		try
		{
			m = java/io/ObjectStreamClass.getMethod("lookupAny", new Class[] {
				java/lang/Class
			});
		}
		catch (NoSuchMethodException e)
		{
			throw new ObjenesisException(e);
		}
		try
		{
			objectStreamClass = (ObjectStreamClass)m.invoke(null, new Object[] {
				type
			});
		}
		catch (IllegalAccessException e)
		{
			throw new ObjenesisException(e);
		}
		catch (InvocationTargetException e)
		{
			throw new ObjenesisException(e);
		}
	}

	public Object newInstance()
	{
		return type.cast(newInstanceMethod.invoke(objectStreamClass, new Object[] {
			type
		}));
		IllegalAccessException e;
		e;
		throw new ObjenesisException(e);
		e;
		throw new ObjenesisException(e);
		e;
		throw new ObjenesisException(e);
	}

	private static Method getNewInstanceMethod()
	{
		Method newInstanceMethod;
		newInstanceMethod = java/io/ObjectStreamClass.getDeclaredMethod("newInstance", new Class[] {
			java/lang/Class
		});
		newInstanceMethod.setAccessible(true);
		return newInstanceMethod;
		RuntimeException e;
		e;
		throw new ObjenesisException(e);
		e;
		throw new ObjenesisException(e);
	}
}
