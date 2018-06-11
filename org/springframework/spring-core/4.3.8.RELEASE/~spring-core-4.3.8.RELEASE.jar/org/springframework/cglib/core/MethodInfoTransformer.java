// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   MethodInfoTransformer.java

package org.springframework.cglib.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

// Referenced classes of package org.springframework.cglib.core:
//			Transformer, ReflectUtils

public class MethodInfoTransformer
	implements Transformer
{

	private static final MethodInfoTransformer INSTANCE = new MethodInfoTransformer();

	public MethodInfoTransformer()
	{
	}

	public static MethodInfoTransformer getInstance()
	{
		return INSTANCE;
	}

	public Object transform(Object value)
	{
		if (value instanceof Method)
			return ReflectUtils.getMethodInfo((Method)value);
		if (value instanceof Constructor)
			return ReflectUtils.getMethodInfo((Constructor)value);
		else
			throw new IllegalArgumentException((new StringBuilder()).append("cannot get method info for ").append(value).toString());
	}

}
