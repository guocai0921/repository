// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ResourceRegion.java

package org.springframework.core.io.support;

import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

public class ResourceRegion
{

	private final Resource resource;
	private final long position;
	private final long count;

	public ResourceRegion(Resource resource, long position, long count)
	{
		Assert.notNull(resource, "Resource must not be null");
		Assert.isTrue(position >= 0L, "'position' must be larger than or equal to 0");
		Assert.isTrue(count >= 0L, "'count' must be larger than or equal to 0");
		this.resource = resource;
		this.position = position;
		this.count = count;
	}

	public Resource getResource()
	{
		return resource;
	}

	public long getPosition()
	{
		return position;
	}

	public long getCount()
	{
		return count;
	}
}
