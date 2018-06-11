// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Item.java

package org.springframework.asm;


// Referenced classes of package org.springframework.asm:
//			ClassWriter

final class Item
{

	int index;
	int type;
	int intVal;
	long longVal;
	String strVal1;
	String strVal2;
	String strVal3;
	int hashCode;
	Item next;

	Item()
	{
	}

	Item(int index)
	{
		this.index = index;
	}

	Item(int index, Item i)
	{
		this.index = index;
		type = i.type;
		intVal = i.intVal;
		longVal = i.longVal;
		strVal1 = i.strVal1;
		strVal2 = i.strVal2;
		strVal3 = i.strVal3;
		hashCode = i.hashCode;
	}

	void set(int intVal)
	{
		type = 3;
		this.intVal = intVal;
		hashCode = 0x7fffffff & type + intVal;
	}

	void set(long longVal)
	{
		type = 5;
		this.longVal = longVal;
		hashCode = 0x7fffffff & type + (int)longVal;
	}

	void set(float floatVal)
	{
		type = 4;
		intVal = Float.floatToRawIntBits(floatVal);
		hashCode = 0x7fffffff & type + (int)floatVal;
	}

	void set(double doubleVal)
	{
		type = 6;
		longVal = Double.doubleToRawLongBits(doubleVal);
		hashCode = 0x7fffffff & type + (int)doubleVal;
	}

	void set(int type, String strVal1, String strVal2, String strVal3)
	{
		this.type = type;
		this.strVal1 = strVal1;
		this.strVal2 = strVal2;
		this.strVal3 = strVal3;
		switch (type)
		{
		case 7: // '\007'
			intVal = 0;
			// fall through

		case 1: // '\001'
		case 8: // '\b'
		case 16: // '\020'
		case 30: // '\036'
			hashCode = 0x7fffffff & type + strVal1.hashCode();
			return;

		case 12: // '\f'
			hashCode = 0x7fffffff & type + strVal1.hashCode() * strVal2.hashCode();
			return;

		default:
			hashCode = 0x7fffffff & type + strVal1.hashCode() * strVal2.hashCode() * strVal3.hashCode();
			return;
		}
	}

	void set(String name, String desc, int bsmIndex)
	{
		type = 18;
		longVal = bsmIndex;
		strVal1 = name;
		strVal2 = desc;
		hashCode = 0x7fffffff & 18 + bsmIndex * strVal1.hashCode() * strVal2.hashCode();
	}

	void set(int position, int hashCode)
	{
		type = 33;
		intVal = position;
		this.hashCode = hashCode;
	}

	boolean isEqualTo(Item i)
	{
		switch (type)
		{
		case 1: // '\001'
		case 7: // '\007'
		case 8: // '\b'
		case 16: // '\020'
		case 30: // '\036'
			return i.strVal1.equals(strVal1);

		case 5: // '\005'
		case 6: // '\006'
		case 32: // ' '
			return i.longVal == longVal;

		case 3: // '\003'
		case 4: // '\004'
			return i.intVal == intVal;

		case 31: // '\037'
			return i.intVal == intVal && i.strVal1.equals(strVal1);

		case 12: // '\f'
			return i.strVal1.equals(strVal1) && i.strVal2.equals(strVal2);

		case 18: // '\022'
			return i.longVal == longVal && i.strVal1.equals(strVal1) && i.strVal2.equals(strVal2);

		case 2: // '\002'
		case 9: // '\t'
		case 10: // '\n'
		case 11: // '\013'
		case 13: // '\r'
		case 14: // '\016'
		case 15: // '\017'
		case 17: // '\021'
		case 19: // '\023'
		case 20: // '\024'
		case 21: // '\025'
		case 22: // '\026'
		case 23: // '\027'
		case 24: // '\030'
		case 25: // '\031'
		case 26: // '\032'
		case 27: // '\033'
		case 28: // '\034'
		case 29: // '\035'
		default:
			return i.strVal1.equals(strVal1) && i.strVal2.equals(strVal2) && i.strVal3.equals(strVal3);
		}
	}
}
