// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   JOptCommandLinePropertySource.java

package org.springframework.core.env;

import java.util.*;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

// Referenced classes of package org.springframework.core.env:
//			CommandLinePropertySource

public class JOptCommandLinePropertySource extends CommandLinePropertySource
{

	public JOptCommandLinePropertySource(OptionSet options)
	{
		super(options);
	}

	public JOptCommandLinePropertySource(String name, OptionSet options)
	{
		super(name, options);
	}

	protected boolean containsOption(String name)
	{
		return ((OptionSet)source).has(name);
	}

	public String[] getPropertyNames()
	{
		List names = new ArrayList();
		Iterator iterator = ((OptionSet)source).specs().iterator();
		do
		{
			if (!iterator.hasNext())
				break;
			OptionSpec spec = (OptionSpec)iterator.next();
			List aliases = new ArrayList(spec.options());
			if (!aliases.isEmpty())
				names.add(aliases.get(aliases.size() - 1));
		} while (true);
		return (String[])names.toArray(new String[names.size()]);
	}

	public List getOptionValues(String name)
	{
		List argValues = ((OptionSet)source).valuesOf(name);
		List stringArgValues = new ArrayList();
		Object argValue;
		for (Iterator iterator = argValues.iterator(); iterator.hasNext(); stringArgValues.add(argValue.toString()))
			argValue = iterator.next();

		if (stringArgValues.isEmpty())
			return ((OptionSet)source).has(name) ? Collections.emptyList() : null;
		else
			return Collections.unmodifiableList(stringArgValues);
	}

	protected List getNonOptionArgs()
	{
		List argValues = ((OptionSet)source).nonOptionArguments();
		List stringArgValues = new ArrayList();
		Object argValue;
		for (Iterator iterator = argValues.iterator(); iterator.hasNext(); stringArgValues.add(argValue.toString()))
			argValue = iterator.next();

		return stringArgValues.isEmpty() ? Collections.emptyList() : Collections.unmodifiableList(stringArgValues);
	}
}
