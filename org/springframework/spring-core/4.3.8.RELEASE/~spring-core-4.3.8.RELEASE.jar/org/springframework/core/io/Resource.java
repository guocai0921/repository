// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Resource.java

package org.springframework.core.io;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

// Referenced classes of package org.springframework.core.io:
//			InputStreamSource

public interface Resource
	extends InputStreamSource
{

	public abstract boolean exists();

	public abstract boolean isReadable();

	public abstract boolean isOpen();

	public abstract URL getURL()
		throws IOException;

	public abstract URI getURI()
		throws IOException;

	public abstract File getFile()
		throws IOException;

	public abstract long contentLength()
		throws IOException;

	public abstract long lastModified()
		throws IOException;

	public abstract Resource createRelative(String s)
		throws IOException;

	public abstract String getFilename();

	public abstract String getDescription();
}
