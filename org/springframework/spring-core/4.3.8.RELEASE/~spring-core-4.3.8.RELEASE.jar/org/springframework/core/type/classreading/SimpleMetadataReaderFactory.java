// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SimpleMetadataReaderFactory.java

package org.springframework.core.type.classreading;

import java.io.IOException;
import org.springframework.core.io.*;
import org.springframework.util.ClassUtils;

// Referenced classes of package org.springframework.core.type.classreading:
//			SimpleMetadataReader, MetadataReaderFactory, MetadataReader

public class SimpleMetadataReaderFactory
	implements MetadataReaderFactory
{

	private final ResourceLoader resourceLoader;

	public SimpleMetadataReaderFactory()
	{
		resourceLoader = new DefaultResourceLoader();
	}

	public SimpleMetadataReaderFactory(ResourceLoader resourceLoader)
	{
		this.resourceLoader = ((ResourceLoader) (resourceLoader == null ? ((ResourceLoader) (new DefaultResourceLoader())) : resourceLoader));
	}

	public SimpleMetadataReaderFactory(ClassLoader classLoader)
	{
		resourceLoader = classLoader == null ? ((ResourceLoader) (new DefaultResourceLoader())) : ((ResourceLoader) (new DefaultResourceLoader(classLoader)));
	}

	public final ResourceLoader getResourceLoader()
	{
		return resourceLoader;
	}

	public MetadataReader getMetadataReader(String className)
		throws IOException
	{
		String resourcePath = (new StringBuilder()).append("classpath:").append(ClassUtils.convertClassNameToResourcePath(className)).append(".class").toString();
		Resource resource = resourceLoader.getResource(resourcePath);
		if (!resource.exists())
		{
			int lastDotIndex = className.lastIndexOf('.');
			if (lastDotIndex != -1)
			{
				String innerClassName = (new StringBuilder()).append(className.substring(0, lastDotIndex)).append('$').append(className.substring(lastDotIndex + 1)).toString();
				String innerClassResourcePath = (new StringBuilder()).append("classpath:").append(ClassUtils.convertClassNameToResourcePath(innerClassName)).append(".class").toString();
				Resource innerClassResource = resourceLoader.getResource(innerClassResourcePath);
				if (innerClassResource.exists())
					resource = innerClassResource;
			}
		}
		return getMetadataReader(resource);
	}

	public MetadataReader getMetadataReader(Resource resource)
		throws IOException
	{
		return new SimpleMetadataReader(resource, resourceLoader.getClassLoader());
	}
}
