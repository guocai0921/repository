// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   NullSafeComparator.java

package org.springframework.util.comparator;

import java.util.Comparator;
import org.springframework.util.Assert;

// Referenced classes of package org.springframework.util.comparator:
//			ComparableComparator

public class NullSafeComparator
	implements Comparator
{

	public static final NullSafeComparator NULLS_LOW = new NullSafeComparator(true);
	public static final NullSafeComparator NULLS_HIGH = new NullSafeComparator(false);
	private final Comparator nonNullComparator;
	private final boolean nullsLow;

	private NullSafeComparator(boolean nullsLow)
	{
		nonNullComparator = new ComparableComparator();
		this.nullsLow = nullsLow;
	}

	public NullSafeComparator(Comparator comparator, boolean nullsLow)
	{
		Assert.notNull(comparator, "The non-null comparator is required");
		nonNullComparator = comparator;
		this.nullsLow = nullsLow;
	}

	public int compare(Object o1, Object o2)
	{
		if (o1 == o2)
			return 0;
		if (o1 == null)
			return nullsLow ? -1 : 1;
		if (o2 == null)
			return nullsLow ? 1 : -1;
		else
			return nonNullComparator.compare(o1, o2);
	}

	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!(obj instanceof NullSafeComparator))
		{
			return false;
		} else
		{
			NullSafeComparator other = (NullSafeComparator)obj;
			return nonNullComparator.equals(other.nonNullComparator) && nullsLow == other.nullsLow;
		}
	}

	public int hashCode()
	{
		return (nullsLow ? -1 : 1) * nonNullComparator.hashCode();
	}

	public String toString()
	{
		return (new StringBuilder()).append("NullSafeComparator: non-null comparator [").append(nonNullComparator).append("]; ").append(nullsLow ? "nulls low" : "nulls high").toString();
	}

}
