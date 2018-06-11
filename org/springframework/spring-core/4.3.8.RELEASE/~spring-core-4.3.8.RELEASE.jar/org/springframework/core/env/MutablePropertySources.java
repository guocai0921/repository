// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   MutablePropertySources.java

package org.springframework.core.env;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

// Referenced classes of package org.springframework.core.env:
//			PropertySource, PropertySources

public class MutablePropertySources
	implements PropertySources
{

	private final Log logger;
	private final List propertySourceList;

	public MutablePropertySources()
	{
		propertySourceList = new CopyOnWriteArrayList();
		logger = LogFactory.getLog(getClass());
	}

	public MutablePropertySources(PropertySources propertySources)
	{
		this();
		PropertySource propertySource;
		for (Iterator iterator1 = propertySources.iterator(); iterator1.hasNext(); addLast(propertySource))
			propertySource = (PropertySource)iterator1.next();

	}

	MutablePropertySources(Log logger)
	{
		propertySourceList = new CopyOnWriteArrayList();
		this.logger = logger;
	}

	public boolean contains(String name)
	{
		return propertySourceList.contains(PropertySource.named(name));
	}

	public PropertySource get(String name)
	{
		int index = propertySourceList.indexOf(PropertySource.named(name));
		return index == -1 ? null : (PropertySource)propertySourceList.get(index);
	}

	public Iterator iterator()
	{
		return propertySourceList.iterator();
	}

	public void addFirst(PropertySource propertySource)
	{
		if (logger.isDebugEnabled())
			logger.debug(String.format("Adding [%s] PropertySource with highest search precedence", new Object[] {
				propertySource.getName()
			}));
		removeIfPresent(propertySource);
		propertySourceList.add(0, propertySource);
	}

	public void addLast(PropertySource propertySource)
	{
		if (logger.isDebugEnabled())
			logger.debug(String.format("Adding [%s] PropertySource with lowest search precedence", new Object[] {
				propertySource.getName()
			}));
		removeIfPresent(propertySource);
		propertySourceList.add(propertySource);
	}

	public void addBefore(String relativePropertySourceName, PropertySource propertySource)
	{
		if (logger.isDebugEnabled())
			logger.debug(String.format("Adding [%s] PropertySource with search precedence immediately higher than [%s]", new Object[] {
				propertySource.getName(), relativePropertySourceName
			}));
		assertLegalRelativeAddition(relativePropertySourceName, propertySource);
		removeIfPresent(propertySource);
		int index = assertPresentAndGetIndex(relativePropertySourceName);
		addAtIndex(index, propertySource);
	}

	public void addAfter(String relativePropertySourceName, PropertySource propertySource)
	{
		if (logger.isDebugEnabled())
			logger.debug(String.format("Adding [%s] PropertySource with search precedence immediately lower than [%s]", new Object[] {
				propertySource.getName(), relativePropertySourceName
			}));
		assertLegalRelativeAddition(relativePropertySourceName, propertySource);
		removeIfPresent(propertySource);
		int index = assertPresentAndGetIndex(relativePropertySourceName);
		addAtIndex(index + 1, propertySource);
	}

	public int precedenceOf(PropertySource propertySource)
	{
		return propertySourceList.indexOf(propertySource);
	}

	public PropertySource remove(String name)
	{
		if (logger.isDebugEnabled())
			logger.debug(String.format("Removing [%s] PropertySource", new Object[] {
				name
			}));
		int index = propertySourceList.indexOf(PropertySource.named(name));
		return index == -1 ? null : (PropertySource)propertySourceList.remove(index);
	}

	public void replace(String name, PropertySource propertySource)
	{
		if (logger.isDebugEnabled())
			logger.debug(String.format("Replacing [%s] PropertySource with [%s]", new Object[] {
				name, propertySource.getName()
			}));
		int index = assertPresentAndGetIndex(name);
		propertySourceList.set(index, propertySource);
	}

	public int size()
	{
		return propertySourceList.size();
	}

	public String toString()
	{
		String names[] = new String[size()];
		for (int i = 0; i < size(); i++)
			names[i] = ((PropertySource)propertySourceList.get(i)).getName();

		return String.format("[%s]", new Object[] {
			StringUtils.arrayToCommaDelimitedString(names)
		});
	}

	protected void assertLegalRelativeAddition(String relativePropertySourceName, PropertySource propertySource)
	{
		String newPropertySourceName = propertySource.getName();
		if (relativePropertySourceName.equals(newPropertySourceName))
			throw new IllegalArgumentException(String.format("PropertySource named [%s] cannot be added relative to itself", new Object[] {
				newPropertySourceName
			}));
		else
			return;
	}

	protected void removeIfPresent(PropertySource propertySource)
	{
		propertySourceList.remove(propertySource);
	}

	private void addAtIndex(int index, PropertySource propertySource)
	{
		removeIfPresent(propertySource);
		propertySourceList.add(index, propertySource);
	}

	private int assertPresentAndGetIndex(String name)
	{
		int index = propertySourceList.indexOf(PropertySource.named(name));
		if (index == -1)
			throw new IllegalArgumentException(String.format("PropertySource named [%s] does not exist", new Object[] {
				name
			}));
		else
			return index;
	}
}
