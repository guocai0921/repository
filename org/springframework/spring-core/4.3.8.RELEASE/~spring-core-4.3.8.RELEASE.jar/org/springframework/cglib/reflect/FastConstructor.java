// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   FastConstructor.java

package org.springframework.cglib.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

// Referenced classes of package org.springframework.cglib.reflect:
//			FastMember, FastClass

public class FastConstructor extends FastMember
{

	FastConstructor(FastClass fc, Constructor constructor)
	{
		super(fc, constructor, fc.getIndex(constructor.getParameterTypes()));
	}

	public Class[] getParameterTypes()
	{
		return ((Constructor)member).getParameterTypes();
	}

	public Class[] getExceptionTypes()
	{
		return ((Constructor)member).getExceptionTypes();
	}

	public Object newInstance()
		throws InvocationTargetException
	{
		return fc.newInstance(index, null);
	}

	public Object newInstance(Object args[])
		throws InvocationTargetException
	{
		return fc.newInstance(index, args);
	}

	public Constructor getJavaConstructor()
	{
		return (Constructor)member;
	}
}
