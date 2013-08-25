package com.framework.leopardus.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.framework.leopardus.R;
import com.framework.leopardus.fragments.BaseFragmentDrawer;
import com.framework.leopardus.fragments.BaseMenuDrawer;
import com.framework.leopardus.fragments.BaseMenuFragment;
import com.framework.leopardus.utils.Injector;
import com.sherlock.navigationdrawer.compat.SherlockActionBarDrawerToggle;

public class BaseDrawerFragmentsActivity extends SherlockFragmentActivity {

	private int layout = R.layout.activity_drawer;
	FragmentManager fragmentManager;
	private boolean enableMenuOnHome = false;
	// ////////////////////////////////////
	private DrawerLayout drawerLayout;
	private SherlockActionBarDrawerToggle drawerToggle;
	private ActionBarHelper actionBarHelper;
	private ListView listView;
	BaseMenuDrawer baseMenuDrawer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(layout);
		getFragmentmanager();
		// ///////////////////////
		LayoutInflater inflater = getLayoutInflater();
		FrameLayout drawer_content = (FrameLayout) findViewById(R.id.drawer_content);
		View view = inflater.inflate(R.layout.activity_fragment_drawer,
				drawer_content, false);
		drawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
		drawerLayout.setDrawerListener(new DrawerListener());
		drawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// ///////////////////////
		listView = (ListView) view.findViewById(R.id.left_drawer);
		baseMenuDrawer = new BaseMenuDrawer(this, listView);
		listView.setOnItemClickListener(new DrawerItemClickListener());
		listView.setCacheColorHint(0);
		listView.setScrollingCacheEnabled(false);
		listView.setScrollContainer(false);
		listView.setFastScrollEnabled(true);
		listView.setSmoothScrollbarEnabled(true);

		actionBarHelper = createActionBarHelper();
		actionBarHelper.init();
		drawerToggle = new SherlockActionBarDrawerToggle(this, drawerLayout,
				R.drawable.ic_drawer_light, R.string.drawer_open,
				R.string.drawer_close);
		drawerToggle.syncState();

		Injector i = new Injector(this);
		i.injectViews(this);
		i.injectMethodsIntoViews(this);
		i.injectMenuItems(this);
	}

	private FragmentManager getFragmentmanager() {
		if (fragmentManager == null) {
			fragmentManager = getSupportFragmentManager();
		}
		return fragmentManager;
	}

	/**
	 * Set the actual fragment
	 * 
	 * @param frgmnt
	 */
	public void setActualFragment(BaseFragmentDrawer frgmnt) {
		FragmentTransaction trans = getFragmentmanager().beginTransaction();
		trans.add(R.id.drawer_content, frgmnt).commit();
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if (this.enableMenuOnHome) {
				// toggle();
				return true;
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	// ///////////////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////////////

	/**
	 * A drawer listener can be used to respond to drawer events such as
	 * becoming fully opened or closed. You should always prefer to perform
	 * expensive operations such as drastic relayout when no animation is
	 * currently in progress, either before or after the drawer animates.
	 * 
	 * When using ActionBarDrawerToggle, all DrawerLayout listener methods
	 * should be forwarded if the ActionBarDrawerToggle is not used as the
	 * DrawerLayout listener directly.
	 */
	private class DrawerListener implements DrawerLayout.DrawerListener {
		@Override
		public void onDrawerOpened(View drawerView) {
			drawerToggle.onDrawerOpened(drawerView);
			actionBarHelper.onDrawerOpened();
		}

		@Override
		public void onDrawerClosed(View drawerView) {
			drawerToggle.onDrawerClosed(drawerView);
			actionBarHelper.onDrawerClosed();
		}

		@Override
		public void onDrawerSlide(View drawerView, float slideOffset) {
			drawerToggle.onDrawerSlide(drawerView, slideOffset);
		}

		@Override
		public void onDrawerStateChanged(int newState) {
			drawerToggle.onDrawerStateChanged(newState);
		}
	}

	/**
	 * Create a compatible helper that will manipulate the action bar if
	 * available.
	 */
	private ActionBarHelper createActionBarHelper() {
		return new ActionBarHelper();
	}

	private class ActionBarHelper {
		private final ActionBar actionBar;
		private CharSequence drawerTitle;
		private CharSequence title;

		private ActionBarHelper() {
			actionBar = BaseDrawerFragmentsActivity.this.getSupportActionBar();
		}

		public void init() {
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setHomeButtonEnabled(true);
			title = drawerTitle = BaseDrawerFragmentsActivity.this.getTitle();
		}

		/**
		 * When the drawer is closed we restore the action bar state reflecting
		 * the specific contents in view.
		 */
		public void onDrawerClosed() {
			actionBar.setTitle(title);
		}

		/**
		 * When the drawer is open we set the action bar to a generic title. The
		 * action bar should only contain data relevant at the top level of the
		 * nav hierarchy represented by the drawer, as the rest of your content
		 * will be dimmed down and non-interactive.
		 */
		public void onDrawerOpened() {
			actionBar.setTitle(drawerTitle);
		}

		public void setTitle(CharSequence title) {
			title = title;
		}
	}

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// mContent.setText("Content "+position);
			actionBarHelper.setTitle("Title " + position);
			drawerLayout.closeDrawer(listView);
		}
	}

	public BaseMenuDrawer getMenu() {
		return baseMenuDrawer;
	}

	public void setActualFragment(Fragment fragment) {
		// TODO Auto-generated method stub

	}

	public void close() {
		finish();
	}

}
