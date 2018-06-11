// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   StaxStreamHandler.java

package org.springframework.util.xml;

import java.util.*;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.springframework.util.Assert;
import org.xml.sax.*;

// Referenced classes of package org.springframework.util.xml:
//			AbstractStaxHandler

class StaxStreamHandler extends AbstractStaxHandler
{

	private final XMLStreamWriter streamWriter;

	public StaxStreamHandler(XMLStreamWriter streamWriter)
	{
		Assert.notNull(streamWriter, "XMLStreamWriter must not be null");
		this.streamWriter = streamWriter;
	}

	protected void startDocumentInternal()
		throws XMLStreamException
	{
		streamWriter.writeStartDocument();
	}

	protected void endDocumentInternal()
		throws XMLStreamException
	{
		streamWriter.writeEndDocument();
	}

	protected void startElementInternal(QName name, Attributes attributes, Map namespaceMapping)
		throws XMLStreamException
	{
		streamWriter.writeStartElement(name.getPrefix(), name.getLocalPart(), name.getNamespaceURI());
		for (Iterator iterator = namespaceMapping.entrySet().iterator(); iterator.hasNext();)
		{
			java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
			String prefix = (String)entry.getKey();
			String namespaceUri = (String)entry.getValue();
			streamWriter.writeNamespace(prefix, namespaceUri);
			if ("".equals(prefix))
				streamWriter.setDefaultNamespace(namespaceUri);
			else
				streamWriter.setPrefix(prefix, namespaceUri);
		}

		for (int i = 0; i < attributes.getLength(); i++)
		{
			QName attrName = toQName(attributes.getURI(i), attributes.getQName(i));
			if (!isNamespaceDeclaration(attrName))
				streamWriter.writeAttribute(attrName.getPrefix(), attrName.getNamespaceURI(), attrName.getLocalPart(), attributes.getValue(i));
		}

	}

	protected void endElementInternal(QName name, Map namespaceMapping)
		throws XMLStreamException
	{
		streamWriter.writeEndElement();
	}

	protected void charactersInternal(String data)
		throws XMLStreamException
	{
		streamWriter.writeCharacters(data);
	}

	protected void cDataInternal(String data)
		throws XMLStreamException
	{
		streamWriter.writeCData(data);
	}

	protected void ignorableWhitespaceInternal(String data)
		throws XMLStreamException
	{
		streamWriter.writeCharacters(data);
	}

	protected void processingInstructionInternal(String target, String data)
		throws XMLStreamException
	{
		streamWriter.writeProcessingInstruction(target, data);
	}

	protected void dtdInternal(String dtd)
		throws XMLStreamException
	{
		streamWriter.writeDTD(dtd);
	}

	protected void commentInternal(String comment)
		throws XMLStreamException
	{
		streamWriter.writeComment(comment);
	}

	public void setDocumentLocator(Locator locator1)
	{
	}

	public void startEntity(String s)
		throws SAXException
	{
	}

	public void endEntity(String s)
		throws SAXException
	{
	}

	protected void skippedEntityInternal(String s)
		throws XMLStreamException
	{
	}
}
