// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   StaxStreamXMLReader.java

package org.springframework.util.xml;

import javax.xml.namespace.QName;
import javax.xml.stream.*;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.xml.sax.*;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.ext.Locator2;
import org.xml.sax.helpers.AttributesImpl;

// Referenced classes of package org.springframework.util.xml:
//			AbstractStaxXMLReader

class StaxStreamXMLReader extends AbstractStaxXMLReader
{

	private static final String DEFAULT_XML_VERSION = "1.0";
	private final XMLStreamReader reader;
	private String xmlVersion;
	private String encoding;

	StaxStreamXMLReader(XMLStreamReader reader)
	{
		xmlVersion = "1.0";
		Assert.notNull(reader, "'reader' must not be null");
		int event = reader.getEventType();
		if (event != 7 && event != 1)
		{
			throw new IllegalStateException("XMLEventReader not at start of document or element");
		} else
		{
			this.reader = reader;
			return;
		}
	}

	protected void parseInternal()
		throws SAXException, XMLStreamException
	{
		boolean documentStarted = false;
		boolean documentEnded = false;
		int elementDepth = 0;
		int eventType = reader.getEventType();
		do
		{
			if (eventType != 7 && eventType != 8 && !documentStarted)
			{
				handleStartDocument();
				documentStarted = true;
			}
			switch (eventType)
			{
			case 1: // '\001'
				elementDepth++;
				handleStartElement();
				break;

			case 2: // '\002'
				if (--elementDepth >= 0)
					handleEndElement();
				break;

			case 3: // '\003'
				handleProcessingInstruction();
				break;

			case 4: // '\004'
			case 6: // '\006'
			case 12: // '\f'
				handleCharacters();
				break;

			case 7: // '\007'
				handleStartDocument();
				documentStarted = true;
				break;

			case 8: // '\b'
				handleEndDocument();
				documentEnded = true;
				break;

			case 5: // '\005'
				handleComment();
				break;

			case 11: // '\013'
				handleDtd();
				break;

			case 9: // '\t'
				handleEntityReference();
				break;
			}
			if (!reader.hasNext() || elementDepth < 0)
				break;
			eventType = reader.next();
		} while (true);
		if (!documentEnded)
			handleEndDocument();
	}

	private void handleStartDocument()
		throws SAXException
	{
		if (7 == reader.getEventType())
		{
			String xmlVersion = reader.getVersion();
			if (StringUtils.hasLength(xmlVersion))
				this.xmlVersion = xmlVersion;
			encoding = reader.getCharacterEncodingScheme();
		}
		if (getContentHandler() != null)
		{
			final Location location = reader.getLocation();
			getContentHandler().setDocumentLocator(new Locator2() {

				final Location val$location;
				final StaxStreamXMLReader this$0;

				public int getColumnNumber()
				{
					return location == null ? -1 : location.getColumnNumber();
				}

				public int getLineNumber()
				{
					return location == null ? -1 : location.getLineNumber();
				}

				public String getPublicId()
				{
					return location == null ? null : location.getPublicId();
				}

				public String getSystemId()
				{
					return location == null ? null : location.getSystemId();
				}

				public String getXMLVersion()
				{
					return xmlVersion;
				}

				public String getEncoding()
				{
					return encoding;
				}

			
			{
				this.this$0 = StaxStreamXMLReader.this;
				location = location1;
				super();
			}
			});
			getContentHandler().startDocument();
			if (reader.standaloneSet())
				setStandalone(reader.isStandalone());
		}
	}

	private void handleStartElement()
		throws SAXException
	{
		if (getContentHandler() != null)
		{
			QName qName = reader.getName();
			if (hasNamespacesFeature())
			{
				for (int i = 0; i < reader.getNamespaceCount(); i++)
					startPrefixMapping(reader.getNamespacePrefix(i), reader.getNamespaceURI(i));

				for (int i = 0; i < reader.getAttributeCount(); i++)
				{
					String prefix = reader.getAttributePrefix(i);
					String namespace = reader.getAttributeNamespace(i);
					if (StringUtils.hasLength(namespace))
						startPrefixMapping(prefix, namespace);
				}

				getContentHandler().startElement(qName.getNamespaceURI(), qName.getLocalPart(), toQualifiedName(qName), getAttributes());
			} else
			{
				getContentHandler().startElement("", "", toQualifiedName(qName), getAttributes());
			}
		}
	}

	private void handleEndElement()
		throws SAXException
	{
		if (getContentHandler() != null)
		{
			QName qName = reader.getName();
			if (hasNamespacesFeature())
			{
				getContentHandler().endElement(qName.getNamespaceURI(), qName.getLocalPart(), toQualifiedName(qName));
				for (int i = 0; i < reader.getNamespaceCount(); i++)
				{
					String prefix = reader.getNamespacePrefix(i);
					if (prefix == null)
						prefix = "";
					endPrefixMapping(prefix);
				}

			} else
			{
				getContentHandler().endElement("", "", toQualifiedName(qName));
			}
		}
	}

	private void handleCharacters()
		throws SAXException
	{
		if (12 == reader.getEventType() && getLexicalHandler() != null)
			getLexicalHandler().startCDATA();
		if (getContentHandler() != null)
			getContentHandler().characters(reader.getTextCharacters(), reader.getTextStart(), reader.getTextLength());
		if (12 == reader.getEventType() && getLexicalHandler() != null)
			getLexicalHandler().endCDATA();
	}

	private void handleComment()
		throws SAXException
	{
		if (getLexicalHandler() != null)
			getLexicalHandler().comment(reader.getTextCharacters(), reader.getTextStart(), reader.getTextLength());
	}

	private void handleDtd()
		throws SAXException
	{
		if (getLexicalHandler() != null)
		{
			Location location = reader.getLocation();
			getLexicalHandler().startDTD(null, location.getPublicId(), location.getSystemId());
		}
		if (getLexicalHandler() != null)
			getLexicalHandler().endDTD();
	}

	private void handleEntityReference()
		throws SAXException
	{
		if (getLexicalHandler() != null)
			getLexicalHandler().startEntity(reader.getLocalName());
		if (getLexicalHandler() != null)
			getLexicalHandler().endEntity(reader.getLocalName());
	}

	private void handleEndDocument()
		throws SAXException
	{
		if (getContentHandler() != null)
			getContentHandler().endDocument();
	}

	private void handleProcessingInstruction()
		throws SAXException
	{
		if (getContentHandler() != null)
			getContentHandler().processingInstruction(reader.getPITarget(), reader.getPIData());
	}

	private Attributes getAttributes()
	{
		AttributesImpl attributes = new AttributesImpl();
		for (int i = 0; i < reader.getAttributeCount(); i++)
		{
			String namespace = reader.getAttributeNamespace(i);
			if (namespace == null || !hasNamespacesFeature())
				namespace = "";
			String type = reader.getAttributeType(i);
			if (type == null)
				type = "CDATA";
			attributes.addAttribute(namespace, reader.getAttributeLocalName(i), toQualifiedName(reader.getAttributeName(i)), type, reader.getAttributeValue(i));
		}

		if (hasNamespacePrefixesFeature())
		{
			for (int i = 0; i < reader.getNamespaceCount(); i++)
			{
				String prefix = reader.getNamespacePrefix(i);
				String namespaceUri = reader.getNamespaceURI(i);
				String qName;
				if (StringUtils.hasLength(prefix))
					qName = (new StringBuilder()).append("xmlns:").append(prefix).toString();
				else
					qName = "xmlns";
				attributes.addAttribute("", "", qName, "CDATA", namespaceUri);
			}

		}
		return attributes;
	}


}
