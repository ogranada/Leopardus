package com.framework.leopardus.activities;

import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.ContextMenu;
import android.view.View;
import android.widget.Toast;

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
import com.framework.leopardus.utils.GenericActionMode;
import com.framework.leopardus.utils.Injector;
import com.framework.leopardus.utils.ProgressDialogHelper;

public class BaseDrawerFragmentsActivity extends SherlockFragmentActivity {

	private InitialFragmentDrawer instance = null;
	private ActionMode actionMode;
	private boolean enableProgressFeatures = false;
	private ProgressDialogHelper pdHelper = new ProgressDialogHelper();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
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
		Injector i = new Injector(this);
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

	public void setActualFragment(Fragment fragment) {
		if (instance != null && fragment instanceof BaseFragmentDrawer) {
			instance.addFragment(R.string.hello_world,
					(BaseFragmentDrawer) fragment);
		}
	}

	public void close() {
		finish();
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
	////////////////////////////////////////////////////////
	
	
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
//		 menu.add("Save")
//		 .setIcon(R.drawable.ic_drawer_dark)
//		 .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// This uses the imported MenuItem from ActionBarSherlock
		// Toast.makeText(this, "Got click: " + item.toString(),
		// Toast.LENGTH_SHORT).show();
		switch (item.getItemId()) {
		case android.R.id.home:
			if(instance.isEnabledMenuOnHomeButton()){
				instance.toggleLeftMenu();
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenu.ContextMenuInfo menuInfo) {
//		 menuonOptionsItemSelectednu.add("Four");
	}

	@Override
	public boolean onContextItemSelected(android.view.MenuItem item) {
		// Note how this callback is using the fully-qualified class name
//		 Toast.makeText(this, "Got click: " + item.toString(),
//		 Toast.LENGTH_SHORT).show();
		return true;
	}

	// registerForContextMenu(findViewById(R.id.show_context_menu)); // TODO:

}
