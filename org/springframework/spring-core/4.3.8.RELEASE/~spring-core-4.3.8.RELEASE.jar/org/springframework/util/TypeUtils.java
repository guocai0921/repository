// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   TypeUtils.java

package org.springframework.util;

import java.lang.reflect.*;

// Referenced classes of package org.springframework.util:
//			Assert, ClassUtils

public abstract class TypeUtils
{

	public TypeUtils()
	{
	}

	public static boolean isAssignable(Type lhsType, Type rhsType)
	{
		Assert.notNull(lhsType, "Left-hand side type must not be null");
		Assert.notNull(rhsType, "Right-hand side type must not be null");
		if (lhsType.equals(rhsType) || java/lang/Object == lhsType)
			return true;
		if (lhsType instanceof Class)
		{
			Class lhsClass = (Class)lhsType;
			if (rhsType instanceof Class)
				return ClassUtils.isAssignable(lhsClass, (Class)rhsType);
			if (rhsType instanceof ParameterizedType)
			{
				Type rhsRaw = ((ParameterizedType)rhsType).getRawType();
				if (rhsRaw instanceof Class)
					return ClassUtils.isAssignable(lhsClass, (Class)rhsRaw);
			} else
			if (lhsClass.isArray() && (rhsType instanceof GenericArrayType))
			{
				Type rhsComponent = ((GenericArrayType)rhsType).getGenericComponentType();
				return isAssignable(((Type) (lhsClass.getComponentType())), rhsComponent);
			}
		}
		if (lhsType instanceof ParameterizedType)
			if (rhsType instanceof Class)
			{
				Type lhsRaw = ((ParameterizedType)lhsType).getRawType();
				if (lhsRaw instanceof Class)
					return ClassUtils.isAssignable((Class)lhsRaw, (Class)rhsType);
			} else
			if (rhsType instanceof ParameterizedType)
				return isAssignable((ParameterizedType)lhsType, (ParameterizedType)rhsType);
		if (lhsType instanceof GenericArrayType)
		{
			Type lhsComponent = ((GenericArrayType)lhsType).getGenericComponentType();
			if (rhsType instanceof Class)
			{
				Class rhsClass = (Class)rhsType;
				if (rhsClass.isArray())
					return isAssignable(lhsComponent, ((Type) (rhsClass.getComponentType())));
			} else
			if (rhsType instanceof GenericArrayType)
			{
				Type rhsComponent = ((GenericArrayType)rhsType).getGenericComponentType();
				return isAssignable(lhsComponent, rhsComponent);
			}
		}
		if (lhsType instanceof WildcardType)
			return isAssignable((WildcardType)lhsType, rhsType);
		else
			return false;
	}

	private static boolean isAssignable(ParameterizedType lhsType, ParameterizedType rhsType)
	{
		if (lhsType.equals(rhsType))
			return true;
		Type lhsTypeArguments[] = lhsType.getActualTypeArguments();
		Type rhsTypeArguments[] = rhsType.getActualTypeArguments();
		if (lhsTypeArguments.length != rhsTypeArguments.length)
			return false;
		int size = lhsTypeArguments.length;
		for (int i = 0; i < size; i++)
		{
			Type lhsArg = lhsTypeArguments[i];
			Type rhsArg = rhsTypeArguments[i];
			if (!lhsArg.equals(rhsArg) && (!(lhsArg instanceof WildcardType) || !isAssignable((WildcardType)lhsArg, rhsArg)))
				return false;
		}

		return true;
	}

	private static boolean isAssignable(WildcardType lhsType, Type rhsType)
	{
		Type lUpperBounds[] = lhsType.getUpperBounds();
		if (lUpperBounds.length == 0)
			lUpperBounds = (new Type[] {
				java/lang/Object
			});
		Type lLowerBounds[] = lhsType.getLowerBounds();
		if (lLowerBounds.length == 0)
			lLowerBounds = (new Type[] {
				null
			});
		if (rhsType instanceof WildcardType)
		{
			WildcardType rhsWcType = (WildcardType)rhsType;
			Type rUpperBounds[] = rhsWcType.getUpperBounds();
			if (rUpperBounds.length == 0)
				rUpperBounds = (new Type[] {
					java/lang/Object
				});
			Type rLowerBounds[] = rhsWcType.getLowerBounds();
			if (rLowerBounds.length == 0)
				rLowerBounds = (new Type[] {
					null
				});
			Type atype1[] = lUpperBounds;
			int l = atype1.length;
			for (int i1 = 0; i1 < l; i1++)
			{
				Type lBound = atype1[i1];
				Type atype2[] = rUpperBounds;
				int k1 = atype2.length;
				for (int i2 = 0; i2 < k1; i2++)
				{
					Type rBound = atype2[i2];
					if (!isAssignableBound(lBound, rBound))
						return false;
				}

				atype2 = rLowerBounds;
				k1 = atype2.length;
				for (int j2 = 0; j2 < k1; j2++)
				{
					Type rBound = atype2[j2];
					if (!isAssignableBound(lBound, rBound))
						return false;
				}

			}

			atype1 = lLowerBounds;
			l = atype1.length;
			for (int j1 = 0; j1 < l; j1++)
			{
				Type lBound = atype1[j1];
				Type atype3[] = rUpperBounds;
				int l1 = atype3.length;
				for (int k2 = 0; k2 < l1; k2++)
				{
					Type rBound = atype3[k2];
					if (!isAssignableBound(rBound, lBound))
						return false;
				}

				atype3 = rLowerBounds;
				l1 = atype3.length;
				for (int l2 = 0; l2 < l1; l2++)
				{
					Type rBound = atype3[l2];
					if (!isAssignableBound(rBound, lBound))
						return false;
				}

			}

		} else
		{
			Type atype[] = lUpperBounds;
			int i = atype.length;
			for (int j = 0; j < i; j++)
			{
				Type lBound = atype[j];
				if (!isAssignableBound(lBound, rhsType))
					return false;
			}

			atype = lLowerBounds;
			i = atype.length;
			for (int k = 0; k < i; k++)
			{
				Type lBound = atype[k];
				if (!isAssignableBound(rhsType, lBound))
					return false;
			}

		}
		return true;
	}

	public static boolean isAssignableBound(Type lhsType, Type rhsType)
	{
		if (rhsType == null)
			return true;
		if (lhsType == null)
			return false;
		else
			return isAssignable(lhsType, rhsType);
	}
}
