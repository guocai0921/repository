// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ResizableByteArrayOutputStream.java

package org.springframework.util;

import java.io.ByteArrayOutputStream;

// Referenced classes of package org.springframework.util:
//			Assert

public class ResizableByteArrayOutputStream extends ByteArrayOutputStream
{

	private static final int DEFAULT_INITIAL_CAPACITY = 256;

	public ResizableByteArrayOutputStream()
	{
		super(256);
	}

	public ResizableByteArrayOutputStream(int initialCapacity)
	{
		super(initialCapacity);
	}

	public synchronized void resize(int targetCapacity)
	{
		Assert.isTrue(targetCapacity >= count, "New capacity must not be smaller than current size");
		byte resizedBuffer[] = new byte[targetCapacity];
		System.arraycopy(buf, 0, resizedBuffer, 0, count);
		buf = resizedBuffer;
	}

	public synchronized void grow(int additionalCapacity)
	{
		Assert.isTrue(additionalCapacity >= 0, "Additional capacity must be 0 or higher");
		if (count + additionalCapacity > buf.length)
		{
			int newCapacity = Math.max(buf.length * 2, count + additionalCapacity);
			resize(newCapacity);
		}
	}

	public synchronized int capacity()
	{
		return buf.length;
	}
}
