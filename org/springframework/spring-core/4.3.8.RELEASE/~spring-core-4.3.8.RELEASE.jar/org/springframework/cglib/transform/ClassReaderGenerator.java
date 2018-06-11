// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ClassReaderGenerator.java

package org.springframework.cglib.transform;

import org.springframework.asm.*;
import org.springframework.cglib.core.ClassGenerator;

public class ClassReaderGenerator
	implements ClassGenerator
{

	private final ClassReader r;
	private final Attribute attrs[];
	private final int flags;

	public ClassReaderGenerator(ClassReader r, int flags)
	{
		this(r, null, flags);
	}

	public ClassReaderGenerator(ClassReader r, Attribute attrs[], int flags)
	{
		this.r = r;
		this.attrs = attrs == null ? new Attribute[0] : attrs;
		this.flags = flags;
	}

	public void generateClass(ClassVisitor v)
	{
		r.accept(v, attrs, flags);
	}
}
