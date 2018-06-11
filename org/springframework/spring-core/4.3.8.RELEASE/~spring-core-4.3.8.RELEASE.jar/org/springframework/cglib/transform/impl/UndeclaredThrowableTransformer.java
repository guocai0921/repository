// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   UndeclaredThrowableTransformer.java

package org.springframework.cglib.transform.impl;

import java.lang.reflect.Constructor;
import org.springframework.asm.Type;
import org.springframework.cglib.core.*;
import org.springframework.cglib.transform.ClassEmitterTransformer;

public class UndeclaredThrowableTransformer extends ClassEmitterTransformer
{

	private Type wrapper;

	public UndeclaredThrowableTransformer(Class wrapper)
	{
		this.wrapper = Type.getType(wrapper);
		boolean found = false;
		Constructor cstructs[] = wrapper.getConstructors();
		int i = 0;
		do
		{
			if (i >= cstructs.length)
				break;
			Class types[] = cstructs[i].getParameterTypes();
			if (types.length == 1 && types[0].equals(java/lang/Throwable))
			{
				found = true;
				break;
			}
			i++;
		} while (true);
		if (!found)
			throw new IllegalArgumentException((new StringBuilder()).append(wrapper).append(" does not have a single-arg constructor that takes a Throwable").toString());
		else
			return;
	}

	public CodeEmitter begin_method(int access, Signature sig, Type exceptions[])
	{
		final CodeEmitter e = super.begin_method(access, sig, exceptions);
		if (TypeUtils.isAbstract(access) || sig.equals(Constants.SIG_STATIC))
			return e;
		else
			return new CodeEmitter(exceptions) {

				private Block handler;
				final Type val$exceptions[];
				final UndeclaredThrowableTransformer this$0;

				public void visitMaxs(int maxStack, int maxLocals)
				{
					handler.end();
					EmitUtils.wrap_undeclared_throwable(this, handler, exceptions, wrapper);
					super.visitMaxs(maxStack, maxLocals);
				}

			
			{
				this.this$0 = UndeclaredThrowableTransformer.this;
				exceptions = atype;
				super(wrap);
				handler = begin_block();
			}
			};
	}

}
