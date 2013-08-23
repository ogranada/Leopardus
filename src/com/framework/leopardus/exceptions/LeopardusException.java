package com.framework.leopardus.exceptions;

public class LeopardusException extends Throwable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 293742384L;

	public LeopardusException() {
		super();
	}
	
	public LeopardusException(String message) {
		super(message);
	}
	
	public LeopardusException(Throwable th) {
		super(th);
	}
	
}
