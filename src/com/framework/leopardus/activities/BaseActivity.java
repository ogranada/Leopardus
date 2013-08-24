package com.framework.leopardus.activities;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;
import com.framework.leopardus.utils.Injector;

public class BaseActivity extends SherlockActivity {

	private int layout;

	public BaseActivity(int lyt) {
		super();
		this.layout = lyt;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Injector i = new Injector(this);
		i.injectViews(this);
		i.injectMethodsIntoViews(this);
	}
	
}
