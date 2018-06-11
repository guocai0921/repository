// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   FixedBackOff.java

package org.springframework.util.backoff;


// Referenced classes of package org.springframework.util.backoff:
//			BackOff, BackOffExecution

public class FixedBackOff
	implements BackOff
{
	private class FixedBackOffExecution
		implements BackOffExecution
	{

		private long currentAttempts;
		final FixedBackOff this$0;

		public long nextBackOff()
		{
			currentAttempts++;
			if (currentAttempts <= getMaxAttempts())
				return getInterval();
			else
				return -1L;
		}

		public String toString()
		{
			StringBuilder sb = new StringBuilder("FixedBackOff{");
			sb.append("interval=").append(interval);
			String attemptValue = maxAttempts != 0x7fffffffffffffffL ? String.valueOf(maxAttempts) : "unlimited";
			sb.append(", currentAttempts=").append(currentAttempts);
			sb.append(", maxAttempts=").append(attemptValue);
			sb.append('}');
			return sb.toString();
		}

		private FixedBackOffExecution()
		{
			this$0 = FixedBackOff.this;
			super();
			currentAttempts = 0L;
		}

	}


	public static final long DEFAULT_INTERVAL = 5000L;
	public static final long UNLIMITED_ATTEMPTS = 0x7fffffffffffffffL;
	private long interval;
	private long maxAttempts;

	public FixedBackOff()
	{
		interval = 5000L;
		maxAttempts = 0x7fffffffffffffffL;
	}

	public FixedBackOff(long interval, long maxAttempts)
	{
		this.interval = 5000L;
		this.maxAttempts = 0x7fffffffffffffffL;
		this.interval = interval;
		this.maxAttempts = maxAttempts;
	}

	public void setInterval(long interval)
	{
		this.interval = interval;
	}

	public long getInterval()
	{
		return interval;
	}

	public void setMaxAttempts(long maxAttempts)
	{
		this.maxAttempts = maxAttempts;
	}

	public long getMaxAttempts()
	{
		return maxAttempts;
	}

	public BackOffExecution start()
	{
		return new FixedBackOffExecution();
	}


}
