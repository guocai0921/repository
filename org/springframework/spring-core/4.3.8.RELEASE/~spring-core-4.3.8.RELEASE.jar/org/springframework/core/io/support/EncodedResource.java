// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   EncodedResource.java

package org.springframework.core.io.support;

import java.io.*;
import java.nio.charset.Charset;
import org.springframework.core.io.InputStreamSource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

public class EncodedResource
	implements InputStreamSource
{

	private final Resource resource;
	private final String encoding;
	private final Charset charset;

	public EncodedResource(Resource resource)
	{
		this(resource, null, null);
	}

	public EncodedResource(Resource resource, String encoding)
	{
		this(resource, encoding, null);
	}

	public EncodedResource(Resource resource, Charset charset)
	{
		this(resource, null, charset);
	}

	private EncodedResource(Resource resource, String encoding, Charset charset)
	{
		Assert.notNull(resource, "Resource must not be null");
		this.resource = resource;
		this.encoding = encoding;
		this.charset = charset;
	}

	public final Resource getResource()
	{
		return resource;
	}

	public final String getEncoding()
	{
		return encoding;
	}

	public final Charset getCharset()
	{
		return charset;
	}

	public boolean requiresReader()
	{
		return encoding != null || charset != null;
	}

	public Reader getReader()
		throws IOException
	{
		if (charset != null)
			return new InputStreamReader(resource.getInputStream(), charset);
		if (encoding != null)
			return new InputStreamReader(resource.getInputStream(), encoding);
		else
			return new InputStreamReader(resource.getInputStream());
	}

	public InputStream getInputStream()
		throws IOException
	{
		return resource.getInputStream();
	}

	public boolean equals(Object other)
	{
		if (this == other)
			return true;
		if (!(other instanceof EncodedResource))
		{
			return false;
		} else
		{
			EncodedResource otherResource = (EncodedResource)other;
			return resource.equals(otherResource.resource) && ObjectUtils.nullSafeEquals(charset, otherResource.charset) && ObjectUtils.nullSafeEquals(encoding, otherResource.encoding);
		}
	}

	public int hashCode()
	{
		return resource.hashCode();
	}

	public String toString()
	{
		return resource.toString();
	}
}
