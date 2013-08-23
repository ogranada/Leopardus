package com.framework.leopardus.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.actionbarsherlock.app.SherlockFragment;
import com.framework.leopardus.R;
import com.framework.leopardus.activities.BaseFragmentsActivity;
import com.framework.leopardus.interfaces.InterfacesHelper;
import com.framework.leopardus.interfaces.MethodInterface;
import com.framework.leopardus.utils.ImageLoaderTool;
import com.framework.leopardus.utils.Injector;
import com.manuelpeinado.fadingactionbar.FadingActionBarHelper;

public class BaseFragment extends SherlockFragment {

	final int layout;
	final MethodInterface iface;
	Activity activity;

	private boolean fadingActionBarEnabled = false;
	int fadingBackground = R.drawable.ab_background;
	int fadingHeader = R.layout.fadingactionbar_header;
	int fadingHeaderImage = R.drawable.tigrillo;
	private String headerImagePath = "";
	private FadingActionBarHelper fadingHelper;
	private ImageView headerImg = null;
	private BaseFragmentsActivity parentActivity;
	
	public BaseFragmentsActivity getParentActivity() {
		return parentActivity;
	}
	
	public void setParentActivity(BaseFragmentsActivity parentActivity) {
		this.parentActivity = parentActivity;
	}
	
	private void startInjection(View v) {
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

	public BaseFragment(Integer lyt, BaseFragmentsActivity parentAct) {
		super();
		layout = lyt;
		iface = InterfacesHelper.getVoidMethod();
		setParentActivity(parentAct);
	}

	public BaseFragment(Integer lyt, MethodInterface intrface, BaseFragmentsActivity parentAct) {
		super();
		layout = lyt;
		iface = intrface;
		setParentActivity(parentAct);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v;
		if (isFadingActionBarEnabled()) {
			v = fadingHelper.createView(inflater);
			headerImg = (ImageView) v.findViewById(R.id.image_header);
			if (fadingHeaderImage != -1) {
				headerImg.setImageResource(fadingHeaderImage);
			} else {
				try {
					ImageLoaderTool.getInstance(v.getContext()).display(
							headerImagePath, headerImg);
				} catch (Exception e) {
					headerImg.setImageResource(R.drawable.tigrillo);
				}
			}
		} else {
			v = inflater.inflate(layout, container, false);
		}
		activity = ((Activity) v.getContext());
		iface.Method(v);
		startInjection(v);
		return v;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (isFadingActionBarEnabled()) {
			fadingHelper = new FadingActionBarHelper()
					.actionBarBackground(fadingBackground)
					.headerLayout(fadingHeader).contentLayout(layout)
					.lightActionBar(true);
			fadingHelper.initActionBar(activity);
		}
	}

	public void setFadingActionBarEnabled(boolean fadingActionBar) {
		this.fadingActionBarEnabled = fadingActionBar;
	}

	public boolean isFadingActionBarEnabled() {
		return fadingActionBarEnabled;
	}

	public void setFadingHeaderImage(int fadingHeaderImage) {
		this.fadingHeaderImage = fadingHeaderImage;
	}

	public void setFadingHeaderImage(String name) {
		this.fadingHeaderImage = -1;
		headerImagePath = name;
		if (activity != null) {
			Context ctx = activity.getBaseContext();
			if (ctx != null && headerImg != null) {
				ImageLoaderTool.getInstance(ctx).display(headerImagePath,
						headerImg);
			}
		}
	}

}
