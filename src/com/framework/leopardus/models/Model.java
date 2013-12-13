package com.framework.leopardus.models;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.framework.leopardus.utils.ImageLoaderHelper;
import com.framework.leopardus.utils.wrappers.ListViewWrapper;

@SuppressWarnings("rawtypes")
public class Model implements Serializable {

	public static Boolean DEBUG = false;

	/**
	 * 
	 */
	private static final long serialVersionUID = -8084362690902083447L;
	private Map<String, Object> data = new HashMap<String, Object>();

	public void updateData(Map<String, Object> new_data) {
		data.putAll(new_data);
	}

	public void updateData(Model new_data) {
		data.putAll(new_data.data);
	}

	public Model addItem(String key, Object value) {
		data.put(key, value);
		return this;
	}

	public String[] getKeys() {
		return data.keySet().toArray(new String[] {});
	}

	public Object getObject(String key) {
		return data.get(key);
	}

	public void storeValues(ListViewWrapper wrapper, View v, Class R_id_class) {
		Map<String, View> views = new HashMap<String, View>();
		Map<String, String> viewdata = new HashMap<String, String>();
		for (String key : data.keySet()) {
			try {
				Field campo = R_id_class.getField(key.toLowerCase());
				try {
					int id = Integer.valueOf(campo.get(null).toString());
					View subview = v.findViewById(id);
					if (subview != null) {
						storeValue(subview, key);
						viewdata.put("viewkey_" + key, subview.toString());
						views.put(subview.toString(), subview);
					}
				} catch (NumberFormatException e) {
					Log.d("Leopardus", "Model Error: " + e.getMessage());// e.printStackTrace();
				} catch (IllegalArgumentException e) {
					Log.d("Leopardus", "Model Error: " + e.getMessage());// e.printStackTrace();
				} catch (IllegalAccessException e) {
					Log.d("Leopardus", "Model Error: " + e.getMessage());// e.printStackTrace();
				}
			} catch (NoSuchFieldException e) {
				if (DEBUG) {
					Log.d("Leopardus", "Model Error: " + e.getMessage());// e.printStackTrace();
				}
			}
		}
		data.putAll(viewdata);
		wrapper.setViews(views);
	}

	private void storeValue(View view, String key) {
		Object value = data.get(key);
		String classname = view.getClass().getName();
		if (classname.endsWith("TextView")) {
			TextView tv = (TextView) view;
			tv.setText(String.valueOf(value));
		} else if (classname.endsWith("ImageView")) {
			ImageLoaderHelper ilt = ImageLoaderHelper.getInstance(view
					.getContext());
			ImageView tv = (ImageView) view;
			ilt.display(String.valueOf(value), tv);
		} else if (classname.endsWith("RadioButton")) {
			RadioButton rb = (RadioButton) view;
			rb.setChecked(((Boolean) value).booleanValue());
		} else if (classname.endsWith("CheckBox")) {
			CheckBox chb = (CheckBox) view;
			chb.setChecked(((Boolean) value).booleanValue());
		} else if (classname.endsWith("ProgressBar")) {
			ProgressBar pb = (ProgressBar) view;
			pb.setProgress(((Integer) value).intValue());
		} else {
			customStoreValue(view, key);
		}
	}

	/**
	 * This method will be overridden if you can use an unsupported widget.
	 * 
	 * @param view
	 * @param key
	 */
	public void customStoreValue(View view, String key) {
	}

	public Model clone() {
		Model m = new Model();
		for (String key : data.keySet()) {
			m.addItem(key, data.get(key));
		}
		return m;
	}
	
	public void remove(String key) {
		data.remove(key);
	}
	
	@Override
	public String toString() {
		return data.toString();
	}

}
