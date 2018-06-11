// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SimpleCommandLineArgsParser.java

package org.springframework.core.env;


// Referenced classes of package org.springframework.core.env:
//			CommandLineArgs

class SimpleCommandLineArgsParser
{

	SimpleCommandLineArgsParser()
	{
	}

	public transient CommandLineArgs parse(String args[])
	{
		CommandLineArgs commandLineArgs = new CommandLineArgs();
		String as[] = args;
		int i = as.length;
		for (int j = 0; j < i; j++)
		{
			String arg = as[j];
			if (arg.startsWith("--"))
			{
				String optionText = arg.substring(2, arg.length());
				String optionValue = null;
				String optionName;
				if (optionText.contains("="))
				{
					optionName = optionText.substring(0, optionText.indexOf("="));
					optionValue = optionText.substring(optionText.indexOf("=") + 1, optionText.length());
				} else
				{
					optionName = optionText;
				}
				if (optionName.isEmpty() || optionValue != null && optionValue.isEmpty())
					throw new IllegalArgumentException((new StringBuilder()).append("Invalid argument syntax: ").append(arg).toString());
				commandLineArgs.addOptionArg(optionName, optionValue);
			} else
			{
				commandLineArgs.addNonOptionArg(arg);
			}
		}

		return commandLineArgs;
	}
}
