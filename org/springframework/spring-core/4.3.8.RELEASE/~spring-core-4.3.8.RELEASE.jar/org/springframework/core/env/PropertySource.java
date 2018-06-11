// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   PropertySource.java

package org.springframework.core.env;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

public abstract class PropertySource
{
	static class ComparisonPropertySource extends StubPropertySource
	{

		private static final String USAGE_ERROR = "ComparisonPropertySource instances are for use with collection comparison only";

		public Object getSource()
		{
			throw new UnsupportedOperationException("ComparisonPropertySource instances are for use with collection comparison only");
		}

		public boolean containsProperty(String name)
		{
			throw new UnsupportedOperationException("ComparisonPropertySource instances are for use with collection comparison only");
		}

		public String getProperty(String name)
		{
			throw new UnsupportedOperationException("ComparisonPropertySource instances are for use with collection comparison only");
		}

		public String toString()
		{
			return String.format("%s [name='%s']", new Object[] {
				getClass().getSimpleName(), name
			});
		}

		public volatile Object getProperty(String s)
		{
			return getProperty(s);
		}

		public ComparisonPropertySource(String name)
		{
			super(name);
		}
	}

	public static class StubPropertySource extends PropertySource
	{

		public String getProperty(String name)
		{
			return null;
		}

		public volatile Object getProperty(String s)
		{
			return getProperty(s);
		}

		public StubPropertySource(String name)
		{
			super(name, new Object());
		}
	}


	protected final Log logger;
	protected final String name;
	protected final Object source;

	public PropertySource(String name, Object source)
	{
		logger = LogFactory.getLog(getClass());
		Assert.hasText(name, "Property source name must contain at least one character");
		Assert.notNull(source, "Property source must not be null");
		this.name = name;
		this.source = source;
	}

	public PropertySource(String name)
	{
		this(name, new Object());
	}

	public String getName()
	{
		return name;
	}

	public Object getSource()
	{
		return source;
	}

	public boolean containsProperty(String name)
	{
		return getProperty(name) != null;
	}

	public abstract Object getProperty(String s);

	public boolean equals(Object obj)
	{
		return this == obj || (obj instanceof PropertySource) && ObjectUtils.nullSafeEquals(name, ((PropertySource)obj).name);
	}

	public int hashCode()
	{
		return ObjectUtils.nullSafeHashCode(name);
	}

	public String toString()
	{
		if (logger.isDebugEnabled())
			return String.format("%s@%s [name='%s', properties=%s]", new Object[] {
				getClass().getSimpleName(), Integer.valueOf(System.identityHashCode(this)), name, source
			});
		else
			return String.format("%s [name='%s']", new Object[] {
				getClass().getSimpleName(), name
			});
	}

	public static PropertySource named(String name)
	{
		return new ComparisonPropertySource(name);
	}
}
