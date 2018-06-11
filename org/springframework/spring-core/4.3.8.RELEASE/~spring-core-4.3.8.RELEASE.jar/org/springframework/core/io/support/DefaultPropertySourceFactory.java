// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DefaultPropertySourceFactory.java

package org.springframework.core.io.support;

import java.io.IOException;
import org.springframework.core.env.PropertySource;

// Referenced classes of package org.springframework.core.io.support:
//			ResourcePropertySource, PropertySourceFactory, EncodedResource

public class DefaultPropertySourceFactory
	implements PropertySourceFactory
{

	public DefaultPropertySourceFactory()
	{
	}

	public PropertySource createPropertySource(String name, EncodedResource resource)
		throws IOException
	{
		return name == null ? new ResourcePropertySource(resource) : new ResourcePropertySource(name, resource);
	}
}
