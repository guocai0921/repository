// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AnnotationVisitor.java

package org.springframework.asm;


// Referenced classes of package org.springframework.asm:
//			Opcodes

public abstract class AnnotationVisitor
{

	protected final int api;
	protected AnnotationVisitor av;

	public AnnotationVisitor(int api)
	{
		this(api, null);
	}

	public AnnotationVisitor(int api, AnnotationVisitor av)
	{
		if (api != 0x40000 && api != 0x50000)
		{
			throw new IllegalArgumentException();
		} else
		{
			this.api = api;
			this.av = av;
			return;
		}
	}

	public void visit(String name, Object value)
	{
		if (av != null)
			av.visit(name, value);
	}

	public void visitEnum(String name, String desc, String value)
	{
		if (av != null)
			av.visitEnum(name, desc, value);
	}

	public AnnotationVisitor visitAnnotation(String name, String desc)
	{
		if (av != null)
			return av.visitAnnotation(name, desc);
		else
			return null;
	}

	public AnnotationVisitor visitArray(String name)
	{
		if (av != null)
			return av.visitArray(name);
		else
			return null;
	}

	public void visitEnd()
	{
		if (av != null)
			av.visitEnd();
	}
}
