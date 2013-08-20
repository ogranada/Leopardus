package com.framework.leopardus.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.view.View;

import com.framework.leopardus.interfaces.InjectView;

public class Injector {

	Context ctx;
	
	public Injector(Context c) {
		ctx = c;
	}
	
	public void injectViews(Object obj){
		Field[] f = obj.getClass().getDeclaredFields();
        for (Field a : f) {
            InjectView i = a.getAnnotation(InjectView.class);
            if (i != null) {
            	try {
            		Class<View> tipo = (Class<View>) a.getType();
            		Constructor<View> constructor = tipo.getDeclaredConstructor(Context.class);
            		Object inst = constructor.newInstance(ctx);
            		a.setAccessible(true);
            		a.set(obj, inst);
				} catch (Exception e) {
					System.err.println("Error: " +e.getMessage());
				}
            }
        }
	}
	
}
