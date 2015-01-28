package com.dm.yx.view.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dm.yx.BaseActivity;
import com.dm.yx.MainPageActivity;
import com.dm.yx.R;
import com.dm.yx.model.User;
import com.dm.yx.model.WakeT;
import com.dm.yx.tools.HealthUtil;
import com.dm.yx.view.visit.PatientVisitListActivity;
import com.dm.yx.view.visit.VisitDetailActivity;
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
	
	@ViewInject(R.id.visit_detail)
	private TextView visitDetail;
	
	
	
    private WakeT wakeT ;
    
    private User user;
    private String type;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.visit_rst_article);
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
		wakeT=(WakeT) getIntent().getSerializableExtra("wakeT");
		user=HealthUtil.getUserInfo();
		type=wakeT.getWakeType();
		if("visit_plan".equals(type))
		{
			visitDetail.setText("填写随访");
		} 
		
	}

	@OnClick(R.id.visit_detail)
	public void getVisitDetail(View v)
	{
		if("visit_result".equals(type))
		{
			Intent intent = new Intent(NoticeDetailActivity.this,VisitDetailActivity.class);
			String patientId="无";
			String operType="无";
			String userName=user.getUserName();
			String visitId=wakeT.getWakeValue();
	//	 	String url="http://192.168.137.1:7001/visit/visit.jsp?visitId="+visitId+"&copyFlag=&name="+userName+"&patientId="+patientId+"&operType="+operType;
		 	String url="http://123.57.78.38:10841/visit/visit.jsp?visitId="+visitId+"&copyFlag=&name="+userName+"&patientId="+patientId+"&operType="+operType;
			intent.putExtra("url", url);
			intent.putExtra("visitId",visitId);
			intent.putExtra("display","N");
			intent.putExtra("userId",wakeT.getUserId());
			intent.putExtra("title", wakeT.getWakeName());
			startActivity(intent);
		}else if("visit_plan".equals(type))
		{
			Intent intent = new Intent(NoticeDetailActivity.this,PatientVisitListActivity.class);
			startActivity(intent);
		}
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
