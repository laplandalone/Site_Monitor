package com.dm.yx.view.user;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.dm.yx.BaseActivity;
import com.dm.yx.MainPageActivity;
import com.dm.yx.R;
import com.dm.yx.model.User;
import com.dm.yx.tools.HealthConstant;
import com.dm.yx.tools.HealthUtil;
import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lurencun.android.utils.ParamUtil;

public class RegisterNextActivity extends BaseActivity
{
	@ViewInject(R.id.title)
	private TextView title;

	@ViewInject(R.id.new_password)
	private EditText passwordET;
	
	@ViewInject(R.id.confirm_password)
	private EditText confirmPasswordET;
	
	private String telephone;
	
	private User user ;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_sign_up_next);
		ViewUtils.inject(this);
		addActivity(this);
		 initValue();
		 initView();
	}

	@OnClick(R.id.registered)
	public void toRegister(View v)
	{
		String password=passwordET.getText()+"";
		String confirmPd=confirmPasswordET.getText()+"";
		if("".equals(password) || "".equals(confirmPd))
		{
			HealthUtil.infoAlert(RegisterNextActivity.this, "密码为空，请输入");
			
		}else if( password.length()<6 || password.length()>12)
		{
			HealthUtil.infoAlert(RegisterNextActivity.this, "密码长度有误，重新输入");
			
		}else if(!password.equals(confirmPd))
		{
			HealthUtil.infoAlert(RegisterNextActivity.this, "密码不一致，请重新输入");
			
		}else if(password.equals(confirmPd))
		{
			dialog.show();
			user = new User();
			user.setTelephone(this.telephone);
			user.setPassword(password);
			Gson gson = new Gson();
			String userStr=gson.toJson(user);
			RequestParams param = webInterface.addUser(userStr);
			invokeWebServer(param, ADD_USER);
		}
	}
	
	
	@OnClick(R.id.back)
	public void toHome(View v)
	{
		Intent intent = new Intent(RegisterNextActivity.this,MainPageActivity.class);
		startActivity(intent);
		exit();
	}
	
	@Override
	protected void initView()
	{
		// TODO Auto-generated method stub
		title.setText("输入密码");
		passwordET.setOnFocusChangeListener(onFocusAutoClearHintListener);
		confirmPasswordET.setOnFocusChangeListener(onFocusAutoClearHintListener);
	}

	@Override
	protected void initValue()
	{
		// TODO Auto-generated method stub
		this.telephone=getIntent().getStringExtra("telephone");
	}

/**
 * 链接web服务
 * 
 * @param param
 */
private void invokeWebServer(RequestParams param, int responseCode)
{
	HealthUtil.LOG_D(getClass(), "connect to web server");
	MineRequestCallBack requestCallBack = new MineRequestCallBack(responseCode);
	if (httpHandler != null)
	{
		httpHandler.cancel();
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
		
		HealthUtil.infoAlert(RegisterNextActivity.this, "信息加载失败，请检查网络后重试");
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
		case ADD_USER:
			returnMsg(arg0.result, ADD_USER);
			break;
		case USER_LOGIN:
			returnMsg(arg0.result, USER_LOGIN);
			break;
		}
	}

	/*
	 * 处理返回结果数据
	 */
	private void returnMsg(String json, int code)
	{
		
		JSONObject jsonObject;
		try
		{
			jsonObject = new JSONObject(json);
			String executeType = jsonObject.get("executeType").toString();
			if (!"success".equals(executeType))
			{
				HealthUtil.infoAlert(RegisterNextActivity.this, "注册失败，请重试");
			
			}else
			{
				String returnMsg = jsonObject.get("returnMsg").toString();
				if(returnMsg.equals("1"))
				{
					HealthUtil.infoAlert(RegisterNextActivity.this, "该手机号已注册");
				}else
				{
					
					Gson gson= new Gson();
					user = HealthUtil.json2Object(returnMsg.toString(), User.class);
					if ( user != null)
					{
						HealthUtil.writeUserInfo(returnMsg.toString());
						User user = HealthUtil.getUserInfo();
						HealthUtil.writeUserId(user.getUserId());
						HealthUtil.writeUserPhone(user.getTelephone());
						ParamUtil.setUserId(user.getUserId());
						Intent intent = new Intent(RegisterNextActivity.this,MainPageActivity.class);
						startActivity(intent);
						HealthUtil.infoAlert(RegisterNextActivity.this, "注册成功");
						exit();
					}
					
				}
			}
		} catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

}
