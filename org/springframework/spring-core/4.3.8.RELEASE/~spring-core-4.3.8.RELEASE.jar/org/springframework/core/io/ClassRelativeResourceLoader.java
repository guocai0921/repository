// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ClassRelativeResourceLoader.java

package org.springframework.core.io;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

// Referenced classes of package org.springframework.core.io:
//			DefaultResourceLoader, Resource, ClassPathResource, ContextResource

public class ClassRelativeResourceLoader extends DefaultResourceLoader
{
	private static class ClassRelativeContextResource extends ClassPathResource
		implements ContextResource
	{

		private final Class clazz;

		public String getPathWithinContext()
		{
			return getPath();
		}

		public Resource createRelative(String relativePath)
		{
			String pathToUse = StringUtils.applyRelativePath(getPath(), relativePath);
			return new ClassRelativeContextResource(pathToUse, clazz);
		}

		public ClassRelativeContextResource(String path, Class clazz)
		{
			super(path, clazz);
			this.clazz = clazz;
		}
	}


	private final Class clazz;

	public ClassRelativeResourceLoader(Class clazz)
	{
		Assert.notNull(clazz, "Class must not be null");
		this.clazz = clazz;
		setClassLoader(clazz.getClassLoader());
	}

	protected Resource getResourceByPath(String path)
	{
		return new ClassRelativeContextResource(path, clazz);
	}
}
