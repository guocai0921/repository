// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AccessFieldTransformer.java

package org.springframework.cglib.transform.impl;

import org.springframework.asm.Type;
import org.springframework.cglib.core.*;
import org.springframework.cglib.transform.ClassEmitterTransformer;

public class AccessFieldTransformer extends ClassEmitterTransformer
{
	public static interface Callback
	{

		public abstract String getPropertyName(Type type, String s);
	}


	private Callback callback;

	public AccessFieldTransformer(Callback callback)
	{
		this.callback = callback;
	}

	public void declare_field(int access, String name, Type type, Object value)
	{
		super.declare_field(access, name, type, value);
		String property = TypeUtils.upperFirst(callback.getPropertyName(getClassType(), name));
		if (property != null)
		{
			CodeEmitter e = begin_method(1, new Signature((new StringBuilder()).append("get").append(property).toString(), type, Constants.TYPES_EMPTY), null);
			e.load_this();
			e.getfield(name);
			e.return_value();
			e.end_method();
			e = begin_method(1, new Signature((new StringBuilder()).append("set").append(property).toString(), Type.VOID_TYPE, new Type[] {
				type
			}), null);
			e.load_this();
			e.load_arg(0);
			e.putfield(name);
			e.return_value();
			e.end_method();
		}
	}
}
