// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AnnotationUtils.java

package org.springframework.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.util.*;

// Referenced classes of package org.springframework.core.annotation:
//			AnnotationAttributes, AnnotationConfigurationException, SynthesizedAnnotation, DefaultAnnotationAttributeExtractor, 
//			SynthesizedAnnotationInvocationHandler, MapAnnotationAttributeExtractor, AliasFor

public abstract class AnnotationUtils
{
	private static class DefaultValueHolder
	{

		final Object defaultValue;

		public DefaultValueHolder(Object defaultValue)
		{
			this.defaultValue = defaultValue;
		}
	}

	private static class AliasDescriptor
	{

		private final Method sourceAttribute;
		private final Class sourceAnnotationType;
		private final String sourceAttributeName;
		private final Method aliasedAttribute;
		private final Class aliasedAnnotationType;
		private final String aliasedAttributeName;
		private final boolean isAliasPair;

		public static AliasDescriptor from(Method attribute)
		{
			AliasDescriptor descriptor = (AliasDescriptor)AnnotationUtils.aliasDescriptorCache.get(attribute);
			if (descriptor != null)
				return descriptor;
			AliasFor aliasFor = (AliasFor)attribute.getAnnotation(org/springframework/core/annotation/AliasFor);
			if (aliasFor == null)
			{
				return null;
			} else
			{
				descriptor = new AliasDescriptor(attribute, aliasFor);
				descriptor.validate();
				AnnotationUtils.aliasDescriptorCache.put(attribute, descriptor);
				return descriptor;
			}
		}

		private void validate()
		{
			if (!isAliasPair && !AnnotationUtils.isAnnotationMetaPresent(sourceAnnotationType, aliasedAnnotationType))
			{
				String msg = String.format("@AliasFor declaration on attribute '%s' in annotation [%s] declares an alias for attribute '%s' in meta-annotation [%s] which is not meta-present.", new Object[] {
					sourceAttributeName, sourceAnnotationType.getName(), aliasedAttributeName, aliasedAnnotationType.getName()
				});
				throw new AnnotationConfigurationException(msg);
			}
			if (isAliasPair)
			{
				AliasFor mirrorAliasFor = (AliasFor)aliasedAttribute.getAnnotation(org/springframework/core/annotation/AliasFor);
				if (mirrorAliasFor == null)
				{
					String msg = String.format("Attribute '%s' in annotation [%s] must be declared as an @AliasFor [%s].", new Object[] {
						aliasedAttributeName, sourceAnnotationType.getName(), sourceAttributeName
					});
					throw new AnnotationConfigurationException(msg);
				}
				String mirrorAliasedAttributeName = getAliasedAttributeName(mirrorAliasFor, aliasedAttribute);
				if (!sourceAttributeName.equals(mirrorAliasedAttributeName))
				{
					String msg = String.format("Attribute '%s' in annotation [%s] must be declared as an @AliasFor [%s], not [%s].", new Object[] {
						aliasedAttributeName, sourceAnnotationType.getName(), sourceAttributeName, mirrorAliasedAttributeName
					});
					throw new AnnotationConfigurationException(msg);
				}
			}
			Class returnType = sourceAttribute.getReturnType();
			Class aliasedReturnType = aliasedAttribute.getReturnType();
			if (returnType != aliasedReturnType && (!aliasedReturnType.isArray() || returnType != aliasedReturnType.getComponentType()))
			{
				String msg = String.format("Misconfigured aliases: attribute '%s' in annotation [%s] and attribute '%s' in annotation [%s] must declare the same return type.", new Object[] {
					sourceAttributeName, sourceAnnotationType.getName(), aliasedAttributeName, aliasedAnnotationType.getName()
				});
				throw new AnnotationConfigurationException(msg);
			}
			if (isAliasPair)
				validateDefaultValueConfiguration(aliasedAttribute);
		}

		private void validateDefaultValueConfiguration(Method aliasedAttribute)
		{
			Assert.notNull(aliasedAttribute, "aliasedAttribute must not be null");
			Object defaultValue = sourceAttribute.getDefaultValue();
			Object aliasedDefaultValue = aliasedAttribute.getDefaultValue();
			if (defaultValue == null || aliasedDefaultValue == null)
			{
				String msg = String.format("Misconfigured aliases: attribute '%s' in annotation [%s] and attribute '%s' in annotation [%s] must declare default values.", new Object[] {
					sourceAttributeName, sourceAnnotationType.getName(), aliasedAttribute.getName(), aliasedAttribute.getDeclaringClass().getName()
				});
				throw new AnnotationConfigurationException(msg);
			}
			if (!ObjectUtils.nullSafeEquals(defaultValue, aliasedDefaultValue))
			{
				String msg = String.format("Misconfigured aliases: attribute '%s' in annotation [%s] and attribute '%s' in annotation [%s] must declare the same default value.", new Object[] {
					sourceAttributeName, sourceAnnotationType.getName(), aliasedAttribute.getName(), aliasedAttribute.getDeclaringClass().getName()
				});
				throw new AnnotationConfigurationException(msg);
			} else
			{
				return;
			}
		}

		private void validateAgainst(AliasDescriptor otherDescriptor)
		{
			validateDefaultValueConfiguration(otherDescriptor.sourceAttribute);
		}

		private boolean isOverrideFor(Class metaAnnotationType)
		{
			return aliasedAnnotationType == metaAnnotationType;
		}

		private boolean isAliasFor(AliasDescriptor otherDescriptor)
		{
			for (AliasDescriptor lhs = this; lhs != null; lhs = lhs.getAttributeOverrideDescriptor())
			{
				for (AliasDescriptor rhs = otherDescriptor; rhs != null; rhs = rhs.getAttributeOverrideDescriptor())
					if (lhs.aliasedAttribute.equals(rhs.aliasedAttribute))
						return true;

			}

			return false;
		}

		public List getAttributeAliasNames()
		{
			if (isAliasPair)
				return Collections.singletonList(aliasedAttributeName);
			List aliases = new ArrayList();
			Iterator iterator = getOtherDescriptors().iterator();
			do
			{
				if (!iterator.hasNext())
					break;
				AliasDescriptor otherDescriptor = (AliasDescriptor)iterator.next();
				if (isAliasFor(otherDescriptor))
				{
					validateAgainst(otherDescriptor);
					aliases.add(otherDescriptor.sourceAttributeName);
				}
			} while (true);
			return aliases;
		}

		private List getOtherDescriptors()
		{
			List otherDescriptors = new ArrayList();
			Iterator iterator = AnnotationUtils.getAttributeMethods(sourceAnnotationType).iterator();
			do
			{
				if (!iterator.hasNext())
					break;
				Method currentAttribute = (Method)iterator.next();
				if (!sourceAttribute.equals(currentAttribute))
				{
					AliasDescriptor otherDescriptor = from(currentAttribute);
					if (otherDescriptor != null)
						otherDescriptors.add(otherDescriptor);
				}
			} while (true);
			return otherDescriptors;
		}

		public String getAttributeOverrideName(Class metaAnnotationType)
		{
			Assert.notNull(metaAnnotationType, "metaAnnotationType must not be null");
			Assert.isTrue(java/lang/annotation/Annotation != metaAnnotationType, "metaAnnotationType must not be [java.lang.annotation.Annotation]");
			for (AliasDescriptor desc = this; desc != null; desc = desc.getAttributeOverrideDescriptor())
				if (desc.isOverrideFor(metaAnnotationType))
					return desc.aliasedAttributeName;

			return null;
		}

		private AliasDescriptor getAttributeOverrideDescriptor()
		{
			if (isAliasPair)
				return null;
			else
				return from(aliasedAttribute);
		}

		private String getAliasedAttributeName(AliasFor aliasFor, Method attribute)
		{
			String attributeName = aliasFor.attribute();
			String value = aliasFor.value();
			boolean attributeDeclared = StringUtils.hasText(attributeName);
			boolean valueDeclared = StringUtils.hasText(value);
			if (attributeDeclared && valueDeclared)
			{
				String msg = String.format("In @AliasFor declared on attribute '%s' in annotation [%s], attribute 'attribute' and its alias 'value' are present with values of [%s] and [%s], but only one is permitted.", new Object[] {
					attribute.getName(), attribute.getDeclaringClass().getName(), attributeName, value
				});
				throw new AnnotationConfigurationException(msg);
			} else
			{
				attributeName = attributeDeclared ? attributeName : value;
				return StringUtils.hasText(attributeName) ? attributeName.trim() : attribute.getName();
			}
		}

		public String toString()
		{
			return String.format("%s: @%s(%s) is an alias for @%s(%s)", new Object[] {
				getClass().getSimpleName(), sourceAnnotationType.getSimpleName(), sourceAttributeName, aliasedAnnotationType.getSimpleName(), aliasedAttributeName
			});
		}

		private AliasDescriptor(Method sourceAttribute, AliasFor aliasFor)
		{
			Class declaringClass = sourceAttribute.getDeclaringClass();
			Assert.isTrue(declaringClass.isAnnotation(), "sourceAttribute must be from an annotation");
			this.sourceAttribute = sourceAttribute;
			sourceAnnotationType = declaringClass;
			sourceAttributeName = sourceAttribute.getName();
			aliasedAnnotationType = java/lang/annotation/Annotation != aliasFor.annotation() ? aliasFor.annotation() : sourceAnnotationType;
			aliasedAttributeName = getAliasedAttributeName(aliasFor, sourceAttribute);
			if (aliasedAnnotationType == sourceAnnotationType && aliasedAttributeName.equals(sourceAttributeName))
			{
				String msg = String.format("@AliasFor declaration on attribute '%s' in annotation [%s] points to itself. Specify 'annotation' to point to a same-named attribute on a meta-annotation.", new Object[] {
					sourceAttribute.getName(), declaringClass.getName()
				});
				throw new AnnotationConfigurationException(msg);
			}
			try
			{
				aliasedAttribute = aliasedAnnotationType.getDeclaredMethod(aliasedAttributeName, new Class[0]);
			}
			catch (NoSuchMethodException ex)
			{
				String msg = String.format("Attribute '%s' in annotation [%s] is declared as an @AliasFor nonexistent attribute '%s' in annotation [%s].", new Object[] {
					sourceAttributeName, sourceAnnotationType.getName(), aliasedAttributeName, aliasedAnnotationType.getName()
				});
				throw new AnnotationConfigurationException(msg, ex);
			}
			isAliasPair = sourceAnnotationType == aliasedAnnotationType;
		}
	}

	private static class AnnotationCollector
	{

		private final Class annotationType;
		private final Class containerAnnotationType;
		private final boolean declaredMode;
		private final Set visited = new HashSet();
		private final Set result = new LinkedHashSet();

		Set getResult(AnnotatedElement element)
		{
			process(element);
			return Collections.unmodifiableSet(result);
		}

		private void process(AnnotatedElement element)
		{
			if (visited.add(element))
				try
				{
					Annotation annotations[] = declaredMode ? element.getDeclaredAnnotations() : element.getAnnotations();
					Annotation aannotation[] = annotations;
					int i = aannotation.length;
					for (int j = 0; j < i; j++)
					{
						Annotation ann = aannotation[j];
						Class currentAnnotationType = ann.annotationType();
						if (ObjectUtils.nullSafeEquals(annotationType, currentAnnotationType))
							result.add(AnnotationUtils.synthesizeAnnotation(ann, element));
						else
						if (ObjectUtils.nullSafeEquals(containerAnnotationType, currentAnnotationType))
							result.addAll(getValue(element, ann));
						else
						if (!AnnotationUtils.isInJavaLangAnnotationPackage(ann))
							process(((AnnotatedElement) (currentAnnotationType)));
					}

				}
				catch (Throwable ex)
				{
					AnnotationUtils.handleIntrospectionFailure(element, ex);
				}
		}

		private List getValue(AnnotatedElement element, Annotation annotation)
		{
			List synthesizedAnnotations;
			synthesizedAnnotations = new ArrayList();
			Annotation aannotation[] = (Annotation[])(Annotation[])AnnotationUtils.getValue(annotation);
			int i = aannotation.length;
			for (int j = 0; j < i; j++)
			{
				Annotation anno = aannotation[j];
				synthesizedAnnotations.add(AnnotationUtils.synthesizeAnnotation(anno, element));
			}

			return synthesizedAnnotations;
			Throwable ex;
			ex;
			AnnotationUtils.handleIntrospectionFailure(element, ex);
			return Collections.emptyList();
		}

		AnnotationCollector(Class annotationType, Class containerAnnotationType, boolean declaredMode)
		{
			this.annotationType = annotationType;
			this.containerAnnotationType = containerAnnotationType == null ? AnnotationUtils.resolveContainerAnnotationType(annotationType) : containerAnnotationType;
			this.declaredMode = declaredMode;
		}
	}

	private static final class AnnotationCacheKey
		implements Comparable
	{

		private final AnnotatedElement element;
		private final Class annotationType;

		public boolean equals(Object other)
		{
			if (this == other)
				return true;
			if (!(other instanceof AnnotationCacheKey))
			{
				return false;
			} else
			{
				AnnotationCacheKey otherKey = (AnnotationCacheKey)other;
				return element.equals(otherKey.element) && annotationType.equals(otherKey.annotationType);
			}
		}

		public int hashCode()
		{
			return element.hashCode() * 29 + annotationType.hashCode();
		}

		public String toString()
		{
			return (new StringBuilder()).append("@").append(annotationType).append(" on ").append(element).toString();
		}

		public int compareTo(AnnotationCacheKey other)
		{
			int result = element.toString().compareTo(other.element.toString());
			if (result == 0)
				result = annotationType.getName().compareTo(other.annotationType.getName());
			return result;
		}

		public volatile int compareTo(Object obj)
		{
			return compareTo((AnnotationCacheKey)obj);
		}

		public AnnotationCacheKey(AnnotatedElement element, Class annotationType)
		{
			this.element = element;
			this.annotationType = annotationType;
		}
	}


	public static final String VALUE = "value";
	private static final String REPEATABLE_CLASS_NAME = "java.lang.annotation.Repeatable";
	private static final Map findAnnotationCache = new ConcurrentReferenceHashMap(256);
	private static final Map metaPresentCache = new ConcurrentReferenceHashMap(256);
	private static final Map annotatedInterfaceCache = new ConcurrentReferenceHashMap(256);
	private static final Map synthesizableCache = new ConcurrentReferenceHashMap(256);
	private static final Map attributeAliasesCache = new ConcurrentReferenceHashMap(256);
	private static final Map attributeMethodsCache = new ConcurrentReferenceHashMap(256);
	private static final Map aliasDescriptorCache = new ConcurrentReferenceHashMap(256);
	private static transient Log logger;

	public AnnotationUtils()
	{
	}

	public static Annotation getAnnotation(Annotation ann, Class annotationType)
	{
		Class annotatedElement;
		if (annotationType.isInstance(ann))
			return synthesizeAnnotation(ann);
		annotatedElement = ann.annotationType();
		return synthesizeAnnotation(annotatedElement.getAnnotation(annotationType), annotatedElement);
		Throwable ex;
		ex;
		handleIntrospectionFailure(annotatedElement, ex);
		return null;
	}

	public static Annotation getAnnotation(AnnotatedElement annotatedElement, Class annotationType)
	{
		Annotation annotation;
		annotation = annotatedElement.getAnnotation(annotationType);
		if (annotation == null)
		{
			Annotation aannotation[] = annotatedElement.getAnnotations();
			int i = aannotation.length;
			int j = 0;
			do
			{
				if (j >= i)
					break;
				Annotation metaAnn = aannotation[j];
				annotation = metaAnn.annotationType().getAnnotation(annotationType);
				if (annotation != null)
					break;
				j++;
			} while (true);
		}
		return synthesizeAnnotation(annotation, annotatedElement);
		Throwable ex;
		ex;
		handleIntrospectionFailure(annotatedElement, ex);
		return null;
	}

	public static Annotation getAnnotation(Method method, Class annotationType)
	{
		Method resolvedMethod = BridgeMethodResolver.findBridgedMethod(method);
		return getAnnotation(((AnnotatedElement) (resolvedMethod)), annotationType);
	}

	public static Annotation[] getAnnotations(AnnotatedElement annotatedElement)
	{
		return synthesizeAnnotationArray(annotatedElement.getAnnotations(), annotatedElement);
		Throwable ex;
		ex;
		handleIntrospectionFailure(annotatedElement, ex);
		return null;
	}

	public static Annotation[] getAnnotations(Method method)
	{
		return synthesizeAnnotationArray(BridgeMethodResolver.findBridgedMethod(method).getAnnotations(), method);
		Throwable ex;
		ex;
		handleIntrospectionFailure(method, ex);
		return null;
	}

	/**
	 * @deprecated Method getRepeatableAnnotation is deprecated
	 */

	public static Set getRepeatableAnnotation(Method method, Class containerAnnotationType, Class annotationType)
	{
		return getRepeatableAnnotations(method, annotationType, containerAnnotationType);
	}

	/**
	 * @deprecated Method getRepeatableAnnotation is deprecated
	 */

	public static Set getRepeatableAnnotation(AnnotatedElement annotatedElement, Class containerAnnotationType, Class annotationType)
	{
		return getRepeatableAnnotations(annotatedElement, annotationType, containerAnnotationType);
	}

	public static Set getRepeatableAnnotations(AnnotatedElement annotatedElement, Class annotationType)
	{
		return getRepeatableAnnotations(annotatedElement, annotationType, null);
	}

	public static Set getRepeatableAnnotations(AnnotatedElement annotatedElement, Class annotationType, Class containerAnnotationType)
	{
		Set annotations = getDeclaredRepeatableAnnotations(annotatedElement, annotationType, containerAnnotationType);
		if (!annotations.isEmpty())
			return annotations;
		if (annotatedElement instanceof Class)
		{
			Class superclass = ((Class)annotatedElement).getSuperclass();
			if (superclass != null && java/lang/Object != superclass)
				return getRepeatableAnnotations(((AnnotatedElement) (superclass)), annotationType, containerAnnotationType);
		}
		return getRepeatableAnnotations(annotatedElement, annotationType, containerAnnotationType, false);
	}

	public static Set getDeclaredRepeatableAnnotations(AnnotatedElement annotatedElement, Class annotationType)
	{
		return getDeclaredRepeatableAnnotations(annotatedElement, annotationType, null);
	}

	public static Set getDeclaredRepeatableAnnotations(AnnotatedElement annotatedElement, Class annotationType, Class containerAnnotationType)
	{
		return getRepeatableAnnotations(annotatedElement, annotationType, containerAnnotationType, true);
	}

	private static Set getRepeatableAnnotations(AnnotatedElement annotatedElement, Class annotationType, Class containerAnnotationType, boolean declaredMode)
	{
		Assert.notNull(annotatedElement, "AnnotatedElement must not be null");
		Assert.notNull(annotationType, "Annotation type must not be null");
		if (annotatedElement instanceof Method)
			annotatedElement = BridgeMethodResolver.findBridgedMethod((Method)annotatedElement);
		return (new AnnotationCollector(annotationType, containerAnnotationType, declaredMode)).getResult(annotatedElement);
		Throwable ex;
		ex;
		handleIntrospectionFailure(annotatedElement, ex);
		return Collections.emptySet();
	}

	public static Annotation findAnnotation(AnnotatedElement annotatedElement, Class annotationType)
	{
		Assert.notNull(annotatedElement, "AnnotatedElement must not be null");
		if (annotationType == null)
		{
			return null;
		} else
		{
			Annotation ann = findAnnotation(annotatedElement, annotationType, ((Set) (new HashSet())));
			return synthesizeAnnotation(ann, annotatedElement);
		}
	}

	private static Annotation findAnnotation(AnnotatedElement annotatedElement, Class annotationType, Set visited)
	{
		Annotation anns[];
		Annotation aannotation[];
		int i;
		int j;
		anns = annotatedElement.getDeclaredAnnotations();
		aannotation = anns;
		i = aannotation.length;
		j = 0;
_L1:
		Annotation ann;
		if (j >= i)
			break MISSING_BLOCK_LABEL_52;
		ann = aannotation[j];
		if (ann.annotationType() == annotationType)
			return ann;
		j++;
		  goto _L1
		aannotation = anns;
		i = aannotation.length;
		j = 0;
_L2:
		Annotation annotation;
		if (j >= i)
			break MISSING_BLOCK_LABEL_133;
		ann = aannotation[j];
		if (isInJavaLangAnnotationPackage(ann) || !visited.add(ann))
			break MISSING_BLOCK_LABEL_118;
		annotation = findAnnotation(((AnnotatedElement) (ann.annotationType())), annotationType, visited);
		if (annotation != null)
			return annotation;
		j++;
		  goto _L2
		Throwable ex;
		ex;
		handleIntrospectionFailure(annotatedElement, ex);
		return null;
	}

	public static Annotation findAnnotation(Method method, Class annotationType)
	{
		Assert.notNull(method, "Method must not be null");
		if (annotationType == null)
			return null;
		AnnotationCacheKey cacheKey = new AnnotationCacheKey(method, annotationType);
		Annotation result = (Annotation)findAnnotationCache.get(cacheKey);
		if (result == null)
		{
			Method resolvedMethod = BridgeMethodResolver.findBridgedMethod(method);
			result = findAnnotation(((AnnotatedElement) (resolvedMethod)), annotationType);
			if (result == null)
				result = searchOnInterfaces(method, annotationType, method.getDeclaringClass().getInterfaces());
			Class clazz = method.getDeclaringClass();
			do
			{
				if (result != null)
					break;
				clazz = clazz.getSuperclass();
				if (clazz == null || java/lang/Object == clazz)
					break;
				try
				{
					Method equivalentMethod = clazz.getDeclaredMethod(method.getName(), method.getParameterTypes());
					Method resolvedEquivalentMethod = BridgeMethodResolver.findBridgedMethod(equivalentMethod);
					result = findAnnotation(((AnnotatedElement) (resolvedEquivalentMethod)), annotationType);
				}
				catch (NoSuchMethodException nosuchmethodexception) { }
				if (result == null)
					result = searchOnInterfaces(method, annotationType, clazz.getInterfaces());
			} while (true);
			if (result != null)
			{
				result = synthesizeAnnotation(result, method);
				findAnnotationCache.put(cacheKey, result);
			}
		}
		return result;
	}

	private static transient Annotation searchOnInterfaces(Method method, Class annotationType, Class ifcs[])
	{
		Annotation annotation = null;
		Class aclass[] = ifcs;
		int i = aclass.length;
		for (int j = 0; j < i; j++)
		{
			Class iface = aclass[j];
			if (!isInterfaceWithAnnotatedMethods(iface))
				continue;
			try
			{
				Method equivalentMethod = iface.getMethod(method.getName(), method.getParameterTypes());
				annotation = getAnnotation(equivalentMethod, annotationType);
			}
			catch (NoSuchMethodException nosuchmethodexception) { }
			if (annotation != null)
				break;
		}

		return annotation;
	}

	static boolean isInterfaceWithAnnotatedMethods(Class iface)
	{
		Boolean found = (Boolean)annotatedInterfaceCache.get(iface);
		if (found != null)
			return found.booleanValue();
		found = Boolean.FALSE;
		Method amethod[] = iface.getMethods();
		int i = amethod.length;
		for (int j = 0; j < i; j++)
		{
			Method ifcMethod = amethod[j];
			try
			{
				if (ifcMethod.getAnnotations().length <= 0)
					continue;
				found = Boolean.TRUE;
				break;
			}
			catch (Throwable ex)
			{
				handleIntrospectionFailure(ifcMethod, ex);
			}
		}

		annotatedInterfaceCache.put(iface, found);
		return found.booleanValue();
	}

	public static Annotation findAnnotation(Class clazz, Class annotationType)
	{
		return findAnnotation(clazz, annotationType, true);
	}

	private static Annotation findAnnotation(Class clazz, Class annotationType, boolean synthesize)
	{
		Assert.notNull(clazz, "Class must not be null");
		if (annotationType == null)
			return null;
		AnnotationCacheKey cacheKey = new AnnotationCacheKey(clazz, annotationType);
		Annotation result = (Annotation)findAnnotationCache.get(cacheKey);
		if (result == null)
		{
			result = findAnnotation(clazz, annotationType, ((Set) (new HashSet())));
			if (result != null && synthesize)
			{
				result = synthesizeAnnotation(result, clazz);
				findAnnotationCache.put(cacheKey, result);
			}
		}
		return result;
	}

	private static Annotation findAnnotation(Class clazz, Class annotationType, Set visited)
	{
		Annotation anns[];
		Annotation aannotation[];
		int j;
		int l;
		anns = clazz.getDeclaredAnnotations();
		aannotation = anns;
		j = aannotation.length;
		l = 0;
_L1:
		Annotation ann;
		if (l >= j)
			break MISSING_BLOCK_LABEL_50;
		ann = aannotation[l];
		if (ann.annotationType() == annotationType)
			return ann;
		l++;
		  goto _L1
		aannotation = anns;
		j = aannotation.length;
		l = 0;
_L2:
		Annotation annotation;
		if (l >= j)
			break MISSING_BLOCK_LABEL_133;
		ann = aannotation[l];
		if (isInJavaLangAnnotationPackage(ann) || !visited.add(ann))
			break MISSING_BLOCK_LABEL_116;
		annotation = findAnnotation(ann.annotationType(), annotationType, visited);
		if (annotation != null)
			return annotation;
		try
		{
			l++;
		}
		catch (Throwable ex)
		{
			handleIntrospectionFailure(clazz, ex);
			return null;
		}
		  goto _L2
		Class aclass[] = clazz.getInterfaces();
		int i = aclass.length;
		for (int k = 0; k < i; k++)
		{
			Class ifc = aclass[k];
			Annotation annotation = findAnnotation(ifc, annotationType, visited);
			if (annotation != null)
				return annotation;
		}

		Class superclass = clazz.getSuperclass();
		if (superclass == null || java/lang/Object == superclass)
			return null;
		else
			return findAnnotation(superclass, annotationType, visited);
	}

	public static Class findAnnotationDeclaringClass(Class annotationType, Class clazz)
	{
		Assert.notNull(annotationType, "Annotation type must not be null");
		if (clazz == null || java/lang/Object == clazz)
			return null;
		if (isAnnotationDeclaredLocally(annotationType, clazz))
			return clazz;
		else
			return findAnnotationDeclaringClass(annotationType, clazz.getSuperclass());
	}

	public static Class findAnnotationDeclaringClassForTypes(List annotationTypes, Class clazz)
	{
		Assert.notEmpty(annotationTypes, "List of annotation types must not be empty");
		if (clazz == null || java/lang/Object == clazz)
			return null;
		for (Iterator iterator = annotationTypes.iterator(); iterator.hasNext();)
		{
			Class annotationType = (Class)iterator.next();
			if (isAnnotationDeclaredLocally(annotationType, clazz))
				return clazz;
		}

		return findAnnotationDeclaringClassForTypes(annotationTypes, clazz.getSuperclass());
	}

	public static boolean isAnnotationDeclaredLocally(Class annotationType, Class clazz)
	{
		Assert.notNull(annotationType, "Annotation type must not be null");
		Assert.notNull(clazz, "Class must not be null");
		Annotation aannotation[];
		int i;
		int j;
		aannotation = clazz.getDeclaredAnnotations();
		i = aannotation.length;
		j = 0;
_L1:
		Annotation ann;
		if (j >= i)
			break MISSING_BLOCK_LABEL_63;
		ann = aannotation[j];
		if (ann.annotationType() == annotationType)
			return true;
		j++;
		  goto _L1
		Throwable ex;
		ex;
		handleIntrospectionFailure(clazz, ex);
		return false;
	}

	public static boolean isAnnotationInherited(Class annotationType, Class clazz)
	{
		Assert.notNull(annotationType, "Annotation type must not be null");
		Assert.notNull(clazz, "Class must not be null");
		return clazz.isAnnotationPresent(annotationType) && !isAnnotationDeclaredLocally(annotationType, clazz);
	}

	public static boolean isAnnotationMetaPresent(Class annotationType, Class metaAnnotationType)
	{
		Assert.notNull(annotationType, "Annotation type must not be null");
		if (metaAnnotationType == null)
			return false;
		AnnotationCacheKey cacheKey = new AnnotationCacheKey(annotationType, metaAnnotationType);
		Boolean metaPresent = (Boolean)metaPresentCache.get(cacheKey);
		if (metaPresent != null)
			return metaPresent.booleanValue();
		metaPresent = Boolean.FALSE;
		if (findAnnotation(annotationType, metaAnnotationType, false) != null)
			metaPresent = Boolean.TRUE;
		metaPresentCache.put(cacheKey, metaPresent);
		return metaPresent.booleanValue();
	}

	public static boolean isInJavaLangAnnotationPackage(Annotation annotation)
	{
		return annotation != null && isInJavaLangAnnotationPackage(annotation.annotationType());
	}

	static boolean isInJavaLangAnnotationPackage(Class annotationType)
	{
		return annotationType != null && isInJavaLangAnnotationPackage(annotationType.getName());
	}

	public static boolean isInJavaLangAnnotationPackage(String annotationType)
	{
		return annotationType != null && annotationType.startsWith("java.lang.annotation");
	}

	public static Map getAnnotationAttributes(Annotation annotation)
	{
		return getAnnotationAttributes(((AnnotatedElement) (null)), annotation);
	}

	public static Map getAnnotationAttributes(Annotation annotation, boolean classValuesAsString)
	{
		return getAnnotationAttributes(annotation, classValuesAsString, false);
	}

	public static AnnotationAttributes getAnnotationAttributes(Annotation annotation, boolean classValuesAsString, boolean nestedAnnotationsAsMap)
	{
		return getAnnotationAttributes(((AnnotatedElement) (null)), annotation, classValuesAsString, nestedAnnotationsAsMap);
	}

	public static AnnotationAttributes getAnnotationAttributes(AnnotatedElement annotatedElement, Annotation annotation)
	{
		return getAnnotationAttributes(annotatedElement, annotation, false, false);
	}

	public static AnnotationAttributes getAnnotationAttributes(AnnotatedElement annotatedElement, Annotation annotation, boolean classValuesAsString, boolean nestedAnnotationsAsMap)
	{
		return getAnnotationAttributes(annotatedElement, annotation, classValuesAsString, nestedAnnotationsAsMap);
	}

	private static AnnotationAttributes getAnnotationAttributes(Object annotatedElement, Annotation annotation, boolean classValuesAsString, boolean nestedAnnotationsAsMap)
	{
		AnnotationAttributes attributes = retrieveAnnotationAttributes(annotatedElement, annotation, classValuesAsString, nestedAnnotationsAsMap);
		postProcessAnnotationAttributes(annotatedElement, attributes, classValuesAsString, nestedAnnotationsAsMap);
		return attributes;
	}

	static AnnotationAttributes retrieveAnnotationAttributes(Object annotatedElement, Annotation annotation, boolean classValuesAsString, boolean nestedAnnotationsAsMap)
	{
		Class annotationType = annotation.annotationType();
		AnnotationAttributes attributes = new AnnotationAttributes(annotationType);
		Iterator iterator = getAttributeMethods(annotationType).iterator();
		do
		{
			if (!iterator.hasNext())
				break;
			Method method = (Method)iterator.next();
			try
			{
				Object attributeValue = method.invoke(annotation, new Object[0]);
				Object defaultValue = method.getDefaultValue();
				if (defaultValue != null && ObjectUtils.nullSafeEquals(attributeValue, defaultValue))
					attributeValue = new DefaultValueHolder(defaultValue);
				attributes.put(method.getName(), adaptValue(annotatedElement, attributeValue, classValuesAsString, nestedAnnotationsAsMap));
			}
			catch (Throwable ex)
			{
				if (ex instanceof InvocationTargetException)
				{
					Throwable targetException = ((InvocationTargetException)ex).getTargetException();
					rethrowAnnotationConfigurationException(targetException);
				}
				throw new IllegalStateException((new StringBuilder()).append("Could not obtain annotation attribute value for ").append(method).toString(), ex);
			}
		} while (true);
		return attributes;
	}

	static Object adaptValue(Object annotatedElement, Object value, boolean classValuesAsString, boolean nestedAnnotationsAsMap)
	{
		if (classValuesAsString)
		{
			if (value instanceof Class)
				return ((Class)value).getName();
			if (value instanceof Class[])
			{
				Class clazzArray[] = (Class[])(Class[])value;
				String classNames[] = new String[clazzArray.length];
				for (int i = 0; i < clazzArray.length; i++)
					classNames[i] = clazzArray[i].getName();

				return classNames;
			}
		}
		if (value instanceof Annotation)
		{
			Annotation annotation = (Annotation)value;
			if (nestedAnnotationsAsMap)
				return getAnnotationAttributes(annotatedElement, annotation, classValuesAsString, true);
			else
				return synthesizeAnnotation(annotation, annotatedElement);
		}
		if (value instanceof Annotation[])
		{
			Annotation annotations[] = (Annotation[])(Annotation[])value;
			if (nestedAnnotationsAsMap)
			{
				AnnotationAttributes mappedAnnotations[] = new AnnotationAttributes[annotations.length];
				for (int i = 0; i < annotations.length; i++)
					mappedAnnotations[i] = getAnnotationAttributes(annotatedElement, annotations[i], classValuesAsString, true);

				return mappedAnnotations;
			} else
			{
				return synthesizeAnnotationArray(annotations, annotatedElement);
			}
		} else
		{
			return value;
		}
	}

	public static void registerDefaultValues(AnnotationAttributes attributes)
	{
		Class annotationType = attributes.annotationType();
		if (annotationType != null && Modifier.isPublic(annotationType.getModifiers()))
		{
			Iterator iterator = getAttributeMethods(annotationType).iterator();
			do
			{
				if (!iterator.hasNext())
					break;
				Method annotationAttribute = (Method)iterator.next();
				String attributeName = annotationAttribute.getName();
				Object defaultValue = annotationAttribute.getDefaultValue();
				if (defaultValue != null && !attributes.containsKey(attributeName))
				{
					if (defaultValue instanceof Annotation)
						defaultValue = getAnnotationAttributes((Annotation)defaultValue, false, true);
					else
					if (defaultValue instanceof Annotation[])
					{
						Annotation realAnnotations[] = (Annotation[])(Annotation[])defaultValue;
						AnnotationAttributes mappedAnnotations[] = new AnnotationAttributes[realAnnotations.length];
						for (int i = 0; i < realAnnotations.length; i++)
							mappedAnnotations[i] = getAnnotationAttributes(realAnnotations[i], false, true);

						defaultValue = mappedAnnotations;
					}
					attributes.put(attributeName, new DefaultValueHolder(defaultValue));
				}
			} while (true);
		}
	}

	public static void postProcessAnnotationAttributes(Object annotatedElement, AnnotationAttributes attributes, boolean classValuesAsString)
	{
		postProcessAnnotationAttributes(annotatedElement, attributes, classValuesAsString, false);
	}

	static void postProcessAnnotationAttributes(Object annotatedElement, AnnotationAttributes attributes, boolean classValuesAsString, boolean nestedAnnotationsAsMap)
	{
		if (attributes == null)
			return;
		Class annotationType = attributes.annotationType();
		Set valuesAlreadyReplaced = new HashSet();
		if (!attributes.validated)
		{
			Map aliasMap = getAttributeAliasMap(annotationType);
			for (Iterator iterator1 = aliasMap.keySet().iterator(); iterator1.hasNext();)
			{
				String attributeName = (String)iterator1.next();
				if (!valuesAlreadyReplaced.contains(attributeName))
				{
					Object value = attributes.get(attributeName);
					boolean valuePresent = value != null && !(value instanceof DefaultValueHolder);
					Iterator iterator2 = ((List)aliasMap.get(attributeName)).iterator();
					while (iterator2.hasNext()) 
					{
						String aliasedAttributeName = (String)iterator2.next();
						if (!valuesAlreadyReplaced.contains(aliasedAttributeName))
						{
							Object aliasedValue = attributes.get(aliasedAttributeName);
							boolean aliasPresent = aliasedValue != null && !(aliasedValue instanceof DefaultValueHolder);
							if (valuePresent || aliasPresent)
								if (valuePresent && aliasPresent)
								{
									if (!ObjectUtils.nullSafeEquals(value, aliasedValue))
									{
										String elementAsString = annotatedElement == null ? "unknown element" : annotatedElement.toString();
										throw new AnnotationConfigurationException(String.format("In AnnotationAttributes for annotation [%s] declared on %s, attribute '%s' and its alias '%s' are declared with values of [%s] and [%s], but only one is permitted.", new Object[] {
											annotationType.getName(), elementAsString, attributeName, aliasedAttributeName, ObjectUtils.nullSafeToString(value), ObjectUtils.nullSafeToString(aliasedValue)
										}));
									}
								} else
								if (aliasPresent)
								{
									attributes.put(attributeName, adaptValue(annotatedElement, aliasedValue, classValuesAsString, nestedAnnotationsAsMap));
									valuesAlreadyReplaced.add(attributeName);
								} else
								{
									attributes.put(aliasedAttributeName, adaptValue(annotatedElement, value, classValuesAsString, nestedAnnotationsAsMap));
									valuesAlreadyReplaced.add(aliasedAttributeName);
								}
						}
					}
				}
			}

			attributes.validated = true;
		}
		Iterator iterator = attributes.keySet().iterator();
		do
		{
			if (!iterator.hasNext())
				break;
			String attributeName = (String)iterator.next();
			if (!valuesAlreadyReplaced.contains(attributeName))
			{
				Object value = attributes.get(attributeName);
				if (value instanceof DefaultValueHolder)
				{
					value = ((DefaultValueHolder)value).defaultValue;
					attributes.put(attributeName, adaptValue(annotatedElement, value, classValuesAsString, nestedAnnotationsAsMap));
				}
			}
		} while (true);
	}

	public static Object getValue(Annotation annotation)
	{
		return getValue(annotation, "value");
	}

	public static Object getValue(Annotation annotation, String attributeName)
	{
		if (annotation == null || !StringUtils.hasText(attributeName))
			return null;
		Method method;
		method = annotation.annotationType().getDeclaredMethod(attributeName, new Class[0]);
		ReflectionUtils.makeAccessible(method);
		return method.invoke(annotation, new Object[0]);
		Exception ex;
		ex;
		return null;
	}

	public static Object getDefaultValue(Annotation annotation)
	{
		return getDefaultValue(annotation, "value");
	}

	public static Object getDefaultValue(Annotation annotation, String attributeName)
	{
		if (annotation == null)
			return null;
		else
			return getDefaultValue(annotation.annotationType(), attributeName);
	}

	public static Object getDefaultValue(Class annotationType)
	{
		return getDefaultValue(annotationType, "value");
	}

	public static Object getDefaultValue(Class annotationType, String attributeName)
	{
		if (annotationType == null || !StringUtils.hasText(attributeName))
			return null;
		return annotationType.getDeclaredMethod(attributeName, new Class[0]).getDefaultValue();
		Exception ex;
		ex;
		return null;
	}

	static Annotation synthesizeAnnotation(Annotation annotation)
	{
		return synthesizeAnnotation(annotation, ((AnnotatedElement) (null)));
	}

	public static Annotation synthesizeAnnotation(Annotation annotation, AnnotatedElement annotatedElement)
	{
		return synthesizeAnnotation(annotation, annotatedElement);
	}

	static Annotation synthesizeAnnotation(Annotation annotation, Object annotatedElement)
	{
		if (annotation == null)
			return null;
		if (annotation instanceof SynthesizedAnnotation)
			return annotation;
		Class annotationType = annotation.annotationType();
		if (!isSynthesizable(annotationType))
		{
			return annotation;
		} else
		{
			DefaultAnnotationAttributeExtractor attributeExtractor = new DefaultAnnotationAttributeExtractor(annotation, annotatedElement);
			InvocationHandler handler = new SynthesizedAnnotationInvocationHandler(attributeExtractor);
			Class exposedInterfaces[] = {
				annotationType, org/springframework/core/annotation/SynthesizedAnnotation
			};
			return (Annotation)Proxy.newProxyInstance(annotation.getClass().getClassLoader(), exposedInterfaces, handler);
		}
	}

	public static Annotation synthesizeAnnotation(Map attributes, Class annotationType, AnnotatedElement annotatedElement)
	{
		Assert.notNull(annotationType, "'annotationType' must not be null");
		if (attributes == null)
		{
			return null;
		} else
		{
			MapAnnotationAttributeExtractor attributeExtractor = new MapAnnotationAttributeExtractor(attributes, annotationType, annotatedElement);
			InvocationHandler handler = new SynthesizedAnnotationInvocationHandler(attributeExtractor);
			Class exposedInterfaces[] = canExposeSynthesizedMarker(annotationType) ? (new Class[] {
				annotationType, org/springframework/core/annotation/SynthesizedAnnotation
			}) : (new Class[] {
				annotationType
			});
			return (Annotation)Proxy.newProxyInstance(annotationType.getClassLoader(), exposedInterfaces, handler);
		}
	}

	public static Annotation synthesizeAnnotation(Class annotationType)
	{
		return synthesizeAnnotation(Collections.emptyMap(), annotationType, null);
	}

	static Annotation[] synthesizeAnnotationArray(Annotation annotations[], Object annotatedElement)
	{
		if (annotations == null)
			return null;
		Annotation synthesized[] = (Annotation[])(Annotation[])Array.newInstance(annotations.getClass().getComponentType(), annotations.length);
		for (int i = 0; i < annotations.length; i++)
			synthesized[i] = synthesizeAnnotation(annotations[i], annotatedElement);

		return synthesized;
	}

	static Annotation[] synthesizeAnnotationArray(Map maps[], Class annotationType)
	{
		Assert.notNull(annotationType, "'annotationType' must not be null");
		if (maps == null)
			return null;
		Annotation synthesized[] = (Annotation[])(Annotation[])Array.newInstance(annotationType, maps.length);
		for (int i = 0; i < maps.length; i++)
			synthesized[i] = synthesizeAnnotation(maps[i], annotationType, null);

		return synthesized;
	}

	static Map getAttributeAliasMap(Class annotationType)
	{
		if (annotationType == null)
			return Collections.emptyMap();
		Map map = (Map)attributeAliasesCache.get(annotationType);
		if (map != null)
			return map;
		map = new LinkedHashMap();
		Iterator iterator = getAttributeMethods(annotationType).iterator();
		do
		{
			if (!iterator.hasNext())
				break;
			Method attribute = (Method)iterator.next();
			List aliasNames = getAttributeAliasNames(attribute);
			if (!aliasNames.isEmpty())
				map.put(attribute.getName(), aliasNames);
		} while (true);
		attributeAliasesCache.put(annotationType, map);
		return map;
	}

	private static boolean canExposeSynthesizedMarker(Class annotationType)
	{
		return Class.forName(org/springframework/core/annotation/SynthesizedAnnotation.getName(), false, annotationType.getClassLoader()) == org/springframework/core/annotation/SynthesizedAnnotation;
		ClassNotFoundException ex;
		ex;
		return false;
	}

	private static boolean isSynthesizable(Class annotationType)
	{
		Boolean synthesizable = (Boolean)synthesizableCache.get(annotationType);
		if (synthesizable != null)
			return synthesizable.booleanValue();
		synthesizable = Boolean.FALSE;
		Iterator iterator = getAttributeMethods(annotationType).iterator();
		do
		{
			if (!iterator.hasNext())
				break;
			Method attribute = (Method)iterator.next();
			if (!getAttributeAliasNames(attribute).isEmpty())
			{
				synthesizable = Boolean.TRUE;
				break;
			}
			Class returnType = attribute.getReturnType();
			Class nestedAnnotationType;
			if ([Ljava/lang/annotation/Annotation;.isAssignableFrom(returnType))
			{
				nestedAnnotationType = returnType.getComponentType();
				if (!isSynthesizable(nestedAnnotationType))
					continue;
				synthesizable = Boolean.TRUE;
				break;
			}
			if (!java/lang/annotation/Annotation.isAssignableFrom(returnType))
				continue;
			nestedAnnotationType = returnType;
			if (!isSynthesizable(nestedAnnotationType))
				continue;
			synthesizable = Boolean.TRUE;
			break;
		} while (true);
		synthesizableCache.put(annotationType, synthesizable);
		return synthesizable.booleanValue();
	}

	static List getAttributeAliasNames(Method attribute)
	{
		Assert.notNull(attribute, "attribute must not be null");
		AliasDescriptor descriptor = AliasDescriptor.from(attribute);
		return descriptor == null ? Collections.emptyList() : descriptor.getAttributeAliasNames();
	}

	static String getAttributeOverrideName(Method attribute, Class metaAnnotationType)
	{
		Assert.notNull(attribute, "attribute must not be null");
		Assert.notNull(metaAnnotationType, "metaAnnotationType must not be null");
		Assert.isTrue(java/lang/annotation/Annotation != metaAnnotationType, "metaAnnotationType must not be [java.lang.annotation.Annotation]");
		AliasDescriptor descriptor = AliasDescriptor.from(attribute);
		return descriptor == null ? null : descriptor.getAttributeOverrideName(metaAnnotationType);
	}

	static List getAttributeMethods(Class annotationType)
	{
		List methods = (List)attributeMethodsCache.get(annotationType);
		if (methods != null)
			return methods;
		methods = new ArrayList();
		Method amethod[] = annotationType.getDeclaredMethods();
		int i = amethod.length;
		for (int j = 0; j < i; j++)
		{
			Method method = amethod[j];
			if (isAttributeMethod(method))
			{
				ReflectionUtils.makeAccessible(method);
				methods.add(method);
			}
		}

		attributeMethodsCache.put(annotationType, methods);
		return methods;
	}

	static Annotation getAnnotation(AnnotatedElement element, String annotationName)
	{
		Annotation aannotation[] = element.getAnnotations();
		int i = aannotation.length;
		for (int j = 0; j < i; j++)
		{
			Annotation annotation = aannotation[j];
			if (annotation.annotationType().getName().equals(annotationName))
				return annotation;
		}

		return null;
	}

	static boolean isAttributeMethod(Method method)
	{
		return method != null && method.getParameterTypes().length == 0 && method.getReturnType() != Void.TYPE;
	}

	static boolean isAnnotationTypeMethod(Method method)
	{
		return method != null && method.getName().equals("annotationType") && method.getParameterTypes().length == 0;
	}

	static Class resolveContainerAnnotationType(Class annotationType)
	{
		Object value;
		Annotation repeatable = getAnnotation(annotationType, "java.lang.annotation.Repeatable");
		if (repeatable == null)
			break MISSING_BLOCK_LABEL_30;
		value = getValue(repeatable);
		return (Class)value;
		Exception ex;
		ex;
		handleIntrospectionFailure(annotationType, ex);
		return null;
	}

	static void rethrowAnnotationConfigurationException(Throwable ex)
	{
		if (ex instanceof AnnotationConfigurationException)
			throw (AnnotationConfigurationException)ex;
		else
			return;
	}

	static void handleIntrospectionFailure(AnnotatedElement element, Throwable ex)
	{
		rethrowAnnotationConfigurationException(ex);
		Log loggerToUse = logger;
		if (loggerToUse == null)
		{
			loggerToUse = LogFactory.getLog(org/springframework/core/annotation/AnnotationUtils);
			logger = loggerToUse;
		}
		if ((element instanceof Class) && java/lang/annotation/Annotation.isAssignableFrom((Class)element))
		{
			if (loggerToUse.isDebugEnabled())
				loggerToUse.debug((new StringBuilder()).append("Failed to introspect meta-annotations on [").append(element).append("]: ").append(ex).toString());
		} else
		if (loggerToUse.isInfoEnabled())
			loggerToUse.info((new StringBuilder()).append("Failed to introspect annotations on [").append(element).append("]: ").append(ex).toString());
	}


}
