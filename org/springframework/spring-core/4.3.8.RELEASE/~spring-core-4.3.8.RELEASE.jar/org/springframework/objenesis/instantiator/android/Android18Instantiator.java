// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Android18Instantiator.java

package org.springframework.objenesis.instantiator.android;

import java.io.ObjectStreamClass;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.springframework.objenesis.ObjenesisException;
import org.springframework.objenesis.instantiator.ObjectInstantiator;

public class Android18Instantiator
	implements ObjectInstantiator
{

	private final Class type;
	private final Method newInstanceMethod = getNewInstanceMethod();
	private final Long objectConstructorId = findConstructorIdForJavaLangObjectConstructor();

	public Android18Instantiator(Class type)
	{
		this.type = type;
	}

	public Object newInstance()
	{
		return type.cast(newInstanceMethod.invoke(null, new Object[] {
			type, objectConstructorId
		}));
		Exception e;
		e;
		throw new ObjenesisException(e);
	}

	private static Method getNewInstanceMethod()
	{
		Method newInstanceMethod;
		newInstanceMethod = java/io/ObjectStreamClass.getDeclaredMethod("newInstance", new Class[] {
			java/lang/Class, Long.TYPE
		});
		newInstanceMethod.setAccessible(true);
		return newInstanceMethod;
		RuntimeException e;
		e;
		throw new ObjenesisException(e);
		e;
		throw new ObjenesisException(e);
	}

	private static Long findConstructorIdForJavaLangObjectConstructor()
	{
		Method newInstanceMethod;
		newInstanceMethod = java/io/ObjectStreamClass.getDeclaredMethod("getConstructorId", new Class[] {
			java/lang/Class
		});
		newInstanceMethod.setAccessible(true);
		return (Long)newInstanceMethod.invoke(null, new Object[] {
			java/lang/Object
		});
		RuntimeException e;
		e;
		throw new ObjenesisException(e);
		e;
		throw new ObjenesisException(e);
		e;
		throw new ObjenesisException(e);
		e;
		throw new ObjenesisException(e);
	}
}
