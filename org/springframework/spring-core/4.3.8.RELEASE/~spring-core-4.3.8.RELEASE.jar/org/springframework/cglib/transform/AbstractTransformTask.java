// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AbstractTransformTask.java

package org.springframework.cglib.transform;

import java.io.*;
import java.net.MalformedURLException;
import java.util.zip.*;
import org.springframework.asm.Attribute;
import org.springframework.asm.ClassReader;
import org.springframework.cglib.core.ClassNameReader;
import org.springframework.cglib.core.DebuggingClassWriter;

// Referenced classes of package org.springframework.cglib.transform:
//			AbstractProcessTask, TransformingClassGenerator, ClassReaderGenerator, ClassTransformer

public abstract class AbstractTransformTask extends AbstractProcessTask
{

	private static final int ZIP_MAGIC = 0x504b0304;
	private static final int CLASS_MAGIC = 0xcafebabe;
	private boolean verbose;

	public AbstractTransformTask()
	{
	}

	public void setVerbose(boolean verbose)
	{
		this.verbose = verbose;
	}

	protected abstract ClassTransformer getClassTransformer(String as[]);

	protected Attribute[] attributes()
	{
		return null;
	}

	protected void processFile(File file)
		throws Exception
	{
		if (isClassFile(file))
			processClassFile(file);
		else
		if (isJarFile(file))
			processJarFile(file);
		else
			log((new StringBuilder()).append("ignoring ").append(file.toURI()).toString(), 1);
	}

	private void processClassFile(File file)
		throws Exception, FileNotFoundException, IOException, MalformedURLException
	{
		DebuggingClassWriter w;
		FileOutputStream fos;
		ClassReader reader = getClassReader(file);
		String name[] = ClassNameReader.getClassInfo(reader);
		w = new DebuggingClassWriter(2);
		ClassTransformer t = getClassTransformer(name);
		if (t == null)
			break MISSING_BLOCK_LABEL_136;
		if (verbose)
			log((new StringBuilder()).append("processing ").append(file.toURI()).toString());
		(new TransformingClassGenerator(new ClassReaderGenerator(getClassReader(file), attributes(), getFlags()), t)).generateClass(w);
		fos = new FileOutputStream(file);
		fos.write(w.toByteArray());
		fos.close();
		break MISSING_BLOCK_LABEL_136;
		Exception exception;
		exception;
		fos.close();
		throw exception;
	}

	protected int getFlags()
	{
		return 0;
	}

	private static ClassReader getClassReader(File file)
		throws Exception
	{
		InputStream in = new BufferedInputStream(new FileInputStream(file));
		ClassReader classreader;
		ClassReader r = new ClassReader(in);
		classreader = r;
		in.close();
		return classreader;
		Exception exception;
		exception;
		in.close();
		throw exception;
	}

	protected boolean isClassFile(File file)
		throws IOException
	{
		return checkMagic(file, 0xffffffffcafebabeL);
	}

	protected void processJarFile(File file)
		throws Exception
	{
		File tempFile;
		if (verbose)
			log((new StringBuilder()).append("processing ").append(file.toURI()).toString());
		tempFile = File.createTempFile(file.getName(), null, new File(file.getAbsoluteFile().getParent()));
		ZipInputStream zip = new ZipInputStream(new FileInputStream(file));
		FileOutputStream fout = new FileOutputStream(tempFile);
		ZipOutputStream out = new ZipOutputStream(fout);
		ZipEntry entry;
		while ((entry = zip.getNextEntry()) != null) 
		{
			byte bytes[] = getBytes(zip);
			if (!entry.isDirectory())
			{
				DataInputStream din = new DataInputStream(new ByteArrayInputStream(bytes));
				if (din.readInt() == 0xcafebabe)
					bytes = process(bytes);
				else
				if (verbose)
					log((new StringBuilder()).append("ignoring ").append(entry.toString()).toString());
			}
			ZipEntry outEntry = new ZipEntry(entry.getName());
			outEntry.setMethod(entry.getMethod());
			outEntry.setComment(entry.getComment());
			outEntry.setSize(bytes.length);
			if (outEntry.getMethod() == 0)
			{
				CRC32 crc = new CRC32();
				crc.update(bytes);
				outEntry.setCrc(crc.getValue());
				outEntry.setCompressedSize(bytes.length);
			}
			out.putNextEntry(outEntry);
			out.write(bytes);
			out.closeEntry();
			zip.closeEntry();
		}
		out.close();
		fout.close();
		break MISSING_BLOCK_LABEL_326;
		Exception exception;
		exception;
		fout.close();
		throw exception;
		zip.close();
		break MISSING_BLOCK_LABEL_342;
		Exception exception1;
		exception1;
		zip.close();
		throw exception1;
		if (file.delete())
		{
			File newFile = new File(tempFile.getAbsolutePath());
			if (!newFile.renameTo(file))
				throw new IOException((new StringBuilder()).append("can not rename ").append(tempFile).append(" to ").append(file).toString());
		} else
		{
			throw new IOException((new StringBuilder()).append("can not delete ").append(file).toString());
		}
		tempFile.delete();
		break MISSING_BLOCK_LABEL_458;
		Exception exception2;
		exception2;
		tempFile.delete();
		throw exception2;
	}

	private byte[] process(byte bytes[])
		throws Exception
	{
		ClassReader reader = new ClassReader(new ByteArrayInputStream(bytes));
		String name[] = ClassNameReader.getClassInfo(reader);
		DebuggingClassWriter w = new DebuggingClassWriter(2);
		ClassTransformer t = getClassTransformer(name);
		if (t != null)
		{
			if (verbose)
				log((new StringBuilder()).append("processing ").append(name[0]).toString());
			(new TransformingClassGenerator(new ClassReaderGenerator(new ClassReader(new ByteArrayInputStream(bytes)), attributes(), getFlags()), t)).generateClass(w);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			out.write(w.toByteArray());
			return out.toByteArray();
		} else
		{
			return bytes;
		}
	}

	private byte[] getBytes(ZipInputStream zip)
		throws IOException
	{
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		InputStream in = new BufferedInputStream(zip);
		int b;
		while ((b = in.read()) != -1) 
			bout.write(b);
		return bout.toByteArray();
	}

	private boolean checkMagic(File file, long magic)
		throws IOException
	{
		DataInputStream in = new DataInputStream(new FileInputStream(file));
		boolean flag;
		int m = in.readInt();
		flag = magic == (long)m;
		in.close();
		return flag;
		Exception exception;
		exception;
		in.close();
		throw exception;
	}

	protected boolean isJarFile(File file)
		throws IOException
	{
		return checkMagic(file, 0x504b0304L);
	}
}
