// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   VfsResource.java

package org.springframework.core.io;

import java.io.*;
import java.net.URI;
import java.net.URL;
import org.springframework.core.NestedIOException;
import org.springframework.util.Assert;

// Referenced classes of package org.springframework.core.io:
//			AbstractResource, VfsUtils, Resource

public class VfsResource extends AbstractResource
{

	private final Object resource;

	public VfsResource(Object resource)
	{
		Assert.notNull(resource, "VirtualFile must not be null");
		this.resource = resource;
	}

	public InputStream getInputStream()
		throws IOException
	{
		return VfsUtils.getInputStream(resource);
	}

	public boolean exists()
	{
		return VfsUtils.exists(resource);
	}

	public boolean isReadable()
	{
		return VfsUtils.isReadable(resource);
	}

	public URL getURL()
		throws IOException
	{
		return VfsUtils.getURL(resource);
		Exception ex;
		ex;
		throw new NestedIOException((new StringBuilder()).append("Failed to obtain URL for file ").append(resource).toString(), ex);
	}

	public URI getURI()
		throws IOException
	{
		return VfsUtils.getURI(resource);
		Exception ex;
		ex;
		throw new NestedIOException((new StringBuilder()).append("Failed to obtain URI for ").append(resource).toString(), ex);
	}

	public File getFile()
		throws IOException
	{
		return VfsUtils.getFile(resource);
	}

	public long contentLength()
		throws IOException
	{
		return VfsUtils.getSize(resource);
	}

	public long lastModified()
		throws IOException
	{
		return VfsUtils.getLastModified(resource);
	}

	public Resource createRelative(String relativePath)
		throws IOException
	{
		if (relativePath.startsWith(".") || !relativePath.contains("/"))
			break MISSING_BLOCK_LABEL_35;
		return new VfsResource(VfsUtils.getChild(resource, relativePath));
		IOException ioexception;
		ioexception;
		return new VfsResource(VfsUtils.getRelative(new URL(getURL(), relativePath)));
	}

	public String getFilename()
	{
		return VfsUtils.getName(resource);
	}

	public String getDescription()
	{
		return (new StringBuilder()).append("VFS resource [").append(resource).append("]").toString();
	}

	public boolean equals(Object obj)
	{
		return obj == this || (obj instanceof VfsResource) && resource.equals(((VfsResource)obj).resource);
	}

	public int hashCode()
	{
		return resource.hashCode();
	}
}
