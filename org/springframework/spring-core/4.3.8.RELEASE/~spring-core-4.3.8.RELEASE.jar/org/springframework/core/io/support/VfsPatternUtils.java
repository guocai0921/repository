// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   VfsPatternUtils.java

package org.springframework.core.io.support;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.net.URL;
import org.springframework.core.io.VfsUtils;

abstract class VfsPatternUtils extends VfsUtils
{

	VfsPatternUtils()
	{
	}

	static Object getVisitorAttribute()
	{
		return doGetVisitorAttribute();
	}

	static String getPath(Object resource)
	{
		return doGetPath(resource);
	}

	static Object findRoot(URL url)
		throws IOException
	{
		return getRoot(url);
	}

	static void visit(Object resource, InvocationHandler visitor)
		throws IOException
	{
		Object visitorProxy = Proxy.newProxyInstance(VIRTUAL_FILE_VISITOR_INTERFACE.getClassLoader(), new Class[] {
			VIRTUAL_FILE_VISITOR_INTERFACE
		}, visitor);
		invokeVfsMethod(VIRTUAL_FILE_METHOD_VISIT, resource, new Object[] {
			visitorProxy
		});
	}
}
