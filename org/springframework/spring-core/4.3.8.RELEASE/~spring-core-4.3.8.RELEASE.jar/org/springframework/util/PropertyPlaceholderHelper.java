// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   PropertyPlaceholderHelper.java

package org.springframework.util;

import java.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

// Referenced classes of package org.springframework.util:
//			Assert, StringUtils

public class PropertyPlaceholderHelper
{
	public static interface PlaceholderResolver
	{

		public abstract String resolvePlaceholder(String s);
	}


	private static final Log logger = LogFactory.getLog(org/springframework/util/PropertyPlaceholderHelper);
	private static final Map wellKnownSimplePrefixes;
	private final String placeholderPrefix;
	private final String placeholderSuffix;
	private final String simplePrefix;
	private final String valueSeparator;
	private final boolean ignoreUnresolvablePlaceholders;

	public PropertyPlaceholderHelper(String placeholderPrefix, String placeholderSuffix)
	{
		this(placeholderPrefix, placeholderSuffix, null, true);
	}

	public PropertyPlaceholderHelper(String placeholderPrefix, String placeholderSuffix, String valueSeparator, boolean ignoreUnresolvablePlaceholders)
	{
		Assert.notNull(placeholderPrefix, "'placeholderPrefix' must not be null");
		Assert.notNull(placeholderSuffix, "'placeholderSuffix' must not be null");
		this.placeholderPrefix = placeholderPrefix;
		this.placeholderSuffix = placeholderSuffix;
		String simplePrefixForSuffix = (String)wellKnownSimplePrefixes.get(this.placeholderSuffix);
		if (simplePrefixForSuffix != null && this.placeholderPrefix.endsWith(simplePrefixForSuffix))
			simplePrefix = simplePrefixForSuffix;
		else
			simplePrefix = this.placeholderPrefix;
		this.valueSeparator = valueSeparator;
		this.ignoreUnresolvablePlaceholders = ignoreUnresolvablePlaceholders;
	}

	public String replacePlaceholders(String value, final Properties properties)
	{
		Assert.notNull(properties, "'properties' must not be null");
		return replacePlaceholders(value, new PlaceholderResolver() {

			final Properties val$properties;
			final PropertyPlaceholderHelper this$0;

			public String resolvePlaceholder(String placeholderName)
			{
				return properties.getProperty(placeholderName);
			}

			
			{
				this.this$0 = PropertyPlaceholderHelper.this;
				properties = properties1;
				super();
			}
		});
	}

	public String replacePlaceholders(String value, PlaceholderResolver placeholderResolver)
	{
		Assert.notNull(value, "'value' must not be null");
		return parseStringValue(value, placeholderResolver, new HashSet());
	}

	protected String parseStringValue(String value, PlaceholderResolver placeholderResolver, Set visitedPlaceholders)
	{
		StringBuilder result = new StringBuilder(value);
		for (int startIndex = value.indexOf(placeholderPrefix); startIndex != -1;)
		{
			int endIndex = findPlaceholderEndIndex(result, startIndex);
			if (endIndex != -1)
			{
				String placeholder = result.substring(startIndex + placeholderPrefix.length(), endIndex);
				String originalPlaceholder = placeholder;
				if (!visitedPlaceholders.add(originalPlaceholder))
					throw new IllegalArgumentException((new StringBuilder()).append("Circular placeholder reference '").append(originalPlaceholder).append("' in property definitions").toString());
				placeholder = parseStringValue(placeholder, placeholderResolver, visitedPlaceholders);
				String propVal = placeholderResolver.resolvePlaceholder(placeholder);
				if (propVal == null && valueSeparator != null)
				{
					int separatorIndex = placeholder.indexOf(valueSeparator);
					if (separatorIndex != -1)
					{
						String actualPlaceholder = placeholder.substring(0, separatorIndex);
						String defaultValue = placeholder.substring(separatorIndex + valueSeparator.length());
						propVal = placeholderResolver.resolvePlaceholder(actualPlaceholder);
						if (propVal == null)
							propVal = defaultValue;
					}
				}
				if (propVal != null)
				{
					propVal = parseStringValue(propVal, placeholderResolver, visitedPlaceholders);
					result.replace(startIndex, endIndex + placeholderSuffix.length(), propVal);
					if (logger.isTraceEnabled())
						logger.trace((new StringBuilder()).append("Resolved placeholder '").append(placeholder).append("'").toString());
					startIndex = result.indexOf(placeholderPrefix, startIndex + propVal.length());
				} else
				if (ignoreUnresolvablePlaceholders)
					startIndex = result.indexOf(placeholderPrefix, endIndex + placeholderSuffix.length());
				else
					throw new IllegalArgumentException((new StringBuilder()).append("Could not resolve placeholder '").append(placeholder).append("' in value \"").append(value).append("\"").toString());
				visitedPlaceholders.remove(originalPlaceholder);
			} else
			{
				startIndex = -1;
			}
		}

		return result.toString();
	}

	private int findPlaceholderEndIndex(CharSequence buf, int startIndex)
	{
		int index = startIndex + placeholderPrefix.length();
		int withinNestedPlaceholder = 0;
		while (index < buf.length()) 
			if (StringUtils.substringMatch(buf, index, placeholderSuffix))
			{
				if (withinNestedPlaceholder > 0)
				{
					withinNestedPlaceholder--;
					index += placeholderSuffix.length();
				} else
				{
					return index;
				}
			} else
			if (StringUtils.substringMatch(buf, index, simplePrefix))
			{
				withinNestedPlaceholder++;
				index += simplePrefix.length();
			} else
			{
				index++;
			}
		return -1;
	}

	static 
	{
		wellKnownSimplePrefixes = new HashMap(4);
		wellKnownSimplePrefixes.put("}", "{");
		wellKnownSimplePrefixes.put("]", "[");
		wellKnownSimplePrefixes.put(")", "(");
	}
}
