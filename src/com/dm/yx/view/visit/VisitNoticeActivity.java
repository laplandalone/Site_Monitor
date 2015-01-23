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
import com.dm.yx.adapter.NewsListAdapter;
import com.dm.yx.adapter.NewsTypeAdapter;
import com.dm.yx.adapter.WakeListAdapter;
import com.dm.yx.model.HospitalNewsT;
import com.dm.yx.model.User;
import com.dm.yx.model.WakeT;
import com.dm.yx.tools.HealthConstant;
import com.dm.yx.tools.HealthUtil;
import com.dm.yx.view.news.NewsActivity;
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

public class VisitNoticeActivity extends BaseActivity implements OnItemClickListener
{
	
	@ViewInject(R.id.title)
	private TextView title;
	
	@ViewInject(R.id.contentnull)
	private RelativeLayout layout;
	private ListView list;
	private String type;
	
	private List<WakeT> wakeTs;
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
		title.setText("我的随访");
	}

	@Override
	protected void initValue()
	{
		// TODO Auto-generated method stub
		// getListRst();
		dialog.setMessage("正在加载,请稍后...");
		dialog.show();
		String hospitalId=HealthUtil.readHospitalId();
		User user = HealthUtil.getUserInfo();
		RequestParams param = webInterface.getUserWake(user.getUserId());
		invokeWebServer(param, GET_LIST);
	}

	@OnClick(R.id.back)
	public void toHome(View v)
	{
		Intent intent = new Intent(VisitNoticeActivity.this, MainPageActivity.class);
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
			HealthUtil.infoAlert(VisitNoticeActivity.this, "信息加载失败，请检查网络后重试");
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
			HealthUtil.infoAlert(VisitNoticeActivity.this, "加载失败请重试.");
			return;
		}
		JsonArray jsonArray = jsonObject.getAsJsonArray("returnMsg");
		Gson gson = new Gson();
		this.wakeTs = gson.fromJson(jsonArray, new TypeToken<List<WakeT>>()
		{
		}.getType());
		WakeListAdapter adapter = new WakeListAdapter(VisitNoticeActivity.this, wakeTs);
		if(this.wakeTs.size()==0)
		{
			layout.setVisibility(View.VISIBLE);
			list.setVisibility(View.GONE);
		}
		this.list.setAdapter(adapter);
		this.list.setOnItemClickListener(this);
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		// TODO Auto-generated method stub
		Intent intent = new Intent(VisitNoticeActivity.this, VisitNoticeDetailActivity.class);
		WakeT wakeT = wakeTs.get(position);
		Bundle bundle = new Bundle();
		bundle.putSerializable("wakeT", wakeT);
		intent.putExtras(bundle);
		startActivity(intent);
	}

}
