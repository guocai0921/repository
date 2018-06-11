// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ClassInfo.java

package org.springframework.cglib.core;

import org.springframework.asm.Type;

public abstract class ClassInfo
{

	protected ClassInfo()
	{
	}

	public abstract Type getType();

	public abstract Type getSuperType();

	public abstract Type[] getInterfaces();

	public abstract int getModifiers();

	public boolean equals(Object o)
	{
		if (o == null)
			return false;
		if (!(o instanceof ClassInfo))
			return false;
		else
			return getType().equals(((ClassInfo)o).getType());
	}

	public int hashCode()
	{
		return getType().hashCode();
	}

	public String toString()
	{
		return getType().getClassName();
	}
}
