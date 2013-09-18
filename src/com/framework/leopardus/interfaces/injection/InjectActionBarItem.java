package com.framework.leopardus.interfaces.injection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectActionBarItem {
	int stringId();
	int iconId() default -1;
	int ItemId();
	int itemPosition() default Menu.NONE;
	boolean ItemAsIcon() default false;
	int showAs() default MenuItem.SHOW_AS_ACTION_WITH_TEXT;
}
