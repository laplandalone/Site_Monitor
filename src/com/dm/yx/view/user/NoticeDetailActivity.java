package com.dm.yx.view.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dm.yx.BaseActivity;
import com.dm.yx.MainPageActivity;
import com.dm.yx.R;
import com.dm.yx.model.HospitalNewsT;
import com.dm.yx.model.WakeT;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class NoticeDetailActivity extends BaseActivity
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
	
    private WakeT wakeT ;
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.article);
		ViewUtils.inject(this);
		addActivity(this);
		
		initView();
		initValue();
	}

	@OnClick(R.id.back)
	public void toHome(View v)
	{
		Intent intent = new Intent(NoticeDetailActivity.this, MainPageActivity.class);
		startActivity(intent);
		exit();
	}
	
	@Override
	protected void initView()
	{
		// TODO Auto-generated method stub
	
	}

	@Override
	protected void initValue()
	{
		// TODO Auto-generated method stub
		title.setText("我的消息");
		this.wakeT=(WakeT) getIntent().getSerializableExtra("wakeT");
		newsTitle.setText(wakeT.getWakeName());
		newsContent.setText(wakeT.getWakeContent());
		createDate.setText(wakeT.getCreateDate());
//		String newsImgs=hospitalNewsT.getNewsImages();
//		 if(newsImgs.endsWith("jpg") || newsImgs.endsWith("png"))
//		 {
//			 bitmapUtils.display(imageView,newsImgs);
//		 }
	}

}
