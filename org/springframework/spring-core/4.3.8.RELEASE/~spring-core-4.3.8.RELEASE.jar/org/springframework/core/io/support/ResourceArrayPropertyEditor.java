// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ResourceArrayPropertyEditor.java

package org.springframework.core.io.support;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

// Referenced classes of package org.springframework.core.io.support:
//			PathMatchingResourcePatternResolver, ResourcePatternResolver

public class ResourceArrayPropertyEditor extends PropertyEditorSupport
{

	private static final Log logger = LogFactory.getLog(org/springframework/core/io/support/ResourceArrayPropertyEditor);
	private final ResourcePatternResolver resourcePatternResolver;
	private PropertyResolver propertyResolver;
	private final boolean ignoreUnresolvablePlaceholders;

	public ResourceArrayPropertyEditor()
	{
		this(((ResourcePatternResolver) (new PathMatchingResourcePatternResolver())), null, true);
	}

	public ResourceArrayPropertyEditor(ResourcePatternResolver resourcePatternResolver, PropertyResolver propertyResolver)
	{
		this(resourcePatternResolver, propertyResolver, true);
	}

	public ResourceArrayPropertyEditor(ResourcePatternResolver resourcePatternResolver, PropertyResolver propertyResolver, boolean ignoreUnresolvablePlaceholders)
	{
		Assert.notNull(resourcePatternResolver, "ResourcePatternResolver must not be null");
		this.resourcePatternResolver = resourcePatternResolver;
		this.propertyResolver = propertyResolver;
		this.ignoreUnresolvablePlaceholders = ignoreUnresolvablePlaceholders;
	}

	public void setAsText(String text)
	{
		String pattern = resolvePath(text).trim();
		try
		{
			setValue(resourcePatternResolver.getResources(pattern));
		}
		catch (IOException ex)
		{
			throw new IllegalArgumentException((new StringBuilder()).append("Could not resolve resource location pattern [").append(pattern).append("]: ").append(ex.getMessage()).toString());
		}
	}

	public void setValue(Object value)
		throws IllegalArgumentException
	{
		if ((value instanceof Collection) || (value instanceof Object[]) && !(value instanceof Resource[]))
		{
			Collection input = ((Collection) ((value instanceof Collection) ? (Collection)value : ((Collection) (Arrays.asList((Object[])(Object[])value)))));
			List merged = new ArrayList();
			for (Iterator iterator = input.iterator(); iterator.hasNext();)
			{
				Object element = iterator.next();
				if (element instanceof String)
				{
					String pattern = resolvePath((String)element).trim();
					try
					{
						Resource resources[] = resourcePatternResolver.getResources(pattern);
						Resource aresource[] = resources;
						int i = aresource.length;
						int j = 0;
						while (j < i) 
						{
							Resource resource = aresource[j];
							if (!merged.contains(resource))
								merged.add(resource);
							j++;
						}
					}
					catch (IOException ex)
					{
						if (logger.isDebugEnabled())
							logger.debug((new StringBuilder()).append("Could not retrieve resources for pattern '").append(pattern).append("'").toString(), ex);
					}
				} else
				if (element instanceof Resource)
				{
					Resource resource = (Resource)element;
					if (!merged.contains(resource))
						merged.add(resource);
				} else
				{
					throw new IllegalArgumentException((new StringBuilder()).append("Cannot convert element [").append(element).append("] to [").append(org/springframework/core/io/Resource.getName()).append("]: only location String and Resource object supported").toString());
				}
			}

			super.setValue(((Object) (merged.toArray(new Resource[merged.size()]))));
		} else
		{
			super.setValue(value);
		}
	}

	protected String resolvePath(String path)
	{
		if (propertyResolver == null)
			propertyResolver = new StandardEnvironment();
		return ignoreUnresolvablePlaceholders ? propertyResolver.resolvePlaceholders(path) : propertyResolver.resolveRequiredPlaceholders(path);
	}

}
