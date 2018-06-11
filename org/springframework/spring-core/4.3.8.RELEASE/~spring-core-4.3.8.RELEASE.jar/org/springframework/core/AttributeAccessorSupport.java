// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AttributeAccessorSupport.java

package org.springframework.core;

import java.io.Serializable;
import java.util.*;
import org.springframework.util.Assert;

// Referenced classes of package org.springframework.core:
//			AttributeAccessor

public abstract class AttributeAccessorSupport
	implements AttributeAccessor, Serializable
{

	private final Map attributes = new LinkedHashMap(0);

	public AttributeAccessorSupport()
	{
	}

	public void setAttribute(String name, Object value)
	{
		Assert.notNull(name, "Name must not be null");
		if (value != null)
			attributes.put(name, value);
		else
			removeAttribute(name);
	}

	public Object getAttribute(String name)
	{
		Assert.notNull(name, "Name must not be null");
		return attributes.get(name);
	}

	public Object removeAttribute(String name)
	{
		Assert.notNull(name, "Name must not be null");
		return attributes.remove(name);
	}

	public boolean hasAttribute(String name)
	{
		Assert.notNull(name, "Name must not be null");
		return attributes.containsKey(name);
	}

	public String[] attributeNames()
	{
		return (String[])attributes.keySet().toArray(new String[attributes.size()]);
	}

	protected void copyAttributesFrom(AttributeAccessor source)
	{
		Assert.notNull(source, "Source must not be null");
		String attributeNames[] = source.attributeNames();
		String as[] = attributeNames;
		int i = as.length;
		for (int j = 0; j < i; j++)
		{
			String attributeName = as[j];
			setAttribute(attributeName, source.getAttribute(attributeName));
		}

	}

	public boolean equals(Object other)
	{
		if (this == other)
			return true;
		if (!(other instanceof AttributeAccessorSupport))
		{
			return false;
		} else
		{
			AttributeAccessorSupport that = (AttributeAccessorSupport)other;
			return attributes.equals(that.attributes);
		}
	}

	public int hashCode()
	{
		return attributes.hashCode();
	}
}
