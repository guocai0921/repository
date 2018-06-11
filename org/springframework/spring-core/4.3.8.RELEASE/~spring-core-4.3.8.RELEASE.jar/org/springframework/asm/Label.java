// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Label.java

package org.springframework.asm;


// Referenced classes of package org.springframework.asm:
//			Opcodes, Edge, ByteVector, Frame, 
//			MethodWriter

public class Label
{

	static final int DEBUG = 1;
	static final int RESOLVED = 2;
	static final int RESIZED = 4;
	static final int PUSHED = 8;
	static final int TARGET = 16;
	static final int STORE = 32;
	static final int REACHABLE = 64;
	static final int JSR = 128;
	static final int RET = 256;
	static final int SUBROUTINE = 512;
	static final int VISITED = 1024;
	static final int VISITED2 = 2048;
	public Object info;
	int status;
	int line;
	int position;
	private int referenceCount;
	private int srcAndRefPositions[];
	int inputStackTop;
	int outputStackMax;
	Frame frame;
	Label successor;
	Edge successors;
	Label next;

	public Label()
	{
	}

	public int getOffset()
	{
		if ((status & 2) == 0)
			throw new IllegalStateException("Label offset position has not been resolved yet");
		else
			return position;
	}

	void put(MethodWriter owner, ByteVector out, int source, boolean wideOffset)
	{
		if ((status & 2) == 0)
		{
			if (wideOffset)
			{
				addReference(-1 - source, out.length);
				out.putInt(-1);
			} else
			{
				addReference(source, out.length);
				out.putShort(-1);
			}
		} else
		if (wideOffset)
			out.putInt(position - source);
		else
			out.putShort(position - source);
	}

	private void addReference(int sourcePosition, int referencePosition)
	{
		if (srcAndRefPositions == null)
			srcAndRefPositions = new int[6];
		if (referenceCount >= srcAndRefPositions.length)
		{
			int a[] = new int[srcAndRefPositions.length + 6];
			System.arraycopy(srcAndRefPositions, 0, a, 0, srcAndRefPositions.length);
			srcAndRefPositions = a;
		}
		srcAndRefPositions[referenceCount++] = sourcePosition;
		srcAndRefPositions[referenceCount++] = referencePosition;
	}

	boolean resolve(MethodWriter owner, int position, byte data[])
	{
		boolean needUpdate = false;
		status |= 2;
		this.position = position;
		for (int i = 0; i < referenceCount;)
		{
			int source = srcAndRefPositions[i++];
			int reference = srcAndRefPositions[i++];
			if (source >= 0)
			{
				int offset = position - source;
				if (offset < -32768 || offset > 32767)
				{
					int opcode = data[reference - 1] & 0xff;
					if (opcode <= 168)
						data[reference - 1] = (byte)(opcode + 49);
					else
						data[reference - 1] = (byte)(opcode + 20);
					needUpdate = true;
				}
				data[reference++] = (byte)(offset >>> 8);
				data[reference] = (byte)offset;
			} else
			{
				int offset = position + source + 1;
				data[reference++] = (byte)(offset >>> 24);
				data[reference++] = (byte)(offset >>> 16);
				data[reference++] = (byte)(offset >>> 8);
				data[reference] = (byte)offset;
			}
		}

		return needUpdate;
	}

	Label getFirst()
	{
		return frame != null ? frame.owner : this;
	}

	boolean inSubroutine(long id)
	{
		if ((status & 0x400) != 0)
			return (srcAndRefPositions[(int)(id >>> 32)] & (int)id) != 0;
		else
			return false;
	}

	boolean inSameSubroutine(Label block)
	{
		if ((status & 0x400) == 0 || (block.status & 0x400) == 0)
			return false;
		for (int i = 0; i < srcAndRefPositions.length; i++)
			if ((srcAndRefPositions[i] & block.srcAndRefPositions[i]) != 0)
				return true;

		return false;
	}

	void addToSubroutine(long id, int nbSubroutines)
	{
		if ((status & 0x400) == 0)
		{
			status |= 0x400;
			srcAndRefPositions = new int[nbSubroutines / 32 + 1];
		}
		srcAndRefPositions[(int)(id >>> 32)] |= (int)id;
	}

	void visitSubroutine(Label JSR, long id, int nbSubroutines)
	{
		Label stack = this;
		do
		{
			if (stack == null)
				break;
			Label l = stack;
			stack = l.next;
			l.next = null;
			Edge e;
			if (JSR != null)
			{
				if ((l.status & 0x800) != 0)
					continue;
				l.status |= 0x800;
				if ((l.status & 0x100) != 0 && !l.inSameSubroutine(JSR))
				{
					e = new Edge();
					e.info = l.inputStackTop;
					e.successor = JSR.successors.successor;
					e.next = l.successors;
					l.successors = e;
				}
			} else
			{
				if (l.inSubroutine(id))
					continue;
				l.addToSubroutine(id, nbSubroutines);
			}
			e = l.successors;
			while (e != null) 
			{
				if (((l.status & 0x80) == 0 || e != l.successors.next) && e.successor.next == null)
				{
					e.successor.next = stack;
					stack = e.successor;
				}
				e = e.next;
			}
		} while (true);
	}

	public String toString()
	{
		return (new StringBuilder()).append("L").append(System.identityHashCode(this)).toString();
	}
}
