package com.framework.leopardus.utils;

public class EventWrapperFactory {

	public static Class<EventWrapper> wraperClass = EventWrapper.class;
	
	public static EventWrapper getWraperInstance() throws Exception {
		return wraperClass.newInstance();
	}
	
}
