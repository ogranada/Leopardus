package com.framework.leopardus.interfaces;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;

import com.framework.leopardus.utils.RESTSimpleHelper;

import android.util.Log;

public abstract class RESTCallback {

	private RESTSimpleHelper RestSimpleHelperInstance;
	private String requestUrl = "";

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public RESTCallback() {
		RestSimpleHelperInstance = null;
	}

	public RESTCallback(RESTSimpleHelper rst) {
		RestSimpleHelperInstance = rst;
	}

	public RESTSimpleHelper getRESTSimpleHelperInstance() {
		return RestSimpleHelperInstance;
	}

	/**
	 * This method is called when the request is finished.
	 * 
	 * @param resp
	 */
	public abstract void onFinish(int status, String section, HttpResponse resp);

	public void onClientProtocolException(ClientProtocolException cpe) {
		Log.e("Leopardus", "RestSimpleHelper: Error with client protocol: "
				+ cpe.getMessage());
	}

	public void onStarted() {
	}

	public void onFinished() {
	}

	public void onIOException(IOException ioe) {
		Log.e("Leopardus",
				"RestSimpleHelper: IOException error: " + ioe.getMessage());
	}

	public void onException(Exception e) {
		Log.e("Leopardus",
				"RestSimpleHelper: Exception error: " + e.getMessage());
	}

}
