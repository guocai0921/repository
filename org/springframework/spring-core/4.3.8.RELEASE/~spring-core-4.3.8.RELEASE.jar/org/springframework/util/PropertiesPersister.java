// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   PropertiesPersister.java

package org.springframework.util;

import java.io.*;
import java.util.Properties;

public interface PropertiesPersister
{

	public abstract void load(Properties properties, InputStream inputstream)
		throws IOException;

	public abstract void load(Properties properties, Reader reader)
		throws IOException;

	public abstract void store(Properties properties, OutputStream outputstream, String s)
		throws IOException;

	public abstract void store(Properties properties, Writer writer, String s)
		throws IOException;

	public abstract void loadFromXml(Properties properties, InputStream inputstream)
		throws IOException;

	public abstract void storeToXml(Properties properties, OutputStream outputstream, String s)
		throws IOException;

	public abstract void storeToXml(Properties properties, OutputStream outputstream, String s, String s1)
		throws IOException;
}
