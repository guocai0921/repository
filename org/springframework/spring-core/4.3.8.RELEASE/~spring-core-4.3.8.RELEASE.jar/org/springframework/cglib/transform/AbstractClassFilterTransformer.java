// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AbstractClassFilterTransformer.java

package org.springframework.cglib.transform;

import org.springframework.asm.*;

// Referenced classes of package org.springframework.cglib.transform:
//			AbstractClassTransformer, ClassTransformer

public abstract class AbstractClassFilterTransformer extends AbstractClassTransformer
{

	private ClassTransformer pass;
	private ClassVisitor target;

	public void setTarget(ClassVisitor target)
	{
		super.setTarget(target);
		pass.setTarget(target);
	}

	protected AbstractClassFilterTransformer(ClassTransformer pass)
	{
		this.pass = pass;
	}

	protected abstract boolean accept(int i, int j, String s, String s1, String s2, String as[]);

	public void visit(int version, int access, String name, String signature, String superName, String interfaces[])
	{
		target = ((ClassVisitor) (accept(version, access, name, signature, superName, interfaces) ? ((ClassVisitor) (pass)) : cv));
		target.visit(version, access, name, signature, superName, interfaces);
	}

	public void visitSource(String source, String debug)
	{
		target.visitSource(source, debug);
	}

	public void visitOuterClass(String owner, String name, String desc)
	{
		target.visitOuterClass(owner, name, desc);
	}

	public AnnotationVisitor visitAnnotation(String desc, boolean visible)
	{
		return target.visitAnnotation(desc, visible);
	}

	public void visitAttribute(Attribute attr)
	{
		target.visitAttribute(attr);
	}

	public void visitInnerClass(String name, String outerName, String innerName, int access)
	{
		target.visitInnerClass(name, outerName, innerName, access);
	}

	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value)
	{
		return target.visitField(access, name, desc, signature, value);
	}

	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String exceptions[])
	{
		return target.visitMethod(access, name, desc, signature, exceptions);
	}

	public void visitEnd()
	{
		target.visitEnd();
		target = null;
	}
}
