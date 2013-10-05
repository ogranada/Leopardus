package com.framework.leopardus.activities;

import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher.OnRefreshListener;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Window;
import com.framework.leopardus.R;
import com.framework.leopardus.utils.Injector;

public class BaseActivity extends SherlockActivity {

	private Integer _layout = R.layout.base_layout;
	private boolean enableProgressFeatures = false;
	private PullToRefreshAttacher pullToRefreshAttacher = null;

	private boolean fullscreen = false;

	public BaseActivity() {
		super();
	}

	public BaseActivity(int layout) {
		super();
		_layout = layout;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if (enableProgressFeatures) {
			requestWindowFeature(Window.FEATURE_PROGRESS);
			requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		}
		if (fullscreen) {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
		setContentView(_layout);
		if (enableProgressFeatures) {
			setSupportProgressBarIndeterminateVisibility(false);
			setSupportProgressBarVisibility(false);
		}
		Injector i = new Injector(this);
		i.injectViews(this);
		i.injectMethodsIntoViews(this);
	}

	public void setFullScreenActivity(boolean fsa) {
		fullscreen = fsa;
	}

	public void showProgressBar() {
		if (enableProgressFeatures) {
			setSupportProgressBarVisibility(true);
		}
	}

	public void showIndeterminateProgressBar() {
		if (enableProgressFeatures) {
			setSupportProgressBarIndeterminateVisibility(true);
		}
	}

	public void setEnableProgressFeatures(boolean enableProgressFeatures) {
		this.enableProgressFeatures = enableProgressFeatures;
	}

	public void hideProgressBar() {
		if (enableProgressFeatures) {
			setSupportProgressBarVisibility(false);
		}
	}

	public void hideIndeterminateProgressBar() {
		if (enableProgressFeatures) {
			setSupportProgressBarIndeterminateVisibility(false);
		}
	}

	public void close() {
		finish();
	}

	/**
	 * Init and return the PullToRefreshAttacher
	 * 
	 * @return PullToRefreshAttacher instance
	 */
	public PullToRefreshAttacher PullToRefreshInit() {
		if (pullToRefreshAttacher == null) {
			pullToRefreshAttacher = PullToRefreshAttacher.get(this);
		}
		return pullToRefreshAttacher;
	}

	/**
	 * Set the refresh listener for the provided view
	 * 
	 * @param view
	 * @param onRefreshListener
	 */
	public void estabilishRereshForView(View view,
			OnRefreshListener onRefreshListener) {
		if (pullToRefreshAttacher == null) {
			PullToRefreshInit();
		}
		pullToRefreshAttacher.addRefreshableView(view, onRefreshListener);
	}

}
