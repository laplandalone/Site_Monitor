 package com.site.view;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
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
import com.site.adapter.CarAdapter;
import com.site.model.Cancel;
import com.site.model.Car;
import com.site.tools.Constant;
import com.site.tools.DateUtils;
import com.site.tools.SiteUtil;


public class CardActivity extends BaseActivity implements OnItemClickListener
{
	@ViewInject(R.id.title)
	private TextView title;
	
	@ViewInject(R.id.site)
	private TextView site;
	
	@ViewInject(R.id.editUser)
	private TextView editUser;
	 
	
	@ViewInject(R.id.card)
	private EditText card;
	
	private String cityId ;
	private String oriLineId ;
	private String realLineId ;
	private String carNo;
	private String stopId;
	private String stopName;
	private String lineName;
	String cardName;
	private CarAdapter adapter;
	private ListView list;
	private List<Car> cars2;
	private Car carT;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_sign_in);
		this.list=(ListView) findViewById(R.id.carlist);
		ViewUtils.inject(this);
		addActivity(this);
		
		initValue();
		initView();
	}

	 
	@Override
	protected void initView()
	{
		// TODO Auto-generated method stub
		title.setText("提交车牌");
		site.setText("线路列表");
		editUser.setText("");
	}

	@Override
	protected void initValue()
	{
		// TODO Auto-generated method stub
		 String lineId= getIntent().getStringExtra("lineId");
		 String carStr=getIntent().getStringExtra("car");
		 
		 List<Car> cars= new Gson().fromJson(carStr, new TypeToken<List<Car>>(){}.getType());   
		  cars2 = new ArrayList<Car>();
		 for(Car car:cars)
		 {
			 if(lineId.equals(car.getLineId()))
			 {
				 cars2.add(car);
			 }
		 }
		 
		  adapter = new CarAdapter(CardActivity.this, cars2);
		  this.list.setAdapter(adapter);
	      this.list.setOnItemClickListener(this);
	}

	@OnClick(R.id.site)
	public void site(View v)
	{
		finish();
	}
	
	@OnClick(R.id.site)
	public void cancel(View v)
	{
		finish();
	}
	
	
	@OnClick(R.id.submit)
	public void submit(View v)
	{
		  cityId=SiteUtil.getCity();
		  oriLineId=getIntent().getStringExtra("lineId");
		  realLineId=getIntent().getStringExtra("lineId");
		  carNo=card.getText().toString();
		  stopId=SiteUtil.getStopId();
		  stopName=SiteUtil.getStopName();
		  lineName=getIntent().getStringExtra("lineName");
		  if(carT!=null)
		  {
			  carNo=carT.getCarNo();
		  }
		  else if(carNo==null || "".equals(carNo))
		  {
			  SiteUtil.infoAlert(CardActivity.this, "站牌为空");
			  return;
		  }
		  
		RequestParams param = webInterface.record("0", cityId, oriLineId, realLineId, carNo, stopId, stopName);
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
		httpHandler = mHttpUtils.send(HttpMethod.POST, Constant.URL_record, param, requestCallBack);
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

			SiteUtil.infoAlert(CardActivity.this, "信息加载失败，请检查网络后重试");
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
		String status=jsonObject.get("status").getAsString();
		String cancelId=jsonObject.get("data").getAsString();
		if("00".equals(status))
		{
			Cancel cancel = new Cancel();
			cancel.setCarNo(carNo);
			cancel.setLineName(lineName);
			cancel.setDate(DateUtils.getDateTime());
			cancel.setStopName(stopName);
			cancel.setCancelId(cancelId);
			SiteUtil.writeCancels(cancel, "add");
			showDialog();
		}
	}

	private void showDialog()
	{
		AlertDialog alertDialog = new AlertDialog.Builder(this).setPositiveButton("确定", new OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// TODO Auto-generated method stub
				finish();
			}
		}).setTitle("提示").setMessage("处理成功").create();
			alertDialog.setCanceledOnTouchOutside(false);
			alertDialog.show();
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) 
	{
		
		 	carT = cars2.get(position);
		for(int i=0;i<cars2.size();i++)
		{
			ImageView v = (ImageView) view.findViewById(R.id.choose);
			if(position==i)
			{
			
			    v.setVisibility(View.VISIBLE);
			}else
			{
				v.setVisibility(View.GONE);
			}
			
		}
		
		
	}
}
