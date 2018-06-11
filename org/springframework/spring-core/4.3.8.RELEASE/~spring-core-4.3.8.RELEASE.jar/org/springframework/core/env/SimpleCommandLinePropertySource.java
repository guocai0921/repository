// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SimpleCommandLinePropertySource.java

package org.springframework.core.env;

import java.util.List;
import java.util.Set;

// Referenced classes of package org.springframework.core.env:
//			CommandLinePropertySource, SimpleCommandLineArgsParser, CommandLineArgs

public class SimpleCommandLinePropertySource extends CommandLinePropertySource
{

	public transient SimpleCommandLinePropertySource(String args[])
	{
		super((new SimpleCommandLineArgsParser()).parse(args));
	}

	public SimpleCommandLinePropertySource(String name, String args[])
	{
		super(name, (new SimpleCommandLineArgsParser()).parse(args));
	}

	public String[] getPropertyNames()
	{
		return (String[])((CommandLineArgs)source).getOptionNames().toArray(new String[((CommandLineArgs)source).getOptionNames().size()]);
	}

	protected boolean containsOption(String name)
	{
		return ((CommandLineArgs)source).containsOption(name);
	}

	protected List getOptionValues(String name)
	{
		return ((CommandLineArgs)source).getOptionValues(name);
	}

	protected List getNonOptionArgs()
	{
		return ((CommandLineArgs)source).getNonOptionArgs();
	}
}
