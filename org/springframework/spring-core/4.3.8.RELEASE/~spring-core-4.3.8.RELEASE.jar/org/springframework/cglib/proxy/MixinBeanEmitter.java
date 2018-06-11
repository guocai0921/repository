// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   MixinBeanEmitter.java

package org.springframework.cglib.proxy;

import java.lang.reflect.Method;
import org.springframework.asm.ClassVisitor;
import org.springframework.cglib.core.ReflectUtils;

// Referenced classes of package org.springframework.cglib.proxy:
//			MixinEmitter

class MixinBeanEmitter extends MixinEmitter
{

	public MixinBeanEmitter(ClassVisitor v, String className, Class classes[])
	{
		super(v, className, classes, null);
	}

	protected Class[] getInterfaces(Class classes[])
	{
		return null;
	}

	protected Method[] getMethods(Class type)
	{
		return ReflectUtils.getPropertyMethods(ReflectUtils.getBeanProperties(type), true, true);
	}
}
