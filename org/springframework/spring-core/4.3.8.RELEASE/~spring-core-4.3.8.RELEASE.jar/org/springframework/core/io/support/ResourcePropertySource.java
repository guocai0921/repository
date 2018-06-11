// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ResourcePropertySource.java

package org.springframework.core.io.support;

import java.io.IOException;
import java.util.Map;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

// Referenced classes of package org.springframework.core.io.support:
//			EncodedResource, PropertiesLoaderUtils

public class ResourcePropertySource extends PropertiesPropertySource
{

	private final String resourceName;

	public ResourcePropertySource(String name, EncodedResource resource)
		throws IOException
	{
		super(name, PropertiesLoaderUtils.loadProperties(resource));
		resourceName = getNameForResource(resource.getResource());
	}

	public ResourcePropertySource(EncodedResource resource)
		throws IOException
	{
		super(getNameForResource(resource.getResource()), PropertiesLoaderUtils.loadProperties(resource));
		resourceName = null;
	}

	public ResourcePropertySource(String name, Resource resource)
		throws IOException
	{
		super(name, PropertiesLoaderUtils.loadProperties(new EncodedResource(resource)));
		resourceName = getNameForResource(resource);
	}

	public ResourcePropertySource(Resource resource)
		throws IOException
	{
		super(getNameForResource(resource), PropertiesLoaderUtils.loadProperties(new EncodedResource(resource)));
		resourceName = null;
	}

	public ResourcePropertySource(String name, String location, ClassLoader classLoader)
		throws IOException
	{
		this(name, (new DefaultResourceLoader(classLoader)).getResource(location));
	}

	public ResourcePropertySource(String location, ClassLoader classLoader)
		throws IOException
	{
		this((new DefaultResourceLoader(classLoader)).getResource(location));
	}

	public ResourcePropertySource(String name, String location)
		throws IOException
	{
		this(name, (new DefaultResourceLoader()).getResource(location));
	}

	public ResourcePropertySource(String location)
		throws IOException
	{
		this((new DefaultResourceLoader()).getResource(location));
	}

	private ResourcePropertySource(String name, String resourceName, Map source)
	{
		super(name, source);
		this.resourceName = resourceName;
	}

	public ResourcePropertySource withName(String name)
	{
		if (this.name.equals(name))
			return this;
		if (resourceName != null)
		{
			if (resourceName.equals(name))
				return new ResourcePropertySource(resourceName, null, (Map)source);
			else
				return new ResourcePropertySource(name, resourceName, (Map)source);
		} else
		{
			return new ResourcePropertySource(name, this.name, (Map)source);
		}
	}

	public ResourcePropertySource withResourceName()
	{
		if (resourceName == null)
			return this;
		else
			return new ResourcePropertySource(resourceName, null, (Map)source);
	}

	private static String getNameForResource(Resource resource)
	{
		String name = resource.getDescription();
		if (!StringUtils.hasText(name))
			name = (new StringBuilder()).append(resource.getClass().getSimpleName()).append("@").append(System.identityHashCode(resource)).toString();
		return name;
	}
}
