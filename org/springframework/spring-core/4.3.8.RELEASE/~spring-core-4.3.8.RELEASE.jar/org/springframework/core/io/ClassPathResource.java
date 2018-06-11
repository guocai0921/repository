// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ClassPathResource.java

package org.springframework.core.io;

import java.io.*;
import java.net.URL;
import org.springframework.util.*;

// Referenced classes of package org.springframework.core.io:
//			AbstractFileResolvingResource, Resource

public class ClassPathResource extends AbstractFileResolvingResource
{

	private final String path;
	private ClassLoader classLoader;
	private Class clazz;

	public ClassPathResource(String path)
	{
		this(path, (ClassLoader)null);
	}

	public ClassPathResource(String path, ClassLoader classLoader)
	{
		Assert.notNull(path, "Path must not be null");
		String pathToUse = StringUtils.cleanPath(path);
		if (pathToUse.startsWith("/"))
			pathToUse = pathToUse.substring(1);
		this.path = pathToUse;
		this.classLoader = classLoader == null ? ClassUtils.getDefaultClassLoader() : classLoader;
	}

	public ClassPathResource(String path, Class clazz)
	{
		Assert.notNull(path, "Path must not be null");
		this.path = StringUtils.cleanPath(path);
		this.clazz = clazz;
	}

	protected ClassPathResource(String path, ClassLoader classLoader, Class clazz)
	{
		this.path = StringUtils.cleanPath(path);
		this.classLoader = classLoader;
		this.clazz = clazz;
	}

	public final String getPath()
	{
		return path;
	}

	public final ClassLoader getClassLoader()
	{
		return clazz == null ? classLoader : clazz.getClassLoader();
	}

	public boolean exists()
	{
		return resolveURL() != null;
	}

	protected URL resolveURL()
	{
		if (clazz != null)
			return clazz.getResource(path);
		if (classLoader != null)
			return classLoader.getResource(path);
		else
			return ClassLoader.getSystemResource(path);
	}

	public InputStream getInputStream()
		throws IOException
	{
		InputStream is;
		if (clazz != null)
			is = clazz.getResourceAsStream(path);
		else
		if (classLoader != null)
			is = classLoader.getResourceAsStream(path);
		else
			is = ClassLoader.getSystemResourceAsStream(path);
		if (is == null)
			throw new FileNotFoundException((new StringBuilder()).append(getDescription()).append(" cannot be opened because it does not exist").toString());
		else
			return is;
	}

	public URL getURL()
		throws IOException
	{
		URL url = resolveURL();
		if (url == null)
			throw new FileNotFoundException((new StringBuilder()).append(getDescription()).append(" cannot be resolved to URL because it does not exist").toString());
		else
			return url;
	}

	public Resource createRelative(String relativePath)
	{
		String pathToUse = StringUtils.applyRelativePath(path, relativePath);
		return new ClassPathResource(pathToUse, classLoader, clazz);
	}

	public String getFilename()
	{
		return StringUtils.getFilename(path);
	}

	public String getDescription()
	{
		StringBuilder builder = new StringBuilder("class path resource [");
		String pathToUse = path;
		if (clazz != null && !pathToUse.startsWith("/"))
		{
			builder.append(ClassUtils.classPackageAsResourcePath(clazz));
			builder.append('/');
		}
		if (pathToUse.startsWith("/"))
			pathToUse = pathToUse.substring(1);
		builder.append(pathToUse);
		builder.append(']');
		return builder.toString();
	}

	public boolean equals(Object obj)
	{
		if (obj == this)
			return true;
		if (obj instanceof ClassPathResource)
		{
			ClassPathResource otherRes = (ClassPathResource)obj;
			return path.equals(otherRes.path) && ObjectUtils.nullSafeEquals(classLoader, otherRes.classLoader) && ObjectUtils.nullSafeEquals(clazz, otherRes.clazz);
		} else
		{
			return false;
		}
	}

	public int hashCode()
	{
		return path.hashCode();
	}
}
