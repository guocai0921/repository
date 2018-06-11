// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AbstractRecursiveAnnotationVisitor.java

package org.springframework.core.type.classreading;

import java.lang.reflect.Field;
import java.security.AccessControlException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.asm.*;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.util.ReflectionUtils;

// Referenced classes of package org.springframework.core.type.classreading:
//			RecursiveAnnotationAttributesVisitor, RecursiveAnnotationArrayVisitor

abstract class AbstractRecursiveAnnotationVisitor extends AnnotationVisitor
{

	protected final Log logger = LogFactory.getLog(getClass());
	protected final AnnotationAttributes attributes;
	protected final ClassLoader classLoader;

	public AbstractRecursiveAnnotationVisitor(ClassLoader classLoader, AnnotationAttributes attributes)
	{
		super(0x50000);
		this.classLoader = classLoader;
		this.attributes = attributes;
	}

	public void visit(String attributeName, Object attributeValue)
	{
		attributes.put(attributeName, attributeValue);
	}

	public AnnotationVisitor visitAnnotation(String attributeName, String asmTypeDescriptor)
	{
		String annotationType = Type.getType(asmTypeDescriptor).getClassName();
		AnnotationAttributes nestedAttributes = new AnnotationAttributes(annotationType, classLoader);
		attributes.put(attributeName, nestedAttributes);
		return new RecursiveAnnotationAttributesVisitor(annotationType, nestedAttributes, classLoader);
	}

	public AnnotationVisitor visitArray(String attributeName)
	{
		return new RecursiveAnnotationArrayVisitor(attributeName, attributes, classLoader);
	}

	public void visitEnum(String attributeName, String asmTypeDescriptor, String attributeValue)
	{
		Object newValue = getEnumValue(asmTypeDescriptor, attributeValue);
		visit(attributeName, newValue);
	}

	protected Object getEnumValue(String asmTypeDescriptor, String attributeValue)
	{
		Object valueToUse = attributeValue;
		try
		{
			Class enumType = classLoader.loadClass(Type.getType(asmTypeDescriptor).getClassName());
			Field enumConstant = ReflectionUtils.findField(enumType, attributeValue);
			if (enumConstant != null)
			{
				ReflectionUtils.makeAccessible(enumConstant);
				valueToUse = enumConstant.get(null);
			}
		}
		catch (ClassNotFoundException ex)
		{
			logger.debug("Failed to classload enum type while reading annotation metadata", ex);
		}
		catch (NoClassDefFoundError ex)
		{
			logger.debug("Failed to classload enum type while reading annotation metadata", ex);
		}
		catch (IllegalAccessException ex)
		{
			logger.debug("Could not access enum value while reading annotation metadata", ex);
		}
		catch (AccessControlException ex)
		{
			logger.debug("Could not access enum value while reading annotation metadata", ex);
		}
		return valueToUse;
	}
}
