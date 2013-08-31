package com.framework.leopardus.fragments;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.framework.leopardus.R;
import com.sherlock.navigationdrawer.compat.SherlockActionBarDrawerToggle;

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
