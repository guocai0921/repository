// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CompositePropertySource.java

package org.springframework.core.env;

import java.util.*;
import org.springframework.util.StringUtils;

// Referenced classes of package org.springframework.core.env:
//			EnumerablePropertySource, PropertySource

public class CompositePropertySource extends EnumerablePropertySource
{

	private final Set propertySources = new LinkedHashSet();

	public CompositePropertySource(String name)
	{
		super(name);
	}

	public Object getProperty(String name)
	{
		for (Iterator iterator = propertySources.iterator(); iterator.hasNext();)
		{
			PropertySource propertySource = (PropertySource)iterator.next();
			Object candidate = propertySource.getProperty(name);
			if (candidate != null)
				return candidate;
		}

		return null;
	}

	public boolean containsProperty(String name)
	{
		for (Iterator iterator = propertySources.iterator(); iterator.hasNext();)
		{
			PropertySource propertySource = (PropertySource)iterator.next();
			if (propertySource.containsProperty(name))
				return true;
		}

		return false;
	}

	public String[] getPropertyNames()
	{
		Set names = new LinkedHashSet();
		PropertySource propertySource;
		for (Iterator iterator = propertySources.iterator(); iterator.hasNext(); names.addAll(Arrays.asList(((EnumerablePropertySource)propertySource).getPropertyNames())))
		{
			propertySource = (PropertySource)iterator.next();
			if (!(propertySource instanceof EnumerablePropertySource))
				throw new IllegalStateException((new StringBuilder()).append("Failed to enumerate property names due to non-enumerable property source: ").append(propertySource).toString());
		}

		return StringUtils.toStringArray(names);
	}

	public void addPropertySource(PropertySource propertySource)
	{
		propertySources.add(propertySource);
	}

	public void addFirstPropertySource(PropertySource propertySource)
	{
		java.util.List existing = new ArrayList(propertySources);
		propertySources.clear();
		propertySources.add(propertySource);
		propertySources.addAll(existing);
	}

	public Collection getPropertySources()
	{
		return propertySources;
	}

	public String toString()
	{
		return String.format("%s [name='%s', propertySources=%s]", new Object[] {
			getClass().getSimpleName(), name, propertySources
		});
	}
}
