// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ProxyingInstantiator.java

package org.springframework.objenesis.instantiator.basic;

import java.io.*;
import org.springframework.objenesis.ObjenesisException;
import org.springframework.objenesis.instantiator.ObjectInstantiator;

// Referenced classes of package org.springframework.objenesis.instantiator.basic:
//			ClassDefinitionUtils

public class ProxyingInstantiator
	implements ObjectInstantiator
{

	private static final int INDEX_CLASS_THIS = 1;
	private static final int INDEX_CLASS_SUPERCLASS = 2;
	private static final int INDEX_UTF8_CONSTRUCTOR_NAME = 3;
	private static final int INDEX_UTF8_CONSTRUCTOR_DESC = 4;
	private static final int INDEX_UTF8_CODE_ATTRIBUTE = 5;
	private static final int INDEX_UTF8_CLASS = 7;
	private static final int INDEX_UTF8_SUPERCLASS = 8;
	private static int CONSTANT_POOL_COUNT = 9;
	private static final byte CODE[] = {
		42, -79
	};
	private static final int CODE_ATTRIBUTE_LENGTH = 12 + CODE.length;
	private static final String SUFFIX = "$$$Objenesis";
	private static final String CONSTRUCTOR_NAME = "<init>";
	private static final String CONSTRUCTOR_DESC = "()V";
	private final Class newType;

	public ProxyingInstantiator(Class type)
	{
		byte classBytes[] = writeExtendingClass(type, "$$$Objenesis");
		try
		{
			newType = ClassDefinitionUtils.defineClass((new StringBuilder()).append(type.getName()).append("$$$Objenesis").toString(), classBytes, type.getClassLoader());
		}
		catch (Exception e)
		{
			throw new ObjenesisException(e);
		}
	}

	public Object newInstance()
	{
		return newType.newInstance();
		InstantiationException e;
		e;
		throw new ObjenesisException(e);
		e;
		throw new ObjenesisException(e);
	}

	private static byte[] writeExtendingClass(Class type, String suffix)
	{
		DataOutputStream in;
		ByteArrayOutputStream bIn;
		Exception exception;
		String parentClazz = ClassDefinitionUtils.classNameToInternalClassName(type.getName());
		String clazz = (new StringBuilder()).append(parentClazz).append(suffix).toString();
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
			in.writeUTF(parentClazz);
			in.writeShort(33);
			in.writeShort(1);
			in.writeShort(2);
			in.writeShort(0);
			in.writeShort(0);
			in.writeShort(1);
			in.writeShort(1);
			in.writeShort(3);
			in.writeShort(4);
			in.writeShort(1);
			in.writeShort(5);
			in.writeInt(CODE_ATTRIBUTE_LENGTH);
			in.writeShort(1);
			in.writeShort(1);
			in.writeInt(CODE.length);
			in.write(CODE);
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
		break MISSING_BLOCK_LABEL_392;
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

}
