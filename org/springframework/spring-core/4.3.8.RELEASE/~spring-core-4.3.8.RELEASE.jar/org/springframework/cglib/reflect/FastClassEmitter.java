// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   FastClassEmitter.java

package org.springframework.cglib.reflect;

import java.lang.reflect.Method;
import java.util.*;
import org.springframework.asm.*;
import org.springframework.cglib.core.*;

class FastClassEmitter extends ClassEmitter
{
	private static class GetIndexCallback
		implements ObjectSwitchCallback
	{

		private CodeEmitter e;
		private Map indexes;

		public void processCase(Object key, Label end)
		{
			e.push(((Integer)indexes.get(key)).intValue());
			e.return_value();
		}

		public void processDefault()
		{
			e.push(-1);
			e.return_value();
		}

		public GetIndexCallback(CodeEmitter e, List methods)
		{
			indexes = new HashMap();
			this.e = e;
			int index = 0;
			for (Iterator it = methods.iterator(); it.hasNext(); indexes.put(it.next(), new Integer(index++)));
		}
	}


	private static final Signature CSTRUCT_CLASS = TypeUtils.parseConstructor("Class");
	private static final Signature METHOD_GET_INDEX = TypeUtils.parseSignature("int getIndex(String, Class[])");
	private static final Signature SIGNATURE_GET_INDEX;
	private static final Signature TO_STRING = TypeUtils.parseSignature("String toString()");
	private static final Signature CONSTRUCTOR_GET_INDEX = TypeUtils.parseSignature("int getIndex(Class[])");
	private static final Signature INVOKE = TypeUtils.parseSignature("Object invoke(int, Object, Object[])");
	private static final Signature NEW_INSTANCE = TypeUtils.parseSignature("Object newInstance(int, Object[])");
	private static final Signature GET_MAX_INDEX = TypeUtils.parseSignature("int getMaxIndex()");
	private static final Signature GET_SIGNATURE_WITHOUT_RETURN_TYPE = TypeUtils.parseSignature("String getSignatureWithoutReturnType(String, Class[])");
	private static final Type FAST_CLASS = TypeUtils.parseType("org.springframework.cglib.reflect.FastClass");
	private static final Type ILLEGAL_ARGUMENT_EXCEPTION = TypeUtils.parseType("IllegalArgumentException");
	private static final Type INVOCATION_TARGET_EXCEPTION;
	private static final Type INVOCATION_TARGET_EXCEPTION_ARRAY[];
	private static final int TOO_MANY_METHODS = 100;

	public FastClassEmitter(ClassVisitor v, String className, Class type)
	{
		super(v);
		Type base = Type.getType(type);
		begin_class(46, 1, className, FAST_CLASS, null, "<generated>");
		CodeEmitter e = begin_method(1, CSTRUCT_CLASS, null);
		e.load_this();
		e.load_args();
		e.super_invoke_constructor(CSTRUCT_CLASS);
		e.return_value();
		e.end_method();
		VisibilityPredicate vp = new VisibilityPredicate(type, false);
		List methods = ReflectUtils.addAllMethods(type, new ArrayList());
		CollectionUtils.filter(methods, vp);
		CollectionUtils.filter(methods, new DuplicatesPredicate());
		List constructors = new ArrayList(Arrays.asList(type.getDeclaredConstructors()));
		CollectionUtils.filter(constructors, vp);
		emitIndexBySignature(methods);
		emitIndexByClassArray(methods);
		e = begin_method(1, CONSTRUCTOR_GET_INDEX, null);
		e.load_args();
		List info = CollectionUtils.transform(constructors, MethodInfoTransformer.getInstance());
		EmitUtils.constructor_switch(e, info, new GetIndexCallback(e, info));
		e.end_method();
		e = begin_method(1, INVOKE, INVOCATION_TARGET_EXCEPTION_ARRAY);
		e.load_arg(1);
		e.checkcast(base);
		e.load_arg(0);
		invokeSwitchHelper(e, methods, 2, base);
		e.end_method();
		e = begin_method(1, NEW_INSTANCE, INVOCATION_TARGET_EXCEPTION_ARRAY);
		e.new_instance(base);
		e.dup();
		e.load_arg(0);
		invokeSwitchHelper(e, constructors, 1, base);
		e.end_method();
		e = begin_method(1, GET_MAX_INDEX, null);
		e.push(methods.size() - 1);
		e.return_value();
		e.end_method();
		end_class();
	}

	private void emitIndexBySignature(List methods)
	{
		CodeEmitter e = begin_method(1, SIGNATURE_GET_INDEX, null);
		List signatures = CollectionUtils.transform(methods, new Transformer() {

			final FastClassEmitter this$0;

			public Object transform(Object obj)
			{
				return ReflectUtils.getSignature((Method)obj).toString();
			}

			
			{
				this.this$0 = FastClassEmitter.this;
				super();
			}
		});
		e.load_arg(0);
		e.invoke_virtual(Constants.TYPE_OBJECT, TO_STRING);
		signatureSwitchHelper(e, signatures);
		e.end_method();
	}

	private void emitIndexByClassArray(List methods)
	{
		CodeEmitter e = begin_method(1, METHOD_GET_INDEX, null);
		if (methods.size() > 100)
		{
			List signatures = CollectionUtils.transform(methods, new Transformer() {

				final FastClassEmitter this$0;

				public Object transform(Object obj)
				{
					String s = ReflectUtils.getSignature((Method)obj).toString();
					return s.substring(0, s.lastIndexOf(')') + 1);
				}

			
			{
				this.this$0 = FastClassEmitter.this;
				super();
			}
			});
			e.load_args();
			e.invoke_static(FAST_CLASS, GET_SIGNATURE_WITHOUT_RETURN_TYPE);
			signatureSwitchHelper(e, signatures);
		} else
		{
			e.load_args();
			List info = CollectionUtils.transform(methods, MethodInfoTransformer.getInstance());
			EmitUtils.method_switch(e, info, new GetIndexCallback(e, info));
		}
		e.end_method();
	}

	private void signatureSwitchHelper(final CodeEmitter e, final List signatures)
	{
		ObjectSwitchCallback callback = new ObjectSwitchCallback() {

			final CodeEmitter val$e;
			final List val$signatures;
			final FastClassEmitter this$0;

			public void processCase(Object key, Label end)
			{
				e.push(signatures.indexOf(key));
				e.return_value();
			}

			public void processDefault()
			{
				e.push(-1);
				e.return_value();
			}

			
			{
				this.this$0 = FastClassEmitter.this;
				e = codeemitter;
				signatures = list;
				super();
			}
		};
		EmitUtils.string_switch(e, (String[])(String[])signatures.toArray(new String[signatures.size()]), 1, callback);
	}

	private static void invokeSwitchHelper(CodeEmitter e, List members, int arg, Type base)
	{
		List info = CollectionUtils.transform(members, MethodInfoTransformer.getInstance());
		Label illegalArg = e.make_label();
		Block block = e.begin_block();
		e.process_switch(getIntRange(info.size()), new ProcessSwitchCallback(info, e, arg, base, illegalArg) {

			final List val$info;
			final CodeEmitter val$e;
			final int val$arg;
			final Type val$base;
			final Label val$illegalArg;

			public void processCase(int key, Label end)
			{
				MethodInfo method = (MethodInfo)info.get(key);
				Type types[] = method.getSignature().getArgumentTypes();
				for (int i = 0; i < types.length; i++)
				{
					e.load_arg(arg);
					e.aaload(i);
					e.unbox(types[i]);
				}

				e.invoke(method, base);
				if (!TypeUtils.isConstructor(method))
					e.box(method.getSignature().getReturnType());
				e.return_value();
			}

			public void processDefault()
			{
				e.goTo(illegalArg);
			}

			
			{
				info = list;
				e = codeemitter;
				arg = i;
				base = type;
				illegalArg = label;
				super();
			}
		});
		block.end();
		EmitUtils.wrap_throwable(block, INVOCATION_TARGET_EXCEPTION);
		e.mark(illegalArg);
		e.throw_exception(ILLEGAL_ARGUMENT_EXCEPTION, "Cannot find matching method/constructor");
	}

	private static int[] getIntRange(int length)
	{
		int range[] = new int[length];
		for (int i = 0; i < length; i++)
			range[i] = i;

		return range;
	}

	static 
	{
		SIGNATURE_GET_INDEX = new Signature("getIndex", Type.INT_TYPE, new Type[] {
			Constants.TYPE_SIGNATURE
		});
		INVOCATION_TARGET_EXCEPTION = TypeUtils.parseType("java.lang.reflect.InvocationTargetException");
		INVOCATION_TARGET_EXCEPTION_ARRAY = (new Type[] {
			INVOCATION_TARGET_EXCEPTION
		});
	}
}
