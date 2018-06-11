// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ResourceLoader.java

package org.springframework.core.io;

import org.springframework.util.ResourceUtils;

// Referenced classes of package org.springframework.core.io:
//			Resource

public interface ResourceLoader
{

	public static final String CLASSPATH_URL_PREFIX = "classpath:";

	public abstract Resource getResource(String s);

	public abstract ClassLoader getClassLoader();
}
