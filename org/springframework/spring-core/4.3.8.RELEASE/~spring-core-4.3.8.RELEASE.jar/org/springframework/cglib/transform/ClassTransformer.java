// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ClassTransformer.java

package org.springframework.cglib.transform;

import org.springframework.asm.ClassVisitor;

public abstract class ClassTransformer extends ClassVisitor
{

	public ClassTransformer()
	{
		super(0x50000);
	}

	public ClassTransformer(int opcode)
	{
		super(opcode);
	}

	public abstract void setTarget(ClassVisitor classvisitor);
}
