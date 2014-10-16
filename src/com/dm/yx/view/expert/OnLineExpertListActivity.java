package com.dm.yx.view.expert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.dm.yx.BaseActivity;
import com.dm.yx.MainPageActivity;
import com.dm.yx.R;
import com.dm.yx.adapter.ExpertListAdapter;
import com.dm.yx.model.Doctor;
import com.dm.yx.model.DoctorList;
import com.dm.yx.tools.HealthConstant;
import com.dm.yx.tools.HealthUtil;
import com.dm.yx.view.faculty.DoctorDetailActivity;
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

/**
 * 科室医栏表
 *
 */
public class OnLineExpertListActivity extends BaseActivity  implements OnItemClickListener
{

	@ViewInject(R.id.title)
	private TextView title;
	
	@ViewInject(R.id.textTitle)
	private TextView textTitle;
	
	private ListView list;

	private String teamId;
	private DoctorList doctorList;
	private List<Map<String, Object>> unhandList = new ArrayList<Map<String, Object>>();
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.online_faculty_list);
		this.list=(ListView) findViewById(R.id.asklist);
		this.teamId=getIntent().getStringExtra("teamId");
		ViewUtils.inject(this);
		addActivity(this);
		initView();
		initValue();
	}
	
	@Override
	protected void initView()
	{
		textTitle.setVisibility(View.GONE);
		// TODO Auto-generated method stubtotal_count
		title.setText("医生列表");
	}

	@Override
	protected void initValue()
	{
		dialog.setMessage("正在加载,请稍后...");
		dialog.show();
		// TODO Auto-generated method stub
		RequestParams param = webInterface.queryDoctorList("1",null,teamId);
		invokeWebServer(param, GET_LIST);
	}
	
	@OnClick(R.id.back)
	public void toHome(View v)
	{
		Intent intent = new Intent(OnLineExpertListActivity.this,MainPageActivity.class);
		startActivity(intent);
		exit();
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
			HealthUtil.infoAlert(OnLineExpertListActivity.this, "信息加载失败，请检查网络后重试");
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
		this.doctorList = HealthUtil.json2Object(returnObj.toString(), DoctorList.class);
		ExpertListAdapter adapter = new ExpertListAdapter(OnLineExpertListActivity.this, doctorList);
		this.list.setAdapter(adapter);
		this.list.setOnItemClickListener(this);
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		// TODO Auto-generated method stub
		Intent intent = new Intent(OnLineExpertListActivity.this,DoctorDetailActivity.class);
		Doctor doctor = doctorList.getDoctors().get(position);
		Bundle bundle = new Bundle();
		bundle.putSerializable("doctor", doctor);
		intent.putExtras(bundle);
		startActivity(intent);
	}

}
