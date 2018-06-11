// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ObjenesisBase.java

package org.springframework.objenesis;

import java.util.concurrent.ConcurrentHashMap;
import org.springframework.objenesis.instantiator.ObjectInstantiator;
import org.springframework.objenesis.strategy.InstantiatorStrategy;

// Referenced classes of package org.springframework.objenesis:
//			Objenesis

public class ObjenesisBase
	implements Objenesis
{

	protected final InstantiatorStrategy strategy;
	protected ConcurrentHashMap cache;

	public ObjenesisBase(InstantiatorStrategy strategy)
	{
		this(strategy, true);
	}

	public ObjenesisBase(InstantiatorStrategy strategy, boolean useCache)
	{
		if (strategy == null)
		{
			throw new IllegalArgumentException("A strategy can't be null");
		} else
		{
			this.strategy = strategy;
			cache = useCache ? new ConcurrentHashMap() : null;
			return;
		}
	}

	public String toString()
	{
		return (new StringBuilder()).append(getClass().getName()).append(" using ").append(strategy.getClass().getName()).append(cache != null ? " with" : " without").append(" caching").toString();
	}

	public Object newInstance(Class clazz)
	{
		return getInstantiatorOf(clazz).newInstance();
	}

	public ObjectInstantiator getInstantiatorOf(Class clazz)
	{
		if (clazz.isPrimitive())
			throw new IllegalArgumentException("Primitive types can't be instantiated in Java");
		if (cache == null)
			return strategy.newInstantiatorOf(clazz);
		ObjectInstantiator instantiator = (ObjectInstantiator)cache.get(clazz.getName());
		if (instantiator == null)
		{
			ObjectInstantiator newInstantiator = strategy.newInstantiatorOf(clazz);
			instantiator = (ObjectInstantiator)cache.putIfAbsent(clazz.getName(), newInstantiator);
			if (instantiator == null)
				instantiator = newInstantiator;
		}
		return instantiator;
	}
}
