package com.dm.yx.view.visit;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dm.yx.BaseActivity;
import com.dm.yx.MainPageActivity;
import com.dm.yx.R;
import com.dm.yx.adapter.ConfigListAdapter;
import com.dm.yx.model.HospitalConfigT;
import com.dm.yx.model.User;
import com.dm.yx.tools.HealthConstant;
import com.dm.yx.tools.HealthUtil;
import com.dm.yx.view.user.LoginActivity;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class PatientVisitListActivity extends BaseActivity implements OnItemClickListener
{
	
	@ViewInject(R.id.title)
	private TextView title;
	private String userId="";
	private User user;
	private List<HospitalConfigT> hospitalConfigTs;
	private ConfigListAdapter adapter;
	private ListView list;
	@ViewInject(R.id.contentnull)
	private RelativeLayout layout;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_list);
		list = (ListView) findViewById(R.id.comlist);
		ViewUtils.inject(this);
		addActivity(this);
		initView();
		initValue();
	}

	@OnClick(R.id.back)
	public void toBack(View v)
	{
		Intent intent = new Intent(PatientVisitListActivity.this, MainPageActivity.class);
		startActivity(intent);
		exit();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, intent);
		switch (requestCode)
		{
		case 0:
			this.user = HealthUtil.getUserInfo();
			if (this.user != null)
			{
				userId=user.getUserId();
			}else
			{
				finish();
			}
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void initView()
	{
		// TODO Auto-generated method stub
		
	}
	

	@Override
	protected void initValue()
	{
		// TODO Auto-generated method stub
		title.setText("患者随访");
		
		user=HealthUtil.getUserInfo();
		if (this.user == null)
		{
			Intent intent = new Intent(PatientVisitListActivity.this, LoginActivity.class);
			startActivityForResult(intent, 0);
		}else
		{
			userId=user.getUserId();
		
				// TODO Auto-generated method stub
			dialog.setMessage("正在加载,请稍后...");
			dialog.show();
			String type=getIntent().getStringExtra("type");
			String typeId=getIntent().getStringExtra("typeId");
			String hospitalId=HealthUtil.readHospitalId();
			RequestParams param = webInterface.getHospitalConfig("102", "VISIT_TYPE");
			invokeWebServer(param, GET_LIST);

			
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

			HealthUtil.infoAlert(PatientVisitListActivity.this, "信息加载失败，请检查网络后重试");
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
		String executeType = jsonObject.get("executeType").getAsString();
		if (!"success".equals(executeType))
		{
			HealthUtil.infoAlert(PatientVisitListActivity.this, "加载失败请重试.");
			return;
		}
		JsonArray jsonArray = jsonObject.getAsJsonArray("returnMsg");
		Gson gson = new Gson();
		this.hospitalConfigTs = gson.fromJson(jsonArray, new TypeToken<List<HospitalConfigT>>()
		{
		}.getType());
		adapter = new ConfigListAdapter(PatientVisitListActivity.this, hospitalConfigTs);
		this.list.setAdapter(adapter);
		this.list.setOnItemClickListener(this);
		if(this.hospitalConfigTs.size()==0)
		{
			layout.setVisibility(View.VISIBLE);
			list.setVisibility(View.GONE);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		// TODO Auto-generated method stub
		Intent intent = new Intent(PatientVisitListActivity.this, VisitDetailActivity.class);
		HospitalConfigT hospitalConfigT = hospitalConfigTs.get(position);
		String url=hospitalConfigT.getRemark();
		String name=hospitalConfigT.getConfigVal();
		User user=HealthUtil.getUserInfo();
		String patientId=user.getCardNo();
		String operType="无";
//		url="http://192.168.137.1:7001/visit/asdAdult.jsp";
		intent.putExtra("url", url+"?name="+user.getUserName()+"&patientId="+patientId+"&operType="+operType);
		intent.putExtra("title", name);
		startActivity(intent);
		 
	}
}
