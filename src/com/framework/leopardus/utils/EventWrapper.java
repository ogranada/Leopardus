package com.framework.leopardus.utils;

import java.lang.reflect.Method;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;

public class EventWrapper {

	public void OnClickListener(final Object baseView, final View objSender,
			final Method method) {
		objSender.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					method.invoke(baseView, v);
				} catch (Exception e) {
					Log.e("Leopardus", method.getName() + ":" + e.getMessage());
				}
			}
		});
	}

	public void OnLongClickListener(final Object baseView,
			final View objSender, final Method method) {
		objSender.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				try {
					return (Boolean) method.invoke(baseView, v);
				} catch (Exception e) {
					Log.e("Leopardus", method.getName() + ":" + e.getMessage());
					return false;
				}
			}
		});
	}

	public void OnTouchListener(final Object baseView, final View objSender,
			final Method method) {
		objSender.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				try {
					return (Boolean) method.invoke(baseView, v, event);
				} catch (Exception e) {
					Log.e("Leopardus", method.getName() + ":" + e.getMessage());
					return false;
				}
			}
		});
	}

}
