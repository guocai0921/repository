// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SpringObjenesis.java

package org.springframework.objenesis;

import org.springframework.core.SpringProperties;
import org.springframework.objenesis.instantiator.ObjectInstantiator;
import org.springframework.objenesis.strategy.InstantiatorStrategy;
import org.springframework.objenesis.strategy.StdInstantiatorStrategy;
import org.springframework.util.ConcurrentReferenceHashMap;

// Referenced classes of package org.springframework.objenesis:
//			ObjenesisException, Objenesis

public class SpringObjenesis
	implements Objenesis
{

	public static final String IGNORE_OBJENESIS_PROPERTY_NAME = "spring.objenesis.ignore";
	private final InstantiatorStrategy strategy;
	private final ConcurrentReferenceHashMap cache;
	private volatile Boolean worthTrying;

	public SpringObjenesis()
	{
		this(null);
	}

	public SpringObjenesis(InstantiatorStrategy strategy)
	{
		cache = new ConcurrentReferenceHashMap();
		this.strategy = ((InstantiatorStrategy) (strategy == null ? ((InstantiatorStrategy) (new StdInstantiatorStrategy())) : strategy));
		if (SpringProperties.getFlag("spring.objenesis.ignore"))
			worthTrying = Boolean.FALSE;
	}

	public boolean isWorthTrying()
	{
		return worthTrying != Boolean.FALSE;
	}

	public Object newInstance(Class clazz, boolean useCache)
	{
		if (!useCache)
			return newInstantiatorOf(clazz).newInstance();
		else
			return getInstantiatorOf(clazz).newInstance();
	}

	public Object newInstance(Class clazz)
	{
		return getInstantiatorOf(clazz).newInstance();
	}

	public ObjectInstantiator getInstantiatorOf(Class clazz)
	{
		ObjectInstantiator instantiator = (ObjectInstantiator)cache.get(clazz);
		if (instantiator == null)
		{
			ObjectInstantiator newInstantiator = newInstantiatorOf(clazz);
			instantiator = (ObjectInstantiator)cache.putIfAbsent(clazz, newInstantiator);
			if (instantiator == null)
				instantiator = newInstantiator;
		}
		return instantiator;
	}

	protected ObjectInstantiator newInstantiatorOf(Class clazz)
	{
		Boolean currentWorthTrying = worthTrying;
		ObjectInstantiator instantiator;
		instantiator = strategy.newInstantiatorOf(clazz);
		if (currentWorthTrying == null)
			worthTrying = Boolean.TRUE;
		return instantiator;
		ObjenesisException ex;
		ex;
		if (currentWorthTrying == null)
		{
			Throwable cause = ex.getCause();
			if ((cause instanceof ClassNotFoundException) || (cause instanceof IllegalAccessException))
				worthTrying = Boolean.FALSE;
		}
		throw ex;
		NoClassDefFoundError err;
		err;
		if (currentWorthTrying == null)
			worthTrying = Boolean.FALSE;
		throw new ObjenesisException(err);
	}
}
