package com.site.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
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
import com.site.adapter.LineAdapter;
import com.site.model.Line;
import com.site.tools.Constant;
import com.site.tools.SiteUtil;


public class LinesActivity extends BaseActivity implements OnItemClickListener
{
	@ViewInject(R.id.title)
	private TextView title;
	
	@ViewInject(R.id.editUser)
	private TextView editUser;
	
	@ViewInject(R.id.site)
	private TextView site;
	
	
	@ViewInject(R.id.contentnull)
	private RelativeLayout layout;
	
	private List<Line> lines;
	private ListView list;
	
	List<Line> choose= new ArrayList<Line>();
	
	private StringBuffer linesb = new StringBuffer();
	private StringBuffer lineIds = new StringBuffer();
	LineAdapter adapter;
	String adpterFlag="normal";
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.line_list);
		this.list=(ListView) findViewById(R.id.linelist);
		ViewUtils.inject(this);
		addActivity(this);
		initValue();
		initView();
	}

	 

	@OnClick(R.id.editUser)
	public void toSubmit(View v)
	{
		Intent intent = new Intent(LinesActivity.this, LineDetailActivity.class);
		String stopName=getIntent().getStringExtra("stopName"); 
		String stopId=getIntent().getStringExtra("stopId");
		String cityId=getIntent().getStringExtra("cityId");
		linesb=new StringBuffer();
		lineIds=new StringBuffer();
		for(Line l:choose)
		{
			linesb.append(l.getLineName()+",");
			lineIds.append(l.getLineId()+",");
		}
		intent.putExtra("lines", linesb.toString());
		intent.putExtra("lineIds", lineIds.toString());
		
		intent.putExtra("stopName",stopName);
		intent.putExtra("stopId",stopId);
		intent.putExtra("cityId",cityId);
		startActivity(intent);
	
	}

	@Override
	protected void initView()
	{
		// TODO Auto-generated method stub
		String cityId=getIntent().getStringExtra("typeName");
		title.setText("选择线路");
		editUser.setText("确定");
		site.setText("周边站点");
		 
	}

	@Override
	protected void initValue()
	{
		// TODO Auto-generated method stub
		dialog.setMessage("正在加载,请稍后...");
		dialog.show();
		String cityId=SiteUtil.getCity();
		String stopId=getIntent().getStringExtra("stopId");
		RequestParams param = webInterface.getLines(cityId,stopId, "0");
		invokeWebServer(param, GET_LIST);

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
		httpHandler = mHttpUtils.send(HttpMethod.POST, Constant.URL_lines, param, requestCallBack);
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

			SiteUtil.infoAlert(LinesActivity.this, "信息加载失败，请检查网络后重试");
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
		JsonArray nearby  =  data.getAsJsonArray("lines");
		Gson gson = new Gson();
		this.lines = gson.fromJson(nearby, new TypeToken<List<Line>>()
		{
		}.getType());
		adapter = new LineAdapter(LinesActivity.this, lines);
		this.list.setAdapter(adapter);
		this.list.setOnItemClickListener(this);
		if(this.lines.size()==0)
		{
			layout.setVisibility(View.VISIBLE);
			list.setVisibility(View.GONE);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		ImageView imageView =(ImageView) view.findViewById(R.id.choose);
		
		if(imageView.getVisibility()==View.VISIBLE)
		{
			choose.remove(lines.get(position));
			imageView.setVisibility(View.GONE);
		}else
		{
			imageView.setVisibility(View.VISIBLE);
			choose.add(lines.get(position));
		}
	}
}
