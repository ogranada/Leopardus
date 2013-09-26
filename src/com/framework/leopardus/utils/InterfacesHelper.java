package com.framework.leopardus.utils;

import org.apache.http.HttpResponse;

import com.framework.leopardus.interfaces.ActivityMethodInterface;
import com.framework.leopardus.interfaces.MethodInterface;
import com.framework.leopardus.interfaces.RESTCallback;

import android.app.Activity;
import android.view.View;

public class InterfacesHelper {

	public static MethodInterface getVoidMethod(){
		return new MethodInterface() {
			
			@Override
			public void Method(View v, Object... args) {
				// TODO Auto-generated method stub
				
			}
		};
	}

	public static ActivityMethodInterface getCloseMethod(){
		return new ActivityMethodInterface() {
			
			@Override
			public void Method(Activity a, Object... args) {
				a.finish();
			}
		};
	}

	public static ActivityMethodInterface getVoidActivityMethod(){
		return new ActivityMethodInterface() {
			
			@Override
			public void Method(Activity a, Object... args) {
				
			}
		};
	}

	public static RESTCallback getVoidRESTCallback(RESTSimpleHelper instance){
		return new RESTCallback(instance) {			
			@Override
			public void onFinish(int status, String section, HttpResponse resp) {
			}
		};
	}
}
