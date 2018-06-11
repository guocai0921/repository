// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   PropertiesLoaderSupport.java

package org.springframework.core.io.support;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.util.*;

// Referenced classes of package org.springframework.core.io.support:
//			EncodedResource, PropertiesLoaderUtils

public abstract class PropertiesLoaderSupport
{

	protected final Log logger = LogFactory.getLog(getClass());
	protected Properties localProperties[];
	protected boolean localOverride;
	private Resource locations[];
	private boolean ignoreResourceNotFound;
	private String fileEncoding;
	private PropertiesPersister propertiesPersister;

	public PropertiesLoaderSupport()
	{
		localOverride = false;
		ignoreResourceNotFound = false;
		propertiesPersister = new DefaultPropertiesPersister();
	}

	public void setProperties(Properties properties)
	{
		localProperties = (new Properties[] {
			properties
		});
	}

	public transient void setPropertiesArray(Properties propertiesArray[])
	{
		localProperties = propertiesArray;
	}

	public void setLocation(Resource location)
	{
		locations = (new Resource[] {
			location
		});
	}

	public transient void setLocations(Resource locations[])
	{
		this.locations = locations;
	}

	public void setLocalOverride(boolean localOverride)
	{
		this.localOverride = localOverride;
	}

	public void setIgnoreResourceNotFound(boolean ignoreResourceNotFound)
	{
		this.ignoreResourceNotFound = ignoreResourceNotFound;
	}

	public void setFileEncoding(String encoding)
	{
		fileEncoding = encoding;
	}

	public void setPropertiesPersister(PropertiesPersister propertiesPersister)
	{
		this.propertiesPersister = ((PropertiesPersister) (propertiesPersister == null ? ((PropertiesPersister) (new DefaultPropertiesPersister())) : propertiesPersister));
	}

	protected Properties mergeProperties()
		throws IOException
	{
		Properties result = new Properties();
		if (localOverride)
			loadProperties(result);
		if (localProperties != null)
		{
			Properties aproperties[] = localProperties;
			int i = aproperties.length;
			for (int j = 0; j < i; j++)
			{
				Properties localProp = aproperties[j];
				CollectionUtils.mergePropertiesIntoMap(localProp, result);
			}

		}
		if (!localOverride)
			loadProperties(result);
		return result;
	}

	protected void loadProperties(Properties props)
		throws IOException
	{
		if (locations != null)
		{
			Resource aresource[] = locations;
			int i = aresource.length;
			for (int j = 0; j < i; j++)
			{
				Resource location = aresource[j];
				if (logger.isDebugEnabled())
					logger.debug((new StringBuilder()).append("Loading properties file from ").append(location).toString());
				try
				{
					PropertiesLoaderUtils.fillProperties(props, new EncodedResource(location, fileEncoding), propertiesPersister);
					continue;
				}
				catch (IOException ex)
				{
					if (ignoreResourceNotFound && ((ex instanceof FileNotFoundException) || (ex instanceof UnknownHostException)))
					{
						if (logger.isInfoEnabled())
							logger.info((new StringBuilder()).append("Properties resource not found: ").append(ex.getMessage()).toString());
					} else
					{
						throw ex;
					}
				}
			}

		}
	}
}
