// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   StaxEventXMLReader.java

package org.springframework.util.xml;

import java.util.Iterator;
import javax.xml.namespace.QName;
import javax.xml.stream.*;
import javax.xml.stream.events.*;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.xml.sax.*;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.ext.Locator2;
import org.xml.sax.helpers.AttributesImpl;

// Referenced classes of package org.springframework.util.xml:
//			AbstractStaxXMLReader

class StaxEventXMLReader extends AbstractStaxXMLReader
{

	private static final String DEFAULT_XML_VERSION = "1.0";
	private final XMLEventReader reader;
	private String xmlVersion;
	private String encoding;

	StaxEventXMLReader(XMLEventReader reader)
	{
		xmlVersion = "1.0";
		Assert.notNull(reader, "'reader' must not be null");
		try
		{
			XMLEvent event = reader.peek();
			if (event != null && !event.isStartDocument() && !event.isStartElement())
				throw new IllegalStateException("XMLEventReader not at start of document or element");
		}
		catch (XMLStreamException ex)
		{
			throw new IllegalStateException((new StringBuilder()).append("Could not read first element: ").append(ex.getMessage()).toString());
		}
		this.reader = reader;
	}

	protected void parseInternal()
		throws SAXException, XMLStreamException
	{
		boolean documentStarted = false;
		boolean documentEnded = false;
		int elementDepth = 0;
		do
		{
			if (!reader.hasNext() || elementDepth < 0)
				break;
			XMLEvent event = reader.nextEvent();
			if (!event.isStartDocument() && !event.isEndDocument() && !documentStarted)
			{
				handleStartDocument(event);
				documentStarted = true;
			}
			switch (event.getEventType())
			{
			case 7: // '\007'
				handleStartDocument(event);
				documentStarted = true;
				break;

			case 1: // '\001'
				elementDepth++;
				handleStartElement(event.asStartElement());
				break;

			case 2: // '\002'
				if (--elementDepth >= 0)
					handleEndElement(event.asEndElement());
				break;

			case 3: // '\003'
				handleProcessingInstruction((ProcessingInstruction)event);
				break;

			case 4: // '\004'
			case 6: // '\006'
			case 12: // '\f'
				handleCharacters(event.asCharacters());
				break;

			case 8: // '\b'
				handleEndDocument();
				documentEnded = true;
				break;

			case 14: // '\016'
				handleNotationDeclaration((NotationDeclaration)event);
				break;

			case 15: // '\017'
				handleEntityDeclaration((EntityDeclaration)event);
				break;

			case 5: // '\005'
				handleComment((Comment)event);
				break;

			case 11: // '\013'
				handleDtd((DTD)event);
				break;

			case 9: // '\t'
				handleEntityReference((EntityReference)event);
				break;
			}
		} while (true);
		if (documentStarted && !documentEnded)
			handleEndDocument();
	}

	private void handleStartDocument(XMLEvent event)
		throws SAXException
	{
		if (event.isStartDocument())
		{
			StartDocument startDocument = (StartDocument)event;
			String xmlVersion = startDocument.getVersion();
			if (StringUtils.hasLength(xmlVersion))
				this.xmlVersion = xmlVersion;
			if (startDocument.encodingSet())
				encoding = startDocument.getCharacterEncodingScheme();
		}
		if (getContentHandler() != null)
		{
			final Location location = event.getLocation();
			getContentHandler().setDocumentLocator(new Locator2() {

				final Location val$location;
				final StaxEventXMLReader this$0;

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
				this.this$0 = StaxEventXMLReader.this;
				location = location1;
				super();
			}
			});
			getContentHandler().startDocument();
		}
	}

	private void handleStartElement(StartElement startElement)
		throws SAXException
	{
		if (getContentHandler() != null)
		{
			QName qName = startElement.getName();
			if (hasNamespacesFeature())
			{
				Namespace namespace;
				for (Iterator i = startElement.getNamespaces(); i.hasNext(); startPrefixMapping(namespace.getPrefix(), namespace.getNamespaceURI()))
					namespace = (Namespace)i.next();

				QName attributeName;
				for (Iterator i = startElement.getAttributes(); i.hasNext(); startPrefixMapping(attributeName.getPrefix(), attributeName.getNamespaceURI()))
				{
					Attribute attribute = (Attribute)i.next();
					attributeName = attribute.getName();
				}

				getContentHandler().startElement(qName.getNamespaceURI(), qName.getLocalPart(), toQualifiedName(qName), getAttributes(startElement));
			} else
			{
				getContentHandler().startElement("", "", toQualifiedName(qName), getAttributes(startElement));
			}
		}
	}

	private void handleCharacters(Characters characters)
		throws SAXException
	{
		char data[] = characters.getData().toCharArray();
		if (getContentHandler() != null && characters.isIgnorableWhiteSpace())
		{
			getContentHandler().ignorableWhitespace(data, 0, data.length);
			return;
		}
		if (characters.isCData() && getLexicalHandler() != null)
			getLexicalHandler().startCDATA();
		if (getContentHandler() != null)
			getContentHandler().characters(data, 0, data.length);
		if (characters.isCData() && getLexicalHandler() != null)
			getLexicalHandler().endCDATA();
	}

	private void handleEndElement(EndElement endElement)
		throws SAXException
	{
		if (getContentHandler() != null)
		{
			QName qName = endElement.getName();
			if (hasNamespacesFeature())
			{
				getContentHandler().endElement(qName.getNamespaceURI(), qName.getLocalPart(), toQualifiedName(qName));
				Namespace namespace;
				for (Iterator i = endElement.getNamespaces(); i.hasNext(); endPrefixMapping(namespace.getPrefix()))
					namespace = (Namespace)i.next();

			} else
			{
				getContentHandler().endElement("", "", toQualifiedName(qName));
			}
		}
	}

	private void handleEndDocument()
		throws SAXException
	{
		if (getContentHandler() != null)
			getContentHandler().endDocument();
	}

	private void handleNotationDeclaration(NotationDeclaration declaration)
		throws SAXException
	{
		if (getDTDHandler() != null)
			getDTDHandler().notationDecl(declaration.getName(), declaration.getPublicId(), declaration.getSystemId());
	}

	private void handleEntityDeclaration(EntityDeclaration entityDeclaration)
		throws SAXException
	{
		if (getDTDHandler() != null)
			getDTDHandler().unparsedEntityDecl(entityDeclaration.getName(), entityDeclaration.getPublicId(), entityDeclaration.getSystemId(), entityDeclaration.getNotationName());
	}

	private void handleProcessingInstruction(ProcessingInstruction pi)
		throws SAXException
	{
		if (getContentHandler() != null)
			getContentHandler().processingInstruction(pi.getTarget(), pi.getData());
	}

	private void handleComment(Comment comment)
		throws SAXException
	{
		if (getLexicalHandler() != null)
		{
			char ch[] = comment.getText().toCharArray();
			getLexicalHandler().comment(ch, 0, ch.length);
		}
	}

	private void handleDtd(DTD dtd)
		throws SAXException
	{
		if (getLexicalHandler() != null)
		{
			Location location = dtd.getLocation();
			getLexicalHandler().startDTD(null, location.getPublicId(), location.getSystemId());
		}
		if (getLexicalHandler() != null)
			getLexicalHandler().endDTD();
	}

	private void handleEntityReference(EntityReference reference)
		throws SAXException
	{
		if (getLexicalHandler() != null)
			getLexicalHandler().startEntity(reference.getName());
		if (getLexicalHandler() != null)
			getLexicalHandler().endEntity(reference.getName());
	}

	private Attributes getAttributes(StartElement event)
	{
		AttributesImpl attributes = new AttributesImpl();
		Attribute attribute;
		QName qName;
		String namespace;
		String type;
		for (Iterator i = event.getAttributes(); i.hasNext(); attributes.addAttribute(namespace, qName.getLocalPart(), toQualifiedName(qName), type, attribute.getValue()))
		{
			attribute = (Attribute)i.next();
			qName = attribute.getName();
			namespace = qName.getNamespaceURI();
			if (namespace == null || !hasNamespacesFeature())
				namespace = "";
			type = attribute.getDTDType();
			if (type == null)
				type = "CDATA";
		}

		if (hasNamespacePrefixesFeature())
		{
			String namespaceUri;
			String qName;
			for (Iterator i = event.getNamespaces(); i.hasNext(); attributes.addAttribute("", "", qName, "CDATA", namespaceUri))
			{
				Namespace namespace = (Namespace)i.next();
				String prefix = namespace.getPrefix();
				namespaceUri = namespace.getNamespaceURI();
				if (StringUtils.hasLength(prefix))
					qName = (new StringBuilder()).append("xmlns:").append(prefix).toString();
				else
					qName = "xmlns";
			}

		}
		return attributes;
	}


}
