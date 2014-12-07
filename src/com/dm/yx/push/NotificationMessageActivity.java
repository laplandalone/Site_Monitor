package com.dm.yx.push;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dm.yx.BaseActivity;
import com.dm.yx.MainPageActivity;
import com.dm.yx.R;
import com.dm.yx.view.user.UserMainActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class NotificationMessageActivity extends BaseActivity {
	private TextView titleTV;
	private TextView messageTV;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notification_message);
		ViewUtils.inject(this);
		addActivity(this);
		titleTV = (TextView) findViewById(R.id.title);
		messageTV = (TextView) findViewById(R.id.notification_message);
		
		String title = getIntent().getStringExtra("title");
		String message = getIntent().getStringExtra("message");
		
		titleTV.setText(title);
		messageTV.setText(message);
	}

	@OnClick(R.id.back)
	public void toHome(View v)
	{
		Intent intent = new Intent(NotificationMessageActivity.this, MainPageActivity.class);
		startActivity(intent);
		exit();
	}
	
	
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initValue() {
		// TODO Auto-generated method stub
		
	}
}
