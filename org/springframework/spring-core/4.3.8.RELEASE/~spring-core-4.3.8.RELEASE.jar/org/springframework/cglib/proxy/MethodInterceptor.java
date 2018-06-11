// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   MethodInterceptor.java

package org.springframework.cglib.proxy;

import java.lang.reflect.Method;

// Referenced classes of package org.springframework.cglib.proxy:
//			Callback, MethodProxy

public interface MethodInterceptor
	extends Callback
{

	public abstract Object intercept(Object obj, Method method, Object aobj[], MethodProxy methodproxy)
		throws Throwable;
}
