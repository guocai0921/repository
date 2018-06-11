// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AnnotatedElementUtils.java

package org.springframework.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.*;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.util.*;

// Referenced classes of package org.springframework.core.annotation:
//			AnnotationAttributes, AnnotationUtils, AnnotationConfigurationException

public class AnnotatedElementUtils
{
	private static class MergedAnnotationAttributesProcessor
		implements Processor
	{

		private final boolean classValuesAsString;
		private final boolean nestedAnnotationsAsMap;
		private final boolean aggregates;
		private final List aggregatedResults;

		public boolean alwaysProcesses()
		{
			return false;
		}

		public boolean aggregates()
		{
			return aggregates;
		}

		public List getAggregatedResults()
		{
			return aggregatedResults;
		}

		public AnnotationAttributes process(AnnotatedElement annotatedElement, Annotation annotation, int metaDepth)
		{
			return AnnotationUtils.retrieveAnnotationAttributes(annotatedElement, annotation, classValuesAsString, nestedAnnotationsAsMap);
		}

		public void postProcess(AnnotatedElement element, Annotation annotation, AnnotationAttributes attributes)
		{
			annotation = AnnotationUtils.synthesizeAnnotation(annotation, element);
			Class targetAnnotationType = attributes.annotationType();
			Set valuesAlreadyReplaced = new HashSet();
			Iterator iterator = AnnotationUtils.getAttributeMethods(annotation.annotationType()).iterator();
			do
			{
				if (!iterator.hasNext())
					break;
				Method attributeMethod = (Method)iterator.next();
				String attributeName = attributeMethod.getName();
				String attributeOverrideName = AnnotationUtils.getAttributeOverrideName(attributeMethod, targetAnnotationType);
				if (attributeOverrideName != null)
				{
					if (!valuesAlreadyReplaced.contains(attributeOverrideName))
					{
						List targetAttributeNames = new ArrayList();
						targetAttributeNames.add(attributeOverrideName);
						valuesAlreadyReplaced.add(attributeOverrideName);
						List aliases = (List)AnnotationUtils.getAttributeAliasMap(targetAnnotationType).get(attributeOverrideName);
						if (aliases != null)
						{
							Iterator iterator1 = aliases.iterator();
							do
							{
								if (!iterator1.hasNext())
									break;
								String alias = (String)iterator1.next();
								if (!valuesAlreadyReplaced.contains(alias))
								{
									targetAttributeNames.add(alias);
									valuesAlreadyReplaced.add(alias);
								}
							} while (true);
						}
						overrideAttributes(element, annotation, attributes, attributeName, targetAttributeNames);
					}
				} else
				if (!"value".equals(attributeName) && attributes.containsKey(attributeName))
					overrideAttribute(element, annotation, attributes, attributeName, attributeName);
			} while (true);
		}

		private void overrideAttributes(AnnotatedElement element, Annotation annotation, AnnotationAttributes attributes, String sourceAttributeName, List targetAttributeNames)
		{
			Object adaptedValue = getAdaptedValue(element, annotation, sourceAttributeName);
			String targetAttributeName;
			for (Iterator iterator = targetAttributeNames.iterator(); iterator.hasNext(); attributes.put(targetAttributeName, adaptedValue))
				targetAttributeName = (String)iterator.next();

		}

		private void overrideAttribute(AnnotatedElement element, Annotation annotation, AnnotationAttributes attributes, String sourceAttributeName, String targetAttributeName)
		{
			attributes.put(targetAttributeName, getAdaptedValue(element, annotation, sourceAttributeName));
		}

		private Object getAdaptedValue(AnnotatedElement element, Annotation annotation, String sourceAttributeName)
		{
			Object value = AnnotationUtils.getValue(annotation, sourceAttributeName);
			return AnnotationUtils.adaptValue(element, value, classValuesAsString, nestedAnnotationsAsMap);
		}

		public volatile void postProcess(AnnotatedElement annotatedelement, Annotation annotation, Object obj)
		{
			postProcess(annotatedelement, annotation, (AnnotationAttributes)obj);
		}

		public volatile Object process(AnnotatedElement annotatedelement, Annotation annotation, int i)
		{
			return process(annotatedelement, annotation, i);
		}

		MergedAnnotationAttributesProcessor()
		{
			this(false, false, false);
		}

		MergedAnnotationAttributesProcessor(boolean classValuesAsString, boolean nestedAnnotationsAsMap)
		{
			this(classValuesAsString, nestedAnnotationsAsMap, false);
		}

		MergedAnnotationAttributesProcessor(boolean classValuesAsString, boolean nestedAnnotationsAsMap, boolean aggregates)
		{
			this.classValuesAsString = classValuesAsString;
			this.nestedAnnotationsAsMap = nestedAnnotationsAsMap;
			this.aggregates = aggregates;
			aggregatedResults = aggregates ? ((List) (new ArrayList())) : null;
		}
	}

	static class AlwaysTrueBooleanAnnotationProcessor extends SimpleAnnotationProcessor
	{

		public final Boolean process(AnnotatedElement annotatedElement, Annotation annotation, int metaDepth)
		{
			return Boolean.TRUE;
		}

		public volatile Object process(AnnotatedElement annotatedelement, Annotation annotation, int i)
		{
			return process(annotatedelement, annotation, i);
		}

		AlwaysTrueBooleanAnnotationProcessor()
		{
		}
	}

	private static abstract class SimpleAnnotationProcessor
		implements Processor
	{

		private final boolean alwaysProcesses;

		public final boolean alwaysProcesses()
		{
			return alwaysProcesses;
		}

		public final void postProcess(AnnotatedElement annotatedelement, Annotation annotation1, Object obj)
		{
		}

		public final boolean aggregates()
		{
			return false;
		}

		public final List getAggregatedResults()
		{
			throw new UnsupportedOperationException("SimpleAnnotationProcessor does not support aggregated results");
		}

		public SimpleAnnotationProcessor()
		{
			this(false);
		}

		public SimpleAnnotationProcessor(boolean alwaysProcesses)
		{
			this.alwaysProcesses = alwaysProcesses;
		}
	}

	private static interface Processor
	{

		public abstract Object process(AnnotatedElement annotatedelement, Annotation annotation, int i);

		public abstract void postProcess(AnnotatedElement annotatedelement, Annotation annotation, Object obj);

		public abstract boolean alwaysProcesses();

		public abstract boolean aggregates();

		public abstract List getAggregatedResults();
	}


	private static final Boolean CONTINUE = null;
	private static final Annotation EMPTY_ANNOTATION_ARRAY[] = new Annotation[0];
	private static final Processor alwaysTrueAnnotationProcessor = new AlwaysTrueBooleanAnnotationProcessor();

	public AnnotatedElementUtils()
	{
	}

	public static transient AnnotatedElement forAnnotations(Annotation annotations[])
	{
		return new AnnotatedElement(annotations) {

			final Annotation val$annotations[];

			public Annotation getAnnotation(Class annotationClass)
			{
				Annotation aannotation[] = annotations;
				int i = aannotation.length;
				for (int j = 0; j < i; j++)
				{
					Annotation ann = aannotation[j];
					if (ann.annotationType() == annotationClass)
						return ann;
				}

				return null;
			}

			public Annotation[] getAnnotations()
			{
				return annotations;
			}

			public Annotation[] getDeclaredAnnotations()
			{
				return annotations;
			}

			
			{
				annotations = aannotation;
				super();
			}
		};
	}

	public static Set getMetaAnnotationTypes(AnnotatedElement element, Class annotationType)
	{
		Assert.notNull(element, "AnnotatedElement must not be null");
		Assert.notNull(annotationType, "'annotationType' must not be null");
		return getMetaAnnotationTypes(element, element.getAnnotation(annotationType));
	}

	public static Set getMetaAnnotationTypes(AnnotatedElement element, String annotationName)
	{
		Assert.notNull(element, "AnnotatedElement must not be null");
		Assert.hasLength(annotationName, "'annotationName' must not be null or empty");
		return getMetaAnnotationTypes(element, AnnotationUtils.getAnnotation(element, annotationName));
	}

	private static Set getMetaAnnotationTypes(AnnotatedElement element, Annotation composed)
	{
		if (composed == null)
			return null;
		Set types;
		types = new LinkedHashSet();
		searchWithGetSemantics(composed.annotationType(), null, null, null, new SimpleAnnotationProcessor(true, types) {

			final Set val$types;

			public Object process(AnnotatedElement annotatedElement, Annotation annotation, int metaDepth)
			{
				types.add(annotation.annotationType().getName());
				return AnnotatedElementUtils.CONTINUE;
			}

			
			{
				types = set;
				super(alwaysProcesses);
			}
		}, new HashSet(), 1);
		return types.isEmpty() ? null : types;
		Throwable ex;
		ex;
		AnnotationUtils.rethrowAnnotationConfigurationException(ex);
		throw new IllegalStateException((new StringBuilder()).append("Failed to introspect annotations on ").append(element).toString(), ex);
	}

	public static boolean hasMetaAnnotationTypes(AnnotatedElement element, Class annotationType)
	{
		Assert.notNull(element, "AnnotatedElement must not be null");
		Assert.notNull(annotationType, "'annotationType' must not be null");
		return hasMetaAnnotationTypes(element, annotationType, null);
	}

	public static boolean hasMetaAnnotationTypes(AnnotatedElement element, String annotationName)
	{
		Assert.notNull(element, "AnnotatedElement must not be null");
		Assert.hasLength(annotationName, "'annotationName' must not be null or empty");
		return hasMetaAnnotationTypes(element, null, annotationName);
	}

	private static boolean hasMetaAnnotationTypes(AnnotatedElement element, Class annotationType, String annotationName)
	{
		return Boolean.TRUE.equals(searchWithGetSemantics(element, annotationType, annotationName, new SimpleAnnotationProcessor() {

			public Boolean process(AnnotatedElement annotatedElement, Annotation annotation, int metaDepth)
			{
				return metaDepth <= 0 ? AnnotatedElementUtils.CONTINUE : Boolean.TRUE;
			}

			public volatile Object process(AnnotatedElement annotatedelement, Annotation annotation, int i)
			{
				return process(annotatedelement, annotation, i);
			}

		}));
	}

	public static boolean isAnnotated(AnnotatedElement element, Class annotationType)
	{
		Assert.notNull(element, "AnnotatedElement must not be null");
		Assert.notNull(annotationType, "'annotationType' must not be null");
		if (element.isAnnotationPresent(annotationType))
			return true;
		else
			return Boolean.TRUE.equals(searchWithGetSemantics(element, annotationType, null, alwaysTrueAnnotationProcessor));
	}

	public static boolean isAnnotated(AnnotatedElement element, String annotationName)
	{
		Assert.notNull(element, "AnnotatedElement must not be null");
		Assert.hasLength(annotationName, "'annotationName' must not be null or empty");
		return Boolean.TRUE.equals(searchWithGetSemantics(element, null, annotationName, alwaysTrueAnnotationProcessor));
	}

	/**
	 * @deprecated Method getAnnotationAttributes is deprecated
	 */

	public static AnnotationAttributes getAnnotationAttributes(AnnotatedElement element, String annotationName)
	{
		return getMergedAnnotationAttributes(element, annotationName);
	}

	/**
	 * @deprecated Method getAnnotationAttributes is deprecated
	 */

	public static AnnotationAttributes getAnnotationAttributes(AnnotatedElement element, String annotationName, boolean classValuesAsString, boolean nestedAnnotationsAsMap)
	{
		return getMergedAnnotationAttributes(element, annotationName, classValuesAsString, nestedAnnotationsAsMap);
	}

	public static AnnotationAttributes getMergedAnnotationAttributes(AnnotatedElement element, Class annotationType)
	{
		Assert.notNull(annotationType, "'annotationType' must not be null");
		AnnotationAttributes attributes = (AnnotationAttributes)searchWithGetSemantics(element, annotationType, null, new MergedAnnotationAttributesProcessor());
		AnnotationUtils.postProcessAnnotationAttributes(element, attributes, false, false);
		return attributes;
	}

	public static AnnotationAttributes getMergedAnnotationAttributes(AnnotatedElement element, String annotationName)
	{
		return getMergedAnnotationAttributes(element, annotationName, false, false);
	}

	public static AnnotationAttributes getMergedAnnotationAttributes(AnnotatedElement element, String annotationName, boolean classValuesAsString, boolean nestedAnnotationsAsMap)
	{
		Assert.hasLength(annotationName, "'annotationName' must not be null or empty");
		AnnotationAttributes attributes = (AnnotationAttributes)searchWithGetSemantics(element, null, annotationName, new MergedAnnotationAttributesProcessor(classValuesAsString, nestedAnnotationsAsMap));
		AnnotationUtils.postProcessAnnotationAttributes(element, attributes, classValuesAsString, nestedAnnotationsAsMap);
		return attributes;
	}

	public static Annotation getMergedAnnotation(AnnotatedElement element, Class annotationType)
	{
		Assert.notNull(annotationType, "'annotationType' must not be null");
		if (!(element instanceof Class))
		{
			Annotation annotation = element.getAnnotation(annotationType);
			if (annotation != null)
				return AnnotationUtils.synthesizeAnnotation(annotation, element);
		}
		AnnotationAttributes attributes = getMergedAnnotationAttributes(element, annotationType);
		return AnnotationUtils.synthesizeAnnotation(attributes, annotationType, element);
	}

	public static Set getAllMergedAnnotations(AnnotatedElement element, Class annotationType)
	{
		Assert.notNull(element, "AnnotatedElement must not be null");
		Assert.notNull(annotationType, "'annotationType' must not be null");
		MergedAnnotationAttributesProcessor processor = new MergedAnnotationAttributesProcessor(false, false, true);
		searchWithGetSemantics(element, annotationType, null, processor);
		return postProcessAndSynthesizeAggregatedResults(element, annotationType, processor.getAggregatedResults());
	}

	public static Set getMergedRepeatableAnnotations(AnnotatedElement element, Class annotationType)
	{
		return getMergedRepeatableAnnotations(element, annotationType, null);
	}

	public static Set getMergedRepeatableAnnotations(AnnotatedElement element, Class annotationType, Class containerType)
	{
		Assert.notNull(element, "AnnotatedElement must not be null");
		Assert.notNull(annotationType, "'annotationType' must not be null");
		if (containerType == null)
			containerType = resolveContainerType(annotationType);
		else
			validateContainerType(annotationType, containerType);
		MergedAnnotationAttributesProcessor processor = new MergedAnnotationAttributesProcessor(false, false, true);
		searchWithGetSemantics(element, annotationType, null, containerType, processor);
		return postProcessAndSynthesizeAggregatedResults(element, annotationType, processor.getAggregatedResults());
	}

	public static MultiValueMap getAllAnnotationAttributes(AnnotatedElement element, String annotationName)
	{
		return getAllAnnotationAttributes(element, annotationName, false, false);
	}

	public static MultiValueMap getAllAnnotationAttributes(AnnotatedElement element, String annotationName, boolean classValuesAsString, boolean nestedAnnotationsAsMap)
	{
		MultiValueMap attributesMap = new LinkedMultiValueMap();
		searchWithGetSemantics(element, null, annotationName, new SimpleAnnotationProcessor(classValuesAsString, nestedAnnotationsAsMap, attributesMap) {

			final boolean val$classValuesAsString;
			final boolean val$nestedAnnotationsAsMap;
			final MultiValueMap val$attributesMap;

			public Object process(AnnotatedElement annotatedElement, Annotation annotation, int metaDepth)
			{
				AnnotationAttributes annotationAttributes = AnnotationUtils.getAnnotationAttributes(annotation, classValuesAsString, nestedAnnotationsAsMap);
				java.util.Map.Entry entry;
				for (Iterator iterator = annotationAttributes.entrySet().iterator(); iterator.hasNext(); attributesMap.add(entry.getKey(), entry.getValue()))
					entry = (java.util.Map.Entry)iterator.next();

				return AnnotatedElementUtils.CONTINUE;
			}

			
			{
				classValuesAsString = flag;
				nestedAnnotationsAsMap = flag1;
				attributesMap = multivaluemap;
				super();
			}
		});
		return attributesMap.isEmpty() ? null : attributesMap;
	}

	public static boolean hasAnnotation(AnnotatedElement element, Class annotationType)
	{
		Assert.notNull(element, "AnnotatedElement must not be null");
		Assert.notNull(annotationType, "'annotationType' must not be null");
		if (element.isAnnotationPresent(annotationType))
			return true;
		else
			return Boolean.TRUE.equals(searchWithFindSemantics(element, annotationType, null, alwaysTrueAnnotationProcessor));
	}

	public static AnnotationAttributes findMergedAnnotationAttributes(AnnotatedElement element, Class annotationType, boolean classValuesAsString, boolean nestedAnnotationsAsMap)
	{
		AnnotationAttributes attributes = (AnnotationAttributes)searchWithFindSemantics(element, annotationType, null, new MergedAnnotationAttributesProcessor(classValuesAsString, nestedAnnotationsAsMap));
		AnnotationUtils.postProcessAnnotationAttributes(element, attributes, classValuesAsString, nestedAnnotationsAsMap);
		return attributes;
	}

	public static AnnotationAttributes findMergedAnnotationAttributes(AnnotatedElement element, String annotationName, boolean classValuesAsString, boolean nestedAnnotationsAsMap)
	{
		AnnotationAttributes attributes = (AnnotationAttributes)searchWithFindSemantics(element, null, annotationName, new MergedAnnotationAttributesProcessor(classValuesAsString, nestedAnnotationsAsMap));
		AnnotationUtils.postProcessAnnotationAttributes(element, attributes, classValuesAsString, nestedAnnotationsAsMap);
		return attributes;
	}

	public static Annotation findMergedAnnotation(AnnotatedElement element, Class annotationType)
	{
		Assert.notNull(annotationType, "'annotationType' must not be null");
		if (!(element instanceof Class))
		{
			Annotation annotation = element.getAnnotation(annotationType);
			if (annotation != null)
				return AnnotationUtils.synthesizeAnnotation(annotation, element);
		}
		AnnotationAttributes attributes = findMergedAnnotationAttributes(element, annotationType, false, false);
		return AnnotationUtils.synthesizeAnnotation(attributes, annotationType, element);
	}

	/**
	 * @deprecated Method findMergedAnnotation is deprecated
	 */

	public static Annotation findMergedAnnotation(AnnotatedElement element, String annotationName)
	{
		AnnotationAttributes attributes = findMergedAnnotationAttributes(element, annotationName, false, false);
		return AnnotationUtils.synthesizeAnnotation(attributes, attributes.annotationType(), element);
	}

	public static Set findAllMergedAnnotations(AnnotatedElement element, Class annotationType)
	{
		Assert.notNull(element, "AnnotatedElement must not be null");
		Assert.notNull(annotationType, "'annotationType' must not be null");
		MergedAnnotationAttributesProcessor processor = new MergedAnnotationAttributesProcessor(false, false, true);
		searchWithFindSemantics(element, annotationType, null, processor);
		return postProcessAndSynthesizeAggregatedResults(element, annotationType, processor.getAggregatedResults());
	}

	public static Set findMergedRepeatableAnnotations(AnnotatedElement element, Class annotationType)
	{
		return findMergedRepeatableAnnotations(element, annotationType, null);
	}

	public static Set findMergedRepeatableAnnotations(AnnotatedElement element, Class annotationType, Class containerType)
	{
		Assert.notNull(element, "AnnotatedElement must not be null");
		Assert.notNull(annotationType, "'annotationType' must not be null");
		if (containerType == null)
			containerType = resolveContainerType(annotationType);
		else
			validateContainerType(annotationType, containerType);
		MergedAnnotationAttributesProcessor processor = new MergedAnnotationAttributesProcessor(false, false, true);
		searchWithFindSemantics(element, annotationType, null, containerType, processor);
		return postProcessAndSynthesizeAggregatedResults(element, annotationType, processor.getAggregatedResults());
	}

	private static Object searchWithGetSemantics(AnnotatedElement element, Class annotationType, String annotationName, Processor processor)
	{
		return searchWithGetSemantics(element, annotationType, annotationName, null, processor);
	}

	private static Object searchWithGetSemantics(AnnotatedElement element, Class annotationType, String annotationName, Class containerType, Processor processor)
	{
		return searchWithGetSemantics(element, annotationType, annotationName, containerType, processor, ((Set) (new HashSet())), 0);
		Throwable ex;
		ex;
		AnnotationUtils.rethrowAnnotationConfigurationException(ex);
		throw new IllegalStateException((new StringBuilder()).append("Failed to introspect annotations on ").append(element).toString(), ex);
	}

	private static Object searchWithGetSemantics(AnnotatedElement element, Class annotationType, String annotationName, Class containerType, Processor processor, Set visited, int metaDepth)
	{
		Assert.notNull(element, "AnnotatedElement must not be null");
		if (!visited.add(element))
			break MISSING_BLOCK_LABEL_163;
		List declaredAnnotations;
		Object result;
		declaredAnnotations = Arrays.asList(element.getDeclaredAnnotations());
		result = searchWithGetSemanticsInAnnotations(element, declaredAnnotations, annotationType, annotationName, containerType, processor, visited, metaDepth);
		if (result != null)
			return result;
		if (!(element instanceof Class))
			break MISSING_BLOCK_LABEL_163;
		List inheritedAnnotations = new ArrayList();
		Annotation aannotation[] = element.getAnnotations();
		int i = aannotation.length;
		for (int j = 0; j < i; j++)
		{
			Annotation annotation = aannotation[j];
			if (!declaredAnnotations.contains(annotation))
				inheritedAnnotations.add(annotation);
		}

		result = searchWithGetSemanticsInAnnotations(element, inheritedAnnotations, annotationType, annotationName, containerType, processor, visited, metaDepth);
		if (result != null)
			return result;
		break MISSING_BLOCK_LABEL_163;
		Throwable ex;
		ex;
		AnnotationUtils.handleIntrospectionFailure(element, ex);
		return null;
	}

	private static Object searchWithGetSemanticsInAnnotations(AnnotatedElement element, List annotations, Class annotationType, String annotationName, Class containerType, Processor processor, Set visited, int metaDepth)
	{
		Iterator iterator = annotations.iterator();
label0:
		do
		{
			Annotation annotation;
			Class currentAnnotationType;
			do
			{
				if (!iterator.hasNext())
					break label0;
				annotation = (Annotation)iterator.next();
				currentAnnotationType = annotation.annotationType();
				if (!AnnotationUtils.isInJavaLangAnnotationPackage(currentAnnotationType))
				{
					if (currentAnnotationType != annotationType && !currentAnnotationType.getName().equals(annotationName) && !processor.alwaysProcesses())
						continue;
					Object result = processor.process(element, annotation, metaDepth);
					if (result != null)
						if (processor.aggregates() && metaDepth == 0)
							processor.getAggregatedResults().add(result);
						else
							return result;
				}
				continue label0;
			} while (currentAnnotationType != containerType);
			Annotation aannotation[] = getRawAnnotationsFromContainer(element, annotation);
			int i = aannotation.length;
			int j = 0;
			while (j < i) 
			{
				Annotation contained = aannotation[j];
				Object result = processor.process(element, contained, metaDepth);
				if (result != null)
					processor.getAggregatedResults().add(result);
				j++;
			}
		} while (true);
		iterator = annotations.iterator();
		do
		{
			if (!iterator.hasNext())
				break;
			Annotation annotation = (Annotation)iterator.next();
			Class currentAnnotationType = annotation.annotationType();
			if (!AnnotationUtils.isInJavaLangAnnotationPackage(currentAnnotationType))
			{
				Object result = searchWithGetSemantics(currentAnnotationType, annotationType, annotationName, containerType, processor, visited, metaDepth + 1);
				if (result != null)
				{
					processor.postProcess(element, annotation, result);
					if (processor.aggregates() && metaDepth == 0)
						processor.getAggregatedResults().add(result);
					else
						return result;
				}
			}
		} while (true);
		return null;
	}

	private static Object searchWithFindSemantics(AnnotatedElement element, Class annotationType, String annotationName, Processor processor)
	{
		return searchWithFindSemantics(element, annotationType, annotationName, null, processor);
	}

	private static Object searchWithFindSemantics(AnnotatedElement element, Class annotationType, String annotationName, Class containerType, Processor processor)
	{
		if (containerType != null && !processor.aggregates())
			throw new IllegalArgumentException("Searches for repeatable annotations must supply an aggregating Processor");
		return searchWithFindSemantics(element, annotationType, annotationName, containerType, processor, ((Set) (new HashSet())), 0);
		Throwable ex;
		ex;
		AnnotationUtils.rethrowAnnotationConfigurationException(ex);
		throw new IllegalStateException((new StringBuilder()).append("Failed to introspect annotations on ").append(element).toString(), ex);
	}

	private static Object searchWithFindSemantics(AnnotatedElement element, Class annotationType, String annotationName, Class containerType, Processor processor, Set visited, int metaDepth)
	{
		Assert.notNull(element, "AnnotatedElement must not be null");
		if (!visited.add(element))
			break MISSING_BLOCK_LABEL_711;
		Annotation annotations[];
		List aggregatedResults;
		Annotation aannotation[];
		int i;
		int j;
		annotations = element.getDeclaredAnnotations();
		aggregatedResults = processor.aggregates() ? ((List) (new ArrayList())) : null;
		aannotation = annotations;
		i = aannotation.length;
		j = 0;
_L3:
		Annotation annotation;
		Class currentAnnotationType;
		if (j >= i)
			break MISSING_BLOCK_LABEL_249;
		annotation = aannotation[j];
		currentAnnotationType = annotation.annotationType();
		if (AnnotationUtils.isInJavaLangAnnotationPackage(currentAnnotationType))
			break MISSING_BLOCK_LABEL_243;
		if (currentAnnotationType != annotationType && !currentAnnotationType.getName().equals(annotationName) && !processor.alwaysProcesses()) goto _L2; else goto _L1
_L1:
		Object result;
		result = processor.process(element, annotation, metaDepth);
		if (result == null)
			break MISSING_BLOCK_LABEL_243;
		if (processor.aggregates() && metaDepth == 0)
		{
			aggregatedResults.add(result);
			break MISSING_BLOCK_LABEL_243;
		}
		return result;
_L2:
		if (currentAnnotationType == containerType)
		{
			Annotation aannotation1[] = getRawAnnotationsFromContainer(element, annotation);
			int l = aannotation1.length;
			for (int i1 = 0; i1 < l; i1++)
			{
				Annotation contained = aannotation1[i1];
				Object result = processor.process(element, contained, metaDepth);
				if (result != null)
					aggregatedResults.add(result);
			}

		}
		j++;
		  goto _L3
		aannotation = annotations;
		i = aannotation.length;
		j = 0;
_L4:
		if (j >= i)
			break MISSING_BLOCK_LABEL_365;
		annotation = aannotation[j];
		currentAnnotationType = annotation.annotationType();
		if (AnnotationUtils.isInJavaLangAnnotationPackage(currentAnnotationType))
			break MISSING_BLOCK_LABEL_359;
		aannotation1 = ((Annotation []) (searchWithFindSemantics(((AnnotatedElement) (currentAnnotationType)), annotationType, annotationName, containerType, processor, visited, metaDepth + 1)));
		if (aannotation1 == null)
			break MISSING_BLOCK_LABEL_359;
		processor.postProcess(currentAnnotationType, annotation, aannotation1);
		if (processor.aggregates() && metaDepth == 0)
		{
			aggregatedResults.add(aannotation1);
			break MISSING_BLOCK_LABEL_359;
		}
		return aannotation1;
		j++;
		  goto _L4
		Method method;
		Object result;
		if (processor.aggregates())
			processor.getAggregatedResults().addAll(0, aggregatedResults);
		if (!(element instanceof Method))
			break MISSING_BLOCK_LABEL_585;
		method = (Method)element;
		Method resolvedMethod = BridgeMethodResolver.findBridgedMethod(method);
		result = searchWithFindSemantics(((AnnotatedElement) (resolvedMethod)), annotationType, annotationName, containerType, processor, visited, metaDepth);
		if (result != null)
			return result;
		Class ifcs[] = method.getDeclaringClass().getInterfaces();
		result = searchOnInterfaces(method, annotationType, annotationName, containerType, processor, visited, metaDepth, ifcs);
		if (result != null)
			return result;
		Class clazz = method.getDeclaringClass();
_L6:
		clazz = clazz.getSuperclass();
		if (clazz == null || java/lang/Object == clazz)
			break MISSING_BLOCK_LABEL_711;
		Method equivalentMethod = clazz.getDeclaredMethod(method.getName(), method.getParameterTypes());
		Method resolvedEquivalentMethod = BridgeMethodResolver.findBridgedMethod(equivalentMethod);
		result = searchWithFindSemantics(((AnnotatedElement) (resolvedEquivalentMethod)), annotationType, annotationName, containerType, processor, visited, metaDepth);
		if (result != null)
			return result;
		break MISSING_BLOCK_LABEL_553;
		NoSuchMethodException nosuchmethodexception;
		nosuchmethodexception;
		result = searchOnInterfaces(method, annotationType, annotationName, containerType, processor, visited, metaDepth, clazz.getInterfaces());
		if (result == null) goto _L6; else goto _L5
_L5:
		return result;
		Class clazz;
		Class aclass[];
		int k;
		if (!(element instanceof Class))
			break MISSING_BLOCK_LABEL_711;
		clazz = (Class)element;
		aclass = clazz.getInterfaces();
		result = aclass.length;
		k = 0;
_L7:
		if (k >= result)
			break MISSING_BLOCK_LABEL_657;
		Class ifc = aclass[k];
		nosuchmethodexception = ((NoSuchMethodException) (searchWithFindSemantics(((AnnotatedElement) (ifc)), annotationType, annotationName, containerType, processor, visited, metaDepth)));
		if (nosuchmethodexception != null)
			return nosuchmethodexception;
		k++;
		  goto _L7
		Class superclass = clazz.getSuperclass();
		if (superclass == null || java/lang/Object == superclass)
			break MISSING_BLOCK_LABEL_711;
		result = searchWithFindSemantics(((AnnotatedElement) (superclass)), annotationType, annotationName, containerType, processor, visited, metaDepth);
		if (result != null)
			return result;
		break MISSING_BLOCK_LABEL_711;
		Throwable ex;
		ex;
		AnnotationUtils.handleIntrospectionFailure(element, ex);
		return null;
	}

	private static Object searchOnInterfaces(Method method, Class annotationType, String annotationName, Class containerType, Processor processor, Set visited, int metaDepth, Class ifcs[])
	{
		Class aclass[];
		int i;
		int j;
		aclass = ifcs;
		i = aclass.length;
		j = 0;
_L3:
		if (j >= i) goto _L2; else goto _L1
_L1:
		Class iface;
		iface = aclass[j];
		if (!AnnotationUtils.isInterfaceWithAnnotatedMethods(iface))
			continue; /* Loop/switch isn't completed */
		Object result;
		Method equivalentMethod = iface.getMethod(method.getName(), method.getParameterTypes());
		result = searchWithFindSemantics(equivalentMethod, annotationType, annotationName, containerType, processor, visited, metaDepth);
		if (result != null)
			return result;
		continue; /* Loop/switch isn't completed */
		NoSuchMethodException nosuchmethodexception;
		nosuchmethodexception;
		j++;
		  goto _L3
_L2:
		return null;
	}

	private static Annotation[] getRawAnnotationsFromContainer(AnnotatedElement element, Annotation container)
	{
		return (Annotation[])(Annotation[])AnnotationUtils.getValue(container);
		Throwable ex;
		ex;
		AnnotationUtils.handleIntrospectionFailure(element, ex);
		return (Annotation[])EMPTY_ANNOTATION_ARRAY;
	}

	private static Class resolveContainerType(Class annotationType)
	{
		Class containerType = AnnotationUtils.resolveContainerAnnotationType(annotationType);
		if (containerType == null)
			throw new IllegalArgumentException((new StringBuilder()).append("Annotation type must be a repeatable annotation: failed to resolve container type for ").append(annotationType.getName()).toString());
		else
			return containerType;
	}

	private static void validateContainerType(Class annotationType, Class containerType)
	{
		try
		{
			Method method = containerType.getDeclaredMethod("value", new Class[0]);
			Class returnType = method.getReturnType();
			if (!returnType.isArray() || returnType.getComponentType() != annotationType)
			{
				String msg = String.format("Container type [%s] must declare a 'value' attribute for an array of type [%s]", new Object[] {
					containerType.getName(), annotationType.getName()
				});
				throw new AnnotationConfigurationException(msg);
			}
		}
		catch (Throwable ex)
		{
			AnnotationUtils.rethrowAnnotationConfigurationException(ex);
			String msg = String.format("Invalid declaration of container type [%s] for repeatable annotation [%s]", new Object[] {
				containerType.getName(), annotationType.getName()
			});
			throw new AnnotationConfigurationException(msg, ex);
		}
	}

	private static Set postProcessAndSynthesizeAggregatedResults(AnnotatedElement element, Class annotationType, List aggregatedResults)
	{
		Set annotations = new LinkedHashSet();
		AnnotationAttributes attributes;
		for (Iterator iterator = aggregatedResults.iterator(); iterator.hasNext(); annotations.add(AnnotationUtils.synthesizeAnnotation(attributes, annotationType, element)))
		{
			attributes = (AnnotationAttributes)iterator.next();
			AnnotationUtils.postProcessAnnotationAttributes(element, attributes, false, false);
		}

		return annotations;
	}


}
