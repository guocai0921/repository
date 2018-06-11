// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   RecursiveAnnotationArrayVisitor.java

package org.springframework.core.type.classreading;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import org.springframework.asm.AnnotationVisitor;
import org.springframework.asm.Type;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.util.ObjectUtils;

// Referenced classes of package org.springframework.core.type.classreading:
//			AbstractRecursiveAnnotationVisitor, RecursiveAnnotationAttributesVisitor

class RecursiveAnnotationArrayVisitor extends AbstractRecursiveAnnotationVisitor
{

	private final String attributeName;
	private final List allNestedAttributes = new ArrayList();

	public RecursiveAnnotationArrayVisitor(String attributeName, AnnotationAttributes attributes, ClassLoader classLoader)
	{
		super(classLoader, attributes);
		this.attributeName = attributeName;
	}

	public void visit(String attributeName, Object attributeValue)
	{
		Object newValue = attributeValue;
		Object existingValue = attributes.get(this.attributeName);
		if (existingValue != null)
		{
			newValue = ((Object) (ObjectUtils.addObjectToArray((Object[])(Object[])existingValue, newValue)));
		} else
		{
			Class arrayClass = newValue.getClass();
			if (java/lang/Enum.isAssignableFrom(arrayClass))
				for (; arrayClass.getSuperclass() != null && !arrayClass.isEnum(); arrayClass = arrayClass.getSuperclass());
			Object newArray[] = (Object[])(Object[])Array.newInstance(arrayClass, 1);
			newArray[0] = newValue;
			newValue = ((Object) (newArray));
		}
		attributes.put(this.attributeName, newValue);
	}

	public AnnotationVisitor visitAnnotation(String attributeName, String asmTypeDescriptor)
	{
		String annotationType = Type.getType(asmTypeDescriptor).getClassName();
		AnnotationAttributes nestedAttributes = new AnnotationAttributes(annotationType, classLoader);
		allNestedAttributes.add(nestedAttributes);
		return new RecursiveAnnotationAttributesVisitor(annotationType, nestedAttributes, classLoader);
	}

	public void visitEnd()
	{
		if (!allNestedAttributes.isEmpty())
			attributes.put(attributeName, ((Object) (allNestedAttributes.toArray(new AnnotationAttributes[allNestedAttributes.size()]))));
	}
}
