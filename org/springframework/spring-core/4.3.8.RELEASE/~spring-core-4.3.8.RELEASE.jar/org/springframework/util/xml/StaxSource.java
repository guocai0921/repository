// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   StaxSource.java

package org.springframework.util.xml;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.sax.SAXSource;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

// Referenced classes of package org.springframework.util.xml:
//			StaxStreamXMLReader, StaxEventXMLReader

class StaxSource extends SAXSource
{

	private XMLEventReader eventReader;
	private XMLStreamReader streamReader;

	StaxSource(XMLStreamReader streamReader)
	{
		super(new StaxStreamXMLReader(streamReader), new InputSource());
		this.streamReader = streamReader;
	}

	StaxSource(XMLEventReader eventReader)
	{
		super(new StaxEventXMLReader(eventReader), new InputSource());
		this.eventReader = eventReader;
	}

	XMLEventReader getXMLEventReader()
	{
		return eventReader;
	}

	XMLStreamReader getXMLStreamReader()
	{
		return streamReader;
	}

	public void setInputSource(InputSource inputSource)
	{
		throw new UnsupportedOperationException("setInputSource is not supported");
	}

	public void setXMLReader(XMLReader reader)
	{
		throw new UnsupportedOperationException("setXMLReader is not supported");
	}
}
