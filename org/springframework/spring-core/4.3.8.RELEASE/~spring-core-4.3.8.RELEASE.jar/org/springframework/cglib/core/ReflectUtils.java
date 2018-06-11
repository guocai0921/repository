// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ReflectUtils.java

package org.springframework.cglib.core;

import java.beans.*;
import java.lang.reflect.*;
import java.security.*;
import java.util.*;
import org.springframework.asm.Attribute;
import org.springframework.asm.Type;

// Referenced classes of package org.springframework.cglib.core:
//			TypeUtils, Signature, CodeGenerationException, Constants, 
//			MethodInfo, ClassInfo

public class ReflectUtils
{

	private static final Map primitives;
	private static final Map transforms;
	private static final ClassLoader defaultLoader = org/springframework/cglib/core/ReflectUtils.getClassLoader();
	private static Method DEFINE_CLASS;
	private static Method DEFINE_CLASS_UNSAFE;
	private static final ProtectionDomain PROTECTION_DOMAIN;
	private static final Object UNSAFE;
	private static final Throwable THROWABLE;
	private static final List OBJECT_METHODS = new ArrayList();
	private static final String CGLIB_PACKAGES[] = {
		"java.lang"
	};

	private ReflectUtils()
	{
	}

	public static ProtectionDomain getProtectionDomain(Class source)
	{
		if (source == null)
			return null;
		else
			return (ProtectionDomain)AccessController.doPrivileged(new PrivilegedAction(source) {

				final Class val$source;

				public Object run()
				{
					return source.getProtectionDomain();
				}

			
			{
				source = class1;
				super();
			}
			});
	}

	public static Type[] getExceptionTypes(Member member)
	{
		if (member instanceof Method)
			return TypeUtils.getTypes(((Method)member).getExceptionTypes());
		if (member instanceof Constructor)
			return TypeUtils.getTypes(((Constructor)member).getExceptionTypes());
		else
			throw new IllegalArgumentException("Cannot get exception types of a field");
	}

	public static Signature getSignature(Member member)
	{
		if (member instanceof Method)
			return new Signature(member.getName(), Type.getMethodDescriptor((Method)member));
		if (member instanceof Constructor)
		{
			Type types[] = TypeUtils.getTypes(((Constructor)member).getParameterTypes());
			return new Signature("<init>", Type.getMethodDescriptor(Type.VOID_TYPE, types));
		} else
		{
			throw new IllegalArgumentException("Cannot get signature of a field");
		}
	}

	public static Constructor findConstructor(String desc)
	{
		return findConstructor(desc, defaultLoader);
	}

	public static Constructor findConstructor(String desc, ClassLoader loader)
	{
		String className;
		int lparen = desc.indexOf('(');
		className = desc.substring(0, lparen).trim();
		return getClass(className, loader).getConstructor(parseTypes(desc, loader));
		ClassNotFoundException e;
		e;
		throw new CodeGenerationException(e);
		e;
		throw new CodeGenerationException(e);
	}

	public static Method findMethod(String desc)
	{
		return findMethod(desc, defaultLoader);
	}

	public static Method findMethod(String desc, ClassLoader loader)
	{
		String className;
		String methodName;
		int lparen = desc.indexOf('(');
		int dot = desc.lastIndexOf('.', lparen);
		className = desc.substring(0, dot).trim();
		methodName = desc.substring(dot + 1, lparen).trim();
		return getClass(className, loader).getDeclaredMethod(methodName, parseTypes(desc, loader));
		ClassNotFoundException e;
		e;
		throw new CodeGenerationException(e);
		e;
		throw new CodeGenerationException(e);
	}

	private static Class[] parseTypes(String desc, ClassLoader loader)
		throws ClassNotFoundException
	{
		int lparen = desc.indexOf('(');
		int rparen = desc.indexOf(')', lparen);
		List params = new ArrayList();
		int start = lparen + 1;
		do
		{
			int comma = desc.indexOf(',', start);
			if (comma < 0)
				break;
			params.add(desc.substring(start, comma).trim());
			start = comma + 1;
		} while (true);
		if (start < rparen)
			params.add(desc.substring(start, rparen).trim());
		Class types[] = new Class[params.size()];
		for (int i = 0; i < types.length; i++)
			types[i] = getClass((String)params.get(i), loader);

		return types;
	}

	private static Class getClass(String className, ClassLoader loader)
		throws ClassNotFoundException
	{
		return getClass(className, loader, CGLIB_PACKAGES);
	}

	private static Class getClass(String className, ClassLoader loader, String packages[])
		throws ClassNotFoundException
	{
		String save;
		int dimensions;
		StringBuffer brackets;
		String prefix;
		String suffix;
		save = className;
		dimensions = 0;
		for (int index = 0; (index = className.indexOf("[]", index) + 1) > 0;)
			dimensions++;

		brackets = new StringBuffer(className.length() - dimensions);
		for (int i = 0; i < dimensions; i++)
			brackets.append('[');

		className = className.substring(0, className.length() - 2 * dimensions);
		prefix = dimensions <= 0 ? "" : (new StringBuilder()).append(brackets).append("L").toString();
		suffix = dimensions <= 0 ? "" : ";";
		return Class.forName((new StringBuilder()).append(prefix).append(className).append(suffix).toString(), false, loader);
		ClassNotFoundException classnotfoundexception;
		classnotfoundexception;
		int i = 0;
_L2:
		if (i >= packages.length)
			break; /* Loop/switch isn't completed */
		return Class.forName((new StringBuilder()).append(prefix).append(packages[i]).append('.').append(className).append(suffix).toString(), false, loader);
		ClassNotFoundException classnotfoundexception1;
		classnotfoundexception1;
		i++;
		if (true) goto _L2; else goto _L1
_L1:
		String transform;
		if (dimensions == 0)
		{
			Class c = (Class)primitives.get(className);
			if (c != null)
				return c;
			break MISSING_BLOCK_LABEL_300;
		}
		transform = (String)transforms.get(className);
		if (transform == null)
			break MISSING_BLOCK_LABEL_300;
		return Class.forName((new StringBuilder()).append(brackets).append(transform).toString(), false, loader);
		classnotfoundexception1;
		throw new ClassNotFoundException(save);
	}

	public static Object newInstance(Class type)
	{
		return newInstance(type, Constants.EMPTY_CLASS_ARRAY, null);
	}

	public static Object newInstance(Class type, Class parameterTypes[], Object args[])
	{
		return newInstance(getConstructor(type, parameterTypes), args);
	}

	public static Object newInstance(Constructor cstruct, Object args[])
	{
		boolean flag;
		Exception exception;
		flag = cstruct.isAccessible();
		Object obj;
		try
		{
			if (!flag)
				cstruct.setAccessible(true);
			Object result = cstruct.newInstance(args);
			obj = result;
		}
		catch (InstantiationException e)
		{
			throw new CodeGenerationException(e);
		}
		catch (IllegalAccessException e)
		{
			throw new CodeGenerationException(e);
		}
		catch (InvocationTargetException e)
		{
			throw new CodeGenerationException(e.getTargetException());
		}
		finally
		{
			if (flag) goto _L0; else goto _L0
		}
		if (!flag)
			cstruct.setAccessible(flag);
		return obj;
		cstruct.setAccessible(flag);
		throw exception;
	}

	public static Constructor getConstructor(Class type, Class parameterTypes[])
	{
		Constructor constructor;
		constructor = type.getDeclaredConstructor(parameterTypes);
		constructor.setAccessible(true);
		return constructor;
		NoSuchMethodException e;
		e;
		throw new CodeGenerationException(e);
	}

	public static String[] getNames(Class classes[])
	{
		if (classes == null)
			return null;
		String names[] = new String[classes.length];
		for (int i = 0; i < names.length; i++)
			names[i] = classes[i].getName();

		return names;
	}

	public static Class[] getClasses(Object objects[])
	{
		Class classes[] = new Class[objects.length];
		for (int i = 0; i < objects.length; i++)
			classes[i] = objects[i].getClass();

		return classes;
	}

	public static Method findNewInstance(Class iface)
	{
		Method m = findInterfaceMethod(iface);
		if (!m.getName().equals("newInstance"))
			throw new IllegalArgumentException((new StringBuilder()).append(iface).append(" missing newInstance method").toString());
		else
			return m;
	}

	public static Method[] getPropertyMethods(PropertyDescriptor properties[], boolean read, boolean write)
	{
		Set methods = new HashSet();
		for (int i = 0; i < properties.length; i++)
		{
			PropertyDescriptor pd = properties[i];
			if (read)
				methods.add(pd.getReadMethod());
			if (write)
				methods.add(pd.getWriteMethod());
		}

		methods.remove(null);
		return (Method[])(Method[])methods.toArray(new Method[methods.size()]);
	}

	public static PropertyDescriptor[] getBeanProperties(Class type)
	{
		return getPropertiesHelper(type, true, true);
	}

	public static PropertyDescriptor[] getBeanGetters(Class type)
	{
		return getPropertiesHelper(type, true, false);
	}

	public static PropertyDescriptor[] getBeanSetters(Class type)
	{
		return getPropertiesHelper(type, false, true);
	}

	private static PropertyDescriptor[] getPropertiesHelper(Class type, boolean read, boolean write)
	{
		PropertyDescriptor all[];
		BeanInfo info = Introspector.getBeanInfo(type, java/lang/Object);
		all = info.getPropertyDescriptors();
		if (read && write)
			return all;
		List properties;
		properties = new ArrayList(all.length);
		for (int i = 0; i < all.length; i++)
		{
			PropertyDescriptor pd = all[i];
			if (read && pd.getReadMethod() != null || write && pd.getWriteMethod() != null)
				properties.add(pd);
		}

		return (PropertyDescriptor[])(PropertyDescriptor[])properties.toArray(new PropertyDescriptor[properties.size()]);
		IntrospectionException e;
		e;
		throw new CodeGenerationException(e);
	}

	public static Method findDeclaredMethod(Class type, String methodName, Class parameterTypes[])
		throws NoSuchMethodException
	{
		Class cl = type;
_L2:
		if (cl == null)
			break; /* Loop/switch isn't completed */
		return cl.getDeclaredMethod(methodName, parameterTypes);
		NoSuchMethodException e;
		e;
		cl = cl.getSuperclass();
		if (true) goto _L2; else goto _L1
_L1:
		throw new NoSuchMethodException(methodName);
	}

	public static List addAllMethods(Class type, List list)
	{
		if (type == java/lang/Object)
			list.addAll(OBJECT_METHODS);
		else
			list.addAll(Arrays.asList(type.getDeclaredMethods()));
		Class superclass = type.getSuperclass();
		if (superclass != null)
			addAllMethods(superclass, list);
		Class interfaces[] = type.getInterfaces();
		for (int i = 0; i < interfaces.length; i++)
			addAllMethods(interfaces[i], list);

		return list;
	}

	public static List addAllInterfaces(Class type, List list)
	{
		Class superclass = type.getSuperclass();
		if (superclass != null)
		{
			list.addAll(Arrays.asList(type.getInterfaces()));
			addAllInterfaces(superclass, list);
		}
		return list;
	}

	public static Method findInterfaceMethod(Class iface)
	{
		if (!iface.isInterface())
			throw new IllegalArgumentException((new StringBuilder()).append(iface).append(" is not an interface").toString());
		Method methods[] = iface.getDeclaredMethods();
		if (methods.length != 1)
			throw new IllegalArgumentException((new StringBuilder()).append("expecting exactly 1 method in ").append(iface).toString());
		else
			return methods[0];
	}

	public static Class defineClass(String className, byte b[], ClassLoader loader)
		throws Exception
	{
		return defineClass(className, b, loader, PROTECTION_DOMAIN);
	}

	public static Class defineClass(String className, byte b[], ClassLoader loader, ProtectionDomain protectionDomain)
		throws Exception
	{
		Class c;
		if (DEFINE_CLASS != null)
		{
			Object args[] = {
				className, b, new Integer(0), new Integer(b.length), protectionDomain
			};
			c = (Class)DEFINE_CLASS.invoke(loader, args);
		} else
		if (DEFINE_CLASS_UNSAFE != null)
		{
			Object args[] = {
				className, b, new Integer(0), new Integer(b.length), loader, protectionDomain
			};
			c = (Class)DEFINE_CLASS_UNSAFE.invoke(UNSAFE, args);
		} else
		{
			throw new CodeGenerationException(THROWABLE);
		}
		Class.forName(className, true, loader);
		return c;
	}

	public static int findPackageProtected(Class classes[])
	{
		for (int i = 0; i < classes.length; i++)
			if (!Modifier.isPublic(classes[i].getModifiers()))
				return i;

		return 0;
	}

	public static MethodInfo getMethodInfo(Member member, int modifiers)
	{
		Signature sig = getSignature(member);
		return new MethodInfo(member, modifiers, sig) {

			private ClassInfo ci;
			final Member val$member;
			final int val$modifiers;
			final Signature val$sig;

			public ClassInfo getClassInfo()
			{
				if (ci == null)
					ci = ReflectUtils.getClassInfo(member.getDeclaringClass());
				return ci;
			}

			public int getModifiers()
			{
				return modifiers;
			}

			public Signature getSignature()
			{
				return sig;
			}

			public Type[] getExceptionTypes()
			{
				return ReflectUtils.getExceptionTypes(member);
			}

			public Attribute getAttribute()
			{
				return null;
			}

			
			{
				member = member1;
				modifiers = i;
				sig = signature;
				super();
			}
		};
	}

	public static MethodInfo getMethodInfo(Member member)
	{
		return getMethodInfo(member, member.getModifiers());
	}

	public static ClassInfo getClassInfo(Class clazz)
	{
		Type type = Type.getType(clazz);
		Type sc = clazz.getSuperclass() != null ? Type.getType(clazz.getSuperclass()) : null;
		return new ClassInfo(type, sc, clazz) {

			final Type val$type;
			final Type val$sc;
			final Class val$clazz;

			public Type getType()
			{
				return type;
			}

			public Type getSuperType()
			{
				return sc;
			}

			public Type[] getInterfaces()
			{
				return TypeUtils.getTypes(clazz.getInterfaces());
			}

			public int getModifiers()
			{
				return clazz.getModifiers();
			}

			
			{
				type = type1;
				sc = type2;
				clazz = class1;
				super();
			}
		};
	}

	public static Method[] findMethods(String namesAndDescriptors[], Method methods[])
	{
		Map map = new HashMap();
		for (int i = 0; i < methods.length; i++)
		{
			Method method = methods[i];
			map.put((new StringBuilder()).append(method.getName()).append(Type.getMethodDescriptor(method)).toString(), method);
		}

		Method result[] = new Method[namesAndDescriptors.length / 2];
		for (int i = 0; i < result.length; i++)
		{
			result[i] = (Method)map.get((new StringBuilder()).append(namesAndDescriptors[i * 2]).append(namesAndDescriptors[i * 2 + 1]).toString());
			if (result[i] != null);
		}

		return result;
	}

	static 
	{
		primitives = new HashMap(8);
		transforms = new HashMap(8);
		Throwable throwable = null;
		ProtectionDomain protectionDomain;
		Method defineClass;
		Method defineClassUnsafe;
		Object unsafe;
		try
		{
			protectionDomain = getProtectionDomain(org/springframework/cglib/core/ReflectUtils);
			try
			{
				defineClass = (Method)AccessController.doPrivileged(new PrivilegedExceptionAction() {

					public Object run()
						throws Exception
					{
						Class loader = Class.forName("java.lang.ClassLoader");
						Method defineClass = loader.getDeclaredMethod("defineClass", new Class[] {
							java/lang/String, [B, Integer.TYPE, Integer.TYPE, java/security/ProtectionDomain
						});
						defineClass.setAccessible(true);
						return defineClass;
					}

				});
				defineClassUnsafe = null;
				unsafe = null;
			}
			catch (Throwable t)
			{
				throwable = t;
				defineClass = null;
				unsafe = AccessController.doPrivileged(new PrivilegedExceptionAction() {

					public Object run()
						throws Exception
					{
						Class u = Class.forName("sun.misc.Unsafe");
						Field theUnsafe = u.getDeclaredField("theUnsafe");
						theUnsafe.setAccessible(true);
						return theUnsafe.get(null);
					}

				});
				Class u = Class.forName("sun.misc.Unsafe");
				defineClassUnsafe = u.getMethod("defineClass", new Class[] {
					java/lang/String, [B, Integer.TYPE, Integer.TYPE, java/lang/ClassLoader, java/security/ProtectionDomain
				});
			}
			AccessController.doPrivileged(new PrivilegedExceptionAction() {

				public Object run()
					throws Exception
				{
					Method methods[] = java/lang/Object.getDeclaredMethods();
					Method amethod[] = methods;
					int i = amethod.length;
					for (int j = 0; j < i; j++)
					{
						Method method = amethod[j];
						if (!"finalize".equals(method.getName()) && (method.getModifiers() & 0x18) <= 0)
							ReflectUtils.OBJECT_METHODS.add(method);
					}

					return null;
				}

			});
		}
		catch (Throwable t)
		{
			if (throwable == null)
				throwable = t;
			protectionDomain = null;
			defineClass = null;
			defineClassUnsafe = null;
			unsafe = null;
		}
		PROTECTION_DOMAIN = protectionDomain;
		DEFINE_CLASS = defineClass;
		DEFINE_CLASS_UNSAFE = defineClassUnsafe;
		UNSAFE = unsafe;
		THROWABLE = throwable;
		primitives.put("byte", Byte.TYPE);
		primitives.put("char", Character.TYPE);
		primitives.put("double", Double.TYPE);
		primitives.put("float", Float.TYPE);
		primitives.put("int", Integer.TYPE);
		primitives.put("long", Long.TYPE);
		primitives.put("short", Short.TYPE);
		primitives.put("boolean", Boolean.TYPE);
		transforms.put("byte", "B");
		transforms.put("char", "C");
		transforms.put("double", "D");
		transforms.put("float", "F");
		transforms.put("int", "I");
		transforms.put("long", "J");
		transforms.put("short", "S");
		transforms.put("boolean", "Z");
	}

}
