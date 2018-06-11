// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   OrderUtils.java

package org.springframework.core.annotation;

import org.springframework.util.ClassUtils;

// Referenced classes of package org.springframework.core.annotation:
//			Order, AnnotationUtils

public abstract class OrderUtils
{

	private static Class priorityAnnotationType = null;

	public OrderUtils()
	{
	}

	public static Integer getOrder(Class type)
	{
		return getOrder(type, null);
	}

	public static Integer getOrder(Class type, Integer defaultOrder)
	{
		Order order = (Order)AnnotationUtils.findAnnotation(type, org/springframework/core/annotation/Order);
		if (order != null)
			return Integer.valueOf(order.value());
		Integer priorityOrder = getPriority(type);
		if (priorityOrder != null)
			return priorityOrder;
		else
			return defaultOrder;
	}

	public static Integer getPriority(Class type)
	{
		if (priorityAnnotationType != null)
		{
			java.lang.annotation.Annotation priority = AnnotationUtils.findAnnotation(type, priorityAnnotationType);
			if (priority != null)
				return (Integer)AnnotationUtils.getValue(priority);
		}
		return null;
	}

	static 
	{
		try
		{
			priorityAnnotationType = ClassUtils.forName("javax.annotation.Priority", org/springframework/core/annotation/OrderUtils.getClassLoader());
		}
		catch (Throwable throwable) { }
	}
}
