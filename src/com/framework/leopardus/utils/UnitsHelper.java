package com.framework.leopardus.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class UnitsHelper {

	public static int[] getScreenSizePixels(Context ctx) {
		int widthHeightInPixels[] = new int[2];
		try {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				Resources resources = ctx.getResources();
				Configuration config = resources.getConfiguration();
				DisplayMetrics dm = resources.getDisplayMetrics();
				double screenWidthInPixels = (double) config.screenWidthDp
						* dm.density;
				double screenHeightInPixels = screenWidthInPixels
						* dm.heightPixels / dm.widthPixels;
				widthHeightInPixels[0] = (int) (screenWidthInPixels + .5);
				widthHeightInPixels[1] = (int) (screenHeightInPixels + .5);
			} else {
				WindowManager wm = (WindowManager) ctx
						.getSystemService(Context.WINDOW_SERVICE);
				Display display = wm.getDefaultDisplay();
				widthHeightInPixels[0] = display.getWidth(); // deprecated
				widthHeightInPixels[1] = display.getHeight(); // deprecated
			}
		} catch (Exception e) {
		}
		return widthHeightInPixels;
	}

	public static int dpToPx(Context ctx, int dp) {
		DisplayMetrics displayMetrics = ctx.getResources().getDisplayMetrics();
		int px = Math.round(dp
				* (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
		return px;
	}

	public static int pxToDp(Context ctx, int px) {
		DisplayMetrics displayMetrics = ctx.getResources().getDisplayMetrics();
		int dp = Math.round(px
				/ (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
		return dp;
	}

}
