// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CommonsLogWriter.java

package org.springframework.util;

import java.io.Writer;
import org.apache.commons.logging.Log;

// Referenced classes of package org.springframework.util:
//			Assert

public class CommonsLogWriter extends Writer
{

	private final Log logger;
	private final StringBuilder buffer = new StringBuilder();

	public CommonsLogWriter(Log logger)
	{
		Assert.notNull(logger, "Logger must not be null");
		this.logger = logger;
	}

	public void write(char ch)
	{
		if (ch == '\n' && buffer.length() > 0)
		{
			logger.debug(buffer.toString());
			buffer.setLength(0);
		} else
		{
			buffer.append(ch);
		}
	}

	public void write(char buffer[], int offset, int length)
	{
		for (int i = 0; i < length; i++)
		{
			char ch = buffer[offset + i];
			if (ch == '\n' && this.buffer.length() > 0)
			{
				logger.debug(this.buffer.toString());
				this.buffer.setLength(0);
			} else
			{
				this.buffer.append(ch);
			}
		}

	}

	public void flush()
	{
	}

	public void close()
	{
	}
}
