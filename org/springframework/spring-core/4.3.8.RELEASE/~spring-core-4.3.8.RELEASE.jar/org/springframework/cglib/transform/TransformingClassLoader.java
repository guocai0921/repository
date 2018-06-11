// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   TransformingClassLoader.java

package org.springframework.cglib.transform;

import org.springframework.asm.ClassReader;
import org.springframework.cglib.core.ClassGenerator;

// Referenced classes of package org.springframework.cglib.transform:
//			AbstractClassLoader, ClassTransformerFactory, TransformingClassGenerator, ClassFilter

public class TransformingClassLoader extends AbstractClassLoader
{

	private ClassTransformerFactory t;

	public TransformingClassLoader(ClassLoader parent, ClassFilter filter, ClassTransformerFactory t)
	{
		super(parent, parent, filter);
		this.t = t;
	}

	protected ClassGenerator getGenerator(ClassReader r)
	{
		ClassTransformer t2 = t.newInstance();
		return new TransformingClassGenerator(super.getGenerator(r), t2);
	}
}
