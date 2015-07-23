package com.site.view;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
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
import com.site.model.Car;
import com.site.tools.Constant;
import com.site.tools.SiteUtil;
import com.site.tools.StringUtil;
import com.site.ui.MainActivity;


@SuppressLint("ResourceAsColor")
public class LineDetailActivity extends BaseActivity implements OnItemClickListener
{
	@ViewInject(R.id.title)
	private TextView title;
	
	@ViewInject(R.id.editUser)
	private TextView editUser;
	
	@ViewInject(R.id.site)
	private TextView site;
	
	@ViewInject(R.id.lineName)
	private TextView lineName;
	
	@ViewInject(R.id.layout)
	private LinearLayout layout;
	
	private List<Car> cars;
	private ListView list;
	
	
	
	LineAdapter adapter;
	String adpterFlag="normal";
	
	String cityId;
	String stopId;
	String stopName;
	String lineIds;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.line);
		ViewUtils.inject(this);
		addActivity(this);
		initValue();
		initView();
	}

	@OnClick(R.id.site)
	public void site(View v)
	{
		finish();
	}
	
	@OnClick(R.id.lineName)
	public void toMap(View v)
	{
		Intent intent = new Intent(LineDetailActivity.this,MainActivity.class);
		intent.putExtra("lineIds", lineIds);
		startActivity(intent);
	}
	
	@OnClick(R.id.editUser)
	public void toCancel(View v)
	{
		Intent intent = new Intent(LineDetailActivity.this,CancelActivity.class);
		
		startActivity(intent);
	}
	
	@Override
	protected void initView()
	{
	 
		String stopName=getIntent().getStringExtra("stopName");
		lineName.setText(stopName);
		title.setText("线路列表");
		editUser.setText("撤销");
		site.setText("选择线路");
		initCar();
	}
	
	public void initCar()
	{
		String lines=getIntent().getStringExtra("lines");
		String lineIdStr=getIntent().getStringExtra("lineIds");
		
		String[] lineNames=lines.split(",");
		String[] lineIds=lineIdStr.split(",");
		int count=0;
		layout.removeAllViews();
    	LinearLayout layout2 = null;
		for(int i=0;i<lineNames.length;i++)
		{
			String name=lineNames[i];
			String lineId=lineIds[i];
    		if(count==0)
    		{
    			LinearLayout.LayoutParams linearLayout1 = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
    			
    			layout2=new LinearLayout(this);
    			layout2.setLayoutParams(linearLayout1);
        		layout2.setOrientation(LinearLayout.HORIZONTAL);
    			layout.addView(layout2);
    		}
    		count++;
    		if(count==3)
    		{
    			count=0;
    		}
    	 
    		Button btn1=new Button(this);  
            btn1.setText(name);   
            btn1.setTag(R.id.tag_one,name);
            btn1.setTag(R.id.tag_two, lineId);
            btn1.setBackgroundResource(R.drawable.bg);
            LinearLayout.LayoutParams linearLayout = new LinearLayout.LayoutParams(200, 150);
    		linearLayout.setMargins(20,20,20, 20);//设置边距
    		btn1.setLayoutParams(linearLayout);
    		layout2.addView(btn1);
    		
            btn1.setOnClickListener(new OnClickListener() 
            {
				@Override
				public void onClick(View arg0) 
				{
					// TODO Auto-generated method stub
					Intent intent = new Intent(LineDetailActivity.this, CardActivity.class);
					intent.putExtra("lineName",arg0.getTag(R.id.tag_one)+"");
					intent.putExtra("lineId",arg0.getTag(R.id.tag_two)+"");
					String catStr=new Gson().toJson(cars);
					intent.putExtra("car",catStr);
					startActivity(intent);
				}
			});
            if(count==3)
            {
            	layout.addView(layout2);
            }
    	}
	}
	Handler handler = new Handler() {  
	    public void handleMessage(Message msg) {  
	        // 要做的事情   
	        super.handleMessage(msg);  
	        RequestParams param = webInterface.query(cityId, stopId, stopName, lineIds);
			invokeWebServer(param, GET_LIST);

	    }  
	};  

	
	public class MyThread implements Runnable {  
	    @Override  
	    public void run() {  
	        // TODO Auto-generated method stub   
	        while (true) {  
	            try {  
	                Thread.sleep(5000);// 线程暂停10秒，单位毫秒   
	                Message message = new Message();  
	                message.what = 1;  
	                handler.sendMessage(message);// 发送消息   
	            } catch (InterruptedException e) {  
	                // TODO Auto-generated catch block   
	                e.printStackTrace();  
	            }  
	        }  
	    }  
	} 
	
	@Override
	protected void initValue()
	{
		// TODO Auto-generated method stub
		dialog.setMessage("正在加载,请稍后...");
		dialog.show();
		cityId=SiteUtil.getCity();
		stopId=getIntent().getStringExtra("stopId");
		stopName=getIntent().getStringExtra("stopName");
		lineIds = getIntent().getStringExtra("lineIds");
		if(lineIds!=null && lineIds.length()>0)
		{
			lineIds=lineIds.substring(0, lineIds.length()-1);
		}
		RequestParams param = webInterface.query(cityId, stopId, stopName, lineIds);
		invokeWebServer(param, GET_LIST);
//		new Thread(new MyThread()).start();  
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
		httpHandler = mHttpUtils.send(HttpMethod.POST, Constant.URL_query, param, requestCallBack);
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

			SiteUtil.infoAlert(LineDetailActivity.this, "信息加载失败，请检查网络后重试");
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
		JsonObject jsonr = jsonObject.getAsJsonObject("data");
		JsonArray cars =  jsonr.getAsJsonArray("cars");
		Gson gson = new Gson();
		this.cars = gson.fromJson(cars, new TypeToken<List<Car>>()
		{
		}.getType());
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		// TODO Auto-generated method stub
//		Intent intent = new Intent(LinesActivity.this, NewsActivity.class);
//		City city =cities.get(position); 
//		Bundle bundle = new Bundle();
//		bundle.putSerializable("city", city);
//		intent.putExtras(bundle);
//		startActivity(intent);
		ImageView imageView =(ImageView) view.findViewById(R.id.choose);
		imageView.setVisibility(View.VISIBLE);
	}
}
