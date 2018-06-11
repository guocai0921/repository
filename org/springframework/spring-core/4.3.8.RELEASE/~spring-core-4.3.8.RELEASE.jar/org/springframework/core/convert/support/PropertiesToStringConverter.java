// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   PropertiesToStringConverter.java

package org.springframework.core.convert.support;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;
import org.springframework.core.convert.converter.Converter;

final class PropertiesToStringConverter
	implements Converter
{

	PropertiesToStringConverter()
	{
	}

	public String convert(Properties source)
	{
		ByteArrayOutputStream os;
		os = new ByteArrayOutputStream(256);
		source.store(os, null);
		return os.toString("ISO-8859-1");
		IOException ex;
		ex;
		throw new IllegalArgumentException((new StringBuilder()).append("Failed to store [").append(source).append("] into String").toString(), ex);
	}

	public volatile Object convert(Object obj)
	{
		return convert((Properties)obj);
	}
}
