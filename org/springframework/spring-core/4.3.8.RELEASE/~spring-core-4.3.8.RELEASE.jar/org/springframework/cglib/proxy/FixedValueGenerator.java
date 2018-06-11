// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   FixedValueGenerator.java

package org.springframework.cglib.proxy;

import java.util.Iterator;
import java.util.List;
import org.springframework.asm.Type;
import org.springframework.cglib.core.*;

// Referenced classes of package org.springframework.cglib.proxy:
//			CallbackGenerator

class FixedValueGenerator
	implements CallbackGenerator
{

	public static final FixedValueGenerator INSTANCE = new FixedValueGenerator();
	private static final Type FIXED_VALUE = TypeUtils.parseType("org.springframework.cglib.proxy.FixedValue");
	private static final Signature LOAD_OBJECT = TypeUtils.parseSignature("Object loadObject()");

	FixedValueGenerator()
	{
	}

	public void generate(ClassEmitter ce, CallbackGenerator.Context context, List methods)
	{
		CodeEmitter e;
		for (Iterator it = methods.iterator(); it.hasNext(); e.end_method())
		{
			MethodInfo method = (MethodInfo)it.next();
			e = context.beginMethod(ce, method);
			context.emitCallback(e, context.getIndex(method));
			e.invoke_interface(FIXED_VALUE, LOAD_OBJECT);
			e.unbox_or_zero(e.getReturnType());
			e.return_value();
		}

	}

	public void generateStatic(CodeEmitter codeemitter, CallbackGenerator.Context context1, List list)
	{
	}

}
