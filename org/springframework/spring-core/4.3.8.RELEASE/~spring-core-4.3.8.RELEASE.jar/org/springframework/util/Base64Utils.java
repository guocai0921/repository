// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Base64Utils.java

package org.springframework.util;

import java.nio.charset.Charset;
import javax.xml.bind.DatatypeConverter;
import org.apache.commons.codec.binary.Base64;

// Referenced classes of package org.springframework.util:
//			Assert, ClassUtils

public abstract class Base64Utils
{
	static class CommonsCodecBase64Delegate
		implements Base64Delegate
	{

		private final Base64 base64 = new Base64();
		private final Base64 base64UrlSafe = new Base64(0, null, true);

		public byte[] encode(byte src[])
		{
			return base64.encode(src);
		}

		public byte[] decode(byte src[])
		{
			return base64.decode(src);
		}

		public byte[] encodeUrlSafe(byte src[])
		{
			return base64UrlSafe.encode(src);
		}

		public byte[] decodeUrlSafe(byte src[])
		{
			return base64UrlSafe.decode(src);
		}

		CommonsCodecBase64Delegate()
		{
		}
	}

	static class JdkBase64Delegate
		implements Base64Delegate
	{

		public byte[] encode(byte src[])
		{
			if (src == null || src.length == 0)
				return src;
			else
				return java.util.Base64.getEncoder().encode(src);
		}

		public byte[] decode(byte src[])
		{
			if (src == null || src.length == 0)
				return src;
			else
				return java.util.Base64.getDecoder().decode(src);
		}

		public byte[] encodeUrlSafe(byte src[])
		{
			if (src == null || src.length == 0)
				return src;
			else
				return java.util.Base64.getUrlEncoder().encode(src);
		}

		public byte[] decodeUrlSafe(byte src[])
		{
			if (src == null || src.length == 0)
				return src;
			else
				return java.util.Base64.getUrlDecoder().decode(src);
		}

		JdkBase64Delegate()
		{
		}
	}

	static interface Base64Delegate
	{

		public abstract byte[] encode(byte abyte0[]);

		public abstract byte[] decode(byte abyte0[]);

		public abstract byte[] encodeUrlSafe(byte abyte0[]);

		public abstract byte[] decodeUrlSafe(byte abyte0[]);
	}


	private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
	private static final Base64Delegate delegate;

	public Base64Utils()
	{
	}

	private static void assertDelegateAvailable()
	{
		Assert.state(delegate != null, "Neither Java 8 nor Apache Commons Codec found - Base64 encoding between byte arrays not supported");
	}

	public static byte[] encode(byte src[])
	{
		assertDelegateAvailable();
		return delegate.encode(src);
	}

	public static byte[] decode(byte src[])
	{
		assertDelegateAvailable();
		return delegate.decode(src);
	}

	public static byte[] encodeUrlSafe(byte src[])
	{
		assertDelegateAvailable();
		return delegate.encodeUrlSafe(src);
	}

	public static byte[] decodeUrlSafe(byte src[])
	{
		assertDelegateAvailable();
		return delegate.decodeUrlSafe(src);
	}

	public static String encodeToString(byte src[])
	{
		if (src == null)
			return null;
		if (src.length == 0)
			return "";
		if (delegate != null)
			return new String(delegate.encode(src), DEFAULT_CHARSET);
		else
			return DatatypeConverter.printBase64Binary(src);
	}

	public static byte[] decodeFromString(String src)
	{
		if (src == null)
			return null;
		if (src.isEmpty())
			return new byte[0];
		if (delegate != null)
			return delegate.decode(src.getBytes(DEFAULT_CHARSET));
		else
			return DatatypeConverter.parseBase64Binary(src);
	}

	public static String encodeToUrlSafeString(byte src[])
	{
		assertDelegateAvailable();
		return new String(delegate.encodeUrlSafe(src), DEFAULT_CHARSET);
	}

	public static byte[] decodeFromUrlSafeString(String src)
	{
		assertDelegateAvailable();
		return delegate.decodeUrlSafe(src.getBytes(DEFAULT_CHARSET));
	}

	static 
	{
		Base64Delegate delegateToUse = null;
		if (ClassUtils.isPresent("java.util.Base64", org/springframework/util/Base64Utils.getClassLoader()))
			delegateToUse = new JdkBase64Delegate();
		else
		if (ClassUtils.isPresent("org.apache.commons.codec.binary.Base64", org/springframework/util/Base64Utils.getClassLoader()))
			delegateToUse = new CommonsCodecBase64Delegate();
		delegate = delegateToUse;
	}
}
