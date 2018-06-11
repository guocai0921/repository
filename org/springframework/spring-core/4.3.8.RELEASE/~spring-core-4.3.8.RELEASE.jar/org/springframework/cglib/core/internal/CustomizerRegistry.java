// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CustomizerRegistry.java

package org.springframework.cglib.core.internal;

import java.util.*;
import org.springframework.cglib.core.Customizer;
import org.springframework.cglib.core.KeyFactoryCustomizer;

public class CustomizerRegistry
{

	private final Class customizerTypes[];
	private Map customizers;

	public CustomizerRegistry(Class customizerTypes[])
	{
		customizers = new HashMap();
		this.customizerTypes = customizerTypes;
	}

	public void add(KeyFactoryCustomizer customizer)
	{
		Class klass = customizer.getClass();
		Class aclass[] = customizerTypes;
		int i = aclass.length;
		for (int j = 0; j < i; j++)
		{
			Class type = aclass[j];
			if (!type.isAssignableFrom(klass))
				continue;
			List list = (List)customizers.get(type);
			if (list == null)
				customizers.put(type, list = new ArrayList());
			list.add(customizer);
		}

	}

	public List get(Class klass)
	{
		List list = (List)customizers.get(klass);
		if (list == null)
			return Collections.emptyList();
		else
			return list;
	}

	/**
	 * @deprecated Method singleton is deprecated
	 */

	public static CustomizerRegistry singleton(Customizer customizer)
	{
		CustomizerRegistry registry = new CustomizerRegistry(new Class[] {
			org/springframework/cglib/core/Customizer
		});
		registry.add(customizer);
		return registry;
	}
}
