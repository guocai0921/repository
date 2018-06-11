// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   FileSystemUtils.java

package org.springframework.util;

import java.io.File;
import java.io.IOException;

// Referenced classes of package org.springframework.util:
//			Assert, FileCopyUtils

public abstract class FileSystemUtils
{

	public FileSystemUtils()
	{
	}

	public static boolean deleteRecursively(File root)
	{
		if (root != null && root.exists())
		{
			if (root.isDirectory())
			{
				File children[] = root.listFiles();
				if (children != null)
				{
					File afile[] = children;
					int i = afile.length;
					for (int j = 0; j < i; j++)
					{
						File child = afile[j];
						deleteRecursively(child);
					}

				}
			}
			return root.delete();
		} else
		{
			return false;
		}
	}

	public static void copyRecursively(File src, File dest)
		throws IOException
	{
		Assert.isTrue(src != null && (src.isDirectory() || src.isFile()), "Source File must denote a directory or file");
		Assert.notNull(dest, "Destination File must not be null");
		doCopyRecursively(src, dest);
	}

	private static void doCopyRecursively(File src, File dest)
		throws IOException
	{
		if (src.isDirectory())
		{
			dest.mkdir();
			File entries[] = src.listFiles();
			if (entries == null)
				throw new IOException((new StringBuilder()).append("Could not list files in directory: ").append(src).toString());
			File afile[] = entries;
			int i = afile.length;
			for (int j = 0; j < i; j++)
			{
				File entry = afile[j];
				doCopyRecursively(entry, new File(dest, entry.getName()));
			}

		} else
		if (src.isFile())
		{
			try
			{
				dest.createNewFile();
			}
			catch (IOException ex)
			{
				IOException ioex = new IOException((new StringBuilder()).append("Failed to create file: ").append(dest).toString());
				ioex.initCause(ex);
				throw ioex;
			}
			FileCopyUtils.copy(src, dest);
		}
	}
}
