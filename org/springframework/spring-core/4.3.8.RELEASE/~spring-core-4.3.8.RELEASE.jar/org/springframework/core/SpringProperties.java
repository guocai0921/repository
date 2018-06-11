// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SpringProperties.java

package org.springframework.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class SpringProperties
{

	private static final String PROPERTIES_RESOURCE_LOCATION = "spring.properties";
	private static final Log logger;
	private static final Properties localProperties;

	public SpringProperties()
	{
	}

	public static void setProperty(String key, String value)
	{
		if (value != null)
			localProperties.setProperty(key, value);
		else
			localProperties.remove(key);
	}

	public static String getProperty(String key)
	{
		String value = localProperties.getProperty(key);
		if (value == null)
			try
			{
				value = System.getProperty(key);
			}
			catch (Throwable ex)
			{
				if (logger.isDebugEnabled())
					logger.debug((new StringBuilder()).append("Could not retrieve system property '").append(key).append("': ").append(ex).toString());
			}
		return value;
	}

	public static void setFlag(String key)
	{
		localProperties.put(key, Boolean.TRUE.toString());
	}

	public static boolean getFlag(String key)
	{
		return Boolean.parseBoolean(getProperty(key));
	}

	static 
	{
		logger = LogFactory.getLog(org/springframework/core/SpringProperties);
		localProperties = new Properties();
		InputStream is;
		ClassLoader cl = org/springframework/core/SpringProperties.getClassLoader();
		URL url = cl == null ? ClassLoader.getSystemResource("spring.properties") : cl.getResource("spring.properties");
		if (url == null)
			break MISSING_BLOCK_LABEL_125;
		logger.info("Found 'spring.properties' file in local classpath");
		is = url.openStream();
		localProperties.load(is);
		is.close();
		break MISSING_BLOCK_LABEL_125;
		Exception exception;
		exception;
		is.close();
		throw exception;
		IOException ex;
		ex;
		if (logger.isInfoEnabled())
			logger.info((new StringBuilder()).append("Could not load 'spring.properties' file from local classpath: ").append(ex).toString());
	}
}
