package com.dm.yx.view.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.dm.yx.BaseActivity;
import com.dm.yx.MainPageActivity;
import com.dm.yx.R;
import com.dm.yx.model.User;
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

public class WebActivity extends BaseActivity
{
	
	@ViewInject(R.id.title)
	private TextView title;
	WebView web;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.visit_detail_webview);
		web = (WebView) findViewById(R.id.webview);  
		web.getSettings().setJavaScriptEnabled(true);   
		ViewUtils.inject(this);
		addActivity(this);
		initView();
		initValue();
	}

	@OnClick(R.id.back)
	public void toBack(View v)
	{
		Intent intent = new Intent(WebActivity.this, MainPageActivity.class);
		startActivity(intent);
		exit();
	}
	
 
	
	@Override
	protected void initView()
	{
		String titleT = getIntent().getStringExtra("title");
		// TODO Auto-generated method stub
		title.setText(titleT);
		String url = getIntent().getStringExtra("url");
	
		 if(web != null) 
	        { 
//			    web.addJavascriptInterface(this, "javatojs");

	            web.setWebViewClient(new WebViewClient() 
	            { 
	            	@Override
	                public boolean shouldOverrideUrlLoading(WebView view, String url) { 
	                    view.loadUrl(url); 
	                    return true; 
	                } 
	            	
	            	@Override
	            	public void onPageStarted(WebView view, String url,
	            			Bitmap favicon) {
	            		super.onPageStarted(view, url, favicon);
	            	}
	                @Override 
	                public void onPageFinished(WebView view,String url) 
	                { 
	                	
	                }

					@Override
					public void onReceivedError(WebView view, int errorCode,
							String description, String failingUrl) 
					{
						HealthUtil.infoAlert(WebActivity.this, "加载失败,请重试...");
						web.setVisibility(View.GONE);
						super.onReceivedError(view, errorCode, description, failingUrl);
					} 
	                
	            }); 
	             
	            loadUrl(url); 
	        } 
	}

	  public void loadUrl(String url) 
	    { 
	        if(web != null) 
	        { 
	            web.loadUrl(url); 
	          
	        } 
	    } 

	@Override
	protected void initValue()
	{
		
	}
}
