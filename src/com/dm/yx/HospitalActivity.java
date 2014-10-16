package com.dm.yx;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class HospitalActivity extends BaseActivity
{

	@ViewInject(R.id.hospital)
	private ImageView hospital;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		ViewUtils.inject(this);
		addActivity(this);
	}

	@OnClick(R.id.hospital)
	public void toMain(View v)
	{
		Intent intent = new Intent(HospitalActivity.this,MainPageActivity.class);
		startActivity(intent);
		finish();
	}
	
	@OnClick(R.id.phone114)
	public void toPhone(View v)
	{
		
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

	}
}
