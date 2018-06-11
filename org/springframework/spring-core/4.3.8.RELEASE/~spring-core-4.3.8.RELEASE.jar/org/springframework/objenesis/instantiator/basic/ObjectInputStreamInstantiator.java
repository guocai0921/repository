// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ObjectInputStreamInstantiator.java

package org.springframework.objenesis.instantiator.basic;

import java.io.*;
import org.springframework.objenesis.ObjenesisException;
import org.springframework.objenesis.instantiator.ObjectInstantiator;

public class ObjectInputStreamInstantiator
	implements ObjectInstantiator
{
	private static class MockStream extends InputStream
	{

		private int pointer;
		private byte data[];
		private int sequence;
		private static final int NEXT[] = {
			1, 2, 2
		};
		private byte buffers[][];
		private final byte FIRST_DATA[];
		private static byte HEADER[];
		private static byte REPEATING_DATA[];

		private static void initialize()
		{
			try
			{
				ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
				DataOutputStream dout = new DataOutputStream(byteOut);
				dout.writeShort(-21267);
				dout.writeShort(5);
				HEADER = byteOut.toByteArray();
				byteOut = new ByteArrayOutputStream();
				dout = new DataOutputStream(byteOut);
				dout.writeByte(115);
				dout.writeByte(113);
				dout.writeInt(0x7e0000);
				REPEATING_DATA = byteOut.toByteArray();
			}
			catch (IOException e)
			{
				throw new Error((new StringBuilder()).append("IOException: ").append(e.getMessage()).toString());
			}
		}

		private void advanceBuffer()
		{
			pointer = 0;
			sequence = NEXT[sequence];
			data = buffers[sequence];
		}

		public int read()
			throws IOException
		{
			int result = data[pointer++];
			if (pointer >= data.length)
				advanceBuffer();
			return result;
		}

		public int available()
			throws IOException
		{
			return 0x7fffffff;
		}

		public int read(byte b[], int off, int len)
			throws IOException
		{
			int left = len;
			for (int remaining = data.length - pointer; remaining <= left; remaining = data.length - pointer)
			{
				System.arraycopy(data, pointer, b, off, remaining);
				off += remaining;
				left -= remaining;
				advanceBuffer();
			}

			if (left > 0)
			{
				System.arraycopy(data, pointer, b, off, left);
				pointer += left;
			}
			return len;
		}

		static 
		{
			initialize();
		}

		public MockStream(Class clazz)
		{
			pointer = 0;
			sequence = 0;
			data = HEADER;
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			DataOutputStream dout = new DataOutputStream(byteOut);
			try
			{
				dout.writeByte(115);
				dout.writeByte(114);
				dout.writeUTF(clazz.getName());
				dout.writeLong(ObjectStreamClass.lookup(clazz).getSerialVersionUID());
				dout.writeByte(2);
				dout.writeShort(0);
				dout.writeByte(120);
				dout.writeByte(112);
			}
			catch (IOException e)
			{
				throw new Error((new StringBuilder()).append("IOException: ").append(e.getMessage()).toString());
			}
			FIRST_DATA = byteOut.toByteArray();
			buffers = (new byte[][] {
				HEADER, FIRST_DATA, REPEATING_DATA
			});
		}
	}


	private ObjectInputStream inputStream;

	public ObjectInputStreamInstantiator(Class clazz)
	{
		if (java/io/Serializable.isAssignableFrom(clazz))
			try
			{
				inputStream = new ObjectInputStream(new MockStream(clazz));
			}
			catch (IOException e)
			{
				throw new Error((new StringBuilder()).append("IOException: ").append(e.getMessage()).toString());
			}
		else
			throw new ObjenesisException(new NotSerializableException((new StringBuilder()).append(clazz).append(" not serializable").toString()));
	}

	public Object newInstance()
	{
		return inputStream.readObject();
		ClassNotFoundException e;
		e;
		throw new Error((new StringBuilder()).append("ClassNotFoundException: ").append(e.getMessage()).toString());
		e;
		throw new ObjenesisException(e);
	}
}
