// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   BeanMapEmitter.java

package org.springframework.cglib.beans;

import java.beans.PropertyDescriptor;
import java.util.*;
import org.springframework.asm.*;
import org.springframework.cglib.core.*;

class BeanMapEmitter extends ClassEmitter
{

	private static final Type BEAN_MAP;
	private static final Type FIXED_KEY_SET = TypeUtils.parseType("org.springframework.cglib.beans.FixedKeySet");
	private static final Signature CSTRUCT_OBJECT = TypeUtils.parseConstructor("Object");
	private static final Signature CSTRUCT_STRING_ARRAY = TypeUtils.parseConstructor("String[]");
	private static final Signature BEAN_MAP_GET = TypeUtils.parseSignature("Object get(Object, Object)");
	private static final Signature BEAN_MAP_PUT = TypeUtils.parseSignature("Object put(Object, Object, Object)");
	private static final Signature KEY_SET = TypeUtils.parseSignature("java.util.Set keySet()");
	private static final Signature NEW_INSTANCE;
	private static final Signature GET_PROPERTY_TYPE = TypeUtils.parseSignature("Class getPropertyType(String)");

	public BeanMapEmitter(ClassVisitor v, String className, Class type, int require)
	{
		super(v);
		begin_class(46, 1, className, BEAN_MAP, null, "<generated>");
		EmitUtils.null_constructor(this);
		EmitUtils.factory_method(this, NEW_INSTANCE);
		generateConstructor();
		Map getters = makePropertyMap(ReflectUtils.getBeanGetters(type));
		Map setters = makePropertyMap(ReflectUtils.getBeanSetters(type));
		Map allProps = new HashMap();
		allProps.putAll(getters);
		allProps.putAll(setters);
		if (require != 0)
		{
			Iterator it = allProps.keySet().iterator();
			do
			{
				if (!it.hasNext())
					break;
				String name = (String)it.next();
				if ((require & 1) != 0 && !getters.containsKey(name) || (require & 2) != 0 && !setters.containsKey(name))
				{
					it.remove();
					getters.remove(name);
					setters.remove(name);
				}
			} while (true);
		}
		generateGet(type, getters);
		generatePut(type, setters);
		String allNames[] = getNames(allProps);
		generateKeySet(allNames);
		generateGetPropertyType(allProps, allNames);
		end_class();
	}

	private Map makePropertyMap(PropertyDescriptor props[])
	{
		Map names = new HashMap();
		for (int i = 0; i < props.length; i++)
			names.put(props[i].getName(), props[i]);

		return names;
	}

	private String[] getNames(Map propertyMap)
	{
		return (String[])(String[])propertyMap.keySet().toArray(new String[propertyMap.size()]);
	}

	private void generateConstructor()
	{
		CodeEmitter e = begin_method(1, CSTRUCT_OBJECT, null);
		e.load_this();
		e.load_arg(0);
		e.super_invoke_constructor(CSTRUCT_OBJECT);
		e.return_value();
		e.end_method();
	}

	private void generateGet(Class type, final Map getters)
	{
		final CodeEmitter e = begin_method(1, BEAN_MAP_GET, null);
		e.load_arg(0);
		e.checkcast(Type.getType(type));
		e.load_arg(1);
		e.checkcast(Constants.TYPE_STRING);
		EmitUtils.string_switch(e, getNames(getters), 1, new ObjectSwitchCallback() {

			final Map val$getters;
			final CodeEmitter val$e;
			final BeanMapEmitter this$0;

			public void processCase(Object key, Label end)
			{
				PropertyDescriptor pd = (PropertyDescriptor)getters.get(key);
				MethodInfo method = ReflectUtils.getMethodInfo(pd.getReadMethod());
				e.invoke(method);
				e.box(method.getSignature().getReturnType());
				e.return_value();
			}

			public void processDefault()
			{
				e.aconst_null();
				e.return_value();
			}

			
			{
				this.this$0 = BeanMapEmitter.this;
				getters = map;
				e = codeemitter;
				super();
			}
		});
		e.end_method();
	}

	private void generatePut(Class type, final Map setters)
	{
		final CodeEmitter e = begin_method(1, BEAN_MAP_PUT, null);
		e.load_arg(0);
		e.checkcast(Type.getType(type));
		e.load_arg(1);
		e.checkcast(Constants.TYPE_STRING);
		EmitUtils.string_switch(e, getNames(setters), 1, new ObjectSwitchCallback() {

			final Map val$setters;
			final CodeEmitter val$e;
			final BeanMapEmitter this$0;

			public void processCase(Object key, Label end)
			{
				PropertyDescriptor pd = (PropertyDescriptor)setters.get(key);
				if (pd.getReadMethod() == null)
				{
					e.aconst_null();
				} else
				{
					MethodInfo read = ReflectUtils.getMethodInfo(pd.getReadMethod());
					e.dup();
					e.invoke(read);
					e.box(read.getSignature().getReturnType());
				}
				e.swap();
				e.load_arg(2);
				MethodInfo write = ReflectUtils.getMethodInfo(pd.getWriteMethod());
				e.unbox(write.getSignature().getArgumentTypes()[0]);
				e.invoke(write);
				e.return_value();
			}

			public void processDefault()
			{
			}

			
			{
				this.this$0 = BeanMapEmitter.this;
				setters = map;
				e = codeemitter;
				super();
			}
		});
		e.aconst_null();
		e.return_value();
		e.end_method();
	}

	private void generateKeySet(String allNames[])
	{
		declare_field(10, "keys", FIXED_KEY_SET, null);
		CodeEmitter e = begin_static();
		e.new_instance(FIXED_KEY_SET);
		e.dup();
		EmitUtils.push_array(e, allNames);
		e.invoke_constructor(FIXED_KEY_SET, CSTRUCT_STRING_ARRAY);
		e.putfield("keys");
		e.return_value();
		e.end_method();
		e = begin_method(1, KEY_SET, null);
		e.load_this();
		e.getfield("keys");
		e.return_value();
		e.end_method();
	}

	private void generateGetPropertyType(final Map allProps, String allNames[])
	{
		final CodeEmitter e = begin_method(1, GET_PROPERTY_TYPE, null);
		e.load_arg(0);
		EmitUtils.string_switch(e, allNames, 1, new ObjectSwitchCallback() {

			final Map val$allProps;
			final CodeEmitter val$e;
			final BeanMapEmitter this$0;

			public void processCase(Object key, Label end)
			{
				PropertyDescriptor pd = (PropertyDescriptor)allProps.get(key);
				EmitUtils.load_class(e, Type.getType(pd.getPropertyType()));
				e.return_value();
			}

			public void processDefault()
			{
				e.aconst_null();
				e.return_value();
			}

			
			{
				this.this$0 = BeanMapEmitter.this;
				allProps = map;
				e = codeemitter;
				super();
			}
		});
		e.end_method();
	}

	static 
	{
		BEAN_MAP = TypeUtils.parseType("org.springframework.cglib.beans.BeanMap");
		NEW_INSTANCE = new Signature("newInstance", BEAN_MAP, new Type[] {
			Constants.TYPE_OBJECT
		});
	}
}
