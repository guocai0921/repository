// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   LocalVariableTableParameterNameDiscoverer.java

package org.springframework.core;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.*;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.asm.*;
import org.springframework.util.ClassUtils;

// Referenced classes of package org.springframework.core:
//			ParameterNameDiscoverer, BridgeMethodResolver

public class LocalVariableTableParameterNameDiscoverer
	implements ParameterNameDiscoverer
{
	private static class LocalVariableTableVisitor extends MethodVisitor
	{

		private static final String CONSTRUCTOR = "<init>";
		private final Class clazz;
		private final Map memberMap;
		private final String name;
		private final Type args[];
		private final String parameterNames[];
		private final boolean isStatic;
		private boolean hasLvtInfo;
		private final int lvtSlotIndex[];

		public void visitLocalVariable(String name, String description, String signature, Label start, Label end, int index)
		{
			hasLvtInfo = true;
			for (int i = 0; i < lvtSlotIndex.length; i++)
				if (lvtSlotIndex[i] == index)
					parameterNames[i] = name;

		}

		public void visitEnd()
		{
			if (hasLvtInfo || isStatic && parameterNames.length == 0)
				memberMap.put(resolveMember(), parameterNames);
		}

		private Member resolveMember()
		{
			Class argTypes[];
			ClassLoader loader = clazz.getClassLoader();
			argTypes = new Class[args.length];
			for (int i = 0; i < args.length; i++)
				argTypes[i] = ClassUtils.resolveClassName(args[i].getClassName(), loader);

			if ("<init>".equals(name))
				return clazz.getDeclaredConstructor(argTypes);
			return clazz.getDeclaredMethod(name, argTypes);
			NoSuchMethodException ex;
			ex;
			throw new IllegalStateException((new StringBuilder()).append("Method [").append(name).append("] was discovered in the .class file but cannot be resolved in the class object").toString(), ex);
		}

		private static int[] computeLvtSlotIndices(boolean isStatic, Type paramTypes[])
		{
			int lvtIndex[] = new int[paramTypes.length];
			int nextIndex = isStatic ? 0 : 1;
			for (int i = 0; i < paramTypes.length; i++)
			{
				lvtIndex[i] = nextIndex;
				if (isWideType(paramTypes[i]))
					nextIndex += 2;
				else
					nextIndex++;
			}

			return lvtIndex;
		}

		private static boolean isWideType(Type aType)
		{
			return aType == Type.LONG_TYPE || aType == Type.DOUBLE_TYPE;
		}

		public LocalVariableTableVisitor(Class clazz, Map map, String name, String desc, boolean isStatic)
		{
			super(0x50000);
			hasLvtInfo = false;
			this.clazz = clazz;
			memberMap = map;
			this.name = name;
			args = Type.getArgumentTypes(desc);
			parameterNames = new String[args.length];
			this.isStatic = isStatic;
			lvtSlotIndex = computeLvtSlotIndices(isStatic, args);
		}
	}

	private static class ParameterNameDiscoveringVisitor extends ClassVisitor
	{

		private static final String STATIC_CLASS_INIT = "<clinit>";
		private final Class clazz;
		private final Map memberMap;

		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String exceptions[])
		{
			if (!isSyntheticOrBridged(access) && !"<clinit>".equals(name))
				return new LocalVariableTableVisitor(clazz, memberMap, name, desc, isStatic(access));
			else
				return null;
		}

		private static boolean isSyntheticOrBridged(int access)
		{
			return (access & 0x1000 | access & 0x40) > 0;
		}

		private static boolean isStatic(int access)
		{
			return (access & 8) > 0;
		}

		public ParameterNameDiscoveringVisitor(Class clazz, Map memberMap)
		{
			super(0x50000);
			this.clazz = clazz;
			this.memberMap = memberMap;
		}
	}


	private static final Log logger = LogFactory.getLog(org/springframework/core/LocalVariableTableParameterNameDiscoverer);
	private static final Map NO_DEBUG_INFO_MAP = Collections.emptyMap();
	private final Map parameterNamesCache = new ConcurrentHashMap(32);

	public LocalVariableTableParameterNameDiscoverer()
	{
	}

	public String[] getParameterNames(Method method)
	{
		Method originalMethod = BridgeMethodResolver.findBridgedMethod(method);
		Class declaringClass = originalMethod.getDeclaringClass();
		Map map = (Map)parameterNamesCache.get(declaringClass);
		if (map == null)
		{
			map = inspectClass(declaringClass);
			parameterNamesCache.put(declaringClass, map);
		}
		if (map != NO_DEBUG_INFO_MAP)
			return (String[])map.get(originalMethod);
		else
			return null;
	}

	public String[] getParameterNames(Constructor ctor)
	{
		Class declaringClass = ctor.getDeclaringClass();
		Map map = (Map)parameterNamesCache.get(declaringClass);
		if (map == null)
		{
			map = inspectClass(declaringClass);
			parameterNamesCache.put(declaringClass, map);
		}
		if (map != NO_DEBUG_INFO_MAP)
			return (String[])map.get(ctor);
		else
			return null;
	}

	private Map inspectClass(Class clazz)
	{
		InputStream is;
		is = clazz.getResourceAsStream(ClassUtils.getClassFileName(clazz));
		if (is == null)
		{
			if (logger.isDebugEnabled())
				logger.debug((new StringBuilder()).append("Cannot find '.class' file for class [").append(clazz).append("] - unable to determine constructor/method parameter names").toString());
			return NO_DEBUG_INFO_MAP;
		}
		Map map1;
		ClassReader classReader = new ClassReader(is);
		Map map = new ConcurrentHashMap(32);
		classReader.accept(new ParameterNameDiscoveringVisitor(clazz, map), 0);
		map1 = map;
		try
		{
			is.close();
		}
		catch (IOException ioexception1) { }
		return map1;
		IOException ex;
		ex;
		if (logger.isDebugEnabled())
			logger.debug((new StringBuilder()).append("Exception thrown while reading '.class' file for class [").append(clazz).append("] - unable to determine constructor/method parameter names").toString(), ex);
		try
		{
			is.close();
		}
		// Misplaced declaration of an exception variable
		catch (IOException ex) { }
		break MISSING_BLOCK_LABEL_237;
		ex;
		if (logger.isDebugEnabled())
			logger.debug((new StringBuilder()).append("ASM ClassReader failed to parse class file [").append(clazz).append("], probably due to a new Java class file version that isn't supported yet - unable to determine constructor/method parameter names").toString(), ex);
		try
		{
			is.close();
		}
		catch (IOException ioexception) { }
		break MISSING_BLOCK_LABEL_237;
		Exception exception;
		exception;
		try
		{
			is.close();
		}
		catch (IOException ioexception2) { }
		throw exception;
		return NO_DEBUG_INFO_MAP;
	}

}
