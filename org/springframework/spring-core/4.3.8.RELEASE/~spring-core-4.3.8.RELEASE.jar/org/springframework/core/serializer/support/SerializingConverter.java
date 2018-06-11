// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SerializingConverter.java

package org.springframework.core.serializer.support;

import java.io.ByteArrayOutputStream;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.serializer.DefaultSerializer;
import org.springframework.core.serializer.Serializer;
import org.springframework.util.Assert;

// Referenced classes of package org.springframework.core.serializer.support:
//			SerializationFailedException

public class SerializingConverter
	implements Converter
{

	private final Serializer serializer;

	public SerializingConverter()
	{
		serializer = new DefaultSerializer();
	}

	public SerializingConverter(Serializer serializer)
	{
		Assert.notNull(serializer, "Serializer must not be null");
		this.serializer = serializer;
	}

	public byte[] convert(Object source)
	{
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream(1024);
		serializer.serialize(source, byteStream);
		return byteStream.toByteArray();
		Throwable ex;
		ex;
		throw new SerializationFailedException((new StringBuilder()).append("Failed to serialize object using ").append(serializer.getClass().getSimpleName()).toString(), ex);
	}

	public volatile Object convert(Object obj)
	{
		return convert(obj);
	}
}
