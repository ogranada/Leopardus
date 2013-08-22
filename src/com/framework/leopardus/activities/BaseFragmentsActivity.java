package com.framework.leopardus.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.framework.leopardus.R;
import com.framework.leopardus.fragments.BaseFragment;
import com.framework.leopardus.fragments.BaseMenuFragment;
import com.framework.leopardus.interfaces.ActivityMethodInterface;
import com.framework.leopardus.interfaces.InterfacesHelper;
import com.framework.leopardus.utils.Injector;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

public class BaseFragmentsActivity extends SlidingFragmentActivity {

	private BaseMenuFragment menuFragment;
	private SlidingMenu slidingMenu;
	private boolean enableMenuOnHome = false;
	ActivityMethodInterface closeCallback = InterfacesHelper.getCloseMethod();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBehindContentView(R.layout.menu_frame);
		FragmentTransaction t = this.getSupportFragmentManager()
				.beginTransaction();
		menuFragment = new BaseMenuFragment();
		t.replace(R.id.menu_frame, menuFragment);
		t.commit();
		slidingMenu = getSlidingMenu();
		slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
		slidingMenu.setShadowDrawable(R.drawable.shadow);
		slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		slidingMenu.setFadeDegree(0.35f);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		// ///////////////////////////////////////////
		setContentView(R.layout.content_frame);
		Fragment firstFragment = new BaseFragment();
		setActualFragment(firstFragment);
		setSlidingActionBarEnabled(true);
		// ///////////////////////////////////////////
		Injector i = new Injector(this);
		i.injectMenuItems(this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if (this.enableMenuOnHome) {
				toggle();
				return true;
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void setActualFragment(Fragment frgmnt) {
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, frgmnt).commit();
		getSlidingMenu().showContent();
	}

	public void setEnabledMenuOnHome() {
		this.enableMenuOnHome = true;
	}

	public void setDisabledMenuOnHome() {
		this.enableMenuOnHome = true;
	}

	public boolean isEnabledMenuOnHome() {
		return enableMenuOnHome;
	}

	public BaseMenuFragment getMenu() {
		return menuFragment;
	}

	public void close() {
		closeCallback.Method(this);
	}

	public void setOnCloseEvent(ActivityMethodInterface ami) {
		closeCallback = ami;
	}

	public Activity getMySelf() {
		return this;
	}

	public void enableHomeAsButton() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	public void disableHomeAsButton() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
	}

}
