// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   BooleanComparator.java

package org.springframework.util.comparator;

import java.io.Serializable;
import java.util.Comparator;

public final class BooleanComparator
	implements Comparator, Serializable
{

	public static final BooleanComparator TRUE_LOW = new BooleanComparator(true);
	public static final BooleanComparator TRUE_HIGH = new BooleanComparator(false);
	private final boolean trueLow;

	public BooleanComparator(boolean trueLow)
	{
		this.trueLow = trueLow;
	}

	public int compare(Boolean v1, Boolean v2)
	{
		return v1.booleanValue() ^ v2.booleanValue() ? v1.booleanValue() ^ trueLow ? 1 : -1 : 0;
	}

	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!(obj instanceof BooleanComparator))
			return false;
		else
			return trueLow == ((BooleanComparator)obj).trueLow;
	}

	public int hashCode()
	{
		return (trueLow ? -1 : 1) * getClass().hashCode();
	}

	public String toString()
	{
		return (new StringBuilder()).append("BooleanComparator: ").append(trueLow ? "true low" : "true high").toString();
	}

	public volatile int compare(Object obj, Object obj1)
	{
		return compare((Boolean)obj, (Boolean)obj1);
	}

}
