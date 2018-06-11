// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   LocalizedResourceHelper.java

package org.springframework.core.io.support;

import java.util.Locale;
import org.springframework.core.io.*;
import org.springframework.util.Assert;

public class LocalizedResourceHelper
{

	public static final String DEFAULT_SEPARATOR = "_";
	private final ResourceLoader resourceLoader;
	private String separator;

	public LocalizedResourceHelper()
	{
		separator = "_";
		resourceLoader = new DefaultResourceLoader();
	}

	public LocalizedResourceHelper(ResourceLoader resourceLoader)
	{
		separator = "_";
		Assert.notNull(resourceLoader, "ResourceLoader must not be null");
		this.resourceLoader = resourceLoader;
	}

	public void setSeparator(String separator)
	{
		this.separator = separator == null ? "_" : separator;
	}

	public Resource findLocalizedResource(String name, String extension, Locale locale)
	{
		Assert.notNull(name, "Name must not be null");
		Assert.notNull(extension, "Extension must not be null");
		Resource resource = null;
		if (locale != null)
		{
			String lang = locale.getLanguage();
			String country = locale.getCountry();
			String variant = locale.getVariant();
			if (variant.length() > 0)
			{
				String location = (new StringBuilder()).append(name).append(separator).append(lang).append(separator).append(country).append(separator).append(variant).append(extension).toString();
				resource = resourceLoader.getResource(location);
			}
			if ((resource == null || !resource.exists()) && country.length() > 0)
			{
				String location = (new StringBuilder()).append(name).append(separator).append(lang).append(separator).append(country).append(extension).toString();
				resource = resourceLoader.getResource(location);
			}
			if ((resource == null || !resource.exists()) && lang.length() > 0)
			{
				String location = (new StringBuilder()).append(name).append(separator).append(lang).append(extension).toString();
				resource = resourceLoader.getResource(location);
			}
		}
		if (resource == null || !resource.exists())
		{
			String location = (new StringBuilder()).append(name).append(extension).toString();
			resource = resourceLoader.getResource(location);
		}
		return resource;
	}
}
