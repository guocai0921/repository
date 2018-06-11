// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ConditionalConverter.java

package org.springframework.core.convert.converter;

import org.springframework.core.convert.TypeDescriptor;

public interface ConditionalConverter
{

	public abstract boolean matches(TypeDescriptor typedescriptor, TypeDescriptor typedescriptor1);
}
