package com.dm.yx.view.user;

import java.util.List;

import org.apache.http.message.BasicNameValuePair;

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
import com.dm.yx.adapter.CheckRstListAdapter;
import com.dm.yx.adapter.CheckRstTypeAdapter;
import com.dm.yx.model.CheckRstT;
import com.dm.yx.tools.HealthConstant;
import com.dm.yx.tools.HealthUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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

public class CheckRstTypeActivity extends BaseActivity  implements OnItemClickListener
{
	
	@ViewInject(R.id.title)
	private TextView title;
	
	private ListView list;
	private String patientId;
	private String checkTime;
	private List<CheckRstT> rstTs;
	
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
		Intent intent = new Intent(CheckRstTypeActivity.this, MainPageActivity.class);
		startActivity(intent);
		exit();
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
			
			HealthUtil.infoAlert(CheckRstTypeActivity.this, "信息加载失败，请检查网络后重试");
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
		
		JsonArray jsonArray = jsonElement.getAsJsonArray();
		
		Gson gson = new Gson();
	   rstTs  = gson.fromJson(jsonArray, new TypeToken<List<CheckRstT>>()
		{
		}.getType());
		if(rstTs!=null && rstTs.size()>0)
		{
			CheckRstTypeAdapter adapter = new CheckRstTypeAdapter(CheckRstTypeActivity.this, rstTs);
			list.setAdapter(adapter);
			list.setOnItemClickListener(this);
		}else
		{
			layout.setVisibility(View.VISIBLE);
			list.setVisibility(View.GONE);
		}
	}
	@Override
	protected void initView()
	{
		patientId=getIntent().getStringExtra("patientId");
		checkTime=getIntent().getStringExtra("checkTime");
		// TODO Auto-generated method stub
		dialog.setMessage("正在加载,请稍后...");
		dialog.show();
		String param="select  check_type,check_type_id from  view_lis_lx_app where patient_id='"+patientId+"' and convert(varchar(10),check_time,102)='"+checkTime+"'"; 
		MineRequestCallBack requestCallBack = new MineRequestCallBack(GET_LIST);
		RequestParams requestParams = new RequestParams("UTF-8");
		BasicNameValuePair nameValuePair = new BasicNameValuePair("param",param);
		requestParams.addBodyParameter(nameValuePair);
		mHttpUtils.send(HttpMethod.POST,  HealthConstant.HIS_URL, requestParams, requestCallBack);
	}
	
	@Override
	protected void initValue()
	{
		// TODO Auto-generated method stub
		title.setText("检验报告单");
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(CheckRstTypeActivity.this,WebActivity.class);
		CheckRstT rstT= rstTs.get(position);
		String checkTypeId=rstT.getCheck_type_id();
		String checkType=rstT.getCheck_type();
		intent.putExtra("url", "http://www.hiseemedical.com:10821/visit/result.jsp?patientId="+patientId+"&checkTime="+checkTime+"&checkTypeId="+checkTypeId+"&checkType="+checkType);
//		intent.putExtra("url", "http://192.168.137.1:7001/visit/result.jsp?patientId="+patientId+"&checkTime="+checkTime+"&checkTypeId="+checkTypeId+"&checkType="+checkType);
//		intent.putExtra("url", "http://www.hiseemedical.com:10821/visit/result.jsp?patientId="+patientId+"&checkTime="+checkTime+"&checkTypeId="+checkTypeId+"&checkType="+checkType);
		intent.putExtra("title", "检验报告单");
		startActivity(intent);
	}

	
}
