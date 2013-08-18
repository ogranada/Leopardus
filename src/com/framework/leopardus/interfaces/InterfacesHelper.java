package com.framework.leopardus.interfaces;

import android.app.Activity;
import android.content.Context;
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
	
}
