// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Proxy.java

package org.springframework.cglib.proxy;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.springframework.cglib.core.CodeGenerationException;

// Referenced classes of package org.springframework.cglib.proxy:
//			Callback, Enhancer, InvocationHandler, NoOp, 
//			CallbackFilter

public class Proxy
	implements Serializable
{
	private static class ProxyImpl extends Proxy
	{

		protected ProxyImpl(InvocationHandler h)
		{
			super(h);
		}
	}


	protected InvocationHandler h;
	private static final CallbackFilter BAD_OBJECT_METHOD_FILTER = new CallbackFilter() {

		public int accept(Method method)
		{
			if (method.getDeclaringClass().getName().equals("java.lang.Object"))
			{
				String name = method.getName();
				if (!name.equals("hashCode") && !name.equals("equals") && !name.equals("toString"))
					return 1;
			}
			return 0;
		}

	};

	protected Proxy(InvocationHandler h)
	{
		Enhancer.registerCallbacks(getClass(), new Callback[] {
			h, null
		});
		this.h = h;
	}

	public static InvocationHandler getInvocationHandler(Object proxy)
	{
		if (!(proxy instanceof ProxyImpl))
			throw new IllegalArgumentException("Object is not a proxy");
		else
			return ((Proxy)proxy).h;
	}

	public static Class getProxyClass(ClassLoader loader, Class interfaces[])
	{
		Enhancer e = new Enhancer();
		e.setSuperclass(org/springframework/cglib/proxy/Proxy$ProxyImpl);
		e.setInterfaces(interfaces);
		e.setCallbackTypes(new Class[] {
			org/springframework/cglib/proxy/InvocationHandler, org/springframework/cglib/proxy/NoOp
		});
		e.setCallbackFilter(BAD_OBJECT_METHOD_FILTER);
		e.setUseFactory(false);
		return e.createClass();
	}

	public static boolean isProxyClass(Class cl)
	{
		return cl.getSuperclass().equals(org/springframework/cglib/proxy/Proxy$ProxyImpl);
	}

	public static Object newProxyInstance(ClassLoader loader, Class interfaces[], InvocationHandler h)
	{
		Class clazz = getProxyClass(loader, interfaces);
		return clazz.getConstructor(new Class[] {
			org/springframework/cglib/proxy/InvocationHandler
		}).newInstance(new Object[] {
			h
		});
		RuntimeException e;
		e;
		throw e;
		e;
		throw new CodeGenerationException(e);
	}

}
