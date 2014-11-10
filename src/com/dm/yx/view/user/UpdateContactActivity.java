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
import com.dm.yx.model.UserContactT;
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

public class UpdateContactActivity extends BaseActivity
{
	@ViewInject(R.id.title)
	private TextView title;

	@ViewInject(R.id.real_name)
	private EditText realNameET;

	@ViewInject(R.id.tel)
	private EditText telephoneET;

	@ViewInject(R.id.userCard)
	private EditText userCard;
	
	@ViewInject(R.id.idcard)
	private EditText idCardET;

	@ViewInject(R.id.check_btn)
	private RadioGroup group;

	@ViewInject(R.id.male)
	private RadioButton maleRadio;

	@ViewInject(R.id.female)
	private RadioButton femaleRadio;
	
	
	private User user;

	private User userT = new User();
	
	private String sex;

	private UserContactT contactT;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_contact);
		ViewUtils.inject(this);
		addActivity(this);
		initView();
		initValue();
	}

	@Override
	protected void initView()
	{
		title.setText("修改联系人");
		this.user = HealthUtil.getUserInfo();
		this.contactT= (UserContactT) getIntent().getSerializableExtra("contactT");
		telephoneET.setText(contactT.getContactTelephone());
		idCardET.setText(contactT.getContactNo());
		realNameET.setText(contactT.getContactName());
		userCard.setText(contactT.getCardId());
		if ("男".equals(contactT.getContactSex()))
		{
			maleRadio.setChecked(true);
		} else if ("女".equals(contactT.getContactSex()))
		{
			femaleRadio.setChecked(true);
		}
	}

	@OnClick(R.id.back)
	public void toHome(View v)
	{
		Intent intent = new Intent(UpdateContactActivity.this, MainPageActivity.class);
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
		
		if("".equals(userNameT))
		{
			HealthUtil.infoAlert(UpdateContactActivity.this, "真实姓名为空.");
			return;
		}else if(userNameT.length()>6)
		{
			HealthUtil.infoAlert(UpdateContactActivity.this, "真实姓名长度无效.");
			return;
		}
		if (!HealthUtil.isMobileNum(phoneNum))
		{
			HealthUtil.infoAlert(UpdateContactActivity.this, "手机号码为空或格式错误.");
			return;
		} else if (!"YES".equals(idCheckRst))
		{
			HealthUtil.infoAlert(UpdateContactActivity.this, idCheckRst);
			return;
		}

		if (radioButton == null)
		{
			HealthUtil.infoAlert(UpdateContactActivity.this, "用户性别为空.");
			return;
		} else
		{
			this.sex = radioButton.getText().toString();
		}
		
		this.contactT.setUserId(user.getUserId());
		this.contactT.setContactTelephone(telephoneET.getText() + "");
		this.contactT.setContactName(userNameT);
		this.contactT.setContactNo(idCardET.getText() + "");
		this.contactT.setContactSex(this.sex);
		this.contactT.setCardId(userCard.getText()+"");
		Gson gson = new Gson();
		String userStr = gson.toJson(contactT);
		dialog.setMessage("处理中,请稍后...");
		dialog.show();
		RequestParams param = webInterface.updateUserContact(userStr);
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

			HealthUtil.infoAlert(UpdateContactActivity.this, "信息加载失败，请检查网络后重试");
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
				HealthUtil.infoAlert(UpdateContactActivity.this, "修改联系人成功.");
				finish();
			} else
			{
				HealthUtil.infoAlert(UpdateContactActivity.this, "修改联系人失败请重试.");
			}

		}
	}
}