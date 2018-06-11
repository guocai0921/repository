// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AbstractClassLoader.java

package org.springframework.cglib.transform;

import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import org.springframework.asm.Attribute;
import org.springframework.asm.ClassReader;
import org.springframework.cglib.core.*;

// Referenced classes of package org.springframework.cglib.transform:
//			ClassFilter, ClassReaderGenerator

public abstract class AbstractClassLoader extends ClassLoader
{

	private ClassFilter filter;
	private ClassLoader classPath;
	private static ProtectionDomain DOMAIN = (ProtectionDomain)AccessController.doPrivileged(new PrivilegedAction() {

		public Object run()
		{
			return org/springframework/cglib/transform/AbstractClassLoader.getProtectionDomain();
		}

	});

	protected AbstractClassLoader(ClassLoader parent, ClassLoader classPath, ClassFilter filter)
	{
		super(parent);
		this.filter = filter;
		this.classPath = classPath;
	}

	public Class loadClass(String name)
		throws ClassNotFoundException
	{
		Class loaded = findLoadedClass(name);
		if (loaded != null && loaded.getClassLoader() == this)
			return loaded;
		if (!filter.accept(name))
			return super.loadClass(name);
		InputStream is;
		is = classPath.getResourceAsStream((new StringBuilder()).append(name.replace('.', '/')).append(".class").toString());
		if (is == null)
			throw new ClassNotFoundException(name);
		ClassReader r = new ClassReader(is);
		is.close();
		break MISSING_BLOCK_LABEL_150;
		Exception exception;
		exception;
		is.close();
		throw exception;
		IOException e;
		e;
		throw new ClassNotFoundException((new StringBuilder()).append(name).append(":").append(e.getMessage()).toString());
		Class c;
		DebuggingClassWriter w = new DebuggingClassWriter(2);
		getGenerator(r).generateClass(w);
		byte b[] = w.toByteArray();
		c = super.defineClass(name, b, 0, b.length, DOMAIN);
		postProcess(c);
		return c;
		w;
		throw w;
		w;
		throw w;
		w;
		throw new CodeGenerationException(w);
	}

	protected ClassGenerator getGenerator(ClassReader r)
	{
		return new ClassReaderGenerator(r, attributes(), getFlags());
	}

	protected int getFlags()
	{
		return 0;
	}

	protected Attribute[] attributes()
	{
		return null;
	}

	protected void postProcess(Class class1)
	{
	}

}
