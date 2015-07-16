 package com.site.view;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dm.yx.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.site.BaseActivity;

public class CardActivity extends BaseActivity
{
	@ViewInject(R.id.title)
	private TextView title;
	
	@ViewInject(R.id.sign_in)
	private ImageButton loginBtn;
	
	@ViewInject(R.id.userName)
	private EditText userName;

	@ViewInject(R.id.password)
	private EditText password;
 
	@ViewInject(R.id.site)
	private TextView site;
	
	
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
		String line = getIntent().getStringExtra("line");
		System.out.println(line);
		 
	}

	@OnClick(R.id.sign_in)
	public void userLogin(View v)
	{
	 
	}

	
}
