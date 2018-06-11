// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ConverterRegistry.java

package org.springframework.core.convert.converter;


// Referenced classes of package org.springframework.core.convert.converter:
//			Converter, GenericConverter, ConverterFactory

public interface ConverterRegistry
{

	public abstract void addConverter(Converter converter);

	public abstract void addConverter(Class class1, Class class2, Converter converter);

	public abstract void addConverter(GenericConverter genericconverter);

	public abstract void addConverterFactory(ConverterFactory converterfactory);

	public abstract void removeConvertible(Class class1, Class class2);
}
