// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ExceptionTypeFilter.java

package org.springframework.util;

import java.util.Collection;

// Referenced classes of package org.springframework.util:
//			InstanceFilter

public class ExceptionTypeFilter extends InstanceFilter
{

	public ExceptionTypeFilter(Collection includes, Collection excludes, boolean matchIfEmpty)
	{
		super(includes, excludes, matchIfEmpty);
	}

	protected boolean match(Class instance, Class candidate)
	{
		return candidate.isAssignableFrom(instance);
	}

	protected volatile boolean match(Object obj, Object obj1)
	{
		return match((Class)obj, (Class)obj1);
	}
}
