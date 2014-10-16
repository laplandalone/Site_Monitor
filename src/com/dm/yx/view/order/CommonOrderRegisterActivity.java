package com.dm.yx.view.order;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
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
import com.dm.yx.model.Team;
import com.dm.yx.model.User;
import com.dm.yx.tools.DateUtils;
import com.dm.yx.tools.HealthConstant;
import com.dm.yx.tools.HealthUtil;
import com.dm.yx.tools.IDCard;
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

public class CommonOrderRegisterActivity extends BaseActivity
{

	@ViewInject(R.id.title)
	private TextView title;

	@ViewInject(R.id.calendar_btn)
	private ImageButton calendarBtn;

	@ViewInject(R.id.register_date)
	private TextView registerDate;
	
	@ViewInject(R.id.department_name)
	private TextView teamTV;

	@ViewInject(R.id.edit_name)
	private EditText editName;

	@ViewInject(R.id.textView_time_1)
	private TextView time1;
	
	@ViewInject(R.id.textView_time_2)
	private TextView time2;
	
	@ViewInject(R.id.edit_phone)
	private EditText editPhone;

	@ViewInject(R.id.edit_idCard)
	private EditText editIdCard;

	@ViewInject(R.id.image_time_1)
	private ImageView imageTime1;
	
	@ViewInject(R.id.image_time_2)
	private ImageView imageTime2;
	
	@ViewInject(R.id.guahao)
	private Button submitBtn;
	
	@ViewInject(R.id.check_btn)
	private RadioGroup group;
	
	@ViewInject(R.id.step_2)
	private ImageView stepTwo;
	
	String thisDate = DateUtils.getCHNDate();

	ArrayList<String> data = DateUtils.getAfterDate();

	private User user;
	private String doctorName = "0";
	private String registerTime;
	private String fee = "0";
	private String registerId = "0";
	private String userOrderNum = "0";
	private String doctorId = "0";
	private String teamId;
	private String teamName;
	private String userId;
	private String userName;
	private String userNo;
	private String userTelephone;
	private String sex;
	private Team team;
    private String dayTime="上午";
    private String dayWeek="";
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_department_detail);
		ViewUtils.inject(this);
		addActivity(this);
		initView();
		initValue();
	}
	
	@OnClick(R.id.textView_time_1)
	public void chooseDay1(View v)
	{
		String dateStr=registerDate.getText().toString()+" 12:00:00";
		if(DateUtils.checkDay(dateStr))
		{
			HealthUtil.infoAlert(CommonOrderRegisterActivity.this, "预约时间已过！");
			return;
		}
		this.dayTime="上午";
		imageTime1.setVisibility(View.VISIBLE);
		imageTime2.setVisibility(View.GONE);
	}
	
	@OnClick(R.id.textView_time_2)
	public void chooseDay2(View v)
	{
		imageTime1.setVisibility(View.GONE);
		imageTime2.setVisibility(View.VISIBLE);
		this.dayTime="下午";
	}
	
	@OnClick(R.id.edit_user_info)
	public void toHisOrder(View v)
	{
		String dateStr=registerDate.getText().toString()+" 16:30:00";
		if(DateUtils.checkDay(dateStr))
		{
			HealthUtil.infoAlert(CommonOrderRegisterActivity.this, "预约时间已过！");
			return;
		}
		Intent intent = new Intent(CommonOrderRegisterActivity.this, HisOrderActivity.class);
		
		dayWeek=DateUtils.getWeekOfStr(registerDate.getText().toString());
		intent.putExtra("doctorName", doctorName);
		intent.putExtra("registerTime", registerDate.getText()+" 星期"+dayWeek+" "+dayTime);
		intent.putExtra("fee", fee);
		intent.putExtra("registerId",registerId);
		intent.putExtra("userOrderNum", userOrderNum);
		intent.putExtra("doctorId", doctorId);
		intent.putExtra("teamId", teamId);
		intent.putExtra("teamName", teamName);
		startActivity(intent);
		finish();
	}

	@OnClick(R.id.back)
	public void toHome(View v)
	{
		Intent intent = new Intent(CommonOrderRegisterActivity.this, MainPageActivity.class);
		startActivity(intent);
		exit();
	}

	@OnClick(R.id.calendar_btn)
	public void clickCalendarBtn(View v)
	{
		new AlertDialog.Builder(CommonOrderRegisterActivity.this).setTitle("提示").setIcon(android.R.drawable.ic_dialog_map)
				.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data), new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						registerDate.setText(data.get(which));
					}
				}).create().show();
	}

	@Override
	protected void initView()
	{
		title.setText("信息确认");
//		stepTwo.setBackgroundResource(R.drawable.bg_step_2);
		// TODO Auto-generated method stub
		registerDate.setText(thisDate);
		String dateStr=thisDate+" 12:00:00";
		if(DateUtils.checkDay(dateStr))
		{
			imageTime1.setVisibility(View.GONE);
			imageTime2.setVisibility(View.VISIBLE);
			this.dayTime="下午";
		}else
		{
			imageTime1.setVisibility(View.VISIBLE);
			imageTime2.setVisibility(View.GONE);
			this.dayTime="上午";
		}
		
		group.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1)
			{
				// 获取变更后的选中项的ID
				int radioButtonId = arg0.getCheckedRadioButtonId();
				// 根据ID获取RadioButton的实例
				RadioButton rb = (RadioButton) CommonOrderRegisterActivity.this.findViewById(radioButtonId);
				// 更新文本内容，以符合选中项
				sex=rb.getText().toString();
			}
		});
	}

	@Override
	protected void initValue()
	{
		// TODO Auto-generated method stub
		this.team = (Team) getIntent().getSerializableExtra("team");
		this.teamId = team.getTeamId();
		this.teamName = team.getTeamName();
		teamTV.setText(teamName);
		this.user = HealthUtil.getUserInfo();
		if (this.user == null)
		{
			Intent intent = new Intent(CommonOrderRegisterActivity.this, LoginActivity.class);
			startActivityForResult(intent, 0);
		} else
		{
			this.editName.setText(user.getUserName());
			this.editPhone.setText(user.getTelephone());
			this.editIdCard.setText(user.getUserNo());
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, intent);
		switch (resultCode)
		{
		case RESULT_OK:
			this.user = HealthUtil.getUserInfo();
			if (this.user != null)
			{
				this.editName.setText(user.getUserName());
				this.editPhone.setText(user.getTelephone());
				this.editIdCard.setText(user.getUserNo());
			}
			break;

		default:
			break;
		}
	}

	@OnClick(R.id.guahao)
	public void submitOrder(View v)
	{
		this.userId = user.getUserId();
		this.userName = editName.getText().toString().trim();
		this.userNo = editIdCard.getText().toString().trim();
		this.userTelephone = editPhone.getText().toString().trim();
		dayWeek=DateUtils.getWeekOfStr(registerDate.getText().toString());
		
		this.registerTime=registerDate.getText().toString().trim()+" 星期"+dayWeek+" "+dayTime;
		
		String dateStr=registerDate.getText().toString()+" 16:30:00";
		if(DateUtils.checkDay(dateStr))
		{
			HealthUtil.infoAlert(CommonOrderRegisterActivity.this, "预约时间已过！");
			return;
		}
	
		String idCheckRst = IDCard.IDCardValidate(userNo);
		RadioButton radioButton = (RadioButton)findViewById(group.getCheckedRadioButtonId());
		if(radioButton==null)
		{
			HealthUtil.infoAlert(CommonOrderRegisterActivity.this, "用户性别为空!");
			return;
		}else
		{
			this.sex=radioButton.getText().toString();
		}
		if ("".equals(userName))
		{
			HealthUtil.infoAlert(CommonOrderRegisterActivity.this, "用户名为空!");
			return;
		}
		if (!HealthUtil.isMobileNum(userTelephone))
		{
			HealthUtil.infoAlert(CommonOrderRegisterActivity.this, "手机号码为空或格式错误!");
			return;
		}
		if (!"YES".equals(idCheckRst))
		{
			HealthUtil.infoAlert(CommonOrderRegisterActivity.this, idCheckRst);
			return;
		}
		
		dialog.setMessage("正在预约,请稍后...");
		dialog.show();
		String hospitalId=HealthUtil.readHospitalId();
		RequestParams param = webInterface.addUserRegisterOrder(hospitalId,userId, registerId, doctorId, doctorName, userOrderNum, fee, registerTime, userName,
				userNo, userTelephone, sex, teamId, teamName);
		invokeWebServer(param, ADD_REGISTER_ORDER);

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

			HealthUtil.infoAlert(CommonOrderRegisterActivity.this, "信息加载失败，请检查网络后重试");
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
					HealthUtil.infoAlert(CommonOrderRegisterActivity.this, "预约成功...");
					Intent intent = new Intent(CommonOrderRegisterActivity.this, ConfirmOrderActivity.class);
					intent.putExtra("hospitalId", HealthUtil.readHospitalId());
					intent.putExtra("orderId", result);
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
					HealthUtil.infoAlert(CommonOrderRegisterActivity.this, "预约失败，请重试...");
				}
				break;
			}
		} catch (Exception e)
		{
			HealthUtil.infoAlert(CommonOrderRegisterActivity.this, "预约失败，请重试...");
		}

	}

}
