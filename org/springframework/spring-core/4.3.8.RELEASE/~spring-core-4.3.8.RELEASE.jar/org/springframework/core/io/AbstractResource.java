// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AbstractResource.java

package org.springframework.core.io;

import java.io.*;
import java.net.*;
import org.springframework.core.NestedIOException;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;

// Referenced classes of package org.springframework.core.io:
//			Resource

public abstract class AbstractResource
	implements Resource
{

	public AbstractResource()
	{
	}

	public boolean exists()
	{
		return getFile().exists();
		IOException ex;
		ex;
		InputStream is = getInputStream();
		is.close();
		return true;
		Throwable isEx;
		isEx;
		return false;
	}

	public boolean isReadable()
	{
		return true;
	}

	public boolean isOpen()
	{
		return false;
	}

	public URL getURL()
		throws IOException
	{
		throw new FileNotFoundException((new StringBuilder()).append(getDescription()).append(" cannot be resolved to URL").toString());
	}

	public URI getURI()
		throws IOException
	{
		URL url = getURL();
		return ResourceUtils.toURI(url);
		URISyntaxException ex;
		ex;
		throw new NestedIOException((new StringBuilder()).append("Invalid URI [").append(url).append("]").toString(), ex);
	}

	public File getFile()
		throws IOException
	{
		throw new FileNotFoundException((new StringBuilder()).append(getDescription()).append(" cannot be resolved to absolute file path").toString());
	}

	public long contentLength()
		throws IOException
	{
		InputStream is;
		is = getInputStream();
		Assert.state(is != null, "Resource InputStream must not be null");
		long l;
		long size = 0L;
		byte buf[] = new byte[255];
		int read;
		while ((read = is.read(buf)) != -1) 
			size += read;
		l = size;
		try
		{
			is.close();
		}
		catch (IOException ioexception) { }
		return l;
		Exception exception;
		exception;
		try
		{
			is.close();
		}
		catch (IOException ioexception1) { }
		throw exception;
	}

	public long lastModified()
		throws IOException
	{
		long lastModified = getFileForLastModifiedCheck().lastModified();
		if (lastModified == 0L)
			throw new FileNotFoundException((new StringBuilder()).append(getDescription()).append(" cannot be resolved in the file system for resolving its last-modified timestamp").toString());
		else
			return lastModified;
	}

	protected File getFileForLastModifiedCheck()
		throws IOException
	{
		return getFile();
	}

	public Resource createRelative(String relativePath)
		throws IOException
	{
		throw new FileNotFoundException((new StringBuilder()).append("Cannot create a relative resource for ").append(getDescription()).toString());
	}

	public String getFilename()
	{
		return null;
	}

	public String toString()
	{
		return getDescription();
	}

	public boolean equals(Object obj)
	{
		return obj == this || (obj instanceof Resource) && ((Resource)obj).getDescription().equals(getDescription());
	}

	public int hashCode()
	{
		return getDescription().hashCode();
	}
}
