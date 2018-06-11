// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AbstractFileResolvingResource.java

package org.springframework.core.io;

import java.io.*;
import java.net.*;
import org.springframework.util.ResourceUtils;

// Referenced classes of package org.springframework.core.io:
//			AbstractResource, Resource, VfsResource, VfsUtils

public abstract class AbstractFileResolvingResource extends AbstractResource
{
	private static class VfsResourceDelegate
	{

		public static Resource getResource(URL url)
			throws IOException
		{
			return new VfsResource(VfsUtils.getRoot(url));
		}

		public static Resource getResource(URI uri)
			throws IOException
		{
			return new VfsResource(VfsUtils.getRoot(uri));
		}

		private VfsResourceDelegate()
		{
		}
	}


	public AbstractFileResolvingResource()
	{
	}

	public File getFile()
		throws IOException
	{
		URL url = getURL();
		if (url.getProtocol().startsWith("vfs"))
			return VfsResourceDelegate.getResource(url).getFile();
		else
			return ResourceUtils.getFile(url, getDescription());
	}

	protected File getFileForLastModifiedCheck()
		throws IOException
	{
		URL url = getURL();
		if (ResourceUtils.isJarURL(url))
		{
			URL actualUrl = ResourceUtils.extractArchiveURL(url);
			if (actualUrl.getProtocol().startsWith("vfs"))
				return VfsResourceDelegate.getResource(actualUrl).getFile();
			else
				return ResourceUtils.getFile(actualUrl, "Jar URL");
		} else
		{
			return getFile();
		}
	}

	protected File getFile(URI uri)
		throws IOException
	{
		if (uri.getScheme().startsWith("vfs"))
			return VfsResourceDelegate.getResource(uri).getFile();
		else
			return ResourceUtils.getFile(uri, getDescription());
	}

	public boolean exists()
	{
		URL url = getURL();
		if (ResourceUtils.isFileURL(url))
			return getFile().exists();
		URLConnection con;
		HttpURLConnection httpCon;
		int code;
		con = url.openConnection();
		customizeConnection(con);
		httpCon = (con instanceof HttpURLConnection) ? (HttpURLConnection)con : null;
		if (httpCon == null)
			break MISSING_BLOCK_LABEL_76;
		code = httpCon.getResponseCode();
		if (code == 200)
			return true;
		if (code == 404)
			return false;
		if (con.getContentLength() >= 0)
			return true;
		if (httpCon == null)
			break MISSING_BLOCK_LABEL_95;
		httpCon.disconnect();
		return false;
		InputStream is = getInputStream();
		is.close();
		return true;
		IOException ex;
		ex;
		return false;
	}

	public boolean isReadable()
	{
		File file;
		URL url = getURL();
		if (!ResourceUtils.isFileURL(url))
			break MISSING_BLOCK_LABEL_37;
		file = getFile();
		return file.canRead() && !file.isDirectory();
		return true;
		IOException ex;
		ex;
		return false;
	}

	public long contentLength()
		throws IOException
	{
		URL url = getURL();
		if (ResourceUtils.isFileURL(url))
		{
			return getFile().length();
		} else
		{
			URLConnection con = url.openConnection();
			customizeConnection(con);
			return (long)con.getContentLength();
		}
	}

	public long lastModified()
		throws IOException
	{
		URL url = getURL();
		if (ResourceUtils.isFileURL(url) || ResourceUtils.isJarURL(url))
		{
			return super.lastModified();
		} else
		{
			URLConnection con = url.openConnection();
			customizeConnection(con);
			return con.getLastModified();
		}
	}

	protected void customizeConnection(URLConnection con)
		throws IOException
	{
		ResourceUtils.useCachesIfNecessary(con);
		if (con instanceof HttpURLConnection)
			customizeConnection((HttpURLConnection)con);
	}

	protected void customizeConnection(HttpURLConnection con)
		throws IOException
	{
		con.setRequestMethod("HEAD");
	}
}
