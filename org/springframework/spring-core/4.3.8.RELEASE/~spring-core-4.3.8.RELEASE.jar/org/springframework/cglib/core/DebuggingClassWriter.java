// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DebuggingClassWriter.java

package org.springframework.cglib.core;

import java.io.*;
import java.lang.reflect.Constructor;
import java.security.AccessController;
import java.security.PrivilegedAction;
import org.springframework.asm.*;

// Referenced classes of package org.springframework.cglib.core:
//			CodeGenerationException

public class DebuggingClassWriter extends ClassVisitor
{

	public static final String DEBUG_LOCATION_PROPERTY = "cglib.debugLocation";
	private static String debugLocation;
	private static Constructor traceCtor;
	private String className;
	private String superName;

	public DebuggingClassWriter(int flags)
	{
		super(0x50000, new ClassWriter(flags));
	}

	public void visit(int version, int access, String name, String signature, String superName, String interfaces[])
	{
		className = name.replace('/', '.');
		this.superName = superName.replace('/', '.');
		super.visit(version, access, name, signature, superName, interfaces);
	}

	public String getClassName()
	{
		return className;
	}

	public String getSuperName()
	{
		return superName;
	}

	public byte[] toByteArray()
	{
		return (byte[])(byte[])AccessController.doPrivileged(new PrivilegedAction() {

			final DebuggingClassWriter this$0;

			public Object run()
			{
				byte b[];
				String dirs;
				b = ((ClassWriter)
// JavaClassFileOutputException: get_constant: invalid tag

			
			{
				this.this$0 = DebuggingClassWriter.this;
				super();
			}
		});
	}

	static 
	{
		debugLocation = System.getProperty("cglib.debugLocation");
		if (debugLocation != null)
		{
			System.err.println((new StringBuilder()).append("CGLIB debugging enabled, writing to '").append(debugLocation).append("'").toString());
			try
			{
				Class clazz = Class.forName("org.springframework.asm.util.TraceClassVisitor");
				traceCtor = clazz.getConstructor(new Class[] {
					org/springframework/asm/ClassVisitor, java/io/PrintWriter
				});
			}
			catch (Throwable throwable) { }
		}
	}




}
