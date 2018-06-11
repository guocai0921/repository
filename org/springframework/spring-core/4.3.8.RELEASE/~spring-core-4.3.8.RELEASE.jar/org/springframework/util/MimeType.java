// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   MimeType.java

package org.springframework.util;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.*;

// Referenced classes of package org.springframework.util:
//			LinkedCaseInsensitiveMap, Assert, CollectionUtils, ObjectUtils, 
//			MimeTypeUtils

public class MimeType
	implements Comparable, Serializable
{
	public static class SpecificityComparator
		implements Comparator
	{

		public int compare(MimeType mimeType1, MimeType mimeType2)
		{
			if (mimeType1.isWildcardType() && !mimeType2.isWildcardType())
				return 1;
			if (mimeType2.isWildcardType() && !mimeType1.isWildcardType())
				return -1;
			if (!mimeType1.getType().equals(mimeType2.getType()))
				return 0;
			if (mimeType1.isWildcardSubtype() && !mimeType2.isWildcardSubtype())
				return 1;
			if (mimeType2.isWildcardSubtype() && !mimeType1.isWildcardSubtype())
				return -1;
			if (!mimeType1.getSubtype().equals(mimeType2.getSubtype()))
				return 0;
			else
				return compareParameters(mimeType1, mimeType2);
		}

		protected int compareParameters(MimeType mimeType1, MimeType mimeType2)
		{
			int paramsSize1 = mimeType1.getParameters().size();
			int paramsSize2 = mimeType2.getParameters().size();
			return paramsSize2 >= paramsSize1 ? ((int) (paramsSize2 != paramsSize1 ? 1 : 0)) : -1;
		}

		public volatile int compare(Object obj, Object obj1)
		{
			return compare((MimeType)obj, (MimeType)obj1);
		}

		public SpecificityComparator()
		{
		}
	}


	private static final long serialVersionUID = 0x38b41dc14060dcafL;
	protected static final String WILDCARD_TYPE = "*";
	private static final String PARAM_CHARSET = "charset";
	private static final BitSet TOKEN;
	private final String type;
	private final String subtype;
	private final Map parameters;

	public MimeType(String type)
	{
		this(type, "*");
	}

	public MimeType(String type, String subtype)
	{
		this(type, subtype, Collections.emptyMap());
	}

	public MimeType(String type, String subtype, Charset charset)
	{
		this(type, subtype, Collections.singletonMap("charset", charset.name()));
	}

	public MimeType(MimeType other, Charset charset)
	{
		this(other.getType(), other.getSubtype(), addCharsetParameter(charset, other.getParameters()));
	}

	public MimeType(MimeType other, Map parameters)
	{
		this(other.getType(), other.getSubtype(), parameters);
	}

	public MimeType(String type, String subtype, Map parameters)
	{
		Assert.hasLength(type, "'type' must not be empty");
		Assert.hasLength(subtype, "'subtype' must not be empty");
		checkToken(type);
		checkToken(subtype);
		this.type = type.toLowerCase(Locale.ENGLISH);
		this.subtype = subtype.toLowerCase(Locale.ENGLISH);
		if (!CollectionUtils.isEmpty(parameters))
		{
			Map map = new LinkedCaseInsensitiveMap(parameters.size(), Locale.ENGLISH);
			String attribute;
			String value;
			for (Iterator iterator = parameters.entrySet().iterator(); iterator.hasNext(); map.put(attribute, value))
			{
				java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
				attribute = (String)entry.getKey();
				value = (String)entry.getValue();
				checkParameters(attribute, value);
			}

			this.parameters = Collections.unmodifiableMap(map);
		} else
		{
			this.parameters = Collections.emptyMap();
		}
	}

	private void checkToken(String token)
	{
		for (int i = 0; i < token.length(); i++)
		{
			char ch = token.charAt(i);
			if (!TOKEN.get(ch))
				throw new IllegalArgumentException((new StringBuilder()).append("Invalid token character '").append(ch).append("' in token \"").append(token).append("\"").toString());
		}

	}

	protected void checkParameters(String attribute, String value)
	{
		Assert.hasLength(attribute, "'attribute' must not be empty");
		Assert.hasLength(value, "'value' must not be empty");
		checkToken(attribute);
		if ("charset".equals(attribute))
		{
			value = unquote(value);
			Charset.forName(value);
		} else
		if (!isQuotedString(value))
			checkToken(value);
	}

	private boolean isQuotedString(String s)
	{
		if (s.length() < 2)
			return false;
		else
			return s.startsWith("\"") && s.endsWith("\"") || s.startsWith("'") && s.endsWith("'");
	}

	protected String unquote(String s)
	{
		if (s == null)
			return null;
		else
			return isQuotedString(s) ? s.substring(1, s.length() - 1) : s;
	}

	public boolean isWildcardType()
	{
		return "*".equals(getType());
	}

	public boolean isWildcardSubtype()
	{
		return "*".equals(getSubtype()) || getSubtype().startsWith("*+");
	}

	public boolean isConcrete()
	{
		return !isWildcardType() && !isWildcardSubtype();
	}

	public String getType()
	{
		return type;
	}

	public String getSubtype()
	{
		return subtype;
	}

	public Charset getCharset()
	{
		String charset = getParameter("charset");
		return charset == null ? null : Charset.forName(unquote(charset));
	}

	/**
	 * @deprecated Method getCharSet is deprecated
	 */

	public Charset getCharSet()
	{
		return getCharset();
	}

	public String getParameter(String name)
	{
		return (String)parameters.get(name);
	}

	public Map getParameters()
	{
		return parameters;
	}

	public boolean includes(MimeType other)
	{
		if (other == null)
			return false;
		if (isWildcardType())
			return true;
		if (getType().equals(other.getType()))
		{
			if (getSubtype().equals(other.getSubtype()))
				return true;
			if (isWildcardSubtype())
			{
				int thisPlusIdx = getSubtype().indexOf('+');
				if (thisPlusIdx == -1)
					return true;
				int otherPlusIdx = other.getSubtype().indexOf('+');
				if (otherPlusIdx != -1)
				{
					String thisSubtypeNoSuffix = getSubtype().substring(0, thisPlusIdx);
					String thisSubtypeSuffix = getSubtype().substring(thisPlusIdx + 1);
					String otherSubtypeSuffix = other.getSubtype().substring(otherPlusIdx + 1);
					if (thisSubtypeSuffix.equals(otherSubtypeSuffix) && "*".equals(thisSubtypeNoSuffix))
						return true;
				}
			}
		}
		return false;
	}

	public boolean isCompatibleWith(MimeType other)
	{
		if (other == null)
			return false;
		if (isWildcardType() || other.isWildcardType())
			return true;
		if (getType().equals(other.getType()))
		{
			if (getSubtype().equals(other.getSubtype()))
				return true;
			if (isWildcardSubtype() || other.isWildcardSubtype())
			{
				int thisPlusIdx = getSubtype().indexOf('+');
				int otherPlusIdx = other.getSubtype().indexOf('+');
				if (thisPlusIdx == -1 && otherPlusIdx == -1)
					return true;
				if (thisPlusIdx != -1 && otherPlusIdx != -1)
				{
					String thisSubtypeNoSuffix = getSubtype().substring(0, thisPlusIdx);
					String otherSubtypeNoSuffix = other.getSubtype().substring(0, otherPlusIdx);
					String thisSubtypeSuffix = getSubtype().substring(thisPlusIdx + 1);
					String otherSubtypeSuffix = other.getSubtype().substring(otherPlusIdx + 1);
					if (thisSubtypeSuffix.equals(otherSubtypeSuffix) && ("*".equals(thisSubtypeNoSuffix) || "*".equals(otherSubtypeNoSuffix)))
						return true;
				}
			}
		}
		return false;
	}

	public boolean equals(Object other)
	{
		if (this == other)
			return true;
		if (!(other instanceof MimeType))
		{
			return false;
		} else
		{
			MimeType otherType = (MimeType)other;
			return type.equalsIgnoreCase(otherType.type) && subtype.equalsIgnoreCase(otherType.subtype) && parametersAreEqual(otherType);
		}
	}

	private boolean parametersAreEqual(MimeType other)
	{
label0:
		{
			if (parameters.size() != other.parameters.size())
				return false;
			Iterator iterator = parameters.keySet().iterator();
			String key;
label1:
			do
			{
				do
				{
					if (!iterator.hasNext())
						break label0;
					key = (String)iterator.next();
					if (!other.parameters.containsKey(key))
						return false;
					if (!"charset".equals(key))
						continue label1;
				} while (ObjectUtils.nullSafeEquals(getCharset(), other.getCharset()));
				return false;
			} while (ObjectUtils.nullSafeEquals(parameters.get(key), other.parameters.get(key)));
			return false;
		}
		return true;
	}

	public int hashCode()
	{
		int result = type.hashCode();
		result = 31 * result + subtype.hashCode();
		result = 31 * result + parameters.hashCode();
		return result;
	}

	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		appendTo(builder);
		return builder.toString();
	}

	protected void appendTo(StringBuilder builder)
	{
		builder.append(type);
		builder.append('/');
		builder.append(subtype);
		appendTo(parameters, builder);
	}

	private void appendTo(Map map, StringBuilder builder)
	{
		java.util.Map.Entry entry;
		for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext(); builder.append((String)entry.getValue()))
		{
			entry = (java.util.Map.Entry)iterator.next();
			builder.append(';');
			builder.append((String)entry.getKey());
			builder.append('=');
		}

	}

	public int compareTo(MimeType other)
	{
		int comp = getType().compareToIgnoreCase(other.getType());
		if (comp != 0)
			return comp;
		comp = getSubtype().compareToIgnoreCase(other.getSubtype());
		if (comp != 0)
			return comp;
		comp = getParameters().size() - other.getParameters().size();
		if (comp != 0)
			return comp;
		TreeSet thisAttributes = new TreeSet(String.CASE_INSENSITIVE_ORDER);
		thisAttributes.addAll(getParameters().keySet());
		TreeSet otherAttributes = new TreeSet(String.CASE_INSENSITIVE_ORDER);
		otherAttributes.addAll(other.getParameters().keySet());
		Iterator thisAttributesIterator = thisAttributes.iterator();
		Iterator otherAttributesIterator = otherAttributes.iterator();
		while (thisAttributesIterator.hasNext()) 
		{
			String thisAttribute = (String)thisAttributesIterator.next();
			String otherAttribute = (String)otherAttributesIterator.next();
			comp = thisAttribute.compareToIgnoreCase(otherAttribute);
			if (comp != 0)
				return comp;
			String thisValue = (String)getParameters().get(thisAttribute);
			String otherValue = (String)other.getParameters().get(otherAttribute);
			if (otherValue == null)
				otherValue = "";
			comp = thisValue.compareTo(otherValue);
			if (comp != 0)
				return comp;
		}
		return 0;
	}

	public static MimeType valueOf(String value)
	{
		return MimeTypeUtils.parseMimeType(value);
	}

	private static Map addCharsetParameter(Charset charset, Map parameters)
	{
		Map map = new LinkedHashMap(parameters);
		map.put("charset", charset.name());
		return map;
	}

	public volatile int compareTo(Object obj)
	{
		return compareTo((MimeType)obj);
	}

	static 
	{
		BitSet ctl = new BitSet(128);
		for (int i = 0; i <= 31; i++)
			ctl.set(i);

		ctl.set(127);
		BitSet separators = new BitSet(128);
		separators.set(40);
		separators.set(41);
		separators.set(60);
		separators.set(62);
		separators.set(64);
		separators.set(44);
		separators.set(59);
		separators.set(58);
		separators.set(92);
		separators.set(34);
		separators.set(47);
		separators.set(91);
		separators.set(93);
		separators.set(63);
		separators.set(61);
		separators.set(123);
		separators.set(125);
		separators.set(32);
		separators.set(9);
		TOKEN = new BitSet(128);
		TOKEN.set(0, 128);
		TOKEN.andNot(ctl);
		TOKEN.andNot(separators);
	}
}
