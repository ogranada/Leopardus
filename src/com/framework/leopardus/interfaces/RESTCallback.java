package com.framework.leopardus.interfaces;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;

import com.framework.leopardus.utils.RESTSimpleTool;

import android.util.Log;

public abstract class RESTCallback {

	private RESTSimpleTool RESTSimpleToolInstance;
	private String requestUrl = "";

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public RESTCallback() {
		RESTSimpleToolInstance = null;
	}

	public RESTCallback(RESTSimpleTool rst) {
		RESTSimpleToolInstance = rst;
	}

	public RESTSimpleTool getRESTSimpleToolInstance() {
		return RESTSimpleToolInstance;
	}

	/**
	 * This method is called when the request is finished.
	 * 
	 * @param resp
	 */
	public abstract void onFinish(int status, String section, HttpResponse resp);

	public void onClientProtocolException(ClientProtocolException cpe) {
		Log.e("Leopardus",
				"RestSimpleTool: Error with client protocol: "
						+ cpe.getMessage());
	}

	public void onIOException(IOException ioe) {
		Log.e("Leopardus",
				"RestSimpleTool: IOException error: " + ioe.getMessage());
	}

}
