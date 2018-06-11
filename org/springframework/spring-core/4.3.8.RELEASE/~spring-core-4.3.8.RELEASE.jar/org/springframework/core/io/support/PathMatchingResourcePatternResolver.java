// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   PathMatchingResourcePatternResolver.java

package org.springframework.core.io.support;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.*;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.*;
import org.springframework.util.*;

// Referenced classes of package org.springframework.core.io.support:
//			ResourcePatternResolver, VfsPatternUtils

public class PathMatchingResourcePatternResolver
	implements ResourcePatternResolver
{
	private static class PatternVirtualFileVisitor
		implements InvocationHandler
	{

		private final String subPattern;
		private final PathMatcher pathMatcher;
		private final String rootPath;
		private final Set resources = new LinkedHashSet();

		public Object invoke(Object proxy, Method method, Object args[])
			throws Throwable
		{
			String methodName = method.getName();
			if (java/lang/Object == method.getDeclaringClass())
			{
				if (methodName.equals("equals"))
					return Boolean.valueOf(proxy == args[0]);
				if (methodName.equals("hashCode"))
					return Integer.valueOf(System.identityHashCode(proxy));
			} else
			{
				if ("getAttributes".equals(methodName))
					return getAttributes();
				if ("visit".equals(methodName))
				{
					visit(args[0]);
					return null;
				}
				if ("toString".equals(methodName))
					return toString();
			}
			throw new IllegalStateException((new StringBuilder()).append("Unexpected method invocation: ").append(method).toString());
		}

		public void visit(Object vfsResource)
		{
			if (pathMatcher.match(subPattern, VfsPatternUtils.getPath(vfsResource).substring(rootPath.length())))
				resources.add(new VfsResource(vfsResource));
		}

		public Object getAttributes()
		{
			return VfsPatternUtils.getVisitorAttribute();
		}

		public Set getResources()
		{
			return resources;
		}

		public int size()
		{
			return resources.size();
		}

		public String toString()
		{
			return (new StringBuilder()).append("sub-pattern: ").append(subPattern).append(", resources: ").append(resources).toString();
		}

		public PatternVirtualFileVisitor(String rootPath, String subPattern, PathMatcher pathMatcher)
		{
			this.subPattern = subPattern;
			this.pathMatcher = pathMatcher;
			this.rootPath = !rootPath.isEmpty() && !rootPath.endsWith("/") ? (new StringBuilder()).append(rootPath).append("/").toString() : rootPath;
		}
	}

	private static class VfsResourceMatchingDelegate
	{

		public static Set findMatchingResources(URL rootDirURL, String locationPattern, PathMatcher pathMatcher)
			throws IOException
		{
			Object root = VfsPatternUtils.findRoot(rootDirURL);
			PatternVirtualFileVisitor visitor = new PatternVirtualFileVisitor(VfsPatternUtils.getPath(root), locationPattern, pathMatcher);
			VfsPatternUtils.visit(root, visitor);
			return visitor.getResources();
		}

		private VfsResourceMatchingDelegate()
		{
		}
	}


	private static final Log logger;
	private static Method equinoxResolveMethod;
	private final ResourceLoader resourceLoader;
	private PathMatcher pathMatcher;

	public PathMatchingResourcePatternResolver()
	{
		pathMatcher = new AntPathMatcher();
		resourceLoader = new DefaultResourceLoader();
	}

	public PathMatchingResourcePatternResolver(ResourceLoader resourceLoader)
	{
		pathMatcher = new AntPathMatcher();
		Assert.notNull(resourceLoader, "ResourceLoader must not be null");
		this.resourceLoader = resourceLoader;
	}

	public PathMatchingResourcePatternResolver(ClassLoader classLoader)
	{
		pathMatcher = new AntPathMatcher();
		resourceLoader = new DefaultResourceLoader(classLoader);
	}

	public ResourceLoader getResourceLoader()
	{
		return resourceLoader;
	}

	public ClassLoader getClassLoader()
	{
		return getResourceLoader().getClassLoader();
	}

	public void setPathMatcher(PathMatcher pathMatcher)
	{
		Assert.notNull(pathMatcher, "PathMatcher must not be null");
		this.pathMatcher = pathMatcher;
	}

	public PathMatcher getPathMatcher()
	{
		return pathMatcher;
	}

	public Resource getResource(String location)
	{
		return getResourceLoader().getResource(location);
	}

	public Resource[] getResources(String locationPattern)
		throws IOException
	{
		Assert.notNull(locationPattern, "Location pattern must not be null");
		if (locationPattern.startsWith("classpath*:"))
			if (getPathMatcher().isPattern(locationPattern.substring("classpath*:".length())))
				return findPathMatchingResources(locationPattern);
			else
				return findAllClassPathResources(locationPattern.substring("classpath*:".length()));
		int prefixEnd = locationPattern.startsWith("war:") ? locationPattern.indexOf("*/") + 1 : locationPattern.indexOf(":") + 1;
		if (getPathMatcher().isPattern(locationPattern.substring(prefixEnd)))
			return findPathMatchingResources(locationPattern);
		else
			return (new Resource[] {
				getResourceLoader().getResource(locationPattern)
			});
	}

	protected Resource[] findAllClassPathResources(String location)
		throws IOException
	{
		String path = location;
		if (path.startsWith("/"))
			path = path.substring(1);
		Set result = doFindAllClassPathResources(path);
		if (logger.isDebugEnabled())
			logger.debug((new StringBuilder()).append("Resolved classpath location [").append(location).append("] to resources ").append(result).toString());
		return (Resource[])result.toArray(new Resource[result.size()]);
	}

	protected Set doFindAllClassPathResources(String path)
		throws IOException
	{
		Set result = new LinkedHashSet(16);
		ClassLoader cl = getClassLoader();
		URL url;
		for (Enumeration resourceUrls = cl == null ? ClassLoader.getSystemResources(path) : cl.getResources(path); resourceUrls.hasMoreElements(); result.add(convertClassLoaderURL(url)))
			url = (URL)resourceUrls.nextElement();

		if ("".equals(path))
			addAllClassLoaderJarRoots(cl, result);
		return result;
	}

	protected Resource convertClassLoaderURL(URL url)
	{
		return new UrlResource(url);
	}

	protected void addAllClassLoaderJarRoots(ClassLoader classLoader, Set result)
	{
		if (classLoader instanceof URLClassLoader)
			try
			{
				URL aurl[] = ((URLClassLoader)classLoader).getURLs();
				int i = aurl.length;
				for (int j = 0; j < i; j++)
				{
					URL url = aurl[j];
					try
					{
						UrlResource jarResource = new UrlResource((new StringBuilder()).append("jar:").append(url.toString()).append("!/").toString());
						if (jarResource.exists())
							result.add(jarResource);
					}
					catch (MalformedURLException ex)
					{
						if (logger.isDebugEnabled())
							logger.debug((new StringBuilder()).append("Cannot search for matching files underneath [").append(url).append("] because it cannot be converted to a valid 'jar:' URL: ").append(ex.getMessage()).toString());
					}
				}

			}
			catch (Exception ex)
			{
				if (logger.isDebugEnabled())
					logger.debug((new StringBuilder()).append("Cannot introspect jar files since ClassLoader [").append(classLoader).append("] does not support 'getURLs()': ").append(ex).toString());
			}
		if (classLoader == ClassLoader.getSystemClassLoader())
			addClassPathManifestEntries(result);
		if (classLoader != null)
			try
			{
				addAllClassLoaderJarRoots(classLoader.getParent(), result);
			}
			catch (Exception ex)
			{
				if (logger.isDebugEnabled())
					logger.debug((new StringBuilder()).append("Cannot introspect jar files in parent ClassLoader since [").append(classLoader).append("] does not support 'getParent()': ").append(ex).toString());
			}
	}

	protected void addClassPathManifestEntries(Set result)
	{
		try
		{
			String javaClassPathProperty = System.getProperty("java.class.path");
			String as[] = StringUtils.delimitedListToStringArray(javaClassPathProperty, System.getProperty("path.separator"));
			int i = as.length;
			for (int j = 0; j < i; j++)
			{
				String path = as[j];
				try
				{
					File file = new File(path);
					UrlResource jarResource = new UrlResource((new StringBuilder()).append("jar:file:").append(file.getAbsolutePath()).append("!/").toString());
					if (jarResource.exists())
						result.add(jarResource);
				}
				catch (MalformedURLException ex)
				{
					if (logger.isDebugEnabled())
						logger.debug((new StringBuilder()).append("Cannot search for matching files underneath [").append(path).append("] because it cannot be converted to a valid 'jar:' URL: ").append(ex.getMessage()).toString());
				}
			}

		}
		catch (Exception ex)
		{
			if (logger.isDebugEnabled())
				logger.debug((new StringBuilder()).append("Failed to evaluate 'java.class.path' manifest entries: ").append(ex).toString());
		}
	}

	protected Resource[] findPathMatchingResources(String locationPattern)
		throws IOException
	{
		String rootDirPath = determineRootDir(locationPattern);
		String subPattern = locationPattern.substring(rootDirPath.length());
		Resource rootDirResources[] = getResources(rootDirPath);
		Set result = new LinkedHashSet(16);
		Resource aresource[] = rootDirResources;
		int i = aresource.length;
		for (int j = 0; j < i; j++)
		{
			Resource rootDirResource = aresource[j];
			rootDirResource = resolveRootDirResource(rootDirResource);
			URL rootDirURL = rootDirResource.getURL();
			if (equinoxResolveMethod != null && rootDirURL.getProtocol().startsWith("bundle"))
			{
				rootDirURL = (URL)ReflectionUtils.invokeMethod(equinoxResolveMethod, null, new Object[] {
					rootDirURL
				});
				rootDirResource = new UrlResource(rootDirURL);
			}
			if (rootDirURL.getProtocol().startsWith("vfs"))
			{
				result.addAll(VfsResourceMatchingDelegate.findMatchingResources(rootDirURL, subPattern, getPathMatcher()));
				continue;
			}
			if (ResourceUtils.isJarURL(rootDirURL) || isJarResource(rootDirResource))
				result.addAll(doFindPathMatchingJarResources(rootDirResource, rootDirURL, subPattern));
			else
				result.addAll(doFindPathMatchingFileResources(rootDirResource, subPattern));
		}

		if (logger.isDebugEnabled())
			logger.debug((new StringBuilder()).append("Resolved location pattern [").append(locationPattern).append("] to resources ").append(result).toString());
		return (Resource[])result.toArray(new Resource[result.size()]);
	}

	protected String determineRootDir(String location)
	{
		int prefixEnd = location.indexOf(":") + 1;
		int rootDirEnd;
		for (rootDirEnd = location.length(); rootDirEnd > prefixEnd && getPathMatcher().isPattern(location.substring(prefixEnd, rootDirEnd)); rootDirEnd = location.lastIndexOf('/', rootDirEnd - 2) + 1);
		if (rootDirEnd == 0)
			rootDirEnd = prefixEnd;
		return location.substring(0, rootDirEnd);
	}

	protected Resource resolveRootDirResource(Resource original)
		throws IOException
	{
		return original;
	}

	protected boolean isJarResource(Resource resource)
		throws IOException
	{
		return false;
	}

	protected Set doFindPathMatchingJarResources(Resource rootDirResource, URL rootDirURL, String subPattern)
		throws IOException
	{
		JarFile jarFile;
		String jarFileUrl;
		String rootEntryPath;
		boolean closeJarFile;
		Set result = doFindPathMatchingJarResources(rootDirResource, subPattern);
		if (result != null)
			return result;
		URLConnection con = rootDirURL.openConnection();
		if (con instanceof JarURLConnection)
		{
			JarURLConnection jarCon = (JarURLConnection)con;
			ResourceUtils.useCachesIfNecessary(jarCon);
			jarFile = jarCon.getJarFile();
			jarFileUrl = jarCon.getJarFileURL().toExternalForm();
			JarEntry jarEntry = jarCon.getJarEntry();
			rootEntryPath = jarEntry == null ? "" : jarEntry.getName();
			closeJarFile = !jarCon.getUseCaches();
		} else
		{
			String urlFile = rootDirURL.getFile();
			try
			{
				int separatorIndex = urlFile.indexOf("!/");
				if (separatorIndex != -1)
				{
					jarFileUrl = urlFile.substring(0, separatorIndex);
					rootEntryPath = urlFile.substring(separatorIndex + "!/".length());
					jarFile = getJarFile(jarFileUrl);
				} else
				{
					jarFile = new JarFile(urlFile);
					jarFileUrl = urlFile;
					rootEntryPath = "";
				}
				closeJarFile = true;
			}
			catch (ZipException ex)
			{
				if (logger.isDebugEnabled())
					logger.debug((new StringBuilder()).append("Skipping invalid jar classpath entry [").append(urlFile).append("]").toString());
				return Collections.emptySet();
			}
		}
		Enumeration entries;
		if (logger.isDebugEnabled())
			logger.debug((new StringBuilder()).append("Looking for matching resources in jar file [").append(jarFileUrl).append("]").toString());
		if (!"".equals(rootEntryPath) && !rootEntryPath.endsWith("/"))
			rootEntryPath = (new StringBuilder()).append(rootEntryPath).append("/").toString();
		Set result = new LinkedHashSet(8);
		entries = jarFile.entries();
		do
		{
			if (!entries.hasMoreElements())
				break;
			JarEntry entry = (JarEntry)entries.nextElement();
			String entryPath = entry.getName();
			if (entryPath.startsWith(rootEntryPath))
			{
				String relativePath = entryPath.substring(rootEntryPath.length());
				if (getPathMatcher().match(subPattern, relativePath))
					result.add(rootDirResource.createRelative(relativePath));
			}
		} while (true);
		entries = result;
		if (closeJarFile)
			jarFile.close();
		return entries;
		Exception exception;
		exception;
		if (closeJarFile)
			jarFile.close();
		throw exception;
	}

	/**
	 * @deprecated Method doFindPathMatchingJarResources is deprecated
	 */

	protected Set doFindPathMatchingJarResources(Resource rootDirResource, String subPattern)
		throws IOException
	{
		return null;
	}

	protected JarFile getJarFile(String jarFileUrl)
		throws IOException
	{
		if (!jarFileUrl.startsWith("file:"))
			break MISSING_BLOCK_LABEL_42;
		return new JarFile(ResourceUtils.toURI(jarFileUrl).getSchemeSpecificPart());
		URISyntaxException ex;
		ex;
		return new JarFile(jarFileUrl.substring("file:".length()));
		return new JarFile(jarFileUrl);
	}

	protected Set doFindPathMatchingFileResources(Resource rootDirResource, String subPattern)
		throws IOException
	{
		File rootDir;
		try
		{
			rootDir = rootDirResource.getFile().getAbsoluteFile();
		}
		catch (IOException ex)
		{
			if (logger.isWarnEnabled())
				logger.warn((new StringBuilder()).append("Cannot search for matching files underneath ").append(rootDirResource).append(" because it does not correspond to a directory in the file system").toString(), ex);
			return Collections.emptySet();
		}
		return doFindMatchingFileSystemResources(rootDir, subPattern);
	}

	protected Set doFindMatchingFileSystemResources(File rootDir, String subPattern)
		throws IOException
	{
		if (logger.isDebugEnabled())
			logger.debug((new StringBuilder()).append("Looking for matching resources in directory tree [").append(rootDir.getPath()).append("]").toString());
		Set matchingFiles = retrieveMatchingFiles(rootDir, subPattern);
		Set result = new LinkedHashSet(matchingFiles.size());
		File file;
		for (Iterator iterator = matchingFiles.iterator(); iterator.hasNext(); result.add(new FileSystemResource(file)))
			file = (File)iterator.next();

		return result;
	}

	protected Set retrieveMatchingFiles(File rootDir, String pattern)
		throws IOException
	{
		if (!rootDir.exists())
		{
			if (logger.isDebugEnabled())
				logger.debug((new StringBuilder()).append("Skipping [").append(rootDir.getAbsolutePath()).append("] because it does not exist").toString());
			return Collections.emptySet();
		}
		if (!rootDir.isDirectory())
		{
			if (logger.isWarnEnabled())
				logger.warn((new StringBuilder()).append("Skipping [").append(rootDir.getAbsolutePath()).append("] because it does not denote a directory").toString());
			return Collections.emptySet();
		}
		if (!rootDir.canRead())
		{
			if (logger.isWarnEnabled())
				logger.warn((new StringBuilder()).append("Cannot search for matching files underneath directory [").append(rootDir.getAbsolutePath()).append("] because the application is not allowed to read the directory").toString());
			return Collections.emptySet();
		}
		String fullPattern = StringUtils.replace(rootDir.getAbsolutePath(), File.separator, "/");
		if (!pattern.startsWith("/"))
			fullPattern = (new StringBuilder()).append(fullPattern).append("/").toString();
		fullPattern = (new StringBuilder()).append(fullPattern).append(StringUtils.replace(pattern, File.separator, "/")).toString();
		Set result = new LinkedHashSet(8);
		doRetrieveMatchingFiles(fullPattern, rootDir, result);
		return result;
	}

	protected void doRetrieveMatchingFiles(String fullPattern, File dir, Set result)
		throws IOException
	{
		if (logger.isDebugEnabled())
			logger.debug((new StringBuilder()).append("Searching directory [").append(dir.getAbsolutePath()).append("] for files matching pattern [").append(fullPattern).append("]").toString());
		File dirContents[] = dir.listFiles();
		if (dirContents == null)
		{
			if (logger.isWarnEnabled())
				logger.warn((new StringBuilder()).append("Could not retrieve contents of directory [").append(dir.getAbsolutePath()).append("]").toString());
			return;
		}
		Arrays.sort(dirContents);
		File afile[] = dirContents;
		int i = afile.length;
		for (int j = 0; j < i; j++)
		{
			File content = afile[j];
			String currPath = StringUtils.replace(content.getAbsolutePath(), File.separator, "/");
			if (content.isDirectory() && getPathMatcher().matchStart(fullPattern, (new StringBuilder()).append(currPath).append("/").toString()))
				if (!content.canRead())
				{
					if (logger.isDebugEnabled())
						logger.debug((new StringBuilder()).append("Skipping subdirectory [").append(dir.getAbsolutePath()).append("] because the application is not allowed to read the directory").toString());
				} else
				{
					doRetrieveMatchingFiles(fullPattern, content, result);
				}
			if (getPathMatcher().match(fullPattern, currPath))
				result.add(content);
		}

	}

	static 
	{
		logger = LogFactory.getLog(org/springframework/core/io/support/PathMatchingResourcePatternResolver);
		try
		{
			Class fileLocatorClass = ClassUtils.forName("org.eclipse.core.runtime.FileLocator", org/springframework/core/io/support/PathMatchingResourcePatternResolver.getClassLoader());
			equinoxResolveMethod = fileLocatorClass.getMethod("resolve", new Class[] {
				java/net/URL
			});
			logger.debug("Found Equinox FileLocator for OSGi bundle URL resolution");
		}
		catch (Throwable ex)
		{
			equinoxResolveMethod = null;
		}
	}
}
