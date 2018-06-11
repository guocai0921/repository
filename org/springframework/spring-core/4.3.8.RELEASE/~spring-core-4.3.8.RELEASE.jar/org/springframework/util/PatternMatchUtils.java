// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   PatternMatchUtils.java

package org.springframework.util;


public abstract class PatternMatchUtils
{

	public PatternMatchUtils()
	{
	}

	public static boolean simpleMatch(String pattern, String str)
	{
		if (pattern == null || str == null)
			return false;
		int firstIndex = pattern.indexOf('*');
		if (firstIndex == -1)
			return pattern.equals(str);
		if (firstIndex == 0)
		{
			if (pattern.length() == 1)
				return true;
			int nextIndex = pattern.indexOf('*', firstIndex + 1);
			if (nextIndex == -1)
				return str.endsWith(pattern.substring(1));
			String part = pattern.substring(1, nextIndex);
			if ("".equals(part))
				return simpleMatch(pattern.substring(nextIndex), str);
			for (int partIndex = str.indexOf(part); partIndex != -1; partIndex = str.indexOf(part, partIndex + 1))
				if (simpleMatch(pattern.substring(nextIndex), str.substring(partIndex + part.length())))
					return true;

			return false;
		} else
		{
			return str.length() >= firstIndex && pattern.substring(0, firstIndex).equals(str.substring(0, firstIndex)) && simpleMatch(pattern.substring(firstIndex), str.substring(firstIndex));
		}
	}

	public static boolean simpleMatch(String patterns[], String str)
	{
		if (patterns != null)
		{
			String as[] = patterns;
			int i = as.length;
			for (int j = 0; j < i; j++)
			{
				String pattern = as[j];
				if (simpleMatch(pattern, str))
					return true;
			}

		}
		return false;
	}
}
