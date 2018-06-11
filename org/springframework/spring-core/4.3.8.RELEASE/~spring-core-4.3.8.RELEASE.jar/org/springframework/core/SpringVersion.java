// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SpringVersion.java

package org.springframework.core;


public class SpringVersion
{

	public SpringVersion()
	{
	}

	public static String getVersion()
	{
		Package pkg = org/springframework/core/SpringVersion.getPackage();
		return pkg == null ? null : pkg.getImplementationVersion();
	}
}
