package com.dm.yx.view.faculty;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dm.yx.BaseActivity;
import com.dm.yx.MainPageActivity;
import com.dm.yx.R;
import com.dm.yx.model.Doctor;
import com.dm.yx.model.OrderExpertList;
import com.dm.yx.tools.HealthConstant;
import com.dm.yx.tools.HealthUtil;
import com.dm.yx.view.order.RegisteredMain;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class DoctorDetailActivity extends BaseActivity
{
	@ViewInject(R.id.title)
	private TextView title;
	@ViewInject(R.id.doctor_name)
	private TextView doctorName;
	@ViewInject(R.id.doctot_introduction)
	private TextView doctotIntroduction;
	@ViewInject(R.id.doctor_position)
	private TextView doctorPosition;
	@ViewInject(R.id.out_patient_time)
	private TextView outPatientTime;
	@ViewInject(R.id.out_patient_place)
	private TextView outPatientPlace;
	@ViewInject(R.id.guahao_fee)
	private TextView guahaoFee;
	@ViewInject(R.id.skill)
	private TextView skill;
	@ViewInject(R.id.registerClick)
	private ImageView registerClick;
	@ViewInject(R.id.doctor_photo)
	private ImageView photo;
	
	private LinearLayout register;
	private ListView list;
	private BitmapUtils bitmapUtils;
	private Doctor  doctor;
	private OrderExpertList expertList;
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_doctor_detail);
		this.doctor=(Doctor) getIntent().getSerializableExtra("doctor");
		ViewUtils.inject(this);
		addActivity(this);
		initView();
		initValue();
	}

	@OnClick(R.id.back)
	public void toHome(View v)
	{
		Intent intent = new Intent(DoctorDetailActivity.this,MainPageActivity.class);
		startActivity(intent);
		exit();
	}
	

	@OnClick(R.id.register)
	public void register(View v)
	{
		Intent intent = new Intent(DoctorDetailActivity.this,RegisteredMain.class);
		intent.putExtra("registerChannel", "doctor");
		intent.putExtra("doctorName",this.doctor.getName());
		startActivity(intent);
	}
	
	@Override
	protected void initView()
	{
		// TODO Auto-generated method stub
		title.setText("医生详情");
	}

	@Override
	protected void initValue()
	{
		bitmapUtils = new BitmapUtils(this);
		bitmapUtils.closeCache();
		// TODO Auto-generated method stub
		this.doctorName.setText(this.doctor.getName());
		this.doctotIntroduction.setText(this.doctor.getIntroduce());
		this.skill.setText(this.doctor.getSkill());
		this.doctorPosition.setText(this.doctor.getPost());
		this.outPatientTime.setText(this.doctor.getWorkTime());
		String fee = this.doctor.getRegisterFee();
		if(null==fee ||  "".equals( fee ) || "null".equals( fee ))
		{
			fee="无";
		}else
		{
			fee+="元";
		}
		this.guahaoFee.setText(fee);
		this.outPatientPlace.setText(this.doctor.getWorkAddress());
		String photoUrl=this.doctor.getPhotoUrl();
		 if(photoUrl.endsWith("jpg") || photoUrl.endsWith("png"))
		 {
			 bitmapUtils.display(this.photo,photoUrl);
		 }
		 dialog.setMessage("正在加载,请稍后...");
		 dialog.show();
		 RequestParams param = webInterface.getTimeRegister(this.doctor.getName());
		 invokeWebServer(param, GET_ORDER_LIST);
			
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
			if (list != null)
			{
				// list.stopLoadMore();
			}
			HealthUtil.infoAlert(DoctorDetailActivity.this, "信息加载失败，请检查网络后重试");
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
			case GET_ORDER_LIST:
				returnMsg(arg0.result, GET_ORDER_LIST);
				break;
			}
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
		JsonObject returnObj = jsonObject.getAsJsonObject("returnMsg");
		switch (code) 
		{
			
			case GET_ORDER_LIST:
				this.expertList = HealthUtil.json2Object(returnObj.toString(), OrderExpertList.class);
				if(expertList.getOrders().size()!=0)
				{
					registerClick.setVisibility(View.VISIBLE);
				}
			default:
				break;
			}
		

	}

}
