// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   LazyLoaderGenerator.java

package org.springframework.cglib.proxy;

import java.util.*;
import org.springframework.asm.Type;
import org.springframework.cglib.core.*;

// Referenced classes of package org.springframework.cglib.proxy:
//			CallbackGenerator

class LazyLoaderGenerator
	implements CallbackGenerator
{

	public static final LazyLoaderGenerator INSTANCE = new LazyLoaderGenerator();
	private static final Signature LOAD_OBJECT = TypeUtils.parseSignature("Object loadObject()");
	private static final Type LAZY_LOADER = TypeUtils.parseType("org.springframework.cglib.proxy.LazyLoader");

	LazyLoaderGenerator()
	{
	}

	public void generate(ClassEmitter ce, CallbackGenerator.Context context, List methods)
	{
		Set indexes = new HashSet();
		Iterator it = methods.iterator();
		do
		{
			if (!it.hasNext())
				break;
			MethodInfo method = (MethodInfo)it.next();
			if (!TypeUtils.isProtected(method.getModifiers()))
			{
				int index = context.getIndex(method);
				indexes.add(new Integer(index));
				CodeEmitter e = context.beginMethod(ce, method);
				e.load_this();
				e.dup();
				e.invoke_virtual_this(loadMethod(index));
				e.checkcast(method.getClassInfo().getType());
				e.load_args();
				e.invoke(method);
				e.return_value();
				e.end_method();
			}
		} while (true);
		CodeEmitter e;
		for (it = indexes.iterator(); it.hasNext(); e.end_method())
		{
			int index = ((Integer)it.next()).intValue();
			String delegate = (new StringBuilder()).append("CGLIB$LAZY_LOADER_").append(index).toString();
			ce.declare_field(2, delegate, Constants.TYPE_OBJECT, null);
			e = ce.begin_method(50, loadMethod(index), null);
			e.load_this();
			e.getfield(delegate);
			e.dup();
			org.springframework.asm.Label end = e.make_label();
			e.ifnonnull(end);
			e.pop();
			e.load_this();
			context.emitCallback(e, index);
			e.invoke_interface(LAZY_LOADER, LOAD_OBJECT);
			e.dup_x1();
			e.putfield(delegate);
			e.mark(end);
			e.return_value();
		}

	}

	private Signature loadMethod(int index)
	{
		return new Signature((new StringBuilder()).append("CGLIB$LOAD_PRIVATE_").append(index).toString(), Constants.TYPE_OBJECT, Constants.TYPES_EMPTY);
	}

	public void generateStatic(CodeEmitter codeemitter, CallbackGenerator.Context context1, List list)
	{
	}

}
