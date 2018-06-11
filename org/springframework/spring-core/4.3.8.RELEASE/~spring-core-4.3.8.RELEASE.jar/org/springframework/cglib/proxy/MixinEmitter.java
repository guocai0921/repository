// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   MixinEmitter.java

package org.springframework.cglib.proxy;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.Type;
import org.springframework.cglib.core.*;

class MixinEmitter extends ClassEmitter
{

	private static final String FIELD_NAME = "CGLIB$DELEGATES";
	private static final Signature CSTRUCT_OBJECT_ARRAY = TypeUtils.parseConstructor("Object[]");
	private static final Type MIXIN;
	private static final Signature NEW_INSTANCE;

	public MixinEmitter(ClassVisitor v, String className, Class classes[], int route[])
	{
		super(v);
		begin_class(46, 1, className, MIXIN, TypeUtils.getTypes(getInterfaces(classes)), "<generated>");
		EmitUtils.null_constructor(this);
		EmitUtils.factory_method(this, NEW_INSTANCE);
		declare_field(2, "CGLIB$DELEGATES", Constants.TYPE_OBJECT_ARRAY, null);
		CodeEmitter e = begin_method(1, CSTRUCT_OBJECT_ARRAY, null);
		e.load_this();
		e.super_invoke_constructor();
		e.load_this();
		e.load_arg(0);
		e.putfield("CGLIB$DELEGATES");
		e.return_value();
		e.end_method();
		Set unique = new HashSet();
		for (int i = 0; i < classes.length; i++)
		{
			Method methods[] = getMethods(classes[i]);
			for (int j = 0; j < methods.length; j++)
			{
				if (!unique.add(MethodWrapper.create(methods[j])))
					continue;
				MethodInfo method = ReflectUtils.getMethodInfo(methods[j]);
				int modifiers = 1;
				if ((method.getModifiers() & 0x80) == 128)
					modifiers |= 0x80;
				e = EmitUtils.begin_method(this, method, modifiers);
				e.load_this();
				e.getfield("CGLIB$DELEGATES");
				e.aaload(route == null ? i : route[i]);
				e.checkcast(method.getClassInfo().getType());
				e.load_args();
				e.invoke(method);
				e.return_value();
				e.end_method();
			}

		}

		end_class();
	}

	protected Class[] getInterfaces(Class classes[])
	{
		return classes;
	}

	protected Method[] getMethods(Class type)
	{
		return type.getMethods();
	}

	static 
	{
		MIXIN = TypeUtils.parseType("org.springframework.cglib.proxy.Mixin");
		NEW_INSTANCE = new Signature("newInstance", MIXIN, new Type[] {
			Constants.TYPE_OBJECT_ARRAY
		});
	}
}
