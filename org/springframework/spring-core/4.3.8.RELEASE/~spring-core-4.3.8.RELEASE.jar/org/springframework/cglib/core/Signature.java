// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Signature.java

package org.springframework.cglib.core;

import org.springframework.asm.Type;

public class Signature
{

	private String name;
	private String desc;

	public Signature(String name, String desc)
	{
		if (name.indexOf('(') >= 0)
		{
			throw new IllegalArgumentException((new StringBuilder()).append("Name '").append(name).append("' is invalid").toString());
		} else
		{
			this.name = name;
			this.desc = desc;
			return;
		}
	}

	public Signature(String name, Type returnType, Type argumentTypes[])
	{
		this(name, Type.getMethodDescriptor(returnType, argumentTypes));
	}

	public String getName()
	{
		return name;
	}

	public String getDescriptor()
	{
		return desc;
	}

	public Type getReturnType()
	{
		return Type.getReturnType(desc);
	}

	public Type[] getArgumentTypes()
	{
		return Type.getArgumentTypes(desc);
	}

	public String toString()
	{
		return (new StringBuilder()).append(name).append(desc).toString();
	}

	public boolean equals(Object o)
	{
		if (o == null)
			return false;
		if (!(o instanceof Signature))
		{
			return false;
		} else
		{
			Signature other = (Signature)o;
			return name.equals(other.name) && desc.equals(other.desc);
		}
	}

	public int hashCode()
	{
		return name.hashCode() ^ desc.hashCode();
	}
}
