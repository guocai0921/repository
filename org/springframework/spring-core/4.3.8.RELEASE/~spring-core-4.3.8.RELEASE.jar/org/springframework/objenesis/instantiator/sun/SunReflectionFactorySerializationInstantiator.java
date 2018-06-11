// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SunReflectionFactorySerializationInstantiator.java

package org.springframework.objenesis.instantiator.sun;

import java.io.NotSerializableException;
import java.lang.reflect.Constructor;
import org.springframework.objenesis.ObjenesisException;
import org.springframework.objenesis.instantiator.ObjectInstantiator;
import org.springframework.objenesis.instantiator.SerializationInstantiatorHelper;

// Referenced classes of package org.springframework.objenesis.instantiator.sun:
//			SunReflectionFactoryHelper

public class SunReflectionFactorySerializationInstantiator
	implements ObjectInstantiator
{

	private final Constructor mungedConstructor;

	public SunReflectionFactorySerializationInstantiator(Class type)
	{
		Class nonSerializableAncestor = SerializationInstantiatorHelper.getNonSerializableSuperClass(type);
		Constructor nonSerializableAncestorConstructor;
		try
		{
			nonSerializableAncestorConstructor = nonSerializableAncestor.getConstructor((Class[])null);
		}
		catch (NoSuchMethodException e)
		{
			throw new ObjenesisException(new NotSerializableException((new StringBuilder()).append(type).append(" has no suitable superclass constructor").toString()));
		}
		mungedConstructor = SunReflectionFactoryHelper.newConstructorForSerialization(type, nonSerializableAncestorConstructor);
		mungedConstructor.setAccessible(true);
	}

	public Object newInstance()
	{
		return mungedConstructor.newInstance((Object[])null);
		Exception e;
		e;
		throw new ObjenesisException(e);
	}
}
