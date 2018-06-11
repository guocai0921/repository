// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AddInitTransformer.java

package org.springframework.cglib.transform.impl;

import java.lang.reflect.Method;
import org.springframework.asm.Type;
import org.springframework.cglib.core.*;
import org.springframework.cglib.transform.ClassEmitterTransformer;

public class AddInitTransformer extends ClassEmitterTransformer
{

	private MethodInfo info;

	public AddInitTransformer(Method method)
	{
		info = ReflectUtils.getMethodInfo(method);
		Type types[] = info.getSignature().getArgumentTypes();
		if (types.length != 1 || !types[0].equals(Constants.TYPE_OBJECT) || !info.getSignature().getReturnType().equals(Type.VOID_TYPE))
			throw new IllegalArgumentException((new StringBuilder()).append(method).append(" illegal signature").toString());
		else
			return;
	}

	public CodeEmitter begin_method(int access, Signature sig, Type exceptions[])
	{
		CodeEmitter emitter = super.begin_method(access, sig, exceptions);
		if (sig.getName().equals("<init>"))
			return new CodeEmitter(emitter) {

				final AddInitTransformer this$0;

				public void visitInsn(int opcode)
				{
					if (opcode == 177)
					{
						load_this();
						invoke(info);
					}
					super.visitInsn(opcode);
				}

			
			{
				this.this$0 = AddInitTransformer.this;
				super(wrap);
			}
			};
		else
			return emitter;
	}

}
