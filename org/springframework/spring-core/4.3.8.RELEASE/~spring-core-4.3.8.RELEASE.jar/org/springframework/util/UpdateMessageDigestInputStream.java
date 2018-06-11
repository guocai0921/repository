// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   UpdateMessageDigestInputStream.java

package org.springframework.util;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

abstract class UpdateMessageDigestInputStream extends InputStream
{

	UpdateMessageDigestInputStream()
	{
	}

	public void updateMessageDigest(MessageDigest messageDigest)
		throws IOException
	{
		int data;
		while ((data = read()) != -1) 
			messageDigest.update((byte)data);
	}

	public void updateMessageDigest(MessageDigest messageDigest, int len)
		throws IOException
	{
		int data;
		for (int bytesRead = 0; bytesRead < len && (data = read()) != -1; bytesRead++)
			messageDigest.update((byte)data);

	}
}
