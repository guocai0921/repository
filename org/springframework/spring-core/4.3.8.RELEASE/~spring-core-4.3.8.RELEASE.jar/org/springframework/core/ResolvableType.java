// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ResolvableType.java

package org.springframework.core;

import java.io.Serializable;
import java.lang.reflect.*;
import java.util.*;
import org.springframework.util.*;

// Referenced classes of package org.springframework.core:
//			ResolvableTypeProvider, MethodParameter, SerializableTypeWrapper

public class ResolvableType
	implements Serializable
{
	private static class WildcardBounds
	{
		static final class Kind extends Enum
		{

			public static final Kind UPPER;
			public static final Kind LOWER;
			private static final Kind $VALUES[];

			public static Kind[] values()
			{
				return (Kind[])$VALUES.clone();
			}

			public static Kind valueOf(String name)
			{
				return (Kind)Enum.valueOf(org/springframework/core/ResolvableType$WildcardBounds$Kind, name);
			}

			static 
			{
				UPPER = new Kind("UPPER", 0);
				LOWER = new Kind("LOWER", 1);
				$VALUES = (new Kind[] {
					UPPER, LOWER
				});
			}

			private Kind(String s, int i)
			{
				super(s, i);
			}
		}


		private final Kind kind;
		private final ResolvableType bounds[];

		public boolean isSameKind(WildcardBounds bounds)
		{
			return kind == bounds.kind;
		}

		public transient boolean isAssignableFrom(ResolvableType types[])
		{
			ResolvableType aresolvabletype[] = bounds;
			int i = aresolvabletype.length;
			for (int j = 0; j < i; j++)
			{
				ResolvableType bound = aresolvabletype[j];
				ResolvableType aresolvabletype1[] = types;
				int k = aresolvabletype1.length;
				for (int l = 0; l < k; l++)
				{
					ResolvableType type = aresolvabletype1[l];
					if (!isAssignable(bound, type))
						return false;
				}

			}

			return true;
		}

		private boolean isAssignable(ResolvableType source, ResolvableType from)
		{
			return kind != Kind.UPPER ? from.isAssignableFrom(source) : source.isAssignableFrom(from);
		}

		public ResolvableType[] getBounds()
		{
			return bounds;
		}

		public static WildcardBounds get(ResolvableType type)
		{
			ResolvableType resolveToWildcard;
			for (resolveToWildcard = type; !(resolveToWildcard.getType() instanceof WildcardType); resolveToWildcard = resolveToWildcard.resolveType())
				if (resolveToWildcard == ResolvableType.NONE)
					return null;

			WildcardType wildcardType = (WildcardType)resolveToWildcard.type;
			Kind boundsType = wildcardType.getLowerBounds().length <= 0 ? Kind.UPPER : Kind.LOWER;
			Type bounds[] = boundsType != Kind.UPPER ? wildcardType.getLowerBounds() : wildcardType.getUpperBounds();
			ResolvableType resolvableBounds[] = new ResolvableType[bounds.length];
			for (int i = 0; i < bounds.length; i++)
				resolvableBounds[i] = ResolvableType.forType(bounds[i], type.variableResolver);

			return new WildcardBounds(boundsType, resolvableBounds);
		}

		public WildcardBounds(Kind kind, ResolvableType bounds[])
		{
			this.kind = kind;
			this.bounds = bounds;
		}
	}

	private static final class SyntheticParameterizedType
		implements ParameterizedType, Serializable
	{

		private final Type rawType;
		private final Type typeArguments[];

		public Type getOwnerType()
		{
			return null;
		}

		public Type getRawType()
		{
			return rawType;
		}

		public Type[] getActualTypeArguments()
		{
			return typeArguments;
		}

		public boolean equals(Object other)
		{
			if (this == other)
				return true;
			if (!(other instanceof ParameterizedType))
			{
				return false;
			} else
			{
				ParameterizedType otherType = (ParameterizedType)other;
				return otherType.getOwnerType() == null && rawType.equals(otherType.getRawType()) && Arrays.equals(typeArguments, otherType.getActualTypeArguments());
			}
		}

		public int hashCode()
		{
			return rawType.hashCode() * 31 + Arrays.hashCode(typeArguments);
		}

		public SyntheticParameterizedType(Type rawType, Type typeArguments[])
		{
			this.rawType = rawType;
			this.typeArguments = typeArguments;
		}
	}

	private static class TypeVariablesVariableResolver
		implements VariableResolver
	{

		private final TypeVariable variables[];
		private final ResolvableType generics[];

		public ResolvableType resolveVariable(TypeVariable variable)
		{
			for (int i = 0; i < variables.length; i++)
				if (((TypeVariable)SerializableTypeWrapper.unwrap(variables[i])).equals(SerializableTypeWrapper.unwrap(variable)))
					return generics[i];

			return null;
		}

		public Object getSource()
		{
			return generics;
		}

		public TypeVariablesVariableResolver(TypeVariable variables[], ResolvableType generics[])
		{
			this.variables = variables;
			this.generics = generics;
		}
	}

	private class DefaultVariableResolver
		implements VariableResolver
	{

		final ResolvableType this$0;

		public ResolvableType resolveVariable(TypeVariable variable)
		{
			return ResolvableType.this.resolveVariable(variable);
		}

		public Object getSource()
		{
			return ResolvableType.this;
		}

		private DefaultVariableResolver()
		{
			this$0 = ResolvableType.this;
			super();
		}

	}

	static interface VariableResolver
		extends Serializable
	{

		public abstract Object getSource();

		public abstract ResolvableType resolveVariable(TypeVariable typevariable);
	}


	public static final ResolvableType NONE = new ResolvableType(null, null, null, Integer.valueOf(0));
	private static final ResolvableType EMPTY_TYPES_ARRAY[] = new ResolvableType[0];
	private static final ConcurrentReferenceHashMap cache = new ConcurrentReferenceHashMap(256);
	private final Type type;
	private final SerializableTypeWrapper.TypeProvider typeProvider;
	private final VariableResolver variableResolver;
	private final ResolvableType componentType;
	private final Class resolved;
	private final Integer hash;
	private ResolvableType superType;
	private ResolvableType interfaces[];
	private ResolvableType generics[];

	private ResolvableType(Type type, SerializableTypeWrapper.TypeProvider typeProvider, VariableResolver variableResolver)
	{
		this.type = type;
		this.typeProvider = typeProvider;
		this.variableResolver = variableResolver;
		componentType = null;
		resolved = null;
		hash = Integer.valueOf(calculateHashCode());
	}

	private ResolvableType(Type type, SerializableTypeWrapper.TypeProvider typeProvider, VariableResolver variableResolver, Integer hash)
	{
		this.type = type;
		this.typeProvider = typeProvider;
		this.variableResolver = variableResolver;
		componentType = null;
		resolved = resolveClass();
		this.hash = hash;
	}

	private ResolvableType(Type type, SerializableTypeWrapper.TypeProvider typeProvider, VariableResolver variableResolver, ResolvableType componentType)
	{
		this.type = type;
		this.typeProvider = typeProvider;
		this.variableResolver = variableResolver;
		this.componentType = componentType;
		resolved = resolveClass();
		hash = null;
	}

	private ResolvableType(Class clazz)
	{
		resolved = clazz == null ? java/lang/Object : clazz;
		type = resolved;
		typeProvider = null;
		variableResolver = null;
		componentType = null;
		hash = null;
	}

	public Type getType()
	{
		return SerializableTypeWrapper.unwrap(type);
	}

	public Class getRawClass()
	{
		if (type == resolved)
			return resolved;
		Type rawType = type;
		if (rawType instanceof ParameterizedType)
			rawType = ((ParameterizedType)rawType).getRawType();
		return (rawType instanceof Class) ? (Class)rawType : null;
	}

	public Object getSource()
	{
		Object source = typeProvider == null ? null : typeProvider.getSource();
		return source == null ? type : source;
	}

	public boolean isInstance(Object obj)
	{
		return obj != null && isAssignableFrom(obj.getClass());
	}

	public boolean isAssignableFrom(Class other)
	{
		return isAssignableFrom(forClass(other), null);
	}

	public boolean isAssignableFrom(ResolvableType other)
	{
		return isAssignableFrom(other, null);
	}

	private boolean isAssignableFrom(ResolvableType other, Map matchedBefore)
	{
		Assert.notNull(other, "ResolvableType must not be null");
		if (this == NONE || other == NONE)
			return false;
		if (isArray())
			return other.isArray() && getComponentType().isAssignableFrom(other.getComponentType());
		if (matchedBefore != null && matchedBefore.get(type) == other.type)
			return true;
		WildcardBounds ourBounds = WildcardBounds.get(this);
		WildcardBounds typeBounds = WildcardBounds.get(other);
		if (typeBounds != null)
			return ourBounds != null && ourBounds.isSameKind(typeBounds) && ourBounds.isAssignableFrom(typeBounds.getBounds());
		if (ourBounds != null)
			return ourBounds.isAssignableFrom(new ResolvableType[] {
				other
			});
		boolean exactMatch = matchedBefore != null;
		boolean checkGenerics = true;
		Class ourResolved = null;
		if (type instanceof TypeVariable)
		{
			TypeVariable variable = (TypeVariable)type;
			if (variableResolver != null)
			{
				ResolvableType resolved = variableResolver.resolveVariable(variable);
				if (resolved != null)
					ourResolved = resolved.resolve();
			}
			if (ourResolved == null && other.variableResolver != null)
			{
				ResolvableType resolved = other.variableResolver.resolveVariable(variable);
				if (resolved != null)
				{
					ourResolved = resolved.resolve();
					checkGenerics = false;
				}
			}
			if (ourResolved == null)
				exactMatch = false;
		}
		if (ourResolved == null)
			ourResolved = resolve(java/lang/Object);
		Class otherResolved = other.resolve(java/lang/Object);
		if (exactMatch ? !ourResolved.equals(otherResolved) : !ClassUtils.isAssignable(ourResolved, otherResolved))
			return false;
		if (checkGenerics)
		{
			ResolvableType ourGenerics[] = getGenerics();
			ResolvableType typeGenerics[] = other.as(ourResolved).getGenerics();
			if (ourGenerics.length != typeGenerics.length)
				return false;
			if (matchedBefore == null)
				matchedBefore = new IdentityHashMap(1);
			matchedBefore.put(type, other.type);
			for (int i = 0; i < ourGenerics.length; i++)
				if (!ourGenerics[i].isAssignableFrom(typeGenerics[i], matchedBefore))
					return false;

		}
		return true;
	}

	public boolean isArray()
	{
		if (this == NONE)
			return false;
		else
			return (type instanceof Class) && ((Class)type).isArray() || (type instanceof GenericArrayType) || resolveType().isArray();
	}

	public ResolvableType getComponentType()
	{
		if (this == NONE)
			return NONE;
		if (this.componentType != null)
			return this.componentType;
		if (type instanceof Class)
		{
			Class componentType = ((Class)type).getComponentType();
			return forType(componentType, variableResolver);
		}
		if (type instanceof GenericArrayType)
			return forType(((GenericArrayType)type).getGenericComponentType(), variableResolver);
		else
			return resolveType().getComponentType();
	}

	public ResolvableType asCollection()
	{
		return as(java/util/Collection);
	}

	public ResolvableType asMap()
	{
		return as(java/util/Map);
	}

	public ResolvableType as(Class type)
	{
		if (this == NONE)
			return NONE;
		if (ObjectUtils.nullSafeEquals(resolve(), type))
			return this;
		ResolvableType aresolvabletype[] = getInterfaces();
		int i = aresolvabletype.length;
		for (int j = 0; j < i; j++)
		{
			ResolvableType interfaceType = aresolvabletype[j];
			ResolvableType interfaceAsType = interfaceType.as(type);
			if (interfaceAsType != NONE)
				return interfaceAsType;
		}

		return getSuperType().as(type);
	}

	public ResolvableType getSuperType()
	{
		Class resolved = resolve();
		if (resolved == null || resolved.getGenericSuperclass() == null)
			return NONE;
		if (superType == null)
			superType = forType(SerializableTypeWrapper.forGenericSuperclass(resolved), asVariableResolver());
		return superType;
	}

	public ResolvableType[] getInterfaces()
	{
		Class resolved = resolve();
		if (resolved == null || ObjectUtils.isEmpty(resolved.getGenericInterfaces()))
			return EMPTY_TYPES_ARRAY;
		if (interfaces == null)
			interfaces = forTypes(SerializableTypeWrapper.forGenericInterfaces(resolved), asVariableResolver());
		return interfaces;
	}

	public boolean hasGenerics()
	{
		return getGenerics().length > 0;
	}

	boolean isEntirelyUnresolvable()
	{
		if (this == NONE)
			return false;
		ResolvableType generics[] = getGenerics();
		ResolvableType aresolvabletype[] = generics;
		int i = aresolvabletype.length;
		for (int j = 0; j < i; j++)
		{
			ResolvableType generic = aresolvabletype[j];
			if (!generic.isUnresolvableTypeVariable() && !generic.isWildcardWithoutBounds())
				return false;
		}

		return true;
	}

	public boolean hasUnresolvableGenerics()
	{
		if (this == NONE)
			return false;
		ResolvableType generics[] = getGenerics();
		ResolvableType aresolvabletype[] = generics;
		int i = aresolvabletype.length;
		for (int j = 0; j < i; j++)
		{
			ResolvableType generic = aresolvabletype[j];
			if (generic.isUnresolvableTypeVariable() || generic.isWildcardWithoutBounds())
				return true;
		}

		Class resolved = resolve();
		if (resolved != null)
		{
			Type atype[] = resolved.getGenericInterfaces();
			int k = atype.length;
			for (int l = 0; l < k; l++)
			{
				Type genericInterface = atype[l];
				if ((genericInterface instanceof Class) && forClass((Class)genericInterface).hasGenerics())
					return true;
			}

			return getSuperType().hasUnresolvableGenerics();
		} else
		{
			return false;
		}
	}

	private boolean isUnresolvableTypeVariable()
	{
		if (type instanceof TypeVariable)
		{
			if (variableResolver == null)
				return true;
			TypeVariable variable = (TypeVariable)type;
			ResolvableType resolved = variableResolver.resolveVariable(variable);
			if (resolved == null || resolved.isUnresolvableTypeVariable())
				return true;
		}
		return false;
	}

	private boolean isWildcardWithoutBounds()
	{
		if (type instanceof WildcardType)
		{
			WildcardType wt = (WildcardType)type;
			if (wt.getLowerBounds().length == 0)
			{
				Type upperBounds[] = wt.getUpperBounds();
				if (upperBounds.length == 0 || upperBounds.length == 1 && java/lang/Object == upperBounds[0])
					return true;
			}
		}
		return false;
	}

	public ResolvableType getNested(int nestingLevel)
	{
		return getNested(nestingLevel, null);
	}

	public ResolvableType getNested(int nestingLevel, Map typeIndexesPerLevel)
	{
		ResolvableType result = this;
		for (int i = 2; i <= nestingLevel; i++)
		{
			if (result.isArray())
			{
				result = result.getComponentType();
				continue;
			}
			for (; result != NONE && !result.hasGenerics(); result = result.getSuperType());
			Integer index = typeIndexesPerLevel == null ? null : (Integer)typeIndexesPerLevel.get(Integer.valueOf(i));
			index = Integer.valueOf(index != null ? index.intValue() : result.getGenerics().length - 1);
			result = result.getGeneric(new int[] {
				index.intValue()
			});
		}

		return result;
	}

	public transient ResolvableType getGeneric(int indexes[])
	{
		ResolvableType generics[] = getGenerics();
		if (indexes == null || indexes.length == 0)
			return generics.length != 0 ? generics[0] : NONE;
		ResolvableType generic = this;
		int ai[] = indexes;
		int i = ai.length;
		for (int j = 0; j < i; j++)
		{
			int index = ai[j];
			generics = generic.getGenerics();
			if (index < 0 || index >= generics.length)
				return NONE;
			generic = generics[index];
		}

		return generic;
	}

	public ResolvableType[] getGenerics()
	{
		if (this == NONE)
			return EMPTY_TYPES_ARRAY;
		if (this.generics == null)
			if (type instanceof Class)
			{
				Class typeClass = (Class)type;
				this.generics = forTypes(SerializableTypeWrapper.forTypeParameters(typeClass), variableResolver);
			} else
			if (type instanceof ParameterizedType)
			{
				Type actualTypeArguments[] = ((ParameterizedType)type).getActualTypeArguments();
				ResolvableType generics[] = new ResolvableType[actualTypeArguments.length];
				for (int i = 0; i < actualTypeArguments.length; i++)
					generics[i] = forType(actualTypeArguments[i], variableResolver);

				this.generics = generics;
			} else
			{
				this.generics = resolveType().getGenerics();
			}
		return this.generics;
	}

	public Class[] resolveGenerics()
	{
		return resolveGenerics(null);
	}

	public Class[] resolveGenerics(Class fallback)
	{
		ResolvableType generics[] = getGenerics();
		Class resolvedGenerics[] = new Class[generics.length];
		for (int i = 0; i < generics.length; i++)
			resolvedGenerics[i] = generics[i].resolve(fallback);

		return resolvedGenerics;
	}

	public transient Class resolveGeneric(int indexes[])
	{
		return getGeneric(indexes).resolve();
	}

	public Class resolve()
	{
		return resolve(null);
	}

	public Class resolve(Class fallback)
	{
		return resolved == null ? fallback : resolved;
	}

	private Class resolveClass()
	{
		if ((type instanceof Class) || type == null)
			return (Class)type;
		if (type instanceof GenericArrayType)
		{
			Class resolvedComponent = getComponentType().resolve();
			return resolvedComponent == null ? null : Array.newInstance(resolvedComponent, 0).getClass();
		} else
		{
			return resolveType().resolve();
		}
	}

	ResolvableType resolveType()
	{
		if (type instanceof ParameterizedType)
			return forType(((ParameterizedType)type).getRawType(), variableResolver);
		if (type instanceof WildcardType)
		{
			Type resolved = resolveBounds(((WildcardType)type).getUpperBounds());
			if (resolved == null)
				resolved = resolveBounds(((WildcardType)type).getLowerBounds());
			return forType(resolved, variableResolver);
		}
		if (type instanceof TypeVariable)
		{
			TypeVariable variable = (TypeVariable)type;
			if (variableResolver != null)
			{
				ResolvableType resolved = variableResolver.resolveVariable(variable);
				if (resolved != null)
					return resolved;
			}
			return forType(resolveBounds(variable.getBounds()), variableResolver);
		} else
		{
			return NONE;
		}
	}

	private Type resolveBounds(Type bounds[])
	{
		if (ObjectUtils.isEmpty(bounds) || java/lang/Object == bounds[0])
			return null;
		else
			return bounds[0];
	}

	private ResolvableType resolveVariable(TypeVariable variable)
	{
		if (type instanceof TypeVariable)
			return resolveType().resolveVariable(variable);
		if (type instanceof ParameterizedType)
		{
			ParameterizedType parameterizedType = (ParameterizedType)type;
			TypeVariable variables[] = resolve().getTypeParameters();
			for (int i = 0; i < variables.length; i++)
				if (ObjectUtils.nullSafeEquals(variables[i].getName(), variable.getName()))
				{
					Type actualType = parameterizedType.getActualTypeArguments()[i];
					return forType(actualType, variableResolver);
				}

			if (parameterizedType.getOwnerType() != null)
				return forType(parameterizedType.getOwnerType(), variableResolver).resolveVariable(variable);
		}
		if (variableResolver != null)
			return variableResolver.resolveVariable(variable);
		else
			return null;
	}

	public boolean equals(Object other)
	{
		if (this == other)
			return true;
		if (!(other instanceof ResolvableType))
			return false;
		ResolvableType otherType = (ResolvableType)other;
		if (!ObjectUtils.nullSafeEquals(type, otherType.type))
			return false;
		if (typeProvider != otherType.typeProvider && (typeProvider == null || otherType.typeProvider == null || !ObjectUtils.nullSafeEquals(typeProvider.getType(), otherType.typeProvider.getType())))
			return false;
		if (variableResolver != otherType.variableResolver && (variableResolver == null || otherType.variableResolver == null || !ObjectUtils.nullSafeEquals(variableResolver.getSource(), otherType.variableResolver.getSource())))
			return false;
		return ObjectUtils.nullSafeEquals(componentType, otherType.componentType);
	}

	public int hashCode()
	{
		return hash == null ? calculateHashCode() : hash.intValue();
	}

	private int calculateHashCode()
	{
		int hashCode = ObjectUtils.nullSafeHashCode(type);
		if (typeProvider != null)
			hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode(typeProvider.getType());
		if (variableResolver != null)
			hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode(variableResolver.getSource());
		if (componentType != null)
			hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode(componentType);
		return hashCode;
	}

	VariableResolver asVariableResolver()
	{
		if (this == NONE)
			return null;
		else
			return new DefaultVariableResolver();
	}

	private Object readResolve()
	{
		return type != null ? this : NONE;
	}

	public String toString()
	{
		if (isArray())
			return (new StringBuilder()).append(getComponentType()).append("[]").toString();
		if (resolved == null)
			return "?";
		if (type instanceof TypeVariable)
		{
			TypeVariable variable = (TypeVariable)type;
			if (variableResolver == null || variableResolver.resolveVariable(variable) == null)
				return "?";
		}
		StringBuilder result = new StringBuilder(resolved.getName());
		if (hasGenerics())
		{
			result.append('<');
			result.append(StringUtils.arrayToDelimitedString(getGenerics(), ", "));
			result.append('>');
		}
		return result.toString();
	}

	public static ResolvableType forClass(Class clazz)
	{
		return new ResolvableType(clazz);
	}

	public static ResolvableType forRawClass(Class clazz)
	{
		return new ResolvableType(clazz) {

			public ResolvableType[] getGenerics()
			{
				return ResolvableType.EMPTY_TYPES_ARRAY;
			}

			public boolean isAssignableFrom(Class other)
			{
				return ClassUtils.isAssignable(getRawClass(), other);
			}

			public boolean isAssignableFrom(ResolvableType other)
			{
				Class otherClass = other.getRawClass();
				return otherClass != null && ClassUtils.isAssignable(getRawClass(), otherClass);
			}

		};
	}

	public static ResolvableType forClass(Class baseType, Class implementationClass)
	{
		Assert.notNull(baseType, "Base type must not be null");
		ResolvableType asType = forType(implementationClass).as(baseType);
		return asType != NONE ? asType : forType(baseType);
	}

	public static transient ResolvableType forClassWithGenerics(Class clazz, Class generics[])
	{
		Assert.notNull(clazz, "Class must not be null");
		Assert.notNull(generics, "Generics array must not be null");
		ResolvableType resolvableGenerics[] = new ResolvableType[generics.length];
		for (int i = 0; i < generics.length; i++)
			resolvableGenerics[i] = forClass(generics[i]);

		return forClassWithGenerics(clazz, resolvableGenerics);
	}

	public static transient ResolvableType forClassWithGenerics(Class clazz, ResolvableType generics[])
	{
		Assert.notNull(clazz, "Class must not be null");
		Assert.notNull(generics, "Generics array must not be null");
		TypeVariable variables[] = clazz.getTypeParameters();
		Assert.isTrue(variables.length == generics.length, "Mismatched number of generics specified");
		Type arguments[] = new Type[generics.length];
		for (int i = 0; i < generics.length; i++)
		{
			ResolvableType generic = generics[i];
			Type argument = generic == null ? null : generic.getType();
			arguments[i] = ((Type) (argument == null ? ((Type) (variables[i])) : argument));
		}

		ParameterizedType syntheticType = new SyntheticParameterizedType(clazz, arguments);
		return forType(syntheticType, new TypeVariablesVariableResolver(variables, generics));
	}

	public static ResolvableType forInstance(Object instance)
	{
		Assert.notNull(instance, "Instance must not be null");
		if (instance instanceof ResolvableTypeProvider)
		{
			ResolvableType type = ((ResolvableTypeProvider)instance).getResolvableType();
			if (type != null)
				return type;
		}
		return forClass(instance.getClass());
	}

	public static ResolvableType forField(Field field)
	{
		Assert.notNull(field, "Field must not be null");
		return forType(null, new SerializableTypeWrapper.FieldTypeProvider(field), null);
	}

	public static ResolvableType forField(Field field, Class implementationClass)
	{
		Assert.notNull(field, "Field must not be null");
		ResolvableType owner = forType(implementationClass).as(field.getDeclaringClass());
		return forType(null, new SerializableTypeWrapper.FieldTypeProvider(field), owner.asVariableResolver());
	}

	public static ResolvableType forField(Field field, ResolvableType implementationType)
	{
		Assert.notNull(field, "Field must not be null");
		ResolvableType owner = implementationType == null ? NONE : implementationType;
		owner = owner.as(field.getDeclaringClass());
		return forType(null, new SerializableTypeWrapper.FieldTypeProvider(field), owner.asVariableResolver());
	}

	public static ResolvableType forField(Field field, int nestingLevel)
	{
		Assert.notNull(field, "Field must not be null");
		return forType(null, new SerializableTypeWrapper.FieldTypeProvider(field), null).getNested(nestingLevel);
	}

	public static ResolvableType forField(Field field, int nestingLevel, Class implementationClass)
	{
		Assert.notNull(field, "Field must not be null");
		ResolvableType owner = forType(implementationClass).as(field.getDeclaringClass());
		return forType(null, new SerializableTypeWrapper.FieldTypeProvider(field), owner.asVariableResolver()).getNested(nestingLevel);
	}

	public static ResolvableType forConstructorParameter(Constructor constructor, int parameterIndex)
	{
		Assert.notNull(constructor, "Constructor must not be null");
		return forMethodParameter(new MethodParameter(constructor, parameterIndex));
	}

	public static ResolvableType forConstructorParameter(Constructor constructor, int parameterIndex, Class implementationClass)
	{
		Assert.notNull(constructor, "Constructor must not be null");
		MethodParameter methodParameter = new MethodParameter(constructor, parameterIndex);
		methodParameter.setContainingClass(implementationClass);
		return forMethodParameter(methodParameter);
	}

	public static ResolvableType forMethodReturnType(Method method)
	{
		Assert.notNull(method, "Method must not be null");
		return forMethodParameter(new MethodParameter(method, -1));
	}

	public static ResolvableType forMethodReturnType(Method method, Class implementationClass)
	{
		Assert.notNull(method, "Method must not be null");
		MethodParameter methodParameter = new MethodParameter(method, -1);
		methodParameter.setContainingClass(implementationClass);
		return forMethodParameter(methodParameter);
	}

	public static ResolvableType forMethodParameter(Method method, int parameterIndex)
	{
		Assert.notNull(method, "Method must not be null");
		return forMethodParameter(new MethodParameter(method, parameterIndex));
	}

	public static ResolvableType forMethodParameter(Method method, int parameterIndex, Class implementationClass)
	{
		Assert.notNull(method, "Method must not be null");
		MethodParameter methodParameter = new MethodParameter(method, parameterIndex);
		methodParameter.setContainingClass(implementationClass);
		return forMethodParameter(methodParameter);
	}

	public static ResolvableType forMethodParameter(MethodParameter methodParameter)
	{
		return forMethodParameter(methodParameter, (Type)null);
	}

	public static ResolvableType forMethodParameter(MethodParameter methodParameter, ResolvableType implementationType)
	{
		Assert.notNull(methodParameter, "MethodParameter must not be null");
		implementationType = implementationType == null ? forType(methodParameter.getContainingClass()) : implementationType;
		ResolvableType owner = implementationType.as(methodParameter.getDeclaringClass());
		return forType(null, new SerializableTypeWrapper.MethodParameterTypeProvider(methodParameter), owner.asVariableResolver()).getNested(methodParameter.getNestingLevel(), methodParameter.typeIndexesPerLevel);
	}

	public static ResolvableType forMethodParameter(MethodParameter methodParameter, Type targetType)
	{
		Assert.notNull(methodParameter, "MethodParameter must not be null");
		ResolvableType owner = forType(methodParameter.getContainingClass()).as(methodParameter.getDeclaringClass());
		return forType(targetType, new SerializableTypeWrapper.MethodParameterTypeProvider(methodParameter), owner.asVariableResolver()).getNested(methodParameter.getNestingLevel(), methodParameter.typeIndexesPerLevel);
	}

	static void resolveMethodParameter(MethodParameter methodParameter)
	{
		Assert.notNull(methodParameter, "MethodParameter must not be null");
		ResolvableType owner = forType(methodParameter.getContainingClass()).as(methodParameter.getDeclaringClass());
		methodParameter.setParameterType(forType(null, new SerializableTypeWrapper.MethodParameterTypeProvider(methodParameter), owner.asVariableResolver()).resolve());
	}

	public static ResolvableType forArrayComponent(ResolvableType componentType)
	{
		Assert.notNull(componentType, "Component type must not be null");
		Class arrayClass = Array.newInstance(componentType.resolve(), 0).getClass();
		return new ResolvableType(arrayClass, null, null, componentType);
	}

	private static ResolvableType[] forTypes(Type types[], VariableResolver owner)
	{
		ResolvableType result[] = new ResolvableType[types.length];
		for (int i = 0; i < types.length; i++)
			result[i] = forType(types[i], owner);

		return result;
	}

	public static ResolvableType forType(Type type)
	{
		return forType(type, null, null);
	}

	public static ResolvableType forType(Type type, ResolvableType owner)
	{
		VariableResolver variableResolver = null;
		if (owner != null)
			variableResolver = owner.asVariableResolver();
		return forType(type, variableResolver);
	}

	static ResolvableType forType(Type type, VariableResolver variableResolver)
	{
		return forType(type, null, variableResolver);
	}

	static ResolvableType forType(Type type, SerializableTypeWrapper.TypeProvider typeProvider, VariableResolver variableResolver)
	{
		if (type == null && typeProvider != null)
			type = SerializableTypeWrapper.forTypeProvider(typeProvider);
		if (type == null)
			return NONE;
		if (type instanceof Class)
			return new ResolvableType(type, typeProvider, variableResolver, (ResolvableType)null);
		cache.purgeUnreferencedEntries();
		ResolvableType key = new ResolvableType(type, typeProvider, variableResolver);
		ResolvableType resolvableType = (ResolvableType)cache.get(key);
		if (resolvableType == null)
		{
			resolvableType = new ResolvableType(type, typeProvider, variableResolver, key.hash);
			cache.put(resolvableType, resolvableType);
		}
		return resolvableType;
	}

	public static void clearCache()
	{
		cache.clear();
	}






}
