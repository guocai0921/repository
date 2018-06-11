// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   FileCopyUtils.java

package org.springframework.util;

import java.io.*;

// Referenced classes of package org.springframework.util:
//			StreamUtils, Assert

public abstract class FileCopyUtils
{

	public static final int BUFFER_SIZE = 4096;

	public FileCopyUtils()
	{
	}

	public static int copy(File in, File out)
		throws IOException
	{
		Assert.notNull(in, "No input File specified");
		Assert.notNull(out, "No output File specified");
		return copy(((InputStream) (new BufferedInputStream(new FileInputStream(in)))), ((OutputStream) (new BufferedOutputStream(new FileOutputStream(out)))));
	}

	public static void copy(byte in[], File out)
		throws IOException
	{
		Assert.notNull(in, "No input byte array specified");
		Assert.notNull(out, "No output File specified");
		ByteArrayInputStream inStream = new ByteArrayInputStream(in);
		OutputStream outStream = new BufferedOutputStream(new FileOutputStream(out));
		copy(((InputStream) (inStream)), outStream);
	}

	public static byte[] copyToByteArray(File in)
		throws IOException
	{
		Assert.notNull(in, "No input File specified");
		return copyToByteArray(((InputStream) (new BufferedInputStream(new FileInputStream(in)))));
	}

	public static int copy(InputStream in, OutputStream out)
		throws IOException
	{
		Assert.notNull(in, "No InputStream specified");
		Assert.notNull(out, "No OutputStream specified");
		int i = StreamUtils.copy(in, out);
		try
		{
			in.close();
		}
		catch (IOException ioexception) { }
		try
		{
			out.close();
		}
		catch (IOException ioexception1) { }
		return i;
		Exception exception;
		exception;
		try
		{
			in.close();
		}
		catch (IOException ioexception2) { }
		try
		{
			out.close();
		}
		catch (IOException ioexception3) { }
		throw exception;
	}

	public static void copy(byte in[], OutputStream out)
		throws IOException
	{
		Assert.notNull(in, "No input byte array specified");
		Assert.notNull(out, "No OutputStream specified");
		out.write(in);
		try
		{
			out.close();
		}
		catch (IOException ioexception) { }
		break MISSING_BLOCK_LABEL_40;
		Exception exception;
		exception;
		try
		{
			out.close();
		}
		catch (IOException ioexception1) { }
		throw exception;
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

	public static int copy(Reader in, Writer out)
		throws IOException
	{
		Assert.notNull(in, "No Reader specified");
		Assert.notNull(out, "No Writer specified");
		int i;
		int byteCount = 0;
		char buffer[] = new char[4096];
		for (int bytesRead = -1; (bytesRead = in.read(buffer)) != -1;)
		{
			out.write(buffer, 0, bytesRead);
			byteCount += bytesRead;
		}

		out.flush();
		i = byteCount;
		try
		{
			in.close();
		}
		catch (IOException ioexception) { }
		try
		{
			out.close();
		}
		catch (IOException ioexception1) { }
		return i;
		Exception exception;
		exception;
		try
		{
			in.close();
		}
		catch (IOException ioexception2) { }
		try
		{
			out.close();
		}
		catch (IOException ioexception3) { }
		throw exception;
	}

	public static void copy(String in, Writer out)
		throws IOException
	{
		Assert.notNull(in, "No input String specified");
		Assert.notNull(out, "No Writer specified");
		out.write(in);
		try
		{
			out.close();
		}
		catch (IOException ioexception) { }
		break MISSING_BLOCK_LABEL_40;
		Exception exception;
		exception;
		try
		{
			out.close();
		}
		catch (IOException ioexception1) { }
		throw exception;
	}

	public static String copyToString(Reader in)
		throws IOException
	{
		if (in == null)
		{
			return "";
		} else
		{
			StringWriter out = new StringWriter();
			copy(in, out);
			return out.toString();
		}
	}
}
