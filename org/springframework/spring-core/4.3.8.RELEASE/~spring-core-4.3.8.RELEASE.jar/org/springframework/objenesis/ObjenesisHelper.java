// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ObjenesisHelper.java

package org.springframework.objenesis;

import java.io.Serializable;
import org.springframework.objenesis.instantiator.ObjectInstantiator;

// Referenced classes of package org.springframework.objenesis:
//			Objenesis, ObjenesisStd, ObjenesisSerializer

public final class ObjenesisHelper
{

	private static final Objenesis OBJENESIS_STD = new ObjenesisStd();
	private static final Objenesis OBJENESIS_SERIALIZER = new ObjenesisSerializer();

	private ObjenesisHelper()
	{
	}

	public static Object newInstance(Class clazz)
	{
		return OBJENESIS_STD.newInstance(clazz);
	}

	public static Serializable newSerializableInstance(Class clazz)
	{
		return (Serializable)OBJENESIS_SERIALIZER.newInstance(clazz);
	}

	public static ObjectInstantiator getInstantiatorOf(Class clazz)
	{
		return OBJENESIS_STD.getInstantiatorOf(clazz);
	}

	public static ObjectInstantiator getSerializableObjectInstantiatorOf(Class clazz)
	{
		return OBJENESIS_SERIALIZER.getInstantiatorOf(clazz);
	}

}
