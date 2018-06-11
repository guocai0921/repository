// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AbstractClassTransformer.java

package org.springframework.cglib.transform;

import org.springframework.asm.ClassVisitor;

// Referenced classes of package org.springframework.cglib.transform:
//			ClassTransformer

public abstract class AbstractClassTransformer extends ClassTransformer
{

	protected AbstractClassTransformer()
	{
		super(0x50000);
	}

	public void setTarget(ClassVisitor target)
	{
		cv = target;
	}
}
