package com.framework.leopardus.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.framework.leopardus.R;

public class ItemsAdapter extends ArrayAdapter<ApplicationMenuItem> {

	public ItemsAdapter(Context context) {
		super(context, 0);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.row, null);
		}
		ImageView icon = (ImageView) convertView.findViewById(R.id.row_icon);
		if(getItem(position).iconRes !=-1){
			icon.setImageResource(getItem(position).iconRes);
		} else {
			icon.setImageBitmap(null);
		}
		TextView title = (TextView) convertView.findViewById(R.id.row_title);
		title.setText(getItem(position).textRes);

		return convertView;
	}

}