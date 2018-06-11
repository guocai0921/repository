// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AnnotationAwareOrderComparator.java

package org.springframework.core.annotation;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.*;
import org.springframework.core.DecoratingProxy;
import org.springframework.core.OrderComparator;

// Referenced classes of package org.springframework.core.annotation:
//			Order, OrderUtils, AnnotationUtils

public class AnnotationAwareOrderComparator extends OrderComparator
{

	public static final AnnotationAwareOrderComparator INSTANCE = new AnnotationAwareOrderComparator();

	public AnnotationAwareOrderComparator()
	{
	}

	protected Integer findOrder(Object obj)
	{
		Integer order = super.findOrder(obj);
		if (order != null)
			return order;
		if (obj instanceof Class)
			return OrderUtils.getOrder((Class)obj);
		if (obj instanceof Method)
		{
			Order ann = (Order)AnnotationUtils.findAnnotation((Method)obj, org/springframework/core/annotation/Order);
			if (ann != null)
				return Integer.valueOf(ann.value());
		} else
		if (obj instanceof AnnotatedElement)
		{
			Order ann = (Order)AnnotationUtils.getAnnotation((AnnotatedElement)obj, org/springframework/core/annotation/Order);
			if (ann != null)
				return Integer.valueOf(ann.value());
		} else
		if (obj != null)
		{
			order = OrderUtils.getOrder(obj.getClass());
			if (order == null && (obj instanceof DecoratingProxy))
				order = OrderUtils.getOrder(((DecoratingProxy)obj).getDecoratedClass());
		}
		return order;
	}

	public Integer getPriority(Object obj)
	{
		Integer priority = null;
		if (obj instanceof Class)
			priority = OrderUtils.getPriority((Class)obj);
		else
		if (obj != null)
		{
			priority = OrderUtils.getPriority(obj.getClass());
			if (priority == null && (obj instanceof DecoratingProxy))
				priority = OrderUtils.getOrder(((DecoratingProxy)obj).getDecoratedClass());
		}
		return priority;
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
