// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AddStaticInitTransformer.java

package org.springframework.cglib.transform.impl;

import java.lang.reflect.Method;
import org.springframework.asm.Type;
import org.springframework.cglib.core.*;
import org.springframework.cglib.transform.ClassEmitterTransformer;

public class AddStaticInitTransformer extends ClassEmitterTransformer
{

	private MethodInfo info;

	public AddStaticInitTransformer(Method classInit)
	{
		info = ReflectUtils.getMethodInfo(classInit);
		if (!TypeUtils.isStatic(info.getModifiers()))
			throw new IllegalArgumentException((new StringBuilder()).append(classInit).append(" is not static").toString());
		Type types[] = info.getSignature().getArgumentTypes();
		if (types.length != 1 || !types[0].equals(Constants.TYPE_CLASS) || !info.getSignature().getReturnType().equals(Type.VOID_TYPE))
			throw new IllegalArgumentException((new StringBuilder()).append(classInit).append(" illegal signature").toString());
		else
			return;
	}

	protected void init()
	{
		if (!TypeUtils.isInterface(getAccess()))
		{
			CodeEmitter e = getStaticHook();
			EmitUtils.load_class_this(e);
			e.invoke(info);
		}
	}
}
