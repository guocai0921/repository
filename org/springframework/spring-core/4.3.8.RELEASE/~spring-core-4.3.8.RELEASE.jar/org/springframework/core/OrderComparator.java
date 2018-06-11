// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   OrderComparator.java

package org.springframework.core;

import java.util.*;
import org.springframework.util.ObjectUtils;

// Referenced classes of package org.springframework.core:
//			PriorityOrdered, Ordered

public class OrderComparator
	implements Comparator
{
	public static interface OrderSourceProvider
	{

		public abstract Object getOrderSource(Object obj);
	}


	public static final OrderComparator INSTANCE = new OrderComparator();

	public OrderComparator()
	{
	}

	public Comparator withSourceProvider(final OrderSourceProvider sourceProvider)
	{
		return new Comparator() {

			final OrderSourceProvider val$sourceProvider;
			final OrderComparator this$0;

			public int compare(Object o1, Object o2)
			{
				return doCompare(o1, o2, sourceProvider);
			}

			
			{
				this.this$0 = OrderComparator.this;
				sourceProvider = ordersourceprovider;
				super();
			}
		};
	}

	public int compare(Object o1, Object o2)
	{
		return doCompare(o1, o2, null);
	}

	private int doCompare(Object o1, Object o2, OrderSourceProvider sourceProvider)
	{
		boolean p1 = o1 instanceof PriorityOrdered;
		boolean p2 = o2 instanceof PriorityOrdered;
		if (p1 && !p2)
			return -1;
		if (p2 && !p1)
		{
			return 1;
		} else
		{
			int i1 = getOrder(o1, sourceProvider);
			int i2 = getOrder(o2, sourceProvider);
			return i1 >= i2 ? ((int) (i1 <= i2 ? 0 : 1)) : -1;
		}
	}

	private int getOrder(Object obj, OrderSourceProvider sourceProvider)
	{
		Integer order = null;
		if (sourceProvider != null)
		{
			Object orderSource = sourceProvider.getOrderSource(obj);
			if (orderSource != null && orderSource.getClass().isArray())
			{
				Object sources[] = ObjectUtils.toObjectArray(orderSource);
				Object aobj[] = sources;
				int i = aobj.length;
				int j = 0;
				do
				{
					if (j >= i)
						break;
					Object source = aobj[j];
					order = findOrder(source);
					if (order != null)
						break;
					j++;
				} while (true);
			} else
			{
				order = findOrder(orderSource);
			}
		}
		return order == null ? getOrder(obj) : order.intValue();
	}

	protected int getOrder(Object obj)
	{
		Integer order = findOrder(obj);
		return order == null ? 0x7fffffff : order.intValue();
	}

	protected Integer findOrder(Object obj)
	{
		return (obj instanceof Ordered) ? Integer.valueOf(((Ordered)obj).getOrder()) : null;
	}

	public Integer getPriority(Object obj)
	{
		return null;
	}

	public static void sort(List list)
	{
		if (list.size() > 1)
			Collections.sort(list, INSTANCE);
	}

	public static void sort(Object array[])
	{
		if (array.length > 1)
			Arrays.sort(array, INSTANCE);
	}

	public static void sortIfNecessary(Object value)
	{
		if (value instanceof Object[])
			sort((Object[])(Object[])value);
		else
		if (value instanceof List)
			sort((List)value);
	}


}
