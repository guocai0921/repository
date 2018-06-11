// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   InstanceFilter.java

package org.springframework.util;

import java.util.*;

// Referenced classes of package org.springframework.util:
//			Assert

public class InstanceFilter
{

	private final Collection includes;
	private final Collection excludes;
	private final boolean matchIfEmpty;

	public InstanceFilter(Collection includes, Collection excludes, boolean matchIfEmpty)
	{
		this.includes = ((Collection) (includes == null ? ((Collection) (Collections.emptyList())) : includes));
		this.excludes = ((Collection) (excludes == null ? ((Collection) (Collections.emptyList())) : excludes));
		this.matchIfEmpty = matchIfEmpty;
	}

	public boolean match(Object instance)
	{
		Assert.notNull(instance, "Instance to match must not be null");
		boolean includesSet = !includes.isEmpty();
		boolean excludesSet = !excludes.isEmpty();
		if (!includesSet && !excludesSet)
			return matchIfEmpty;
		boolean matchIncludes = match(instance, includes);
		boolean matchExcludes = match(instance, excludes);
		if (!includesSet)
			return !matchExcludes;
		if (!excludesSet)
			return matchIncludes;
		else
			return matchIncludes && !matchExcludes;
	}

	protected boolean match(Object instance, Object candidate)
	{
		return instance.equals(candidate);
	}

	protected boolean match(Object instance, Collection candidates)
	{
		for (Iterator iterator = candidates.iterator(); iterator.hasNext();)
		{
			Object candidate = iterator.next();
			if (match(instance, candidate))
				return true;
		}

		return false;
	}

	public String toString()
	{
		StringBuilder sb = new StringBuilder(getClass().getSimpleName());
		sb.append(": includes=").append(includes);
		sb.append(", excludes=").append(excludes);
		sb.append(", matchIfEmpty=").append(matchIfEmpty);
		return sb.toString();
	}
}
