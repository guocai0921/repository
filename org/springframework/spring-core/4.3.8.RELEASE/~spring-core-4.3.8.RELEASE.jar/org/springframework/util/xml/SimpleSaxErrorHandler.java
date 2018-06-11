// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SimpleSaxErrorHandler.java

package org.springframework.util.xml;

import org.apache.commons.logging.Log;
import org.xml.sax.*;

public class SimpleSaxErrorHandler
	implements ErrorHandler
{

	private final Log logger;

	public SimpleSaxErrorHandler(Log logger)
	{
		this.logger = logger;
	}

	public void warning(SAXParseException ex)
		throws SAXException
	{
		logger.warn("Ignored XML validation warning", ex);
	}

	public void error(SAXParseException ex)
		throws SAXException
	{
		throw ex;
	}

	public void fatalError(SAXParseException ex)
		throws SAXException
	{
		throw ex;
	}
}
