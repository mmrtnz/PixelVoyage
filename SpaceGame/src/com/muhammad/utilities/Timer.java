package com.muhammad.utilities;

final public class Timer
{
	private long startTime, waitTime;
	
	public Timer(long waitTime)
	{
		reset(waitTime);
	}
	
	public boolean done()
	{
		return System.currentTimeMillis()-startTime >= waitTime;
	}
	
	public void reset(long newWaitTime)
	{
		startTime=System.currentTimeMillis();
		waitTime = newWaitTime;
	}
}
