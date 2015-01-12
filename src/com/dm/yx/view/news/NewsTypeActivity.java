package com.dm.yx.view.news;

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
import com.dm.yx.adapter.NewsTypeAdapter;
import com.dm.yx.tools.HealthConstant;
import com.dm.yx.tools.HealthUtil;
import com.google.gson.JsonArray;
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

public class NewsTypeActivity extends BaseActivity implements OnItemClickListener
{
	
	@ViewInject(R.id.title)
	private TextView title;
	
	@ViewInject(R.id.contentnull)
	private RelativeLayout layout;
	private ListView list;
	private String type;
	
	private JsonArray jsonArray;
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

	@Override
	protected void initView()
	{
		this.type=getIntent().getStringExtra("type");
		if("BAIKE".equals(type))
		{
			title.setText("就医帮助");
		}else
		{
			title.setText("患教中心");
		}
	}

	@Override
	protected void initValue()
	{
		// TODO Auto-generated method stub
		// getListRst();
		dialog.setMessage("正在加载,请稍后...");
		dialog.show();
		String hospitalId=HealthUtil.readHospitalId();
		RequestParams param = webInterface.getNewsType(hospitalId, this.type);
		invokeWebServer(param, GET_LIST);
	}

	@OnClick(R.id.back)
	public void toHome(View v)
	{
		Intent intent = new Intent(NewsTypeActivity.this, MainPageActivity.class);
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
			HealthUtil.infoAlert(NewsTypeActivity.this, "信息加载失败，请检查网络后重试");
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
	 * executeType
	 */
	private void returnMsg(String json, int code)
	{
		JsonParser jsonParser = new JsonParser();
		JsonElement jsonElement = jsonParser.parse(json);
		
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		String executeType = jsonObject.get("executeType").getAsString();
		if (!"success".equals(executeType))
		{
			HealthUtil.infoAlert(NewsTypeActivity.this, "加载失败请重试.");
			return;
		}
		this.jsonArray = jsonObject.getAsJsonArray("returnMsg");
		if(this.jsonArray.size()==0)
		{
			layout.setVisibility(View.VISIBLE);
			list.setVisibility(View.GONE);
		}
		
		NewsTypeAdapter adapter = new NewsTypeAdapter(NewsTypeActivity.this, jsonArray);
		this.list.setAdapter(adapter);
		this.list.setOnItemClickListener(this);
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		// TODO Auto-generated method stub
		Intent intent = new Intent(NewsTypeActivity.this, NewsActivity.class);
		JsonElement element= jsonArray.get(position);
		JsonObject jsonObject = element.getAsJsonObject();
		String typeId=jsonObject.get("configId").getAsString();
		String configVal=jsonObject.get("configVal").getAsString();
		intent.putExtra("typeId", typeId);
		intent.putExtra("type", type);
		intent.putExtra("typeName", configVal);
		startActivity(intent);
	}

}
