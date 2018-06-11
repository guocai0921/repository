// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AbstractXMLReader.java

package org.springframework.util.xml;

import org.xml.sax.*;
import org.xml.sax.ext.LexicalHandler;

abstract class AbstractXMLReader
	implements XMLReader
{

	private DTDHandler dtdHandler;
	private ContentHandler contentHandler;
	private EntityResolver entityResolver;
	private ErrorHandler errorHandler;
	private LexicalHandler lexicalHandler;

	AbstractXMLReader()
	{
	}

	public void setContentHandler(ContentHandler contentHandler)
	{
		this.contentHandler = contentHandler;
	}

	public ContentHandler getContentHandler()
	{
		return contentHandler;
	}

	public void setDTDHandler(DTDHandler dtdHandler)
	{
		this.dtdHandler = dtdHandler;
	}

	public DTDHandler getDTDHandler()
	{
		return dtdHandler;
	}

	public void setEntityResolver(EntityResolver entityResolver)
	{
		this.entityResolver = entityResolver;
	}

	public EntityResolver getEntityResolver()
	{
		return entityResolver;
	}

	public void setErrorHandler(ErrorHandler errorHandler)
	{
		this.errorHandler = errorHandler;
	}

	public ErrorHandler getErrorHandler()
	{
		return errorHandler;
	}

	protected LexicalHandler getLexicalHandler()
	{
		return lexicalHandler;
	}

	public boolean getFeature(String name)
		throws SAXNotRecognizedException, SAXNotSupportedException
	{
		if (name.startsWith("http://xml.org/sax/features/"))
			return false;
		else
			throw new SAXNotRecognizedException(name);
	}

	public void setFeature(String name, boolean value)
		throws SAXNotRecognizedException, SAXNotSupportedException
	{
		if (name.startsWith("http://xml.org/sax/features/"))
		{
			if (value)
				throw new SAXNotSupportedException(name);
			else
				return;
		} else
		{
			throw new SAXNotRecognizedException(name);
		}
	}

	public Object getProperty(String name)
		throws SAXNotRecognizedException, SAXNotSupportedException
	{
		if ("http://xml.org/sax/properties/lexical-handler".equals(name))
			return lexicalHandler;
		else
			throw new SAXNotRecognizedException(name);
	}

	public void setProperty(String name, Object value)
		throws SAXNotRecognizedException, SAXNotSupportedException
	{
		if ("http://xml.org/sax/properties/lexical-handler".equals(name))
			lexicalHandler = (LexicalHandler)value;
		else
			throw new SAXNotRecognizedException(name);
	}
}
