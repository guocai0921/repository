// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DomContentHandler.java

package org.springframework.util.xml;

import java.util.ArrayList;
import java.util.List;
import org.springframework.util.Assert;
import org.w3c.dom.*;
import org.xml.sax.*;

class DomContentHandler
	implements ContentHandler
{

	private final Document document;
	private final List elements = new ArrayList();
	private final Node node;

	DomContentHandler(Node node)
	{
		Assert.notNull(node, "node must not be null");
		this.node = node;
		if (node instanceof Document)
			document = (Document)node;
		else
			document = node.getOwnerDocument();
		Assert.notNull(document, "document must not be null");
	}

	private Node getParent()
	{
		if (!elements.isEmpty())
			return (Node)elements.get(elements.size() - 1);
		else
			return node;
	}

	public void startElement(String uri, String localName, String qName, Attributes attributes)
		throws SAXException
	{
		Node parent = getParent();
		Element element = document.createElementNS(uri, qName);
		for (int i = 0; i < attributes.getLength(); i++)
		{
			String attrUri = attributes.getURI(i);
			String attrQname = attributes.getQName(i);
			String value = attributes.getValue(i);
			if (!attrQname.startsWith("xmlns"))
				element.setAttributeNS(attrUri, attrQname, value);
		}

		element = (Element)parent.appendChild(element);
		elements.add(element);
	}

	public void endElement(String uri, String localName, String qName)
		throws SAXException
	{
		elements.remove(elements.size() - 1);
	}

	public void characters(char ch[], int start, int length)
		throws SAXException
	{
		String data = new String(ch, start, length);
		Node parent = getParent();
		Node lastChild = parent.getLastChild();
		if (lastChild != null && lastChild.getNodeType() == 3)
		{
			((Text)lastChild).appendData(data);
		} else
		{
			Text text = document.createTextNode(data);
			parent.appendChild(text);
		}
	}

	public void processingInstruction(String target, String data)
		throws SAXException
	{
		Node parent = getParent();
		org.w3c.dom.ProcessingInstruction pi = document.createProcessingInstruction(target, data);
		parent.appendChild(pi);
	}

	public void setDocumentLocator(Locator locator1)
	{
	}

	public void startDocument()
		throws SAXException
	{
	}

	public void endDocument()
		throws SAXException
	{
	}

	public void startPrefixMapping(String s, String s1)
		throws SAXException
	{
	}

	public void endPrefixMapping(String s)
		throws SAXException
	{
	}

	public void ignorableWhitespace(char ac[], int i, int j)
		throws SAXException
	{
	}

	public void skippedEntity(String s)
		throws SAXException
	{
	}
}
