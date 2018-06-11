// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ResourceUtils.java

package org.springframework.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.*;

// Referenced classes of package org.springframework.util:
//			Assert, ClassUtils, StringUtils

public abstract class ResourceUtils
{

	public static final String CLASSPATH_URL_PREFIX = "classpath:";
	public static final String FILE_URL_PREFIX = "file:";
	public static final String JAR_URL_PREFIX = "jar:";
	public static final String WAR_URL_PREFIX = "war:";
	public static final String URL_PROTOCOL_FILE = "file";
	public static final String URL_PROTOCOL_JAR = "jar";
	public static final String URL_PROTOCOL_WAR = "war";
	public static final String URL_PROTOCOL_ZIP = "zip";
	public static final String URL_PROTOCOL_WSJAR = "wsjar";
	public static final String URL_PROTOCOL_VFSZIP = "vfszip";
	public static final String URL_PROTOCOL_VFSFILE = "vfsfile";
	public static final String URL_PROTOCOL_VFS = "vfs";
	public static final String JAR_FILE_EXTENSION = ".jar";
	public static final String JAR_URL_SEPARATOR = "!/";
	public static final String WAR_URL_SEPARATOR = "*/";

	public ResourceUtils()
	{
	}

	public static boolean isUrl(String resourceLocation)
	{
		if (resourceLocation == null)
			return false;
		if (resourceLocation.startsWith("classpath:"))
			return true;
		new URL(resourceLocation);
		return true;
		MalformedURLException ex;
		ex;
		return false;
	}

	public static URL getURL(String resourceLocation)
		throws FileNotFoundException
	{
		Assert.notNull(resourceLocation, "Resource location must not be null");
		if (resourceLocation.startsWith("classpath:"))
		{
			String path = resourceLocation.substring("classpath:".length());
			ClassLoader cl = ClassUtils.getDefaultClassLoader();
			URL url = cl == null ? ClassLoader.getSystemResource(path) : cl.getResource(path);
			if (url == null)
			{
				String description = (new StringBuilder()).append("class path resource [").append(path).append("]").toString();
				throw new FileNotFoundException((new StringBuilder()).append(description).append(" cannot be resolved to URL because it does not exist").toString());
			} else
			{
				return url;
			}
		}
		return new URL(resourceLocation);
		MalformedURLException ex;
		ex;
		return (new File(resourceLocation)).toURI().toURL();
		MalformedURLException ex2;
		ex2;
		throw new FileNotFoundException((new StringBuilder()).append("Resource location [").append(resourceLocation).append("] is neither a URL not a well-formed file path").toString());
	}

	public static File getFile(String resourceLocation)
		throws FileNotFoundException
	{
		Assert.notNull(resourceLocation, "Resource location must not be null");
		if (resourceLocation.startsWith("classpath:"))
		{
			String path = resourceLocation.substring("classpath:".length());
			String description = (new StringBuilder()).append("class path resource [").append(path).append("]").toString();
			ClassLoader cl = ClassUtils.getDefaultClassLoader();
			URL url = cl == null ? ClassLoader.getSystemResource(path) : cl.getResource(path);
			if (url == null)
				throw new FileNotFoundException((new StringBuilder()).append(description).append(" cannot be resolved to absolute file path because it does not exist").toString());
			else
				return getFile(url, description);
		}
		return getFile(new URL(resourceLocation));
		MalformedURLException ex;
		ex;
		return new File(resourceLocation);
	}

	public static File getFile(URL resourceUrl)
		throws FileNotFoundException
	{
		return getFile(resourceUrl, "URL");
	}

	public static File getFile(URL resourceUrl, String description)
		throws FileNotFoundException
	{
		Assert.notNull(resourceUrl, "Resource URL must not be null");
		if (!"file".equals(resourceUrl.getProtocol()))
			throw new FileNotFoundException((new StringBuilder()).append(description).append(" cannot be resolved to absolute file path because it does not reside in the file system: ").append(resourceUrl).toString());
		return new File(toURI(resourceUrl).getSchemeSpecificPart());
		URISyntaxException ex;
		ex;
		return new File(resourceUrl.getFile());
	}

	public static File getFile(URI resourceUri)
		throws FileNotFoundException
	{
		return getFile(resourceUri, "URI");
	}

	public static File getFile(URI resourceUri, String description)
		throws FileNotFoundException
	{
		Assert.notNull(resourceUri, "Resource URI must not be null");
		if (!"file".equals(resourceUri.getScheme()))
			throw new FileNotFoundException((new StringBuilder()).append(description).append(" cannot be resolved to absolute file path because it does not reside in the file system: ").append(resourceUri).toString());
		else
			return new File(resourceUri.getSchemeSpecificPart());
	}

	public static boolean isFileURL(URL url)
	{
		String protocol = url.getProtocol();
		return "file".equals(protocol) || "vfsfile".equals(protocol) || "vfs".equals(protocol);
	}

	public static boolean isJarURL(URL url)
	{
		String protocol = url.getProtocol();
		return "jar".equals(protocol) || "war".equals(protocol) || "zip".equals(protocol) || "vfszip".equals(protocol) || "wsjar".equals(protocol);
	}

	public static boolean isJarFileURL(URL url)
	{
		return "file".equals(url.getProtocol()) && url.getPath().toLowerCase().endsWith(".jar");
	}

	public static URL extractJarFileURL(URL jarUrl)
		throws MalformedURLException
	{
		String jarFile;
		String urlFile = jarUrl.getFile();
		int separatorIndex = urlFile.indexOf("!/");
		if (separatorIndex == -1)
			break MISSING_BLOCK_LABEL_91;
		jarFile = urlFile.substring(0, separatorIndex);
		return new URL(jarFile);
		MalformedURLException ex;
		ex;
		if (!jarFile.startsWith("/"))
			jarFile = (new StringBuilder()).append("/").append(jarFile).toString();
		return new URL((new StringBuilder()).append("file:").append(jarFile).toString());
		return jarUrl;
	}

	public static URL extractArchiveURL(URL jarUrl)
		throws MalformedURLException
	{
		String urlFile = jarUrl.getFile();
		int endIndex = urlFile.indexOf("*/");
		if (endIndex != -1)
		{
			String warFile = urlFile.substring(0, endIndex);
			int startIndex = warFile.indexOf("war:");
			if (startIndex != -1)
				return new URL(warFile.substring(startIndex + "war:".length()));
		}
		return extractJarFileURL(jarUrl);
	}

	public static URI toURI(URL url)
		throws URISyntaxException
	{
		return toURI(url.toString());
	}

	public static URI toURI(String location)
		throws URISyntaxException
	{
		return new URI(StringUtils.replace(location, " ", "%20"));
	}

	public static void useCachesIfNecessary(URLConnection con)
	{
		con.setUseCaches(con.getClass().getSimpleName().startsWith("JNLP"));
	}
}
