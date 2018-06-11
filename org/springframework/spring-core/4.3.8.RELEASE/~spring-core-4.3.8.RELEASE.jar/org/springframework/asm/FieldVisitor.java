// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   FieldVisitor.java

package org.springframework.asm;


// Referenced classes of package org.springframework.asm:
//			Opcodes, AnnotationVisitor, TypePath, Attribute

public abstract class FieldVisitor
{

	protected final int api;
	protected FieldVisitor fv;

	public FieldVisitor(int api)
	{
		this(api, null);
	}

	public FieldVisitor(int api, FieldVisitor fv)
	{
		if (api != 0x40000 && api != 0x50000)
		{
			throw new IllegalArgumentException();
		} else
		{
			this.api = api;
			this.fv = fv;
			return;
		}
	}

	public AnnotationVisitor visitAnnotation(String desc, boolean visible)
	{
		if (fv != null)
			return fv.visitAnnotation(desc, visible);
		else
			return null;
	}

	public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible)
	{
		if (fv != null)
			return fv.visitTypeAnnotation(typeRef, typePath, desc, visible);
		else
			return null;
	}

	public void visitAttribute(Attribute attr)
	{
		if (fv != null)
			fv.visitAttribute(attr);
	}

	public void visitEnd()
	{
		if (fv != null)
			fv.visitEnd();
	}
}
