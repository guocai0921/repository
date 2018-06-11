// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   PathResource.java

package org.springframework.core.io;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.FileTime;
import org.springframework.util.Assert;

// Referenced classes of package org.springframework.core.io:
//			AbstractResource, WritableResource, Resource

public class PathResource extends AbstractResource
	implements WritableResource
{

	private final Path path;

	public PathResource(Path path)
	{
		Assert.notNull(path, "Path must not be null");
		this.path = path.normalize();
	}

	public PathResource(String path)
	{
		Assert.notNull(path, "Path must not be null");
		this.path = Paths.get(path, new String[0]).normalize();
	}

	public PathResource(URI uri)
	{
		Assert.notNull(uri, "URI must not be null");
		path = Paths.get(uri).normalize();
	}

	public final String getPath()
	{
		return path.toString();
	}

	public boolean exists()
	{
		return Files.exists(path, new LinkOption[0]);
	}

	public boolean isReadable()
	{
		return Files.isReadable(path) && !Files.isDirectory(path, new LinkOption[0]);
	}

	public InputStream getInputStream()
		throws IOException
	{
		if (!exists())
			throw new FileNotFoundException((new StringBuilder()).append(getPath()).append(" (no such file or directory)").toString());
		if (Files.isDirectory(path, new LinkOption[0]))
			throw new FileNotFoundException((new StringBuilder()).append(getPath()).append(" (is a directory)").toString());
		else
			return Files.newInputStream(path, new OpenOption[0]);
	}

	public boolean isWritable()
	{
		return Files.isWritable(path) && !Files.isDirectory(path, new LinkOption[0]);
	}

	public OutputStream getOutputStream()
		throws IOException
	{
		if (Files.isDirectory(path, new LinkOption[0]))
			throw new FileNotFoundException((new StringBuilder()).append(getPath()).append(" (is a directory)").toString());
		else
			return Files.newOutputStream(path, new OpenOption[0]);
	}

	public URL getURL()
		throws IOException
	{
		return path.toUri().toURL();
	}

	public URI getURI()
		throws IOException
	{
		return path.toUri();
	}

	public File getFile()
		throws IOException
	{
		return path.toFile();
		UnsupportedOperationException ex;
		ex;
		throw new FileNotFoundException((new StringBuilder()).append(path).append(" cannot be resolved to absolute file path").toString());
	}

	public long contentLength()
		throws IOException
	{
		return Files.size(path);
	}

	public long lastModified()
		throws IOException
	{
		return Files.getLastModifiedTime(path, new LinkOption[0]).toMillis();
	}

	public Resource createRelative(String relativePath)
		throws IOException
	{
		return new PathResource(path.resolve(relativePath));
	}

	public String getFilename()
	{
		return path.getFileName().toString();
	}

	public String getDescription()
	{
		return (new StringBuilder()).append("path [").append(path.toAbsolutePath()).append("]").toString();
	}

	public boolean equals(Object obj)
	{
		return this == obj || (obj instanceof PathResource) && path.equals(((PathResource)obj).path);
	}

	public int hashCode()
	{
		return path.hashCode();
	}
}
