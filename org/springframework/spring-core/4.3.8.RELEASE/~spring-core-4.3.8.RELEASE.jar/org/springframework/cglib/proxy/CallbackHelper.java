// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CallbackHelper.java

package org.springframework.cglib.proxy;

import java.lang.reflect.Method;
import java.util.*;
import org.springframework.cglib.core.ReflectUtils;

// Referenced classes of package org.springframework.cglib.proxy:
//			CallbackFilter, Enhancer, Callback

public abstract class CallbackHelper
	implements CallbackFilter
{

	private Map methodMap;
	private List callbacks;

	public CallbackHelper(Class superclass, Class interfaces[])
	{
		methodMap = new HashMap();
		callbacks = new ArrayList();
		List methods = new ArrayList();
		Enhancer.getMethods(superclass, interfaces, methods);
		Map indexes = new HashMap();
		int i = 0;
		for (int size = methods.size(); i < size; i++)
		{
			Method method = (Method)methods.get(i);
			Object callback = getCallback(method);
			if (callback == null)
				throw new IllegalStateException("getCallback cannot return null");
			boolean isCallback = callback instanceof Callback;
			if (!isCallback && !(callback instanceof Class))
				throw new IllegalStateException("getCallback must return a Callback or a Class");
			if (i > 0 && (callbacks.get(i - 1) instanceof Callback) ^ isCallback)
				throw new IllegalStateException("getCallback must return a Callback or a Class consistently for every Method");
			Integer index = (Integer)indexes.get(callback);
			if (index == null)
			{
				index = new Integer(callbacks.size());
				indexes.put(callback, index);
			}
			methodMap.put(method, index);
			callbacks.add(callback);
		}

	}

	protected abstract Object getCallback(Method method);

	public Callback[] getCallbacks()
	{
		if (callbacks.size() == 0)
			return new Callback[0];
		if (callbacks.get(0) instanceof Callback)
			return (Callback[])(Callback[])callbacks.toArray(new Callback[callbacks.size()]);
		else
			throw new IllegalStateException("getCallback returned classes, not callbacks; call getCallbackTypes instead");
	}

	public Class[] getCallbackTypes()
	{
		if (callbacks.size() == 0)
			return new Class[0];
		if (callbacks.get(0) instanceof Callback)
			return ReflectUtils.getClasses(getCallbacks());
		else
			return (Class[])(Class[])callbacks.toArray(new Class[callbacks.size()]);
	}

	public int accept(Method method)
	{
		return ((Integer)methodMap.get(method)).intValue();
	}

	public int hashCode()
	{
		return methodMap.hashCode();
	}

	public boolean equals(Object o)
	{
		if (o == null)
			return false;
		if (!(o instanceof CallbackHelper))
			return false;
		else
			return methodMap.equals(((CallbackHelper)o).methodMap);
	}
}
