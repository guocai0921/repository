// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ConfigurableEnvironment.java

package org.springframework.core.env;

import java.util.Map;

// Referenced classes of package org.springframework.core.env:
//			Environment, ConfigurablePropertyResolver, MutablePropertySources

public interface ConfigurableEnvironment
	extends Environment, ConfigurablePropertyResolver
{

	public transient abstract void setActiveProfiles(String as[]);

	public abstract void addActiveProfile(String s);

	public transient abstract void setDefaultProfiles(String as[]);

	public abstract MutablePropertySources getPropertySources();

	public abstract Map getSystemEnvironment();

	public abstract Map getSystemProperties();

	public abstract void merge(ConfigurableEnvironment configurableenvironment);
}
