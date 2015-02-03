package com.dm.yx.view.visit;

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
import com.dm.yx.view.user.LoginActivity;
import com.dm.yx.view.user.UserMainActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class PatientMainActivity extends BaseActivity
{
	@ViewInject(R.id.title)
	private TextView title;
	
	@ViewInject(R.id.item_layout1)
	private LinearLayout itemLayout1;

	@ViewInject(R.id.item_layout2)
	private LinearLayout itemLayout2;

	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.patient_main);
		ViewUtils.inject(this);
		addActivity(this);
		initView();
		initValue();
	}


	@OnClick(R.id.item_layout1)
	public void toMyOrder(View v)
	{
		Intent intent = new Intent(PatientMainActivity.this, PatientVisitListActivity.class);
		startActivity(intent);
	}

	@OnClick(R.id.item_layout2)
	public void toMyQuestion(View v)
	{
		Intent intent = new Intent(PatientMainActivity.this,  VisitNoticeActivity.class);
		startActivity(intent);
	}

	@OnClick(R.id.back)
	public void toHome(View v)
	{
		Intent intent = new Intent(PatientMainActivity.this, MainPageActivity.class);
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
				
			}
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void initView()
	{
		title.setText("患者随访");
		// TODO Auto-generated method stub
		User user = HealthUtil.getUserInfo();
		if (user == null)
		{
			Intent intent = new Intent(PatientMainActivity.this, LoginActivity.class);
			startActivityForResult(intent, 0);
		}else if( user.getCardNo()==null || "".equals(user.getCardNo()))
		{
			HealthUtil.infoAlert(PatientMainActivity.this, "姓名或病案号校验失败，请到个人中心完善...");
			finish();
		}
	}

	@Override
	protected void initValue()
	{
		// TODO Auto-generated method stub

	}
}
