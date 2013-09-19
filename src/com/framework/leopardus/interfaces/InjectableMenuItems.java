package com.framework.leopardus.interfaces;

import com.framework.leopardus.interfaces.injection.InjectMenuItem;

public interface InjectableMenuItems {

	int addNewItem(InjectMenuItem i);

	void addNewEvent(int menuId, InjectMenuItem i, MenuItemEvent menuItemEvent);

}
