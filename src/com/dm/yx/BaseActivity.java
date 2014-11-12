package com.dm.yx;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.dm.yx.adapter.MyProgressDialog;
import com.dm.yx.application.RegApplication;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.HttpHandler;
import com.lurencun.android.webservice.IWebServiceInterface;
import com.lurencun.android.webservice.WebServiceInterfaceImpl;

public abstract class BaseActivity extends FragmentActivity {

	public HttpHandler httpHandler;

	public HttpUtils mHttpUtils = new HttpUtils();
	public static final int GET_LIST = 1001;
	public static final int GET_DATE_INFO = 1002;
	public static final int GET_LIST_MORE = 1003;
	public static final int ADD_REGISTER_ORDER = 1004;
	public static final int USER_LOGIN = 1005;
	public static final int ADD_QUESTION = 1006;
	public static final int ADD_USER = 1007;
	public static final int UPDATE_USER = 1008;
	public static final int GET_ORDER_NUM = 1009;
	public static final int AUTH_CODE = 10010;
	public static final int CHECK_AUTH_CODE = 10011;
	public static final int SET_PSW = 10012;
	public static final int PAY_STATE = 10003;
	public static final int RSA_SIGN = 10004;
	public static final int GET_ORDER_LIST = 10005;
	public static final int ADD_VISIT = 10006;
	
	protected ProgressDialog dialog;
	protected IWebServiceInterface webInterface = new WebServiceInterfaceImpl();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog = new ProgressDialog(this);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
	}

	public void addActivity(Activity activity) {
		RegApplication.getInstance().addActivity(activity);
	}

	public void exit() {
		RegApplication.getInstance().exit();
	}

	/**
	 * 初始化对象
	 */
	protected abstract void initView();

	/**
	 * 初始化赋值
	 */
	protected abstract void initValue();

	public void finish(View v) {
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			Log.i("NetWorkState", "Unavailabel");
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						Log.i("NetWorkState", "Availabel");
						return true;
					}
				}
			}
		}
		return false;
	}

	protected OnKeyListener onKeyListener = new OnKeyListener() {

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_ENTER
					&& event.getAction() == KeyEvent.ACTION_DOWN) {
				/* 隐藏软键盘 */
				InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				if (inputMethodManager.isActive()) {
					inputMethodManager.hideSoftInputFromWindow(
							v.getApplicationWindowToken(), 0);
				}
				return true;
			}
			return false;
		}
	};

	public static OnFocusChangeListener onFocusAutoClearHintListener = new OnFocusChangeListener() {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			EditText textView = (EditText) v;
			String hint;
			if (hasFocus) {
				hint = textView.getHint().toString();
				textView.setTag(hint);
				textView.setHint("");
			} else {
				hint = textView.getTag().toString();
				textView.setHint(hint);
			}
		}
	};
	
	protected ProgressDialog getProgressDialog(Context context) {
		try {
			if (dialog == null) {
				if (ProgressDialog.class.getField("THEME_HOLO_LIGHT") != null) {

					dialog = new MyProgressDialog(context,
							AlertDialog.THEME_HOLO_LIGHT);
					dialog.setCanceledOnTouchOutside(false);
					return dialog;
				}
			}
		} catch (NoSuchFieldException localNoSuchFieldException) {
			dialog = new MyProgressDialog(context);
			localNoSuchFieldException.printStackTrace();
		}
		dialog.setCanceledOnTouchOutside(false);
		return dialog;

	}

}
