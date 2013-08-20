package com.framework.leopardus.interfaces;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import android.view.View.OnClickListener;

import com.framework.leopardus.utils.InjectableMethods;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectMethod {

	int id();
	InjectableMethods method() default InjectableMethods.OnClickListener;
}
