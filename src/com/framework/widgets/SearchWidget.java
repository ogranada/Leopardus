package com.framework.widgets;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;
import com.framework.leopardus.R;

/**
 * A widget that provides a user interface for the user to enter a search query.
 * 
 * @see android.view.MenuItem#SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW
 * @attr ref android.R.styleable#SearchWidget_ubication
 */
public class SearchWidget extends LinearLayout {

	Button searchLeft;
	Button searchRight;
	private EditText search;
	LinearLayout backsearch;
	SearchView.OnQueryTextListener queryTextListener;

	public int boton = 1;

	public SearchWidget(Context context) {
		super(context);
		inflateComponent(context);
	}

	public SearchWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		inflateComponent(context);
		processAttributes(context, attrs);
	}

	private void processAttributes(Context context, AttributeSet attrs) {
		try {
			boton = attrs.getAttributeIntValue("SearchView", "ubication", 1);
		} catch (Exception e) {
		}
		setButtonUbication();
	}

	private void inflateComponent(Context context) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.searchwidget_layout, this);
		searchLeft = (Button) findViewById(R.id.btn_search_left);
		searchRight = (Button) findViewById(R.id.btn_search_right);
		backsearch = (LinearLayout) findViewById(R.id.lay_fondo_busqueda);
		setSearch((EditText) findViewById(R.id.txt_search));
		clearSearchFocus();
		getSearch().addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				queryTextListener.onQueryTextChange(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		OnClickListener searchbtnocl = new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				queryTextListener
						.onQueryTextSubmit(getSearch().getText().toString());
			}
		};
		searchLeft.setOnClickListener(searchbtnocl);
		searchRight.setOnClickListener(searchbtnocl);
	}

	public void clearSearchFocus() {
		if (getSearch() != null) {
			getSearch().clearFocus();
			getSearch().setSelected(false);
		}
	}

	public void setButtonUbication() {
		if (boton == 1) {
			android.view.ViewGroup.LayoutParams lp = searchLeft
					.getLayoutParams();
			lp.width = 1;
			searchLeft.setVisibility(View.INVISIBLE);
		} else {
			android.view.ViewGroup.LayoutParams lp = searchRight
					.getLayoutParams();
			lp.width = 1;
			searchRight.setVisibility(View.INVISIBLE);
		}
	}

	public void setTextViewBackground(int resid) {
		backsearch.setBackgroundResource(resid);
	}

	public void setButtonBackground(Drawable background) {
		try {
			searchLeft.setBackground(background);
		} catch (Exception e) {
		}
		try {
			searchRight.setBackground(background);
		} catch (Exception e) {
		}
	}

	public void setButtonBackground(int resid) {
		try {
			searchLeft.setBackgroundResource(resid);
		} catch (Exception e) {
		}
		try {
			searchRight.setBackgroundResource(resid);
		} catch (Exception e) {
		}
	}

	public void setOnQueryTextListener(OnQueryTextListener qtl) {
		this.queryTextListener = qtl;
	}

	public void setTextColor(int color) {
		try {
			getSearch().setTextColor(color);
		} catch (Exception e) {
		}
	}

	public EditText getSearch() {
		return search;
	}

	public  void setSearch(EditText search) {
		this.search = search;
	}
	
}
