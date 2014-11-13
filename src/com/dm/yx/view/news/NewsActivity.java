package com.dm.yx.view.news;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dm.yx.BaseActivity;
import com.dm.yx.MainPageActivity;
import com.dm.yx.R;
import com.dm.yx.adapter.NewsListAdapter;
import com.dm.yx.model.HospitalNewsT;
import com.dm.yx.tools.HealthConstant;
import com.dm.yx.tools.HealthUtil;
import com.dm.yx.tools.pinyinUtil;
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

/**
 * 医院资讯
 * 
 */
public class NewsActivity extends BaseActivity implements OnItemClickListener
{
	@ViewInject(R.id.title)
	private TextView title;
	@ViewInject(R.id.contentnull)
	private RelativeLayout layout;
	
	private List<HospitalNewsT> hospitalNewsTs;
	private String hospitalId;
	private ListView list;
	
	@ViewInject(R.id.edit)
	private EditText edit;
	
	NewsListAdapter adapter;
	String adpterFlag="normal";
	private List<HospitalNewsT> searchList = new ArrayList<HospitalNewsT>();
	
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

	@OnClick(R.id.back)
	public void toHome(View v)
	{
		Intent intent = new Intent(NewsActivity.this, MainPageActivity.class);
		startActivity(intent);
		exit();
	}

	@Override
	protected void initView()
	{
		// TODO Auto-generated method stub
		String typeName=getIntent().getStringExtra("typeName");
		title.setText(typeName);
		edit.setHint("请输入关键字");
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
				searchList.clear();
				if (text != null && !text.trim().equalsIgnoreCase("")) 
				{
					String searchtext=pinyinUtil.getPinyin(text);
					 
					boolean firstFlag = pinyinUtil.checkFirstChar(text);
				
					if(firstFlag)
					{
						searchtext=searchtext.toLowerCase();
					}
					for(int i=0;i<hospitalNewsTs.size();i++)
					{
						HospitalNewsT hospitalNewsT = hospitalNewsTs.get(i);
						String title=hospitalNewsT.getNewsTitle();
						String pinYinAll=pinyinUtil.getPinyinAll(title)+"";
						if(firstFlag)
						{
							if(pinYinAll!=null && pinYinAll.contains(searchtext))
							{
								searchList.add(hospitalNewsT);
							}
						}else
						{
							if(text!=null && title.contains(text))
							{
								searchList.add(hospitalNewsT);
							}
						}
					
					}
					adpterFlag="search";
					adapter.setHospitalNewsTs(searchList);;
					adapter.notifyDataSetChanged();
			}else
			{
				adpterFlag="normal";
				adapter.setHospitalNewsTs(hospitalNewsTs);
				adapter.notifyDataSetChanged();
			}
			}
		});
	}

	@Override
	protected void initValue()
	{
		// TODO Auto-generated method stub
		dialog.setMessage("正在加载,请稍后...");
		dialog.show();
		String type=getIntent().getStringExtra("type");
		String typeId=getIntent().getStringExtra("typeId");
		String hospitalId=HealthUtil.readHospitalId();
		RequestParams param = webInterface.getNewsByHospitalId(hospitalId,type,typeId);
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

			HealthUtil.infoAlert(NewsActivity.this, "信息加载失败，请检查网络后重试");
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
			HealthUtil.infoAlert(NewsActivity.this, "加载失败请重试.");
			return;
		}
		JsonArray jsonArray = jsonObject.getAsJsonArray("returnMsg");
		Gson gson = new Gson();
		this.hospitalNewsTs = gson.fromJson(jsonArray, new TypeToken<List<HospitalNewsT>>()
		{
		}.getType());
		adapter = new NewsListAdapter(NewsActivity.this, hospitalNewsTs);
		this.list.setAdapter(adapter);
		this.list.setOnItemClickListener(this);
		if(this.hospitalNewsTs.size()==0)
		{
			layout.setVisibility(View.VISIBLE);
			list.setVisibility(View.GONE);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		// TODO Auto-generated method stub
		Intent intent = new Intent(NewsActivity.this, NewsDetailActivity.class);
		String typeName=getIntent().getStringExtra("typeName");
		HospitalNewsT hospitalNewsT = null;
		if(adpterFlag.equals("normal"))
		{
			hospitalNewsT = this.hospitalNewsTs.get(position);
		}else
		{
			hospitalNewsT = this.searchList.get(position);
		}
		Bundle bundle = new Bundle();
		bundle.putSerializable("hospitalNewsT", hospitalNewsT);
		intent.putExtra("typeName", typeName);
		intent.putExtras(bundle);
		startActivity(intent);
	}
}
