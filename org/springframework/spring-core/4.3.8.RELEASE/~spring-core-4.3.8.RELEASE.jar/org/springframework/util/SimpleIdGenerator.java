// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SimpleIdGenerator.java

package org.springframework.util;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

// Referenced classes of package org.springframework.util:
//			IdGenerator

public class SimpleIdGenerator
	implements IdGenerator
{

	private final AtomicLong mostSigBits = new AtomicLong(0L);
	private final AtomicLong leastSigBits = new AtomicLong(0L);

	public SimpleIdGenerator()
	{
	}

	public UUID generateId()
	{
		long leastSigBits = this.leastSigBits.incrementAndGet();
		if (leastSigBits == 0L)
			mostSigBits.incrementAndGet();
		return new UUID(mostSigBits.get(), leastSigBits);
	}
}
