// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ConcurrentReferenceHashMap.java

package org.springframework.util;

import java.lang.ref.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;

// Referenced classes of package org.springframework.util:
//			Assert, ObjectUtils

public class ConcurrentReferenceHashMap extends AbstractMap
	implements ConcurrentMap
{
	private static final class WeakEntryReference extends WeakReference
		implements Reference
	{

		private final int hash;
		private final Reference nextReference;

		public int getHash()
		{
			return hash;
		}

		public Reference getNext()
		{
			return nextReference;
		}

		public void release()
		{
			enqueue();
			clear();
		}

		public volatile ConcurrentReferenceHashMap.Entry get()
		{
			return (ConcurrentReferenceHashMap.Entry)super.get();
		}

		public WeakEntryReference(ConcurrentReferenceHashMap.Entry entry, int hash, Reference next, ReferenceQueue queue)
		{
			super(entry, queue);
			this.hash = hash;
			nextReference = next;
		}
	}

	private static final class SoftEntryReference extends SoftReference
		implements Reference
	{

		private final int hash;
		private final Reference nextReference;

		public int getHash()
		{
			return hash;
		}

		public Reference getNext()
		{
			return nextReference;
		}

		public void release()
		{
			enqueue();
			clear();
		}

		public volatile ConcurrentReferenceHashMap.Entry get()
		{
			return (ConcurrentReferenceHashMap.Entry)super.get();
		}

		public SoftEntryReference(ConcurrentReferenceHashMap.Entry entry, int hash, Reference next, ReferenceQueue queue)
		{
			super(entry, queue);
			this.hash = hash;
			nextReference = next;
		}
	}

	protected class ReferenceManager
	{

		private final ReferenceQueue queue = new ReferenceQueue();
		final ConcurrentReferenceHashMap this$0;

		public Reference createReference(ConcurrentReferenceHashMap.Entry entry, int hash, Reference next)
		{
			if (referenceType == ReferenceType.WEAK)
				return new WeakEntryReference(entry, hash, next, queue);
			else
				return new SoftEntryReference(entry, hash, next, queue);
		}

		public Reference pollForPurge()
		{
			return (Reference)queue.poll();
		}

		protected ReferenceManager()
		{
			this.this$0 = ConcurrentReferenceHashMap.this;
			super();
		}
	}

	protected static final class Restructure extends Enum
	{

		public static final Restructure WHEN_NECESSARY;
		public static final Restructure NEVER;
		private static final Restructure $VALUES[];

		public static Restructure[] values()
		{
			return (Restructure[])$VALUES.clone();
		}

		public static Restructure valueOf(String name)
		{
			return (Restructure)Enum.valueOf(org/springframework/util/ConcurrentReferenceHashMap$Restructure, name);
		}

		static 
		{
			WHEN_NECESSARY = new Restructure("WHEN_NECESSARY", 0);
			NEVER = new Restructure("NEVER", 1);
			$VALUES = (new Restructure[] {
				WHEN_NECESSARY, NEVER
			});
		}

		private Restructure(String s, int i)
		{
			super(s, i);
		}
	}

	private class EntryIterator
		implements Iterator
	{

		private int segmentIndex;
		private int referenceIndex;
		private Reference references[];
		private Reference reference;
		private ConcurrentReferenceHashMap.Entry next;
		private ConcurrentReferenceHashMap.Entry last;
		final ConcurrentReferenceHashMap this$0;

		public boolean hasNext()
		{
			getNextIfNecessary();
			return next != null;
		}

		public ConcurrentReferenceHashMap.Entry next()
		{
			getNextIfNecessary();
			if (next == null)
			{
				throw new NoSuchElementException();
			} else
			{
				last = next;
				next = null;
				return last;
			}
		}

		private void getNextIfNecessary()
		{
			for (; next == null; next = reference.get())
			{
				moveToNextReference();
				if (reference == null)
					return;
			}

		}

		private void moveToNextReference()
		{
			if (reference != null)
				reference = reference.getNext();
			while (reference == null && references != null) 
				if (referenceIndex >= references.length)
				{
					moveToNextSegment();
					referenceIndex = 0;
				} else
				{
					reference = references[referenceIndex];
					referenceIndex++;
				}
		}

		private void moveToNextSegment()
		{
			reference = null;
			references = null;
			if (segmentIndex < segments.length)
			{
				references = segments[segmentIndex].references;
				segmentIndex++;
			}
		}

		public void remove()
		{
			Assert.state(last != null, "No element to remove");
			ConcurrentReferenceHashMap.this.remove(last.getKey());
		}

		public volatile Object next()
		{
			return next();
		}

		public EntryIterator()
		{
			this$0 = ConcurrentReferenceHashMap.this;
			super();
			moveToNextSegment();
		}
	}

	private class EntrySet extends AbstractSet
	{

		final ConcurrentReferenceHashMap this$0;

		public Iterator iterator()
		{
			return new EntryIterator();
		}

		public boolean contains(Object o)
		{
			if (o != null && (o instanceof java.util.Map.Entry))
			{
				java.util.Map.Entry entry = (java.util.Map.Entry)o;
				Reference reference = getReference(entry.getKey(), Restructure.NEVER);
				ConcurrentReferenceHashMap.Entry other = reference == null ? null : reference.get();
				if (other != null)
					return ObjectUtils.nullSafeEquals(entry.getValue(), other.getValue());
			}
			return false;
		}

		public boolean remove(Object o)
		{
			if (o instanceof java.util.Map.Entry)
			{
				java.util.Map.Entry entry = (java.util.Map.Entry)o;
				return ConcurrentReferenceHashMap.this.remove(entry.getKey(), entry.getValue());
			} else
			{
				return false;
			}
		}

		public int size()
		{
			return ConcurrentReferenceHashMap.this.size();
		}

		public void clear()
		{
			ConcurrentReferenceHashMap.this.clear();
		}

		private EntrySet()
		{
			this$0 = ConcurrentReferenceHashMap.this;
			super();
		}

	}

	private abstract class Entries
	{

		final ConcurrentReferenceHashMap this$0;

		public abstract void add(Object obj);

		private Entries()
		{
			this$0 = ConcurrentReferenceHashMap.this;
			super();
		}

	}

	private static final class TaskOption extends Enum
	{

		public static final TaskOption RESTRUCTURE_BEFORE;
		public static final TaskOption RESTRUCTURE_AFTER;
		public static final TaskOption SKIP_IF_EMPTY;
		public static final TaskOption RESIZE;
		private static final TaskOption $VALUES[];

		public static TaskOption[] values()
		{
			return (TaskOption[])$VALUES.clone();
		}

		public static TaskOption valueOf(String name)
		{
			return (TaskOption)Enum.valueOf(org/springframework/util/ConcurrentReferenceHashMap$TaskOption, name);
		}

		static 
		{
			RESTRUCTURE_BEFORE = new TaskOption("RESTRUCTURE_BEFORE", 0);
			RESTRUCTURE_AFTER = new TaskOption("RESTRUCTURE_AFTER", 1);
			SKIP_IF_EMPTY = new TaskOption("SKIP_IF_EMPTY", 2);
			RESIZE = new TaskOption("RESIZE", 3);
			$VALUES = (new TaskOption[] {
				RESTRUCTURE_BEFORE, RESTRUCTURE_AFTER, SKIP_IF_EMPTY, RESIZE
			});
		}

		private TaskOption(String s, int i)
		{
			super(s, i);
		}
	}

	private abstract class Task
	{

		private final EnumSet options;
		final ConcurrentReferenceHashMap this$0;

		public boolean hasOption(TaskOption option)
		{
			return options.contains(option);
		}

		protected Object execute(Reference reference, ConcurrentReferenceHashMap.Entry entry, Entries entries)
		{
			return execute(reference, entry);
		}

		protected Object execute(Reference reference, ConcurrentReferenceHashMap.Entry entry)
		{
			return null;
		}

		public transient Task(TaskOption options[])
		{
			this$0 = ConcurrentReferenceHashMap.this;
			super();
			this.options = options.length != 0 ? EnumSet.of(options[0], options) : EnumSet.noneOf(org/springframework/util/ConcurrentReferenceHashMap$TaskOption);
		}
	}

	protected static final class Entry
		implements java.util.Map.Entry
	{

		private final Object key;
		private volatile Object value;

		public Object getKey()
		{
			return key;
		}

		public Object getValue()
		{
			return value;
		}

		public Object setValue(Object value)
		{
			Object previous = this.value;
			this.value = value;
			return previous;
		}

		public String toString()
		{
			return (new StringBuilder()).append(key).append("=").append(value).toString();
		}

		public final boolean equals(Object other)
		{
			if (this == other)
				return true;
			if (!(other instanceof java.util.Map.Entry))
			{
				return false;
			} else
			{
				java.util.Map.Entry otherEntry = (java.util.Map.Entry)other;
				return ObjectUtils.nullSafeEquals(getKey(), otherEntry.getKey()) && ObjectUtils.nullSafeEquals(getValue(), otherEntry.getValue());
			}
		}

		public final int hashCode()
		{
			return ObjectUtils.nullSafeHashCode(key) ^ ObjectUtils.nullSafeHashCode(value);
		}


		public Entry(Object key, Object value)
		{
			this.key = key;
			this.value = value;
		}
	}

	protected static interface Reference
	{

		public abstract ConcurrentReferenceHashMap.Entry get();

		public abstract int getHash();

		public abstract Reference getNext();

		public abstract void release();
	}

	protected final class Segment extends ReentrantLock
	{

		private final ReferenceManager referenceManager;
		private final int initialSize;
		private volatile Reference references[];
		private volatile int count;
		private int resizeThreshold;
		final ConcurrentReferenceHashMap this$0;

		public Reference getReference(Object key, int hash, Restructure restructure)
		{
			if (restructure == Restructure.WHEN_NECESSARY)
				restructureIfNecessary(false);
			if (count == 0)
			{
				return null;
			} else
			{
				Reference references[] = this.references;
				int index = getIndex(hash, references);
				Reference head = references[index];
				return findInChain(head, key, hash);
			}
		}

		public Object doTask(final int hash, final Object key, Task task)
		{
			boolean resize;
			resize = task.hasOption(TaskOption.RESIZE);
			if (task.hasOption(TaskOption.RESTRUCTURE_BEFORE))
				restructureIfNecessary(resize);
			if (task.hasOption(TaskOption.SKIP_IF_EMPTY) && count == 0)
				return task.execute(null, null, null);
			lock();
			Object obj;
			final int index = getIndex(hash, references);
			final Reference head = references[index];
			Reference reference = findInChain(head, key, hash);
			ConcurrentReferenceHashMap.Entry entry = reference == null ? null : reference.get();
			Entries entries = new Entries() {

				final Object val$key;
				final int val$hash;
				final Reference val$head;
				final int val$index;
				final Segment this$1;

				public void add(Object value)
				{
					ConcurrentReferenceHashMap.Entry newEntry = new ConcurrentReferenceHashMap.Entry(key, value);
					Reference newReference = referenceManager.createReference(newEntry, hash, head);
					references[index] = newReference;
					count++;
				}

				
				{
					this.this$1 = Segment.this;
					key = obj;
					hash = i;
					head = reference;
					index = j;
					super();
				}
			};
			obj = task.execute(reference, entry, entries);
			unlock();
			if (task.hasOption(TaskOption.RESTRUCTURE_AFTER))
				restructureIfNecessary(resize);
			return obj;
			Exception exception;
			exception;
			unlock();
			if (task.hasOption(TaskOption.RESTRUCTURE_AFTER))
				restructureIfNecessary(resize);
			throw exception;
		}

		public void clear()
		{
			if (count == 0)
				return;
			lock();
			setReferences(createReferenceArray(initialSize));
			count = 0;
			unlock();
			break MISSING_BLOCK_LABEL_43;
			Exception exception;
			exception;
			unlock();
			throw exception;
		}

		protected final void restructureIfNecessary(boolean allowResize)
		{
			Reference reference;
			boolean needsResize = count > 0 && count >= resizeThreshold;
			reference = referenceManager.pollForPurge();
			if (reference == null && (!needsResize || !allowResize))
				break MISSING_BLOCK_LABEL_334;
			lock();
			int countAfterRestructure = count;
			Set toPurge = Collections.emptySet();
			if (reference != null)
			{
				toPurge = new HashSet();
				for (; reference != null; reference = referenceManager.pollForPurge())
					toPurge.add(reference);

			}
			countAfterRestructure -= toPurge.size();
			boolean needsResize = countAfterRestructure > 0 && countAfterRestructure >= resizeThreshold;
			boolean resizing = false;
			int restructureSize = references.length;
			if (allowResize && needsResize && restructureSize < 0x40000000)
			{
				restructureSize <<= 1;
				resizing = true;
			}
			Reference restructured[] = resizing ? createReferenceArray(restructureSize) : references;
			for (int i = 0; i < references.length; i++)
			{
				reference = references[i];
				if (!resizing)
					restructured[i] = null;
				for (; reference != null; reference = reference.getNext())
					if (!toPurge.contains(reference) && reference.get() != null)
					{
						int index = getIndex(reference.getHash(), restructured);
						restructured[index] = referenceManager.createReference(reference.get(), reference.getHash(), restructured[index]);
					}

			}

			if (resizing)
				setReferences(restructured);
			count = Math.max(countAfterRestructure, 0);
			unlock();
			break MISSING_BLOCK_LABEL_334;
			Exception exception;
			exception;
			unlock();
			throw exception;
		}

		private Reference findInChain(Reference reference, Object key, int hash)
		{
			for (; reference != null; reference = reference.getNext())
			{
				if (reference.getHash() != hash)
					continue;
				ConcurrentReferenceHashMap.Entry entry = reference.get();
				if (entry == null)
					continue;
				Object entryKey = entry.getKey();
				if (entryKey == key || entryKey.equals(key))
					return reference;
			}

			return null;
		}

		private Reference[] createReferenceArray(int size)
		{
			return (Reference[])(Reference[])Array.newInstance(org/springframework/util/ConcurrentReferenceHashMap$Reference, size);
		}

		private int getIndex(int hash, Reference references[])
		{
			return hash & references.length - 1;
		}

		private void setReferences(Reference references[])
		{
			this.references = references;
			resizeThreshold = (int)((float)references.length * getLoadFactor());
		}

		public final int getSize()
		{
			return references.length;
		}

		public final int getCount()
		{
			return count;
		}




		public Segment(int initialCapacity)
		{
			this.this$0 = ConcurrentReferenceHashMap.this;
			super();
			count = 0;
			referenceManager = createReferenceManager();
			initialSize = 1 << ConcurrentReferenceHashMap.calculateShift(initialCapacity, 0x40000000);
			setReferences(createReferenceArray(initialSize));
		}
	}

	public static final class ReferenceType extends Enum
	{

		public static final ReferenceType SOFT;
		public static final ReferenceType WEAK;
		private static final ReferenceType $VALUES[];

		public static ReferenceType[] values()
		{
			return (ReferenceType[])$VALUES.clone();
		}

		public static ReferenceType valueOf(String name)
		{
			return (ReferenceType)Enum.valueOf(org/springframework/util/ConcurrentReferenceHashMap$ReferenceType, name);
		}

		static 
		{
			SOFT = new ReferenceType("SOFT", 0);
			WEAK = new ReferenceType("WEAK", 1);
			$VALUES = (new ReferenceType[] {
				SOFT, WEAK
			});
		}

		private ReferenceType(String s, int i)
		{
			super(s, i);
		}
	}


	private static final int DEFAULT_INITIAL_CAPACITY = 16;
	private static final float DEFAULT_LOAD_FACTOR = 0.75F;
	private static final int DEFAULT_CONCURRENCY_LEVEL = 16;
	private static final ReferenceType DEFAULT_REFERENCE_TYPE;
	private static final int MAXIMUM_CONCURRENCY_LEVEL = 0x10000;
	private static final int MAXIMUM_SEGMENT_SIZE = 0x40000000;
	private final Segment segments[];
	private final float loadFactor;
	private final ReferenceType referenceType;
	private final int shift;
	private Set entrySet;

	public ConcurrentReferenceHashMap()
	{
		this(16, 0.75F, 16, DEFAULT_REFERENCE_TYPE);
	}

	public ConcurrentReferenceHashMap(int initialCapacity)
	{
		this(initialCapacity, 0.75F, 16, DEFAULT_REFERENCE_TYPE);
	}

	public ConcurrentReferenceHashMap(int initialCapacity, float loadFactor)
	{
		this(initialCapacity, loadFactor, 16, DEFAULT_REFERENCE_TYPE);
	}

	public ConcurrentReferenceHashMap(int initialCapacity, int concurrencyLevel)
	{
		this(initialCapacity, 0.75F, concurrencyLevel, DEFAULT_REFERENCE_TYPE);
	}

	public ConcurrentReferenceHashMap(int initialCapacity, ReferenceType referenceType)
	{
		this(initialCapacity, 0.75F, 16, referenceType);
	}

	public ConcurrentReferenceHashMap(int initialCapacity, float loadFactor, int concurrencyLevel)
	{
		this(initialCapacity, loadFactor, concurrencyLevel, DEFAULT_REFERENCE_TYPE);
	}

	public ConcurrentReferenceHashMap(int initialCapacity, float loadFactor, int concurrencyLevel, ReferenceType referenceType)
	{
		Assert.isTrue(initialCapacity >= 0, "Initial capacity must not be negative");
		Assert.isTrue(loadFactor > 0.0F, "Load factor must be positive");
		Assert.isTrue(concurrencyLevel > 0, "Concurrency level must be positive");
		Assert.notNull(referenceType, "Reference type must not be null");
		this.loadFactor = loadFactor;
		shift = calculateShift(concurrencyLevel, 0x10000);
		int size = 1 << shift;
		this.referenceType = referenceType;
		int roundedUpSegmentCapacity = (int)(((long)(initialCapacity + size) - 1L) / (long)size);
		segments = (Segment[])(Segment[])Array.newInstance(org/springframework/util/ConcurrentReferenceHashMap$Segment, size);
		for (int i = 0; i < segments.length; i++)
			segments[i] = new Segment(roundedUpSegmentCapacity);

	}

	protected final float getLoadFactor()
	{
		return loadFactor;
	}

	protected final int getSegmentsSize()
	{
		return segments.length;
	}

	protected final Segment getSegment(int index)
	{
		return segments[index];
	}

	protected ReferenceManager createReferenceManager()
	{
		return new ReferenceManager();
	}

	protected int getHash(Object o)
	{
		int hash = o != null ? o.hashCode() : 0;
		hash += hash << 15 ^ 0xffffcd7d;
		hash ^= hash >>> 10;
		hash += hash << 3;
		hash ^= hash >>> 6;
		hash += (hash << 2) + (hash << 14);
		hash ^= hash >>> 16;
		return hash;
	}

	public Object get(Object key)
	{
		Reference reference = getReference(key, Restructure.WHEN_NECESSARY);
		Entry entry = reference == null ? null : reference.get();
		return entry == null ? null : entry.getValue();
	}

	public boolean containsKey(Object key)
	{
		Reference reference = getReference(key, Restructure.WHEN_NECESSARY);
		Entry entry = reference == null ? null : reference.get();
		return entry != null && ObjectUtils.nullSafeEquals(entry.getKey(), key);
	}

	protected final Reference getReference(Object key, Restructure restructure)
	{
		int hash = getHash(key);
		return getSegmentForHash(hash).getReference(key, hash, restructure);
	}

	public Object put(Object key, Object value)
	{
		return put(key, value, true);
	}

	public Object putIfAbsent(Object key, Object value)
	{
		return put(key, value, false);
	}

	private Object put(Object key, Object value, boolean overwriteExisting)
	{
		return doTask(key, new Task(value) {

			final boolean val$overwriteExisting;
			final Object val$value;
			final ConcurrentReferenceHashMap this$0;

			protected Object execute(Reference reference, ConcurrentReferenceHashMap.Entry entry, Entries entries)
			{
				if (entry != null)
				{
					Object previousValue = entry.getValue();
					if (overwriteExisting)
						entry.setValue(value);
					return previousValue;
				} else
				{
					entries.add(value);
					return null;
				}
			}

			transient 
			{
				this.this$0 = ConcurrentReferenceHashMap.this;
				overwriteExisting = flag;
				value = obj;
				super(options);
			}
		});
	}

	public Object remove(Object key)
	{
		return doTask(key, new Task(new TaskOption[] {
			TaskOption.RESTRUCTURE_AFTER, TaskOption.SKIP_IF_EMPTY
		}) {

			final ConcurrentReferenceHashMap this$0;

			protected Object execute(Reference reference, ConcurrentReferenceHashMap.Entry entry)
			{
				if (entry != null)
				{
					reference.release();
					return entry.value;
				} else
				{
					return null;
				}
			}

			transient 
			{
				this.this$0 = ConcurrentReferenceHashMap.this;
				super(options);
			}
		});
	}

	public boolean remove(Object key, Object value)
	{
		return ((Boolean)doTask(key, new Task(value) {

			final Object val$value;
			final ConcurrentReferenceHashMap this$0;

			protected Boolean execute(Reference reference, ConcurrentReferenceHashMap.Entry entry)
			{
				if (entry != null && ObjectUtils.nullSafeEquals(entry.getValue(), value))
				{
					reference.release();
					return Boolean.valueOf(true);
				} else
				{
					return Boolean.valueOf(false);
				}
			}

			protected volatile Object execute(Reference reference, ConcurrentReferenceHashMap.Entry entry)
			{
				return execute(reference, entry);
			}

			transient 
			{
				this.this$0 = ConcurrentReferenceHashMap.this;
				value = obj;
				super(options);
			}
		})).booleanValue();
	}

	public boolean replace(Object key, Object oldValue, Object newValue)
	{
		return ((Boolean)doTask(key, new Task(newValue) {

			final Object val$oldValue;
			final Object val$newValue;
			final ConcurrentReferenceHashMap this$0;

			protected Boolean execute(Reference reference, ConcurrentReferenceHashMap.Entry entry)
			{
				if (entry != null && ObjectUtils.nullSafeEquals(entry.getValue(), oldValue))
				{
					entry.setValue(newValue);
					return Boolean.valueOf(true);
				} else
				{
					return Boolean.valueOf(false);
				}
			}

			protected volatile Object execute(Reference reference, ConcurrentReferenceHashMap.Entry entry)
			{
				return execute(reference, entry);
			}

			transient 
			{
				this.this$0 = ConcurrentReferenceHashMap.this;
				oldValue = obj;
				newValue = obj1;
				super(options);
			}
		})).booleanValue();
	}

	public Object replace(Object key, Object value)
	{
		return doTask(key, new Task(value) {

			final Object val$value;
			final ConcurrentReferenceHashMap this$0;

			protected Object execute(Reference reference, ConcurrentReferenceHashMap.Entry entry)
			{
				if (entry != null)
				{
					Object previousValue = entry.getValue();
					entry.setValue(value);
					return previousValue;
				} else
				{
					return null;
				}
			}

			transient 
			{
				this.this$0 = ConcurrentReferenceHashMap.this;
				value = obj;
				super(options);
			}
		});
	}

	public void clear()
	{
		Segment asegment[] = segments;
		int i = asegment.length;
		for (int j = 0; j < i; j++)
		{
			Segment segment = asegment[j];
			segment.clear();
		}

	}

	public void purgeUnreferencedEntries()
	{
		Segment asegment[] = segments;
		int i = asegment.length;
		for (int j = 0; j < i; j++)
		{
			Segment segment = asegment[j];
			segment.restructureIfNecessary(false);
		}

	}

	public int size()
	{
		int size = 0;
		Segment asegment[] = segments;
		int i = asegment.length;
		for (int j = 0; j < i; j++)
		{
			Segment segment = asegment[j];
			size += segment.getCount();
		}

		return size;
	}

	public Set entrySet()
	{
		if (entrySet == null)
			entrySet = new EntrySet();
		return entrySet;
	}

	private Object doTask(Object key, Task task)
	{
		int hash = getHash(key);
		return getSegmentForHash(hash).doTask(hash, key, task);
	}

	private Segment getSegmentForHash(int hash)
	{
		return segments[hash >>> 32 - shift & segments.length - 1];
	}

	protected static int calculateShift(int minimumValue, int maximumValue)
	{
		int shift = 0;
		for (int value = 1; value < minimumValue && value < maximumValue;)
		{
			value <<= 1;
			shift++;
		}

		return shift;
	}

	static 
	{
		DEFAULT_REFERENCE_TYPE = ReferenceType.SOFT;
	}


}
