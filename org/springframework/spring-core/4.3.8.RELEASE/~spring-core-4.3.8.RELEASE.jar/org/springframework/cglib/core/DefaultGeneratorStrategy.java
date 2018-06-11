// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DefaultGeneratorStrategy.java

package org.springframework.cglib.core;

import org.springframework.asm.ClassWriter;

// Referenced classes of package org.springframework.cglib.core:
//			GeneratorStrategy, ClassGenerator, DebuggingClassWriter

public class DefaultGeneratorStrategy
	implements GeneratorStrategy
{

	public static final DefaultGeneratorStrategy INSTANCE = new DefaultGeneratorStrategy();

	public DefaultGeneratorStrategy()
	{
	}

	public byte[] generate(ClassGenerator cg)
		throws Exception
	{
		DebuggingClassWriter cw = getClassVisitor();
		transform(cg).generateClass(cw);
		return transform(cw.toByteArray());
	}

	protected DebuggingClassWriter getClassVisitor()
		throws Exception
	{
		return new DebuggingClassWriter(2);
	}

	protected final ClassWriter getClassWriter()
	{
		throw new UnsupportedOperationException("You are calling getClassWriter, which no longer exists in this cglib version.");
	}

	protected byte[] transform(byte b[])
		throws Exception
	{
		return b;
	}

	protected ClassGenerator transform(ClassGenerator cg)
		throws Exception
	{
		return cg;
	}

}
