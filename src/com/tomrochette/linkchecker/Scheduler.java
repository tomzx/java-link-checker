package com.tomrochette.linkchecker;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Scheduler
{
	private static final Logger logger = LogManager.getLogger(Scheduler.class);
	protected int count = 0;

	public synchronized void increment()
	{
		this.count += 1;
		logger.debug("Scheduler::increment(" + this.count + ")");
	}

	public synchronized void decrement()
	{
		this.count -= 1;
		logger.debug("Scheduler::decrement(" + this.count + ")");
		if (this.count == 0)
		{
			logger.debug("Scheduler::decrement to 0");
			this.notify();
		}
	}

	public synchronized void waitForCompletion()
	{
		try
		{
			this.wait();
		}
		catch (InterruptedException e)
		{
		}
	}
}
