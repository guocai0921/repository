// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ObjenesisStd.java

package org.springframework.objenesis;

import org.springframework.objenesis.strategy.StdInstantiatorStrategy;

// Referenced classes of package org.springframework.objenesis:
//			ObjenesisBase

public class ObjenesisStd extends ObjenesisBase
{

	public ObjenesisStd()
	{
		super(new StdInstantiatorStrategy());
	}

	public ObjenesisStd(boolean useCache)
	{
		super(new StdInstantiatorStrategy(), useCache);
	}
}
