package com.framework.leopardus.utils;

import android.app.Activity;
import android.app.ProgressDialog;

public class ProgressDialogHelper {

	private static ProgressDialog progressDialog;
	private Activity act;

	public ProgressDialog getInstance(Activity act) {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(act);
			this.act = act;
		}
		return progressDialog;
	}

	public void setProgressDialogInfo(String title, String message) {
		this.setProgressDialogInfo(title, message, ProgressDialog.STYLE_SPINNER);
	}

	public void setProgressDialogInfo(String title, String message, int style) {
		progressDialog.setTitle(title);
		progressDialog.setMessage(message);
		progressDialog.setProgressStyle(style);
	}

	public void show() {
		try {
			if (!act.isFinishing()) {
				progressDialog.show();
			}
		} catch (Exception e) {

		}
	}

	public void hide() {
		try {
			progressDialog.dismiss();
		} catch (Exception e) {

		}
	}

}
