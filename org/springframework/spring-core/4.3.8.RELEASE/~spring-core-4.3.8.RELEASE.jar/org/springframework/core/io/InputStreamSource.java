// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   InputStreamSource.java

package org.springframework.core.io;

import java.io.IOException;
import java.io.InputStream;

public interface InputStreamSource
{

	public abstract InputStream getInputStream()
		throws IOException;
}
