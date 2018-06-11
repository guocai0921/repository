// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   JdkVersion.java

package org.springframework.core;


/**
 * @deprecated Class JdkVersion is deprecated
 */

public abstract class JdkVersion
{

	public static final int JAVA_13 = 0;
	public static final int JAVA_14 = 1;
	public static final int JAVA_15 = 2;
	public static final int JAVA_16 = 3;
	public static final int JAVA_17 = 4;
	public static final int JAVA_18 = 5;
	public static final int JAVA_19 = 6;
	private static final String javaVersion = System.getProperty("java.version");
	private static final int majorJavaVersion;

	public JdkVersion()
	{
	}

	public static String getJavaVersion()
	{
		return javaVersion;
	}

	public static int getMajorJavaVersion()
	{
		return majorJavaVersion;
	}

	static 
	{
		if (javaVersion.contains("1.9."))
			majorJavaVersion = 6;
		else
		if (javaVersion.contains("1.8."))
			majorJavaVersion = 5;
		else
		if (javaVersion.contains("1.7."))
			majorJavaVersion = 4;
		else
			majorJavaVersion = 3;
	}
}
