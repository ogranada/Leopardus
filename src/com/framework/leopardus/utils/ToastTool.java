package com.framework.leopardus.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastTool {

	public static void shortToast(Context ctx, String msj) {
		Toast.makeText(ctx, msj, Toast.LENGTH_SHORT).show();
	}

	public static void shortToast(Context ctx, int msj) {
		Toast.makeText(ctx, msj, Toast.LENGTH_SHORT).show();
	}

	public static void longToast(Context ctx, String msj) {
		Toast.makeText(ctx, msj, Toast.LENGTH_SHORT).show();
	}

	public static void longToast(Context ctx, int msj) {
		Toast.makeText(ctx, msj, Toast.LENGTH_SHORT).show();
	}
	
}
