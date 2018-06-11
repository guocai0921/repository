// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AbstractXMLStreamReader.java

package org.springframework.util.xml;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.*;
import org.springframework.util.Assert;

abstract class AbstractXMLStreamReader
	implements XMLStreamReader
{

	AbstractXMLStreamReader()
	{
	}

	public String getElementText()
		throws XMLStreamException
	{
		if (getEventType() != 1)
			throw new XMLStreamException("parser must be on START_ELEMENT to read next text", getLocation());
		int eventType = next();
		StringBuilder builder = new StringBuilder();
		for (; eventType != 2; eventType = next())
		{
			if (eventType == 4 || eventType == 12 || eventType == 6 || eventType == 9)
			{
				builder.append(getText());
				continue;
			}
			if (eventType == 3 || eventType == 5)
				continue;
			if (eventType == 8)
				throw new XMLStreamException("unexpected end of document when reading element text content", getLocation());
			if (eventType == 1)
				throw new XMLStreamException("element text content may not contain START_ELEMENT", getLocation());
			else
				throw new XMLStreamException((new StringBuilder()).append("Unexpected event type ").append(eventType).toString(), getLocation());
		}

		return builder.toString();
	}

	public String getAttributeLocalName(int index)
	{
		return getAttributeName(index).getLocalPart();
	}

	public String getAttributeNamespace(int index)
	{
		return getAttributeName(index).getNamespaceURI();
	}

	public String getAttributePrefix(int index)
	{
		return getAttributeName(index).getPrefix();
	}

	public String getNamespaceURI()
	{
		int eventType = getEventType();
		if (eventType == 1 || eventType == 2)
			return getName().getNamespaceURI();
		else
			throw new IllegalStateException("parser must be on START_ELEMENT or END_ELEMENT state");
	}

	public String getNamespaceURI(String prefix)
	{
		Assert.notNull(prefix, "No prefix given");
		return getNamespaceContext().getNamespaceURI(prefix);
	}

	public boolean hasText()
	{
		int eventType = getEventType();
		return eventType == 6 || eventType == 4 || eventType == 5 || eventType == 12 || eventType == 9;
	}

	public String getPrefix()
	{
		int eventType = getEventType();
		if (eventType == 1 || eventType == 2)
			return getName().getPrefix();
		else
			throw new IllegalStateException("parser must be on START_ELEMENT or END_ELEMENT state");
	}

	public boolean hasName()
	{
		int eventType = getEventType();
		return eventType == 1 || eventType == 2;
	}

	public boolean isWhiteSpace()
	{
		return getEventType() == 6;
	}

	public boolean isStartElement()
	{
		return getEventType() == 1;
	}

	public boolean isEndElement()
	{
		return getEventType() == 2;
	}

	public boolean isCharacters()
	{
		return getEventType() == 4;
	}

	public int nextTag()
		throws XMLStreamException
	{
		int eventType;
		for (eventType = next(); eventType == 4 && isWhiteSpace() || eventType == 12 && isWhiteSpace() || eventType == 6 || eventType == 3 || eventType == 5; eventType = next());
		if (eventType != 1 && eventType != 2)
			throw new XMLStreamException("expected start or end tag", getLocation());
		else
			return eventType;
	}

	public void require(int expectedType, String namespaceURI, String localName)
		throws XMLStreamException
	{
		int eventType = getEventType();
		if (eventType != expectedType)
			throw new XMLStreamException((new StringBuilder()).append("Expected [").append(expectedType).append("] but read [").append(eventType).append("]").toString());
		else
			return;
	}

	public String getAttributeValue(String namespaceURI, String localName)
	{
		for (int i = 0; i < getAttributeCount(); i++)
		{
			QName name = getAttributeName(i);
			if (name.getLocalPart().equals(localName) && (namespaceURI == null || name.getNamespaceURI().equals(namespaceURI)))
				return getAttributeValue(i);
		}

		return null;
	}

	public boolean hasNext()
		throws XMLStreamException
	{
		return getEventType() != 8;
	}

	public String getLocalName()
	{
		return getName().getLocalPart();
	}

	public char[] getTextCharacters()
	{
		return getText().toCharArray();
	}

	public int getTextCharacters(int sourceStart, char target[], int targetStart, int length)
		throws XMLStreamException
	{
		char source[] = getTextCharacters();
		length = Math.min(length, source.length);
		System.arraycopy(source, sourceStart, target, targetStart, length);
		return length;
	}

	public int getTextLength()
	{
		return getText().length();
	}
}
