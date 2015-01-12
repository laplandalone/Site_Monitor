package com.dm.yx.view.user;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dm.yx.BaseActivity;
import com.dm.yx.MainPageActivity;
import com.dm.yx.R;
import com.dm.yx.model.User;
import com.dm.yx.tools.HealthConstant;
import com.dm.yx.tools.HealthUtil;
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

@SuppressLint("NewApi")
public class RegisterActivity extends BaseActivity
{
	@ViewInject(R.id.title)
	private TextView title;

	@ViewInject(R.id.user_name)
	private EditText userNameET;

	@ViewInject(R.id.confirm_password)
	private EditText confirmNum;
	
	@ViewInject(R.id.get_config_num)
	private Button pswBtn;
	
	private Timer timer=null;
    private TimerTask task=null;
    private long  Count=0;
    private long  TimerNuit=1000;
    private Handler handler=null;
    private Message msg=null;
    private int SETTING_100MILLISECOND_ID=0;
    private int settingTimerNuit=SETTING_100MILLISECOND_ID;
	private String flag="";    
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_sign_up);
		ViewUtils.inject(this);
		addActivity(this);
		initView();
		initValue();
		handler=new Handler(){

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					Count++;
					int totalSec=0;
					int yunshu=0;
					if(settingTimerNuit==SETTING_100MILLISECOND_ID)
					{
						totalSec=(int)(Count/60);
						yunshu=(int)(Count%60);
					}
					int mai=totalSec/60;
					int sec=totalSec%60;
					try {
						if(settingTimerNuit==SETTING_100MILLISECOND_ID)
						{
							if(yunshu==59)
							{
								cancelTimer();
								return;
							}
							pswBtn.setTextSize(16);
							pswBtn.setBackgroundResource(R.drawable.user_registet_time);
//							pswBtn.setTextColor(color.TextColorWhite);
							pswBtn.setText(60-yunshu+"秒后重发");	
//							pswBtn.setText(String.format("%1$02d:%2$02d:%3$d",mai,sec,yunshu));						  	
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;			
				}
				super.handleMessage(msg);
			}
			
		};
	}

	public void  cancelTimer()
	{
		try
		{
			if(null!=task)
			{
				task.cancel();
				task=null;
				timer.cancel();
				timer.purge();
				timer=null;
			}	
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		Count=0;
		pswBtn.setText("");
		pswBtn.setBackgroundResource(R.drawable.btn_confignum);
	}
	@OnClick(R.id.get_config_num)
	public void getConfiNum(View v)
	{
		
		if(null!=task)
		{
			 return;
		}	
		String telephone = userNameET.getText() + "";
	
		if (!HealthUtil.isMobileNum(telephone))
		{
			HealthUtil.infoAlert(RegisterActivity.this, "手机号码为空或格式错误!");
			return;
		}else
		{
			dialog.setMessage("处理中,请稍后...");
			dialog.show();
			if("editPhone".equals(flag))
			{
				RequestParams param = webInterface.getAuthCode(userNameET.getText()+"","edit_phone");
				invokeWebServer(param, AUTH_CODE);
			}else
			{
				RequestParams param = webInterface.getAuthCode(userNameET.getText()+"","NEW_USER");
				invokeWebServer(param, AUTH_CODE);
			}
//			pswBtn.setTextColor(color.white);
//			pswBtn.setBackgroundResource(R.drawable.time_default);
		     if(null==timer){
		    	 if(null==task){
		    		 task=new TimerTask() {	
						@Override
						public void run() {
							if(null==msg){
								msg=new Message();
							}else{
								msg=Message.obtain();
							}
							msg.what=1;
							handler.sendMessage(msg);						
							}
					};
		    	 }
		    	 
		    	 timer=new Timer(true);
		    	 timer.schedule(task,TimerNuit,TimerNuit);
		     }

		}
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
			cancelTimer();
			HealthUtil.infoAlert(RegisterActivity.this, "信息加载失败，请检查网络后重试");
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
			case AUTH_CODE:
				returnMsg(arg0.result, AUTH_CODE);
				break;
			case CHECK_AUTH_CODE:
				returnMsg(arg0.result, CHECK_AUTH_CODE);
				break;
			case EDIT_PHONE:
				returnMsg(arg0.result, EDIT_PHONE);
				break;
			}
		}

	}

	/*
	 * 处理返回结果数据
	 */
	private void returnMsg(String json, int responseCode)
	{
		try
		{
			JsonParser jsonParser = new JsonParser();
			JsonElement jsonElement = jsonParser.parse(json);
			JsonObject jsonObject = jsonElement.getAsJsonObject();

			switch (responseCode)
			{
			    case AUTH_CODE:
			    
				String returnObj = jsonObject.get("returnMsg").getAsString();
				
				JsonElement jsonElementT = jsonParser.parse(returnObj);
				JsonObject jsonObjectT = jsonElementT.getAsJsonObject();
				String status=jsonObjectT.get("status").getAsString();
				if("000".equals(status))
				{
					cancelTimer();
					HealthUtil.infoAlert(RegisterActivity.this, "该号码已注册，请重试...");
					return;
				}
				if(!"100".equals(status))
				{
					cancelTimer();
					HealthUtil.infoAlert(RegisterActivity.this, "获取验证码失败，请重试...");
					return;
				}else
				{
					showSuccessDialog();
				}
				break;

			    case CHECK_AUTH_CODE:
			    String returnMsg = jsonObject.get("returnMsg").getAsString();
			    if("true".equals(returnMsg))
			    {   
			    	if(!"editPhone".equals(flag))
			    	{
			    	    String telephone = userNameET.getText() + "";	
						Intent intent = new Intent(RegisterActivity.this, RegisterNextActivity.class);
						intent.putExtra("telephone", telephone);
						startActivity(intent);
						finish();
			    	}else 
			    	{
			    		String telephone = userNameET.getText() + "";
			    		User user = HealthUtil.getUserInfo();
			    		RequestParams param = webInterface.updateUserPhone(user.getUserId(), telephone);
			    		invokeWebServer(param, EDIT_PHONE);
			    	}
			    }else
			    {
			    	HealthUtil.infoAlert(RegisterActivity.this, "手机号或验证码输入有误，请重试...");
			    }
			    break;
			    case EDIT_PHONE:
			    	 String returnMsgT = jsonObject.get("returnMsg").getAsString();
			    	if("true".equals(returnMsgT))
				    {
			    		String telephone = userNameET.getText().toString();
			    		HealthUtil.writeUserPhone(telephone);
			    		User user = HealthUtil.getUserInfo();
			    		user.setTelephone(telephone);
			    		Gson gson = new Gson();
			    		String info=gson.toJson(user);
			    		HealthUtil.writeUserInfo(info);
			    		HealthUtil.infoAlert(RegisterActivity.this, "修改手机号码成功...");
			    		finish();
				    }else
				    {
				    	HealthUtil.infoAlert(RegisterActivity.this, "手机号或验证码输入有误，请重试...");
				    }
			    break;
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			switch (responseCode)
			{
			    case AUTH_CODE:
			    	cancelTimer();
					HealthUtil.infoAlert(RegisterActivity.this, "获取验证码失败,请重试...");
					break;
			    case CHECK_AUTH_CODE:
			    	HealthUtil.infoAlert(RegisterActivity.this, "手机号或验证码输入有误,请重试...");
			    	break;
			}
			 
			
		}

	}

	@OnClick(R.id.sign_up)
	public void toNext(View v)
	{
		String telephone = userNameET.getText().toString().trim();
		String authCode=confirmNum.getText().toString().trim();
		
		if (!HealthUtil.isMobileNum(telephone))
		{
			HealthUtil.infoAlert(RegisterActivity.this, "手机号码为空或格式错误!");
			return;
		}
		if("".equals(authCode))
		{
			HealthUtil.infoAlert(RegisterActivity.this, "验证码为空!");
			return;
		}
		
		RequestParams param = webInterface.checkAuthCode(telephone, authCode);
		invokeWebServer(param, CHECK_AUTH_CODE);
	}

	@OnClick(R.id.back)
	public void toHome(View v)
	{
		Intent intent = new Intent(RegisterActivity.this, MainPageActivity.class);
		startActivity(intent);
		exit();
	}

	private void showSuccessDialog()
	{
		AlertDialog alertDialog = new AlertDialog.Builder(this)
				.setPositiveButton("确定", new OnClickListener()
				{

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						// TODO Auto-generated method stub

					}
				}).setTitle("提示").setMessage("验证码已发送成功请注意查收，60秒后可重新获取").create();
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.show();
	}

	@Override
	protected void initView()
	{
		// TODO Auto-generated method stub
		
		userNameET.setOnFocusChangeListener(onFocusAutoClearHintListener);
		confirmNum.setOnFocusChangeListener(onFocusAutoClearHintListener);
	}

	@Override
	protected void initValue()
	{
		// TODO Auto-generated method stub
		flag=getIntent().getStringExtra("flag");
		if("editPhone".equals(flag))
		{
			title.setText("修改手机号码");
		}else
		{
			title.setText("用户注册");
		}
	}

	
	
	
}
