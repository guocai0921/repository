// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   VfsUtils.java

package org.springframework.core.io;

import java.io.*;
import java.lang.reflect.*;
import java.net.URI;
import java.net.URL;
import org.springframework.util.ReflectionUtils;

public abstract class VfsUtils
{

	private static final String VFS3_PKG = "org.jboss.vfs.";
	private static final String VFS_NAME = "VFS";
	private static Method VFS_METHOD_GET_ROOT_URL = null;
	private static Method VFS_METHOD_GET_ROOT_URI = null;
	private static Method VIRTUAL_FILE_METHOD_EXISTS = null;
	private static Method VIRTUAL_FILE_METHOD_GET_INPUT_STREAM;
	private static Method VIRTUAL_FILE_METHOD_GET_SIZE;
	private static Method VIRTUAL_FILE_METHOD_GET_LAST_MODIFIED;
	private static Method VIRTUAL_FILE_METHOD_TO_URL;
	private static Method VIRTUAL_FILE_METHOD_TO_URI;
	private static Method VIRTUAL_FILE_METHOD_GET_NAME;
	private static Method VIRTUAL_FILE_METHOD_GET_PATH_NAME;
	private static Method VIRTUAL_FILE_METHOD_GET_CHILD;
	protected static Class VIRTUAL_FILE_VISITOR_INTERFACE;
	protected static Method VIRTUAL_FILE_METHOD_VISIT;
	private static Field VISITOR_ATTRIBUTES_FIELD_RECURSE = null;
	private static Method GET_PHYSICAL_FILE = null;

	public VfsUtils()
	{
	}

	protected static transient Object invokeVfsMethod(Method method, Object target, Object args[])
		throws IOException
	{
		return method.invoke(target, args);
		InvocationTargetException ex;
		ex;
		Throwable targetEx = ex.getTargetException();
		if (targetEx instanceof IOException)
			throw (IOException)targetEx;
		ReflectionUtils.handleInvocationTargetException(ex);
		break MISSING_BLOCK_LABEL_40;
		ex;
		ReflectionUtils.handleReflectionException(ex);
		throw new IllegalStateException("Invalid code path reached");
	}

	static boolean exists(Object vfsResource)
	{
		return ((Boolean)invokeVfsMethod(VIRTUAL_FILE_METHOD_EXISTS, vfsResource, new Object[0])).booleanValue();
		IOException ex;
		ex;
		return false;
	}

	static boolean isReadable(Object vfsResource)
	{
		return ((Long)invokeVfsMethod(VIRTUAL_FILE_METHOD_GET_SIZE, vfsResource, new Object[0])).longValue() > 0L;
		IOException ex;
		ex;
		return false;
	}

	static long getSize(Object vfsResource)
		throws IOException
	{
		return ((Long)invokeVfsMethod(VIRTUAL_FILE_METHOD_GET_SIZE, vfsResource, new Object[0])).longValue();
	}

	static long getLastModified(Object vfsResource)
		throws IOException
	{
		return ((Long)invokeVfsMethod(VIRTUAL_FILE_METHOD_GET_LAST_MODIFIED, vfsResource, new Object[0])).longValue();
	}

	static InputStream getInputStream(Object vfsResource)
		throws IOException
	{
		return (InputStream)invokeVfsMethod(VIRTUAL_FILE_METHOD_GET_INPUT_STREAM, vfsResource, new Object[0]);
	}

	static URL getURL(Object vfsResource)
		throws IOException
	{
		return (URL)invokeVfsMethod(VIRTUAL_FILE_METHOD_TO_URL, vfsResource, new Object[0]);
	}

	static URI getURI(Object vfsResource)
		throws IOException
	{
		return (URI)invokeVfsMethod(VIRTUAL_FILE_METHOD_TO_URI, vfsResource, new Object[0]);
	}

	static String getName(Object vfsResource)
	{
		return (String)invokeVfsMethod(VIRTUAL_FILE_METHOD_GET_NAME, vfsResource, new Object[0]);
		IOException ex;
		ex;
		throw new IllegalStateException("Cannot get resource name", ex);
	}

	static Object getRelative(URL url)
		throws IOException
	{
		return invokeVfsMethod(VFS_METHOD_GET_ROOT_URL, null, new Object[] {
			url
		});
	}

	static Object getChild(Object vfsResource, String path)
		throws IOException
	{
		return invokeVfsMethod(VIRTUAL_FILE_METHOD_GET_CHILD, vfsResource, new Object[] {
			path
		});
	}

	static File getFile(Object vfsResource)
		throws IOException
	{
		return (File)invokeVfsMethod(GET_PHYSICAL_FILE, vfsResource, new Object[0]);
	}

	static Object getRoot(URI url)
		throws IOException
	{
		return invokeVfsMethod(VFS_METHOD_GET_ROOT_URI, null, new Object[] {
			url
		});
	}

	protected static Object getRoot(URL url)
		throws IOException
	{
		return invokeVfsMethod(VFS_METHOD_GET_ROOT_URL, null, new Object[] {
			url
		});
	}

	protected static Object doGetVisitorAttribute()
	{
		return ReflectionUtils.getField(VISITOR_ATTRIBUTES_FIELD_RECURSE, null);
	}

	protected static String doGetPath(Object resource)
	{
		return (String)ReflectionUtils.invokeMethod(VIRTUAL_FILE_METHOD_GET_PATH_NAME, resource);
	}

	static 
	{
		ClassLoader loader = org/springframework/core/io/VfsUtils.getClassLoader();
		try
		{
			Class vfsClass = loader.loadClass("org.jboss.vfs.VFS");
			VFS_METHOD_GET_ROOT_URL = ReflectionUtils.findMethod(vfsClass, "getChild", new Class[] {
				java/net/URL
			});
			VFS_METHOD_GET_ROOT_URI = ReflectionUtils.findMethod(vfsClass, "getChild", new Class[] {
				java/net/URI
			});
			Class virtualFile = loader.loadClass("org.jboss.vfs.VirtualFile");
			VIRTUAL_FILE_METHOD_EXISTS = ReflectionUtils.findMethod(virtualFile, "exists");
			VIRTUAL_FILE_METHOD_GET_INPUT_STREAM = ReflectionUtils.findMethod(virtualFile, "openStream");
			VIRTUAL_FILE_METHOD_GET_SIZE = ReflectionUtils.findMethod(virtualFile, "getSize");
			VIRTUAL_FILE_METHOD_GET_LAST_MODIFIED = ReflectionUtils.findMethod(virtualFile, "getLastModified");
			VIRTUAL_FILE_METHOD_TO_URI = ReflectionUtils.findMethod(virtualFile, "toURI");
			VIRTUAL_FILE_METHOD_TO_URL = ReflectionUtils.findMethod(virtualFile, "toURL");
			VIRTUAL_FILE_METHOD_GET_NAME = ReflectionUtils.findMethod(virtualFile, "getName");
			VIRTUAL_FILE_METHOD_GET_PATH_NAME = ReflectionUtils.findMethod(virtualFile, "getPathName");
			GET_PHYSICAL_FILE = ReflectionUtils.findMethod(virtualFile, "getPhysicalFile");
			VIRTUAL_FILE_METHOD_GET_CHILD = ReflectionUtils.findMethod(virtualFile, "getChild", new Class[] {
				java/lang/String
			});
			VIRTUAL_FILE_VISITOR_INTERFACE = loader.loadClass("org.jboss.vfs.VirtualFileVisitor");
			VIRTUAL_FILE_METHOD_VISIT = ReflectionUtils.findMethod(virtualFile, "visit", new Class[] {
				VIRTUAL_FILE_VISITOR_INTERFACE
			});
			Class visitorAttributesClass = loader.loadClass("org.jboss.vfs.VisitorAttributes");
			VISITOR_ATTRIBUTES_FIELD_RECURSE = ReflectionUtils.findField(visitorAttributesClass, "RECURSE");
		}
		catch (ClassNotFoundException ex)
		{
			throw new IllegalStateException("Could not detect JBoss VFS infrastructure", ex);
		}
	}
}
