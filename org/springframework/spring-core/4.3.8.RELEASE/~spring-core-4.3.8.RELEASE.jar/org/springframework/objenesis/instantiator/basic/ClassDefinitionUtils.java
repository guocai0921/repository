// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ClassDefinitionUtils.java

package org.springframework.objenesis.instantiator.basic;

import java.io.*;
import java.lang.reflect.Method;
import java.security.*;
import org.springframework.objenesis.ObjenesisException;

public final class ClassDefinitionUtils
{

	public static final byte OPS_aload_0 = 42;
	public static final byte OPS_invokespecial = -73;
	public static final byte OPS_return = -79;
	public static final byte OPS_new = -69;
	public static final byte OPS_dup = 89;
	public static final byte OPS_areturn = -80;
	public static final int CONSTANT_Utf8 = 1;
	public static final int CONSTANT_Integer = 3;
	public static final int CONSTANT_Float = 4;
	public static final int CONSTANT_Long = 5;
	public static final int CONSTANT_Double = 6;
	public static final int CONSTANT_Class = 7;
	public static final int CONSTANT_String = 8;
	public static final int CONSTANT_Fieldref = 9;
	public static final int CONSTANT_Methodref = 10;
	public static final int CONSTANT_InterfaceMethodref = 11;
	public static final int CONSTANT_NameAndType = 12;
	public static final int CONSTANT_MethodHandle = 15;
	public static final int CONSTANT_MethodType = 16;
	public static final int CONSTANT_InvokeDynamic = 18;
	public static final int ACC_PUBLIC = 1;
	public static final int ACC_FINAL = 16;
	public static final int ACC_SUPER = 32;
	public static final int ACC_INTERFACE = 512;
	public static final int ACC_ABSTRACT = 1024;
	public static final int ACC_SYNTHETIC = 4096;
	public static final int ACC_ANNOTATION = 8192;
	public static final int ACC_ENUM = 16384;
	public static final byte MAGIC[] = {
		-54, -2, -70, -66
	};
	public static final byte VERSION[] = {
		0, 0, 0, 49
	};
	private static Method DEFINE_CLASS;
	private static final ProtectionDomain PROTECTION_DOMAIN = (ProtectionDomain)AccessController.doPrivileged(new PrivilegedAction() {

		public ProtectionDomain run()
		{
			return org/springframework/objenesis/instantiator/basic/ClassDefinitionUtils.getProtectionDomain();
		}

		public volatile Object run()
		{
			return run();
		}

	});

	private ClassDefinitionUtils()
	{
	}

	public static Class defineClass(String className, byte b[], ClassLoader loader)
		throws Exception
	{
		Object args[] = {
			className, b, new Integer(0), new Integer(b.length), PROTECTION_DOMAIN
		};
		Class c = (Class)DEFINE_CLASS.invoke(loader, args);
		Class.forName(className, true, loader);
		return c;
	}

	public static byte[] readClass(String className)
		throws IOException
	{
		byte b[];
		InputStream in;
		className = classNameToResource(className);
		b = new byte[2500];
		in = org/springframework/objenesis/instantiator/basic/ClassDefinitionUtils.getClassLoader().getResourceAsStream(className);
		int length = in.read(b);
		in.close();
		break MISSING_BLOCK_LABEL_43;
		Exception exception;
		exception;
		in.close();
		throw exception;
		if (length >= 2500)
		{
			throw new IllegalArgumentException("The class is longer that 2500 bytes which is currently unsupported");
		} else
		{
			byte copy[] = new byte[length];
			System.arraycopy(b, 0, copy, 0, length);
			return copy;
		}
	}

	public static void writeClass(String fileName, byte bytes[])
		throws IOException
	{
		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(fileName));
		out.write(bytes);
		out.close();
		break MISSING_BLOCK_LABEL_35;
		Exception exception;
		exception;
		out.close();
		throw exception;
	}

	public static String classNameToInternalClassName(String className)
	{
		return className.replace('.', '/');
	}

	public static String classNameToResource(String className)
	{
		return (new StringBuilder()).append(classNameToInternalClassName(className)).append(".class").toString();
	}

	public static Class getExistingClass(ClassLoader classLoader, String className)
	{
		return Class.forName(className, true, classLoader);
		ClassNotFoundException e;
		e;
		return null;
	}

	static 
	{
		AccessController.doPrivileged(new PrivilegedAction() {

			public Object run()
			{
				try
				{
					Class loader = Class.forName("java.lang.ClassLoader");
					ClassDefinitionUtils.DEFINE_CLASS = loader.getDeclaredMethod("defineClass", new Class[] {
						java/lang/String, [B, Integer.TYPE, Integer.TYPE, java/security/ProtectionDomain
					});
					ClassDefinitionUtils.DEFINE_CLASS.setAccessible(true);
				}
				catch (ClassNotFoundException e)
				{
					throw new ObjenesisException(e);
				}
				catch (NoSuchMethodException e)
				{
					throw new ObjenesisException(e);
				}
				return null;
			}

		});
	}


}
