// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ObjenesisSerializer.java

package org.springframework.objenesis;

import org.springframework.objenesis.strategy.SerializingInstantiatorStrategy;

// Referenced classes of package org.springframework.objenesis:
//			ObjenesisBase

public class ObjenesisSerializer extends ObjenesisBase
{

	public ObjenesisSerializer()
	{
		super(new SerializingInstantiatorStrategy());
	}

	public ObjenesisSerializer(boolean useCache)
	{
		super(new SerializingInstantiatorStrategy(), useCache);
	}
}
