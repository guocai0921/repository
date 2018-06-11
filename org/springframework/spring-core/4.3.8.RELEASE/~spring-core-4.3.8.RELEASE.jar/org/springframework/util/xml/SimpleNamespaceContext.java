// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SimpleNamespaceContext.java

package org.springframework.util.xml;

import java.util.*;
import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import org.springframework.util.Assert;

public class SimpleNamespaceContext
	implements NamespaceContext
{

	private final Map prefixToNamespaceUri = new HashMap();
	private final Map namespaceUriToPrefixes = new HashMap();
	private String defaultNamespaceUri;

	public SimpleNamespaceContext()
	{
		defaultNamespaceUri = "";
	}

	public String getNamespaceURI(String prefix)
	{
		Assert.notNull(prefix, "No prefix given");
		if ("xml".equals(prefix))
			return "http://www.w3.org/XML/1998/namespace";
		if ("xmlns".equals(prefix))
			return "http://www.w3.org/2000/xmlns/";
		if ("".equals(prefix))
			return defaultNamespaceUri;
		if (prefixToNamespaceUri.containsKey(prefix))
			return (String)prefixToNamespaceUri.get(prefix);
		else
			return "";
	}

	public String getPrefix(String namespaceUri)
	{
		Set prefixes = getPrefixesSet(namespaceUri);
		return prefixes.isEmpty() ? null : (String)prefixes.iterator().next();
	}

	public Iterator getPrefixes(String namespaceUri)
	{
		return getPrefixesSet(namespaceUri).iterator();
	}

	private Set getPrefixesSet(String namespaceUri)
	{
		Assert.notNull(namespaceUri, "No namespaceUri given");
		if (defaultNamespaceUri.equals(namespaceUri))
			return Collections.singleton("");
		if ("http://www.w3.org/XML/1998/namespace".equals(namespaceUri))
			return Collections.singleton("xml");
		if ("http://www.w3.org/2000/xmlns/".equals(namespaceUri))
		{
			return Collections.singleton("xmlns");
		} else
		{
			Set prefixes = (Set)namespaceUriToPrefixes.get(namespaceUri);
			return prefixes == null ? Collections.emptySet() : Collections.unmodifiableSet(prefixes);
		}
	}

	public void setBindings(Map bindings)
	{
		java.util.Map.Entry entry;
		for (Iterator iterator = bindings.entrySet().iterator(); iterator.hasNext(); bindNamespaceUri((String)entry.getKey(), (String)entry.getValue()))
			entry = (java.util.Map.Entry)iterator.next();

	}

	public void bindDefaultNamespaceUri(String namespaceUri)
	{
		bindNamespaceUri("", namespaceUri);
	}

	public void bindNamespaceUri(String prefix, String namespaceUri)
	{
		Assert.notNull(prefix, "No prefix given");
		Assert.notNull(namespaceUri, "No namespaceUri given");
		if ("".equals(prefix))
		{
			defaultNamespaceUri = namespaceUri;
		} else
		{
			prefixToNamespaceUri.put(prefix, namespaceUri);
			Set prefixes = (Set)namespaceUriToPrefixes.get(namespaceUri);
			if (prefixes == null)
			{
				prefixes = new LinkedHashSet();
				namespaceUriToPrefixes.put(namespaceUri, prefixes);
			}
			prefixes.add(prefix);
		}
	}

	public void removeBinding(String prefix)
	{
		if ("".equals(prefix))
			defaultNamespaceUri = "";
		else
		if (prefix != null)
		{
			String namespaceUri = (String)prefixToNamespaceUri.remove(prefix);
			if (namespaceUri != null)
			{
				Set prefixes = (Set)namespaceUriToPrefixes.get(namespaceUri);
				if (prefixes != null)
				{
					prefixes.remove(prefix);
					if (prefixes.isEmpty())
						namespaceUriToPrefixes.remove(namespaceUri);
				}
			}
		}
	}

	public void clear()
	{
		prefixToNamespaceUri.clear();
		namespaceUriToPrefixes.clear();
	}

	public Iterator getBoundPrefixes()
	{
		return prefixToNamespaceUri.keySet().iterator();
	}
}
