package com.framework.leopardus.adapters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.framework.leopardus.models.Model;
import com.framework.leopardus.utils.wrappers.ListViewWrapper;

@SuppressWarnings("rawtypes")
public class ModelAdapter extends BaseAdapter {

	private List<Model> rows;
	private LayoutInflater inflater;
	private int layout;
	private Class Ridclass;
	private ListViewWrapper wrapper;

	public ModelAdapter(Context ctx, int lyt, List<Model> model_rows,
			Class R_id_class, ListViewWrapper wrppr) {
		rows = model_rows;
		inflater = LayoutInflater.from(ctx);
		layout = lyt;
		Ridclass = R_id_class;
		wrapper = wrppr;
	}

	@Override
	public int getCount() {
		return rows.size();
	}

	@Override
	public Object getItem(int position) {
		return rows.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(layout, null);
		}
		rows.get(position).storeValues(wrapper, convertView, Ridclass);
		wrapper.overrideEvents(convertView,rows.get(position));
		return convertView;
	}
	
	public void clear() {
		rows = null;
		System.gc();
		rows = new ArrayList<Model>();
	}
	
}
