package com.site.view;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.LocationClient;
import com.dm.yx.R;
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
import com.site.BaseActivity;
import com.site.adapter.NearBysListAdapter;
import com.site.model.City;
import com.site.model.NearBy;
import com.site.tools.HealthConstant;
import com.site.tools.HealthUtil;

/**
 * 医院资讯
 * 
 */
public class NearByActivity extends BaseActivity implements OnItemClickListener
{
	
	@ViewInject(R.id.title)
	private TextView title;
	
	@ViewInject(R.id.editUser)
	private TextView editUser;
	
	@ViewInject(R.id.site)
	private TextView site;
	
	
	@ViewInject(R.id.contentnull)
	private RelativeLayout layout;
	
	private List<NearBy> nearBys;
	private ListView list;
	
	LocationClient mLocClient;
	
	NearBysListAdapter adapter;
	String cityId="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_article_list);
		this.list=(ListView) findViewById(R.id.newlist);
		ViewUtils.inject(this);
		addActivity(this);
		initValue();
		initView();
	}

	
	@OnClick(R.id.site)
	public void toCity(View v)
	{
		Intent intent = new Intent(NearByActivity.this, CityActivity.class);
		startActivity(intent);
		exit();
	}

	@OnClick(R.id.editUser)
	public void toSearch(View v)
	{
		Intent intent = new Intent(NearByActivity.this, SearchActivity.class);
		Bundle bundle = new Bundle();
		intent.putExtra("cityId", cityId);
		startActivity(intent);
	}
	
	@Override
	protected void initView()
	{
		// TODO Auto-generated method stub
		City city=(City) getIntent().getSerializableExtra("city");
		title.setText("周边站点");
		editUser.setText("站名");
		if(HealthUtil.getCity()!=null && !"".equals(HealthUtil.getCity()) && !"null".equals(HealthUtil.getCity()) )
		{
			site.setText(HealthUtil.getCity());
		}
		
		if(city!=null)
		{
			site.setText(city.getCityName());
			cityId=city.getCityId();
		}
	}

	@Override
	protected void initValue()
	{
		City city=(City) getIntent().getSerializableExtra("city");
		 
		String lat=HealthUtil.getLatitude();
		String lit=HealthUtil.getLongitude();
		if(city!=null)
		{
			// TODO Auto-generated method stub
			dialog.setMessage("正在加载,请稍后...");
			dialog.show();
		
			RequestParams param = webInterface.getNearBy(city.getCityId(),HealthUtil.getLongitude(),HealthUtil.getLatitude());
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

			HealthUtil.infoAlert(NearByActivity.this, "信息加载失败，请检查网络后重试");
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
		JsonObject jsonr = jsonObject.getAsJsonObject("jsonr");
		JsonObject data =  jsonr.getAsJsonObject("data");
		JsonArray nearby  =  data.getAsJsonArray("nearby");
		Gson gson = new Gson();
		this.nearBys = gson.fromJson(nearby, new TypeToken<List<NearBy>>()
		{
		}.getType());
		adapter = new NearBysListAdapter(NearByActivity.this, nearBys);
		this.list.setAdapter(adapter);
		this.list.setOnItemClickListener(this);
		if(this.nearBys.size()==0)
		{
			layout.setVisibility(View.VISIBLE);
			list.setVisibility(View.GONE);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		// TODO Auto-generated method stub
		Intent intent = new Intent(NearByActivity.this, LinesActivity.class);
		NearBy nearBy = nearBys.get(position);
		Bundle bundle = new Bundle();
		bundle.putSerializable("nearBy", nearBy);
		intent.putExtra("cityId", cityId);
		intent.putExtras(bundle);
		startActivity(intent);
	}
}
