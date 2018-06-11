// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   FastMember.java

package org.springframework.cglib.reflect;

import java.lang.reflect.Member;

// Referenced classes of package org.springframework.cglib.reflect:
//			FastClass

public abstract class FastMember
{

	protected FastClass fc;
	protected Member member;
	protected int index;

	protected FastMember(FastClass fc, Member member, int index)
	{
		this.fc = fc;
		this.member = member;
		this.index = index;
	}

	public abstract Class[] getParameterTypes();

	public abstract Class[] getExceptionTypes();

	public int getIndex()
	{
		return index;
	}

	public String getName()
	{
		return member.getName();
	}

	public Class getDeclaringClass()
	{
		return fc.getJavaClass();
	}

	public int getModifiers()
	{
		return member.getModifiers();
	}

	public String toString()
	{
		return member.toString();
	}

	public int hashCode()
	{
		return member.hashCode();
	}

	public boolean equals(Object o)
	{
		if (o == null || !(o instanceof FastMember))
			return false;
		else
			return member.equals(((FastMember)o).member);
	}
}
