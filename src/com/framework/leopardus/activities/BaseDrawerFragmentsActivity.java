package com.framework.leopardus.activities;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher.OnRefreshListener;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.View;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.framework.leopardus.R;
import com.framework.leopardus.adapters.ApplicationMenuItem;
import com.framework.leopardus.enums.Ubications;
import com.framework.leopardus.fragments.BaseFragmentDrawer;
import com.framework.leopardus.fragments.InitialFragmentDrawer;
import com.framework.leopardus.interfaces.ActivityMethodInterface;
import com.framework.leopardus.interfaces.MenuItemEvent;
import com.framework.leopardus.interfaces.injection.InjectActionBarItem;
import com.framework.leopardus.utils.GenericActionMode;
import com.framework.leopardus.utils.Injector;
import com.framework.leopardus.utils.ProgressDialogHelper;

@SuppressLint("UseSparseArrays")
public class BaseDrawerFragmentsActivity extends SherlockFragmentActivity {

	private InitialFragmentDrawer instance = null;
	private ActionMode actionMode;
	private boolean enableProgressFeatures = false;
	private ProgressDialogHelper pdHelper = new ProgressDialogHelper();
	private PullToRefreshAttacher pullToRefreshAttacher = null;
	private Map<Integer, InjectActionBarItem> abItems = new HashMap<Integer, InjectActionBarItem>();
	private Map<Integer, Runnable> abMethods = new HashMap<Integer, Runnable>();

	public int addNewActionBarItem(BaseDrawerFragmentsActivity obj,
			InjectActionBarItem i) {
		abItems.put(abItems.size(), i);
		return abItems.size() - 1;
	}

	public void addNewActionBarItem(int menuId, Runnable r) {
		abMethods.put(menuId, r);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Injector i = new Injector(this);
		i.injectActionBarItems(this);
		super.onCreate(savedInstanceState);
		if (enableProgressFeatures) {
			requestWindowFeature(Window.FEATURE_PROGRESS);
			requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		}
		setContentView(R.layout.activity_drawer);
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		if (enableProgressFeatures) {
			setSupportProgressBarIndeterminateVisibility(false);
			setSupportProgressBarVisibility(false);
		}
		setInitialFragment();
		i.injectViews(this);
		i.injectMethodsIntoViews(this);
		i.injectMenuItems(this);
	}

	private void setInitialFragment() {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		instance = InitialFragmentDrawer.newInstance();
		fragmentTransaction.add(R.id.drawer_content, instance).commit();
	}
	
	@Override
	protected void onPause() {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		instance = InitialFragmentDrawer.newInstance();
		fragmentTransaction.remove(instance);
		instance = null;
		super.onPause();
	}

	public void addFragment(BaseFragmentDrawer frgmnt) {
		if (instance != null) {
			instance.addFragment(R.string.hello_world, frgmnt);
		}
	}

	public int addNewItem(BaseDrawerFragmentsActivity obj, int stringId,
			int iconId) {
		return addNewItem(obj, stringId, iconId, Ubications.LEFT);
	}

	public int addNewItem(BaseDrawerFragmentsActivity obj, int stringId,
			int iconId, Ubications u) {
		return instance.addMenuItem(stringId, iconId, u);
	}

	public void addNewEvent(int menuId, Ubications ubication,
			MenuItemEvent menuItemEvent) {
		instance.addNewEvent(menuId, ubication, menuItemEvent);
	}

	Stack<Fragment> fragments = new Stack<Fragment>();
	
	public void setActualFragment(Fragment frgmnt, boolean addToStack) {		
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
		if (instance != null && frgmnt instanceof BaseFragmentDrawer) {
			instance.addFragment(R.string.hello_world,
					(BaseFragmentDrawer) frgmnt);
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
				instance.getOnCloseEvent().Method(this);
				return false;// super.onKeyDown(keyCode, event);
			} else {
				fragments.pop();
				setActualFragment(fragments.pop());
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
	}


	public void close() {
		instance.getOnCloseEvent().Method(this);
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

	public void setOnCloseEvent(ActivityMethodInterface ami) {
		instance.setOnCloseEvent(ami);
	}

	public void setLeftMenuEnabled(boolean leftMenuEnabled) {
		instance.setLeftMenuEnabled(leftMenuEnabled);
	}

	public void setRightMenuEnabled(boolean rightMenuEnabled) {
		instance.setRightMenuEnabled(rightMenuEnabled);
	}

	/**
	 * Enable home icon on action bar as button
	 */
	public void enableHomeAsButton() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	/**
	 * Enable toogle Menu on Home button click
	 */
	public void setEnabledMenuOnHomeButton() {
		instance.setEnabledMenuOnHomeButton();
	}

	/**
	 * Disable toogle Menu on Home button click
	 */
	public void setDisabledMenuOnHomeButton() {
		instance.setDisabledMenuOnHomeButton();
	}

	/**
	 * Return if is enabled toogle menu on home
	 * 
	 * @return
	 */
	public boolean isEnabledMenuOnHomeButton() {
		return instance.isEnabledMenuOnHomeButton();
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

	public ProgressDialogHelper getProgressDialogHelper() {
		return pdHelper;
	}

	public void setEnableProgressFeatures(boolean enableProgressFeatures) {
		this.enableProgressFeatures = enableProgressFeatures;
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
		instance.enableAutoExit();
	}

	public void disableAutoExit() {
		instance.disableAutoExit();
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

	public void injectActionBarItems(Menu menu) {
		for (int key : abItems.keySet()) {
			InjectActionBarItem i = abItems.get(key);
			MenuItem mi = menu.add(Menu.NONE, key, i.itemPosition(),
					i.stringId());
			mi.setIcon(i.iconId());
			if (i.ItemAsIcon()) {
				mi.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
			} else {
				mi.setShowAsAction(i.showAs());
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		injectActionBarItems(menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if (instance.isEnabledMenuOnHomeButton()) {
				instance.toggleLeftMenu();
			}
			break;
		default:
			try {
				abMethods.get(item.getItemId()).run();
			} catch (Exception e) {

			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenu.ContextMenuInfo menuInfo) {
		// menuonOptionsItemSelectednu.add("Four");
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
