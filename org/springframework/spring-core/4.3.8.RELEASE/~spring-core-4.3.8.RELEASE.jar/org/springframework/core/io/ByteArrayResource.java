// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ByteArrayResource.java

package org.springframework.core.io;

import java.io.*;
import java.util.Arrays;

// Referenced classes of package org.springframework.core.io:
//			AbstractResource

public class ByteArrayResource extends AbstractResource
{

	private final byte byteArray[];
	private final String description;

	public ByteArrayResource(byte byteArray[])
	{
		this(byteArray, "resource loaded from byte array");
	}

	public ByteArrayResource(byte byteArray[], String description)
	{
		if (byteArray == null)
		{
			throw new IllegalArgumentException("Byte array must not be null");
		} else
		{
			this.byteArray = byteArray;
			this.description = description == null ? "" : description;
			return;
		}
	}

	public final byte[] getByteArray()
	{
		return byteArray;
	}

	public boolean exists()
	{
		return true;
	}

	public long contentLength()
	{
		return (long)byteArray.length;
	}

	public InputStream getInputStream()
		throws IOException
	{
		return new ByteArrayInputStream(byteArray);
	}

	public String getDescription()
	{
		return (new StringBuilder()).append("Byte array resource [").append(description).append("]").toString();
	}

	public boolean equals(Object obj)
	{
		return obj == this || (obj instanceof ByteArrayResource) && Arrays.equals(((ByteArrayResource)obj).byteArray, byteArray);
	}

	public int hashCode()
	{
		return [B.hashCode() * 29 * byteArray.length;
	}
}
