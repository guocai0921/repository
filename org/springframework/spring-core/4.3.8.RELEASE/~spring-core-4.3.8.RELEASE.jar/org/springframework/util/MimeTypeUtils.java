// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   MimeTypeUtils.java

package org.springframework.util;

import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.*;

// Referenced classes of package org.springframework.util:
//			InvalidMimeTypeException, MimeType, StringUtils, Assert

public abstract class MimeTypeUtils
{

	private static final byte BOUNDARY_CHARS[] = {
		45, 95, 49, 50, 51, 52, 53, 54, 55, 56, 
		57, 48, 97, 98, 99, 100, 101, 102, 103, 104, 
		105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 
		115, 116, 117, 118, 119, 120, 121, 122, 65, 66, 
		67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 
		77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 
		87, 88, 89, 90
	};
	private static final Random RND = new Random();
	private static Charset US_ASCII = Charset.forName("US-ASCII");
	public static final Comparator SPECIFICITY_COMPARATOR = new MimeType.SpecificityComparator();
	public static final MimeType ALL = MimeType.valueOf("*/*");
	public static final String ALL_VALUE = "*/*";
	/**
	 * @deprecated Field APPLICATION_ATOM_XML is deprecated
	 */
	public static final MimeType APPLICATION_ATOM_XML = MimeType.valueOf("application/atom+xml");
	/**
	 * @deprecated Field APPLICATION_ATOM_XML_VALUE is deprecated
	 */
	public static final String APPLICATION_ATOM_XML_VALUE = "application/atom+xml";
	/**
	 * @deprecated Field APPLICATION_FORM_URLENCODED is deprecated
	 */
	public static final MimeType APPLICATION_FORM_URLENCODED = MimeType.valueOf("application/x-www-form-urlencoded");
	/**
	 * @deprecated Field APPLICATION_FORM_URLENCODED_VALUE is deprecated
	 */
	public static final String APPLICATION_FORM_URLENCODED_VALUE = "application/x-www-form-urlencoded";
	public static final MimeType APPLICATION_JSON = MimeType.valueOf("application/json");
	public static final String APPLICATION_JSON_VALUE = "application/json";
	public static final MimeType APPLICATION_OCTET_STREAM = MimeType.valueOf("application/octet-stream");
	public static final String APPLICATION_OCTET_STREAM_VALUE = "application/octet-stream";
	/**
	 * @deprecated Field APPLICATION_XHTML_XML is deprecated
	 */
	public static final MimeType APPLICATION_XHTML_XML = MimeType.valueOf("application/xhtml+xml");
	/**
	 * @deprecated Field APPLICATION_XHTML_XML_VALUE is deprecated
	 */
	public static final String APPLICATION_XHTML_XML_VALUE = "application/xhtml+xml";
	public static final MimeType APPLICATION_XML = MimeType.valueOf("application/xml");
	public static final String APPLICATION_XML_VALUE = "application/xml";
	public static final MimeType IMAGE_GIF = MimeType.valueOf("image/gif");
	public static final String IMAGE_GIF_VALUE = "image/gif";
	public static final MimeType IMAGE_JPEG = MimeType.valueOf("image/jpeg");
	public static final String IMAGE_JPEG_VALUE = "image/jpeg";
	public static final MimeType IMAGE_PNG = MimeType.valueOf("image/png");
	public static final String IMAGE_PNG_VALUE = "image/png";
	/**
	 * @deprecated Field MULTIPART_FORM_DATA is deprecated
	 */
	public static final MimeType MULTIPART_FORM_DATA = MimeType.valueOf("multipart/form-data");
	/**
	 * @deprecated Field MULTIPART_FORM_DATA_VALUE is deprecated
	 */
	public static final String MULTIPART_FORM_DATA_VALUE = "multipart/form-data";
	public static final MimeType TEXT_HTML = MimeType.valueOf("text/html");
	public static final String TEXT_HTML_VALUE = "text/html";
	public static final MimeType TEXT_PLAIN = MimeType.valueOf("text/plain");
	public static final String TEXT_PLAIN_VALUE = "text/plain";
	public static final MimeType TEXT_XML = MimeType.valueOf("text/xml");
	public static final String TEXT_XML_VALUE = "text/xml";

	public MimeTypeUtils()
	{
	}

	public static MimeType parseMimeType(String mimeType)
	{
		String type;
		String subtype;
		Map parameters;
		if (!StringUtils.hasLength(mimeType))
			throw new InvalidMimeTypeException(mimeType, "'mimeType' must not be empty");
		int index = mimeType.indexOf(';');
		String fullType = (index < 0 ? mimeType : mimeType.substring(0, index)).trim();
		if (fullType.isEmpty())
			throw new InvalidMimeTypeException(mimeType, "'mimeType' must not be empty");
		if ("*".equals(fullType))
			fullType = "*/*";
		int subIndex = fullType.indexOf('/');
		if (subIndex == -1)
			throw new InvalidMimeTypeException(mimeType, "does not contain '/'");
		if (subIndex == fullType.length() - 1)
			throw new InvalidMimeTypeException(mimeType, "does not contain subtype after '/'");
		type = fullType.substring(0, subIndex);
		subtype = fullType.substring(subIndex + 1, fullType.length());
		if ("*".equals(type) && !"*".equals(subtype))
			throw new InvalidMimeTypeException(mimeType, "wildcard type is legal only in '*/*' (all mime types)");
		parameters = null;
		do
		{
			int nextIndex = index + 1;
			boolean quoted = false;
			for (; nextIndex < mimeType.length(); nextIndex++)
			{
				char ch = mimeType.charAt(nextIndex);
				if (ch == ';')
				{
					if (!quoted)
						break;
					continue;
				}
				if (ch == '"')
					quoted = !quoted;
			}

			String parameter = mimeType.substring(index + 1, nextIndex).trim();
			if (parameter.length() > 0)
			{
				if (parameters == null)
					parameters = new LinkedHashMap(4);
				int eqIndex = parameter.indexOf('=');
				if (eqIndex >= 0)
				{
					String attribute = parameter.substring(0, eqIndex);
					String value = parameter.substring(eqIndex + 1, parameter.length());
					parameters.put(attribute, value);
				}
			}
			index = nextIndex;
		} while (index < mimeType.length());
		return new MimeType(type, subtype, parameters);
		UnsupportedCharsetException ex;
		ex;
		throw new InvalidMimeTypeException(mimeType, (new StringBuilder()).append("unsupported charset '").append(ex.getCharsetName()).append("'").toString());
		ex;
		throw new InvalidMimeTypeException(mimeType, ex.getMessage());
	}

	public static List parseMimeTypes(String mimeTypes)
	{
		if (!StringUtils.hasLength(mimeTypes))
			return Collections.emptyList();
		String tokens[] = StringUtils.tokenizeToStringArray(mimeTypes, ",");
		List result = new ArrayList(tokens.length);
		String as[] = tokens;
		int i = as.length;
		for (int j = 0; j < i; j++)
		{
			String token = as[j];
			result.add(parseMimeType(token));
		}

		return result;
	}

	public static String toString(Collection mimeTypes)
	{
		StringBuilder builder = new StringBuilder();
		Iterator iterator = mimeTypes.iterator();
		do
		{
			if (!iterator.hasNext())
				break;
			MimeType mimeType = (MimeType)iterator.next();
			mimeType.appendTo(builder);
			if (iterator.hasNext())
				builder.append(", ");
		} while (true);
		return builder.toString();
	}

	public static void sortBySpecificity(List mimeTypes)
	{
		Assert.notNull(mimeTypes, "'mimeTypes' must not be null");
		if (mimeTypes.size() > 1)
			Collections.sort(mimeTypes, SPECIFICITY_COMPARATOR);
	}

	public static byte[] generateMultipartBoundary()
	{
		byte boundary[] = new byte[RND.nextInt(11) + 30];
		for (int i = 0; i < boundary.length; i++)
			boundary[i] = BOUNDARY_CHARS[RND.nextInt(BOUNDARY_CHARS.length)];

		return boundary;
	}

	public static String generateMultipartBoundaryString()
	{
		return new String(generateMultipartBoundary(), US_ASCII);
	}

}
