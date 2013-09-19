package com.framework.leopardus.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.framework.leopardus.exceptions.LeopardusException;
import com.framework.leopardus.interfaces.InjectableActionBarItem;
import com.framework.leopardus.interfaces.InjectableMenuItems;
import com.framework.leopardus.interfaces.MenuItemEvent;
import com.framework.leopardus.interfaces.injection.InjectActionBarItem;
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
						throw new RuntimeException(new LeopardusException(e));
					}
				}
			}
		}
	}
	
	@SuppressWarnings("all")
	public void injectMenuItems(final InjectableMenuItems obj) {
		Method[] methods = obj.getClass().getMethods();
		for (Method method : methods) {
			InjectMenuItem i = method.getAnnotation(InjectMenuItem.class);
			final Method _method = method;
			if (i != null) {
				int menuId = obj.addNewItem(i);
				obj.addNewEvent(menuId, i, new MenuItemEvent() {
					@Override
					public void onListItemClick(Object lv, View v, long id) {
						try {
							_method.invoke(obj, ((AdapterView) lv), v, id);
						} catch (Exception e) {
							Log.e("Leopardus", e.getMessage());
						}
					}
				});
			}
		}
	}

	public void injectActionBarItems(final InjectableActionBarItem obj) {
		Method[] methods = obj.getClass().getMethods();
		for (Method method : methods) {
			InjectActionBarItem i = method.getAnnotation(InjectActionBarItem.class);
			final Method _method = method;
			if (i != null) {
				int menuId = obj.addNewActionBarItem(i);
				obj.addNewActionBarItem(menuId, new Runnable() {
					
					@Override
					public void run() {
						try {
							_method.invoke(obj);
						} catch (Exception e) {
							Log.e("Leopardus", e.getMessage());
						}
					}
				});
			}
		}
	}

	// TODO: inject action modes
	// TODO: change menus lists' by Maps
	// TODO: inject WebView javascript methods
}
