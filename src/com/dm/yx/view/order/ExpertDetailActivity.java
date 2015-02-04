package com.dm.yx.view.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dm.yx.BaseActivity;
import com.dm.yx.MainPageActivity;
import com.dm.yx.R;
import com.dm.yx.adapter.ExpertDetailAdapter;
import com.dm.yx.model.OrderExpert;
import com.dm.yx.model.OrderExpertList;
import com.dm.yx.tools.HealthConstant;
import com.dm.yx.tools.HealthUtil;
import com.dm.yx.tools.ObjectCensor;
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

/**
 * 专家详情
 *
 */
public class ExpertDetailActivity extends BaseActivity implements OnItemClickListener
{
	@ViewInject(R.id.title)
	private TextView title;

	@ViewInject(R.id.doctorName)
	private TextView doctorName;
	@ViewInject(R.id.facultyName)
	private TextView facultyName;
	@ViewInject(R.id.position)
	private TextView position;
	@ViewInject(R.id.departNameDec)
	private TextView departNameDec;
	@ViewInject(R.id.orderTime)
	private TextView orderTime;
	@ViewInject(R.id.weekTime)
	private TextView weekTime;
	
	@ViewInject(R.id.doctor_head)
	ImageView doctorHead;
	
	private ListView list;
	
	private OrderExpert expert; 
	private OrderExpertList expertList;
	
	private BitmapUtils bitmapUtils;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.expert_doctor_detail);
		this.list=(ListView) findViewById(R.id.list_view);
		this.expert=(OrderExpert) getIntent().getSerializableExtra("expert");
		ViewUtils.inject(this);
		addActivity(this);
		bitmapUtils = new BitmapUtils(this);
		bitmapUtils.closeCache();
		initView();
		initValue();
	}
	@OnClick(R.id.back)
	public void toHome(View v)
	{
		Intent intent = new Intent(ExpertDetailActivity.this,MainPageActivity.class);
		startActivity(intent);
		exit();
	}
	@Override
	protected void initView()
	{
		// TODO Auto-generated method stub
		title.setText("预约时间");
	}

	@Override
	protected void initValue()
	{
		
		dialog.setMessage("正在加载,请稍后...");
		dialog.show();
		
		this.doctorName.setText(expert.getDoctorName());
		this.facultyName.setText(expert.getTeamName());
		this.position.setText(expert.getPost());
		this.departNameDec.setText(expert.getIntroduce());
	
		String date=expert.getDay();
		String week=expert.getWeek();
		// TODO Auto-generated method stub
		this.orderTime.setText(date);
		this.weekTime.setText("星期"+week);
		String teamId=expert.getTeamId();
		String userId=HealthUtil.readUserId();
		String hospitalId=HealthUtil.readHospitalId();
		RequestParams param = webInterface.queryOrderByDoctorIdList(hospitalId,userId,teamId,expert.getDoctorId(),week,date);
		//RequestParams param = webInterface.queryOrderDoctorList("10001");
		invokeWebServer(param, GET_LIST);
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
			if (list != null)
			{
				// list.stopLoadMore();
			}
			HealthUtil.infoAlert(ExpertDetailActivity.this, "信息加载失败，请检查网络后重试");
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
		this.expertList = HealthUtil.json2Object(returnObj.toString(), OrderExpertList.class);
		ExpertDetailAdapter adapter = new ExpertDetailAdapter(ExpertDetailActivity.this, expertList);
		this.list.setAdapter(adapter);
		this.list.setOnItemClickListener(this);
		if(this.expertList!=null && this.expertList.getOrders().size()!=0)
		{
			OrderExpert expert = expertList.getOrders().get(0);
			departNameDec.setText(expert.getSkill());
			this.position.setText(expert.getPost());
			bitmapUtils.configDefaultLoadingImage(R.drawable.doctor_head);
			bitmapUtils.configDefaultLoadFailedImage(R.drawable.doctor_head);
			bitmapUtils.display(doctorHead,expert.getPhotoUrl());
		}
		
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		// TODO Auto-generated method stub
		Intent intent = new Intent(ExpertDetailActivity.this,ExpertRegisterActivity.class);
		
		OrderExpert orderExpert=expertList.getOrders().get(position);
		String doctorName=expert.getDoctorName();
		String registerTime=orderExpert.getWorkTime();
		String fee=orderExpert.getFee();
		String registerId=orderExpert.getRegisterId();
		String userOrderNum=orderExpert.getUserOrderNum();
		String doctorId=orderExpert.getDoctorId();
		String teamId=orderExpert.getTeamId();
		String teamName=orderExpert.getTeamName();
		String day=orderExpert.getDay();
		String userFlag=orderExpert.getUserFlag();
		String orderTeamCount=orderExpert.getOrderTeamCount();
		String numMax=orderExpert.getNumMax();
		
		if("1000".equals(userOrderNum))
		{
			HealthUtil.infoAlert(ExpertDetailActivity.this, "该时间预约号已满,请选择其他时间");
			return;
		}
		if(ObjectCensor.isStrRegular(orderTeamCount))
		{
			int num= Integer.parseInt(orderTeamCount);
			if(num>=2)
			{
				HealthUtil.infoAlert(ExpertDetailActivity.this, "同一天最多能预约二个号");
				return;
			}
		}
		if("Y".equals(userFlag))
		{
			HealthUtil.infoAlert(ExpertDetailActivity.this, "已预约,请重新选择");
			return;
		}
		if("true".equals(numMax))
		{
			HealthUtil.infoAlert(ExpertDetailActivity.this, "该时间预约号已满,请选择其他时间");
			return;
		}
		
		intent.putExtra("doctorName", doctorName);
		intent.putExtra("registerTime", day+" "+registerTime);
		intent.putExtra("fee", fee);
		intent.putExtra("registerId", registerId);
		intent.putExtra("userOrderNum", userOrderNum);
		
		intent.putExtra("doctorId", doctorId);
		intent.putExtra("teamId", teamId);
		intent.putExtra("teamName", teamName);
		startActivity(intent);
	}

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initView();
		initValue();
	}
}
