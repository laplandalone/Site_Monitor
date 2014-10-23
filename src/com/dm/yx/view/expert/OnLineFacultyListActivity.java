package com.dm.yx.view.expert;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dm.yx.BaseActivity;
import com.dm.yx.MainPageActivity;
import com.dm.yx.R;
import com.dm.yx.adapter.FacultyListAdapter;
import com.dm.yx.model.Team;
import com.dm.yx.model.TeamList;
import com.dm.yx.tools.HealthConstant;
import com.dm.yx.tools.HealthUtil;
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
 * 在线专家科室列表
 * 
 */
public class OnLineFacultyListActivity extends BaseActivity implements OnItemClickListener
{

	@ViewInject(R.id.title)
	private TextView title;
	
	private ListView list;
	
	private LinearLayout searchLayout;
	
//	@ViewInject(R.id.edit)
//	private EditText edit;
	
	private TeamList teamList;
	FacultyListAdapter adapter ;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_list);
		this.list = (ListView) findViewById(R.id.comlist);
//		this.searchLayout = (LinearLayout) findViewById(R.id.search);
//		searchLayout.setVisibility(View.VISIBLE);
		ViewUtils.inject(this);
		addActivity(this);
		initView();
		initValue();

	}

	@OnClick(R.id.back)
	public void toHome(View v)
	{
		Intent intent = new Intent(OnLineFacultyListActivity.this,MainPageActivity.class);
		startActivity(intent);
		exit();
	}

	@Override
	protected void initView()
	{
		// TODO Auto-generated method stub
		title.setText("专家列表");
//		edit.addTextChangedListener(new TextWatcher() {
//			
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before, int count) {
//				// TODO Auto-generated method stub
//			
//			}
//			
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count,
//					int after) {
//				// TODO Auto-generated method stub
//			}
//			
//			@Override
//			public void afterTextChanged(Editable s) 
//			{
//				// TODO Auto-generated method stub
//				
//				String text = edit.getText().toString();
//				text=pinyinUtil.getPi nyin(text);
//				List<Team> teams = new ArrayList<Team>();
//				for(int i=0;i<teamList.getTeams().size();i++)
//				{
//					Team team = teamList.getTeams().get(i);
//					String pinYin=team.getPinYin();
//					if(pinYin!=null && pinYin.contains(text))
//					{
//						teams.add(team);
//					}
//				}
//				adapter.setTeams(teams);
//				adapter.notifyDataSetChanged();
//			}
//		});
	}

	@Override
	protected void initValue()
	{
		// TODO Auto-generated method stub
		// getListRst();
		dialog.setMessage("正在加载,请稍后...");
		dialog.show();
		String hospitalId=HealthUtil.readHospitalId();
		RequestParams param = webInterface.queryTeamList(hospitalId,"1");
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
			HealthUtil.infoAlert(OnLineFacultyListActivity.this, "信息加载失败，请检查网络后重试");
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
		this.teamList = HealthUtil.json2Object(returnObj.toString(), TeamList.class);
		adapter = new FacultyListAdapter(OnLineFacultyListActivity.this, teamList);
		this.list.setAdapter(adapter);
		this.list.setOnItemClickListener(this);
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		// TODO Auto-generated method stub
		Intent intent = new Intent(OnLineFacultyListActivity.this, OnLineFacultyDescActivity.class);
		Team team =teamList.getTeams().get(position);
		Bundle bundle = new Bundle();
		bundle.putSerializable("team", team);
		intent.putExtras(bundle);
		startActivity(intent);
	}

}
