// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   XMLEventStreamReader.java

package org.springframework.util.xml;

import java.util.Iterator;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.*;
import javax.xml.stream.events.*;

// Referenced classes of package org.springframework.util.xml:
//			AbstractXMLStreamReader

class XMLEventStreamReader extends AbstractXMLStreamReader
{

	private XMLEvent event;
	private final XMLEventReader eventReader;

	public XMLEventStreamReader(XMLEventReader eventReader)
		throws XMLStreamException
	{
		this.eventReader = eventReader;
		event = eventReader.nextEvent();
	}

	public QName getName()
	{
		if (event.isStartElement())
			return event.asStartElement().getName();
		if (event.isEndElement())
			return event.asEndElement().getName();
		else
			throw new IllegalStateException();
	}

	public Location getLocation()
	{
		return event.getLocation();
	}

	public int getEventType()
	{
		return event.getEventType();
	}

	public String getVersion()
	{
		if (event.isStartDocument())
			return ((StartDocument)event).getVersion();
		else
			return null;
	}

	public Object getProperty(String name)
		throws IllegalArgumentException
	{
		return eventReader.getProperty(name);
	}

	public boolean isStandalone()
	{
		if (event.isStartDocument())
			return ((StartDocument)event).isStandalone();
		else
			throw new IllegalStateException();
	}

	public boolean standaloneSet()
	{
		if (event.isStartDocument())
			return ((StartDocument)event).standaloneSet();
		else
			throw new IllegalStateException();
	}

	public String getEncoding()
	{
		return null;
	}

	public String getCharacterEncodingScheme()
	{
		return null;
	}

	public String getPITarget()
	{
		if (event.isProcessingInstruction())
			return ((ProcessingInstruction)event).getTarget();
		else
			throw new IllegalStateException();
	}

	public String getPIData()
	{
		if (event.isProcessingInstruction())
			return ((ProcessingInstruction)event).getData();
		else
			throw new IllegalStateException();
	}

	public int getTextStart()
	{
		return 0;
	}

	public String getText()
	{
		if (event.isCharacters())
			return event.asCharacters().getData();
		if (event.getEventType() == 5)
			return ((Comment)event).getText();
		else
			throw new IllegalStateException();
	}

	public int getAttributeCount()
	{
		if (!event.isStartElement())
		{
			throw new IllegalStateException();
		} else
		{
			Iterator attributes = event.asStartElement().getAttributes();
			return countIterator(attributes);
		}
	}

	public boolean isAttributeSpecified(int index)
	{
		return getAttribute(index).isSpecified();
	}

	public QName getAttributeName(int index)
	{
		return getAttribute(index).getName();
	}

	public String getAttributeType(int index)
	{
		return getAttribute(index).getDTDType();
	}

	public String getAttributeValue(int index)
	{
		return getAttribute(index).getValue();
	}

	private Attribute getAttribute(int index)
	{
		if (!event.isStartElement())
			throw new IllegalStateException();
		int count = 0;
		for (Iterator attributes = event.asStartElement().getAttributes(); attributes.hasNext();)
		{
			Attribute attribute = (Attribute)attributes.next();
			if (count == index)
				return attribute;
			count++;
		}

		throw new IllegalArgumentException();
	}

	public NamespaceContext getNamespaceContext()
	{
		if (event.isStartElement())
			return event.asStartElement().getNamespaceContext();
		else
			throw new IllegalStateException();
	}

	public int getNamespaceCount()
	{
		Iterator namespaces;
		if (event.isStartElement())
			namespaces = event.asStartElement().getNamespaces();
		else
		if (event.isEndElement())
			namespaces = event.asEndElement().getNamespaces();
		else
			throw new IllegalStateException();
		return countIterator(namespaces);
	}

	public String getNamespacePrefix(int index)
	{
		return getNamespace(index).getPrefix();
	}

	public String getNamespaceURI(int index)
	{
		return getNamespace(index).getNamespaceURI();
	}

	private Namespace getNamespace(int index)
	{
		Iterator namespaces;
		if (event.isStartElement())
			namespaces = event.asStartElement().getNamespaces();
		else
		if (event.isEndElement())
			namespaces = event.asEndElement().getNamespaces();
		else
			throw new IllegalStateException();
		for (int count = 0; namespaces.hasNext(); count++)
		{
			Namespace namespace = (Namespace)namespaces.next();
			if (count == index)
				return namespace;
		}

		throw new IllegalArgumentException();
	}

	public int next()
		throws XMLStreamException
	{
		event = eventReader.nextEvent();
		return event.getEventType();
	}

	public void close()
		throws XMLStreamException
	{
		eventReader.close();
	}

	private static int countIterator(Iterator iterator)
	{
		int count;
		for (count = 0; iterator.hasNext(); count++)
			iterator.next();

		return count;
	}
}
