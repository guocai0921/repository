// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ResourcePatternUtils.java

package org.springframework.core.io.support;

import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ResourceUtils;

// Referenced classes of package org.springframework.core.io.support:
//			ResourcePatternResolver, PathMatchingResourcePatternResolver

public abstract class ResourcePatternUtils
{

	public ResourcePatternUtils()
	{
	}

	public static boolean isUrl(String resourceLocation)
	{
		return resourceLocation != null && (resourceLocation.startsWith("classpath*:") || ResourceUtils.isUrl(resourceLocation));
	}

	public static ResourcePatternResolver getResourcePatternResolver(ResourceLoader resourceLoader)
	{
		if (resourceLoader instanceof ResourcePatternResolver)
			return (ResourcePatternResolver)resourceLoader;
		if (resourceLoader != null)
			return new PathMatchingResourcePatternResolver(resourceLoader);
		else
			return new PathMatchingResourcePatternResolver();
	}
}
