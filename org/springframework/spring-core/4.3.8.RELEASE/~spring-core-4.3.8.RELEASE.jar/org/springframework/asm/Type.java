// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Type.java

package org.springframework.asm;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

// Referenced classes of package org.springframework.asm:
//			Opcodes

public class Type
{

	public static final int VOID = 0;
	public static final int BOOLEAN = 1;
	public static final int CHAR = 2;
	public static final int BYTE = 3;
	public static final int SHORT = 4;
	public static final int INT = 5;
	public static final int FLOAT = 6;
	public static final int LONG = 7;
	public static final int DOUBLE = 8;
	public static final int ARRAY = 9;
	public static final int OBJECT = 10;
	public static final int METHOD = 11;
	public static final Type VOID_TYPE = new Type(0, null, 0x56050000, 1);
	public static final Type BOOLEAN_TYPE = new Type(1, null, 0x5a000501, 1);
	public static final Type CHAR_TYPE = new Type(2, null, 0x43000601, 1);
	public static final Type BYTE_TYPE = new Type(3, null, 0x42000501, 1);
	public static final Type SHORT_TYPE = new Type(4, null, 0x53000701, 1);
	public static final Type INT_TYPE = new Type(5, null, 0x49000001, 1);
	public static final Type FLOAT_TYPE = new Type(6, null, 0x46020201, 1);
	public static final Type LONG_TYPE = new Type(7, null, 0x4a010102, 1);
	public static final Type DOUBLE_TYPE = new Type(8, null, 0x44030302, 1);
	private final int sort;
	private final char buf[];
	private final int off;
	private final int len;

	private Type(int sort, char buf[], int off, int len)
	{
		this.sort = sort;
		this.buf = buf;
		this.off = off;
		this.len = len;
	}

	public static Type getType(String typeDescriptor)
	{
		return getType(typeDescriptor.toCharArray(), 0);
	}

	public static Type getObjectType(String internalName)
	{
		char buf[] = internalName.toCharArray();
		return new Type(buf[0] != '[' ? 10 : 9, buf, 0, buf.length);
	}

	public static Type getMethodType(String methodDescriptor)
	{
		return getType(methodDescriptor.toCharArray(), 0);
	}

	public static transient Type getMethodType(Type returnType, Type argumentTypes[])
	{
		return getType(getMethodDescriptor(returnType, argumentTypes));
	}

	public static Type getType(Class c)
	{
		if (c.isPrimitive())
		{
			if (c == Integer.TYPE)
				return INT_TYPE;
			if (c == Void.TYPE)
				return VOID_TYPE;
			if (c == Boolean.TYPE)
				return BOOLEAN_TYPE;
			if (c == Byte.TYPE)
				return BYTE_TYPE;
			if (c == Character.TYPE)
				return CHAR_TYPE;
			if (c == Short.TYPE)
				return SHORT_TYPE;
			if (c == Double.TYPE)
				return DOUBLE_TYPE;
			if (c == Float.TYPE)
				return FLOAT_TYPE;
			else
				return LONG_TYPE;
		} else
		{
			return getType(getDescriptor(c));
		}
	}

	public static Type getType(Constructor c)
	{
		return getType(getConstructorDescriptor(c));
	}

	public static Type getType(Method m)
	{
		return getType(getMethodDescriptor(m));
	}

	public static Type[] getArgumentTypes(String methodDescriptor)
	{
		char buf[] = methodDescriptor.toCharArray();
		int off = 1;
		int size = 0;
		do
		{
			char car = buf[off++];
			if (car == ')')
				break;
			if (car == 'L')
			{
				while (buf[off++] != ';') ;
				size++;
			} else
			if (car != '[')
				size++;
		} while (true);
		Type args[] = new Type[size];
		off = 1;
		for (size = 0; buf[off] != ')'; size++)
		{
			args[size] = getType(buf, off);
			off += args[size].len + (args[size].sort != 10 ? 0 : 2);
		}

		return args;
	}

	public static Type[] getArgumentTypes(Method method)
	{
		Class classes[] = method.getParameterTypes();
		Type types[] = new Type[classes.length];
		for (int i = classes.length - 1; i >= 0; i--)
			types[i] = getType(classes[i]);

		return types;
	}

	public static Type getReturnType(String methodDescriptor)
	{
		char buf[] = methodDescriptor.toCharArray();
		int off = 1;
		do
		{
			char car;
			do
			{
				car = buf[off++];
				if (car == ')')
					return getType(buf, off);
			} while (car != 'L');
			while (buf[off++] != ';') ;
		} while (true);
	}

	public static Type getReturnType(Method method)
	{
		return getType(method.getReturnType());
	}

	public static int getArgumentsAndReturnSizes(String desc)
	{
		int n = 1;
		int c = 1;
		do
		{
			char car = desc.charAt(c++);
			if (car == ')')
			{
				car = desc.charAt(c);
				return n << 2 | (car != 'V' ? car != 'D' && car != 'J' ? 1 : 2 : 0);
			}
			if (car == 'L')
			{
				while (desc.charAt(c++) != ';') ;
				n++;
			} else
			if (car == '[')
			{
				while ((car = desc.charAt(c)) == '[') 
					c++;
				if (car == 'D' || car == 'J')
					n--;
			} else
			if (car == 'D' || car == 'J')
				n += 2;
			else
				n++;
		} while (true);
	}

	private static Type getType(char buf[], int off)
	{
		switch (buf[off])
		{
		case 86: // 'V'
		{
			return VOID_TYPE;
		}

		case 90: // 'Z'
		{
			return BOOLEAN_TYPE;
		}

		case 67: // 'C'
		{
			return CHAR_TYPE;
		}

		case 66: // 'B'
		{
			return BYTE_TYPE;
		}

		case 83: // 'S'
		{
			return SHORT_TYPE;
		}

		case 73: // 'I'
		{
			return INT_TYPE;
		}

		case 70: // 'F'
		{
			return FLOAT_TYPE;
		}

		case 74: // 'J'
		{
			return LONG_TYPE;
		}

		case 68: // 'D'
		{
			return DOUBLE_TYPE;
		}

		case 91: // '['
		{
			int len;
			for (len = 1; buf[off + len] == '['; len++);
			if (buf[off + len] == 'L')
				for (len++; buf[off + len] != ';'; len++);
			return new Type(9, buf, off, len + 1);
		}

		case 76: // 'L'
		{
			int len;
			for (len = 1; buf[off + len] != ';'; len++);
			return new Type(10, buf, off + 1, len - 1);
		}

		case 69: // 'E'
		case 71: // 'G'
		case 72: // 'H'
		case 75: // 'K'
		case 77: // 'M'
		case 78: // 'N'
		case 79: // 'O'
		case 80: // 'P'
		case 81: // 'Q'
		case 82: // 'R'
		case 84: // 'T'
		case 85: // 'U'
		case 87: // 'W'
		case 88: // 'X'
		case 89: // 'Y'
		default:
		{
			return new Type(11, buf, off, buf.length - off);
		}
		}
	}

	public int getSort()
	{
		return sort;
	}

	public int getDimensions()
	{
		int i;
		for (i = 1; buf[off + i] == '['; i++);
		return i;
	}

	public Type getElementType()
	{
		return getType(buf, off + getDimensions());
	}

	public String getClassName()
	{
		switch (sort)
		{
		case 0: // '\0'
			return "void";

		case 1: // '\001'
			return "boolean";

		case 2: // '\002'
			return "char";

		case 3: // '\003'
			return "byte";

		case 4: // '\004'
			return "short";

		case 5: // '\005'
			return "int";

		case 6: // '\006'
			return "float";

		case 7: // '\007'
			return "long";

		case 8: // '\b'
			return "double";

		case 9: // '\t'
			StringBuilder sb = new StringBuilder(getElementType().getClassName());
			for (int i = getDimensions(); i > 0; i--)
				sb.append("[]");

			return sb.toString();

		case 10: // '\n'
			return (new String(buf, off, len)).replace('/', '.').intern();
		}
		return null;
	}

	public String getInternalName()
	{
		return (new String(buf, off, len)).intern();
	}

	public Type[] getArgumentTypes()
	{
		return getArgumentTypes(getDescriptor());
	}

	public Type getReturnType()
	{
		return getReturnType(getDescriptor());
	}

	public int getArgumentsAndReturnSizes()
	{
		return getArgumentsAndReturnSizes(getDescriptor());
	}

	public String getDescriptor()
	{
		StringBuilder buf = new StringBuilder();
		getDescriptor(buf);
		return buf.toString();
	}

	public static transient String getMethodDescriptor(Type returnType, Type argumentTypes[])
	{
		StringBuilder buf = new StringBuilder();
		buf.append('(');
		for (int i = 0; i < argumentTypes.length; i++)
			argumentTypes[i].getDescriptor(buf);

		buf.append(')');
		returnType.getDescriptor(buf);
		return buf.toString();
	}

	private void getDescriptor(StringBuilder buf)
	{
		if (this.buf == null)
			buf.append((char)((off & 0xff000000) >>> 24));
		else
		if (sort == 10)
		{
			buf.append('L');
			buf.append(this.buf, off, len);
			buf.append(';');
		} else
		{
			buf.append(this.buf, off, len);
		}
	}

	public static String getInternalName(Class c)
	{
		return c.getName().replace('.', '/');
	}

	public static String getDescriptor(Class c)
	{
		StringBuilder buf = new StringBuilder();
		getDescriptor(buf, c);
		return buf.toString();
	}

	public static String getConstructorDescriptor(Constructor c)
	{
		Class parameters[] = c.getParameterTypes();
		StringBuilder buf = new StringBuilder();
		buf.append('(');
		for (int i = 0; i < parameters.length; i++)
			getDescriptor(buf, parameters[i]);

		return buf.append(")V").toString();
	}

	public static String getMethodDescriptor(Method m)
	{
		Class parameters[] = m.getParameterTypes();
		StringBuilder buf = new StringBuilder();
		buf.append('(');
		for (int i = 0; i < parameters.length; i++)
			getDescriptor(buf, parameters[i]);

		buf.append(')');
		getDescriptor(buf, m.getReturnType());
		return buf.toString();
	}

	private static void getDescriptor(StringBuilder buf, Class c)
	{
		Class d = c;
		do
		{
			if (d.isPrimitive())
			{
				char car;
				if (d == Integer.TYPE)
					car = 'I';
				else
				if (d == Void.TYPE)
					car = 'V';
				else
				if (d == Boolean.TYPE)
					car = 'Z';
				else
				if (d == Byte.TYPE)
					car = 'B';
				else
				if (d == Character.TYPE)
					car = 'C';
				else
				if (d == Short.TYPE)
					car = 'S';
				else
				if (d == Double.TYPE)
					car = 'D';
				else
				if (d == Float.TYPE)
					car = 'F';
				else
					car = 'J';
				buf.append(car);
				return;
			}
			if (!d.isArray())
				break;
			buf.append('[');
			d = d.getComponentType();
		} while (true);
		buf.append('L');
		String name = d.getName();
		int len = name.length();
		for (int i = 0; i < len; i++)
		{
			char car = name.charAt(i);
			buf.append(car != '.' ? car : '/');
		}

		buf.append(';');
	}

	public int getSize()
	{
		return buf != null ? 1 : off & 0xff;
	}

	public int getOpcode(int opcode)
	{
		if (opcode == 46 || opcode == 79)
			return opcode + (buf != null ? 4 : (off & 0xff00) >> 8);
		else
			return opcode + (buf != null ? 4 : (off & 0xff0000) >> 16);
	}

	public boolean equals(Object o)
	{
		if (this == o)
			return true;
		if (!(o instanceof Type))
			return false;
		Type t = (Type)o;
		if (sort != t.sort)
			return false;
		if (sort >= 9)
		{
			if (len != t.len)
				return false;
			int i = off;
			int j = t.off;
			for (int end = i + len; i < end;)
			{
				if (buf[i] != t.buf[j])
					return false;
				i++;
				j++;
			}

		}
		return true;
	}

	public int hashCode()
	{
		int hc = 13 * sort;
		if (sort >= 9)
		{
			int i = off;
			for (int end = i + len; i < end; i++)
				hc = 17 * (hc + buf[i]);

		}
		return hc;
	}

	public String toString()
	{
		return getDescriptor();
	}

}
