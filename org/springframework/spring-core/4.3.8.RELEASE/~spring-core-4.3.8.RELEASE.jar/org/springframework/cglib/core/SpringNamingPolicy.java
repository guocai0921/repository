// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SpringNamingPolicy.java

package org.springframework.cglib.core;


// Referenced classes of package org.springframework.cglib.core:
//			DefaultNamingPolicy

public class SpringNamingPolicy extends DefaultNamingPolicy
{

	public static final SpringNamingPolicy INSTANCE = new SpringNamingPolicy();

	public SpringNamingPolicy()
	{
	}

	protected String getTag()
	{
		return "BySpringCGLIB";
	}

}
