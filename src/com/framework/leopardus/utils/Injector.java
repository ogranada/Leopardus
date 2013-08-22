package com.framework.leopardus.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.framework.leopardus.activities.BaseFragmentsActivity;
import com.framework.leopardus.interfaces.MenuItemEvent;
import com.framework.leopardus.interfaces.injection.InjectMenuItem;
import com.framework.leopardus.interfaces.injection.InjectMethod;
import com.framework.leopardus.interfaces.injection.InjectView;

public class Injector {

	View view;
	Context context;

	public Injector(View v) {
		view = v;
		context = v.getContext();
	}

	public Injector(Context c) {
		view = null;
		context = c;		
	}

	public void injectViews(Object obj) {
		Field[] f = obj.getClass().getDeclaredFields();
		for (Field a : f) {
			InjectView i = a.getAnnotation(InjectView.class);
			if (i != null && i.id() != -1) {
				a.setAccessible(true);
				try {
					Object inst = view.findViewById(i.id());
					a.set(obj, inst);
				} catch (Exception e) {
					System.err.println("Error: " + e.getMessage());
					// Class<View> tipo = (Class<View>) a.getType();
					// Constructor<View> constructor =
					// tipo.getDeclaredConstructor(Context.class);
					// Object inst = constructor.newInstance(ctx);
				}
			}
		}
	}

	public void injectMethodsIntoViews(final Object obj) {
		Method[] methods = obj.getClass().getMethods();
		for (Method method : methods) {
			InjectMethod i = method.getAnnotation(InjectMethod.class);
			if (i != null && i.id() != -1) {
				EventWrapper ew;
				try {
					ew = EventWrapperFactory.getWraperInstance();
				} catch (Exception e) {
					ew = new EventWrapper();
				}
				View v = view.findViewById(i.id());
				if (v != null) {
					try {
						String mname = i.method().name();
						Method invocable = ew.getClass().getMethod(mname,
								Object.class, View.class, Method.class);
						invocable.invoke(ew, obj, v, method);
					} catch (Exception e) {
						System.err.println("Error: " + e.getMessage());
					}
				}
			}
		}
	}

	public void injectMenuItems(final BaseFragmentsActivity obj) {
		Method[] methods = obj.getClass().getMethods();
		for (Method method : methods) {
			InjectMenuItem i = method.getAnnotation(InjectMenuItem.class);
			final Method _method = method;
			if (i != null) {
				int menuId = obj.getMenu().addNewItem(obj, i.stringId(),
						i.iconId());
				obj.getMenu().addNewEvent(menuId, new MenuItemEvent() {

					@Override
					public void onListItemClick(ListView lv, View v, long id) {
						try {
							_method.invoke(obj, lv, v, id);
						} catch (Exception e) {
							Log.e("Leopardus", e.getMessage());
						}
					}
				});
			}
		}
	}

}
