// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   TypePath.java

package org.springframework.asm;


// Referenced classes of package org.springframework.asm:
//			ByteVector

public class TypePath
{

	public static final int ARRAY_ELEMENT = 0;
	public static final int INNER_TYPE = 1;
	public static final int WILDCARD_BOUND = 2;
	public static final int TYPE_ARGUMENT = 3;
	byte b[];
	int offset;

	TypePath(byte b[], int offset)
	{
		this.b = b;
		this.offset = offset;
	}

	public int getLength()
	{
		return b[offset];
	}

	public int getStep(int index)
	{
		return b[offset + 2 * index + 1];
	}

	public int getStepArgument(int index)
	{
		return b[offset + 2 * index + 2];
	}

	public static TypePath fromString(String typePath)
	{
		if (typePath == null || typePath.length() == 0)
			return null;
		int n = typePath.length();
		ByteVector out = new ByteVector(n);
		out.putByte(0);
		int i = 0;
		do
		{
			if (i >= n)
				break;
			char c = typePath.charAt(i++);
			if (c == '[')
				out.put11(0, 0);
			else
			if (c == '.')
				out.put11(1, 0);
			else
			if (c == '*')
				out.put11(2, 0);
			else
			if (c >= '0' && c <= '9')
			{
				int typeArg = c - 48;
				for (; i < n && (c = typePath.charAt(i)) >= '0' && c <= '9'; i++)
					typeArg = (typeArg * 10 + c) - 48;

				if (i < n && typePath.charAt(i) == ';')
					i++;
				out.put11(3, typeArg);
			}
		} while (true);
		out.data[0] = (byte)(out.length / 2);
		return new TypePath(out.data, 0);
	}

	public String toString()
	{
		int length = getLength();
		StringBuilder result = new StringBuilder(length * 2);
		for (int i = 0; i < length; i++)
			switch (getStep(i))
			{
			case 0: // '\0'
				result.append('[');
				break;

			case 1: // '\001'
				result.append('.');
				break;

			case 2: // '\002'
				result.append('*');
				break;

			case 3: // '\003'
				result.append(getStepArgument(i)).append(';');
				break;

			default:
				result.append('_');
				break;
			}

		return result.toString();
	}
}
