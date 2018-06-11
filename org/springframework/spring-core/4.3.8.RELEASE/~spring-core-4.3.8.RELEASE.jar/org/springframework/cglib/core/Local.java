// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Local.java

package org.springframework.cglib.core;

import org.springframework.asm.Type;

public class Local
{

	private Type type;
	private int index;

	public Local(int index, Type type)
	{
		this.type = type;
		this.index = index;
	}

	public int getIndex()
	{
		return index;
	}

	public Type getType()
	{
		return type;
	}
}
