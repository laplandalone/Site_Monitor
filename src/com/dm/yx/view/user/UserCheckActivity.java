package com.dm.yx.view.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dm.yx.BaseActivity;
import com.dm.yx.MainPageActivity;
import com.dm.yx.R;
import com.dm.yx.model.User;
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
	private User user;
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
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, intent);
		switch (requestCode)
		{
		case 0:
			this.user = HealthUtil.getUserInfo();
			if (this.user != null)
			{
//				userId=user.getUserId();
			}else
			{
				finish();
			}
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void initValue()
	{
		// TODO Auto-generated method stub
		title.setText("就诊档案");
		user=HealthUtil.getUserInfo();
		if (this.user == null)
		{
			Intent intent = new Intent(UserCheckActivity.this, LoginActivity.class);
			startActivityForResult(intent, 0);
		}else
		{
//			userId=user.getUserId();
		}
	}

}
