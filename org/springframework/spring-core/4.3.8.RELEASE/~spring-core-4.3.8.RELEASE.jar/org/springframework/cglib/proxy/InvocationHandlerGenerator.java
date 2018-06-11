// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   InvocationHandlerGenerator.java

package org.springframework.cglib.proxy;

import java.util.Iterator;
import java.util.List;
import org.springframework.asm.Type;
import org.springframework.cglib.core.*;

// Referenced classes of package org.springframework.cglib.proxy:
//			CallbackGenerator

class InvocationHandlerGenerator
	implements CallbackGenerator
{

	public static final InvocationHandlerGenerator INSTANCE = new InvocationHandlerGenerator();
	private static final Type INVOCATION_HANDLER = TypeUtils.parseType("org.springframework.cglib.proxy.InvocationHandler");
	private static final Type UNDECLARED_THROWABLE_EXCEPTION = TypeUtils.parseType("org.springframework.cglib.proxy.UndeclaredThrowableException");
	private static final Type METHOD = TypeUtils.parseType("java.lang.reflect.Method");
	private static final Signature INVOKE = TypeUtils.parseSignature("Object invoke(Object, java.lang.reflect.Method, Object[])");

	InvocationHandlerGenerator()
	{
	}

	public void generate(ClassEmitter ce, CallbackGenerator.Context context, List methods)
	{
		CodeEmitter e;
		for (Iterator it = methods.iterator(); it.hasNext(); e.end_method())
		{
			MethodInfo method = (MethodInfo)it.next();
			Signature impl = context.getImplSignature(method);
			ce.declare_field(26, impl.getName(), METHOD, null);
			e = context.beginMethod(ce, method);
			Block handler = e.begin_block();
			context.emitCallback(e, context.getIndex(method));
			e.load_this();
			e.getfield(impl.getName());
			e.create_arg_array();
			e.invoke_interface(INVOCATION_HANDLER, INVOKE);
			e.unbox(method.getSignature().getReturnType());
			e.return_value();
			handler.end();
			EmitUtils.wrap_undeclared_throwable(e, handler, method.getExceptionTypes(), UNDECLARED_THROWABLE_EXCEPTION);
		}

	}

	public void generateStatic(CodeEmitter e, CallbackGenerator.Context context, List methods)
	{
		MethodInfo method;
		for (Iterator it = methods.iterator(); it.hasNext(); e.putfield(context.getImplSignature(method).getName()))
		{
			method = (MethodInfo)it.next();
			EmitUtils.load_method(e, method);
		}

	}

}
