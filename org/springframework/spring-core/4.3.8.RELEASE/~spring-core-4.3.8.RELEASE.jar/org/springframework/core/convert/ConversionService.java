// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ConversionService.java

package org.springframework.core.convert;


// Referenced classes of package org.springframework.core.convert:
//			TypeDescriptor

public interface ConversionService
{

	public abstract boolean canConvert(Class class1, Class class2);

	public abstract boolean canConvert(TypeDescriptor typedescriptor, TypeDescriptor typedescriptor1);

	public abstract Object convert(Object obj, Class class1);

	public abstract Object convert(Object obj, TypeDescriptor typedescriptor, TypeDescriptor typedescriptor1);
}
