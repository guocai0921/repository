// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SunReflectionFactoryHelper.java

package org.springframework.objenesis.instantiator.sun;

import java.lang.reflect.*;
import org.springframework.objenesis.ObjenesisException;

class SunReflectionFactoryHelper
{

	SunReflectionFactoryHelper()
	{
	}

	public static Constructor newConstructorForSerialization(Class type, Constructor constructor)
	{
		Object reflectionFactory;
		Method newConstructorForSerializationMethod;
		Class reflectionFactoryClass = getReflectionFactoryClass();
		reflectionFactory = createReflectionFactory(reflectionFactoryClass);
		newConstructorForSerializationMethod = getNewConstructorForSerializationMethod(reflectionFactoryClass);
		return (Constructor)newConstructorForSerializationMethod.invoke(reflectionFactory, new Object[] {
			type, constructor
		});
		IllegalArgumentException e;
		e;
		throw new ObjenesisException(e);
		e;
		throw new ObjenesisException(e);
		e;
		throw new ObjenesisException(e);
	}

	private static Class getReflectionFactoryClass()
	{
		return Class.forName("sun.reflect.ReflectionFactory");
		ClassNotFoundException e;
		e;
		throw new ObjenesisException(e);
	}

	private static Object createReflectionFactory(Class reflectionFactoryClass)
	{
		Method method = reflectionFactoryClass.getDeclaredMethod("getReflectionFactory", new Class[0]);
		return method.invoke(null, new Object[0]);
		NoSuchMethodException e;
		e;
		throw new ObjenesisException(e);
		e;
		throw new ObjenesisException(e);
		e;
		throw new ObjenesisException(e);
		e;
		throw new ObjenesisException(e);
	}

	private static Method getNewConstructorForSerializationMethod(Class reflectionFactoryClass)
	{
		return reflectionFactoryClass.getDeclaredMethod("newConstructorForSerialization", new Class[] {
			java/lang/Class, java/lang/reflect/Constructor
		});
		NoSuchMethodException e;
		e;
		throw new ObjenesisException(e);
	}
}
