// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   XMLEventStreamWriter.java

package org.springframework.util.xml;

import java.util.*;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.*;
import javax.xml.stream.events.*;
import org.springframework.util.Assert;

class XMLEventStreamWriter
	implements XMLStreamWriter
{

	private static final String DEFAULT_ENCODING = "UTF-8";
	private final XMLEventWriter eventWriter;
	private final XMLEventFactory eventFactory;
	private final List endElements = new ArrayList();
	private boolean emptyElement;

	public XMLEventStreamWriter(XMLEventWriter eventWriter, XMLEventFactory eventFactory)
	{
		emptyElement = false;
		Assert.notNull(eventWriter, "'eventWriter' must not be null");
		Assert.notNull(eventFactory, "'eventFactory' must not be null");
		this.eventWriter = eventWriter;
		this.eventFactory = eventFactory;
	}

	public void setNamespaceContext(NamespaceContext context)
		throws XMLStreamException
	{
		eventWriter.setNamespaceContext(context);
	}

	public NamespaceContext getNamespaceContext()
	{
		return eventWriter.getNamespaceContext();
	}

	public void setPrefix(String prefix, String uri)
		throws XMLStreamException
	{
		eventWriter.setPrefix(prefix, uri);
	}

	public String getPrefix(String uri)
		throws XMLStreamException
	{
		return eventWriter.getPrefix(uri);
	}

	public void setDefaultNamespace(String uri)
		throws XMLStreamException
	{
		eventWriter.setDefaultNamespace(uri);
	}

	public Object getProperty(String name)
		throws IllegalArgumentException
	{
		throw new IllegalArgumentException();
	}

	public void writeStartDocument()
		throws XMLStreamException
	{
		closeEmptyElementIfNecessary();
		eventWriter.add(eventFactory.createStartDocument());
	}

	public void writeStartDocument(String version)
		throws XMLStreamException
	{
		closeEmptyElementIfNecessary();
		eventWriter.add(eventFactory.createStartDocument("UTF-8", version));
	}

	public void writeStartDocument(String encoding, String version)
		throws XMLStreamException
	{
		closeEmptyElementIfNecessary();
		eventWriter.add(eventFactory.createStartDocument(encoding, version));
	}

	public void writeStartElement(String localName)
		throws XMLStreamException
	{
		closeEmptyElementIfNecessary();
		doWriteStartElement(eventFactory.createStartElement(new QName(localName), null, null));
	}

	public void writeStartElement(String namespaceURI, String localName)
		throws XMLStreamException
	{
		closeEmptyElementIfNecessary();
		doWriteStartElement(eventFactory.createStartElement(new QName(namespaceURI, localName), null, null));
	}

	public void writeStartElement(String prefix, String localName, String namespaceURI)
		throws XMLStreamException
	{
		closeEmptyElementIfNecessary();
		doWriteStartElement(eventFactory.createStartElement(new QName(namespaceURI, localName, prefix), null, null));
	}

	private void doWriteStartElement(StartElement startElement)
		throws XMLStreamException
	{
		eventWriter.add(startElement);
		endElements.add(eventFactory.createEndElement(startElement.getName(), startElement.getNamespaces()));
	}

	public void writeEmptyElement(String localName)
		throws XMLStreamException
	{
		closeEmptyElementIfNecessary();
		writeStartElement(localName);
		emptyElement = true;
	}

	public void writeEmptyElement(String namespaceURI, String localName)
		throws XMLStreamException
	{
		closeEmptyElementIfNecessary();
		writeStartElement(namespaceURI, localName);
		emptyElement = true;
	}

	public void writeEmptyElement(String prefix, String localName, String namespaceURI)
		throws XMLStreamException
	{
		closeEmptyElementIfNecessary();
		writeStartElement(prefix, localName, namespaceURI);
		emptyElement = true;
	}

	private void closeEmptyElementIfNecessary()
		throws XMLStreamException
	{
		if (emptyElement)
		{
			emptyElement = false;
			writeEndElement();
		}
	}

	public void writeEndElement()
		throws XMLStreamException
	{
		closeEmptyElementIfNecessary();
		int last = endElements.size() - 1;
		EndElement lastEndElement = (EndElement)endElements.get(last);
		eventWriter.add(lastEndElement);
		endElements.remove(last);
	}

	public void writeAttribute(String localName, String value)
		throws XMLStreamException
	{
		eventWriter.add(eventFactory.createAttribute(localName, value));
	}

	public void writeAttribute(String namespaceURI, String localName, String value)
		throws XMLStreamException
	{
		eventWriter.add(eventFactory.createAttribute(new QName(namespaceURI, localName), value));
	}

	public void writeAttribute(String prefix, String namespaceURI, String localName, String value)
		throws XMLStreamException
	{
		eventWriter.add(eventFactory.createAttribute(prefix, namespaceURI, localName, value));
	}

	public void writeNamespace(String prefix, String namespaceURI)
		throws XMLStreamException
	{
		doWriteNamespace(eventFactory.createNamespace(prefix, namespaceURI));
	}

	public void writeDefaultNamespace(String namespaceURI)
		throws XMLStreamException
	{
		doWriteNamespace(eventFactory.createNamespace(namespaceURI));
	}

	private void doWriteNamespace(Namespace namespace)
		throws XMLStreamException
	{
		int last = endElements.size() - 1;
		EndElement oldEndElement = (EndElement)endElements.get(last);
		Iterator oldNamespaces = oldEndElement.getNamespaces();
		List newNamespaces = new ArrayList();
		Namespace oldNamespace;
		for (; oldNamespaces.hasNext(); newNamespaces.add(oldNamespace))
			oldNamespace = (Namespace)oldNamespaces.next();

		newNamespaces.add(namespace);
		EndElement newEndElement = eventFactory.createEndElement(oldEndElement.getName(), newNamespaces.iterator());
		eventWriter.add(namespace);
		endElements.set(last, newEndElement);
	}

	public void writeCharacters(String text)
		throws XMLStreamException
	{
		closeEmptyElementIfNecessary();
		eventWriter.add(eventFactory.createCharacters(text));
	}

	public void writeCharacters(char text[], int start, int len)
		throws XMLStreamException
	{
		closeEmptyElementIfNecessary();
		eventWriter.add(eventFactory.createCharacters(new String(text, start, len)));
	}

	public void writeCData(String data)
		throws XMLStreamException
	{
		closeEmptyElementIfNecessary();
		eventWriter.add(eventFactory.createCData(data));
	}

	public void writeComment(String data)
		throws XMLStreamException
	{
		closeEmptyElementIfNecessary();
		eventWriter.add(eventFactory.createComment(data));
	}

	public void writeProcessingInstruction(String target)
		throws XMLStreamException
	{
		closeEmptyElementIfNecessary();
		eventWriter.add(eventFactory.createProcessingInstruction(target, ""));
	}

	public void writeProcessingInstruction(String target, String data)
		throws XMLStreamException
	{
		closeEmptyElementIfNecessary();
		eventWriter.add(eventFactory.createProcessingInstruction(target, data));
	}

	public void writeDTD(String dtd)
		throws XMLStreamException
	{
		closeEmptyElementIfNecessary();
		eventWriter.add(eventFactory.createDTD(dtd));
	}

	public void writeEntityRef(String name)
		throws XMLStreamException
	{
		closeEmptyElementIfNecessary();
		eventWriter.add(eventFactory.createEntityReference(name, null));
	}

	public void writeEndDocument()
		throws XMLStreamException
	{
		closeEmptyElementIfNecessary();
		eventWriter.add(eventFactory.createEndDocument());
	}

	public void flush()
		throws XMLStreamException
	{
		eventWriter.flush();
	}

	public void close()
		throws XMLStreamException
	{
		closeEmptyElementIfNecessary();
		eventWriter.close();
	}
}
