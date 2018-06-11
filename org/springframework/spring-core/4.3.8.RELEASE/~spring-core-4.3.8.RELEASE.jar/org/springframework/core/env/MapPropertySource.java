// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   MapPropertySource.java

package org.springframework.core.env;

import java.util.Map;
import org.springframework.util.StringUtils;

// Referenced classes of package org.springframework.core.env:
//			EnumerablePropertySource

public class MapPropertySource extends EnumerablePropertySource
{

	public MapPropertySource(String name, Map source)
	{
		super(name, source);
	}

	public Object getProperty(String name)
	{
		return ((Map)source).get(name);
	}

	public boolean containsProperty(String name)
	{
		return ((Map)source).containsKey(name);
	}

	public String[] getPropertyNames()
	{
		return StringUtils.toStringArray(((Map)source).keySet());
	}
}
