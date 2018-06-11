// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DispatcherGenerator.java

package org.springframework.cglib.proxy;

import java.util.Iterator;
import java.util.List;
import org.springframework.asm.Type;
import org.springframework.cglib.core.*;

// Referenced classes of package org.springframework.cglib.proxy:
//			CallbackGenerator

class DispatcherGenerator
	implements CallbackGenerator
{

	public static final DispatcherGenerator INSTANCE = new DispatcherGenerator(false);
	public static final DispatcherGenerator PROXY_REF_INSTANCE = new DispatcherGenerator(true);
	private static final Type DISPATCHER = TypeUtils.parseType("org.springframework.cglib.proxy.Dispatcher");
	private static final Type PROXY_REF_DISPATCHER = TypeUtils.parseType("org.springframework.cglib.proxy.ProxyRefDispatcher");
	private static final Signature LOAD_OBJECT = TypeUtils.parseSignature("Object loadObject()");
	private static final Signature PROXY_REF_LOAD_OBJECT = TypeUtils.parseSignature("Object loadObject(Object)");
	private boolean proxyRef;

	private DispatcherGenerator(boolean proxyRef)
	{
		this.proxyRef = proxyRef;
	}

	public void generate(ClassEmitter ce, CallbackGenerator.Context context, List methods)
	{
		Iterator it = methods.iterator();
		do
		{
			if (!it.hasNext())
				break;
			MethodInfo method = (MethodInfo)it.next();
			if (!TypeUtils.isProtected(method.getModifiers()))
			{
				CodeEmitter e = context.beginMethod(ce, method);
				context.emitCallback(e, context.getIndex(method));
				if (proxyRef)
				{
					e.load_this();
					e.invoke_interface(PROXY_REF_DISPATCHER, PROXY_REF_LOAD_OBJECT);
				} else
				{
					e.invoke_interface(DISPATCHER, LOAD_OBJECT);
				}
				e.checkcast(method.getClassInfo().getType());
				e.load_args();
				e.invoke(method);
				e.return_value();
				e.end_method();
			}
		} while (true);
	}

	public void generateStatic(CodeEmitter codeemitter, CallbackGenerator.Context context1, List list)
	{
	}

}
