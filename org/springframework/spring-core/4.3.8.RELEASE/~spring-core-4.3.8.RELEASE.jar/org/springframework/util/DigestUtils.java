// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DigestUtils.java

package org.springframework.util;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

// Referenced classes of package org.springframework.util:
//			UpdateMessageDigestInputStream, StreamUtils

public abstract class DigestUtils
{

	private static final String MD5_ALGORITHM_NAME = "MD5";
	private static final char HEX_CHARS[] = {
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
		'a', 'b', 'c', 'd', 'e', 'f'
	};

	public DigestUtils()
	{
	}

	public static byte[] md5Digest(byte bytes[])
	{
		return digest("MD5", bytes);
	}

	public static byte[] md5Digest(InputStream inputStream)
		throws IOException
	{
		return digest("MD5", inputStream);
	}

	public static String md5DigestAsHex(byte bytes[])
	{
		return digestAsHexString("MD5", bytes);
	}

	public static String md5DigestAsHex(InputStream inputStream)
		throws IOException
	{
		return digestAsHexString("MD5", inputStream);
	}

	public static StringBuilder appendMd5DigestAsHex(byte bytes[], StringBuilder builder)
	{
		return appendDigestAsHex("MD5", bytes, builder);
	}

	public static StringBuilder appendMd5DigestAsHex(InputStream inputStream, StringBuilder builder)
		throws IOException
	{
		return appendDigestAsHex("MD5", inputStream, builder);
	}

	private static MessageDigest getDigest(String algorithm)
	{
		return MessageDigest.getInstance(algorithm);
		NoSuchAlgorithmException ex;
		ex;
		throw new IllegalStateException((new StringBuilder()).append("Could not find MessageDigest with algorithm \"").append(algorithm).append("\"").toString(), ex);
	}

	private static byte[] digest(String algorithm, byte bytes[])
	{
		return getDigest(algorithm).digest(bytes);
	}

	private static byte[] digest(String algorithm, InputStream inputStream)
		throws IOException
	{
		MessageDigest messageDigest = getDigest(algorithm);
		if (inputStream instanceof UpdateMessageDigestInputStream)
		{
			((UpdateMessageDigestInputStream)inputStream).updateMessageDigest(messageDigest);
			return messageDigest.digest();
		}
		byte buffer[] = new byte[4096];
		for (int bytesRead = -1; (bytesRead = inputStream.read(buffer)) != -1;)
			messageDigest.update(buffer, 0, bytesRead);

		return messageDigest.digest();
	}

	private static String digestAsHexString(String algorithm, byte bytes[])
	{
		char hexDigest[] = digestAsHexChars(algorithm, bytes);
		return new String(hexDigest);
	}

	private static String digestAsHexString(String algorithm, InputStream inputStream)
		throws IOException
	{
		char hexDigest[] = digestAsHexChars(algorithm, inputStream);
		return new String(hexDigest);
	}

	private static StringBuilder appendDigestAsHex(String algorithm, byte bytes[], StringBuilder builder)
	{
		char hexDigest[] = digestAsHexChars(algorithm, bytes);
		return builder.append(hexDigest);
	}

	private static StringBuilder appendDigestAsHex(String algorithm, InputStream inputStream, StringBuilder builder)
		throws IOException
	{
		char hexDigest[] = digestAsHexChars(algorithm, inputStream);
		return builder.append(hexDigest);
	}

	private static char[] digestAsHexChars(String algorithm, byte bytes[])
	{
		byte digest[] = digest(algorithm, bytes);
		return encodeHex(digest);
	}

	private static char[] digestAsHexChars(String algorithm, InputStream inputStream)
		throws IOException
	{
		byte digest[] = digest(algorithm, inputStream);
		return encodeHex(digest);
	}

	private static char[] encodeHex(byte bytes[])
	{
		char chars[] = new char[32];
		for (int i = 0; i < chars.length; i += 2)
		{
			byte b = bytes[i / 2];
			chars[i] = HEX_CHARS[b >>> 4 & 0xf];
			chars[i + 1] = HEX_CHARS[b & 0xf];
		}

		return chars;
	}

}
