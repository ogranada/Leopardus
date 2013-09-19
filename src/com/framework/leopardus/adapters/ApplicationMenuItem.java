package com.framework.leopardus.adapters;

public class ApplicationMenuItem {
	public int textRes;
	public int iconRes;
	private int menuKey;

	public ApplicationMenuItem(int strRes) {
		this.textRes = strRes;
		this.iconRes = -1;
	}

	public ApplicationMenuItem(int strRes, int iconRes) {
		this.textRes = strRes;
		this.iconRes = iconRes;
	}

	public void setMenuKey(int key) {
		this.menuKey = key;
	}
	
	public int getMenuKey() {
		return menuKey;
	}

}
