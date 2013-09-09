package com.framework.leopardus.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.framework.leopardus.R;

public class BaseFragmentDrawer extends SherlockFragment {

	int layout = R.layout.base_layout;
			
	public BaseFragmentDrawer() {
		
	}
	
	public BaseFragmentDrawer(int lyt) {
		layout = lyt;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(layout, container, false);
		return view;
	}
	
	

}
