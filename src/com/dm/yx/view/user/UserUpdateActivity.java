package com.dm.yx.view.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.dm.yx.BaseActivity;
import com.dm.yx.MainPageActivity;
import com.dm.yx.R;
import com.dm.yx.model.User;
import com.dm.yx.tools.HealthConstant;
import com.dm.yx.tools.HealthUtil;
import com.dm.yx.tools.IDCard;
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

public class UserUpdateActivity extends BaseActivity
{
	@ViewInject(R.id.title)
	private TextView title;

	@ViewInject(R.id.real_name)
	private EditText realNameET;

	@ViewInject(R.id.tel)
	private EditText telephoneET;

	@ViewInject(R.id.idcard)
	private EditText idCardET;

	@ViewInject(R.id.check_btn)
	private RadioGroup group;

	@ViewInject(R.id.male)
	private RadioButton maleRadio;

	@ViewInject(R.id.female)
	private RadioButton femaleRadio;
	
	@ViewInject(R.id.psw)
	private EditText psw;
	
	@ViewInject(R.id.confirmPsw)
	private EditText confirmPsw;
	
	private User user;

	private User userT = new User();
	
	private String sex;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_people_info);
		ViewUtils.inject(this);
		addActivity(this);
		initView();
		initValue();
	}

	@Override
	protected void initView()
	{
		title.setText("资料更新");
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		this.user = HealthUtil.getUserInfo();
		realNameET.setText(user.getUserName());
		idCardET.setText(user.getUserNo());
		telephoneET.setText(user.getTelephone());
		psw.setText(user.getPassword());
		confirmPsw.setText(user.getPassword());
		if ("男".equals(user.getSex()))
		{
			maleRadio.setChecked(true);
		} else if ("女".equals(user.getSex()))
		{
			femaleRadio.setChecked(true);
		}
		
		realNameET.setOnFocusChangeListener(onFocusAutoClearHintListener);
		idCardET.setOnFocusChangeListener(onFocusAutoClearHintListener);
		psw.setOnFocusChangeListener(onFocusAutoClearHintListener);
		confirmPsw.setOnFocusChangeListener(onFocusAutoClearHintListener);
	}

	@OnClick(R.id.back)
	public void toHome(View v)
	{
		Intent intent = new Intent(UserUpdateActivity.this, MainPageActivity.class);
		startActivity(intent);
		exit();
	}
	
	@Override
	protected void initValue()
	{

	}

	@OnClick(R.id.edit_commit)
	public void submit(View v)
	{
		String phoneNum = telephoneET.getText() + "";
		String idNum = idCardET.getText() + "";
		String idCheckRst = IDCard.IDCardValidate(idNum);
		RadioButton radioButton = (RadioButton) findViewById(group.getCheckedRadioButtonId());
		String userNameT=realNameET.getText() + "";
		String pswStr = psw.getText().toString();
		String confirmPswStr = confirmPsw.getText().toString();
		
		if("".equals(userNameT))
		{
			HealthUtil.infoAlert(UserUpdateActivity.this, "用户名为空.");
			return;
		}else if(userNameT.length()>6)
		{
			HealthUtil.infoAlert(UserUpdateActivity.this, "用户名长度无效.");
			return;
		}
		if (!HealthUtil.isMobileNum(phoneNum))
		{
			HealthUtil.infoAlert(UserUpdateActivity.this, "手机号码为空或格式错误.");
			return;
		} else if (!"YES".equals(idCheckRst))
		{
			HealthUtil.infoAlert(UserUpdateActivity.this, idCheckRst);
			return;
		}

		if (radioButton == null)
		{
			HealthUtil.infoAlert(UserUpdateActivity.this, "用户性别为空.");
			return;
		} else
		{
			this.sex = radioButton.getText().toString();
		}
		
		if("".equals(pswStr))
		{
			HealthUtil.infoAlert(UserUpdateActivity.this, "密码为空.");
			return;
		}
		
		if(pswStr.length()<6 || pswStr.length()>12)
		{
			HealthUtil.infoAlert(UserUpdateActivity.this, "密码长度有误.");
			return;
		}
		
		if(!pswStr.equals(confirmPswStr))
		{
			HealthUtil.infoAlert(UserUpdateActivity.this, "密码不一致.");
			return;
		}
		
		this.userT.setUserId(user.getUserId());
		this.userT.setTelephone(telephoneET.getText() + "");
		this.userT.setUserName(userNameT);
		this.userT.setUserNo(idCardET.getText() + "");
		this.userT.setPassword(user.getPassword());
		this.userT.setSex(this.sex);
		this.userT.setPassword(pswStr);
		Gson gson = new Gson();
		String userStr = gson.toJson(userT);
		dialog.setMessage("更新中,请稍后...");
		dialog.show();
		RequestParams param = webInterface.updateUser(userStr);
		invokeWebServer(param, UPDATE_USER);
	}

	/**
	 * 链接web服务
	 * 
	 * @param param
	 *            2131493634
	 */
	private void invokeWebServer(RequestParams param, int responseCode)
	{
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

			HealthUtil.infoAlert(UserUpdateActivity.this, "信息加载失败，请检查网络后重试");
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
				
				HealthUtil.infoAlert(UserUpdateActivity.this, "用户资料更新成功.");
				finish();
			} else
			{
				HealthUtil.infoAlert(UserUpdateActivity.this, "用户资料更新失败请重试.");
			}

		}
	}
}