// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   GCJSerializationInstantiator.java

package org.springframework.objenesis.instantiator.gcj;

import java.lang.reflect.Method;
import org.springframework.objenesis.ObjenesisException;
import org.springframework.objenesis.instantiator.SerializationInstantiatorHelper;

// Referenced classes of package org.springframework.objenesis.instantiator.gcj:
//			GCJInstantiatorBase

public class GCJSerializationInstantiator extends GCJInstantiatorBase
{

	private Class superType;

	public GCJSerializationInstantiator(Class type)
	{
		super(type);
		superType = SerializationInstantiatorHelper.getNonSerializableSuperClass(type);
	}

	public Object newInstance()
	{
		return type.cast(newObjectMethod.invoke(dummyStream, new Object[] {
			type, superType
		}));
		Exception e;
		e;
		throw new ObjenesisException(e);
	}
}
