// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ClassVisitor.java

package org.springframework.asm;


// Referenced classes of package org.springframework.asm:
//			Opcodes, AnnotationVisitor, TypePath, Attribute, 
//			FieldVisitor, MethodVisitor

public abstract class ClassVisitor
{

	protected final int api;
	protected ClassVisitor cv;

	public ClassVisitor(int api)
	{
		this(api, null);
	}

	public ClassVisitor(int api, ClassVisitor cv)
	{
		if (api != 0x40000 && api != 0x50000)
		{
			throw new IllegalArgumentException();
		} else
		{
			this.api = api;
			this.cv = cv;
			return;
		}
	}

	public void visit(int version, int access, String name, String signature, String superName, String interfaces[])
	{
		if (cv != null)
			cv.visit(version, access, name, signature, superName, interfaces);
	}

	public void visitSource(String source, String debug)
	{
		if (cv != null)
			cv.visitSource(source, debug);
	}

	public void visitOuterClass(String owner, String name, String desc)
	{
		if (cv != null)
			cv.visitOuterClass(owner, name, desc);
	}

	public AnnotationVisitor visitAnnotation(String desc, boolean visible)
	{
		if (cv != null)
			return cv.visitAnnotation(desc, visible);
		else
			return null;
	}

	public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible)
	{
		if (cv != null)
			return cv.visitTypeAnnotation(typeRef, typePath, desc, visible);
		else
			return null;
	}

	public void visitAttribute(Attribute attr)
	{
		if (cv != null)
			cv.visitAttribute(attr);
	}

	public void visitInnerClass(String name, String outerName, String innerName, int access)
	{
		if (cv != null)
			cv.visitInnerClass(name, outerName, innerName, access);
	}

	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value)
	{
		if (cv != null)
			return cv.visitField(access, name, desc, signature, value);
		else
			return null;
	}

	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String exceptions[])
	{
		if (cv != null)
			return cv.visitMethod(access, name, desc, signature, exceptions);
		else
			return null;
	}

	public void visitEnd()
	{
		if (cv != null)
			cv.visitEnd();
	}
}
