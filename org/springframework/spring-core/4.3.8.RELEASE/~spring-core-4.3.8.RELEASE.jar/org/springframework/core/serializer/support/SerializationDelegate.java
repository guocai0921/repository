// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SerializationDelegate.java

package org.springframework.core.serializer.support;

import java.io.*;
import org.springframework.core.serializer.*;
import org.springframework.util.Assert;

public class SerializationDelegate
	implements Serializer, Deserializer
{

	private final Serializer serializer;
	private final Deserializer deserializer;

	public SerializationDelegate(ClassLoader classLoader)
	{
		serializer = new DefaultSerializer();
		deserializer = new DefaultDeserializer(classLoader);
	}

	public SerializationDelegate(Serializer serializer, Deserializer deserializer)
	{
		Assert.notNull(serializer, "Serializer must not be null");
		Assert.notNull(deserializer, "Deserializer must not be null");
		this.serializer = serializer;
		this.deserializer = deserializer;
	}

	public void serialize(Object object, OutputStream outputStream)
		throws IOException
	{
		serializer.serialize(object, outputStream);
	}

	public Object deserialize(InputStream inputStream)
		throws IOException
	{
		return deserializer.deserialize(inputStream);
	}
}
