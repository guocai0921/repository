// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Log4jConfigurer.java

package org.springframework.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;

// Referenced classes of package org.springframework.util:
//			ResourceUtils, SystemPropertyUtils

/**
 * @deprecated Class Log4jConfigurer is deprecated
 */

public abstract class Log4jConfigurer
{

	public static final String CLASSPATH_URL_PREFIX = "classpath:";
	public static final String XML_FILE_EXTENSION = ".xml";

	public Log4jConfigurer()
	{
	}

	public static void initLogging(String location)
		throws FileNotFoundException
	{
		String resolvedLocation = SystemPropertyUtils.resolvePlaceholders(location);
		URL url = ResourceUtils.getURL(resolvedLocation);
		if ("file".equals(url.getProtocol()) && !ResourceUtils.getFile(url).exists())
			throw new FileNotFoundException((new StringBuilder()).append("Log4j config file [").append(resolvedLocation).append("] not found").toString());
		if (resolvedLocation.toLowerCase().endsWith(".xml"))
			DOMConfigurator.configure(url);
		else
			PropertyConfigurator.configure(url);
	}

	public static void initLogging(String location, long refreshInterval)
		throws FileNotFoundException
	{
		String resolvedLocation = SystemPropertyUtils.resolvePlaceholders(location);
		File file = ResourceUtils.getFile(resolvedLocation);
		if (!file.exists())
			throw new FileNotFoundException((new StringBuilder()).append("Log4j config file [").append(resolvedLocation).append("] not found").toString());
		if (resolvedLocation.toLowerCase().endsWith(".xml"))
			DOMConfigurator.configureAndWatch(file.getAbsolutePath(), refreshInterval);
		else
			PropertyConfigurator.configureAndWatch(file.getAbsolutePath(), refreshInterval);
	}

	public static void shutdownLogging()
	{
		LogManager.shutdown();
	}

	public static void setWorkingDirSystemProperty(String key)
	{
		System.setProperty(key, (new File("")).getAbsolutePath());
	}
}
