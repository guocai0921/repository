// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   InstanceComparator.java

package org.springframework.util.comparator;

import java.util.Comparator;
import org.springframework.util.Assert;

public class InstanceComparator
	implements Comparator
{

	private final Class instanceOrder[];

	public transient InstanceComparator(Class instanceOrder[])
	{
		Assert.notNull(instanceOrder, "'instanceOrder' must not be null");
		this.instanceOrder = instanceOrder;
	}

	public int compare(Object o1, Object o2)
	{
		int i1 = getOrder(o1);
		int i2 = getOrder(o2);
		return i1 >= i2 ? ((int) (i1 != i2 ? 1 : 0)) : -1;
	}

	private int getOrder(Object object)
	{
		if (object != null)
		{
			for (int i = 0; i < instanceOrder.length; i++)
				if (instanceOrder[i].isInstance(object))
					return i;

		}
		return instanceOrder.length;
	}
}
