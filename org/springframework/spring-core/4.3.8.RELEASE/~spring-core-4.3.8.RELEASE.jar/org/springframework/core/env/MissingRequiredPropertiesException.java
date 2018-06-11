// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   MissingRequiredPropertiesException.java

package org.springframework.core.env;

import java.util.LinkedHashSet;
import java.util.Set;

public class MissingRequiredPropertiesException extends IllegalStateException
{

	private final Set missingRequiredProperties = new LinkedHashSet();

	public MissingRequiredPropertiesException()
	{
	}

	public Set getMissingRequiredProperties()
	{
		return missingRequiredProperties;
	}

	void addMissingRequiredProperty(String key)
	{
		missingRequiredProperties.add(key);
	}

	public String getMessage()
	{
		return String.format("The following properties were declared as required but could not be resolved: %s", new Object[] {
			getMissingRequiredProperties()
		});
	}
}
