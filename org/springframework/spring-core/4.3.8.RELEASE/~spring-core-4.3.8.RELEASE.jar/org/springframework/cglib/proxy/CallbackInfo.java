// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CallbackInfo.java

package org.springframework.cglib.proxy;

import org.springframework.asm.Type;

// Referenced classes of package org.springframework.cglib.proxy:
//			CallbackGenerator, NoOp, NoOpGenerator, MethodInterceptor, 
//			MethodInterceptorGenerator, InvocationHandler, InvocationHandlerGenerator, LazyLoader, 
//			LazyLoaderGenerator, Dispatcher, DispatcherGenerator, FixedValue, 
//			FixedValueGenerator, ProxyRefDispatcher, Callback

class CallbackInfo
{

	private Class cls;
	private CallbackGenerator generator;
	private Type type;
	private static final CallbackInfo CALLBACKS[];

	public static Type[] determineTypes(Class callbackTypes[])
	{
		return determineTypes(callbackTypes, true);
	}

	public static Type[] determineTypes(Class callbackTypes[], boolean checkAll)
	{
		Type types[] = new Type[callbackTypes.length];
		for (int i = 0; i < types.length; i++)
			types[i] = determineType(callbackTypes[i], checkAll);

		return types;
	}

	public static Type[] determineTypes(Callback callbacks[])
	{
		return determineTypes(callbacks, true);
	}

	public static Type[] determineTypes(Callback callbacks[], boolean checkAll)
	{
		Type types[] = new Type[callbacks.length];
		for (int i = 0; i < types.length; i++)
			types[i] = determineType(callbacks[i], checkAll);

		return types;
	}

	public static CallbackGenerator[] getGenerators(Type callbackTypes[])
	{
		CallbackGenerator generators[] = new CallbackGenerator[callbackTypes.length];
		for (int i = 0; i < generators.length; i++)
			generators[i] = getGenerator(callbackTypes[i]);

		return generators;
	}

	private CallbackInfo(Class cls, CallbackGenerator generator)
	{
		this.cls = cls;
		this.generator = generator;
		type = Type.getType(cls);
	}

	private static Type determineType(Callback callback, boolean checkAll)
	{
		if (callback == null)
			throw new IllegalStateException("Callback is null");
		else
			return determineType(callback.getClass(), checkAll);
	}

	private static Type determineType(Class callbackType, boolean checkAll)
	{
		Class cur = null;
		Type type = null;
		for (int i = 0; i < CALLBACKS.length; i++)
		{
			CallbackInfo info = CALLBACKS[i];
			if (!info.cls.isAssignableFrom(callbackType))
				continue;
			if (cur != null)
				throw new IllegalStateException((new StringBuilder()).append("Callback implements both ").append(cur).append(" and ").append(info.cls).toString());
			cur = info.cls;
			type = info.type;
			if (!checkAll)
				break;
		}

		if (cur == null)
			throw new IllegalStateException((new StringBuilder()).append("Unknown callback type ").append(callbackType).toString());
		else
			return type;
	}

	private static CallbackGenerator getGenerator(Type callbackType)
	{
		for (int i = 0; i < CALLBACKS.length; i++)
		{
			CallbackInfo info = CALLBACKS[i];
			if (info.type.equals(callbackType))
				return info.generator;
		}

		throw new IllegalStateException((new StringBuilder()).append("Unknown callback type ").append(callbackType).toString());
	}

	static 
	{
		CALLBACKS = (new CallbackInfo[] {
			new CallbackInfo(org/springframework/cglib/proxy/NoOp, NoOpGenerator.INSTANCE), new CallbackInfo(org/springframework/cglib/proxy/MethodInterceptor, MethodInterceptorGenerator.INSTANCE), new CallbackInfo(org/springframework/cglib/proxy/InvocationHandler, InvocationHandlerGenerator.INSTANCE), new CallbackInfo(org/springframework/cglib/proxy/LazyLoader, LazyLoaderGenerator.INSTANCE), new CallbackInfo(org/springframework/cglib/proxy/Dispatcher, DispatcherGenerator.INSTANCE), new CallbackInfo(org/springframework/cglib/proxy/FixedValue, FixedValueGenerator.INSTANCE), new CallbackInfo(org/springframework/cglib/proxy/ProxyRefDispatcher, DispatcherGenerator.PROXY_REF_INSTANCE)
		});
	}
}
