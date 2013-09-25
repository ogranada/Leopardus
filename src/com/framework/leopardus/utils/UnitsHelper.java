package com.framework.leopardus.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

public class UnitsHelper {

	public void getScreenSizePixels(Context ctx)
	{
		int widthHeightInPixels[] = new int[2];
	    Resources resources = ctx.getResources();
	    Configuration config = resources.getConfiguration();
	    DisplayMetrics dm = resources.getDisplayMetrics();
	    double screenWidthInPixels = (double)config.screenWidthDp * dm.density;
	    double screenHeightInPixels = screenWidthInPixels * dm.heightPixels / dm.widthPixels;
	    widthHeightInPixels[0] = (int)(screenWidthInPixels + .5);
	    widthHeightInPixels[1] = (int)(screenHeightInPixels + .5);
	}
	
	
	public int dpToPx(Context ctx, int dp) {
	    DisplayMetrics displayMetrics = ctx.getResources().getDisplayMetrics();
	    int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));       
	    return px;
	}
	
	public int pxToDp(Context ctx, int px) {
	    DisplayMetrics displayMetrics = ctx.getResources().getDisplayMetrics();
	    int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
	    return dp;
	}
	
}
