package com.framework.leopardus.utils;

import java.util.Map;

import android.app.Activity;
import android.content.Context;

import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.framework.leopardus.adapters.ApplicationMenuItem;
import com.framework.leopardus.interfaces.ActivityMethodInterface;

public class GenericActionMode implements ActionMode.Callback {

	Map<ApplicationMenuItem, ActivityMethodInterface> items;
	Context context;

	public GenericActionMode(Context ctx,
			Map<ApplicationMenuItem, ActivityMethodInterface> newItems) {
		;
		items = newItems;
		context = ctx;
	}

	@Override
	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		for (ApplicationMenuItem item : items.keySet()) {
			MenuItem mi = menu.add(context.getResources().getString(
					item.textRes));
			if (item.iconRes != -1) {
				mi.setIcon(context.getResources().getDrawable(item.iconRes));
			}
			mi.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		}
		return true;
	}

	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		return false;
	}

	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		if (!items.containsValue(item)) {
			return false;
		} else {
			items.get(item).Method((Activity)context, item);
			return true;
		}
	}

	@Override
	public void onDestroyActionMode(ActionMode mode) {
	}

}
