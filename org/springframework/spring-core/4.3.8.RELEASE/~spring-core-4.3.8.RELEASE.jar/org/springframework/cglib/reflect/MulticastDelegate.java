// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   MulticastDelegate.java

package org.springframework.cglib.reflect;

import java.security.ProtectionDomain;
import java.util.*;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.Type;
import org.springframework.cglib.core.*;

public abstract class MulticastDelegate
	implements Cloneable
{
	public static class Generator extends AbstractClassGenerator
	{

		private static final org.springframework.cglib.core.AbstractClassGenerator.Source SOURCE = new org.springframework.cglib.core.AbstractClassGenerator.Source(org/springframework/cglib/reflect/MulticastDelegate.getName());
		private static final Type MULTICAST_DELEGATE;
		private static final Signature NEW_INSTANCE;
		private static final Signature ADD_DELEGATE;
		private static final Signature ADD_HELPER;
		private Class iface;

		protected ClassLoader getDefaultClassLoader()
		{
			return iface.getClassLoader();
		}

		protected ProtectionDomain getProtectionDomain()
		{
			return ReflectUtils.getProtectionDomain(iface);
		}

		public void setInterface(Class iface)
		{
			this.iface = iface;
		}

		public MulticastDelegate create()
		{
			setNamePrefix(org/springframework/cglib/reflect/MulticastDelegate.getName());
			return (MulticastDelegate)super.create(iface.getName());
		}

		public void generateClass(ClassVisitor cv)
		{
			MethodInfo method = ReflectUtils.getMethodInfo(ReflectUtils.findInterfaceMethod(iface));
			ClassEmitter ce = new ClassEmitter(cv);
			ce.begin_class(46, 1, getClassName(), MULTICAST_DELEGATE, new Type[] {
				Type.getType(iface)
			}, "<generated>");
			EmitUtils.null_constructor(ce);
			emitProxy(ce, method);
			CodeEmitter e = ce.begin_method(1, NEW_INSTANCE, null);
			e.new_instance_this();
			e.dup();
			e.invoke_constructor_this();
			e.return_value();
			e.end_method();
			e = ce.begin_method(1, ADD_DELEGATE, null);
			e.load_this();
			e.load_arg(0);
			e.checkcast(Type.getType(iface));
			e.invoke_virtual_this(ADD_HELPER);
			e.return_value();
			e.end_method();
			ce.end_class();
		}

		private void emitProxy(ClassEmitter ce, final MethodInfo method)
		{
			int modifiers = 1;
			if ((method.getModifiers() & 0x80) == 128)
				modifiers |= 0x80;
			final CodeEmitter e = EmitUtils.begin_method(ce, method, modifiers);
			Type returnType = method.getSignature().getReturnType();
			final boolean returns = returnType != Type.VOID_TYPE;
			Local result = null;
			if (returns)
			{
				result = e.make_local(returnType);
				e.zero_or_null(returnType);
				e.store_local(result);
			}
			e.load_this();
			e.super_getfield("targets", Constants.TYPE_OBJECT_ARRAY);
			final Local result2 = result;
			EmitUtils.process_array(e, Constants.TYPE_OBJECT_ARRAY, new ProcessArrayCallback() {

				final CodeEmitter val$e;
				final MethodInfo val$method;
				final boolean val$returns;
				final Local val$result2;
				final Generator this$0;

				public void processElement(Type type)
				{
					e.checkcast(Type.getType(iface));
					e.load_args();
					e.invoke(method);
					if (returns)
						e.store_local(result2);
				}

				
				{
					this.this$0 = Generator.this;
					e = codeemitter;
					method = methodinfo;
					returns = flag;
					result2 = local;
					super();
				}
			});
			if (returns)
				e.load_local(result);
			e.return_value();
			e.end_method();
		}

		protected Object firstInstance(Class type)
		{
			return ((MulticastDelegate)ReflectUtils.newInstance(type)).newInstance();
		}

		protected Object nextInstance(Object instance)
		{
			return ((MulticastDelegate)instance).newInstance();
		}

		static 
		{
			MULTICAST_DELEGATE = TypeUtils.parseType("org.springframework.cglib.reflect.MulticastDelegate");
			NEW_INSTANCE = new Signature("newInstance", MULTICAST_DELEGATE, new Type[0]);
			ADD_DELEGATE = new Signature("add", MULTICAST_DELEGATE, new Type[] {
				Constants.TYPE_OBJECT
			});
			ADD_HELPER = new Signature("addHelper", MULTICAST_DELEGATE, new Type[] {
				Constants.TYPE_OBJECT
			});
		}


		public Generator()
		{
			super(SOURCE);
		}
	}


	protected Object targets[];

	protected MulticastDelegate()
	{
		targets = new Object[0];
	}

	public List getTargets()
	{
		return new ArrayList(Arrays.asList(targets));
	}

	public abstract MulticastDelegate add(Object obj);

	protected MulticastDelegate addHelper(Object target)
	{
		MulticastDelegate copy = newInstance();
		copy.targets = new Object[targets.length + 1];
		System.arraycopy(((Object) (targets)), 0, ((Object) (copy.targets)), 0, targets.length);
		copy.targets[targets.length] = target;
		return copy;
	}

	public MulticastDelegate remove(Object target)
	{
		for (int i = targets.length - 1; i >= 0; i--)
			if (targets[i].equals(target))
			{
				MulticastDelegate copy = newInstance();
				copy.targets = new Object[targets.length - 1];
				System.arraycopy(((Object) (targets)), 0, ((Object) (copy.targets)), 0, i);
				System.arraycopy(((Object) (targets)), i + 1, ((Object) (copy.targets)), i, targets.length - i - 1);
				return copy;
			}

		return this;
	}

	public abstract MulticastDelegate newInstance();

	public static MulticastDelegate create(Class iface)
	{
		Generator gen = new Generator();
		gen.setInterface(iface);
		return gen.create();
	}
}
