// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ConverterFactory.java

package org.springframework.core.convert.converter;


// Referenced classes of package org.springframework.core.convert.converter:
//			Converter

public interface ConverterFactory
{

	public abstract Converter getConverter(Class class1);
}
