// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   PropertiesPropertySource.java

package org.springframework.core.env;

import java.util.Map;
import java.util.Properties;

// Referenced classes of package org.springframework.core.env:
//			MapPropertySource

public class PropertiesPropertySource extends MapPropertySource
{

	public PropertiesPropertySource(String name, Properties source)
	{
		super(name, source);
	}

	protected PropertiesPropertySource(String name, Map source)
	{
		super(name, source);
	}
}
