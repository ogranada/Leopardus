package com.framework.leopardus.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import android.app.Activity;
import android.view.View;

import com.framework.leopardus.interfaces.InjectMethod;
import com.framework.leopardus.interfaces.InjectView;

public class Injector {

	View view;

	public Injector(View v) {
		view = v;
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
						Method invocable = ew.getClass().getMethod(mname, Object.class, View.class,Method.class);
						invocable.invoke(ew, obj, v, method);
					} catch (Exception e) {
						System.err.println("Error: " + e.getMessage());
					}
				}
			}
		}
	}

}
