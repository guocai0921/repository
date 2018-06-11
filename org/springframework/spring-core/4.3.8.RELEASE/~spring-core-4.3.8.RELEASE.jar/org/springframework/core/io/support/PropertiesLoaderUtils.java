// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   PropertiesLoaderUtils.java

package org.springframework.core.io.support;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.Properties;
import org.springframework.core.io.Resource;
import org.springframework.util.*;

// Referenced classes of package org.springframework.core.io.support:
//			EncodedResource

public abstract class PropertiesLoaderUtils
{

	private static final String XML_FILE_EXTENSION = ".xml";

	public PropertiesLoaderUtils()
	{
	}

	public static Properties loadProperties(EncodedResource resource)
		throws IOException
	{
		Properties props = new Properties();
		fillProperties(props, resource);
		return props;
	}

	public static void fillProperties(Properties props, EncodedResource resource)
		throws IOException
	{
		fillProperties(props, resource, ((PropertiesPersister) (new DefaultPropertiesPersister())));
	}

	static void fillProperties(Properties props, EncodedResource resource, PropertiesPersister persister)
		throws IOException
	{
		InputStream stream;
		Reader reader;
		stream = null;
		reader = null;
		String filename = resource.getResource().getFilename();
		if (filename != null && filename.endsWith(".xml"))
		{
			stream = resource.getInputStream();
			persister.loadFromXml(props, stream);
		} else
		if (resource.requiresReader())
		{
			reader = resource.getReader();
			persister.load(props, reader);
		} else
		{
			stream = resource.getInputStream();
			persister.load(props, stream);
		}
		if (stream != null)
			stream.close();
		if (reader != null)
			reader.close();
		break MISSING_BLOCK_LABEL_129;
		Exception exception;
		exception;
		if (stream != null)
			stream.close();
		if (reader != null)
			reader.close();
		throw exception;
	}

	public static Properties loadProperties(Resource resource)
		throws IOException
	{
		Properties props = new Properties();
		fillProperties(props, resource);
		return props;
	}

	public static void fillProperties(Properties props, Resource resource)
		throws IOException
	{
		InputStream is = resource.getInputStream();
		String filename = resource.getFilename();
		if (filename != null && filename.endsWith(".xml"))
			props.loadFromXML(is);
		else
			props.load(is);
		is.close();
		break MISSING_BLOCK_LABEL_56;
		Exception exception;
		exception;
		is.close();
		throw exception;
	}

	public static Properties loadAllProperties(String resourceName)
		throws IOException
	{
		return loadAllProperties(resourceName, null);
	}

	public static Properties loadAllProperties(String resourceName, ClassLoader classLoader)
		throws IOException
	{
		Enumeration urls;
		Properties props;
		Assert.notNull(resourceName, "Resource name must not be null");
		ClassLoader classLoaderToUse = classLoader;
		if (classLoaderToUse == null)
			classLoaderToUse = ClassUtils.getDefaultClassLoader();
		urls = classLoaderToUse == null ? ClassLoader.getSystemResources(resourceName) : classLoaderToUse.getResources(resourceName);
		props = new Properties();
_L2:
		InputStream is;
		if (!urls.hasMoreElements())
			break; /* Loop/switch isn't completed */
		URL url = (URL)urls.nextElement();
		URLConnection con = url.openConnection();
		ResourceUtils.useCachesIfNecessary(con);
		is = con.getInputStream();
		if (resourceName.endsWith(".xml"))
			props.loadFromXML(is);
		else
			props.load(is);
		is.close();
		if (true) goto _L2; else goto _L1
		Exception exception;
		exception;
		is.close();
		throw exception;
_L1:
		return props;
	}
}
