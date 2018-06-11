// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ParallelSorterEmitter.java

package org.springframework.cglib.util;

import org.springframework.asm.ClassVisitor;
import org.springframework.asm.Type;
import org.springframework.cglib.core.*;

class ParallelSorterEmitter extends ClassEmitter
{

	private static final Type PARALLEL_SORTER;
	private static final Signature CSTRUCT_OBJECT_ARRAY = TypeUtils.parseConstructor("Object[]");
	private static final Signature NEW_INSTANCE;
	private static final Signature SWAP = TypeUtils.parseSignature("void swap(int, int)");

	public ParallelSorterEmitter(ClassVisitor v, String className, Object arrays[])
	{
		super(v);
		begin_class(46, 1, className, PARALLEL_SORTER, null, "<generated>");
		EmitUtils.null_constructor(this);
		EmitUtils.factory_method(this, NEW_INSTANCE);
		generateConstructor(arrays);
		generateSwap(arrays);
		end_class();
	}

	private String getFieldName(int index)
	{
		return (new StringBuilder()).append("FIELD_").append(index).toString();
	}

	private void generateConstructor(Object arrays[])
	{
		CodeEmitter e = begin_method(1, CSTRUCT_OBJECT_ARRAY, null);
		e.load_this();
		e.super_invoke_constructor();
		e.load_this();
		e.load_arg(0);
		e.super_putfield("a", Constants.TYPE_OBJECT_ARRAY);
		for (int i = 0; i < arrays.length; i++)
		{
			Type type = Type.getType(arrays[i].getClass());
			declare_field(2, getFieldName(i), type, null);
			e.load_this();
			e.load_arg(0);
			e.push(i);
			e.aaload();
			e.checkcast(type);
			e.putfield(getFieldName(i));
		}

		e.return_value();
		e.end_method();
	}

	private void generateSwap(Object arrays[])
	{
		CodeEmitter e = begin_method(1, SWAP, null);
		for (int i = 0; i < arrays.length; i++)
		{
			Type type = Type.getType(arrays[i].getClass());
			Type component = TypeUtils.getComponentType(type);
			org.springframework.cglib.core.Local T = e.make_local(type);
			e.load_this();
			e.getfield(getFieldName(i));
			e.store_local(T);
			e.load_local(T);
			e.load_arg(0);
			e.load_local(T);
			e.load_arg(1);
			e.array_load(component);
			e.load_local(T);
			e.load_arg(1);
			e.load_local(T);
			e.load_arg(0);
			e.array_load(component);
			e.array_store(component);
			e.array_store(component);
		}

		e.return_value();
		e.end_method();
	}

	static 
	{
		PARALLEL_SORTER = TypeUtils.parseType("org.springframework.cglib.util.ParallelSorter");
		NEW_INSTANCE = new Signature("newInstance", PARALLEL_SORTER, new Type[] {
			Constants.TYPE_OBJECT_ARRAY
		});
	}
}
