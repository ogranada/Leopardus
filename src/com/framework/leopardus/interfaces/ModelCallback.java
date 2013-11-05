package com.framework.leopardus.interfaces;

import com.framework.leopardus.models.Model;

public abstract class ModelCallback {

	public abstract void onModelLoaded(Model m);

	public void onStarted() {
	}

	public void onFinished() {
	}

	public void onFinish() {

	}

	public void onObjectNotFound(int status, String respStr) {
		
	}

}
