// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   MixinEverythingEmitter.java

package org.springframework.cglib.proxy;

import java.lang.reflect.Method;
import java.util.*;
import org.springframework.asm.ClassVisitor;
import org.springframework.cglib.core.*;

// Referenced classes of package org.springframework.cglib.proxy:
//			MixinEmitter

class MixinEverythingEmitter extends MixinEmitter
{

	public MixinEverythingEmitter(ClassVisitor v, String className, Class classes[])
	{
		super(v, className, classes, null);
	}

	protected Class[] getInterfaces(Class classes[])
	{
		List list = new ArrayList();
		for (int i = 0; i < classes.length; i++)
			ReflectUtils.addAllInterfaces(classes[i], list);

		return (Class[])(Class[])list.toArray(new Class[list.size()]);
	}

	protected Method[] getMethods(Class type)
	{
		List methods = new ArrayList(Arrays.asList(type.getMethods()));
		CollectionUtils.filter(methods, new RejectModifierPredicate(24));
		return (Method[])(Method[])methods.toArray(new Method[methods.size()]);
	}
}
