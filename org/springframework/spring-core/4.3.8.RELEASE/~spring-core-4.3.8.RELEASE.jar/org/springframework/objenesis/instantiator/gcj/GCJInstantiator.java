// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   GCJInstantiator.java

package org.springframework.objenesis.instantiator.gcj;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.springframework.objenesis.ObjenesisException;

// Referenced classes of package org.springframework.objenesis.instantiator.gcj:
//			GCJInstantiatorBase

public class GCJInstantiator extends GCJInstantiatorBase
{

	public GCJInstantiator(Class type)
	{
		super(type);
	}

	public Object newInstance()
	{
		return type.cast(newObjectMethod.invoke(dummyStream, new Object[] {
			type, java/lang/Object
		}));
		RuntimeException e;
		e;
		throw new ObjenesisException(e);
		e;
		throw new ObjenesisException(e);
		e;
		throw new ObjenesisException(e);
	}
}
