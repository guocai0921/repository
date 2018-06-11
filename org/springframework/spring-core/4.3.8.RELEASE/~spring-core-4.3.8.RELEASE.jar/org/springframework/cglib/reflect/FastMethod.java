// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   FastMethod.java

package org.springframework.cglib.reflect;

import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.springframework.asm.Type;
import org.springframework.cglib.core.Signature;

// Referenced classes of package org.springframework.cglib.reflect:
//			FastMember, FastClass

public class FastMethod extends FastMember
{

	FastMethod(FastClass fc, Method method)
	{
		super(fc, method, helper(fc, method));
	}

	private static int helper(FastClass fc, Method method)
	{
		int index = fc.getIndex(new Signature(method.getName(), Type.getMethodDescriptor(method)));
		if (index < 0)
		{
			Class types[] = method.getParameterTypes();
			System.err.println((new StringBuilder()).append("hash=").append(method.getName().hashCode()).append(" size=").append(types.length).toString());
			for (int i = 0; i < types.length; i++)
				System.err.println((new StringBuilder()).append("  types[").append(i).append("]=").append(types[i].getName()).toString());

			throw new IllegalArgumentException((new StringBuilder()).append("Cannot find method ").append(method).toString());
		} else
		{
			return index;
		}
	}

	public Class getReturnType()
	{
		return ((Method)member).getReturnType();
	}

	public Class[] getParameterTypes()
	{
		return ((Method)member).getParameterTypes();
	}

	public Class[] getExceptionTypes()
	{
		return ((Method)member).getExceptionTypes();
	}

	public Object invoke(Object obj, Object args[])
		throws InvocationTargetException
	{
		return fc.invoke(index, obj, args);
	}

	public Method getJavaMethod()
	{
		return (Method)member;
	}
}
