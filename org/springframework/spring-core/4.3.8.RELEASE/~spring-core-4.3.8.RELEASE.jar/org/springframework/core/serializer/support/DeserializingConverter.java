// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DeserializingConverter.java

package org.springframework.core.serializer.support;

import java.io.ByteArrayInputStream;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.serializer.DefaultDeserializer;
import org.springframework.core.serializer.Deserializer;
import org.springframework.util.Assert;

// Referenced classes of package org.springframework.core.serializer.support:
//			SerializationFailedException

public class DeserializingConverter
	implements Converter
{

	private final Deserializer deserializer;

	public DeserializingConverter()
	{
		deserializer = new DefaultDeserializer();
	}

	public DeserializingConverter(ClassLoader classLoader)
	{
		deserializer = new DefaultDeserializer(classLoader);
	}

	public DeserializingConverter(Deserializer deserializer)
	{
		Assert.notNull(deserializer, "Deserializer must not be null");
		this.deserializer = deserializer;
	}

	public Object convert(byte source[])
	{
		ByteArrayInputStream byteStream = new ByteArrayInputStream(source);
		return deserializer.deserialize(byteStream);
		Throwable ex;
		ex;
		throw new SerializationFailedException((new StringBuilder()).append("Failed to deserialize payload. Is the byte array a result of corresponding serialization for ").append(deserializer.getClass().getSimpleName()).append("?").toString(), ex);
	}

	public volatile Object convert(Object obj)
	{
		return convert((byte[])obj);
	}
}
