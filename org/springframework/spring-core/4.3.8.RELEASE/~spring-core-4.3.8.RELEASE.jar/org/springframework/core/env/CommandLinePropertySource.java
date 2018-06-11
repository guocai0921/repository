// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CommandLinePropertySource.java

package org.springframework.core.env;

import java.util.Collection;
import java.util.List;
import org.springframework.util.StringUtils;

// Referenced classes of package org.springframework.core.env:
//			EnumerablePropertySource

public abstract class CommandLinePropertySource extends EnumerablePropertySource
{

	public static final String COMMAND_LINE_PROPERTY_SOURCE_NAME = "commandLineArgs";
	public static final String DEFAULT_NON_OPTION_ARGS_PROPERTY_NAME = "nonOptionArgs";
	private String nonOptionArgsPropertyName;

	public CommandLinePropertySource(Object source)
	{
		super("commandLineArgs", source);
		nonOptionArgsPropertyName = "nonOptionArgs";
	}

	public CommandLinePropertySource(String name, Object source)
	{
		super(name, source);
		nonOptionArgsPropertyName = "nonOptionArgs";
	}

	public void setNonOptionArgsPropertyName(String nonOptionArgsPropertyName)
	{
		this.nonOptionArgsPropertyName = nonOptionArgsPropertyName;
	}

	public final boolean containsProperty(String name)
	{
		if (nonOptionArgsPropertyName.equals(name))
			return !getNonOptionArgs().isEmpty();
		else
			return containsOption(name);
	}

	public final String getProperty(String name)
	{
		if (nonOptionArgsPropertyName.equals(name))
		{
			Collection nonOptionArguments = getNonOptionArgs();
			if (nonOptionArguments.isEmpty())
				return null;
			else
				return StringUtils.collectionToCommaDelimitedString(nonOptionArguments);
		}
		Collection optionValues = getOptionValues(name);
		if (optionValues == null)
			return null;
		else
			return StringUtils.collectionToCommaDelimitedString(optionValues);
	}

	protected abstract boolean containsOption(String s);

	protected abstract List getOptionValues(String s);

	protected abstract List getNonOptionArgs();

	public volatile Object getProperty(String s)
	{
		return getProperty(s);
	}
}
