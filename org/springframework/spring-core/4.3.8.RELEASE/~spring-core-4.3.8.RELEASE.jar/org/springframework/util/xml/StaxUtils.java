// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   StaxUtils.java

package org.springframework.util.xml;

import javax.xml.stream.*;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.stax.StAXResult;
import javax.xml.transform.stax.StAXSource;
import org.xml.sax.ContentHandler;
import org.xml.sax.XMLReader;

// Referenced classes of package org.springframework.util.xml:
//			StaxSource, StaxResult, StaxStreamHandler, StaxEventHandler, 
//			StaxStreamXMLReader, StaxEventXMLReader, XMLEventStreamReader, XMLEventStreamWriter

public abstract class StaxUtils
{

	public StaxUtils()
	{
	}

	public static Source createStaxSource(XMLStreamReader streamReader)
	{
		return new StAXSource(streamReader);
	}

	public static Source createStaxSource(XMLEventReader eventReader)
		throws XMLStreamException
	{
		return new StAXSource(eventReader);
	}

	public static Source createCustomStaxSource(XMLStreamReader streamReader)
	{
		return new StaxSource(streamReader);
	}

	public static Source createCustomStaxSource(XMLEventReader eventReader)
	{
		return new StaxSource(eventReader);
	}

	public static boolean isStaxSource(Source source)
	{
		return (source instanceof StAXSource) || (source instanceof StaxSource);
	}

	public static XMLStreamReader getXMLStreamReader(Source source)
	{
		if (source instanceof StAXSource)
			return ((StAXSource)source).getXMLStreamReader();
		if (source instanceof StaxSource)
			return ((StaxSource)source).getXMLStreamReader();
		else
			throw new IllegalArgumentException((new StringBuilder()).append("Source '").append(source).append("' is neither StaxSource nor StAXSource").toString());
	}

	public static XMLEventReader getXMLEventReader(Source source)
	{
		if (source instanceof StAXSource)
			return ((StAXSource)source).getXMLEventReader();
		if (source instanceof StaxSource)
			return ((StaxSource)source).getXMLEventReader();
		else
			throw new IllegalArgumentException((new StringBuilder()).append("Source '").append(source).append("' is neither StaxSource nor StAXSource").toString());
	}

	public static Result createStaxResult(XMLStreamWriter streamWriter)
	{
		return new StAXResult(streamWriter);
	}

	public static Result createStaxResult(XMLEventWriter eventWriter)
	{
		return new StAXResult(eventWriter);
	}

	public static Result createCustomStaxResult(XMLStreamWriter streamWriter)
	{
		return new StaxResult(streamWriter);
	}

	public static Result createCustomStaxResult(XMLEventWriter eventWriter)
	{
		return new StaxResult(eventWriter);
	}

	public static boolean isStaxResult(Result result)
	{
		return (result instanceof StAXResult) || (result instanceof StaxResult);
	}

	public static XMLStreamWriter getXMLStreamWriter(Result result)
	{
		if (result instanceof StAXResult)
			return ((StAXResult)result).getXMLStreamWriter();
		if (result instanceof StaxResult)
			return ((StaxResult)result).getXMLStreamWriter();
		else
			throw new IllegalArgumentException((new StringBuilder()).append("Result '").append(result).append("' is neither StaxResult nor StAXResult").toString());
	}

	public static XMLEventWriter getXMLEventWriter(Result result)
	{
		if (result instanceof StAXResult)
			return ((StAXResult)result).getXMLEventWriter();
		if (result instanceof StaxResult)
			return ((StaxResult)result).getXMLEventWriter();
		else
			throw new IllegalArgumentException((new StringBuilder()).append("Result '").append(result).append("' is neither StaxResult nor StAXResult").toString());
	}

	public static ContentHandler createContentHandler(XMLStreamWriter streamWriter)
	{
		return new StaxStreamHandler(streamWriter);
	}

	public static ContentHandler createContentHandler(XMLEventWriter eventWriter)
	{
		return new StaxEventHandler(eventWriter);
	}

	public static XMLReader createXMLReader(XMLStreamReader streamReader)
	{
		return new StaxStreamXMLReader(streamReader);
	}

	public static XMLReader createXMLReader(XMLEventReader eventReader)
	{
		return new StaxEventXMLReader(eventReader);
	}

	public static XMLStreamReader createEventStreamReader(XMLEventReader eventReader)
		throws XMLStreamException
	{
		return new XMLEventStreamReader(eventReader);
	}

	public static XMLStreamWriter createEventStreamWriter(XMLEventWriter eventWriter)
	{
		return new XMLEventStreamWriter(eventWriter, XMLEventFactory.newFactory());
	}

	public static XMLStreamWriter createEventStreamWriter(XMLEventWriter eventWriter, XMLEventFactory eventFactory)
	{
		return new XMLEventStreamWriter(eventWriter, eventFactory);
	}
}
