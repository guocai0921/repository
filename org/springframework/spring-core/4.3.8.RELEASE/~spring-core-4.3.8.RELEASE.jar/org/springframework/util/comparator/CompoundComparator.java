// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CompoundComparator.java

package org.springframework.util.comparator;

import java.io.Serializable;
import java.util.*;
import org.springframework.util.Assert;

// Referenced classes of package org.springframework.util.comparator:
//			InvertibleComparator

public class CompoundComparator
	implements Comparator, Serializable
{

	private final List comparators;

	public CompoundComparator()
	{
		comparators = new ArrayList();
	}

	public transient CompoundComparator(Comparator comparators[])
	{
		Assert.notNull(comparators, "Comparators must not be null");
		this.comparators = new ArrayList(comparators.length);
		Comparator acomparator[] = comparators;
		int i = acomparator.length;
		for (int j = 0; j < i; j++)
		{
			Comparator comparator = acomparator[j];
			addComparator(comparator);
		}

	}

	public void addComparator(Comparator comparator)
	{
		if (comparator instanceof InvertibleComparator)
			comparators.add((InvertibleComparator)comparator);
		else
			comparators.add(new InvertibleComparator(comparator));
	}

	public void addComparator(Comparator comparator, boolean ascending)
	{
		comparators.add(new InvertibleComparator(comparator, ascending));
	}

	public void setComparator(int index, Comparator comparator)
	{
		if (comparator instanceof InvertibleComparator)
			comparators.set(index, (InvertibleComparator)comparator);
		else
			comparators.set(index, new InvertibleComparator(comparator));
	}

	public void setComparator(int index, Comparator comparator, boolean ascending)
	{
		comparators.set(index, new InvertibleComparator(comparator, ascending));
	}

	public void invertOrder()
	{
		InvertibleComparator comparator;
		for (Iterator iterator = comparators.iterator(); iterator.hasNext(); comparator.invertOrder())
			comparator = (InvertibleComparator)iterator.next();

	}

	public void invertOrder(int index)
	{
		((InvertibleComparator)comparators.get(index)).invertOrder();
	}

	public void setAscendingOrder(int index)
	{
		((InvertibleComparator)comparators.get(index)).setAscending(true);
	}

	public void setDescendingOrder(int index)
	{
		((InvertibleComparator)comparators.get(index)).setAscending(false);
	}

	public int getComparatorCount()
	{
		return comparators.size();
	}

	public int compare(Object o1, Object o2)
	{
		Assert.state(comparators.size() > 0, "No sort definitions have been added to this CompoundComparator to compare");
		for (Iterator iterator = comparators.iterator(); iterator.hasNext();)
		{
			InvertibleComparator comparator = (InvertibleComparator)iterator.next();
			int result = comparator.compare(o1, o2);
			if (result != 0)
				return result;
		}

		return 0;
	}

	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!(obj instanceof CompoundComparator))
		{
			return false;
		} else
		{
			CompoundComparator other = (CompoundComparator)obj;
			return comparators.equals(other.comparators);
		}
	}

	public int hashCode()
	{
		return comparators.hashCode();
	}

	public String toString()
	{
		return (new StringBuilder()).append("CompoundComparator: ").append(comparators).toString();
	}
}
