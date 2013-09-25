package com.framework.leopardus.interfaces.injection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.framework.leopardus.enums.Ubications;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
/**
 * thris anottation need a method with the next params:
 * <ul>
 * <li>ListView lv</li>
 * <li>View v</li>
 * <li>long id</li>
 * </ul>
 * @author ogranada
 *
 */
public @interface InjectMenuItem {
	int stringId();
	int iconId() default -1;
	int position() default 0;
	Ubications ubication() default Ubications.LEFT;
}
