// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   FastByteArrayOutputStream.java

package org.springframework.util;

import java.io.*;
import java.security.MessageDigest;
import java.util.Iterator;
import java.util.LinkedList;

// Referenced classes of package org.springframework.util:
//			Assert, UpdateMessageDigestInputStream

public class FastByteArrayOutputStream extends OutputStream
{
	private static final class FastByteArrayInputStream extends UpdateMessageDigestInputStream
	{

		private final FastByteArrayOutputStream fastByteArrayOutputStream;
		private final Iterator buffersIterator;
		private byte currentBuffer[];
		private int currentBufferLength;
		private int nextIndexInCurrentBuffer;
		private int totalBytesRead;

		public int read()
		{
			if (currentBuffer == null)
				return -1;
			if (nextIndexInCurrentBuffer < currentBufferLength)
			{
				totalBytesRead++;
				return currentBuffer[nextIndexInCurrentBuffer++];
			}
			if (buffersIterator.hasNext())
			{
				currentBuffer = (byte[])buffersIterator.next();
				if (currentBuffer == fastByteArrayOutputStream.buffers.getLast())
					currentBufferLength = fastByteArrayOutputStream.index;
				else
					currentBufferLength = currentBuffer.length;
				nextIndexInCurrentBuffer = 0;
			} else
			{
				currentBuffer = null;
			}
			return read();
		}

		public int read(byte b[])
		{
			return read(b, 0, b.length);
		}

		public int read(byte b[], int off, int len)
		{
			if (b == null)
				throw new NullPointerException();
			if (off < 0 || len < 0 || len > b.length - off)
				throw new IndexOutOfBoundsException();
			if (len == 0)
				return 0;
			if (len < 0)
				throw new IllegalArgumentException((new StringBuilder()).append("len must be 0 or greater: ").append(len).toString());
			if (off < 0)
				throw new IllegalArgumentException((new StringBuilder()).append("off must be 0 or greater: ").append(off).toString());
			if (currentBuffer == null)
				return -1;
			if (nextIndexInCurrentBuffer < currentBufferLength)
			{
				int bytesToCopy = Math.min(len, currentBufferLength - nextIndexInCurrentBuffer);
				System.arraycopy(currentBuffer, nextIndexInCurrentBuffer, b, off, bytesToCopy);
				totalBytesRead += bytesToCopy;
				nextIndexInCurrentBuffer += bytesToCopy;
				int remaining = read(b, off + bytesToCopy, len - bytesToCopy);
				return bytesToCopy + Math.max(remaining, 0);
			}
			if (buffersIterator.hasNext())
			{
				currentBuffer = (byte[])buffersIterator.next();
				if (currentBuffer == fastByteArrayOutputStream.buffers.getLast())
					currentBufferLength = fastByteArrayOutputStream.index;
				else
					currentBufferLength = currentBuffer.length;
				nextIndexInCurrentBuffer = 0;
			} else
			{
				currentBuffer = null;
			}
			return read(b, off, len);
		}

		public long skip(long n)
			throws IOException
		{
			if (n > 0x7fffffffL)
				throw new IllegalArgumentException((new StringBuilder()).append("n exceeds maximum (2147483647): ").append(n).toString());
			if (n == 0L)
				return 0L;
			if (n < 0L)
				throw new IllegalArgumentException((new StringBuilder()).append("n must be 0 or greater: ").append(n).toString());
			int len = (int)n;
			if (currentBuffer == null)
				return 0L;
			if (nextIndexInCurrentBuffer < currentBufferLength)
			{
				int bytesToSkip = Math.min(len, currentBufferLength - nextIndexInCurrentBuffer);
				totalBytesRead += bytesToSkip;
				nextIndexInCurrentBuffer += bytesToSkip;
				return (long)bytesToSkip + skip(len - bytesToSkip);
			}
			if (buffersIterator.hasNext())
			{
				currentBuffer = (byte[])buffersIterator.next();
				if (currentBuffer == fastByteArrayOutputStream.buffers.getLast())
					currentBufferLength = fastByteArrayOutputStream.index;
				else
					currentBufferLength = currentBuffer.length;
				nextIndexInCurrentBuffer = 0;
			} else
			{
				currentBuffer = null;
			}
			return skip(len);
		}

		public int available()
		{
			return fastByteArrayOutputStream.size() - totalBytesRead;
		}

		public void updateMessageDigest(MessageDigest messageDigest)
		{
			updateMessageDigest(messageDigest, available());
		}

		public void updateMessageDigest(MessageDigest messageDigest, int len)
		{
			if (currentBuffer == null)
				return;
			if (len == 0)
				return;
			if (len < 0)
				throw new IllegalArgumentException((new StringBuilder()).append("len must be 0 or greater: ").append(len).toString());
			if (nextIndexInCurrentBuffer < currentBufferLength)
			{
				int bytesToCopy = Math.min(len, currentBufferLength - nextIndexInCurrentBuffer);
				messageDigest.update(currentBuffer, nextIndexInCurrentBuffer, bytesToCopy);
				nextIndexInCurrentBuffer += bytesToCopy;
				updateMessageDigest(messageDigest, len - bytesToCopy);
			} else
			{
				if (buffersIterator.hasNext())
				{
					currentBuffer = (byte[])buffersIterator.next();
					if (currentBuffer == fastByteArrayOutputStream.buffers.getLast())
						currentBufferLength = fastByteArrayOutputStream.index;
					else
						currentBufferLength = currentBuffer.length;
					nextIndexInCurrentBuffer = 0;
				} else
				{
					currentBuffer = null;
				}
				updateMessageDigest(messageDigest, len);
			}
		}

		public FastByteArrayInputStream(FastByteArrayOutputStream fastByteArrayOutputStream)
		{
			currentBufferLength = 0;
			nextIndexInCurrentBuffer = 0;
			totalBytesRead = 0;
			this.fastByteArrayOutputStream = fastByteArrayOutputStream;
			buffersIterator = fastByteArrayOutputStream.buffers.iterator();
			if (buffersIterator.hasNext())
			{
				currentBuffer = (byte[])buffersIterator.next();
				if (currentBuffer == fastByteArrayOutputStream.buffers.getLast())
					currentBufferLength = fastByteArrayOutputStream.index;
				else
					currentBufferLength = currentBuffer.length;
			}
		}
	}


	private static final int DEFAULT_BLOCK_SIZE = 256;
	private final LinkedList buffers;
	private final int initialBlockSize;
	private int nextBlockSize;
	private int alreadyBufferedSize;
	private int index;
	private boolean closed;

	public FastByteArrayOutputStream()
	{
		this(256);
	}

	public FastByteArrayOutputStream(int initialBlockSize)
	{
		buffers = new LinkedList();
		nextBlockSize = 0;
		alreadyBufferedSize = 0;
		index = 0;
		closed = false;
		Assert.isTrue(initialBlockSize > 0, "Initial block size must be greater than 0");
		this.initialBlockSize = initialBlockSize;
		nextBlockSize = initialBlockSize;
	}

	public void write(int datum)
		throws IOException
	{
		if (closed)
			throw new IOException("Stream closed");
		if (buffers.peekLast() == null || ((byte[])buffers.getLast()).length == index)
			addBuffer(1);
		((byte[])buffers.getLast())[index++] = (byte)datum;
	}

	public void write(byte data[], int offset, int length)
		throws IOException
	{
		if (data == null)
			throw new NullPointerException();
		if (offset < 0 || offset + length > data.length || length < 0)
			throw new IndexOutOfBoundsException();
		if (closed)
			throw new IOException("Stream closed");
		if (buffers.peekLast() == null || ((byte[])buffers.getLast()).length == index)
			addBuffer(length);
		if (index + length > ((byte[])buffers.getLast()).length)
		{
			int pos = offset;
			do
			{
				if (index == ((byte[])buffers.getLast()).length)
					addBuffer(length);
				int copyLength = ((byte[])buffers.getLast()).length - index;
				if (length < copyLength)
					copyLength = length;
				System.arraycopy(data, pos, buffers.getLast(), index, copyLength);
				pos += copyLength;
				index += copyLength;
				length -= copyLength;
			} while (length > 0);
		} else
		{
			System.arraycopy(data, offset, buffers.getLast(), index, length);
			index += length;
		}
	}

	public void close()
	{
		closed = true;
	}

	public String toString()
	{
		return new String(toByteArrayUnsafe());
	}

	public int size()
	{
		return alreadyBufferedSize + index;
	}

	public byte[] toByteArrayUnsafe()
	{
		int totalSize = size();
		if (totalSize == 0)
		{
			return new byte[0];
		} else
		{
			resize(totalSize);
			return (byte[])buffers.getFirst();
		}
	}

	public byte[] toByteArray()
	{
		byte bytesUnsafe[] = toByteArrayUnsafe();
		byte ret[] = new byte[bytesUnsafe.length];
		System.arraycopy(bytesUnsafe, 0, ret, 0, bytesUnsafe.length);
		return ret;
	}

	public void reset()
	{
		buffers.clear();
		nextBlockSize = initialBlockSize;
		closed = false;
		index = 0;
		alreadyBufferedSize = 0;
	}

	public InputStream getInputStream()
	{
		return new FastByteArrayInputStream(this);
	}

	public void writeTo(OutputStream out)
		throws IOException
	{
		for (Iterator it = buffers.iterator(); it.hasNext();)
		{
			byte bytes[] = (byte[])it.next();
			if (it.hasNext())
				out.write(bytes, 0, bytes.length);
			else
				out.write(bytes, 0, index);
		}

	}

	public void resize(int targetCapacity)
	{
		Assert.isTrue(targetCapacity >= size(), "New capacity must not be smaller than current size");
		if (buffers.peekFirst() == null)
			nextBlockSize = targetCapacity - size();
		else
		if (size() != targetCapacity || ((byte[])buffers.getFirst()).length != targetCapacity)
		{
			int totalSize = size();
			byte data[] = new byte[targetCapacity];
			int pos = 0;
			for (Iterator it = buffers.iterator(); it.hasNext();)
			{
				byte bytes[] = (byte[])it.next();
				if (it.hasNext())
				{
					System.arraycopy(bytes, 0, data, pos, bytes.length);
					pos += bytes.length;
				} else
				{
					System.arraycopy(bytes, 0, data, pos, index);
				}
			}

			buffers.clear();
			buffers.add(data);
			index = totalSize;
			alreadyBufferedSize = 0;
		}
	}

	private void addBuffer(int minCapacity)
	{
		if (buffers.peekLast() != null)
		{
			alreadyBufferedSize += index;
			index = 0;
		}
		if (nextBlockSize < minCapacity)
			nextBlockSize = nextPowerOf2(minCapacity);
		buffers.add(new byte[nextBlockSize]);
		nextBlockSize *= 2;
	}

	private static int nextPowerOf2(int val)
	{
		val = --val >> 1 | val;
		val = val >> 2 | val;
		val = val >> 4 | val;
		val = val >> 8 | val;
		val = val >> 16 | val;
		return ++val;
	}


}
