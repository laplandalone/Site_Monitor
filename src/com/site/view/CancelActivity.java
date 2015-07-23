package com.site.view;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.site.BaseActivity;
import com.site.R;
import com.site.adapter.CancelAdapter;
import com.site.model.Cancel;
import com.site.tools.Constant;
import com.site.tools.DateUtils;
import com.site.tools.SiteUtil;

public class CancelActivity extends BaseActivity implements OnItemClickListener {
	@ViewInject(R.id.title)
	private TextView title;

	@ViewInject(R.id.site)
	private TextView site;
	@ViewInject(R.id.editUser)
	private TextView editUser;

	@ViewInject(R.id.card)
	private EditText card;

	private CancelAdapter adapter;
	private ListView list;

	@ViewInject(R.id.contentnull)
	private RelativeLayout layout;
	private Cancel cancel;
	private List<Cancel> cancels = new ArrayList<Cancel>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.line_list);
		this.list = (ListView) findViewById(R.id.linelist);
		ViewUtils.inject(this);
		addActivity(this);
		initValue();
		initView();
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		title.setText("撤销列表");
		site.setText("线路列表");
		editUser.setText("");
	}

	@OnClick(R.id.site)
	public void site(View v) {
		finish();
	}

	@Override
	protected void initValue() {
		cancels = SiteUtil.readCancels();
		adapter = new CancelAdapter(CancelActivity.this, cancels);
		this.list.setAdapter(adapter);
		this.list.setOnItemClickListener(this);
		if (this.cancels == null || this.cancels.size() == 0) {
			layout.setVisibility(View.VISIBLE);
			list.setVisibility(View.GONE);
		}
	}

	/**
	 * 链接web服务
	 * 
	 * @param param
	 */
	private void invokeWebServer(RequestParams param, int responseCode) {
		SiteUtil.LOG_D(getClass(), "connect to web server");
		MineRequestCallBack requestCallBack = new MineRequestCallBack(
				responseCode);
		if (httpHandler != null) {
			httpHandler.cancel();
		}
		httpHandler = mHttpUtils.send(HttpMethod.POST, Constant.URL_cancel,
				param, requestCallBack);
	}

	/**
	 * 获取后台返回的数据
	 */
	class MineRequestCallBack extends RequestCallBack<String> {

		private int responseCode;

		public MineRequestCallBack(int responseCode) {
			super();
			this.responseCode = responseCode;
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			SiteUtil.LOG_D(getClass(), "onFailure-->msg=" + msg);
			if (dialog.isShowing()) {
				dialog.cancel();
			}

			SiteUtil.infoAlert(CancelActivity.this, "信息加载失败，请检查网络后重试");
		}

		@Override
		public void onSuccess(ResponseInfo<String> arg0) {
			// TODO Auto-generated method stub
			SiteUtil.LOG_D(getClass(), "result=" + arg0.result);
			if (dialog.isShowing()) {
				dialog.cancel();
			}
			switch (responseCode) {
			case GET_LIST:
				returnMsg(arg0.result, GET_LIST);
				break;
			}
		}

	}

	/*
	 * 处理返回结果数据
	 */
	private void returnMsg(String json, int code) {
		try {
			JsonParser jsonParser = new JsonParser();
			JsonElement jsonElement = jsonParser.parse(json);
			JsonObject jsonObject = jsonElement.getAsJsonObject();
			String status = jsonObject.get("status").getAsString();
			if ("00".equals(status)) {
				showDialog();
				SiteUtil.writeCancels(cancel, "delete");
			} else {
				SiteUtil.infoAlert(CancelActivity.this, "处理失败,请重试...");
			}
		} catch (Exception e) {
			SiteUtil.infoAlert(CancelActivity.this, "处理失败,请重试...");
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		cancel = cancels.get(arg2);

		RequestParams param = webInterface.cancel(cancel.getCancelId());
		invokeWebServer(param, GET_LIST);
	}

	private void showDialog() {
		AlertDialog alertDialog = new AlertDialog.Builder(this)
				.setPositiveButton("确定", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						finish();
					}
				}).setTitle("提示").setMessage("处理成功").create();
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.show();
	}
}
