// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CallbackFilter.java

package org.springframework.cglib.proxy;

import java.lang.reflect.Method;

public interface CallbackFilter
{

	public abstract int accept(Method method);

	public abstract boolean equals(Object obj);
}
