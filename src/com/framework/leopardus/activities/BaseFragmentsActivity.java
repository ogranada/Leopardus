package com.framework.leopardus.activities;

import java.util.Stack;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

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
	private FragmentManager fragmentManager = getSupportFragmentManager();
	ActivityMethodInterface closeCallback = InterfacesHelper.getCloseMethod();

	Stack<Fragment> fragments = new Stack<Fragment>();

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
		setActualFragment(firstFragment, false);
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

	/**
	 * Set the actual fragment
	 * 
	 * @param frgmnt
	 */
	public void setActualFragment(Fragment frgmnt) {
		setActualFragment(frgmnt, true);
	}

	private void setActualFragment(Fragment frgmnt, boolean addToStack) {
		if (frgmnt == null) {
			return;
		}
		if (addToStack) {
			if (fragments.size() > 0) {
				Fragment top = fragments.pop();
				if (!fragments.contains(frgmnt) && !frgmnt.equals(top)) {
					fragments.push(top);
					fragments.push(frgmnt);
				} else {
					fragments.push(top);
					if (fragments.contains(frgmnt)) {
						top = null;
						while (!frgmnt.equals(top)) {
							top = fragments.pop();
						}
						fragments.push(frgmnt);
					}
				}
			} else if (fragments.size() == 0) {
				fragments.push(frgmnt);
			}
		}
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.replace(R.id.content_frame, frgmnt);
		transaction.commit();
		getSlidingMenu().showContent();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if (fragments.size() <= 1) {
				closeCallback.Method(this);
				return false;// super.onKeyDown(keyCode, event);
			} else {
				fragments.pop();
				setActualFragment(fragments.pop());
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
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
