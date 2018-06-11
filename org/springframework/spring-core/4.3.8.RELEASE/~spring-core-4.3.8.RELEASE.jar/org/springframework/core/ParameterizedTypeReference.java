// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ParameterizedTypeReference.java

package org.springframework.core;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import org.springframework.util.Assert;

public abstract class ParameterizedTypeReference
{

	private final Type type;

	protected ParameterizedTypeReference()
	{
		Class parameterizedTypeReferenceSubclass = findParameterizedTypeReferenceSubclass(getClass());
		Type type = parameterizedTypeReferenceSubclass.getGenericSuperclass();
		Assert.isInstanceOf(java/lang/reflect/ParameterizedType, type, "Type must be a parameterized type");
		ParameterizedType parameterizedType = (ParameterizedType)type;
		Assert.isTrue(parameterizedType.getActualTypeArguments().length == 1, "Number of type arguments must be 1");
		this.type = parameterizedType.getActualTypeArguments()[0];
	}

	public Type getType()
	{
		return type;
	}

	public boolean equals(Object obj)
	{
		return this == obj || (obj instanceof ParameterizedTypeReference) && type.equals(((ParameterizedTypeReference)obj).type);
	}

	public int hashCode()
	{
		return type.hashCode();
	}

	public String toString()
	{
		return (new StringBuilder()).append("ParameterizedTypeReference<").append(type).append(">").toString();
	}

	private static Class findParameterizedTypeReferenceSubclass(Class child)
	{
		Class parent = child.getSuperclass();
		if (java/lang/Object == parent)
			throw new IllegalStateException("Expected ParameterizedTypeReference superclass");
		if (org/springframework/core/ParameterizedTypeReference == parent)
			return child;
		else
			return findParameterizedTypeReferenceSubclass(parent);
	}
}
