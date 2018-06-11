// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   TransformingClassGenerator.java

package org.springframework.cglib.transform;

import org.springframework.asm.ClassVisitor;
import org.springframework.cglib.core.ClassGenerator;

// Referenced classes of package org.springframework.cglib.transform:
//			ClassTransformer

public class TransformingClassGenerator
	implements ClassGenerator
{

	private ClassGenerator gen;
	private ClassTransformer t;

	public TransformingClassGenerator(ClassGenerator gen, ClassTransformer t)
	{
		this.gen = gen;
		this.t = t;
	}

	public void generateClass(ClassVisitor v)
		throws Exception
	{
		t.setTarget(v);
		gen.generateClass(t);
	}
}
