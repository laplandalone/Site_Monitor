package com.dm.yx.view.user;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.dm.yx.BaseActivity;
import com.dm.yx.R;
import com.dm.yx.model.User;
import com.dm.yx.tools.HealthConstant;
import com.dm.yx.tools.HealthUtil;
import com.dm.yx.view.user.UserUpdateActivity.MineRequestCallBack;
import com.google.gson.Gson;
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

public class UserPasswordActivity extends BaseActivity {

	@ViewInject(R.id.title)
	private TextView title;
	
	@ViewInject(R.id.old_psw)
	private EditText oldPsw;
	
	@ViewInject(R.id.psw)
	private EditText psw;
	
	@ViewInject(R.id.confirmPsw)
	private EditText confirmPsw;
	
	private User user;
	
	private User userT = new User();
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_password);
		ViewUtils.inject(this);
		addActivity(this);
		initView();
		initValue();
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		title.setText("我的密码");
		this.user = HealthUtil.getUserInfo();
		oldPsw.setOnFocusChangeListener(onFocusAutoClearHintListener);
		psw.setOnFocusChangeListener(onFocusAutoClearHintListener);
		confirmPsw.setOnFocusChangeListener(onFocusAutoClearHintListener);
	}

	@Override
	protected void initValue() {
		// TODO Auto-generated method stub

	}
	
	@OnClick(R.id.edit_commit)
	public void submit(View v)
	{
		String oldPswStr = oldPsw.getText().toString();
		String pswStr = psw.getText().toString();
		String confirmPswStr = confirmPsw.getText().toString();
		
		if("".equals(oldPswStr))
		{
			HealthUtil.infoAlert(UserPasswordActivity.this, "原密码为空.");
			return;
		}
		
		if(!oldPswStr.equals(user.getPassword()))
		{
			HealthUtil.infoAlert(UserPasswordActivity.this, "原密码错误.");
			return;
		}
		
		if("".equals(pswStr))
		{
			HealthUtil.infoAlert(UserPasswordActivity.this, "原密码为空.");
			return;
		}
		
		if("".equals(pswStr))
		{
			HealthUtil.infoAlert(UserPasswordActivity.this, "新密码为空.");
			return;
		}
		
		if(pswStr.length()<6 || pswStr.length()>12)
		{
			HealthUtil.infoAlert(UserPasswordActivity.this, "新密码长度有误.");
			return;
		}
		
		if(!pswStr.equals(confirmPswStr))
		{
			HealthUtil.infoAlert(UserPasswordActivity.this, "新密码不一致.");
			return;
		}
		this.userT.setUserId(user.getUserId());
		this.userT.setTelephone(user.getTelephone());
		this.userT.setUserName(user.getUserName());
		this.userT.setUserNo(user.getUserNo());
		this.userT.setPassword(pswStr);
		this.userT.setSex(user.getSex());
		Gson gson = new Gson();
		String updateUserStr = gson.toJson(userT);
		RequestParams param = webInterface.updateUser(updateUserStr);
		invokeWebServer(param, UPDATE_USER);
	}
	
	/**
	 * 链接web服务
	 * 
	 * @param param
	 *           
	 */
	private void invokeWebServer(RequestParams param, int responseCode)
	{
		dialog.setMessage("更新中,请稍后...");
		dialog.show();
		HealthUtil.LOG_D(getClass(), "connect to web server");
		MineRequestCallBack requestCallBack = new MineRequestCallBack(responseCode);
		if (httpHandler != null)
		{
			httpHandler.stop();
		}
		httpHandler = mHttpUtils.send(HttpMethod.POST, HealthConstant.URL, param, requestCallBack);
	}

	/**
	 * 获取后台返回的数据
	 */
	class MineRequestCallBack extends RequestCallBack<String>
	{

		private int responseCode;

		public MineRequestCallBack(int responseCode)
		{
			super();
			this.responseCode = responseCode;
		}

		@Override
		public void onFailure(HttpException error, String msg)
		{
			HealthUtil.LOG_D(getClass(), "onFailure-->msg=" + msg);
			if (dialog.isShowing())
			{
				dialog.cancel();
			}

			HealthUtil.infoAlert(UserPasswordActivity.this, "信息加载失败，请检查网络后重试");
		}

		@Override
		public void onSuccess(ResponseInfo<String> arg0)
		{
			// TODO Auto-generated method stub
			HealthUtil.LOG_D(getClass(), "result=" + arg0.result);
			if (dialog.isShowing())
			{
				dialog.cancel();
			}
			switch (responseCode)
			{
			case UPDATE_USER:
				returnMsg(arg0.result, ADD_USER);
				break;
			}
		}

		/*
		 * 处理返回结果数据
		 */
		private void returnMsg(String json, int code)
		{
			JsonParser jsonParser = new JsonParser();
			JsonElement jsonElement = jsonParser.parse(json);
			JsonObject jsonObject = jsonElement.getAsJsonObject();
			String executeType = jsonObject.get("executeType").getAsString();
			if ("success".equals(executeType))
			{
				
				Gson gson = new Gson();
				String userStr = gson.toJson(userT);
				
				HealthUtil.writeUserInfo(userStr);
				HealthUtil.writeUserId(userT.getUserId());
				HealthUtil.writeUserPassword(userT.getPassword());
				HealthUtil.writeChooseUsers(userT);
				HealthUtil.infoAlert(UserPasswordActivity.this, "密码更新成功.");
				finish();
			} else
			{
				HealthUtil.infoAlert(UserPasswordActivity.this, "密码更新失败请重试.");
			}

		}
	}
	
	
}
