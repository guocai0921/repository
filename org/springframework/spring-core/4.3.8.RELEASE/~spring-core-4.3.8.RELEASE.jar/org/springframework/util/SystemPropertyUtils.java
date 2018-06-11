// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SystemPropertyUtils.java

package org.springframework.util;

import java.io.PrintStream;

// Referenced classes of package org.springframework.util:
//			PropertyPlaceholderHelper

public abstract class SystemPropertyUtils
{
	private static class SystemPropertyPlaceholderResolver
		implements PropertyPlaceholderHelper.PlaceholderResolver
	{

		private final String text;

		public String resolvePlaceholder(String placeholderName)
		{
			String propVal;
			propVal = System.getProperty(placeholderName);
			if (propVal == null)
				propVal = System.getenv(placeholderName);
			return propVal;
			Throwable ex;
			ex;
			System.err.println((new StringBuilder()).append("Could not resolve placeholder '").append(placeholderName).append("' in [").append(text).append("] as system property: ").append(ex).toString());
			return null;
		}

		public SystemPropertyPlaceholderResolver(String text)
		{
			this.text = text;
		}
	}


	public static final String PLACEHOLDER_PREFIX = "${";
	public static final String PLACEHOLDER_SUFFIX = "}";
	public static final String VALUE_SEPARATOR = ":";
	private static final PropertyPlaceholderHelper strictHelper = new PropertyPlaceholderHelper("${", "}", ":", false);
	private static final PropertyPlaceholderHelper nonStrictHelper = new PropertyPlaceholderHelper("${", "}", ":", true);

	public SystemPropertyUtils()
	{
	}

	public static String resolvePlaceholders(String text)
	{
		return resolvePlaceholders(text, false);
	}

	public static String resolvePlaceholders(String text, boolean ignoreUnresolvablePlaceholders)
	{
		PropertyPlaceholderHelper helper = ignoreUnresolvablePlaceholders ? nonStrictHelper : strictHelper;
		return helper.replacePlaceholders(text, new SystemPropertyPlaceholderResolver(text));
	}

}
