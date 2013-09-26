package com.framework.leopardus.models;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.framework.leopardus.utils.ImageLoaderHelper;

@SuppressWarnings("rawtypes")
public class Model {

	private Map<String, Object> data = new HashMap<String, Object>();

	public void updateData(Map<String, Object> new_data) {
		data.putAll(new_data);
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

	public void storeValues(View v, Class R_id_class) {
		for (String key : data.keySet()) {
			try {
				Field campo = R_id_class.getField(key.toLowerCase());
				try {
					int id = Integer.valueOf(campo.get(null).toString());
					View subview = v.findViewById(id);
					storeValue(subview, key);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
		}
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

	public void customStoreValue(View view, String key) {
	}

}
