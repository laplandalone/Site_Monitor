 package com.site.view;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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
import com.site.BaseActivity;
import com.site.R;
import com.site.tools.Constant;
import com.site.tools.SiteUtil;
import com.site.tools.StringUtil;

public class CardActivity extends BaseActivity
{
	@ViewInject(R.id.title)
	private TextView title;
	
	@ViewInject(R.id.site)
	private TextView site;
	
	@ViewInject(R.id.carName)
	private TextView carName;
	
	@ViewInject(R.id.card)
	private EditText card;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_sign_in);
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
	}

	@Override
	protected void initValue()
	{
		// TODO Auto-generated method stub
		String cardName= getIntent().getStringExtra("carName");
		String stopFlag = getIntent().getStringExtra("stopFlag");
		this.carName.setText(cardName);
		 int stop=0;
         if(StringUtil.checkStringIsNum(stopFlag))
         {
         	stop=Integer.parseInt(stopFlag);
         }
         if(stop==0)//到站
         {
        	 this.carName.setBackgroundResource(R.drawable.red_bg);
         }else
         if(stop>=1 && stop<=3)//到站
         {
        	 this.carName.setBackgroundResource(R.drawable.bg);
         }else
         if(stop<0)//到站
         {
        	 this.carName.setBackgroundResource(R.drawable.arrived);
         }
	}

	@OnClick(R.id.submit)
	public void submit(View v)
	{
		String cityId=SiteUtil.getCity();
		String oriLineId=getIntent().getStringExtra("lineId");
		String realLineId=getIntent().getStringExtra("lineId");
		String carNo=card.getText().toString();
		String stopId=SiteUtil.getStopId();
		String stopName=SiteUtil.getStopName();
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
	
	}

	
}
