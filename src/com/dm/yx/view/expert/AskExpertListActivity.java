package com.dm.yx.view.expert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dm.yx.BaseActivity;
import com.dm.yx.MainPageActivity;
import com.dm.yx.R;
import com.dm.yx.adapter.ExpertListAdapter;
import com.dm.yx.model.Doctor;
import com.dm.yx.model.DoctorList;
import com.dm.yx.model.TeamList;
import com.dm.yx.tools.HealthConstant;
import com.dm.yx.tools.HealthUtil;
import com.dm.yx.tools.pinyinUtil;
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
 * 在线咨询专家列表
 *
 * 
 */
public class AskExpertListActivity extends BaseActivity  implements OnItemClickListener
{

	@ViewInject(R.id.title)
	private TextView title;
	private ListView list;

	private String teamId;
	
	private DoctorList doctorList;
	
	List<Doctor> doctors;
	
	private LinearLayout searchLayout;
	
	@ViewInject(R.id.edit)
	private EditText edit;
	
	private TeamList searchList = new TeamList();
	
	ExpertListAdapter adapter ;
	
	String adpterFlag="normal";
	
	@ViewInject(R.id.contentnull)
	private RelativeLayout layout;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.online_faculty_list);
		this.list=(ListView) findViewById(R.id.asklist);
		this.teamId=getIntent().getStringExtra("teamId");
		ViewUtils.inject(this);
		addActivity(this);
		this.searchLayout = (LinearLayout) findViewById(R.id.search);
		searchLayout.setVisibility(View.VISIBLE);
		initView();
		initValue();
		
	}
	
	@Override
	protected void initView()
	{
		// TODO Auto-generated method stub 2130903241
		title.setText("在线医生");
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
					String searchtext=pinyinUtil.getPinyin(text);
					boolean firstFlag = pinyinUtil.checkFirstChar(text);
					if(firstFlag)
					{
						searchtext=searchtext.toLowerCase();
					}
					doctors = new ArrayList<Doctor>();
					for(int i=0;i<doctorList.getDoctors().size();i++)
					{
						Doctor doctor = doctorList.getDoctors().get(i);
						String pinYin=doctor.getPinYin();
						String name = doctor.getName();
						String pinYinAll=pinyinUtil.getPinyinAll(name)+"";
						if(firstFlag)
						{
							boolean b = true;
							if(pinYin!=null && pinYin.contains(searchtext))
							{
								doctors.add(doctor);
								b=false;
							}
							if(b)
							{
								if(pinYinAll.contains(searchtext))
								{
									doctors.add(doctor);
								}
							}
						}else
						{
							if(text!=null && name.contains(text))
							{
								doctors.add(doctor);
							}
						}
					}
					adpterFlag="search";
					adapter.setDoctors(doctors);
					adapter.notifyDataSetChanged();
			}else
			{
				adpterFlag="normal";
				adapter.setDoctors(doctorList.getDoctors());
				adapter.notifyDataSetChanged();
			}
			}
		});
	}

	@Override
	protected void initValue()
	{
		dialog.setMessage("正在加载,请稍后...");
		dialog.show();
		// TODO Auto-generated method stub
		RequestParams param = webInterface.queryDoctorList("1","0",teamId);
		invokeWebServer(param, GET_LIST);
	}
	@OnClick(R.id.back)
	public void toHome(View v)
	{
		Intent intent = new Intent(AskExpertListActivity.this,MainPageActivity.class);
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
			HealthUtil.infoAlert(AskExpertListActivity.this, "信息加载失败，请检查网络后重试");
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
		adapter = new ExpertListAdapter(AskExpertListActivity.this, doctorList);
		if(this.doctorList.getDoctors().size()==0)
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
		Intent intent = new Intent(AskExpertListActivity.this,TabQuestionActivity.class);
		Doctor doctor=null;
		if(adpterFlag.equals("normal"))
		{
			 doctor = doctorList.getDoctors().get(position);
		}else
		{
			doctor = doctors.get(position);
		}
		
		Bundle bundle = new Bundle();
		bundle.putSerializable("doctor", doctor);
		intent.putExtras(bundle);
		intent.putExtra("questionType", "expert");
		startActivity(intent);
	}

}
