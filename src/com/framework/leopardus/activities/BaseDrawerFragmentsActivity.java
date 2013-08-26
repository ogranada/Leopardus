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
import com.framework.leopardus.fragments.InitialFragmentDrawer;
import com.framework.leopardus.interfaces.MenuItemEvent;
import com.framework.leopardus.utils.Injector;
import com.sherlock.navigationdrawer.compat.SherlockActionBarDrawerToggle;

public class BaseDrawerFragmentsActivity extends SherlockFragmentActivity {

	InitialFragmentDrawer instance = null;
	FragmentManager fragmentManager = getSupportFragmentManager();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drawer);
		setInitialFragment();
		Injector i = new Injector(this);
		i.injectViews(this);
		i.injectMethodsIntoViews(this);
		i.injectMenuItems(this);
	}

	private void setInitialFragment() {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		instance = InitialFragmentDrawer.newInstance();
		fragmentTransaction.add(R.id.drawer_content, instance).commit();
	}

	public void addFragment(BaseFragmentDrawer frgmnt){
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//		fragmentTransaction.add(R.id.drawer_content, frgmnt).commit();
	}

	public int addNewItem(BaseDrawerFragmentsActivity obj, int stringId,
			int iconId) {
		return instance.addMenuItem(stringId, iconId);
	}

	public void addNewEvent(int menuId, MenuItemEvent menuItemEvent) {
		// TODO Auto-generated method stub
		
	}

	public void setActualFragment(Fragment fragment) {
		
	}

	public void close() {
		finish();
	}
	
}
