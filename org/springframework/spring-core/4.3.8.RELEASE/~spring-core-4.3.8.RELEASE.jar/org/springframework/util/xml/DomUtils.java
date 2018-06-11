// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DomUtils.java

package org.springframework.util.xml;

import java.util.*;
import org.springframework.util.Assert;
import org.w3c.dom.*;
import org.xml.sax.ContentHandler;

// Referenced classes of package org.springframework.util.xml:
//			DomContentHandler

public abstract class DomUtils
{

	public DomUtils()
	{
	}

	public static transient List getChildElementsByTagName(Element ele, String childEleNames[])
	{
		Assert.notNull(ele, "Element must not be null");
		Assert.notNull(childEleNames, "Element names collection must not be null");
		List childEleNameList = Arrays.asList(childEleNames);
		NodeList nl = ele.getChildNodes();
		List childEles = new ArrayList();
		for (int i = 0; i < nl.getLength(); i++)
		{
			Node node = nl.item(i);
			if ((node instanceof Element) && nodeNameMatch(node, childEleNameList))
				childEles.add((Element)node);
		}

		return childEles;
	}

	public static List getChildElementsByTagName(Element ele, String childEleName)
	{
		return getChildElementsByTagName(ele, new String[] {
			childEleName
		});
	}

	public static Element getChildElementByTagName(Element ele, String childEleName)
	{
		Assert.notNull(ele, "Element must not be null");
		Assert.notNull(childEleName, "Element name must not be null");
		NodeList nl = ele.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++)
		{
			Node node = nl.item(i);
			if ((node instanceof Element) && nodeNameMatch(node, childEleName))
				return (Element)node;
		}

		return null;
	}

	public static String getChildElementValueByTagName(Element ele, String childEleName)
	{
		Element child = getChildElementByTagName(ele, childEleName);
		return child == null ? null : getTextValue(child);
	}

	public static List getChildElements(Element ele)
	{
		Assert.notNull(ele, "Element must not be null");
		NodeList nl = ele.getChildNodes();
		List childEles = new ArrayList();
		for (int i = 0; i < nl.getLength(); i++)
		{
			Node node = nl.item(i);
			if (node instanceof Element)
				childEles.add((Element)node);
		}

		return childEles;
	}

	public static String getTextValue(Element valueEle)
	{
		Assert.notNull(valueEle, "Element must not be null");
		StringBuilder sb = new StringBuilder();
		NodeList nl = valueEle.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++)
		{
			Node item = nl.item(i);
			if ((item instanceof CharacterData) && !(item instanceof Comment) || (item instanceof EntityReference))
				sb.append(item.getNodeValue());
		}

		return sb.toString();
	}

	public static boolean nodeNameEquals(Node node, String desiredName)
	{
		Assert.notNull(node, "Node must not be null");
		Assert.notNull(desiredName, "Desired name must not be null");
		return nodeNameMatch(node, desiredName);
	}

	public static ContentHandler createContentHandler(Node node)
	{
		return new DomContentHandler(node);
	}

	private static boolean nodeNameMatch(Node node, String desiredName)
	{
		return desiredName.equals(node.getNodeName()) || desiredName.equals(node.getLocalName());
	}

	private static boolean nodeNameMatch(Node node, Collection desiredNames)
	{
		return desiredNames.contains(node.getNodeName()) || desiredNames.contains(node.getLocalName());
	}
}
