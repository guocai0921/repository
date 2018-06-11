// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   PropertySources.java

package org.springframework.core.env;


// Referenced classes of package org.springframework.core.env:
//			PropertySource

public interface PropertySources
	extends Iterable
{

	public abstract boolean contains(String s);

	public abstract PropertySource get(String s);
}
