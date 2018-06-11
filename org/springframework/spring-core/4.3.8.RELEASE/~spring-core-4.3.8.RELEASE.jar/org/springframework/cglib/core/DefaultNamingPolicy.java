// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DefaultNamingPolicy.java

package org.springframework.cglib.core;


// Referenced classes of package org.springframework.cglib.core:
//			NamingPolicy, Predicate

public class DefaultNamingPolicy
	implements NamingPolicy
{

	public static final DefaultNamingPolicy INSTANCE = new DefaultNamingPolicy();
	private static final boolean STRESS_HASH_CODE = Boolean.getBoolean("org.springframework.cglib.test.stressHashCodes");

	public DefaultNamingPolicy()
	{
	}

	public String getClassName(String prefix, String source, Object key, Predicate names)
	{
		if (prefix == null)
			prefix = "org.springframework.cglib.empty.Object";
		else
		if (prefix.startsWith("java"))
			prefix = (new StringBuilder()).append("$").append(prefix).toString();
		String base = (new StringBuilder()).append(prefix).append("$$").append(source.substring(source.lastIndexOf('.') + 1)).append(getTag()).append("$$").append(Integer.toHexString(STRESS_HASH_CODE ? 0 : key.hashCode())).toString();
		String attempt = base;
		int index = 2;
		for (; names.evaluate(attempt); attempt = (new StringBuilder()).append(base).append("_").append(index++).toString());
		return attempt;
	}

	protected String getTag()
	{
		return "ByCGLIB";
	}

	public int hashCode()
	{
		return getTag().hashCode();
	}

	public boolean equals(Object o)
	{
		return (o instanceof DefaultNamingPolicy) && ((DefaultNamingPolicy)o).getTag().equals(getTag());
	}

}
