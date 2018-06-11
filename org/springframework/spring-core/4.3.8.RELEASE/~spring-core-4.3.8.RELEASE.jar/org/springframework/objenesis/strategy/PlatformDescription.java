// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   PlatformDescription.java

package org.springframework.objenesis.strategy;

import java.lang.reflect.Field;
import org.springframework.objenesis.ObjenesisException;

public final class PlatformDescription
{

	public static final String JROCKIT = "BEA";
	public static final String GNU = "GNU libgcj";
	public static final String HOTSPOT = "Java HotSpot";
	/**
	 * @deprecated Field SUN is deprecated
	 */
	public static final String SUN = "Java HotSpot";
	public static final String OPENJDK = "OpenJDK";
	public static final String PERC = "PERC";
	public static final String DALVIK = "Dalvik";
	public static final String SPECIFICATION_VERSION = System.getProperty("java.specification.version");
	public static final String VM_VERSION = System.getProperty("java.runtime.version");
	public static final String VM_INFO = System.getProperty("java.vm.info");
	public static final String VENDOR_VERSION = System.getProperty("java.vm.version");
	public static final String VENDOR = System.getProperty("java.vm.vendor");
	public static final String JVM_NAME = System.getProperty("java.vm.name");
	public static final int ANDROID_VERSION = getAndroidVersion();
	public static final boolean IS_ANDROID_OPENJDK = getIsAndroidOpenJDK();
	public static final String GAE_VERSION = getGaeRuntimeVersion();

	public static String describePlatform()
	{
		String desc = (new StringBuilder()).append("Java ").append(SPECIFICATION_VERSION).append(" (VM vendor name=\"").append(VENDOR).append("\", VM vendor version=").append(VENDOR_VERSION).append(", JVM name=\"").append(JVM_NAME).append("\", JVM version=").append(VM_VERSION).append(", JVM info=").append(VM_INFO).toString();
		int androidVersion = ANDROID_VERSION;
		if (androidVersion != 0)
			desc = (new StringBuilder()).append(desc).append(", API level=").append(ANDROID_VERSION).toString();
		desc = (new StringBuilder()).append(desc).append(")").toString();
		return desc;
	}

	public static boolean isThisJVM(String name)
	{
		return JVM_NAME.startsWith(name);
	}

	public static boolean isAndroidOpenJDK()
	{
		return IS_ANDROID_OPENJDK;
	}

	private static boolean getIsAndroidOpenJDK()
	{
		if (getAndroidVersion() == 0)
		{
			return false;
		} else
		{
			String bootClasspath = System.getProperty("java.boot.class.path");
			return bootClasspath != null && bootClasspath.toLowerCase().contains("core-oj.jar");
		}
	}

	public static boolean isGoogleAppEngine()
	{
		return GAE_VERSION != null;
	}

	private static String getGaeRuntimeVersion()
	{
		return System.getProperty("com.google.appengine.runtime.version");
	}

	private static int getAndroidVersion()
	{
		if (!isThisJVM("Dalvik"))
			return 0;
		else
			return getAndroidVersion0();
	}

	private static int getAndroidVersion0()
	{
		Class clazz;
		try
		{
			clazz = Class.forName("android.os.Build$VERSION");
		}
		catch (ClassNotFoundException e)
		{
			throw new ObjenesisException(e);
		}
		Field field;
		try
		{
			field = clazz.getField("SDK_INT");
		}
		catch (NoSuchFieldException e)
		{
			return getOldAndroidVersion(clazz);
		}
		int version;
		try
		{
			version = ((Integer)field.get(null)).intValue();
		}
		catch (IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
		return version;
	}

	private static int getOldAndroidVersion(Class versionClass)
	{
		Field field;
		try
		{
			field = versionClass.getField("SDK");
		}
		catch (NoSuchFieldException e)
		{
			throw new ObjenesisException(e);
		}
		String version;
		try
		{
			version = (String)field.get(null);
		}
		catch (IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
		return Integer.parseInt(version);
	}

	private PlatformDescription()
	{
	}

}
