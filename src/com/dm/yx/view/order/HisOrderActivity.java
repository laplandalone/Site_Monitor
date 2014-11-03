package com.dm.yx.view.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.dm.yx.BaseActivity;
import com.dm.yx.MainPageActivity;
import com.dm.yx.R;
import com.dm.yx.model.User;
import com.dm.yx.model.UserContactT;
import com.dm.yx.tools.HealthConstant;
import com.dm.yx.tools.HealthUtil;
import com.dm.yx.tools.IDCard;
import com.dm.yx.view.user.ChooseContactListActivity;
import com.dm.yx.view.user.ContactListActivity;
import com.dm.yx.view.user.LoginActivity;
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

public class HisOrderActivity extends BaseActivity
{
	@ViewInject(R.id.title)
	private TextView title;

	@ViewInject(R.id.name)
	private EditText editName;
	
	@ViewInject(R.id.phone)
	private EditText editPhone;
	
	@ViewInject(R.id.idcard)
	private EditText editIdCard;
	
	@ViewInject(R.id.check_btn)
	private RadioGroup group;
	@ViewInject(R.id.settings)
	private Button settings;
	
	private String doctorName="0";
	private String registerTime;
	private String fee;
	private String registerId="0";
	private String userOrderNum="0";
	private String doctorId="0";
	private String teamId;
	private String teamName;
	private String userId;
	private String userName;
	private String userNo;
	private String userTelephone;
	private String sex;
	private User user;
	
	@ViewInject(R.id.male)
	private RadioButton maleRadio;

	@ViewInject(R.id.female)
	private RadioButton femaleRadio;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.expert_info_config);
		ViewUtils.inject(this);
		addActivity(this);
		initView();
		initValue();
	}

	@Override
	protected void initView()
	{
		title.setText("个人信息");
		editIdCard.setOnKeyListener(onKeyListener);  
		editName.setOnFocusChangeListener(onFocusAutoClearHintListener);
		editPhone.setOnFocusChangeListener(onFocusAutoClearHintListener);
		editIdCard.setOnFocusChangeListener(onFocusAutoClearHintListener);
//		settings.setVisibility(View.VISIBLE);
//		settings.setText("选择联系人");
	}

	@Override
	protected void initValue()
	{
		this.user=HealthUtil.getUserInfo();
		if(this.user==null)
		{
			Intent intent = new Intent(HisOrderActivity.this,LoginActivity.class);
			startActivityForResult(intent,0);
		}
		// TODO Auto-generated method stub
		this.doctorName = getIntent().getStringExtra("doctorName");
		this.registerTime = getIntent().getStringExtra("registerTime");
		this.fee = getIntent().getStringExtra("fee");
		this.registerId = getIntent().getStringExtra("registerId");
		this.userOrderNum = getIntent().getStringExtra("userOrderNum");
		this.doctorId = getIntent().getStringExtra("doctorId");
		this.teamId = getIntent().getStringExtra("teamId");
		this.teamName = getIntent().getStringExtra("teamName");
	}
	
	@OnClick(R.id.back)
	public void toHome(View v)
	{
		Intent intent = new Intent(HisOrderActivity.this,MainPageActivity.class);
		startActivity(intent);
		exit();
	}
	
	@OnClick(R.id.settings)
	public void setting(View v)
	{
		Intent intent = new Intent(HisOrderActivity.this,ChooseContactListActivity.class);
		startActivityForResult(intent, 0);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, intent);
		switch (resultCode)
		{
		case RESULT_OK:
			UserContactT contactT=(UserContactT) intent.getSerializableExtra("contactT"); 
			if (contactT != null)
			{
				this.editName.setText(contactT.getContactName());
				this.editPhone.setText(contactT.getContactTelephone());
				this.editIdCard.setText(contactT.getContactNo());
				this.sex = contactT.getContactSex();
				if ("男".equals(contactT.getContactSex()))
				{
					maleRadio.setChecked(true);
				} else if ("女".equals(contactT.getContactSex()))
				{
					femaleRadio.setChecked(true);
				}
			}
			break;

		default:
			break;
		}
	}
	
	@OnClick(R.id.submit)
	public void submit(View v)
	{
		this.userId=user.getUserId();
		this.userName=editName.getText().toString().trim();
		this.userNo=editIdCard.getText().toString().trim();
		this.userTelephone=editPhone.getText().toString().trim();
		RadioButton radioButton = (RadioButton)findViewById(group.getCheckedRadioButtonId());
		
		String idCheckRst = IDCard.IDCardValidate(userNo);
		if("".equals(userName))
		{
			HealthUtil.infoAlert(HisOrderActivity.this, "用户名为空.");
			return;
		}
		if(userName.length()>6)
		{
			HealthUtil.infoAlert(HisOrderActivity.this, "用户名长度无效.");
			return;
		}
		if (!HealthUtil.isMobileNum(userTelephone))
		{
			HealthUtil.infoAlert(HisOrderActivity.this, "手机号码为空或格式错误.");
			return;
		}
		if (!"YES".equals(idCheckRst))
		{
			HealthUtil.infoAlert(HisOrderActivity.this, idCheckRst);
			return;
		}
		if(radioButton==null)
		{
			HealthUtil.infoAlert(HisOrderActivity.this, "用户性别为空.");
			return;
		}else
		{
			this.sex=radioButton.getText().toString();
		}
		dialog.setMessage("正在预约,请稍后...");
		dialog.show();
		String hospitalId=HealthUtil.readHospitalId();
//		RequestParams param = webInterface.addUserRegisterOrder(hospitalId,userId, registerId, doctorId, doctorName, userOrderNum, fee, registerTime, userName, userNo, userTelephone,sex, teamId, teamName);
//		invokeWebServer(param,ADD_REGISTER_ORDER);
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
			
			HealthUtil.infoAlert(HisOrderActivity.this, "信息加载失败，请检查网络后重试");
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
			case GET_LIST:
				returnMsg(arg0.result, GET_LIST);
				break;
			case GET_LIST_MORE:
				returnMsg(arg0.result, GET_LIST_MORE);
				break;
			case ADD_REGISTER_ORDER:
				returnMsg(arg0.result, ADD_REGISTER_ORDER);
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
		case GET_LIST:
			
			
			break;
		case ADD_REGISTER_ORDER:
			String result = jsonObject.get("returnMsg").getAsString();
			if(!"".equals(result))
			{
				HealthUtil.infoAlert(HisOrderActivity.this, "预约成功...");
				Intent intent = new Intent(HisOrderActivity.this,ConfirmOrderActivity.class);
				intent.putExtra("hospitalId", HealthUtil.readHospitalId());
				intent.putExtra("orderId", result);
				intent.putExtra("doctorName", doctorName   ); 
				intent.putExtra("registerTime", registerTime ); 
				intent.putExtra("fee", fee          ); 
				intent.putExtra("userOrderNum", userOrderNum ); 
				intent.putExtra("teamName",  teamName    ); 
				intent.putExtra("userName", userName     ); 
				intent.putExtra("userNo",userNo        ); 
				intent.putExtra("userTelephone",userTelephone ); 
				intent.putExtra("sex", sex);
				startActivity(intent);
				finish();
			}else
			{
				HealthUtil.infoAlert(HisOrderActivity.this, "预约失败，请重试...");
			}
			break;
		}
		}catch(Exception e)
		{
			HealthUtil.infoAlert(HisOrderActivity.this, "预约失败，请重试...");
		}
		
	}

}
