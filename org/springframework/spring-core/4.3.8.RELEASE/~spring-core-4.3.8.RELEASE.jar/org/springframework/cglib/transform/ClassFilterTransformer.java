// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ClassFilterTransformer.java

package org.springframework.cglib.transform;


// Referenced classes of package org.springframework.cglib.transform:
//			AbstractClassFilterTransformer, ClassFilter, ClassTransformer

public class ClassFilterTransformer extends AbstractClassFilterTransformer
{

	private ClassFilter filter;

	public ClassFilterTransformer(ClassFilter filter, ClassTransformer pass)
	{
		super(pass);
		this.filter = filter;
	}

	protected boolean accept(int version, int access, String name, String signature, String superName, String interfaces[])
	{
		return filter.accept(name.replace('/', '.'));
	}
}
