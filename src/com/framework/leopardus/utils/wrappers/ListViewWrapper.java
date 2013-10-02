package com.framework.leopardus.utils.wrappers;

import java.util.ArrayList;
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
			adapter = new ModelAdapter(ctx, row_lyt, rows, R_id_getClass, this);
			lv.setAdapter(adapter);
		} catch (Exception e) {
			throw new Exception(
					"This event must be called from UI thread, use Activity.runOnUiThread");
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

	public abstract void overrideEvents(View v, Model model);

}
