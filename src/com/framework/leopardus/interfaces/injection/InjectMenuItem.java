package com.framework.leopardus.interfaces.injection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.framework.leopardus.enums.Ubications;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectMenuItem {
	int stringId();
	int iconId() default -1;
	Ubications ubication() default Ubications.LEFT;
}
