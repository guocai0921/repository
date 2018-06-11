// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   TypeReference.java

package org.springframework.asm;


public class TypeReference
{

	public static final int CLASS_TYPE_PARAMETER = 0;
	public static final int METHOD_TYPE_PARAMETER = 1;
	public static final int CLASS_EXTENDS = 16;
	public static final int CLASS_TYPE_PARAMETER_BOUND = 17;
	public static final int METHOD_TYPE_PARAMETER_BOUND = 18;
	public static final int FIELD = 19;
	public static final int METHOD_RETURN = 20;
	public static final int METHOD_RECEIVER = 21;
	public static final int METHOD_FORMAL_PARAMETER = 22;
	public static final int THROWS = 23;
	public static final int LOCAL_VARIABLE = 64;
	public static final int RESOURCE_VARIABLE = 65;
	public static final int EXCEPTION_PARAMETER = 66;
	public static final int INSTANCEOF = 67;
	public static final int NEW = 68;
	public static final int CONSTRUCTOR_REFERENCE = 69;
	public static final int METHOD_REFERENCE = 70;
	public static final int CAST = 71;
	public static final int CONSTRUCTOR_INVOCATION_TYPE_ARGUMENT = 72;
	public static final int METHOD_INVOCATION_TYPE_ARGUMENT = 73;
	public static final int CONSTRUCTOR_REFERENCE_TYPE_ARGUMENT = 74;
	public static final int METHOD_REFERENCE_TYPE_ARGUMENT = 75;
	private int value;

	public TypeReference(int typeRef)
	{
		value = typeRef;
	}

	public static TypeReference newTypeReference(int sort)
	{
		return new TypeReference(sort << 24);
	}

	public static TypeReference newTypeParameterReference(int sort, int paramIndex)
	{
		return new TypeReference(sort << 24 | paramIndex << 16);
	}

	public static TypeReference newTypeParameterBoundReference(int sort, int paramIndex, int boundIndex)
	{
		return new TypeReference(sort << 24 | paramIndex << 16 | boundIndex << 8);
	}

	public static TypeReference newSuperTypeReference(int itfIndex)
	{
		itfIndex &= 0xffff;
		return new TypeReference(0x10000000 | itfIndex << 8);
	}

	public static TypeReference newFormalParameterReference(int paramIndex)
	{
		return new TypeReference(0x16000000 | paramIndex << 16);
	}

	public static TypeReference newExceptionReference(int exceptionIndex)
	{
		return new TypeReference(0x17000000 | exceptionIndex << 8);
	}

	public static TypeReference newTryCatchReference(int tryCatchBlockIndex)
	{
		return new TypeReference(0x42000000 | tryCatchBlockIndex << 8);
	}

	public static TypeReference newTypeArgumentReference(int sort, int argIndex)
	{
		return new TypeReference(sort << 24 | argIndex);
	}

	public int getSort()
	{
		return value >>> 24;
	}

	public int getTypeParameterIndex()
	{
		return (value & 0xff0000) >> 16;
	}

	public int getTypeParameterBoundIndex()
	{
		return (value & 0xff00) >> 8;
	}

	public int getSuperTypeIndex()
	{
		return (short)((value & 0xffff00) >> 8);
	}

	public int getFormalParameterIndex()
	{
		return (value & 0xff0000) >> 16;
	}

	public int getExceptionIndex()
	{
		return (value & 0xffff00) >> 8;
	}

	public int getTryCatchBlockIndex()
	{
		return (value & 0xffff00) >> 8;
	}

	public int getTypeArgumentIndex()
	{
		return value & 0xff;
	}

	public int getValue()
	{
		return value;
	}
}
