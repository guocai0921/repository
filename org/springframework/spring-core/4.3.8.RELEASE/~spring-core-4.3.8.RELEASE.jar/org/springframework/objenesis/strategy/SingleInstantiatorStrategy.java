// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SingleInstantiatorStrategy.java

package org.springframework.objenesis.strategy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.springframework.objenesis.ObjenesisException;
import org.springframework.objenesis.instantiator.ObjectInstantiator;

// Referenced classes of package org.springframework.objenesis.strategy:
//			InstantiatorStrategy

public class SingleInstantiatorStrategy
	implements InstantiatorStrategy
{

	private Constructor constructor;

	public SingleInstantiatorStrategy(Class instantiator)
	{
		try
		{
			constructor = instantiator.getConstructor(new Class[] {
				java/lang/Class
			});
		}
		catch (NoSuchMethodException e)
		{
			throw new ObjenesisException(e);
		}
	}

	public ObjectInstantiator newInstantiatorOf(Class type)
	{
		return (ObjectInstantiator)constructor.newInstance(new Object[] {
			type
		});
		InstantiationException e;
		e;
		throw new ObjenesisException(e);
		e;
		throw new ObjenesisException(e);
		e;
		throw new ObjenesisException(e);
	}
}
