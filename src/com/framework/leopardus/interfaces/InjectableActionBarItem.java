package com.framework.leopardus.interfaces;

import com.framework.leopardus.interfaces.injection.InjectActionBarItem;

public interface InjectableActionBarItem {

	int addNewActionBarItem(InjectActionBarItem i);

	void addNewActionBarItem(int menuId, Runnable runnable);

}
