package com.dm.yx.view.user;

import android.content.Intent;
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

public class UserCheckActivity extends BaseActivity
{
	
	@ViewInject(R.id.title)
	private TextView title;
	
	@ViewInject(R.id.name)
	private TextView name;
	
	@ViewInject(R.id.idcard)
	private TextView idcard;
	
	@ViewInject(R.id.userCard)
	private TextView userCard;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_info_check);
		ViewUtils.inject(this);
		addActivity(this);
		initView();
		initValue();
	}

	@OnClick(R.id.back)
	public void toBack(View v)
	{
		Intent intent = new Intent(UserCheckActivity.this, MainPageActivity.class);
		startActivity(intent);
		exit();
	}
	
	@OnClick(R.id.submit)
	public void health1(View v)
	{
		HealthUtil.infoAlert(UserCheckActivity.this, "正在建设中...");
//		Intent intent = new Intent(UserCheckActivity.this,MyHealthActivity.class);
//		startActivity(intent);
//		exit();
	}
	
	@Override
	protected void initView()
	{
		// TODO Auto-generated method stub
		name.setOnFocusChangeListener(onFocusAutoClearHintListener);
		idcard.setOnFocusChangeListener(onFocusAutoClearHintListener);
		userCard.setOnFocusChangeListener(onFocusAutoClearHintListener);
	}
	

	@Override
	protected void initValue()
	{
		// TODO Auto-generated method stub
		title.setText("就诊档案");
	}

}
