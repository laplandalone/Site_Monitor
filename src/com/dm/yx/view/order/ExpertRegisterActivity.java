package com.dm.yx.view.order;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
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

public class ExpertRegisterActivity extends BaseActivity
{
	@ViewInject(R.id.title)
	private TextView title;

	@ViewInject(R.id.submit)
	private Button submitBtn;

	@ViewInject(R.id.ready)
	private ImageButton ready;

	@ViewInject(R.id.textView_name)
	private TextView textViewName;

	@ViewInject(R.id.textView_time)
	private TextView textViewTime;

	@ViewInject(R.id.textView_number)
	private TextView textViewNumber;

	@ViewInject(R.id.textView_fee)
	private TextView textViewFee;

	@ViewInject(R.id.edit_name)
	private EditText editName;

	@ViewInject(R.id.edit_phone)
	private EditText editPhone;

	@ViewInject(R.id.edit_idCard)
	private EditText editIdCard;
	@ViewInject(R.id.check_btn)
	private RadioGroup group;
	@ViewInject(R.id.male)
	private RadioButton maleRadio;

	@ViewInject(R.id.female)
	private RadioButton femaleRadio;

	@ViewInject(R.id.check_btn)
	private RadioButton radioGroup;

	@ViewInject(R.id.step_2)
	private ImageView stepTwo;
	private String doctorName;
	private String registerTime;
	private String fee;
	private String registerId;
	private String userOrderNum;
	private String doctorId;
	private String teamId;
	private String teamName;
	private String userId;
	private String userName;
	private String userNo;
	private String userTelephone;
	public String sex;
	private User user;

	private static final int RQF_PAY = 1;   //支付宝支付
	private static final int GET_ORDER_INFO = 2;   //加密
	private static final int RECHARGE = 3;   //充值
	private Map<String,String> map;  //解析支付宝返回结果后的map
	private String readyFlag="true";
	private String contactId="";
	private String contactNo="";
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.expert_register_info_config);
		ViewUtils.inject(this);
		addActivity(this);
		initView();
		initValue();
	}

	@OnClick(R.id.ready)
	public void ready(View v)
	{
		if("true".equals(readyFlag))
		{
			readyFlag="false";
			ready.setBackgroundResource(R.drawable.symptom_select_false);
		}else
		{
			readyFlag="true";
			ready.setBackgroundResource(R.drawable.symptom_select_true);
		}
	}
	@OnClick(R.id.edit_user_info)
	public void toHisOrder(View v)
	{
//		Intent intent = new Intent(ExpertRegisterActivity.this, HisOrderActivity.class);
//		intent.putExtra("doctorName", doctorName);
//		intent.putExtra("registerTime", registerTime);
//		intent.putExtra("fee", fee);
//		intent.putExtra("registerId", registerId);
//		intent.putExtra("userOrderNum", userOrderNum);
//
//		intent.putExtra("doctorId", doctorId);
//		intent.putExtra("teamId", teamId);
//		intent.putExtra("teamName", teamName);
//		startActivity(intent);
//		finish();
		Intent intent = new Intent(ExpertRegisterActivity.this,ChooseContactListActivity.class);
		startActivityForResult(intent, 1);
		
	}


	
	@OnClick(R.id.back)
	public void toHome(View v)
	{
		Intent intent = new Intent(ExpertRegisterActivity.this, MainPageActivity.class);
		startActivity(intent);
		exit();
	}

	@OnClick(R.id.submit)
	public void submitOrder(View v)
	{
		if (this.user == null)
		{
			Intent intent = new Intent(ExpertRegisterActivity.this, LoginActivity.class);
			startActivityForResult(intent, 0);
		}
		this.userId = user.getUserId();
		this.userName = editName.getText().toString().trim();
		this.userNo = editIdCard.getText().toString().trim();
		this.userTelephone = editPhone.getText().toString().trim();
		RadioButton radioButton = (RadioButton)findViewById(group.getCheckedRadioButtonId());
		
		String idCheckRst = IDCard.IDCardValidate(userNo);
		if ("".equals(userName))
		{
			HealthUtil.infoAlert(ExpertRegisterActivity.this, "用户名为空.");
			return;
		}
		if(userName.length()>6)
		{
			HealthUtil.infoAlert(ExpertRegisterActivity.this, "用户名长度无效.");
			return;
		}
		if (!HealthUtil.isMobileNum(userTelephone))
		{
			HealthUtil.infoAlert(ExpertRegisterActivity.this, "手机号码为空或格式错误.");
			return;
		}
		if (!"YES".equals(idCheckRst))
		{
			HealthUtil.infoAlert(ExpertRegisterActivity.this, idCheckRst);
			return;
		}
		if(radioButton==null)
		{
			HealthUtil.infoAlert(ExpertRegisterActivity.this, "用户性别为空.");
			return;
		}else
		{
			this.sex=radioButton.getText().toString();
		}
		
		String hospitalId=HealthUtil.readHospitalId();

		if(!userNo.equals(contactNo))
		{
			contactId="";
		}
		
		dialog.setMessage("正在预约,请稍后...");
		dialog.show();
		
		RequestParams param = webInterface.addUserRegisterOrder(hospitalId,userId, registerId, doctorId, doctorName, userOrderNum, fee, registerTime, userName,
				userNo, userTelephone, sex,teamId, teamName,readyFlag,contactId);
		invokeWebServer(param, ADD_REGISTER_ORDER);

	}
	
	@Override
	protected void initView()
	{
		title.setText("信息确认");
//		stepTwo.setBackgroundResource(R.drawable.bg_step_2);
		this.doctorName = getIntent().getStringExtra("doctorName");
		this.registerTime = getIntent().getStringExtra("registerTime");
		this.fee = getIntent().getStringExtra("fee");
		this.registerId = getIntent().getStringExtra("registerId");
		this.userOrderNum = getIntent().getStringExtra("userOrderNum");
		this.doctorId = getIntent().getStringExtra("doctorId");
		this.teamId = getIntent().getStringExtra("teamId");
		this.teamName = getIntent().getStringExtra("teamName");

		int num =Integer.parseInt(userOrderNum);
		if(num>1000)
		{
			num=num-1000;
		}
		
		textViewName.setText(this.doctorName);
		textViewTime.setText(this.registerTime);
		textViewNumber.setText(num+"");
		textViewFee.setText(this.fee+"元");

		group.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1)
			{
				// 获取变更后的选中项的ID
				int radioButtonId = arg0.getCheckedRadioButtonId();
				// 根据ID获取RadioButton的实例
				RadioButton rb = (RadioButton) ExpertRegisterActivity.this.findViewById(radioButtonId);
				// 更新文本内容，以符合选中项
				sex=rb.getText().toString();
			}
		});
	}

	@Override
	protected void initValue()
	{
		this.user = HealthUtil.getUserInfo();
		if (this.user == null)
		{
			Intent intent = new Intent(ExpertRegisterActivity.this, LoginActivity.class);
			startActivityForResult(intent, 0);
		} else
		{
			this.editName.setText(user.getUserName());
			this.editPhone.setText(user.getTelephone());
			this.editIdCard.setText(user.getUserNo());
			this.sex = user.getSex();
			if ("男".equals(user.getSex()))
			{
				maleRadio.setChecked(true);
			} else if ("女".equals(user.getSex()))
			{
				femaleRadio.setChecked(true);
			}
		}
		// TODO Auto-generated method stub

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
		// TODO Auto-generated method stub
		if(intent==null)
		{
			return;
		}
		super.onActivityResult(requestCode, resultCode, intent);
		switch (requestCode)
		{
		case 0:
			this.user = HealthUtil.getUserInfo();
			if (this.user != null)
			{
				this.editName.setText(user.getUserName());
				this.editPhone.setText(user.getTelephone());
				this.editIdCard.setText(user.getUserNo());
				this.sex = user.getSex();
				if ("男".equals(user.getSex()))
				{
					maleRadio.setChecked(true);
				} else if ("女".equals(user.getSex()))
				{
					femaleRadio.setChecked(true);
				}
			}else
			{
				finish();
			}
			break;
		case 1:
			UserContactT contactT=(UserContactT) intent.getSerializableExtra("contactT"); 
			if (contactT != null)
			{
				this.editName.setText(contactT.getContactName());
				this.editPhone.setText(contactT.getContactTelephone());
				this.editIdCard.setText(contactT.getContactNo());
				this.sex = contactT.getContactSex();
				this.contactId=contactT.getContactId();
				this.contactNo=contactT.getContactNo();
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

			HealthUtil.infoAlert(ExpertRegisterActivity.this, "信息加载失败，请检查网络后重试");
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
				if (!"".equals(result))
				{
					HealthUtil.infoAlert(ExpertRegisterActivity.this, "预约成功...");
					Intent intent = new Intent(ExpertRegisterActivity.this, ConfirmOrderActivity.class);
					intent.putExtra("hospitalId", HealthUtil.readHospitalId());
					intent.putExtra("orderId", result);
					intent.putExtra("payState", "100");
					intent.putExtra("doctorName", doctorName);
					intent.putExtra("registerTime", registerTime);
					intent.putExtra("fee", fee);
					intent.putExtra("userOrderNum", userOrderNum);
					intent.putExtra("teamName", teamName);
					intent.putExtra("userName", userName);
					intent.putExtra("userNo", userNo);
					intent.putExtra("userTelephone", userTelephone);
					intent.putExtra("sex", sex);
					
					startActivity(intent);
					finish();
				} else
				{
					HealthUtil.infoAlert(ExpertRegisterActivity.this, "预约失败，请重试...");
				}
				break;
			}
		} catch (Exception e)
		{
			HealthUtil.infoAlert(ExpertRegisterActivity.this, "预约失败，请重试...");
		}

	}
}
