package com.bn.tu;

public abstract class TouchListener 
{
	EventType et;
	
	public TouchListener(EventType et)
	{
		this.et=et;
	}
	
	public abstract void eventOccurrence(float[] eventData);
}
