// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DefaultResourceLoader.java

package org.springframework.core.io;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import org.springframework.util.*;

// Referenced classes of package org.springframework.core.io:
//			ProtocolResolver, ResourceLoader, ClassPathResource, UrlResource, 
//			Resource, ContextResource

public class DefaultResourceLoader
	implements ResourceLoader
{
	protected static class ClassPathContextResource extends ClassPathResource
		implements ContextResource
	{

		public String getPathWithinContext()
		{
			return getPath();
		}

		public Resource createRelative(String relativePath)
		{
			String pathToUse = StringUtils.applyRelativePath(getPath(), relativePath);
			return new ClassPathContextResource(pathToUse, getClassLoader());
		}

		public ClassPathContextResource(String path, ClassLoader classLoader)
		{
			super(path, classLoader);
		}
	}


	private ClassLoader classLoader;
	private final Set protocolResolvers;

	public DefaultResourceLoader()
	{
		protocolResolvers = new LinkedHashSet(4);
		classLoader = ClassUtils.getDefaultClassLoader();
	}

	public DefaultResourceLoader(ClassLoader classLoader)
	{
		protocolResolvers = new LinkedHashSet(4);
		this.classLoader = classLoader;
	}

	public void setClassLoader(ClassLoader classLoader)
	{
		this.classLoader = classLoader;
	}

	public ClassLoader getClassLoader()
	{
		return classLoader == null ? ClassUtils.getDefaultClassLoader() : classLoader;
	}

	public void addProtocolResolver(ProtocolResolver resolver)
	{
		Assert.notNull(resolver, "ProtocolResolver must not be null");
		protocolResolvers.add(resolver);
	}

	public Collection getProtocolResolvers()
	{
		return protocolResolvers;
	}

	public Resource getResource(String location)
	{
		Assert.notNull(location, "Location must not be null");
		for (Iterator iterator = protocolResolvers.iterator(); iterator.hasNext();)
		{
			ProtocolResolver protocolResolver = (ProtocolResolver)iterator.next();
			Resource resource = protocolResolver.resolve(location, this);
			if (resource != null)
				return resource;
		}

		if (location.startsWith("/"))
			return getResourceByPath(location);
		if (location.startsWith("classpath:"))
			return new ClassPathResource(location.substring("classpath:".length()), getClassLoader());
		URL url = new URL(location);
		return new UrlResource(url);
		MalformedURLException ex;
		ex;
		return getResourceByPath(location);
	}

	protected Resource getResourceByPath(String path)
	{
		return new ClassPathContextResource(path, getClassLoader());
	}
}
