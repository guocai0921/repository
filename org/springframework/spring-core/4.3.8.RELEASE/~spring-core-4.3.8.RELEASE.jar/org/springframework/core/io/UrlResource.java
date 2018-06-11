// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   UrlResource.java

package org.springframework.core.io;

import java.io.*;
import java.net.*;
import org.springframework.util.*;

// Referenced classes of package org.springframework.core.io:
//			AbstractFileResolvingResource, Resource

public class UrlResource extends AbstractFileResolvingResource
{

	private final URI uri;
	private final URL url;
	private final URL cleanedUrl;

	public UrlResource(URI uri)
		throws MalformedURLException
	{
		Assert.notNull(uri, "URI must not be null");
		this.uri = uri;
		url = uri.toURL();
		cleanedUrl = getCleanedUrl(url, uri.toString());
	}

	public UrlResource(URL url)
	{
		Assert.notNull(url, "URL must not be null");
		this.url = url;
		cleanedUrl = getCleanedUrl(this.url, url.toString());
		uri = null;
	}

	public UrlResource(String path)
		throws MalformedURLException
	{
		Assert.notNull(path, "Path must not be null");
		uri = null;
		url = new URL(path);
		cleanedUrl = getCleanedUrl(url, path);
	}

	public UrlResource(String protocol, String location)
		throws MalformedURLException
	{
		this(protocol, location, null);
	}

	public UrlResource(String protocol, String location, String fragment)
		throws MalformedURLException
	{
		try
		{
			uri = new URI(protocol, location, fragment);
			url = uri.toURL();
			cleanedUrl = getCleanedUrl(url, uri.toString());
		}
		catch (URISyntaxException ex)
		{
			MalformedURLException exToThrow = new MalformedURLException(ex.getMessage());
			exToThrow.initCause(ex);
			throw exToThrow;
		}
	}

	private URL getCleanedUrl(URL originalUrl, String originalPath)
	{
		return new URL(StringUtils.cleanPath(originalPath));
		MalformedURLException ex;
		ex;
		return originalUrl;
	}

	public InputStream getInputStream()
		throws IOException
	{
		URLConnection con;
		con = url.openConnection();
		ResourceUtils.useCachesIfNecessary(con);
		return con.getInputStream();
		IOException ex;
		ex;
		if (con instanceof HttpURLConnection)
			((HttpURLConnection)con).disconnect();
		throw ex;
	}

	public URL getURL()
		throws IOException
	{
		return url;
	}

	public URI getURI()
		throws IOException
	{
		if (uri != null)
			return uri;
		else
			return super.getURI();
	}

	public File getFile()
		throws IOException
	{
		if (uri != null)
			return super.getFile(uri);
		else
			return super.getFile();
	}

	public Resource createRelative(String relativePath)
		throws MalformedURLException
	{
		if (relativePath.startsWith("/"))
			relativePath = relativePath.substring(1);
		return new UrlResource(new URL(url, relativePath));
	}

	public String getFilename()
	{
		return StringUtils.getFilename(cleanedUrl.getPath());
	}

	public String getDescription()
	{
		return (new StringBuilder()).append("URL [").append(url).append("]").toString();
	}

	public boolean equals(Object obj)
	{
		return obj == this || (obj instanceof UrlResource) && cleanedUrl.equals(((UrlResource)obj).cleanedUrl);
	}

	public int hashCode()
	{
		return cleanedUrl.hashCode();
	}
}
