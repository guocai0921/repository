// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   InputStreamResource.java

package org.springframework.core.io;

import java.io.IOException;
import java.io.InputStream;

// Referenced classes of package org.springframework.core.io:
//			AbstractResource

public class InputStreamResource extends AbstractResource
{

	private final InputStream inputStream;
	private final String description;
	private boolean read;

	public InputStreamResource(InputStream inputStream)
	{
		this(inputStream, "resource loaded through InputStream");
	}

	public InputStreamResource(InputStream inputStream, String description)
	{
		read = false;
		if (inputStream == null)
		{
			throw new IllegalArgumentException("InputStream must not be null");
		} else
		{
			this.inputStream = inputStream;
			this.description = description == null ? "" : description;
			return;
		}
	}

	public boolean exists()
	{
		return true;
	}

	public boolean isOpen()
	{
		return true;
	}

	public InputStream getInputStream()
		throws IOException, IllegalStateException
	{
		if (read)
		{
			throw new IllegalStateException("InputStream has already been read - do not use InputStreamResource if a stream needs to be read multiple times");
		} else
		{
			read = true;
			return inputStream;
		}
	}

	public String getDescription()
	{
		return (new StringBuilder()).append("InputStream resource [").append(description).append("]").toString();
	}

	public boolean equals(Object obj)
	{
		return obj == this || (obj instanceof InputStreamResource) && ((InputStreamResource)obj).inputStream.equals(inputStream);
	}

	public int hashCode()
	{
		return inputStream.hashCode();
	}
}
