// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ComparableComparator.java

package org.springframework.util.comparator;

import java.util.Comparator;

public class ComparableComparator
	implements Comparator
{

	public static final ComparableComparator INSTANCE = new ComparableComparator();

	public ComparableComparator()
	{
	}

	public int compare(Comparable o1, Comparable o2)
	{
		return o1.compareTo(o2);
	}

	public volatile int compare(Object obj, Object obj1)
	{
		return compare((Comparable)obj, (Comparable)obj1);
	}

}
