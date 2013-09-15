package com.framework.leopardus.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.framework.leopardus.R;
import com.framework.leopardus.adapters.ApplicationMenuItem;
import com.framework.leopardus.adapters.ItemsAdapter;
import com.framework.leopardus.enums.Ubications;
import com.framework.leopardus.interfaces.ActivityMethodInterface;
import com.framework.leopardus.interfaces.MenuItemEvent;
import com.framework.leopardus.utils.InterfacesHelper;
import com.sherlock.navigationdrawer.compat.SherlockActionBarDrawerToggle;

@SuppressLint("UseSparseArrays")
public class InitialFragmentDrawer extends SherlockFragment {

	List<Integer[]> menuItemsLeft = new ArrayList<Integer[]>(0);
	List<Integer[]> menuItemsRight = new ArrayList<Integer[]>(0);
	private Map<Integer, MenuItemEvent> menuEventsLeft = new HashMap<Integer, MenuItemEvent>(
			0);
	private Map<Integer, MenuItemEvent> menuEventsRight = new HashMap<Integer, MenuItemEvent>(
			0);
	private boolean autoExit = false;
	private Activity activity;
	private DrawerLayout mDrawerLayout;
	private ListView listViewLeft;
	private ListView listViewRight;
	private int title = -1;
	// private TextView mContent;
	boolean leftMenuEnabled = true;
	boolean rightMenuEnabled = false;
	// /////////////////////////
	boolean leftMenuOpened = false;
	boolean rightMenuOpened = false;

	private ActionBarHelper mActionBar;

	private SherlockActionBarDrawerToggle mDrawerToggle; // TODO
	private android.view.ViewGroup.LayoutParams rightMenuParams;
	private android.view.ViewGroup.LayoutParams leftMenuParams;
	private boolean enableMenuOnHome;
	ActivityMethodInterface closeCallback = InterfacesHelper.getCloseMethod();

	public static InitialFragmentDrawer newInstance() {
		return new InitialFragmentDrawer();
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
		View view = inflater.inflate(R.layout.activity_fragment_drawer,
				container, false);
		mDrawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
		listViewLeft = (ListView) view.findViewById(R.id.drawer_menu_left);
		listViewRight = (ListView) view.findViewById(R.id.drawer_menu_right);
		mDrawerLayout.setDrawerListener(new DemoDrawerListener());
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		activity = getActivity();
		establishItemsAdapter();
		if (leftMenuEnabled) {
			listViewLeft.setOnItemClickListener(new DrawerItemClickListener() {

				@Override
				protected Ubications getUbication() {
					return Ubications.LEFT;
				}
			});
			listViewLeft.setCacheColorHint(0);
			listViewLeft.setScrollingCacheEnabled(false);
			listViewLeft.setScrollContainer(false);
			listViewLeft.setFastScrollEnabled(true);
			listViewLeft.setSmoothScrollbarEnabled(true);
		} else {
			this.rightMenuParams = listViewLeft.getLayoutParams();
			mDrawerLayout.removeView(listViewLeft);
		}
		if (rightMenuEnabled) {
			listViewRight.setOnItemClickListener(new DrawerItemClickListener() {

				@Override
				protected Ubications getUbication() {
					return Ubications.RIGHT;
				}
			});
			listViewRight.setCacheColorHint(0);
			listViewRight.setScrollingCacheEnabled(false);
			listViewRight.setScrollContainer(false);
			listViewRight.setFastScrollEnabled(true);
			listViewRight.setSmoothScrollbarEnabled(true);
		} else {
			this.rightMenuParams = listViewRight.getLayoutParams();
			mDrawerLayout.removeView(listViewRight);
		}

		mActionBar = createActionBarHelper();
		mActionBar.init();

		if (title != -1) {
			mActionBar.setTitle(activity.getResources().getString(title));
			title = -1;
		}
		mActionBar.setHomeButtonEnabled(enableMenuOnHome);
		// ActionBarDrawerToggle provides convenient helpers for tying together
		// the
		// prescribed interactions between a top-level sliding drawer and the
		// action bar.
		mDrawerToggle = new SherlockActionBarDrawerToggle(this.getActivity(),
				mDrawerLayout, R.drawable.ic_drawer_light,
				R.string.drawer_open, R.string.drawer_close);
		mDrawerToggle.syncState();
		return view;
	}

	private void establishItemsAdapter() {
		ItemsAdapter leftAdapter = new ItemsAdapter(activity);
		ItemsAdapter rightAdapter = new ItemsAdapter(activity);
		int size = menuItemsLeft.size();
		for (int i = 0; i < size; i++) {
			Integer[] item = menuItemsLeft.get(i);
			leftAdapter.add(new ApplicationMenuItem(item[0], item[1]));
		}
		size = menuItemsRight.size();
		for (int i = 0; i < size; i++) {
			Integer[] item = menuItemsRight.get(i);
			rightAdapter.add(new ApplicationMenuItem(item[0], item[1]));
		}
		// if (autoExit) {
		// menuItemsLeft.add(new Integer[]{R.string.quit,
		// R.drawable.ico_dark_quit});
		// leftAdapter.add(new ApplicationMenuItem(R.string.quit,
		// R.drawable.ico_dark_quit));
		// // menuEventsLeft.put(le, value)
		// addMenuItem(R.string.quit,
		// R.drawable.ico_dark_quit, Ubications.LEFT);
		// }
		// TODO:
		if (autoExit) {
			int pos = addMenuItem(R.string.quit, R.drawable.ico_dark_quit,
					Ubications.LEFT);
			// int pos = menuEventsLeft.size();
			leftAdapter.add(new ApplicationMenuItem(R.string.quit,
					R.drawable.ico_dark_quit));
			addNewEvent(pos, Ubications.LEFT, new MenuItemEvent() {

				@Override
				public void onListItemClick(Object lv, View v, long id) {
					closeCallback.Method(getActivity());
				}
			});
		}

		listViewLeft.setAdapter(leftAdapter);
		listViewRight.setAdapter(rightAdapter);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater = ((SherlockFragmentActivity) getActivity())
				.getSupportMenuInflater();
		inflater.inflate(R.menu.main, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		/*
		 * The action bar home/up action should open or close the drawer.
		 * mDrawerToggle will take care of this.
		 */
		try {
			if (mDrawerToggle.onOptionsItemSelected(item)) {
				return true;
			}
		} catch (Exception e) {
			Log.e("Leopardus",
					"Trying toggle left menu, but it doesn't enabled");
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	/**
	 * This list item click listener implements very simple view switching by
	 * changing the primary content text. The drawer is closed when a selection
	 * is made.
	 */
	private abstract class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		// listViewLeft lv, View v, int position, long id
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Map<Integer, MenuItemEvent> menuEvents = menuEventsLeft;
			ListView listView = listViewLeft;
			String ttl = "";
			if (getUbication().equals(Ubications.RIGHT)) {
				menuEvents = menuEventsRight;
				ttl = activity.getResources().getString(
						menuItemsRight.get(position)[0]);
				listView = listViewRight;
			} else {
				menuEvents = menuEventsLeft;
				ttl = activity.getResources().getString(
						menuItemsLeft.get(position)[0]);
				listView = listViewLeft;
			}
			if (menuEvents.containsKey(Integer.valueOf(position))) {
				mActionBar.setTitle(ttl);
				MenuItemEvent evt = menuEvents.get(position);
				evt.onListItemClick(parent, view, id);
				mDrawerLayout.closeDrawer(listView);
			}
		}

		protected abstract Ubications getUbication();

	}

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
	private class DemoDrawerListener implements DrawerLayout.DrawerListener {
		@Override
		public void onDrawerOpened(View drawerView) {
			mDrawerToggle.onDrawerOpened(drawerView);
			mActionBar.onDrawerOpened();
			if (drawerView.equals(listViewLeft)) {
				leftMenuOpened = true;
			} else {
				rightMenuOpened = true;
			}
		}

		@Override
		public void onDrawerClosed(View drawerView) {
			mDrawerToggle.onDrawerClosed(drawerView);
			mActionBar.onDrawerClosed();
			if (drawerView.equals(listViewLeft)) {
				leftMenuOpened = false;
			} else {
				rightMenuOpened = false;
			}
		}

		@Override
		public void onDrawerSlide(View drawerView, float slideOffset) {
			mDrawerToggle.onDrawerSlide(drawerView, slideOffset);
		}

		@Override
		public void onDrawerStateChanged(int newState) {
			mDrawerToggle.onDrawerStateChanged(newState);
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
		private final ActionBar mActionBar;
		private CharSequence mDrawerTitle;
		private CharSequence mTitle;

		private ActionBarHelper() {
			mActionBar = ((SherlockFragmentActivity) getActivity())
					.getSupportActionBar();
		}

		public void init() {
			// mActionBar.setDisplayHomeAsUpEnabled(true);
			mActionBar.setHomeButtonEnabled(enableMenuOnHome);
			mTitle = mDrawerTitle = getActivity().getTitle();
		}

		public void setHomeButtonEnabled(boolean enabled) {
			mActionBar.setHomeButtonEnabled(enabled);
		}

		/**
		 * When the drawer is closed we restore the action bar state reflecting
		 * the specific contents in view.
		 */
		public void onDrawerClosed() {
			mActionBar.setTitle(mTitle);
		}

		/**
		 * When the drawer is open we set the action bar to a generic title. The
		 * action bar should only contain data relevant at the top level of the
		 * nav hierarchy represented by the drawer, as the rest of your content
		 * will be dimmed down and non-interactive.
		 */
		public void onDrawerOpened() {
			mActionBar.setTitle(mDrawerTitle);
		}

		public void setTitle(CharSequence title) {
			mTitle = title;
		}
	}

	public int addMenuItem(int stringId, int iconId) {
		return addMenuItem(stringId, iconId, Ubications.LEFT);
	}

	public int addMenuItem(int stringId, int iconId, Ubications ubication) {
		List<Integer[]> menuItems = ubication.equals(Ubications.LEFT) ? menuItemsLeft
				: menuItemsRight;
		menuItems.add(new Integer[] { stringId, iconId, ubication.ordinal() });
		return menuItems.size() - 1;
	}

	public void addFragment(int stringId, BaseFragmentDrawer bfd) {
		getFragmentManager().beginTransaction()
				.replace(R.id.content_section, bfd).commit();
		if (mActionBar != null) {
			mActionBar.setTitle(activity.getResources().getString(stringId));
		} else {
			title = stringId;
		}
		if (mDrawerLayout != null) {
			mDrawerLayout.closeDrawer(listViewLeft);
		}
	}

	public void addNewEvent(int menuId, Ubications ubication,
			MenuItemEvent menuItemEvent) {
		if (ubication.equals(Ubications.LEFT)) {
			menuEventsLeft.put(menuId, menuItemEvent);
		} else {
			menuEventsRight.put(menuId, menuItemEvent);
		}
	}

	public void setLeftMenuEnabled(boolean leftMenuEnabled) {
		this.leftMenuEnabled = leftMenuEnabled;
		if (mDrawerLayout != null) {
			if (!leftMenuEnabled) {
				mDrawerLayout.removeView(listViewLeft);
				this.leftMenuParams = listViewLeft.getLayoutParams();
			} else {
				mDrawerLayout.addView(listViewRight, this.leftMenuParams);
			}
		}
	}

	public void setRightMenuEnabled(boolean rightMenuEnabled) {
		this.rightMenuEnabled = rightMenuEnabled;
		if (mDrawerLayout != null) {
			if (!rightMenuEnabled) {
				this.rightMenuParams = listViewRight.getLayoutParams();
				mDrawerLayout.removeView(listViewRight);
			} else {
				mDrawerLayout.addView(listViewRight, this.rightMenuParams);
			}
		}
	}

	/**
	 * Enable toogle Menu on Home button click
	 */
	public void setEnabledMenuOnHomeButton() {
		this.enableMenuOnHome = true;
		if (mActionBar != null) {
			mActionBar.setHomeButtonEnabled(enableMenuOnHome);
		}
	}

	/**
	 * Disable toogle Menu on Home button click
	 */
	public void setDisabledMenuOnHomeButton() {
		this.enableMenuOnHome = false;
		if (mActionBar != null) {
			mActionBar.setHomeButtonEnabled(enableMenuOnHome);
		}
	}

	/**
	 * Return if is enabled toogle menu on home
	 * 
	 * @return
	 */
	public boolean isEnabledMenuOnHomeButton() {
		return enableMenuOnHome;
	}

	public void toggleLeftMenu() {
		if (leftMenuOpened) {
			mDrawerLayout.closeDrawer(listViewLeft);
		} else {
			mDrawerLayout.openDrawer(listViewLeft);
		}
	}

	public void toggleRightMenu() {
		if (rightMenuOpened) {
			mDrawerLayout.closeDrawer(listViewRight);
		} else {
			mDrawerLayout.openDrawer(listViewRight);
		}
	}

	public void enableAutoExit() {
		autoExit = true;
	}

	public void disableAutoExit() {
		autoExit = false;
	}

	public void setOnCloseEvent(ActivityMethodInterface ami) {
		closeCallback = ami;
	}

}
