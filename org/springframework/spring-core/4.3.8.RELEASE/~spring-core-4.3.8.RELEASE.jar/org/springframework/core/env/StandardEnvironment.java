// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   StandardEnvironment.java

package org.springframework.core.env;


// Referenced classes of package org.springframework.core.env:
//			AbstractEnvironment, MapPropertySource, SystemEnvironmentPropertySource, MutablePropertySources

public class StandardEnvironment extends AbstractEnvironment
{

	public static final String SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME = "systemEnvironment";
	public static final String SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME = "systemProperties";

	public StandardEnvironment()
	{
	}

	protected void customizePropertySources(MutablePropertySources propertySources)
	{
		propertySources.addLast(new MapPropertySource("systemProperties", getSystemProperties()));
		propertySources.addLast(new SystemEnvironmentPropertySource("systemEnvironment", getSystemEnvironment()));
	}
}
