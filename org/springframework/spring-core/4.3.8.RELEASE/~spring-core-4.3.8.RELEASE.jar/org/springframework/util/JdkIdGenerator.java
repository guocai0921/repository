// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   JdkIdGenerator.java

package org.springframework.util;

import java.util.UUID;

// Referenced classes of package org.springframework.util:
//			IdGenerator

public class JdkIdGenerator
	implements IdGenerator
{

	public JdkIdGenerator()
	{
	}

	public UUID generateId()
	{
		return UUID.randomUUID();
	}
}
