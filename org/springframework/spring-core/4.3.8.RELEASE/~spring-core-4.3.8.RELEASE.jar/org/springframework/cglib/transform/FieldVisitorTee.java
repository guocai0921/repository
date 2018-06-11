// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   FieldVisitorTee.java

package org.springframework.cglib.transform;

import org.springframework.asm.*;

// Referenced classes of package org.springframework.cglib.transform:
//			AnnotationVisitorTee

public class FieldVisitorTee extends FieldVisitor
{

	private FieldVisitor fv1;
	private FieldVisitor fv2;

	public FieldVisitorTee(FieldVisitor fv1, FieldVisitor fv2)
	{
		super(0x50000);
		this.fv1 = fv1;
		this.fv2 = fv2;
	}

	public AnnotationVisitor visitAnnotation(String desc, boolean visible)
	{
		return AnnotationVisitorTee.getInstance(fv1.visitAnnotation(desc, visible), fv2.visitAnnotation(desc, visible));
	}

	public void visitAttribute(Attribute attr)
	{
		fv1.visitAttribute(attr);
		fv2.visitAttribute(attr);
	}

	public void visitEnd()
	{
		fv1.visitEnd();
		fv2.visitEnd();
	}

	public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible)
	{
		return AnnotationVisitorTee.getInstance(fv1.visitTypeAnnotation(typeRef, typePath, desc, visible), fv2.visitTypeAnnotation(typeRef, typePath, desc, visible));
	}
}
