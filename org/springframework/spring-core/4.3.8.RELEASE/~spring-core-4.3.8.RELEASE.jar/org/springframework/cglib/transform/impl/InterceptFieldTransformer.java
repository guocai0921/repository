// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   InterceptFieldTransformer.java

package org.springframework.cglib.transform.impl;

import org.springframework.asm.Type;
import org.springframework.cglib.core.*;
import org.springframework.cglib.transform.ClassEmitterTransformer;

// Referenced classes of package org.springframework.cglib.transform.impl:
//			InterceptFieldFilter

public class InterceptFieldTransformer extends ClassEmitterTransformer
{

	private static final String CALLBACK_FIELD = "$CGLIB_READ_WRITE_CALLBACK";
	private static final Type CALLBACK;
	private static final Type ENABLED = TypeUtils.parseType("org.springframework.cglib.transform.impl.InterceptFieldEnabled");
	private static final Signature ENABLED_SET;
	private static final Signature ENABLED_GET;
	private InterceptFieldFilter filter;

	public InterceptFieldTransformer(InterceptFieldFilter filter)
	{
		this.filter = filter;
	}

	public void begin_class(int version, int access, String className, Type superType, Type interfaces[], String sourceFile)
	{
		if (!TypeUtils.isInterface(access))
		{
			super.begin_class(version, access, className, superType, TypeUtils.add(interfaces, ENABLED), sourceFile);
			super.declare_field(130, "$CGLIB_READ_WRITE_CALLBACK", CALLBACK, null);
			CodeEmitter e = super.begin_method(1, ENABLED_GET, null);
			e.load_this();
			e.getfield("$CGLIB_READ_WRITE_CALLBACK");
			e.return_value();
			e.end_method();
			e = super.begin_method(1, ENABLED_SET, null);
			e.load_this();
			e.load_arg(0);
			e.putfield("$CGLIB_READ_WRITE_CALLBACK");
			e.return_value();
			e.end_method();
		} else
		{
			super.begin_class(version, access, className, superType, interfaces, sourceFile);
		}
	}

	public void declare_field(int access, String name, Type type, Object value)
	{
		super.declare_field(access, name, type, value);
		if (!TypeUtils.isStatic(access))
		{
			if (filter.acceptRead(getClassType(), name))
				addReadMethod(name, type);
			if (filter.acceptWrite(getClassType(), name))
				addWriteMethod(name, type);
		}
	}

	private void addReadMethod(String name, Type type)
	{
		CodeEmitter e = super.begin_method(1, readMethodSig(name, type.getDescriptor()), null);
		e.load_this();
		e.getfield(name);
		e.load_this();
		e.invoke_interface(ENABLED, ENABLED_GET);
		org.springframework.asm.Label intercept = e.make_label();
		e.ifnonnull(intercept);
		e.return_value();
		e.mark(intercept);
		org.springframework.cglib.core.Local result = e.make_local(type);
		e.store_local(result);
		e.load_this();
		e.invoke_interface(ENABLED, ENABLED_GET);
		e.load_this();
		e.push(name);
		e.load_local(result);
		e.invoke_interface(CALLBACK, readCallbackSig(type));
		if (!TypeUtils.isPrimitive(type))
			e.checkcast(type);
		e.return_value();
		e.end_method();
	}

	private void addWriteMethod(String name, Type type)
	{
		CodeEmitter e = super.begin_method(1, writeMethodSig(name, type.getDescriptor()), null);
		e.load_this();
		e.dup();
		e.invoke_interface(ENABLED, ENABLED_GET);
		org.springframework.asm.Label skip = e.make_label();
		e.ifnull(skip);
		e.load_this();
		e.invoke_interface(ENABLED, ENABLED_GET);
		e.load_this();
		e.push(name);
		e.load_this();
		e.getfield(name);
		e.load_arg(0);
		e.invoke_interface(CALLBACK, writeCallbackSig(type));
		if (!TypeUtils.isPrimitive(type))
			e.checkcast(type);
		org.springframework.asm.Label go = e.make_label();
		e.goTo(go);
		e.mark(skip);
		e.load_arg(0);
		e.mark(go);
		e.putfield(name);
		e.return_value();
		e.end_method();
	}

	public CodeEmitter begin_method(int access, Signature sig, Type exceptions[])
	{
		return new CodeEmitter(super.begin_method(access, sig, exceptions)) {

			final InterceptFieldTransformer this$0;

			public void visitFieldInsn(int opcode, String owner, String name, String desc)
			{
				Type towner = TypeUtils.fromInternalName(owner);
				switch (opcode)
				{
				default:
					break;

				case 180: 
					if (filter.acceptRead(towner, name))
					{
						helper(towner, InterceptFieldTransformer.readMethodSig(name, desc));
						return;
					}
					break;

				case 181: 
					if (filter.acceptWrite(towner, name))
					{
						helper(towner, InterceptFieldTransformer.writeMethodSig(name, desc));
						return;
					}
					break;
				}
				super.visitFieldInsn(opcode, owner, name, desc);
			}

			private void helper(Type owner, Signature sig)
			{
				invoke_virtual(owner, sig);
			}

			
			{
				this.this$0 = InterceptFieldTransformer.this;
				super(wrap);
			}
		};
	}

	private static Signature readMethodSig(String name, String desc)
	{
		return new Signature((new StringBuilder()).append("$cglib_read_").append(name).toString(), (new StringBuilder()).append("()").append(desc).toString());
	}

	private static Signature writeMethodSig(String name, String desc)
	{
		return new Signature((new StringBuilder()).append("$cglib_write_").append(name).toString(), (new StringBuilder()).append("(").append(desc).append(")V").toString());
	}

	private static Signature readCallbackSig(Type type)
	{
		Type remap = remap(type);
		return new Signature((new StringBuilder()).append("read").append(callbackName(remap)).toString(), remap, new Type[] {
			Constants.TYPE_OBJECT, Constants.TYPE_STRING, remap
		});
	}

	private static Signature writeCallbackSig(Type type)
	{
		Type remap = remap(type);
		return new Signature((new StringBuilder()).append("write").append(callbackName(remap)).toString(), remap, new Type[] {
			Constants.TYPE_OBJECT, Constants.TYPE_STRING, remap, remap
		});
	}

	private static Type remap(Type type)
	{
		switch (type.getSort())
		{
		case 9: // '\t'
		case 10: // '\n'
			return Constants.TYPE_OBJECT;
		}
		return type;
	}

	private static String callbackName(Type type)
	{
		return type != Constants.TYPE_OBJECT ? TypeUtils.upperFirst(TypeUtils.getClassName(type)) : "Object";
	}

	static 
	{
		CALLBACK = TypeUtils.parseType("org.springframework.cglib.transform.impl.InterceptFieldCallback");
		ENABLED_SET = new Signature("setInterceptFieldCallback", Type.VOID_TYPE, new Type[] {
			CALLBACK
		});
		ENABLED_GET = new Signature("getInterceptFieldCallback", CALLBACK, new Type[0]);
	}



}
