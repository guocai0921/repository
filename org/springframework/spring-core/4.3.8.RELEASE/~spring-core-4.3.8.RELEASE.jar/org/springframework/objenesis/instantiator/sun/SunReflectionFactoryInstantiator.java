// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SunReflectionFactoryInstantiator.java

package org.springframework.objenesis.instantiator.sun;

import java.lang.reflect.Constructor;
import org.springframework.objenesis.ObjenesisException;
import org.springframework.objenesis.instantiator.ObjectInstantiator;

// Referenced classes of package org.springframework.objenesis.instantiator.sun:
//			SunReflectionFactoryHelper

public class SunReflectionFactoryInstantiator
	implements ObjectInstantiator
{

	private final Constructor mungedConstructor;

	public SunReflectionFactoryInstantiator(Class type)
	{
		Constructor javaLangObjectConstructor = getJavaLangObjectConstructor();
		mungedConstructor = SunReflectionFactoryHelper.newConstructorForSerialization(type, javaLangObjectConstructor);
		mungedConstructor.setAccessible(true);
	}

	public Object newInstance()
	{
		return mungedConstructor.newInstance((Object[])null);
		Exception e;
		e;
		throw new ObjenesisException(e);
	}

	private static Constructor getJavaLangObjectConstructor()
	{
		return java/lang/Object.getConstructor((Class[])null);
		NoSuchMethodException e;
		e;
		throw new ObjenesisException(e);
	}
}
