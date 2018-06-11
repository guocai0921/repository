// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   MagicInstantiator.java

package org.springframework.objenesis.instantiator.sun;

import java.io.*;
import org.springframework.objenesis.ObjenesisException;
import org.springframework.objenesis.instantiator.ObjectInstantiator;
import org.springframework.objenesis.instantiator.basic.ClassDefinitionUtils;

public class MagicInstantiator
	implements ObjectInstantiator
{

	private static final int INDEX_CLASS_THIS = 1;
	private static final int INDEX_CLASS_SUPERCLASS = 2;
	private static final int INDEX_UTF8_CONSTRUCTOR_NAME = 3;
	private static final int INDEX_UTF8_CONSTRUCTOR_DESC = 4;
	private static final int INDEX_UTF8_CODE_ATTRIBUTE = 5;
	private static final int INDEX_UTF8_INSTANTIATOR_CLASS = 7;
	private static final int INDEX_UTF8_SUPERCLASS = 8;
	private static final int INDEX_CLASS_INTERFACE = 9;
	private static final int INDEX_UTF8_INTERFACE = 10;
	private static final int INDEX_UTF8_NEWINSTANCE_NAME = 11;
	private static final int INDEX_UTF8_NEWINSTANCE_DESC = 12;
	private static final int INDEX_METHODREF_OBJECT_CONSTRUCTOR = 13;
	private static final int INDEX_CLASS_OBJECT = 14;
	private static final int INDEX_UTF8_OBJECT = 15;
	private static final int INDEX_NAMEANDTYPE_DEFAULT_CONSTRUCTOR = 16;
	private static final int INDEX_CLASS_TYPE = 17;
	private static final int INDEX_UTF8_TYPE = 18;
	private static int CONSTANT_POOL_COUNT = 19;
	private static final byte CONSTRUCTOR_CODE[] = {
		42, -73, 0, 13, -79
	};
	private static final int CONSTRUCTOR_CODE_ATTRIBUTE_LENGTH = 12 + CONSTRUCTOR_CODE.length;
	private static final byte NEWINSTANCE_CODE[] = {
		-69, 0, 17, 89, -73, 0, 13, -80
	};
	private static final int NEWINSTANCE_CODE_ATTRIBUTE_LENGTH = 12 + NEWINSTANCE_CODE.length;
	private static final String CONSTRUCTOR_NAME = "<init>";
	private static final String CONSTRUCTOR_DESC = "()V";
	private ObjectInstantiator instantiator;

	public MagicInstantiator(Class type)
	{
		instantiator = newInstantiatorOf(type);
	}

	public ObjectInstantiator getInstantiator()
	{
		return instantiator;
	}

	private ObjectInstantiator newInstantiatorOf(Class type)
	{
		Class clazz;
		String suffix = type.getSimpleName();
		String className = (new StringBuilder()).append(getClass().getName()).append("$$$").append(suffix).toString();
		clazz = ClassDefinitionUtils.getExistingClass(getClass().getClassLoader(), className);
		if (clazz == null)
		{
			byte classBytes[] = writeExtendingClass(type, className);
			try
			{
				clazz = ClassDefinitionUtils.defineClass(className, classBytes, getClass().getClassLoader());
			}
			catch (Exception e)
			{
				throw new ObjenesisException(e);
			}
		}
		return (ObjectInstantiator)clazz.newInstance();
		InstantiationException e;
		e;
		throw new ObjenesisException(e);
		e;
		throw new ObjenesisException(e);
	}

	private byte[] writeExtendingClass(Class type, String className)
	{
		DataOutputStream in;
		ByteArrayOutputStream bIn;
		Exception exception;
		String clazz = ClassDefinitionUtils.classNameToInternalClassName(className);
		in = null;
		bIn = new ByteArrayOutputStream(1000);
		try
		{
			in = new DataOutputStream(bIn);
			in.write(ClassDefinitionUtils.MAGIC);
			in.write(ClassDefinitionUtils.VERSION);
			in.writeShort(CONSTANT_POOL_COUNT);
			in.writeByte(7);
			in.writeShort(7);
			in.writeByte(7);
			in.writeShort(8);
			in.writeByte(1);
			in.writeUTF("<init>");
			in.writeByte(1);
			in.writeUTF("()V");
			in.writeByte(1);
			in.writeUTF("Code");
			in.writeByte(1);
			in.writeUTF((new StringBuilder()).append("L").append(clazz).append(";").toString());
			in.writeByte(1);
			in.writeUTF(clazz);
			in.writeByte(1);
			in.writeUTF("sun/reflect/MagicAccessorImpl");
			in.writeByte(7);
			in.writeShort(10);
			in.writeByte(1);
			in.writeUTF(org/springframework/objenesis/instantiator/ObjectInstantiator.getName().replace('.', '/'));
			in.writeByte(1);
			in.writeUTF("newInstance");
			in.writeByte(1);
			in.writeUTF("()Ljava/lang/Object;");
			in.writeByte(10);
			in.writeShort(14);
			in.writeShort(16);
			in.writeByte(7);
			in.writeShort(15);
			in.writeByte(1);
			in.writeUTF("java/lang/Object");
			in.writeByte(12);
			in.writeShort(3);
			in.writeShort(4);
			in.writeByte(7);
			in.writeShort(18);
			in.writeByte(1);
			in.writeUTF(ClassDefinitionUtils.classNameToInternalClassName(type.getName()));
			in.writeShort(49);
			in.writeShort(1);
			in.writeShort(2);
			in.writeShort(1);
			in.writeShort(9);
			in.writeShort(0);
			in.writeShort(2);
			in.writeShort(1);
			in.writeShort(3);
			in.writeShort(4);
			in.writeShort(1);
			in.writeShort(5);
			in.writeInt(CONSTRUCTOR_CODE_ATTRIBUTE_LENGTH);
			in.writeShort(0);
			in.writeShort(1);
			in.writeInt(CONSTRUCTOR_CODE.length);
			in.write(CONSTRUCTOR_CODE);
			in.writeShort(0);
			in.writeShort(0);
			in.writeShort(1);
			in.writeShort(11);
			in.writeShort(12);
			in.writeShort(1);
			in.writeShort(5);
			in.writeInt(NEWINSTANCE_CODE_ATTRIBUTE_LENGTH);
			in.writeShort(2);
			in.writeShort(1);
			in.writeInt(NEWINSTANCE_CODE.length);
			in.write(NEWINSTANCE_CODE);
			in.writeShort(0);
			in.writeShort(0);
			in.writeShort(0);
		}
		catch (IOException e)
		{
			throw new ObjenesisException(e);
		}
		finally
		{
			if (in == null) goto _L0; else goto _L0
		}
		if (in != null)
			try
			{
				in.close();
			}
			catch (IOException e)
			{
				throw new ObjenesisException(e);
			}
		break MISSING_BLOCK_LABEL_621;
		try
		{
			in.close();
		}
		catch (IOException e)
		{
			throw new ObjenesisException(e);
		}
		throw exception;
		return bIn.toByteArray();
	}

	public Object newInstance()
	{
		return instantiator.newInstance();
	}

}
