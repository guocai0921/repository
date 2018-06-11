// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AbstractStaxHandler.java

package org.springframework.util.xml;

import java.util.*;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.*;
import org.xml.sax.ext.LexicalHandler;

abstract class AbstractStaxHandler
	implements ContentHandler, LexicalHandler
{

	private final List namespaceMappings = new ArrayList();
	private boolean inCData;

	AbstractStaxHandler()
	{
	}

	public final void startDocument()
		throws SAXException
	{
		removeAllNamespaceMappings();
		newNamespaceMapping();
		try
		{
			startDocumentInternal();
		}
		catch (XMLStreamException ex)
		{
			throw new SAXException((new StringBuilder()).append("Could not handle startDocument: ").append(ex.getMessage()).toString(), ex);
		}
	}

	public final void endDocument()
		throws SAXException
	{
		removeAllNamespaceMappings();
		try
		{
			endDocumentInternal();
		}
		catch (XMLStreamException ex)
		{
			throw new SAXException((new StringBuilder()).append("Could not handle endDocument: ").append(ex.getMessage()).toString(), ex);
		}
	}

	public final void startPrefixMapping(String prefix, String uri)
	{
		currentNamespaceMapping().put(prefix, uri);
	}

	public final void endPrefixMapping(String s)
	{
	}

	public final void startElement(String uri, String localName, String qName, Attributes atts)
		throws SAXException
	{
		try
		{
			startElementInternal(toQName(uri, qName), atts, currentNamespaceMapping());
			newNamespaceMapping();
		}
		catch (XMLStreamException ex)
		{
			throw new SAXException((new StringBuilder()).append("Could not handle startElement: ").append(ex.getMessage()).toString(), ex);
		}
	}

	public final void endElement(String uri, String localName, String qName)
		throws SAXException
	{
		try
		{
			endElementInternal(toQName(uri, qName), currentNamespaceMapping());
			removeNamespaceMapping();
		}
		catch (XMLStreamException ex)
		{
			throw new SAXException((new StringBuilder()).append("Could not handle endElement: ").append(ex.getMessage()).toString(), ex);
		}
	}

	public final void characters(char ch[], int start, int length)
		throws SAXException
	{
		try
		{
			String data = new String(ch, start, length);
			if (!inCData)
				charactersInternal(data);
			else
				cDataInternal(data);
		}
		catch (XMLStreamException ex)
		{
			throw new SAXException((new StringBuilder()).append("Could not handle characters: ").append(ex.getMessage()).toString(), ex);
		}
	}

	public final void ignorableWhitespace(char ch[], int start, int length)
		throws SAXException
	{
		try
		{
			ignorableWhitespaceInternal(new String(ch, start, length));
		}
		catch (XMLStreamException ex)
		{
			throw new SAXException((new StringBuilder()).append("Could not handle ignorableWhitespace:").append(ex.getMessage()).toString(), ex);
		}
	}

	public final void processingInstruction(String target, String data)
		throws SAXException
	{
		try
		{
			processingInstructionInternal(target, data);
		}
		catch (XMLStreamException ex)
		{
			throw new SAXException((new StringBuilder()).append("Could not handle processingInstruction: ").append(ex.getMessage()).toString(), ex);
		}
	}

	public final void skippedEntity(String name)
		throws SAXException
	{
		try
		{
			skippedEntityInternal(name);
		}
		catch (XMLStreamException ex)
		{
			throw new SAXException((new StringBuilder()).append("Could not handle skippedEntity: ").append(ex.getMessage()).toString(), ex);
		}
	}

	public final void startDTD(String name, String publicId, String systemId)
		throws SAXException
	{
		try
		{
			StringBuilder builder = new StringBuilder("<!DOCTYPE ");
			builder.append(name);
			if (publicId != null)
			{
				builder.append(" PUBLIC \"");
				builder.append(publicId);
				builder.append("\" \"");
			} else
			{
				builder.append(" SYSTEM \"");
			}
			builder.append(systemId);
			builder.append("\">");
			dtdInternal(builder.toString());
		}
		catch (XMLStreamException ex)
		{
			throw new SAXException((new StringBuilder()).append("Could not handle startDTD: ").append(ex.getMessage()).toString(), ex);
		}
	}

	public final void endDTD()
		throws SAXException
	{
	}

	public final void startCDATA()
		throws SAXException
	{
		inCData = true;
	}

	public final void endCDATA()
		throws SAXException
	{
		inCData = false;
	}

	public final void comment(char ch[], int start, int length)
		throws SAXException
	{
		try
		{
			commentInternal(new String(ch, start, length));
		}
		catch (XMLStreamException ex)
		{
			throw new SAXException((new StringBuilder()).append("Could not handle comment: ").append(ex.getMessage()).toString(), ex);
		}
	}

	public void startEntity(String s)
		throws SAXException
	{
	}

	public void endEntity(String s)
		throws SAXException
	{
	}

	protected QName toQName(String namespaceUri, String qualifiedName)
	{
		int idx = qualifiedName.indexOf(':');
		if (idx == -1)
		{
			return new QName(namespaceUri, qualifiedName);
		} else
		{
			String prefix = qualifiedName.substring(0, idx);
			String localPart = qualifiedName.substring(idx + 1);
			return new QName(namespaceUri, localPart, prefix);
		}
	}

	protected boolean isNamespaceDeclaration(QName qName)
	{
		String prefix = qName.getPrefix();
		String localPart = qName.getLocalPart();
		return "xmlns".equals(localPart) && prefix.isEmpty() || "xmlns".equals(prefix) && !localPart.isEmpty();
	}

	private Map currentNamespaceMapping()
	{
		return (Map)namespaceMappings.get(namespaceMappings.size() - 1);
	}

	private void newNamespaceMapping()
	{
		namespaceMappings.add(new HashMap());
	}

	private void removeNamespaceMapping()
	{
		namespaceMappings.remove(namespaceMappings.size() - 1);
	}

	private void removeAllNamespaceMappings()
	{
		namespaceMappings.clear();
	}

	protected abstract void startDocumentInternal()
		throws XMLStreamException;

	protected abstract void endDocumentInternal()
		throws XMLStreamException;

	protected abstract void startElementInternal(QName qname, Attributes attributes, Map map)
		throws XMLStreamException;

	protected abstract void endElementInternal(QName qname, Map map)
		throws XMLStreamException;

	protected abstract void charactersInternal(String s)
		throws XMLStreamException;

	protected abstract void cDataInternal(String s)
		throws XMLStreamException;

	protected abstract void ignorableWhitespaceInternal(String s)
		throws XMLStreamException;

	protected abstract void processingInstructionInternal(String s, String s1)
		throws XMLStreamException;

	protected abstract void skippedEntityInternal(String s)
		throws XMLStreamException;

	protected abstract void dtdInternal(String s)
		throws XMLStreamException;

	protected abstract void commentInternal(String s)
		throws XMLStreamException;
}
