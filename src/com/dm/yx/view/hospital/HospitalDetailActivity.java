package com.dm.yx.view.hospital;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dm.yx.BaseActivity;
import com.dm.yx.MainPageActivity;
import com.dm.yx.R;
import com.dm.yx.adapter.HospitalBranchAdapter;
import com.dm.yx.model.HospitalT;
import com.dm.yx.model.TeamT;
import com.dm.yx.tools.HealthConstant;
import com.dm.yx.tools.HealthUtil;
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
 * 医院导航
 * @author Lapland_Alone
 *
 */
@SuppressLint("NewApi")
public class HospitalDetailActivity extends BaseActivity implements OnItemClickListener
{

	private ListView list;
	
	@ViewInject(R.id.yaxinline)
	private LinearLayout yaxinLayout;
	
	@ViewInject(R.id.hospital_more_detail)
	private LinearLayout hospital_more_detail;
	
	
	@ViewInject(R.id.hospital_more_img)
	private ImageView hospitalImgMore;
	
	@ViewInject(R.id.hospital_more_text)
	private TextView hospitalTextMore;
	
	@ViewInject(R.id.descText)
	private TextView descText;
	
	@ViewInject(R.id.title)
	private TextView title;
	
	@ViewInject(R.id.description)
	private TextView description;
	
	@ViewInject(R.id.website)
	private TextView website;
	
	
	@ViewInject(R.id.yaxinmoreText)
	private TextView yaxinmoreText;
	
	@ViewInject(R.id.yaxinline5)
	private TextView yaxinline5;
	
	@ViewInject(R.id.yaxinline6)
	private TextView yaxinline6;
	
	@ViewInject(R.id.yaxinline7)
	private TextView yaxinline7;
	
	@ViewInject(R.id.yaxinline8)
	private TextView yaxinline8;
	
	@ViewInject(R.id.yaxinline9)
	private TextView yaxinline9;
	
	@ViewInject(R.id.yaxinline10)
	private TextView yaxinline10;
	
	@ViewInject(R.id.yaxinline11)
	private TextView yaxinline11;
	
	@ViewInject(R.id.yaxinline12)
	private TextView yaxinline12;
	
	@ViewInject(R.id.yaxinline13)
	private TextView yaxinline13;
	
	@ViewInject(R.id.yaxinline14)
	private TextView yaxinline14;
	
	@ViewInject(R.id.yaxinline15)
	private TextView yaxinline15;
	
	private List<TeamT> teamTs;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hospital_hospital_detail);
		this.list=(ListView) findViewById(R.id.comlist);
		ViewUtils.inject(this);
		initValue();
		initView();
		
		addActivity(this);
	}

	@OnClick(R.id.back)
	public void toHome(View v)
	{
		Intent intent = new Intent(HospitalDetailActivity.this,MainPageActivity.class);
		startActivity(intent);
		exit();
	}
	
	@OnClick(R.id.yaxinmore)
	public void yaxinmore(View v)
	{
		   int display=0;
		   if(yaxinline5.getVisibility()==View.VISIBLE)
		   {
			   display=View.GONE;
			   yaxinmoreText.setText("更多");
		   }else
		   {
			   display=View.VISIBLE;
			   yaxinmoreText.setText("收起");
		   }
		   
		    yaxinline5.setVisibility(display);
			yaxinline6.setVisibility(display);
			yaxinline7.setVisibility(display);
			yaxinline8.setVisibility(display);
			yaxinline9.setVisibility(display);
			yaxinline10.setVisibility(display);
			yaxinline11.setVisibility(display);
			yaxinline12.setVisibility(display);
			yaxinline13.setVisibility(display);
			yaxinline14.setVisibility(display);
			yaxinline15.setVisibility(display);
	}
	
	@OnClick(R.id.more_detail)
	public void toMoreDetail(View v)
	{
		int maxLine=description.getMaxLines();
		if(maxLine==9)
		{
			description.setMaxLines(description.getLineCount());
			descText.setText("收起");
		}else
		{
			description.setMaxLines(9);
			descText.setText("更多");
		}
		
	}
	
	@Override
	protected void initView()
	{
		// TODO Auto-generated method stub
		
		title.setText("地图导航");
		website.setText("http://www.wahh.com.cn/");
		yaxinLayout.setVisibility(View.VISIBLE);
	}

	private void setListView()
	{   
		if(teamTs.size()<3)
		{
			hospital_more_detail.setVisibility(View.GONE);
		}
		
		int heigths=0;
		String text = hospitalTextMore.getText().toString();
		if("更多".equals(text))
		{
			this.hospitalTextMore.setText("收起");
			heigths=getHeight(list,teamTs.size())+4;
		}else
		{
			this.hospitalTextMore.setText("更多");
			heigths=getHeight(list,2);
		}
		ViewGroup.LayoutParams params = this.list.getLayoutParams(); 
	    params.height =heigths; 
	    this.list.setLayoutParams(params); 
	}
	

    public static int getHeight(ListView listView,int num)
    {
        ListAdapter listAdapter=listView.getAdapter();
        if(listAdapter==null){return 0;}
        int maxHeight=0;
        int itemNum=listAdapter.getCount();
        for(int i=0;i<itemNum;i++)
        {
        	if(i==num)break;
            View listItem=listAdapter.getView(i,null,listView);
            listItem.measure(0,0);
            int thisHeight=listItem.getMeasuredHeight();//计算子项View的宽高
            maxHeight+=thisHeight;
        }
        return maxHeight;
    }
	
	private void intentBaiduMap()
	{
		try
		{
			String uri = "intent://map/marker?location=30.584072,114.266477,&title=123&content=4567&src=健康管家#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end";
			@SuppressWarnings("deprecation")
			Intent intent = Intent.getIntent(uri);
			startActivity(intent);
		} catch (Exception e)
		{
			e.printStackTrace();
			String path = "http://api.map.baidu.com/marker?location=30.584072,114.266477,&title=123&content=4567"+"&output=html";
			Uri uri = Uri.parse(path);
			Intent intent = new Intent(Intent.ACTION_VIEW,uri);
			startActivity(intent);
		}
	}
	
	@Override
	protected void initValue()
	{
		dialog.setMessage("正在加载,请稍后...");
		dialog.show();
		// TODO Auto-generated method stub
		String hospitalId=HealthUtil.readHospitalId();
		RequestParams param = webInterface.getTeamByHospitalId(hospitalId);
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
			HealthUtil.infoAlert(HospitalDetailActivity.this, "信息加载失败，请检查网络后重试");
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
		JsonArray jsonArray = jsonObject.getAsJsonArray("returnMsg");
		Gson gson = new Gson();  
		this.teamTs =	 gson.fromJson(jsonArray, new TypeToken<List<TeamT>>(){}.getType());  
		HospitalBranchAdapter adapter = new HospitalBranchAdapter(HospitalDetailActivity.this, teamTs);
		this.list.setAdapter(adapter);
		this.list.setOnItemClickListener(this);
		setListView();
	}
	
	@OnClick(R.id.hospital_more_detail)
	public void hospitalMore(View v)
	{
		setListView();
	}
	
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		TeamT teamT = teamTs.get(position);
		try
		{
			String uri = "intent://map/marker?location="+teamT.getY()+","+teamT.getX()+",&title="+teamT.getTeamName()+"&content=&src=健康管家#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end";
			@SuppressWarnings("deprecation")
			Intent intent = Intent.getIntent(uri);
			startActivity(intent);
		} catch (Exception e)
		{
			e.printStackTrace();
			String path = "http://api.map.baidu.com/marker?location="+teamT.getY()+","+teamT.getX()+",&title="+teamT.getTeamName()+"&content=&output=html";
			Uri uri = Uri.parse(path);
			Intent intent = new Intent(Intent.ACTION_VIEW,uri);
			startActivity(intent);
		}
		
	}

}
