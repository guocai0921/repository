// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SystemEnvironmentPropertySource.java

package org.springframework.core.env;

import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.springframework.util.Assert;

// Referenced classes of package org.springframework.core.env:
//			MapPropertySource

public class SystemEnvironmentPropertySource extends MapPropertySource
{

	public SystemEnvironmentPropertySource(String name, Map source)
	{
		super(name, source);
	}

	public boolean containsProperty(String name)
	{
		return getProperty(name) != null;
	}

	public Object getProperty(String name)
	{
		String actualName = resolvePropertyName(name);
		if (logger.isDebugEnabled() && !name.equals(actualName))
			logger.debug(String.format("PropertySource [%s] does not contain '%s', but found equivalent '%s'", new Object[] {
				getName(), name, actualName
			}));
		return super.getProperty(actualName);
	}

	private String resolvePropertyName(String name)
	{
		Assert.notNull(name, "Property name must not be null");
		String resolvedName = checkPropertyName(name);
		if (resolvedName != null)
			return resolvedName;
		String uppercasedName = name.toUpperCase();
		if (!name.equals(uppercasedName))
		{
			resolvedName = checkPropertyName(uppercasedName);
			if (resolvedName != null)
				return resolvedName;
		}
		return name;
	}

	private String checkPropertyName(String name)
	{
		if (containsKey(name))
			return name;
		String noDotName = name.replace('.', '_');
		if (!name.equals(noDotName) && containsKey(noDotName))
			return noDotName;
		String noHyphenName = name.replace('-', '_');
		if (!name.equals(noHyphenName) && containsKey(noHyphenName))
			return noHyphenName;
		String noDotNoHyphenName = noDotName.replace('-', '_');
		if (!noDotName.equals(noDotNoHyphenName) && containsKey(noDotNoHyphenName))
			return noDotNoHyphenName;
		else
			return null;
	}

	private boolean containsKey(String name)
	{
		return isSecurityManagerPresent() ? ((Map)source).keySet().contains(name) : ((Map)source).containsKey(name);
	}

	protected boolean isSecurityManagerPresent()
	{
		return System.getSecurityManager() != null;
	}
}
