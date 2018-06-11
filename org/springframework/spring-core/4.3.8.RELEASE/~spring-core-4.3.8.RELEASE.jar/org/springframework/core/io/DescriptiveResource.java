// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DescriptiveResource.java

package org.springframework.core.io;

import java.io.*;

// Referenced classes of package org.springframework.core.io:
//			AbstractResource

public class DescriptiveResource extends AbstractResource
{

	private final String description;

	public DescriptiveResource(String description)
	{
		this.description = description == null ? "" : description;
	}

	public boolean exists()
	{
		return false;
	}

	public boolean isReadable()
	{
		return false;
	}

	public InputStream getInputStream()
		throws IOException
	{
		throw new FileNotFoundException((new StringBuilder()).append(getDescription()).append(" cannot be opened because it does not point to a readable resource").toString());
	}

	public String getDescription()
	{
		return description;
	}

	public boolean equals(Object obj)
	{
		return obj == this || (obj instanceof DescriptiveResource) && ((DescriptiveResource)obj).description.equals(description);
	}

	public int hashCode()
	{
		return description.hashCode();
	}
}
