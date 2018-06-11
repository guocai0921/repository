// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AbstractStaxXMLReader.java

package org.springframework.util.xml;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import org.springframework.util.StringUtils;
import org.xml.sax.*;

// Referenced classes of package org.springframework.util.xml:
//			AbstractXMLReader

abstract class AbstractStaxXMLReader extends AbstractXMLReader
{
	private static class StaxLocator
		implements Locator
	{

		private final Location location;

		public String getPublicId()
		{
			return location.getPublicId();
		}

		public String getSystemId()
		{
			return location.getSystemId();
		}

		public int getLineNumber()
		{
			return location.getLineNumber();
		}

		public int getColumnNumber()
		{
			return location.getColumnNumber();
		}

		public StaxLocator(Location location)
		{
			this.location = location;
		}
	}


	private static final String NAMESPACES_FEATURE_NAME = "http://xml.org/sax/features/namespaces";
	private static final String NAMESPACE_PREFIXES_FEATURE_NAME = "http://xml.org/sax/features/namespace-prefixes";
	private static final String IS_STANDALONE_FEATURE_NAME = "http://xml.org/sax/features/is-standalone";
	private boolean namespacesFeature;
	private boolean namespacePrefixesFeature;
	private Boolean isStandalone;
	private final Map namespaces = new LinkedHashMap();

	AbstractStaxXMLReader()
	{
		namespacesFeature = true;
		namespacePrefixesFeature = false;
	}

	public boolean getFeature(String name)
		throws SAXNotRecognizedException, SAXNotSupportedException
	{
		if ("http://xml.org/sax/features/namespaces".equals(name))
			return namespacesFeature;
		if ("http://xml.org/sax/features/namespace-prefixes".equals(name))
			return namespacePrefixesFeature;
		if ("http://xml.org/sax/features/is-standalone".equals(name))
		{
			if (isStandalone != null)
				return isStandalone.booleanValue();
			else
				throw new SAXNotSupportedException("startDocument() callback not completed yet");
		} else
		{
			return super.getFeature(name);
		}
	}

	public void setFeature(String name, boolean value)
		throws SAXNotRecognizedException, SAXNotSupportedException
	{
		if ("http://xml.org/sax/features/namespaces".equals(name))
			namespacesFeature = value;
		else
		if ("http://xml.org/sax/features/namespace-prefixes".equals(name))
			namespacePrefixesFeature = value;
		else
			super.setFeature(name, value);
	}

	protected void setStandalone(boolean standalone)
	{
		isStandalone = Boolean.valueOf(standalone);
	}

	protected boolean hasNamespacesFeature()
	{
		return namespacesFeature;
	}

	protected boolean hasNamespacePrefixesFeature()
	{
		return namespacePrefixesFeature;
	}

	protected String toQualifiedName(QName qName)
	{
		String prefix = qName.getPrefix();
		if (!StringUtils.hasLength(prefix))
			return qName.getLocalPart();
		else
			return (new StringBuilder()).append(prefix).append(":").append(qName.getLocalPart()).toString();
	}

	public final void parse(InputSource ignored)
		throws SAXException
	{
		parse();
	}

	public final void parse(String ignored)
		throws SAXException
	{
		parse();
	}

	private void parse()
		throws SAXException
	{
		try
		{
			parseInternal();
		}
		catch (XMLStreamException ex)
		{
			Locator locator = null;
			if (ex.getLocation() != null)
				locator = new StaxLocator(ex.getLocation());
			SAXParseException saxException = new SAXParseException(ex.getMessage(), locator, ex);
			if (getErrorHandler() != null)
				getErrorHandler().fatalError(saxException);
			else
				throw saxException;
		}
	}

	protected abstract void parseInternal()
		throws SAXException, XMLStreamException;

	protected void startPrefixMapping(String prefix, String namespace)
		throws SAXException
	{
		if (getContentHandler() != null)
		{
			if (prefix == null)
				prefix = "";
			if (!StringUtils.hasLength(namespace))
				return;
			if (!namespace.equals(namespaces.get(prefix)))
			{
				getContentHandler().startPrefixMapping(prefix, namespace);
				namespaces.put(prefix, namespace);
			}
		}
	}

	protected void endPrefixMapping(String prefix)
		throws SAXException
	{
		if (getContentHandler() != null && namespaces.containsKey(prefix))
		{
			getContentHandler().endPrefixMapping(prefix);
			namespaces.remove(prefix);
		}
	}
}
