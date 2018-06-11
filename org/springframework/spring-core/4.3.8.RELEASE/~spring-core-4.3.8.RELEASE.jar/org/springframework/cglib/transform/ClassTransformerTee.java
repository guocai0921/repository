// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ClassTransformerTee.java

package org.springframework.cglib.transform;

import org.springframework.asm.ClassVisitor;

// Referenced classes of package org.springframework.cglib.transform:
//			ClassTransformer, ClassVisitorTee

public class ClassTransformerTee extends ClassTransformer
{

	private ClassVisitor branch;

	public ClassTransformerTee(ClassVisitor branch)
	{
		super(0x50000);
		this.branch = branch;
	}

	public void setTarget(ClassVisitor target)
	{
		cv = new ClassVisitorTee(branch, target);
	}
}
