// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SerializationUtils.java

package org.springframework.util;

import java.io.*;

public abstract class SerializationUtils
{

	public SerializationUtils()
	{
	}

	public static byte[] serialize(Object object)
	{
		if (object == null)
			return null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
		try
		{
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			oos.flush();
		}
		catch (IOException ex)
		{
			throw new IllegalArgumentException((new StringBuilder()).append("Failed to serialize object of type: ").append(object.getClass()).toString(), ex);
		}
		return baos.toByteArray();
	}

	public static Object deserialize(byte bytes[])
	{
		if (bytes == null)
			return null;
		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
		return ois.readObject();
		IOException ex;
		ex;
		throw new IllegalArgumentException("Failed to deserialize object", ex);
		ex;
		throw new IllegalStateException("Failed to deserialize object type", ex);
	}
}
