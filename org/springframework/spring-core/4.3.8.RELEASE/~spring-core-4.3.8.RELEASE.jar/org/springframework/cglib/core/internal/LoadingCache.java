// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   LoadingCache.java

package org.springframework.cglib.core.internal;

import java.util.concurrent.*;

// Referenced classes of package org.springframework.cglib.core.internal:
//			Function

public class LoadingCache
{

	protected final ConcurrentMap map = new ConcurrentHashMap();
	protected final Function loader;
	protected final Function keyMapper;
	public static final Function IDENTITY = new Function() {

		public Object apply(Object key)
		{
			return key;
		}

	};

	public LoadingCache(Function keyMapper, Function loader)
	{
		this.keyMapper = keyMapper;
		this.loader = loader;
	}

	public static Function identity()
	{
		return IDENTITY;
	}

	public Object get(Object key)
	{
		Object cacheKey = keyMapper.apply(key);
		Object v = map.get(cacheKey);
		if (v != null && !(v instanceof FutureTask))
			return v;
		else
			return createEntry(key, cacheKey, v);
	}

	protected Object createEntry(final Object key, Object cacheKey, Object v)
	{
		boolean creator = false;
		FutureTask task;
		if (v != null)
		{
			task = (FutureTask)v;
		} else
		{
			task = new FutureTask(new Callable() {

				final Object val$key;
				final LoadingCache this$0;

				public Object call()
					throws Exception
				{
					return loader.apply(key);
				}

			
			{
				this.this$0 = LoadingCache.this;
				key = obj;
				super();
			}
			});
			Object prevTask = map.putIfAbsent(cacheKey, task);
			if (prevTask == null)
			{
				creator = true;
				task.run();
			} else
			if (prevTask instanceof FutureTask)
				task = (FutureTask)prevTask;
			else
				return prevTask;
		}
		Object result;
		try
		{
			result = task.get();
		}
		catch (InterruptedException e)
		{
			throw new IllegalStateException("Interrupted while loading cache item", e);
		}
		catch (ExecutionException e)
		{
			Throwable cause = e.getCause();
			if (cause instanceof RuntimeException)
				throw (RuntimeException)cause;
			else
				throw new IllegalStateException("Unable to load cache item", cause);
		}
		if (creator)
			map.put(cacheKey, result);
		return result;
	}

}
