// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ClassUtils.java

package org.springframework.util;

import java.beans.Introspector;
import java.lang.reflect.*;
import java.util.*;

// Referenced classes of package org.springframework.util:
//			Assert, ReflectionUtils, CollectionUtils

public abstract class ClassUtils
{

	public static final String ARRAY_SUFFIX = "[]";
	private static final String INTERNAL_ARRAY_PREFIX = "[";
	private static final String NON_PRIMITIVE_ARRAY_PREFIX = "[L";
	private static final char PACKAGE_SEPARATOR = 46;
	private static final char PATH_SEPARATOR = 47;
	private static final char INNER_CLASS_SEPARATOR = 36;
	public static final String CGLIB_CLASS_SEPARATOR = "$$";
	public static final String CLASS_FILE_SUFFIX = ".class";
	private static final Map primitiveWrapperTypeMap;
	private static final Map primitiveTypeToWrapperMap;
	private static final Map primitiveTypeNameMap;
	private static final Map commonClassCache = new HashMap(32);

	public ClassUtils()
	{
	}

	private static transient void registerCommonClasses(Class commonClasses[])
	{
		Class aclass[] = commonClasses;
		int i = aclass.length;
		for (int j = 0; j < i; j++)
		{
			Class clazz = aclass[j];
			commonClassCache.put(clazz.getName(), clazz);
		}

	}

	public static ClassLoader getDefaultClassLoader()
	{
		ClassLoader cl = null;
		try
		{
			cl = Thread.currentThread().getContextClassLoader();
		}
		catch (Throwable throwable) { }
		if (cl == null)
		{
			cl = org/springframework/util/ClassUtils.getClassLoader();
			if (cl == null)
				try
				{
					cl = ClassLoader.getSystemClassLoader();
				}
				catch (Throwable throwable1) { }
		}
		return cl;
	}

	public static ClassLoader overrideThreadContextClassLoader(ClassLoader classLoaderToUse)
	{
		Thread currentThread = Thread.currentThread();
		ClassLoader threadContextClassLoader = currentThread.getContextClassLoader();
		if (classLoaderToUse != null && !classLoaderToUse.equals(threadContextClassLoader))
		{
			currentThread.setContextClassLoader(classLoaderToUse);
			return threadContextClassLoader;
		} else
		{
			return null;
		}
	}

	public static Class forName(String name, ClassLoader classLoader)
		throws ClassNotFoundException, LinkageError
	{
		ClassLoader clToUse;
		Assert.notNull(name, "Name must not be null");
		Class clazz = resolvePrimitiveClassName(name);
		if (clazz == null)
			clazz = (Class)commonClassCache.get(name);
		if (clazz != null)
			return clazz;
		if (name.endsWith("[]"))
		{
			String elementClassName = name.substring(0, name.length() - "[]".length());
			Class elementClass = forName(elementClassName, classLoader);
			return Array.newInstance(elementClass, 0).getClass();
		}
		if (name.startsWith("[L") && name.endsWith(";"))
		{
			String elementName = name.substring("[L".length(), name.length() - 1);
			Class elementClass = forName(elementName, classLoader);
			return Array.newInstance(elementClass, 0).getClass();
		}
		if (name.startsWith("["))
		{
			String elementName = name.substring("[".length());
			Class elementClass = forName(elementName, classLoader);
			return Array.newInstance(elementClass, 0).getClass();
		}
		clToUse = classLoader;
		if (clToUse == null)
			clToUse = getDefaultClassLoader();
		return clToUse == null ? Class.forName(name) : clToUse.loadClass(name);
		ClassNotFoundException ex;
		ex;
		String innerClassName;
		int lastDotIndex = name.lastIndexOf('.');
		if (lastDotIndex == -1)
			break MISSING_BLOCK_LABEL_265;
		innerClassName = (new StringBuilder()).append(name.substring(0, lastDotIndex)).append('$').append(name.substring(lastDotIndex + 1)).toString();
		return clToUse == null ? Class.forName(innerClassName) : clToUse.loadClass(innerClassName);
		ClassNotFoundException classnotfoundexception;
		classnotfoundexception;
		throw ex;
	}

	public static Class resolveClassName(String className, ClassLoader classLoader)
		throws IllegalArgumentException
	{
		return forName(className, classLoader);
		ClassNotFoundException ex;
		ex;
		throw new IllegalArgumentException((new StringBuilder()).append("Cannot find class [").append(className).append("]").toString(), ex);
		ex;
		throw new IllegalArgumentException((new StringBuilder()).append("Error loading class [").append(className).append("]: problem with class file or dependent class.").toString(), ex);
	}

	public static Class resolvePrimitiveClassName(String name)
	{
		Class result = null;
		if (name != null && name.length() <= 8)
			result = (Class)primitiveTypeNameMap.get(name);
		return result;
	}

	public static boolean isPresent(String className, ClassLoader classLoader)
	{
		forName(className, classLoader);
		return true;
		Throwable ex;
		ex;
		return false;
	}

	public static Class getUserClass(Object instance)
	{
		Assert.notNull(instance, "Instance must not be null");
		return getUserClass(instance.getClass());
	}

	public static Class getUserClass(Class clazz)
	{
		if (clazz != null && clazz.getName().contains("$$"))
		{
			Class superclass = clazz.getSuperclass();
			if (superclass != null && java/lang/Object != superclass)
				return superclass;
		}
		return clazz;
	}

	public static boolean isCacheSafe(Class clazz, ClassLoader classLoader)
	{
		Assert.notNull(clazz, "Class must not be null");
		ClassLoader target = clazz.getClassLoader();
		if (target == null)
			return true;
		ClassLoader cur = classLoader;
		if (cur == target)
			return true;
		do
		{
			if (cur == null)
				break MISSING_BLOCK_LABEL_42;
			cur = cur.getParent();
		} while (cur != target);
		return true;
		return false;
		SecurityException ex;
		ex;
		return true;
	}

	public static String getShortName(String className)
	{
		Assert.hasLength(className, "Class name must not be empty");
		int lastDotIndex = className.lastIndexOf('.');
		int nameEndIndex = className.indexOf("$$");
		if (nameEndIndex == -1)
			nameEndIndex = className.length();
		String shortName = className.substring(lastDotIndex + 1, nameEndIndex);
		shortName = shortName.replace('$', '.');
		return shortName;
	}

	public static String getShortName(Class clazz)
	{
		return getShortName(getQualifiedName(clazz));
	}

	public static String getShortNameAsProperty(Class clazz)
	{
		String shortName = getShortName(clazz);
		int dotIndex = shortName.lastIndexOf('.');
		shortName = dotIndex == -1 ? shortName : shortName.substring(dotIndex + 1);
		return Introspector.decapitalize(shortName);
	}

	public static String getClassFileName(Class clazz)
	{
		Assert.notNull(clazz, "Class must not be null");
		String className = clazz.getName();
		int lastDotIndex = className.lastIndexOf('.');
		return (new StringBuilder()).append(className.substring(lastDotIndex + 1)).append(".class").toString();
	}

	public static String getPackageName(Class clazz)
	{
		Assert.notNull(clazz, "Class must not be null");
		return getPackageName(clazz.getName());
	}

	public static String getPackageName(String fqClassName)
	{
		Assert.notNull(fqClassName, "Class name must not be null");
		int lastDotIndex = fqClassName.lastIndexOf('.');
		return lastDotIndex == -1 ? "" : fqClassName.substring(0, lastDotIndex);
	}

	public static String getQualifiedName(Class clazz)
	{
		Assert.notNull(clazz, "Class must not be null");
		if (clazz.isArray())
			return getQualifiedNameForArray(clazz);
		else
			return clazz.getName();
	}

	private static String getQualifiedNameForArray(Class clazz)
	{
		StringBuilder result = new StringBuilder();
		for (; clazz.isArray(); result.append("[]"))
			clazz = clazz.getComponentType();

		result.insert(0, clazz.getName());
		return result.toString();
	}

	public static String getQualifiedMethodName(Method method)
	{
		return getQualifiedMethodName(method, null);
	}

	public static String getQualifiedMethodName(Method method, Class clazz)
	{
		Assert.notNull(method, "Method must not be null");
		return (new StringBuilder()).append((clazz == null ? method.getDeclaringClass() : clazz).getName()).append('.').append(method.getName()).toString();
	}

	public static String getDescriptiveType(Object value)
	{
		if (value == null)
			return null;
		Class clazz = value.getClass();
		if (Proxy.isProxyClass(clazz))
		{
			StringBuilder result = new StringBuilder(clazz.getName());
			result.append(" implementing ");
			Class ifcs[] = clazz.getInterfaces();
			for (int i = 0; i < ifcs.length; i++)
			{
				result.append(ifcs[i].getName());
				if (i < ifcs.length - 1)
					result.append(',');
			}

			return result.toString();
		}
		if (clazz.isArray())
			return getQualifiedNameForArray(clazz);
		else
			return clazz.getName();
	}

	public static boolean matchesTypeName(Class clazz, String typeName)
	{
		return typeName != null && (typeName.equals(clazz.getName()) || typeName.equals(clazz.getSimpleName()) || clazz.isArray() && typeName.equals(getQualifiedNameForArray(clazz)));
	}

	public static transient boolean hasConstructor(Class clazz, Class paramTypes[])
	{
		return getConstructorIfAvailable(clazz, paramTypes) != null;
	}

	public static transient Constructor getConstructorIfAvailable(Class clazz, Class paramTypes[])
	{
		Assert.notNull(clazz, "Class must not be null");
		return clazz.getConstructor(paramTypes);
		NoSuchMethodException ex;
		ex;
		return null;
	}

	public static transient boolean hasMethod(Class clazz, String methodName, Class paramTypes[])
	{
		return getMethodIfAvailable(clazz, methodName, paramTypes) != null;
	}

	public static transient Method getMethod(Class clazz, String methodName, Class paramTypes[])
	{
		Assert.notNull(clazz, "Class must not be null");
		Assert.notNull(methodName, "Method name must not be null");
		if (paramTypes == null)
			break MISSING_BLOCK_LABEL_51;
		return clazz.getMethod(methodName, paramTypes);
		NoSuchMethodException ex;
		ex;
		throw new IllegalStateException((new StringBuilder()).append("Expected method not found: ").append(ex).toString());
		Set candidates = new HashSet(1);
		Method methods[] = clazz.getMethods();
		Method amethod[] = methods;
		int i = amethod.length;
		for (int j = 0; j < i; j++)
		{
			Method method = amethod[j];
			if (methodName.equals(method.getName()))
				candidates.add(method);
		}

		if (candidates.size() == 1)
			return (Method)candidates.iterator().next();
		if (candidates.isEmpty())
			throw new IllegalStateException((new StringBuilder()).append("Expected method not found: ").append(clazz.getName()).append('.').append(methodName).toString());
		else
			throw new IllegalStateException((new StringBuilder()).append("No unique method found: ").append(clazz.getName()).append('.').append(methodName).toString());
	}

	public static transient Method getMethodIfAvailable(Class clazz, String methodName, Class paramTypes[])
	{
		Assert.notNull(clazz, "Class must not be null");
		Assert.notNull(methodName, "Method name must not be null");
		if (paramTypes == null)
			break MISSING_BLOCK_LABEL_26;
		return clazz.getMethod(methodName, paramTypes);
		NoSuchMethodException ex;
		ex;
		return null;
		Set candidates = new HashSet(1);
		Method methods[] = clazz.getMethods();
		Method amethod[] = methods;
		int i = amethod.length;
		for (int j = 0; j < i; j++)
		{
			Method method = amethod[j];
			if (methodName.equals(method.getName()))
				candidates.add(method);
		}

		if (candidates.size() == 1)
			return (Method)candidates.iterator().next();
		else
			return null;
	}

	public static int getMethodCountForName(Class clazz, String methodName)
	{
		Assert.notNull(clazz, "Class must not be null");
		Assert.notNull(methodName, "Method name must not be null");
		int count = 0;
		Method declaredMethods[] = clazz.getDeclaredMethods();
		Class ifcs[] = declaredMethods;
		int i = ifcs.length;
		for (int j = 0; j < i; j++)
		{
			Method method = ifcs[j];
			if (methodName.equals(method.getName()))
				count++;
		}

		ifcs = clazz.getInterfaces();
		Class aclass[] = ifcs;
		int k = aclass.length;
		for (int l = 0; l < k; l++)
		{
			Class ifc = aclass[l];
			count += getMethodCountForName(ifc, methodName);
		}

		if (clazz.getSuperclass() != null)
			count += getMethodCountForName(clazz.getSuperclass(), methodName);
		return count;
	}

	public static boolean hasAtLeastOneMethodWithName(Class clazz, String methodName)
	{
		Assert.notNull(clazz, "Class must not be null");
		Assert.notNull(methodName, "Method name must not be null");
		Method declaredMethods[] = clazz.getDeclaredMethods();
		Class ifcs[] = declaredMethods;
		int i = ifcs.length;
		for (int j = 0; j < i; j++)
		{
			Method method = ifcs[j];
			if (method.getName().equals(methodName))
				return true;
		}

		ifcs = clazz.getInterfaces();
		Class aclass[] = ifcs;
		int k = aclass.length;
		for (int l = 0; l < k; l++)
		{
			Class ifc = aclass[l];
			if (hasAtLeastOneMethodWithName(ifc, methodName))
				return true;
		}

		return clazz.getSuperclass() != null && hasAtLeastOneMethodWithName(clazz.getSuperclass(), methodName);
	}

	public static Method getMostSpecificMethod(Method method, Class targetClass)
	{
		if (method == null || !isOverridable(method, targetClass) || targetClass == null || targetClass == method.getDeclaringClass())
			break MISSING_BLOCK_LABEL_74;
		if (!Modifier.isPublic(method.getModifiers()))
			break MISSING_BLOCK_LABEL_50;
		return targetClass.getMethod(method.getName(), method.getParameterTypes());
		NoSuchMethodException ex;
		ex;
		return method;
		Method specificMethod = ReflectionUtils.findMethod(targetClass, method.getName(), method.getParameterTypes());
		return specificMethod == null ? method : specificMethod;
		specificMethod;
		return method;
	}

	public static boolean isUserLevelMethod(Method method)
	{
		Assert.notNull(method, "Method must not be null");
		return method.isBridge() || !method.isSynthetic() && !isGroovyObjectMethod(method);
	}

	private static boolean isGroovyObjectMethod(Method method)
	{
		return method.getDeclaringClass().getName().equals("groovy.lang.GroovyObject");
	}

	private static boolean isOverridable(Method method, Class targetClass)
	{
		if (Modifier.isPrivate(method.getModifiers()))
			return false;
		if (Modifier.isPublic(method.getModifiers()) || Modifier.isProtected(method.getModifiers()))
			return true;
		else
			return getPackageName(method.getDeclaringClass()).equals(getPackageName(targetClass));
	}

	public static transient Method getStaticMethod(Class clazz, String methodName, Class args[])
	{
		Assert.notNull(clazz, "Class must not be null");
		Assert.notNull(methodName, "Method name must not be null");
		Method method = clazz.getMethod(methodName, args);
		return Modifier.isStatic(method.getModifiers()) ? method : null;
		NoSuchMethodException ex;
		ex;
		return null;
	}

	public static boolean isPrimitiveWrapper(Class clazz)
	{
		Assert.notNull(clazz, "Class must not be null");
		return primitiveWrapperTypeMap.containsKey(clazz);
	}

	public static boolean isPrimitiveOrWrapper(Class clazz)
	{
		Assert.notNull(clazz, "Class must not be null");
		return clazz.isPrimitive() || isPrimitiveWrapper(clazz);
	}

	public static boolean isPrimitiveArray(Class clazz)
	{
		Assert.notNull(clazz, "Class must not be null");
		return clazz.isArray() && clazz.getComponentType().isPrimitive();
	}

	public static boolean isPrimitiveWrapperArray(Class clazz)
	{
		Assert.notNull(clazz, "Class must not be null");
		return clazz.isArray() && isPrimitiveWrapper(clazz.getComponentType());
	}

	public static Class resolvePrimitiveIfNecessary(Class clazz)
	{
		Assert.notNull(clazz, "Class must not be null");
		return !clazz.isPrimitive() || clazz == Void.TYPE ? clazz : (Class)primitiveTypeToWrapperMap.get(clazz);
	}

	public static boolean isAssignable(Class lhsType, Class rhsType)
	{
		Assert.notNull(lhsType, "Left-hand side type must not be null");
		Assert.notNull(rhsType, "Right-hand side type must not be null");
		if (lhsType.isAssignableFrom(rhsType))
			return true;
		if (lhsType.isPrimitive())
		{
			Class resolvedPrimitive = (Class)primitiveWrapperTypeMap.get(rhsType);
			if (lhsType == resolvedPrimitive)
				return true;
		} else
		{
			Class resolvedWrapper = (Class)primitiveTypeToWrapperMap.get(rhsType);
			if (resolvedWrapper != null && lhsType.isAssignableFrom(resolvedWrapper))
				return true;
		}
		return false;
	}

	public static boolean isAssignableValue(Class type, Object value)
	{
		Assert.notNull(type, "Type must not be null");
		return value == null ? !type.isPrimitive() : isAssignable(type, value.getClass());
	}

	public static String convertResourcePathToClassName(String resourcePath)
	{
		Assert.notNull(resourcePath, "Resource path must not be null");
		return resourcePath.replace('/', '.');
	}

	public static String convertClassNameToResourcePath(String className)
	{
		Assert.notNull(className, "Class name must not be null");
		return className.replace('.', '/');
	}

	public static String addResourcePathToPackagePath(Class clazz, String resourceName)
	{
		Assert.notNull(resourceName, "Resource name must not be null");
		if (!resourceName.startsWith("/"))
			return (new StringBuilder()).append(classPackageAsResourcePath(clazz)).append('/').append(resourceName).toString();
		else
			return (new StringBuilder()).append(classPackageAsResourcePath(clazz)).append(resourceName).toString();
	}

	public static String classPackageAsResourcePath(Class clazz)
	{
		if (clazz == null)
			return "";
		String className = clazz.getName();
		int packageEndIndex = className.lastIndexOf('.');
		if (packageEndIndex == -1)
		{
			return "";
		} else
		{
			String packageName = className.substring(0, packageEndIndex);
			return packageName.replace('.', '/');
		}
	}

	public static transient String classNamesToString(Class classes[])
	{
		return classNamesToString(((Collection) (Arrays.asList(classes))));
	}

	public static String classNamesToString(Collection classes)
	{
		if (CollectionUtils.isEmpty(classes))
			return "[]";
		StringBuilder sb = new StringBuilder("[");
		Iterator it = classes.iterator();
		do
		{
			if (!it.hasNext())
				break;
			Class clazz = (Class)it.next();
			sb.append(clazz.getName());
			if (it.hasNext())
				sb.append(", ");
		} while (true);
		sb.append("]");
		return sb.toString();
	}

	public static Class[] toClassArray(Collection collection)
	{
		if (collection == null)
			return null;
		else
			return (Class[])collection.toArray(new Class[collection.size()]);
	}

	public static Class[] getAllInterfaces(Object instance)
	{
		Assert.notNull(instance, "Instance must not be null");
		return getAllInterfacesForClass(instance.getClass());
	}

	public static Class[] getAllInterfacesForClass(Class clazz)
	{
		return getAllInterfacesForClass(clazz, null);
	}

	public static Class[] getAllInterfacesForClass(Class clazz, ClassLoader classLoader)
	{
		Set ifcs = getAllInterfacesForClassAsSet(clazz, classLoader);
		return (Class[])ifcs.toArray(new Class[ifcs.size()]);
	}

	public static Set getAllInterfacesAsSet(Object instance)
	{
		Assert.notNull(instance, "Instance must not be null");
		return getAllInterfacesForClassAsSet(instance.getClass());
	}

	public static Set getAllInterfacesForClassAsSet(Class clazz)
	{
		return getAllInterfacesForClassAsSet(clazz, null);
	}

	public static Set getAllInterfacesForClassAsSet(Class clazz, ClassLoader classLoader)
	{
		Assert.notNull(clazz, "Class must not be null");
		if (clazz.isInterface() && isVisible(clazz, classLoader))
			return Collections.singleton(clazz);
		Set interfaces = new LinkedHashSet();
		for (; clazz != null; clazz = clazz.getSuperclass())
		{
			Class ifcs[] = clazz.getInterfaces();
			Class aclass[] = ifcs;
			int i = aclass.length;
			for (int j = 0; j < i; j++)
			{
				Class ifc = aclass[j];
				interfaces.addAll(getAllInterfacesForClassAsSet(ifc, classLoader));
			}

		}

		return interfaces;
	}

	public static Class createCompositeInterface(Class interfaces[], ClassLoader classLoader)
	{
		Assert.notEmpty(interfaces, "Interfaces must not be empty");
		return Proxy.getProxyClass(classLoader, interfaces);
	}

	public static Class determineCommonAncestor(Class clazz1, Class clazz2)
	{
		if (clazz1 == null)
			return clazz2;
		if (clazz2 == null)
			return clazz1;
		if (clazz1.isAssignableFrom(clazz2))
			return clazz1;
		if (clazz2.isAssignableFrom(clazz1))
			return clazz2;
		Class ancestor = clazz1;
		do
		{
			ancestor = ancestor.getSuperclass();
			if (ancestor == null || java/lang/Object == ancestor)
				return null;
		} while (!ancestor.isAssignableFrom(clazz2));
		return ancestor;
	}

	public static boolean isVisible(Class clazz, ClassLoader classLoader)
	{
		if (classLoader == null)
			return true;
		Class actualClass = classLoader.loadClass(clazz.getName());
		return clazz == actualClass;
		ClassNotFoundException ex;
		ex;
		return false;
	}

	public static boolean isCglibProxy(Object object)
	{
		return isCglibProxyClass(object.getClass());
	}

	public static boolean isCglibProxyClass(Class clazz)
	{
		return clazz != null && isCglibProxyClassName(clazz.getName());
	}

	public static boolean isCglibProxyClassName(String className)
	{
		return className != null && className.contains("$$");
	}

	static 
	{
		primitiveWrapperTypeMap = new IdentityHashMap(8);
		primitiveTypeToWrapperMap = new IdentityHashMap(8);
		primitiveTypeNameMap = new HashMap(32);
		primitiveWrapperTypeMap.put(java/lang/Boolean, Boolean.TYPE);
		primitiveWrapperTypeMap.put(java/lang/Byte, Byte.TYPE);
		primitiveWrapperTypeMap.put(java/lang/Character, Character.TYPE);
		primitiveWrapperTypeMap.put(java/lang/Double, Double.TYPE);
		primitiveWrapperTypeMap.put(java/lang/Float, Float.TYPE);
		primitiveWrapperTypeMap.put(java/lang/Integer, Integer.TYPE);
		primitiveWrapperTypeMap.put(java/lang/Long, Long.TYPE);
		primitiveWrapperTypeMap.put(java/lang/Short, Short.TYPE);
		java.util.Map.Entry entry;
		for (Iterator iterator = primitiveWrapperTypeMap.entrySet().iterator(); iterator.hasNext(); registerCommonClasses(new Class[] {
	(Class)entry.getKey()
}))
		{
			entry = (java.util.Map.Entry)iterator.next();
			primitiveTypeToWrapperMap.put(entry.getValue(), entry.getKey());
		}

		Set primitiveTypes = new HashSet(32);
		primitiveTypes.addAll(primitiveWrapperTypeMap.values());
		primitiveTypes.addAll(Arrays.asList(new Class[] {
			[Z, [B, [C, [D, [F, [I, [J, [S
		}));
		primitiveTypes.add(Void.TYPE);
		Class primitiveType;
		for (Iterator iterator1 = primitiveTypes.iterator(); iterator1.hasNext(); primitiveTypeNameMap.put(primitiveType.getName(), primitiveType))
			primitiveType = (Class)iterator1.next();

		registerCommonClasses(new Class[] {
			[Ljava/lang/Boolean;, [Ljava/lang/Byte;, [Ljava/lang/Character;, [Ljava/lang/Double;, [Ljava/lang/Float;, [Ljava/lang/Integer;, [Ljava/lang/Long;, [Ljava/lang/Short;
		});
		registerCommonClasses(new Class[] {
			java/lang/Number, [Ljava/lang/Number;, java/lang/String, [Ljava/lang/String;, java/lang/Object, [Ljava/lang/Object;, java/lang/Class, [Ljava/lang/Class;
		});
		registerCommonClasses(new Class[] {
			java/lang/Throwable, java/lang/Exception, java/lang/RuntimeException, java/lang/Error, java/lang/StackTraceElement, [Ljava/lang/StackTraceElement;
		});
	}
}
