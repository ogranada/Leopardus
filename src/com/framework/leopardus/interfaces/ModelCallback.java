package com.framework.leopardus.interfaces;

import com.framework.leopardus.models.Model;

public abstract class ModelCallback {

	public abstract void onModelLoaded(Model m);
	
	public void onFinish(){
		
	}
	
}
