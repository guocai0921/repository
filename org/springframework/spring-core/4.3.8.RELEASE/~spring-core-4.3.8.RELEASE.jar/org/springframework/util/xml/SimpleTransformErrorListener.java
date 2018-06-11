// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SimpleTransformErrorListener.java

package org.springframework.util.xml;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;
import org.apache.commons.logging.Log;

public class SimpleTransformErrorListener
	implements ErrorListener
{

	private final Log logger;

	public SimpleTransformErrorListener(Log logger)
	{
		this.logger = logger;
	}

	public void warning(TransformerException ex)
		throws TransformerException
	{
		logger.warn("XSLT transformation warning", ex);
	}

	public void error(TransformerException ex)
		throws TransformerException
	{
		logger.error("XSLT transformation error", ex);
	}

	public void fatalError(TransformerException ex)
		throws TransformerException
	{
		throw ex;
	}
}
