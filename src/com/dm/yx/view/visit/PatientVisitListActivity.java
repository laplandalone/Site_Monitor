package com.dm.yx.view.visit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dm.yx.BaseActivity;
import com.dm.yx.MainPageActivity;
import com.dm.yx.R;
import com.dm.yx.tools.HealthUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class PatientVisitListActivity extends BaseActivity
{
	
	@ViewInject(R.id.title)
	private TextView title;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.visit);
		ViewUtils.inject(this);
		addActivity(this);
		initView();
		initValue();
	}

	@OnClick(R.id.back)
	public void toBack(View v)
	{
		Intent intent = new Intent(PatientVisitListActivity.this, MainPageActivity.class);
		startActivity(intent);
		exit();
	}
	
	@OnClick(R.id.submit)
	public void health1(View v)
	{
		HealthUtil.infoAlert(PatientVisitListActivity.this, "正在建设中...");
//		Intent intent = new Intent(UserCheckActivity.this,MyHealthActivity.class);
//		startActivity(intent);
//		exit();
	}
	
	@OnClick(R.id.asd)
	public void asd(View v)
	{
//		Intent intent = new Intent(PatientVisitListActivity.this,VisitDetailActivity.class);
//		intent.putExtra("url", "http://hiseemedical.com:10821/visit/asd.html");
//		intent.putExtra("title", "心先手术随访");
//		startActivity(intent);
		
		Uri uri = Uri.parse("http://hiseemedical.com:10821/visit/asd.html");    
		Intent it = new Intent(Intent.ACTION_VIEW, uri);    
//		1it.setClassName("com.tencent.mtt","com.tencent.mtt.SplashActivity");
		startActivity(it);
		
	}
	
	@OnClick(R.id.mvr)
	public void mvr(View v)
	{
//		Intent intent = new Intent(PatientVisitListActivity.this,VisitDetailActivity.class);
//		intent.putExtra("url", "http://hiseemedical.com:10821/visit/mvr.html");
//		intent.putExtra("title", "房颤手术随访");
//		startActivity(intent);
		Uri uri = Uri.parse("http://hiseemedical.com:10821/visit/mvr.html");    
		Intent it = new Intent(Intent.ACTION_VIEW, uri);    
		startActivity(it);
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
		title.setText("随访");
	}

}
