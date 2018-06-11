// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AddPropertyTransformer.java

package org.springframework.cglib.transform.impl;

import java.util.Map;
import java.util.Set;
import org.springframework.asm.Type;
import org.springframework.cglib.core.EmitUtils;
import org.springframework.cglib.core.TypeUtils;
import org.springframework.cglib.transform.ClassEmitterTransformer;

public class AddPropertyTransformer extends ClassEmitterTransformer
{

	private final String names[];
	private final Type types[];

	public AddPropertyTransformer(Map props)
	{
		int size = props.size();
		names = (String[])(String[])props.keySet().toArray(new String[size]);
		types = new Type[size];
		for (int i = 0; i < size; i++)
			types[i] = (Type)props.get(names[i]);

	}

	public AddPropertyTransformer(String names[], Type types[])
	{
		this.names = names;
		this.types = types;
	}

	public void end_class()
	{
		if (!TypeUtils.isAbstract(getAccess()))
			EmitUtils.add_properties(this, names, types);
		super.end_class();
	}
}
