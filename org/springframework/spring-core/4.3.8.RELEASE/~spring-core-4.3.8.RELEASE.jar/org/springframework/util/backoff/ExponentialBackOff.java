// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ExponentialBackOff.java

package org.springframework.util.backoff;


// Referenced classes of package org.springframework.util.backoff:
//			BackOff, BackOffExecution

public class ExponentialBackOff
	implements BackOff
{
	private class ExponentialBackOffExecution
		implements BackOffExecution
	{

		private long currentInterval;
		private long currentElapsedTime;
		final ExponentialBackOff this$0;

		public long nextBackOff()
		{
			if (currentElapsedTime >= maxElapsedTime)
			{
				return -1L;
			} else
			{
				long nextInterval = computeNextInterval();
				currentElapsedTime += nextInterval;
				return nextInterval;
			}
		}

		private long computeNextInterval()
		{
			long maxInterval = getMaxInterval();
			if (currentInterval >= maxInterval)
				return maxInterval;
			if (currentInterval < 0L)
			{
				long initialInterval = getInitialInterval();
				currentInterval = initialInterval >= maxInterval ? maxInterval : initialInterval;
			} else
			{
				currentInterval = multiplyInterval(maxInterval);
			}
			return currentInterval;
		}

		private long multiplyInterval(long maxInterval)
		{
			long i = currentInterval;
			i = (long)((double)i * getMultiplier());
			return i <= maxInterval ? i : maxInterval;
		}

		public String toString()
		{
			StringBuilder sb = new StringBuilder("ExponentialBackOff{");
			sb.append("currentInterval=").append(currentInterval >= 0L ? (new StringBuilder()).append(currentInterval).append("ms").toString() : "n/a");
			sb.append(", multiplier=").append(getMultiplier());
			sb.append('}');
			return sb.toString();
		}

		private ExponentialBackOffExecution()
		{
			this$0 = ExponentialBackOff.this;
			super();
			currentInterval = -1L;
			currentElapsedTime = 0L;
		}

	}


	public static final long DEFAULT_INITIAL_INTERVAL = 2000L;
	public static final double DEFAULT_MULTIPLIER = 1.5D;
	public static final long DEFAULT_MAX_INTERVAL = 30000L;
	public static final long DEFAULT_MAX_ELAPSED_TIME = 0x7fffffffffffffffL;
	private long initialInterval;
	private double multiplier;
	private long maxInterval;
	private long maxElapsedTime;

	public ExponentialBackOff()
	{
		initialInterval = 2000L;
		multiplier = 1.5D;
		maxInterval = 30000L;
		maxElapsedTime = 0x7fffffffffffffffL;
	}

	public ExponentialBackOff(long initialInterval, double multiplier)
	{
		this.initialInterval = 2000L;
		this.multiplier = 1.5D;
		maxInterval = 30000L;
		maxElapsedTime = 0x7fffffffffffffffL;
		checkMultiplier(multiplier);
		this.initialInterval = initialInterval;
		this.multiplier = multiplier;
	}

	public void setInitialInterval(long initialInterval)
	{
		this.initialInterval = initialInterval;
	}

	public long getInitialInterval()
	{
		return initialInterval;
	}

	public void setMultiplier(double multiplier)
	{
		checkMultiplier(multiplier);
		this.multiplier = multiplier;
	}

	public double getMultiplier()
	{
		return multiplier;
	}

	public void setMaxInterval(long maxInterval)
	{
		this.maxInterval = maxInterval;
	}

	public long getMaxInterval()
	{
		return maxInterval;
	}

	public void setMaxElapsedTime(long maxElapsedTime)
	{
		this.maxElapsedTime = maxElapsedTime;
	}

	public long getMaxElapsedTime()
	{
		return maxElapsedTime;
	}

	public BackOffExecution start()
	{
		return new ExponentialBackOffExecution();
	}

	private void checkMultiplier(double multiplier)
	{
		if (multiplier < 1.0D)
			throw new IllegalArgumentException((new StringBuilder()).append("Invalid multiplier '").append(multiplier).append("'. Should be equalor higher than 1. A multiplier of 1 is equivalent to a fixed interval").toString());
		else
			return;
	}

}
