// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Environment.java

package org.springframework.core.env;


// Referenced classes of package org.springframework.core.env:
//			PropertyResolver

public interface Environment
	extends PropertyResolver
{

	public abstract String[] getActiveProfiles();

	public abstract String[] getDefaultProfiles();

	public transient abstract boolean acceptsProfiles(String as[]);
}
