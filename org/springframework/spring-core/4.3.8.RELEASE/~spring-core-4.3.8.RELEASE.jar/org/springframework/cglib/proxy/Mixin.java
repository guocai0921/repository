// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Mixin.java

package org.springframework.cglib.proxy;

import java.security.ProtectionDomain;
import java.util.*;
import org.springframework.asm.ClassVisitor;
import org.springframework.cglib.core.*;

// Referenced classes of package org.springframework.cglib.proxy:
//			MixinEmitter, MixinBeanEmitter, MixinEverythingEmitter

public abstract class Mixin
{
	private static class Route
	{

		private Class classes[];
		private int route[];



		Route(Object delegates[])
		{
			Map map = new HashMap();
			ArrayList collect = new ArrayList();
label0:
			for (int i = 0; i < delegates.length; i++)
			{
				Class delegate = delegates[i].getClass();
				collect.clear();
				ReflectUtils.addAllInterfaces(delegate, collect);
				Iterator it = collect.iterator();
				do
				{
					if (!it.hasNext())
						continue label0;
					Class iface = (Class)it.next();
					if (!map.containsKey(iface))
						map.put(iface, new Integer(i));
				} while (true);
			}

			classes = new Class[map.size()];
			route = new int[map.size()];
			int index = 0;
			for (Iterator it = map.keySet().iterator(); it.hasNext();)
			{
				Class key = (Class)it.next();
				classes[index] = key;
				route[index] = ((Integer)map.get(key)).intValue();
				index++;
			}

		}
	}

	public static class Generator extends AbstractClassGenerator
	{

		private static final org.springframework.cglib.core.AbstractClassGenerator.Source SOURCE = new org.springframework.cglib.core.AbstractClassGenerator.Source(org/springframework/cglib/proxy/Mixin.getName());
		private Class classes[];
		private Object delegates[];
		private int style;
		private int route[];

		protected ClassLoader getDefaultClassLoader()
		{
			return classes[0].getClassLoader();
		}

		protected ProtectionDomain getProtectionDomain()
		{
			return ReflectUtils.getProtectionDomain(classes[0]);
		}

		public void setStyle(int style)
		{
			switch (style)
			{
			case 0: // '\0'
			case 1: // '\001'
			case 2: // '\002'
				this.style = style;
				break;

			default:
				throw new IllegalArgumentException((new StringBuilder()).append("Unknown mixin style: ").append(style).toString());
			}
		}

		public void setClasses(Class classes[])
		{
			this.classes = classes;
		}

		public void setDelegates(Object delegates[])
		{
			this.delegates = delegates;
		}

		public Mixin create()
		{
			if (classes == null && delegates == null)
				throw new IllegalStateException("Either classes or delegates must be set");
label0:
			switch (style)
			{
			default:
				break;

			case 0: // '\0'
				if (classes == null)
				{
					Route r = Mixin.route(delegates);
					classes = r.classes;
					route = r.route;
				}
				break;

			case 1: // '\001'
			case 2: // '\002'
				if (classes == null)
				{
					classes = ReflectUtils.getClasses(delegates);
					break;
				}
				if (delegates == null)
					break;
				Class temp[] = ReflectUtils.getClasses(delegates);
				if (classes.length != temp.length)
					throw new IllegalStateException("Specified classes are incompatible with delegates");
				int i = 0;
				do
				{
					if (i >= classes.length)
						break label0;
					if (!classes[i].isAssignableFrom(temp[i]))
						throw new IllegalStateException((new StringBuilder()).append("Specified class ").append(classes[i]).append(" is incompatible with delegate class ").append(temp[i]).append(" (index ").append(i).append(")").toString());
					i++;
				} while (true);
			}
			setNamePrefix(classes[ReflectUtils.findPackageProtected(classes)].getName());
			return (Mixin)super.create(Mixin.KEY_FACTORY.newInstance(style, ReflectUtils.getNames(classes), route));
		}

		public void generateClass(ClassVisitor v)
		{
			switch (style)
			{
			case 0: // '\0'
				new MixinEmitter(v, getClassName(), classes, route);
				break;

			case 1: // '\001'
				new MixinBeanEmitter(v, getClassName(), classes);
				break;

			case 2: // '\002'
				new MixinEverythingEmitter(v, getClassName(), classes);
				break;
			}
		}

		protected Object firstInstance(Class type)
		{
			return ((Mixin)ReflectUtils.newInstance(type)).newInstance(delegates);
		}

		protected Object nextInstance(Object instance)
		{
			return ((Mixin)instance).newInstance(delegates);
		}


		public Generator()
		{
			super(SOURCE);
			style = 0;
		}
	}

	static interface MixinKey
	{

		public abstract Object newInstance(int i, String as[], int ai[]);
	}


	private static final MixinKey KEY_FACTORY;
	private static final Map ROUTE_CACHE = Collections.synchronizedMap(new HashMap());
	public static final int STYLE_INTERFACES = 0;
	public static final int STYLE_BEANS = 1;
	public static final int STYLE_EVERYTHING = 2;

	public Mixin()
	{
	}

	public abstract Mixin newInstance(Object aobj[]);

	public static Mixin create(Object delegates[])
	{
		Generator gen = new Generator();
		gen.setDelegates(delegates);
		return gen.create();
	}

	public static Mixin create(Class interfaces[], Object delegates[])
	{
		Generator gen = new Generator();
		gen.setClasses(interfaces);
		gen.setDelegates(delegates);
		return gen.create();
	}

	public static Mixin createBean(Object beans[])
	{
		return createBean(null, beans);
	}

	public static Mixin createBean(ClassLoader loader, Object beans[])
	{
		Generator gen = new Generator();
		gen.setStyle(1);
		gen.setDelegates(beans);
		gen.setClassLoader(loader);
		return gen.create();
	}

	public static Class[] getClasses(Object delegates[])
	{
		return (Class[])(Class[])route(delegates).classes.clone();
	}

	private static Route route(Object delegates[])
	{
		Object key = ClassesKey.create(delegates);
		Route route = (Route)ROUTE_CACHE.get(key);
		if (route == null)
			ROUTE_CACHE.put(key, route = new Route(delegates));
		return route;
	}

	static 
	{
		KEY_FACTORY = (MixinKey)KeyFactory.create(org/springframework/cglib/proxy/Mixin$MixinKey, KeyFactory.CLASS_BY_NAME);
	}


}
