// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ClassTransformerChain.java

package org.springframework.cglib.transform;

import org.springframework.asm.ClassVisitor;
import org.springframework.asm.MethodVisitor;

// Referenced classes of package org.springframework.cglib.transform:
//			AbstractClassTransformer, ClassTransformer

public class ClassTransformerChain extends AbstractClassTransformer
{

	private ClassTransformer chain[];

	public ClassTransformerChain(ClassTransformer chain[])
	{
		this.chain = (ClassTransformer[])(ClassTransformer[])chain.clone();
	}

	public void setTarget(ClassVisitor v)
	{
		super.setTarget(chain[0]);
		ClassVisitor next = v;
		for (int i = chain.length - 1; i >= 0; i--)
		{
			chain[i].setTarget(next);
			next = chain[i];
		}

	}

	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String exceptions[])
	{
		return cv.visitMethod(access, name, desc, signature, exceptions);
	}

	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("ClassTransformerChain{");
		for (int i = 0; i < chain.length; i++)
		{
			if (i > 0)
				sb.append(", ");
			sb.append(chain[i].toString());
		}

		sb.append("}");
		return sb.toString();
	}
}
