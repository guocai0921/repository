// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   InvertibleComparator.java

package org.springframework.util.comparator;

import java.io.Serializable;
import java.util.Comparator;
import org.springframework.util.Assert;

public class InvertibleComparator
	implements Comparator, Serializable
{

	private final Comparator comparator;
	private boolean ascending;

	public InvertibleComparator(Comparator comparator)
	{
		ascending = true;
		Assert.notNull(comparator, "Comparator must not be null");
		this.comparator = comparator;
	}

	public InvertibleComparator(Comparator comparator, boolean ascending)
	{
		this.ascending = true;
		Assert.notNull(comparator, "Comparator must not be null");
		this.comparator = comparator;
		setAscending(ascending);
	}

	public void setAscending(boolean ascending)
	{
		this.ascending = ascending;
	}

	public boolean isAscending()
	{
		return ascending;
	}

	public void invertOrder()
	{
		ascending = !ascending;
	}

	public int compare(Object o1, Object o2)
	{
		int result = comparator.compare(o1, o2);
		if (result != 0)
		{
			if (!ascending)
				if (0x80000000 == result)
					result = 0x7fffffff;
				else
					result *= -1;
			return result;
		} else
		{
			return 0;
		}
	}

	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!(obj instanceof InvertibleComparator))
		{
			return false;
		} else
		{
			InvertibleComparator other = (InvertibleComparator)obj;
			return comparator.equals(other.comparator) && ascending == other.ascending;
		}
	}

	public int hashCode()
	{
		return comparator.hashCode();
	}

	public String toString()
	{
		return (new StringBuilder()).append("InvertibleComparator: [").append(comparator).append("]; ascending=").append(ascending).toString();
	}
}
