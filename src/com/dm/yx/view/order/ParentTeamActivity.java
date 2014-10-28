package com.dm.yx.view.order;

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
import com.dm.yx.tools.HealthUtil;
import com.dm.yx.view.expert.QuestionActivity;
import com.dm.yx.view.order.UserOrderActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class ParentTeamActivity extends BaseActivity
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
		setContentView(R.layout.parent_team);
		ViewUtils.inject(this);
		addActivity(this);
		initView();
		initValue();
	}

	@OnClick(R.id.team1)
	public void team1(View v)
	{
		Intent intent = new Intent(ParentTeamActivity.this, FacultyExpertListActivity.class);
		intent.putExtra("orderType", "expert");
		intent.putExtra("parentId", "1");
		startActivity(intent);
	}

	@OnClick(R.id.team2)
	public void team2(View v)
	{
		Intent intent = new Intent(ParentTeamActivity.this, FacultyExpertListActivity.class);
		intent.putExtra("orderType", "expert");
		intent.putExtra("parentId", "2");
		startActivity(intent);
	}

	@OnClick(R.id.team3)
	public void team3(View v)
	{
		Intent intent = new Intent(ParentTeamActivity.this, FacultyExpertListActivity.class);
		intent.putExtra("orderType", "expert");
		intent.putExtra("parentId", "3");
		startActivity(intent);
	}

	@OnClick(R.id.team4)
	public void team4(View v)
	{
		Intent intent = new Intent(ParentTeamActivity.this, FacultyExpertListActivity.class);
		intent.putExtra("orderType", "expert");
		intent.putExtra("parentId", "4");
		startActivity(intent);
	}

	@OnClick(R.id.back)
	public void toHome(View v)
	{
		Intent intent = new Intent(ParentTeamActivity.this, MainPageActivity.class);
		startActivity(intent);
		exit();
	}

	@Override
	protected void initView()
	{
		title.setText("门诊分类");
		// TODO Auto-generated method stub
	}

	@Override
	protected void initValue()
	{
		// TODO Auto-generated method stub

	}

}
