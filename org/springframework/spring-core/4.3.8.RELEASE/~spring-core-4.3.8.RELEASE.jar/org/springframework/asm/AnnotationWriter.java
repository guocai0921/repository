// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AnnotationWriter.java

package org.springframework.asm;


// Referenced classes of package org.springframework.asm:
//			AnnotationVisitor, Opcodes, Type, ByteVector, 
//			ClassWriter, Item, TypePath

final class AnnotationWriter extends AnnotationVisitor
{

	private final ClassWriter cw;
	private int size;
	private final boolean named;
	private final ByteVector bv;
	private final ByteVector parent;
	private final int offset;
	AnnotationWriter next;
	AnnotationWriter prev;

	AnnotationWriter(ClassWriter cw, boolean named, ByteVector bv, ByteVector parent, int offset)
	{
		super(0x50000);
		this.cw = cw;
		this.named = named;
		this.bv = bv;
		this.parent = parent;
		this.offset = offset;
	}

	public void visit(String name, Object value)
	{
		size++;
		if (named)
			bv.putShort(cw.newUTF8(name));
		if (value instanceof String)
			bv.put12(115, cw.newUTF8((String)value));
		else
		if (value instanceof Byte)
			bv.put12(66, cw.newInteger(((Byte)value).byteValue()).index);
		else
		if (value instanceof Boolean)
		{
			int v = ((Boolean)value).booleanValue() ? 1 : 0;
			bv.put12(90, cw.newInteger(v).index);
		} else
		if (value instanceof Character)
			bv.put12(67, cw.newInteger(((Character)value).charValue()).index);
		else
		if (value instanceof Short)
			bv.put12(83, cw.newInteger(((Short)value).shortValue()).index);
		else
		if (value instanceof Type)
			bv.put12(99, cw.newUTF8(((Type)value).getDescriptor()));
		else
		if (value instanceof byte[])
		{
			byte v[] = (byte[])(byte[])value;
			bv.put12(91, v.length);
			for (int i = 0; i < v.length; i++)
				bv.put12(66, cw.newInteger(v[i]).index);

		} else
		if (value instanceof boolean[])
		{
			boolean v[] = (boolean[])(boolean[])value;
			bv.put12(91, v.length);
			for (int i = 0; i < v.length; i++)
				bv.put12(90, cw.newInteger(v[i] ? 1 : 0).index);

		} else
		if (value instanceof short[])
		{
			short v[] = (short[])(short[])value;
			bv.put12(91, v.length);
			for (int i = 0; i < v.length; i++)
				bv.put12(83, cw.newInteger(v[i]).index);

		} else
		if (value instanceof char[])
		{
			char v[] = (char[])(char[])value;
			bv.put12(91, v.length);
			for (int i = 0; i < v.length; i++)
				bv.put12(67, cw.newInteger(v[i]).index);

		} else
		if (value instanceof int[])
		{
			int v[] = (int[])(int[])value;
			bv.put12(91, v.length);
			for (int i = 0; i < v.length; i++)
				bv.put12(73, cw.newInteger(v[i]).index);

		} else
		if (value instanceof long[])
		{
			long v[] = (long[])(long[])value;
			bv.put12(91, v.length);
			for (int i = 0; i < v.length; i++)
				bv.put12(74, cw.newLong(v[i]).index);

		} else
		if (value instanceof float[])
		{
			float v[] = (float[])(float[])value;
			bv.put12(91, v.length);
			for (int i = 0; i < v.length; i++)
				bv.put12(70, cw.newFloat(v[i]).index);

		} else
		if (value instanceof double[])
		{
			double v[] = (double[])(double[])value;
			bv.put12(91, v.length);
			for (int i = 0; i < v.length; i++)
				bv.put12(68, cw.newDouble(v[i]).index);

		} else
		{
			Item i = cw.newConstItem(value);
			bv.put12(".s.IFJDCS".charAt(i.type), i.index);
		}
	}

	public void visitEnum(String name, String desc, String value)
	{
		size++;
		if (named)
			bv.putShort(cw.newUTF8(name));
		bv.put12(101, cw.newUTF8(desc)).putShort(cw.newUTF8(value));
	}

	public AnnotationVisitor visitAnnotation(String name, String desc)
	{
		size++;
		if (named)
			bv.putShort(cw.newUTF8(name));
		bv.put12(64, cw.newUTF8(desc)).putShort(0);
		return new AnnotationWriter(cw, true, bv, bv, bv.length - 2);
	}

	public AnnotationVisitor visitArray(String name)
	{
		size++;
		if (named)
			bv.putShort(cw.newUTF8(name));
		bv.put12(91, 0);
		return new AnnotationWriter(cw, false, bv, bv, bv.length - 2);
	}

	public void visitEnd()
	{
		if (parent != null)
		{
			byte data[] = parent.data;
			data[offset] = (byte)(size >>> 8);
			data[offset + 1] = (byte)size;
		}
	}

	int getSize()
	{
		int size = 0;
		for (AnnotationWriter aw = this; aw != null; aw = aw.next)
			size += aw.bv.length;

		return size;
	}

	void put(ByteVector out)
	{
		int n = 0;
		int size = 2;
		AnnotationWriter aw = this;
		AnnotationWriter last = null;
		for (; aw != null; aw = aw.next)
		{
			n++;
			size += aw.bv.length;
			aw.visitEnd();
			aw.prev = last;
			last = aw;
		}

		out.putInt(size);
		out.putShort(n);
		for (aw = last; aw != null; aw = aw.prev)
			out.putByteArray(aw.bv.data, 0, aw.bv.length);

	}

	static void put(AnnotationWriter panns[], int off, ByteVector out)
	{
		int size = 1 + 2 * (panns.length - off);
		for (int i = off; i < panns.length; i++)
			size += panns[i] != null ? panns[i].getSize() : 0;

		out.putInt(size).putByte(panns.length - off);
		for (int i = off; i < panns.length; i++)
		{
			AnnotationWriter aw = panns[i];
			AnnotationWriter last = null;
			int n = 0;
			for (; aw != null; aw = aw.next)
			{
				n++;
				aw.visitEnd();
				aw.prev = last;
				last = aw;
			}

			out.putShort(n);
			for (aw = last; aw != null; aw = aw.prev)
				out.putByteArray(aw.bv.data, 0, aw.bv.length);

		}

	}

	static void putTarget(int typeRef, TypePath typePath, ByteVector out)
	{
		switch (typeRef >>> 24)
		{
		case 0: // '\0'
		case 1: // '\001'
		case 22: // '\026'
			out.putShort(typeRef >>> 16);
			break;

		case 19: // '\023'
		case 20: // '\024'
		case 21: // '\025'
			out.putByte(typeRef >>> 24);
			break;

		case 71: // 'G'
		case 72: // 'H'
		case 73: // 'I'
		case 74: // 'J'
		case 75: // 'K'
			out.putInt(typeRef);
			break;

		default:
			out.put12(typeRef >>> 24, (typeRef & 0xffff00) >> 8);
			break;
		}
		if (typePath == null)
		{
			out.putByte(0);
		} else
		{
			int length = typePath.b[typePath.offset] * 2 + 1;
			out.putByteArray(typePath.b, typePath.offset, length);
		}
	}
}
