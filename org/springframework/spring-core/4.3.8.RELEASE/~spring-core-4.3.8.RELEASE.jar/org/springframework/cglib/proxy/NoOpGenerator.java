// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   NoOpGenerator.java

package org.springframework.cglib.proxy;

import java.util.Iterator;
import java.util.List;
import org.springframework.cglib.core.*;

// Referenced classes of package org.springframework.cglib.proxy:
//			CallbackGenerator

class NoOpGenerator
	implements CallbackGenerator
{

	public static final NoOpGenerator INSTANCE = new NoOpGenerator();

	NoOpGenerator()
	{
	}

	public void generate(ClassEmitter ce, CallbackGenerator.Context context, List methods)
	{
		Iterator it = methods.iterator();
		do
		{
			if (!it.hasNext())
				break;
			MethodInfo method = (MethodInfo)it.next();
			if (TypeUtils.isBridge(method.getModifiers()) || TypeUtils.isProtected(context.getOriginalModifiers(method)) && TypeUtils.isPublic(method.getModifiers()))
			{
				CodeEmitter e = EmitUtils.begin_method(ce, method);
				e.load_this();
				context.emitLoadArgsAndInvoke(e, method);
				e.return_value();
				e.end_method();
			}
		} while (true);
	}

	public void generateStatic(CodeEmitter codeemitter, CallbackGenerator.Context context1, List list)
	{
	}

}
