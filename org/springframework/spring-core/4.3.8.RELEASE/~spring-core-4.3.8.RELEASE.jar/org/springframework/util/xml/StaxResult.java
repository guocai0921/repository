// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   StaxResult.java

package org.springframework.util.xml;

import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.sax.SAXResult;
import org.xml.sax.ContentHandler;
import org.xml.sax.ext.LexicalHandler;

// Referenced classes of package org.springframework.util.xml:
//			StaxStreamHandler, StaxEventHandler

class StaxResult extends SAXResult
{

	private XMLEventWriter eventWriter;
	private XMLStreamWriter streamWriter;

	public StaxResult(XMLStreamWriter streamWriter)
	{
		StaxStreamHandler handler = new StaxStreamHandler(streamWriter);
		super.setHandler(handler);
		super.setLexicalHandler(handler);
		this.streamWriter = streamWriter;
	}

	public StaxResult(XMLEventWriter eventWriter)
	{
		StaxEventHandler handler = new StaxEventHandler(eventWriter);
		super.setHandler(handler);
		super.setLexicalHandler(handler);
		this.eventWriter = eventWriter;
	}

	public XMLEventWriter getXMLEventWriter()
	{
		return eventWriter;
	}

	public XMLStreamWriter getXMLStreamWriter()
	{
		return streamWriter;
	}

	public void setHandler(ContentHandler handler)
	{
		throw new UnsupportedOperationException("setHandler is not supported");
	}

	public void setLexicalHandler(LexicalHandler handler)
	{
		throw new UnsupportedOperationException("setLexicalHandler is not supported");
	}
}
