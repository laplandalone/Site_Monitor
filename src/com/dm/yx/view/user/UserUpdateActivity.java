package com.dm.yx.view.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.dm.yx.tools.StringUtil;
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

	@ViewInject(R.id.telephone)
	private TextView telephone;
	
	@ViewInject(R.id.real_name)
	private EditText realNameET;

	@ViewInject(R.id.tel)
	private EditText telephoneET;

	@ViewInject(R.id.idcard)
	private EditText idCardET;
	
	@ViewInject(R.id.patient_id)
	private EditText patientId;

	@ViewInject(R.id.check_btn)
	private RadioGroup group;

	@ViewInject(R.id.male)
	private RadioButton maleRadio;

	@ViewInject(R.id.female)
	private RadioButton femaleRadio;
	

	
	private User user;

	private User userT = new User();
	
	private String sex;

	private String updateUserStr;
	
	private boolean noticeFlag=true;
	
	private String userNameChange="";
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
		String name=user.getUserName();
		String no=user.getUserNo();
		telephone.setText(user.getTelephone());
		if(name!=null && !"".equals(name))
		{
			realNameET.setText(name);
//			realNameET.setEnabled(false);
		}
		if(no!=null && !"".equals(no))
		{
			idCardET.setText(no);
			idCardET.setEnabled(false);
			noticeFlag=false;
		}
		telephoneET.setText(user.getTelephone());
		
		if ("男".equals(user.getSex()))
		{
			maleRadio.setChecked(true);
		} else if ("女".equals(user.getSex()))
		{
			femaleRadio.setChecked(true);
		}
		patientId.setText(user.getCardNo());
		realNameET.setOnFocusChangeListener(onFocusAutoClearHintListener);
		idCardET.setOnFocusChangeListener(onFocusAutoClearHintListener);
	
		/*
		realNameET.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				// TODO Auto-generated method stub
			
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after)
			{
				
				userNameChange=realNameET.getText().toString();
				// TODO Auto-generated method stub
			}
			
			@Override
			public void afterTextChanged(Editable s) 
			{
				// TODO Auto-generated method stub
				String text = realNameET.getText().toString();
				if(StringUtil.checkStringIsNum(s.charAt(s.length()-1)))
				{
					realNameET.setText(text.substring(0, text.length()-1));
				}
			}
		});*/
	}

	@OnClick(R.id.my_password)
	public void toPassword(View v)
	{
		Intent intent = new Intent(UserUpdateActivity.this, UserPasswordActivity.class);
		startActivity(intent);
	}
	
	@OnClick(R.id.back)
	public void toHome(View v)
	{
		Intent intent = new Intent(UserUpdateActivity.this, MainPageActivity.class);
		startActivity(intent);
		exit();
	}
	
	@OnClick(R.id.editPhone)
	public void editPhone(View v)
	{
		Intent intent = new Intent(UserUpdateActivity.this, RegisterActivity.class);
		intent.putExtra("flag", "editPhone");
		startActivity(intent);
		
	}
	
	@Override
	protected void initValue()
	{

	}

	@OnClick(R.id.edit_commit)
	public void submit(View v)
	{
		String phoneNum = telephoneET.getText().toString() + "";
		String idNum = idCardET.getText().toString() + "";
		String idCheckRst = IDCard.IDCardValidate(idNum);
		RadioButton radioButton = (RadioButton) findViewById(group.getCheckedRadioButtonId());
		String userNameT=realNameET.getText() + "";
		
		if(StringUtil.checkContainIsNum(userNameT))
		{
			HealthUtil.infoAlert(UserUpdateActivity.this, "用户名不允许包含数字.");
			return;
		}
		
		if(userNameT.length()>6)
		{
			HealthUtil.infoAlert(UserUpdateActivity.this, "用户名长度无效.");
			return;
		}
		
		if(!"".equals(idNum))
		{
			idCheckRst = IDCard.IDCardValidate(idNum);
			if(!"YES".equals(idCheckRst))
			{
				HealthUtil.infoAlert(UserUpdateActivity.this, idCheckRst);
				return;
			}
		}else
		{
			noticeFlag=false;
		}
		
		String patentIdT = patientId.getText().toString() + "";
		
		if (!HealthUtil.isMobileNum(phoneNum))
		{
			HealthUtil.infoAlert(UserUpdateActivity.this, "手机号码为空或格式错误.");
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
		
		
		
		this.userT.setUserId(user.getUserId());
		this.userT.setTelephone(telephoneET.getText() + "");
		this.userT.setUserName(userNameT);
		this.userT.setUserNo(idCardET.getText() + "");
		this.userT.setPassword(user.getPassword());
		this.userT.setSex(this.sex);
		this.userT.setCardNo(patentIdT);
//		this.userT.setPassword(pswStr);
		Gson gson = new Gson();
		updateUserStr = gson.toJson(userT);
		if(noticeFlag)
		{
			updateUser();
		}else
		{
			RequestParams param = webInterface.updateUser(updateUserStr);
    		invokeWebServer(param, UPDATE_USER);
		}
		
	}

	private void updateUser()
	{
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle("提示");  
		alertDialog.setMessage("身份证号信息填写完成之后不能修改，是否需要修改？");  
		alertDialog.setPositiveButton("取消",  
	                new DialogInterface.OnClickListener() {  
	                    public void onClick(DialogInterface dialog, int whichButton) 
	                    {  
	                    	
	                    }  
	                });  

		  
		alertDialog.setNeutralButton("确定",  
	                new DialogInterface.OnClickListener() {  
	                    public void onClick(DialogInterface dialog, int whichButton) 
	                    {  
	                		RequestParams param = webInterface.updateUser(updateUserStr);
	                		invokeWebServer(param, UPDATE_USER);
	                    }  
	                });  
		alertDialog.show();  
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
				HealthUtil.writeUserPassword(userT.getPassword());
				HealthUtil.writeChooseUsers(userT);
				HealthUtil.infoAlert(UserUpdateActivity.this, "用户资料更新成功.");
				finish();
			} else
			{
				HealthUtil.infoAlert(UserUpdateActivity.this, "用户资料更新失败请重试.");
			}

		}
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initView();
	}
}