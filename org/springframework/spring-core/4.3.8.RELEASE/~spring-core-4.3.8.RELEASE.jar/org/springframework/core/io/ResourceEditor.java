// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ResourceEditor.java

package org.springframework.core.io;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.net.URL;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

// Referenced classes of package org.springframework.core.io:
//			DefaultResourceLoader, Resource, ResourceLoader

public class ResourceEditor extends PropertyEditorSupport
{

	private final ResourceLoader resourceLoader;
	private PropertyResolver propertyResolver;
	private final boolean ignoreUnresolvablePlaceholders;

	public ResourceEditor()
	{
		this(((ResourceLoader) (new DefaultResourceLoader())), null);
	}

	public ResourceEditor(ResourceLoader resourceLoader, PropertyResolver propertyResolver)
	{
		this(resourceLoader, propertyResolver, true);
	}

	public ResourceEditor(ResourceLoader resourceLoader, PropertyResolver propertyResolver, boolean ignoreUnresolvablePlaceholders)
	{
		Assert.notNull(resourceLoader, "ResourceLoader must not be null");
		this.resourceLoader = resourceLoader;
		this.propertyResolver = propertyResolver;
		this.ignoreUnresolvablePlaceholders = ignoreUnresolvablePlaceholders;
	}

	public void setAsText(String text)
	{
		if (StringUtils.hasText(text))
		{
			String locationToUse = resolvePath(text).trim();
			setValue(resourceLoader.getResource(locationToUse));
		} else
		{
			setValue(null);
		}
	}

	protected String resolvePath(String path)
	{
		if (propertyResolver == null)
			propertyResolver = new StandardEnvironment();
		return ignoreUnresolvablePlaceholders ? propertyResolver.resolvePlaceholders(path) : propertyResolver.resolveRequiredPlaceholders(path);
	}

	public String getAsText()
	{
		Resource value = (Resource)getValue();
		return value == null ? "" : value.getURL().toExternalForm();
		IOException ex;
		ex;
		return null;
	}
}
