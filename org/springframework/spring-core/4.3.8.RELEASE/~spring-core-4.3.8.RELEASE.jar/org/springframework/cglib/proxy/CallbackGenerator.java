// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CallbackGenerator.java

package org.springframework.cglib.proxy;

import java.util.List;
import org.springframework.cglib.core.*;

interface CallbackGenerator
{
	public static interface Context
	{

		public abstract ClassLoader getClassLoader();

		public abstract CodeEmitter beginMethod(ClassEmitter classemitter, MethodInfo methodinfo);

		public abstract int getOriginalModifiers(MethodInfo methodinfo);

		public abstract int getIndex(MethodInfo methodinfo);

		public abstract void emitCallback(CodeEmitter codeemitter, int i);

		public abstract Signature getImplSignature(MethodInfo methodinfo);

		public abstract void emitLoadArgsAndInvoke(CodeEmitter codeemitter, MethodInfo methodinfo);
	}


	public abstract void generate(ClassEmitter classemitter, Context context, List list)
		throws Exception;

	public abstract void generateStatic(CodeEmitter codeemitter, Context context, List list)
		throws Exception;
}
