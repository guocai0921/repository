// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   StreamUtils.java

package org.springframework.util;

import java.io.*;
import java.nio.charset.Charset;

// Referenced classes of package org.springframework.util:
//			Assert

public abstract class StreamUtils
{
	private static class NonClosingOutputStream extends FilterOutputStream
	{

		public void write(byte b[], int off, int let)
			throws IOException
		{
			out.write(b, off, let);
		}

		public void close()
			throws IOException
		{
		}

		public NonClosingOutputStream(OutputStream out)
		{
			super(out);
		}
	}

	private static class NonClosingInputStream extends FilterInputStream
	{

		public void close()
			throws IOException
		{
		}

		public NonClosingInputStream(InputStream in)
		{
			super(in);
		}
	}


	public static final int BUFFER_SIZE = 4096;
	private static final byte EMPTY_CONTENT[] = new byte[0];

	public StreamUtils()
	{
	}

	public static byte[] copyToByteArray(InputStream in)
		throws IOException
	{
		if (in == null)
		{
			return new byte[0];
		} else
		{
			ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
			copy(in, out);
			return out.toByteArray();
		}
	}

	public static String copyToString(InputStream in, Charset charset)
		throws IOException
	{
		if (in == null)
			return "";
		StringBuilder out = new StringBuilder();
		InputStreamReader reader = new InputStreamReader(in, charset);
		char buffer[] = new char[4096];
		for (int bytesRead = -1; (bytesRead = reader.read(buffer)) != -1;)
			out.append(buffer, 0, bytesRead);

		return out.toString();
	}

	public static void copy(byte in[], OutputStream out)
		throws IOException
	{
		Assert.notNull(in, "No input byte array specified");
		Assert.notNull(out, "No OutputStream specified");
		out.write(in);
	}

	public static void copy(String in, Charset charset, OutputStream out)
		throws IOException
	{
		Assert.notNull(in, "No input String specified");
		Assert.notNull(charset, "No charset specified");
		Assert.notNull(out, "No OutputStream specified");
		Writer writer = new OutputStreamWriter(out, charset);
		writer.write(in);
		writer.flush();
	}

	public static int copy(InputStream in, OutputStream out)
		throws IOException
	{
		Assert.notNull(in, "No InputStream specified");
		Assert.notNull(out, "No OutputStream specified");
		int byteCount = 0;
		byte buffer[] = new byte[4096];
		for (int bytesRead = -1; (bytesRead = in.read(buffer)) != -1;)
		{
			out.write(buffer, 0, bytesRead);
			byteCount += bytesRead;
		}

		out.flush();
		return byteCount;
	}

	public static long copyRange(InputStream in, OutputStream out, long start, long end)
		throws IOException
	{
		Assert.notNull(in, "No InputStream specified");
		Assert.notNull(out, "No OutputStream specified");
		long skipped = in.skip(start);
		if (skipped < start)
			throw new IOException((new StringBuilder()).append("Skipped only ").append(skipped).append(" bytes out of ").append(start).append(" required").toString());
		long bytesToCopy = (end - start) + 1L;
		byte buffer[] = new byte[4096];
		do
		{
			if (bytesToCopy <= 0L)
				break;
			int bytesRead = in.read(buffer);
			if (bytesRead == -1)
				break;
			if ((long)bytesRead <= bytesToCopy)
			{
				out.write(buffer, 0, bytesRead);
				bytesToCopy -= bytesRead;
			} else
			{
				out.write(buffer, 0, (int)bytesToCopy);
				bytesToCopy = 0L;
			}
		} while (true);
		return ((end - start) + 1L) - bytesToCopy;
	}

	public static int drain(InputStream in)
		throws IOException
	{
		Assert.notNull(in, "No InputStream specified");
		byte buffer[] = new byte[4096];
		int bytesRead = -1;
		int byteCount;
		for (byteCount = 0; (bytesRead = in.read(buffer)) != -1; byteCount += bytesRead);
		return byteCount;
	}

	public static InputStream emptyInput()
	{
		return new ByteArrayInputStream(EMPTY_CONTENT);
	}

	public static InputStream nonClosing(InputStream in)
	{
		Assert.notNull(in, "No InputStream specified");
		return new NonClosingInputStream(in);
	}

	public static OutputStream nonClosing(OutputStream out)
	{
		Assert.notNull(out, "No OutputStream specified");
		return new NonClosingOutputStream(out);
	}

}
