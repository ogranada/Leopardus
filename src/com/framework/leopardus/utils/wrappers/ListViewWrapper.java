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

	public ListViewWrapper(Activity act, int list_lyt, int row_lyt,
			Class R_id_getClass) {
		Context ctx = act.getApplicationContext();
		initRows();
		adapter = new ModelAdapter(ctx, row_lyt, rows, R_id_getClass, this);
		ListView lv = (ListView) act.findViewById(list_lyt);
		lv.setAdapter(adapter);
	}

	private void initRows() {
		rows = new ArrayList<Model>();
	}

	public ListViewWrapper(View v, int list_lyt, int row_lyt,
			Class R_id_getClass) {
		Context ctx = v.getContext();
		initRows();
		adapter = new ModelAdapter(ctx, row_lyt, rows, R_id_getClass, this);
		ListView lv = (ListView) v.findViewById(list_lyt);
		lv.setAdapter(adapter);
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
