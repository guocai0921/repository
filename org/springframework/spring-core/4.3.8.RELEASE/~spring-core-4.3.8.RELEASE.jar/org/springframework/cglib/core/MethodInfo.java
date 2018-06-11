// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   MethodInfo.java

package org.springframework.cglib.core;

import org.springframework.asm.Type;

// Referenced classes of package org.springframework.cglib.core:
//			Signature, ClassInfo

public abstract class MethodInfo
{

	protected MethodInfo()
	{
	}

	public abstract ClassInfo getClassInfo();

	public abstract int getModifiers();

	public abstract Signature getSignature();

	public abstract Type[] getExceptionTypes();

	public boolean equals(Object o)
	{
		if (o == null)
			return false;
		if (!(o instanceof MethodInfo))
			return false;
		else
			return getSignature().equals(((MethodInfo)o).getSignature());
	}

	public int hashCode()
	{
		return getSignature().hashCode();
	}

	public String toString()
	{
		return getSignature().toString();
	}
}
