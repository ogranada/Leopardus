package com.framework.leopardus.fragments;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.framework.leopardus.R;
import com.framework.leopardus.activities.BaseFragmentsActivity;
import com.framework.leopardus.adapters.ApplicationMenuItem;
import com.framework.leopardus.adapters.ItemsAdapter;

public class BaseMenuFragment extends ListFragment {

	List<Integer[]> menuItemsList = new ArrayList<Integer[]>();
	boolean autoExit = true;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ItemsAdapter adapter = new ItemsAdapter(getActivity());
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

	public void enableAutoExit() {
		autoExit = true;
	}

	public void disableAutoExit() {
		autoExit = true;
	}

	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		Fragment newContent = null;
//		switch (position) {
//		case 0:
//			break;
//		case 1:
//			break;
//		case 2:
//			break;
//
//		}
		switchFragment(newContent);
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
