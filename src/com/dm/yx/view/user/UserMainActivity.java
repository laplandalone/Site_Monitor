package com.dm.yx.view.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dm.yx.BaseActivity;
import com.dm.yx.MainPageActivity;
import com.dm.yx.R;
import com.dm.yx.model.User;
import com.dm.yx.model.UserContactT;
import com.dm.yx.tools.HealthUtil;
import com.dm.yx.view.expert.QuestionActivity;
import com.dm.yx.view.order.UserOrderActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class UserMainActivity extends BaseActivity
{
	@ViewInject(R.id.title)
	private TextView title;
	
	@ViewInject(R.id.item_layout1)
	private LinearLayout itemLayout1;

	@ViewInject(R.id.item_layout2)
	private LinearLayout itemLayout2;

	@ViewInject(R.id.login_name)
	private TextView loginNameTV;

	@ViewInject(R.id.photo)
	private ImageView photo;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub 2131493633
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_login_main);
		ViewUtils.inject(this);
		addActivity(this);
		initView();
		initValue();
	}

	@OnClick(R.id.health_data_lay)
	public void toMyHealth(View v)
	{
		Intent intent = new Intent(UserMainActivity.this, MyHealthActivity.class);
		startActivity(intent);
	}

	@OnClick(R.id.item_layout1)
	public void toMyOrder(View v)
	{
		Intent intent = new Intent(UserMainActivity.this, UserOrderActivity.class);
		startActivity(intent);
	}

	@OnClick(R.id.item_layout2)
	public void toMyQuestion(View v)
	{
		Intent intent = new Intent(UserMainActivity.this, QuestionActivity.class);
		intent.putExtra("questionType", "user");
		startActivity(intent);
	}

	@OnClick(R.id.item_layout3)
	public void contact(View v)
	{
		Intent intent = new Intent(UserMainActivity.this, ContactListActivity.class);
		startActivity(intent);
	}
	
	@OnClick(R.id.item_layout4)
	public void shake(View v)
	{
		Intent intent = new Intent(UserMainActivity.this, ShakeReward.class);
		startActivity(intent);
	}
	
	@OnClick(R.id.item_layout5)
	public void relate(View v)
	{
		Intent intent = new Intent(UserMainActivity.this, UserAccountActivity.class);
		startActivity(intent);
	}
	
	@OnClick(R.id.item_layout7)
	public void favorable(View v)
	{
		Intent intent = new Intent(UserMainActivity.this,  PrivilegeListActivity.class);
		startActivity(intent);
	}
	
	
	@OnClick(R.id.user_info_detail)
	public void updateUser(View v)
	{
		Intent intent = new Intent(UserMainActivity.this, UserUpdateActivity.class);
		startActivityForResult(intent, 0);
	}

	@OnClick(R.id.back)
	public void toHome(View v)
	{
		Intent intent = new Intent(UserMainActivity.this, MainPageActivity.class);
		startActivity(intent);
		exit();
	}

	@OnClick(R.id.outLogin)
	public void loginOut(View v)
	{
		HealthUtil.writeUserInfo("");
		HealthUtil.writeUserId("");
		HealthUtil.writeLoginAuto("");
		HealthUtil.writeHospitalTs("");
		Intent intent = new Intent(UserMainActivity.this, MainPageActivity.class);
		startActivity(intent);
		exit();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, intent);
		switch (resultCode)
		{
		case 0:
			User user = HealthUtil.getUserInfo();
			if (user != null && !"".equals(user))
			{
				loginNameTV.setText(user.getUserName());
				if("男".equals(user.getSex()))
				{
					photo.setBackgroundResource(R.drawable.male);
				}else
				{
					photo.setBackgroundResource(R.drawable.female);
				}
			}
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void initView()
	{
		title.setText("个人中心");
		// TODO Auto-generated method stub
		User user = HealthUtil.getUserInfo();
		loginNameTV.setText(user.getUserName());
		if("男".equals(user.getSex()))
		{
			photo.setBackgroundResource(R.drawable.male);
		}else
		{
			photo.setBackgroundResource(R.drawable.female);
		}
	}

	@Override
	protected void initValue()
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initView();
	}
}
