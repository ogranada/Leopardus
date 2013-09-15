package com.framework.leopardus.activities;

import java.util.Map;
import java.util.Stack;

import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher.OnRefreshListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.View;

import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.framework.leopardus.R;
import com.framework.leopardus.adapters.ApplicationMenuItem;
import com.framework.leopardus.enums.Ubications;
import com.framework.leopardus.fragments.BaseFragment;
import com.framework.leopardus.fragments.BaseMenuFragment;
import com.framework.leopardus.interfaces.ActivityMethodInterface;
import com.framework.leopardus.utils.GenericActionMode;
import com.framework.leopardus.utils.Injector;
import com.framework.leopardus.utils.InterfacesHelper;
import com.framework.leopardus.utils.ProgressDialogHelper;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class BaseFragmentsActivity extends SlidingFragmentActivity {

	private ActionMode actionMode;
	private BaseMenuFragment menuFragment;
	private BaseMenuFragment menuFragmentRight;
	private SlidingMenu slidingMenu;
	private boolean enableMenuOnHome = false;
	private FragmentManager fragmentManager = getSupportFragmentManager();
	private ProgressDialogHelper pdHelper = new ProgressDialogHelper();
	private PullToRefreshAttacher pullToRefreshAttacher = null;
	private boolean enableProgressFeatures = false;
	Stack<Fragment> fragments = new Stack<Fragment>();
	ActivityMethodInterface closeCallback = InterfacesHelper.getCloseMethod();
	private boolean leftMenuEnabled = true;
	private boolean rightMenuEnabled = false;

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

	public ProgressDialogHelper getProgressDialogHelper() {
		return pdHelper;
	}

	public void setEnableProgressFeatures(boolean enableProgressFeatures) {
		this.enableProgressFeatures = enableProgressFeatures;
	}

	/**
	 * Init and return the PullToRefreshAttacher
	 * 
	 * @return PullToRefreshAttacher instance
	 */
	public PullToRefreshAttacher PullToRefreshInit() {
		if (pullToRefreshAttacher == null) {
			pullToRefreshAttacher = PullToRefreshAttacher.get(this);
		}
		return pullToRefreshAttacher;
	}

	/**
	 * Set the refresh listener for the provided view
	 * 
	 * @param view
	 * @param onRefreshListener
	 */
	public void estabilishRereshForView(View view,
			OnRefreshListener onRefreshListener) {
		if (pullToRefreshAttacher == null) {
			PullToRefreshInit();
		}
		pullToRefreshAttacher.addRefreshableView(view, onRefreshListener);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (enableProgressFeatures) {
			requestWindowFeature(Window.FEATURE_PROGRESS);
			requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		}
		setBehindContentView(R.layout.menu_frame);
		if (enableProgressFeatures) {
			setSupportProgressBarIndeterminateVisibility(false);
			setSupportProgressBarVisibility(false);
		}
		FragmentTransaction t = this.getSupportFragmentManager()
				.beginTransaction();
		menuFragment = new BaseMenuFragment();
		menuFragmentRight = new BaseMenuFragment();
		menuFragmentRight.disableAutoExit();
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

		enabledMenuVerification();
		
		// ///////////////////////////////////////////
		Injector i = new Injector(this);
		i.injectMenuItems(this);
		i.injectViews(this);
		i.injectMethodsIntoViews(this);
	}

	private void enabledMenuVerification() {
		if (leftMenuEnabled && rightMenuEnabled) {
			slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
			slidingMenu.setSecondaryMenu(R.layout.menu_frame_two);
		} else if (leftMenuEnabled && !rightMenuEnabled) {
			slidingMenu.setMode(SlidingMenu.LEFT);
		}else if (!leftMenuEnabled && rightMenuEnabled) {
			slidingMenu.setMode(SlidingMenu.LEFT_OF);
			slidingMenu.setSecondaryMenu(R.layout.menu_frame_two);
		}
		
		if (rightMenuEnabled) {
			getSupportFragmentManager().beginTransaction()
			.replace(R.id.second_menu_frame, menuFragmentRight).commit();			
		}
	}

	/**
	 * Set the actual fragment
	 * 
	 * @param frgmnt
	 */
	public void setActualFragment(Fragment frgmnt) {
		setActualFragment(frgmnt, true);
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

	/**
	 * Enable toogle Menu on Home button click
	 */
	public void setEnabledMenuOnHomeButton() {
		this.enableMenuOnHome = true;
	}

	/**
	 * Disable toogle Menu on Home button click
	 */
	public void setDisabledMenuOnHomeButton() {
		this.enableMenuOnHome = true;
	}

	/**
	 * Return if is enabled toogle menu on home
	 * 
	 * @return
	 */
	public boolean isEnabledMenuOnHomeButton() {
		return enableMenuOnHome;
	}

	/**
	 * Return the left activity menu
	 * 
	 * @return
	 */
	public BaseMenuFragment getMenu() {
		return getMenu(Ubications.LEFT);
	}

	/**
	 * Return the specified activity menu
	 * 
	 * @param ubication
	 * @return
	 */
	public BaseMenuFragment getMenu(Ubications ubication) {
		return ubication.equals(Ubications.LEFT) ? menuFragment
				: menuFragmentRight;
	}

	public void close() {
		closeCallback.Method(this);
	}

	public void setOnCloseEvent(ActivityMethodInterface ami) {
		closeCallback = ami;
	}

	/**
	 * Enable home icon on action bar as button
	 */
	public void enableHomeAsButton() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	/**
	 * Disable home icon on action bar as button
	 */
	public void disableHomeAsButton() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
	}

	public void showProgressBar() {
		if (enableProgressFeatures) {
			setSupportProgressBarVisibility(true);
		}
	}

	public void showIndeterminateProgressBar() {
		if (enableProgressFeatures) {
			setSupportProgressBarIndeterminateVisibility(true);
		}
	}

	public void hideProgressBar() {
		if (enableProgressFeatures) {
			setSupportProgressBarVisibility(false);
		}
	}

	public void hideIndeterminateProgressBar() {
		if (enableProgressFeatures) {
			setSupportProgressBarIndeterminateVisibility(false);
		}
	}

	public void enableAutoExit() {
		getMenu().enableAutoExit();
	}

	public void disableAutoExit() {
		getMenu().disableAutoExit();
	}

	public void setLeftMenuEnabled(boolean leftMenuEnabled) {
		this.leftMenuEnabled = leftMenuEnabled;
		enabledMenuVerification();
	}

	public void setRightMenuEnabled(boolean rightMenuEnabled) {
		this.rightMenuEnabled = rightMenuEnabled;
		enabledMenuVerification();
	}

	// //////////////////////////////////////////////////////

	public void startActionMode(
			Map<ApplicationMenuItem, ActivityMethodInterface> items) {
		super.startActionMode(new GenericActionMode(this, items));
	}

	public void stopActionMode() {
		if (actionMode != null) {
			actionMode.finish();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// menu.add("Save")
		// .setIcon(R.drawable.ic_drawer_dark)
		// .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		return super.onCreateOptionsMenu(menu);
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

	public boolean _onOptionsItemSelected(MenuItem item) {
		// This uses the imported MenuItem from ActionBarSherlock
		// Toast.makeText(this, "Got click: " + item.toString(),
		// Toast.LENGTH_SHORT).show();
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenu.ContextMenuInfo menuInfo) {
		// menu.add("One");
		// menu.add("Two");
		// menu.add("Three");
		// menu.add("Four");
	}

	@Override
	public boolean onContextItemSelected(android.view.MenuItem item) {
		// Note how this callback is using the fully-qualified class name
		// Toast.makeText(this, "Got click: " + item.toString(),
		// Toast.LENGTH_SHORT).show();
		return true;
	}

	// registerForContextMenu(findViewById(R.id.show_context_menu)); // TODO:

}
