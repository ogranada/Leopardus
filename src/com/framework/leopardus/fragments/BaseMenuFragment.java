package com.framework.leopardus.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.framework.leopardus.R;
import com.framework.leopardus.activities.BaseFragmentsActivity;
import com.framework.leopardus.adapters.ApplicationMenuItem;
import com.framework.leopardus.adapters.ItemsAdapter;
import com.framework.leopardus.interfaces.MenuItemEvent;

public class BaseMenuFragment extends ListFragment {

	private List<Integer[]> menuItemsList = new ArrayList<Integer[]>(0);
	private Map<Integer, MenuItemEvent> menuEvents = new HashMap<Integer, MenuItemEvent>(0);
	private boolean autoExit = true;
	private Activity activity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		activity = getActivity();
		establishItemsAdapter();
	}

	private void establishItemsAdapter() {
		if (activity != null) {
			ItemsAdapter adapter = new ItemsAdapter(activity);
			int size = menuItemsList.size();
			for (int i = 0; i < size; i++) {
				Integer[] item = menuItemsList.get(i);
				adapter.add(new ApplicationMenuItem(item[0], item[1]));
			}
			if (autoExit) {
				adapter.add(new ApplicationMenuItem(R.string.quit,
						R.drawable.ico_dark_quit));
			}
			setListAdapter(adapter);
		}
	}

	/**
	 * Disable the exit Item
	 */
	public void enableAutoExit() {
		autoExit = true;
	}

	/**
	 * Disable the exit Item
	 */
	public void disableAutoExit() {
		autoExit = true;
	}

	/**
	 * Add item to sliding menu
	 * 
	 * @param stringId
	 * @param drawableId
	 * @return Item Identifier if operation was successful otherwise return -1
	 */
	public Integer addNewItem(Context context, int stringId, int drawableId) {
		try {
			context.getResources().getString(stringId);
			context.getResources().getDrawable(drawableId);
			menuItemsList.add(new Integer[] { stringId, drawableId });
			establishItemsAdapter();
			return menuItemsList.size() - 1;
		} catch (Exception w) {
			Log.e("LEOPARDUS", w.getMessage());
			return -1;
		}
	}

	/**
	 * Bind event to item with the itemIdentifier specified
	 * @param itemIdentifier
	 * @param evt
	 */
	public void addNewEvent(Integer itemIdentifier, MenuItemEvent evt){
		menuEvents.put(itemIdentifier, evt);
	}
	
	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		Fragment newContent = null;
		if (position == menuItemsList.size()) {
			switchFragment(newContent);
		} else if(menuEvents.containsKey(Integer.valueOf(position))){
			MenuItemEvent evt = menuEvents.get(position);
			evt.onListItemClick(lv, v, id);
		}
	}

	private void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;
		if (getActivity() instanceof BaseFragmentsActivity && fragment != null) {
			BaseFragmentsActivity fca = (BaseFragmentsActivity) getActivity();
			fca.setActualFragment(fragment);
		} else if (getActivity() instanceof BaseFragmentsActivity
				&& fragment == null) {
			BaseFragmentsActivity fca = (BaseFragmentsActivity) getActivity();
			fca.close();
		}
	}

}
