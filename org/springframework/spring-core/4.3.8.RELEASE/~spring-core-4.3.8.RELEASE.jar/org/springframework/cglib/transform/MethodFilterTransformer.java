// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   MethodFilterTransformer.java

package org.springframework.cglib.transform;

import org.springframework.asm.ClassVisitor;
import org.springframework.asm.MethodVisitor;

// Referenced classes of package org.springframework.cglib.transform:
//			AbstractClassTransformer, MethodFilter, ClassTransformer

public class MethodFilterTransformer extends AbstractClassTransformer
{

	private MethodFilter filter;
	private ClassTransformer pass;
	private ClassVisitor direct;

	public MethodFilterTransformer(MethodFilter filter, ClassTransformer pass)
	{
		this.filter = filter;
		this.pass = pass;
		super.setTarget(pass);
	}

	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String exceptions[])
	{
		return ((ClassVisitor) (filter.accept(access, name, desc, signature, exceptions) ? pass : direct)).visitMethod(access, name, desc, signature, exceptions);
	}

	public void setTarget(ClassVisitor target)
	{
		pass.setTarget(target);
		direct = target;
	}
}
