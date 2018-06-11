// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AlternativeJdkIdGenerator.java

package org.springframework.util;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;

// Referenced classes of package org.springframework.util:
//			IdGenerator

public class AlternativeJdkIdGenerator
	implements IdGenerator
{

	private final Random random;

	public AlternativeJdkIdGenerator()
	{
		SecureRandom secureRandom = new SecureRandom();
		byte seed[] = new byte[8];
		secureRandom.nextBytes(seed);
		random = new Random((new BigInteger(seed)).longValue());
	}

	public UUID generateId()
	{
		byte randomBytes[] = new byte[16];
		random.nextBytes(randomBytes);
		long mostSigBits = 0L;
		for (int i = 0; i < 8; i++)
			mostSigBits = mostSigBits << 8 | (long)(randomBytes[i] & 0xff);

		long leastSigBits = 0L;
		for (int i = 8; i < 16; i++)
			leastSigBits = leastSigBits << 8 | (long)(randomBytes[i] & 0xff);

		return new UUID(mostSigBits, leastSigBits);
	}
}
