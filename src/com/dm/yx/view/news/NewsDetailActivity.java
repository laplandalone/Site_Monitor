package com.dm.yx.view.news;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dm.yx.BaseActivity;
import com.dm.yx.MainPageActivity;
import com.dm.yx.R;
import com.dm.yx.model.HospitalNewsT;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class NewsDetailActivity extends BaseActivity
{
	@ViewInject(R.id.news_photo)
	private ImageView imageView;
	
    @ViewInject(R.id.newsTitle)
	private TextView newsTitle;
	
    @ViewInject(R.id.newsContent)
	private TextView newsContent;
	
    @ViewInject(R.id.newsDate)
	private TextView createDate;
	
	@ViewInject(R.id.title)
	private TextView title;
	
	@ViewInject(R.id.urlText)
	private TextView url_text;
	
    private HospitalNewsT hospitalNewsT;
    
    private BitmapUtils bitmapUtils;
    
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.article);
		ViewUtils.inject(this);
		addActivity(this);
		
		bitmapUtils = new BitmapUtils(this);
		bitmapUtils.closeCache();
		initView();
		initValue();
	}

	@OnClick(R.id.back)
	public void toHome(View v)
	{
		Intent intent = new Intent(NewsDetailActivity.this, MainPageActivity.class);
		startActivity(intent);
		exit();
	}
	
	@Override
	protected void initView()
	{
		// TODO Auto-generated method stub
		String typeName=getIntent().getStringExtra("typeName");
		title.setText(typeName);
	}

	@Override
	protected void initValue()
	{
		// TODO Auto-generated method stub
		this.hospitalNewsT=(HospitalNewsT) getIntent().getSerializableExtra("hospitalNewsT");
		newsTitle.setText(hospitalNewsT.getNewsTitle());
		newsContent.setText(hospitalNewsT.getNewsContent());
		createDate.setText(hospitalNewsT.getCreateDate());
		String newsImgs=hospitalNewsT.getNewsImages();
		
		String url=hospitalNewsT.getUrl();
		String urlText=hospitalNewsT.getUrlText();
		if(url!=null && !"".equals(url) && urlText!=null && !"".equals(urlText))
		{
			String urlT="<a href=\""+url+"\">"+urlText+"</a> ";
			url_text.setText(Html.fromHtml(urlT));
			url_text.setMovementMethod(LinkMovementMethod.getInstance());
		}
		
		if(newsImgs.endsWith("jpg") || newsImgs.endsWith("png"))
		 {
			 bitmapUtils.display(imageView,newsImgs);
		 }
	}

}
