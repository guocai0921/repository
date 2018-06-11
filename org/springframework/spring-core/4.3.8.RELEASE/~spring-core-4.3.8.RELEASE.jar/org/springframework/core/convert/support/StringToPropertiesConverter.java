// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   StringToPropertiesConverter.java

package org.springframework.core.convert.support;

import java.io.ByteArrayInputStream;
import java.util.Properties;
import org.springframework.core.convert.converter.Converter;

final class StringToPropertiesConverter
	implements Converter
{

	StringToPropertiesConverter()
	{
	}

	public Properties convert(String source)
	{
		Properties props;
		props = new Properties();
		props.load(new ByteArrayInputStream(source.getBytes("ISO-8859-1")));
		return props;
		Exception ex;
		ex;
		throw new IllegalArgumentException((new StringBuilder()).append("Failed to parse [").append(source).append("] into Properties").toString(), ex);
	}

	public volatile Object convert(Object obj)
	{
		return convert((String)obj);
	}
}
