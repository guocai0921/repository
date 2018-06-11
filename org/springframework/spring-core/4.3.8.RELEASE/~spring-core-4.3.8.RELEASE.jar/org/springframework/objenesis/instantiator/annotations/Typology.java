// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Typology.java

package org.springframework.objenesis.instantiator.annotations;


public final class Typology extends Enum
{

	public static final Typology STANDARD;
	public static final Typology SERIALIZATION;
	public static final Typology NOT_COMPLIANT;
	public static final Typology UNKNOWN;
	private static final Typology $VALUES[];

	public static Typology[] values()
	{
		return (Typology[])$VALUES.clone();
	}

	public static Typology valueOf(String name)
	{
		return (Typology)Enum.valueOf(org/springframework/objenesis/instantiator/annotations/Typology, name);
	}

	private Typology(String s, int i)
	{
		super(s, i);
	}

	static 
	{
		STANDARD = new Typology("STANDARD", 0);
		SERIALIZATION = new Typology("SERIALIZATION", 1);
		NOT_COMPLIANT = new Typology("NOT_COMPLIANT", 2);
		UNKNOWN = new Typology("UNKNOWN", 3);
		$VALUES = (new Typology[] {
			STANDARD, SERIALIZATION, NOT_COMPLIANT, UNKNOWN
		});
	}
}
