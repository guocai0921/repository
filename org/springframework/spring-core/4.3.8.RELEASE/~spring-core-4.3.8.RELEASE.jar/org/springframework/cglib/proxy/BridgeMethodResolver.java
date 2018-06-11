// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   BridgeMethodResolver.java

package org.springframework.cglib.proxy;

import java.io.IOException;
import java.util.*;
import org.springframework.asm.*;
import org.springframework.cglib.core.Signature;

class BridgeMethodResolver
{
	private static class BridgedFinder extends ClassVisitor
	{

		private Map resolved;
		private Set eligibleMethods;
		private Signature currentMethod;

		public void visit(int i, int j, String s, String s1, String s2, String as[])
		{
		}

		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String exceptions[])
		{
			Signature sig = new Signature(name, desc);
			if (eligibleMethods.remove(sig))
			{
				currentMethod = sig;
				return new MethodVisitor(0x50000) {

					final BridgedFinder this$0;

					public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf)
					{
						if (opcode == 183 && currentMethod != null)
						{
							Signature target = new Signature(name, desc);
							if (!target.equals(currentMethod))
								resolved.put(currentMethod, target);
							currentMethod = null;
						}
					}

				
				{
					this.this$0 = BridgedFinder.this;
					super(x0);
				}
				};
			} else
			{
				return null;
			}
		}




		BridgedFinder(Set eligibleMethods, Map resolved)
		{
			super(0x50000);
			currentMethod = null;
			this.resolved = resolved;
			this.eligibleMethods = eligibleMethods;
		}
	}


	private final Map declToBridge;
	private final ClassLoader classLoader;

	public BridgeMethodResolver(Map declToBridge, ClassLoader classLoader)
	{
		this.declToBridge = declToBridge;
		this.classLoader = classLoader;
	}

	public Map resolveAll()
	{
		Map resolved = new HashMap();
		for (Iterator entryIter = declToBridge.entrySet().iterator(); entryIter.hasNext();)
		{
			java.util.Map.Entry entry = (java.util.Map.Entry)entryIter.next();
			Class owner = (Class)entry.getKey();
			Set bridges = (Set)entry.getValue();
			try
			{
				(new ClassReader(classLoader.getResourceAsStream((new StringBuilder()).append(owner.getName().replace('.', '/')).append(".class").toString()))).accept(new BridgedFinder(bridges, resolved), 6);
			}
			catch (IOException ioexception) { }
		}

		return resolved;
	}
}
