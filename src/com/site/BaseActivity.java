package com.site;

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

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.HttpHandler;
import com.lurencun.android.webservice.IWebServiceInterface;
import com.lurencun.android.webservice.WebServiceInterfaceImpl;
import com.site.adapter.MyProgressDialog;
import com.site.application.RegApplication;
import com.site.tools.SiteUtil;

/**
 *
 */
public abstract class BaseActivity extends FragmentActivity {

	public HttpHandler httpHandler;
	private LocationClient locationClient;
	public HttpUtils mHttpUtils = new HttpUtils();
	public static final int GET_LIST = 1001;
	public static final int GET_CITY = 1002;

	protected ProgressDialog dialog;
	protected IWebServiceInterface webInterface = new WebServiceInterfaceImpl();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog = new ProgressDialog(this);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		// startLocation();
	}

	public void addActivity(Activity activity) {
		RegApplication.getInstance().addActivity(activity);
	}

	public void exit() {
		RegApplication.getInstance().exit();
	}

	public void finish(Class name) 
	{
		RegApplication.getInstance().finish(name);
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

	/**
	 * 定位请求开始
	 * 
	 */
	public void startLocation() {
		LocationClientOption locationOption = new LocationClientOption();
		locationOption.setIsNeedAddress(true);// 获取文字地理信息
		locationOption.setScanSpan(7200000);
		locationClient = new LocationClient(getApplicationContext(),
				locationOption);
		locationClient.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation arg0) {
				if (arg0 == null) {
					return;
				}
				// 先把转换成百度的经纬度存到本地，防止获取地址失败，后面需要用到经纬度时但为空的情况
				SiteUtil.writeLongitude(arg0.getLongitude() + "");
				SiteUtil.writeLatitude(arg0.getLatitude() + "");
				String district = arg0.getDistrict();
				String city = arg0.getCity();
				String addressString = arg0.getAddrStr();
				// QueueUtil.writeLocationCityInfo(city);
				// QueueUtil.writeLocationDirectInfo(district);
				// QueueUtil.writeLocationDetailsInfo(addressString);
				// QueueUtil.locationSuccess = true;
				// sendLocationBroadCast();
			}
		});
		locationClient.start();

	};

}
