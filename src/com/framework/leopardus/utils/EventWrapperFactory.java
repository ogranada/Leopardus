package com.framework.leopardus.utils;

import com.framework.leopardus.utils.wrappers.EventWrapper;

public class EventWrapperFactory {

	public static Class<EventWrapper> wraperClass = EventWrapper.class;
	
	public static EventWrapper getWraperInstance() throws Exception {
		return wraperClass.newInstance();
	}
	
}
