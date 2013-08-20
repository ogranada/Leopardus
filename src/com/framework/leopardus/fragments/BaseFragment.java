package com.framework.leopardus.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framework.leopardus.R;
import com.framework.leopardus.interfaces.InterfacesHelper;
import com.framework.leopardus.interfaces.MethodInterface;
import com.framework.leopardus.utils.Injector;

public class BaseFragment extends Fragment{

	final int layout;
	final MethodInterface iface;
	
	private void startInjection(View v){
		Injector inj = new Injector(v);
		inj.injectViews(this);
		inj.injectMethodsIntoViews(this);
	}
	
	public BaseFragment() {
		super();
		layout = R.layout.base_layout;
		iface = InterfacesHelper.getVoidMethod();
	}

	public BaseFragment(Integer lyt) {
		super();
		layout = lyt;
		iface = InterfacesHelper.getVoidMethod();
	}

	public BaseFragment(Integer lyt, MethodInterface intrface) {
		super();
		layout = lyt;
		iface = intrface;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(layout, container, false);
		iface.Method(v);
		startInjection(v);
		return v;
	}

}
