// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SerializationInstantiatorHelper.java

package org.springframework.objenesis.instantiator;

import java.io.Serializable;

public class SerializationInstantiatorHelper
{

	public SerializationInstantiatorHelper()
	{
	}

	public static Class getNonSerializableSuperClass(Class type)
	{
		Class result;
		for (result = type; java/io/Serializable.isAssignableFrom(result);)
		{
			result = result.getSuperclass();
			if (result == null)
				throw new Error("Bad class hierarchy: No non-serializable parents");
		}

		return result;
	}
}
