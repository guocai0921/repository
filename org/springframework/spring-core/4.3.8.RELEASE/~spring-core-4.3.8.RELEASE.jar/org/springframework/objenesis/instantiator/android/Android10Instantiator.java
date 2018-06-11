// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Android10Instantiator.java

package org.springframework.objenesis.instantiator.android;

import java.io.ObjectInputStream;
import java.lang.reflect.Method;
import org.springframework.objenesis.ObjenesisException;
import org.springframework.objenesis.instantiator.ObjectInstantiator;

public class Android10Instantiator
	implements ObjectInstantiator
{

	private final Class type;
	private final Method newStaticMethod = getNewStaticMethod();

	public Android10Instantiator(Class type)
	{
		this.type = type;
	}

	public Object newInstance()
	{
		return type.cast(newStaticMethod.invoke(null, new Object[] {
			type, java/lang/Object
		}));
		Exception e;
		e;
		throw new ObjenesisException(e);
	}

	private static Method getNewStaticMethod()
	{
		Method newStaticMethod;
		newStaticMethod = java/io/ObjectInputStream.getDeclaredMethod("newInstance", new Class[] {
			java/lang/Class, java/lang/Class
		});
		newStaticMethod.setAccessible(true);
		return newStaticMethod;
		RuntimeException e;
		e;
		throw new ObjenesisException(e);
		e;
		throw new ObjenesisException(e);
	}
}
