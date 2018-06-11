// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ParameterNameDiscoverer.java

package org.springframework.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public interface ParameterNameDiscoverer
{

	public abstract String[] getParameterNames(Method method);

	public abstract String[] getParameterNames(Constructor constructor);
}
