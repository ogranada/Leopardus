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

public class BaseFragmentNavigationDrawer extends SherlockFragment {

	private DrawerLayout drawerLayout;
	private ListView listView;
	private SherlockActionBarDrawerToggle drawerToggle;
	private ActionBarHelper actionBarHelper;


	private TextView mContent;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = null;
		view = inflater.inflate(R.layout.activity_fragment_drawer, container,
				false);
		drawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
		listView = (ListView) view.findViewById(R.id.left_drawer);
		//////////////// ACTIVITY CONTENT ///////////////////
		mContent = (TextView) view.findViewById(R.id.content_text);
		/////////////////////////////////////////////////////
		drawerLayout.setDrawerListener(new DrawerListener());
		drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		listView.setAdapter(new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, "Title a,Title b,Title c,Title d".split(",")));
		listView.setOnItemClickListener(new DrawerItemClickListener());
		listView.setCacheColorHint(0);
		listView.setScrollingCacheEnabled(false);
		listView.setScrollContainer(false);
		listView.setFastScrollEnabled(true);
		listView.setSmoothScrollbarEnabled(true);

		actionBarHelper = createActionBarHelper();
		actionBarHelper.init();

		// ActionBarDrawerToggle provides convenient helpers for tying together
		// the
		// prescribed interactions between a top-level sliding drawer and the
		// action bar.
		drawerToggle = new SherlockActionBarDrawerToggle(this.getActivity(), drawerLayout, R.drawable.ic_drawer_light, R.string.drawer_open, R.string.drawer_close);
		drawerToggle.syncState();
		
		return view;
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
			actionBar = ((SherlockFragmentActivity)getActivity()).getSupportActionBar();
		}

		public void init() {
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setHomeButtonEnabled(true);
			title = drawerTitle = getActivity().getTitle();
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


	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			mContent.setText("Content "+position);
			actionBarHelper.setTitle("Title "+position);
			drawerLayout.closeDrawer(listView);
		}
	}

}
