// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CommandLineArgs.java

package org.springframework.core.env;

import java.util.*;

class CommandLineArgs
{

	private final Map optionArgs = new HashMap();
	private final List nonOptionArgs = new ArrayList();

	CommandLineArgs()
	{
	}

	public void addOptionArg(String optionName, String optionValue)
	{
		if (!optionArgs.containsKey(optionName))
			optionArgs.put(optionName, new ArrayList());
		if (optionValue != null)
			((List)optionArgs.get(optionName)).add(optionValue);
	}

	public Set getOptionNames()
	{
		return Collections.unmodifiableSet(optionArgs.keySet());
	}

	public boolean containsOption(String optionName)
	{
		return optionArgs.containsKey(optionName);
	}

	public List getOptionValues(String optionName)
	{
		return (List)optionArgs.get(optionName);
	}

	public void addNonOptionArg(String value)
	{
		nonOptionArgs.add(value);
	}

	public List getNonOptionArgs()
	{
		return Collections.unmodifiableList(nonOptionArgs);
	}
}
