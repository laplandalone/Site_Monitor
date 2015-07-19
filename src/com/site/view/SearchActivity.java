package com.site.view;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.site.R;
import com.site.adapter.SearchLineAdapter;
import com.site.model.City;
import com.site.model.SearchLine;
import com.site.tools.Constant;
import com.site.tools.SiteUtil;

/**
 * 医院资讯
 * 
 */
public class SearchActivity extends BaseActivity implements OnItemClickListener
{
	@ViewInject(R.id.title)
	private TextView title;
	
	@ViewInject(R.id.editUser)
	private TextView editUser;
	
	@ViewInject(R.id.site)
	private TextView site;
	
	@ViewInject(R.id.edit)
	private EditText edit;
	
	@ViewInject(R.id.contentnull)
	private RelativeLayout layout;
	
	private List<SearchLine> searchLines;
	private ListView list;
	
 
	
	SearchLineAdapter adapter;
	String cityId="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_list);
		this.list=(ListView) findViewById(R.id.comlist);
		ViewUtils.inject(this);
		addActivity(this);
		initValue();
		initView();
	}

	
	@OnClick(R.id.site)
	public void toCity(View v)
	{
		Intent intent = new Intent(SearchActivity.this, CityActivity.class);
		startActivity(intent);
		exit();
	}

	@OnClick(R.id.editUser)
	public void toSearch(View v)
	{
		Intent intent = new Intent(SearchActivity.this, CityActivity.class);
		startActivity(intent);
		exit();
	}
	
	@Override
	protected void initView()
	{
		// TODO Auto-generated method stub
		 cityId=getIntent().getStringExtra("cityId");
		 
	    title.setText("周边站点");
			editUser.setText("站名");
			 
		 
	 
		 edit.setOnFocusChangeListener(onFocusAutoClearHintListener);
		 edit.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					// TODO Auto-generated method stub
				
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					// TODO Auto-generated method stub
				}
				
				@Override
				public void afterTextChanged(Editable s) 
				{
					// TODO Auto-generated method stub
					String text = edit.getText().toString();
					if (text != null && !text.trim().equalsIgnoreCase("")) 
					{
						RequestParams param = webInterface.getLineName(cityId,text,"0");
						invokeWebServer(param, GET_LIST);
					}else
					{
//						list.setAdapter(adapter);
//						adapter.setTeams(teamList.getTeams());
//						adapter.notifyDataSetChanged();
//						adpterFlag="faculty";
					}
				}
			});
	}

	@Override
	protected void initValue()
	{
		City city=(City) getIntent().getSerializableExtra("city");
		
		
	}

	/**
	 * 链接web服务
	 * 
	 * @param param
	 */
	private void invokeWebServer(RequestParams param, int responseCode)
	{
		SiteUtil.LOG_D(getClass(), "connect to web server");
		MineRequestCallBack requestCallBack = new MineRequestCallBack(responseCode);
		if (httpHandler != null)
		{
			httpHandler.cancel();
		}
		httpHandler = mHttpUtils.send(HttpMethod.POST, Constant.URL_lineName, param, requestCallBack);
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
			SiteUtil.LOG_D(getClass(), "onFailure-->msg=" + msg);
			if (dialog.isShowing())
			{
				dialog.cancel();
			}

			SiteUtil.infoAlert(SearchActivity.this, "信息加载失败，请检查网络后重试");
		}

		@Override
		public void onSuccess(ResponseInfo<String> arg0)
		{
			// TODO Auto-generated method stub
			SiteUtil.LOG_D(getClass(), "result=" + arg0.result);
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
		JsonArray nearby  =  data.getAsJsonArray("stoplist");
		Gson gson = new Gson();
		this.searchLines = gson.fromJson(nearby, new TypeToken<List<SearchLine>>()
		{
		}.getType());
		adapter = new SearchLineAdapter(SearchActivity.this, searchLines);
		this.list.setAdapter(adapter);
		this.list.setOnItemClickListener(this);
		if(this.searchLines.size()==0)
		{
			layout.setVisibility(View.VISIBLE);
			list.setVisibility(View.GONE);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		// TODO Auto-generated method stub
		Intent intent = new Intent(SearchActivity.this, LinesActivity.class);
		SearchLine searchLine = searchLines.get(position);
		 
		Bundle bundle = new Bundle();
		intent.putExtra("cityId", cityId);
		intent.putExtra("stopName", searchLine.getStopName());
		intent.putExtra("stopId", searchLine.getStopId());
		intent.putExtras(bundle);
		startActivity(intent);
	}
}
