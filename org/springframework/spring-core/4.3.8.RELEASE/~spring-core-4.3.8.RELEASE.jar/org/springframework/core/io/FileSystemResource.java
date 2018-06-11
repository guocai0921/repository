// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   FileSystemResource.java

package org.springframework.core.io;

import java.io.*;
import java.net.URI;
import java.net.URL;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

// Referenced classes of package org.springframework.core.io:
//			AbstractResource, WritableResource, Resource

public class FileSystemResource extends AbstractResource
	implements WritableResource
{

	private final File file;
	private final String path;

	public FileSystemResource(File file)
	{
		Assert.notNull(file, "File must not be null");
		this.file = file;
		path = StringUtils.cleanPath(file.getPath());
	}

	public FileSystemResource(String path)
	{
		Assert.notNull(path, "Path must not be null");
		file = new File(path);
		this.path = StringUtils.cleanPath(path);
	}

	public final String getPath()
	{
		return path;
	}

	public boolean exists()
	{
		return file.exists();
	}

	public boolean isReadable()
	{
		return file.canRead() && !file.isDirectory();
	}

	public InputStream getInputStream()
		throws IOException
	{
		return new FileInputStream(file);
	}

	public boolean isWritable()
	{
		return file.canWrite() && !file.isDirectory();
	}

	public OutputStream getOutputStream()
		throws IOException
	{
		return new FileOutputStream(file);
	}

	public URL getURL()
		throws IOException
	{
		return file.toURI().toURL();
	}

	public URI getURI()
		throws IOException
	{
		return file.toURI();
	}

	public File getFile()
	{
		return file;
	}

	public long contentLength()
		throws IOException
	{
		return file.length();
	}

	public Resource createRelative(String relativePath)
	{
		String pathToUse = StringUtils.applyRelativePath(path, relativePath);
		return new FileSystemResource(pathToUse);
	}

	public String getFilename()
	{
		return file.getName();
	}

	public String getDescription()
	{
		return (new StringBuilder()).append("file [").append(file.getAbsolutePath()).append("]").toString();
	}

	public boolean equals(Object obj)
	{
		return obj == this || (obj instanceof FileSystemResource) && path.equals(((FileSystemResource)obj).path);
	}

	public int hashCode()
	{
		return path.hashCode();
	}
}
