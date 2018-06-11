// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ExceptionDepthComparator.java

package org.springframework.core;

import java.util.*;
import org.springframework.util.Assert;

public class ExceptionDepthComparator
	implements Comparator
{

	private final Class targetException;

	public ExceptionDepthComparator(Throwable exception)
	{
		Assert.notNull(exception, "Target exception must not be null");
		targetException = exception.getClass();
	}

	public ExceptionDepthComparator(Class exceptionType)
	{
		Assert.notNull(exceptionType, "Target exception type must not be null");
		targetException = exceptionType;
	}

	public int compare(Class o1, Class o2)
	{
		int depth1 = getDepth(o1, targetException, 0);
		int depth2 = getDepth(o2, targetException, 0);
		return depth1 - depth2;
	}

	private int getDepth(Class declaredException, Class exceptionToMatch, int depth)
	{
		if (exceptionToMatch.equals(declaredException))
			return depth;
		if (exceptionToMatch == java/lang/Throwable)
			return 0x7fffffff;
		else
			return getDepth(declaredException, exceptionToMatch.getSuperclass(), depth + 1);
	}

	public static Class findClosestMatch(Collection exceptionTypes, Throwable targetException)
	{
		Assert.notEmpty(exceptionTypes, "Exception types must not be empty");
		if (exceptionTypes.size() == 1)
		{
			return (Class)exceptionTypes.iterator().next();
		} else
		{
			List handledExceptions = new ArrayList(exceptionTypes);
			Collections.sort(handledExceptions, new ExceptionDepthComparator(targetException));
			return (Class)handledExceptions.get(0);
		}
	}

	public volatile int compare(Object obj, Object obj1)
	{
		return compare((Class)obj, (Class)obj1);
	}
}
