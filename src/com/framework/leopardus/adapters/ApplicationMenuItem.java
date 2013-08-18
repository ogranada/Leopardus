package com.framework.leopardus.adapters;

public class ApplicationMenuItem {
	public int textRes;
	public int iconRes;
	public ApplicationMenuItem(int strRes) {
		this.textRes = strRes; 
		this.iconRes = -1;
	}
	public ApplicationMenuItem(int strRes, int iconRes) {
		this.textRes = strRes; 
		this.iconRes = iconRes;
	}
}
