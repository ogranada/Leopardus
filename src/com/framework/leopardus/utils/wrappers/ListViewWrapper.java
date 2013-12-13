package com.framework.leopardus.utils.wrappers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ListView;

import com.framework.leopardus.adapters.ModelAdapter;
import com.framework.leopardus.models.Model;

@SuppressWarnings("rawtypes")
public abstract class ListViewWrapper {

	private ModelAdapter adapter;
	private List<Model> rows;
	private Model lastModel;
	private ListView lv;
	private Context ctx;
	private int row_lyt;
	private Class R_id_getClass;

	public static interface ListViewWrapperAction {
		void run(List<Model> rows, ModelAdapter adapter, ListView lv);
	}

	private ListViewWrapperAction onPreUpdate;
	private ListViewWrapperAction onPostUpdate;
	private ListViewWrapperAction onNoItemsAdded;

	private Map<String, View> views = new HashMap<String, View>(0);

	public void setViews(Map<String, View> vws) {
		views = vws;
	}

	public View getView(String name) {
		return views.get(name);
	}

	public ListViewWrapper(Activity act, int list_lyt, int row_lyt,
			Class R_id_getClass) {
		ctx = act.getApplicationContext();
		this.row_lyt = row_lyt;
		this.R_id_getClass = R_id_getClass;
		initRows();
		lv = (ListView) act.findViewById(list_lyt);
		try {
			updateAdapter();
		} catch (Exception e) {
		}
	}

	public ListViewWrapper(View v, int list_lyt, int row_lyt,
			Class R_id_getClass) {
		ctx = v.getContext();
		this.row_lyt = row_lyt;
		this.R_id_getClass = R_id_getClass;
		initRows();
		lv = (ListView) v.findViewById(list_lyt);
		try {
			updateAdapter();
		} catch (Exception e) {
		}
	}

	public void updateAdapter() throws Exception {
		try {
			if (onPreUpdate != null) {
				onPreUpdate.run(rows, adapter, lv);
			}
			if (onNoItemsAdded != null && rows.size()==0) {
				List<Model> rows2 = new ArrayList<Model>(0);
				onNoItemsAdded.run(rows2, adapter, lv);
				adapter = new ModelAdapter(ctx, row_lyt, rows2, R_id_getClass,
						this);
			} else {
				adapter = new ModelAdapter(ctx, row_lyt, rows, R_id_getClass,
						this);
			}
			lv.setAdapter(adapter);
			if (onPostUpdate != null) {
				onPostUpdate.run(rows, adapter, lv);
			}
		} catch (Exception e) {
			throw new Exception(
					"Error: "
							+ e.getMessage()
							+ "\n Remember:\nThis event must be called from UI thread, use Activity.runOnUiThread");
		}
	}

	private void initRows() {
		rows = new ArrayList<Model>();
	}

	public void addItems(Map<String, Object> map) {
		Model model = new Model();
		model.updateData(map);
		rows.add(model);
		lastModel = model;
	}

	public ListViewWrapper addItem(String vw_name, Object value) {
		Model model = new Model();
		model.addItem(vw_name, value);
		rows.add(model);
		lastModel = model;
		return this;
	}

	public ListViewWrapper addItem(Model model) {
		rows.add(model);
		lastModel = model;
		return this;
	}

	public ListViewWrapper addToLast(String vw_name, Object value) {
		if (lastModel != null) {
			lastModel.addItem(vw_name, value);
			return this;
		} else {
			return addItem(vw_name, value);
		}
	}

	public void clear() {
		rows = null;
		System.gc();
		rows = new ArrayList<Model>();
	}

	public void setOnPostUpdate(ListViewWrapperAction onPostUpdate) {
		this.onPostUpdate = onPostUpdate;
	}

	public void setOnPreUpdate(ListViewWrapperAction onPreUpdate) {
		this.onPreUpdate = onPreUpdate;
	}

	public void setOnNoItemsAdded(ListViewWrapperAction onNoItemsAdded) {
		this.onNoItemsAdded = onNoItemsAdded;
	}

	public abstract void overrideEvents(View v, Model model);

}
