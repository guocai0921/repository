// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DefaultDeserializer.java

package org.springframework.core.serializer;

import java.io.*;
import org.springframework.core.ConfigurableObjectInputStream;
import org.springframework.core.NestedIOException;

// Referenced classes of package org.springframework.core.serializer:
//			Deserializer

public class DefaultDeserializer
	implements Deserializer
{

	private final ClassLoader classLoader;

	public DefaultDeserializer()
	{
		classLoader = null;
	}

	public DefaultDeserializer(ClassLoader classLoader)
	{
		this.classLoader = classLoader;
	}

	public Object deserialize(InputStream inputStream)
		throws IOException
	{
		ObjectInputStream objectInputStream = new ConfigurableObjectInputStream(inputStream, classLoader);
		return objectInputStream.readObject();
		ClassNotFoundException ex;
		ex;
		throw new NestedIOException("Failed to deserialize object type", ex);
	}
}
