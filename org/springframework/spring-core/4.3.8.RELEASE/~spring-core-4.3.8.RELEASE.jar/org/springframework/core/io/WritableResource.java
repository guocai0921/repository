// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   WritableResource.java

package org.springframework.core.io;

import java.io.IOException;
import java.io.OutputStream;

// Referenced classes of package org.springframework.core.io:
//			Resource

public interface WritableResource
	extends Resource
{

	public abstract boolean isWritable();

	public abstract OutputStream getOutputStream()
		throws IOException;
}
