// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   StaxEventHandler.java

package org.springframework.util.xml;

import java.util.*;
import javax.xml.namespace.QName;
import javax.xml.stream.*;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;

// Referenced classes of package org.springframework.util.xml:
//			AbstractStaxHandler

class StaxEventHandler extends AbstractStaxHandler
{
	private static final class LocatorLocationAdapter
		implements Location
	{

		private final Locator locator;

		public int getLineNumber()
		{
			return locator.getLineNumber();
		}

		public int getColumnNumber()
		{
			return locator.getColumnNumber();
		}

		public int getCharacterOffset()
		{
			return -1;
		}

		public String getPublicId()
		{
			return locator.getPublicId();
		}

		public String getSystemId()
		{
			return locator.getSystemId();
		}

		public LocatorLocationAdapter(Locator locator)
		{
			this.locator = locator;
		}
	}


	private final XMLEventFactory eventFactory;
	private final XMLEventWriter eventWriter;

	public StaxEventHandler(XMLEventWriter eventWriter)
	{
		eventFactory = XMLEventFactory.newInstance();
		this.eventWriter = eventWriter;
	}

	public StaxEventHandler(XMLEventWriter eventWriter, XMLEventFactory factory)
	{
		eventFactory = factory;
		this.eventWriter = eventWriter;
	}

	public void setDocumentLocator(Locator locator)
	{
		if (locator != null)
			eventFactory.setLocation(new LocatorLocationAdapter(locator));
	}

	protected void startDocumentInternal()
		throws XMLStreamException
	{
		eventWriter.add(eventFactory.createStartDocument());
	}

	protected void endDocumentInternal()
		throws XMLStreamException
	{
		eventWriter.add(eventFactory.createEndDocument());
	}

	protected void startElementInternal(QName name, Attributes atts, Map namespaceMapping)
		throws XMLStreamException
	{
		List attributes = getAttributes(atts);
		List namespaces = getNamespaces(namespaceMapping);
		eventWriter.add(eventFactory.createStartElement(name, attributes.iterator(), namespaces.iterator()));
	}

	private List getNamespaces(Map namespaceMapping)
	{
		List result = new ArrayList();
		String prefix;
		String namespaceUri;
		for (Iterator iterator = namespaceMapping.entrySet().iterator(); iterator.hasNext(); result.add(eventFactory.createNamespace(prefix, namespaceUri)))
		{
			java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
			prefix = (String)entry.getKey();
			namespaceUri = (String)entry.getValue();
		}

		return result;
	}

	private List getAttributes(Attributes attributes)
	{
		List result = new ArrayList();
		for (int i = 0; i < attributes.getLength(); i++)
		{
			QName attrName = toQName(attributes.getURI(i), attributes.getQName(i));
			if (!isNamespaceDeclaration(attrName))
				result.add(eventFactory.createAttribute(attrName, attributes.getValue(i)));
		}

		return result;
	}

	protected void endElementInternal(QName name, Map namespaceMapping)
		throws XMLStreamException
	{
		List namespaces = getNamespaces(namespaceMapping);
		eventWriter.add(eventFactory.createEndElement(name, namespaces.iterator()));
	}

	protected void charactersInternal(String data)
		throws XMLStreamException
	{
		eventWriter.add(eventFactory.createCharacters(data));
	}

	protected void cDataInternal(String data)
		throws XMLStreamException
	{
		eventWriter.add(eventFactory.createCData(data));
	}

	protected void ignorableWhitespaceInternal(String data)
		throws XMLStreamException
	{
		eventWriter.add(eventFactory.createIgnorableSpace(data));
	}

	protected void processingInstructionInternal(String target, String data)
		throws XMLStreamException
	{
		eventWriter.add(eventFactory.createProcessingInstruction(target, data));
	}

	protected void dtdInternal(String dtd)
		throws XMLStreamException
	{
		eventWriter.add(eventFactory.createDTD(dtd));
	}

	protected void commentInternal(String comment)
		throws XMLStreamException
	{
		eventWriter.add(eventFactory.createComment(comment));
	}

	protected void skippedEntityInternal(String s)
		throws XMLStreamException
	{
	}
}
