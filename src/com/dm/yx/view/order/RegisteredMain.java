package com.dm.yx.view.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dm.yx.BaseActivity;
import com.dm.yx.MainPageActivity;
import com.dm.yx.R;
import com.dm.yx.tools.HealthUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 手机挂号
 *
 */
public class RegisteredMain extends BaseActivity
{

	@ViewInject(R.id.expert)
	private Button expertBtn;
	@ViewInject(R.id.normal)
	private Button normalBtn;
	
	@ViewInject(R.id.title)
	private TextView title;
	
	@ViewInject(R.id.open_memo)
	private TextView open_memo;
	
	@ViewInject(R.id.regist_memo)
	private TextView regist_memo;
	
	@ViewInject(R.id.regist_memo3)
	private TextView regist_memo3;
	
	@ViewInject(R.id.regist_memo4)
	private TextView regist_memo4;
	
	@ViewInject(R.id.regist_memo5)
	private TextView regist_memo5;
	
	@ViewInject(R.id.regist_memo6)
	private TextView regist_memo6;
	
	
	
	@ViewInject(R.id.regist_memo8)
	private TextView regist_memo8;
	
	@ViewInject(R.id.regist_memo9)
	private TextView regist_memo9;
	
	@ViewInject(R.id.regist_memo10)
	private TextView regist_memo10;
	
	@ViewInject(R.id.regist_memo13)
	private TextView regist_memo13;
	
	@ViewInject(R.id.regist_memo14)
	private TextView regist_memo14;
	
	@ViewInject(R.id.regist_memo15)
	private TextView regist_memo15;
	
	@ViewInject(R.id.regist_memo16)
	private TextView regist_memo16;
	
	@ViewInject(R.id.regist_memo17)
	private TextView regist_memo17;
	
	@ViewInject(R.id.regist_memo18)
	private TextView regist_memo18;
	
	@ViewInject(R.id.regist_memo19)
	private TextView regist_memo19;
	
	@ViewInject(R.id.regist_memo20)
	private TextView regist_memo20;
	
	@ViewInject(R.id.regist_memo21)
	private TextView regist_memo21;
	
	@ViewInject(R.id.ready)
	private ImageButton read;
	
	private Boolean readFlag=true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guahao_mian);
		ViewUtils.inject(this);
		addActivity(this);
		initView();
	}
	
	@OnClick(R.id.open)
	public void openInfo(View v)
	{
		if(regist_memo5.getVisibility()==8)
		{
			regist_memo5.setVisibility(View.VISIBLE);
			regist_memo6.setVisibility(View.VISIBLE);
			regist_memo8.setVisibility(View.VISIBLE);
			regist_memo9.setVisibility(View.VISIBLE);
			regist_memo10.setVisibility(View.VISIBLE);
			regist_memo13.setVisibility(View.VISIBLE);
			regist_memo14.setVisibility(View.VISIBLE);
			regist_memo15.setVisibility(View.VISIBLE);
			regist_memo16.setVisibility(View.VISIBLE);
			regist_memo17.setVisibility(View.VISIBLE);
			regist_memo18.setVisibility(View.VISIBLE);
			regist_memo19.setVisibility(View.VISIBLE);
			regist_memo20.setVisibility(View.VISIBLE);
			regist_memo21.setVisibility(View.VISIBLE);
			open_memo.setText("收起");
		}else
		{
			regist_memo5.setVisibility(View.GONE);
			regist_memo6.setVisibility(View.GONE);
			regist_memo8.setVisibility(View.GONE);
			regist_memo9.setVisibility(View.GONE);
			regist_memo10.setVisibility(View.GONE);
			regist_memo13.setVisibility(View.GONE);
			regist_memo14.setVisibility(View.GONE);
			regist_memo15.setVisibility(View.GONE);
			regist_memo16.setVisibility(View.GONE);
			regist_memo17.setVisibility(View.GONE);
			regist_memo18.setVisibility(View.GONE);
			regist_memo19.setVisibility(View.GONE);
			regist_memo20.setVisibility(View.GONE);
			regist_memo21.setVisibility(View.GONE);
			open_memo.setText("展开");
		}
		
		
	}
	
	@OnClick(R.id.ready)
	public void read(View v)
	{
		if(readFlag)
		{
			readFlag=false;
			read.setBackgroundResource(R.drawable.symptom_select_false);
		}else
		{
			readFlag=true;
			read.setBackgroundResource(R.drawable.symptom_select_true);
		}
		
	}
	
	@OnClick(R.id.expert)
	public void expertOrder(View v)
	{
		if(!readFlag)
		{
			HealthUtil.infoAlert(RegisteredMain.this, "请先阅读并同意声明");
			return;
		}
		Intent intent = new Intent(RegisteredMain.this,FacultyExpertListActivity.class);
		intent.putExtra("orderType", "expert");
		startActivity(intent);
		finish();
	}

	@OnClick(R.id.back)
	public void toHome(View v)
	{
		Intent intent = new Intent(RegisteredMain.this,MainPageActivity.class);
		startActivity(intent);
		exit();
	}
	
	@OnClick(R.id.normal)
	public void normalOrder(View v)
	{
		if(!readFlag)
		{
			HealthUtil.infoAlert(RegisteredMain.this, "请先阅读并同意声明");
			return;
		}
		Intent intent = new Intent(RegisteredMain.this,FacultyExpertListActivity.class);
		intent.putExtra("orderType", "normal");
		startActivity(intent);
	}
	
	@Override
	protected void initView()
	{
		// TODO Auto-generated method stub
		title.setText("预约挂号");
		if("102".equals(HealthUtil.readHospitalId()))
		{
			expertBtn.setBackgroundResource(R.drawable.guahao_btnx);
			normalBtn.setVisibility(View.GONE);
			regist_memo3.setText(R.string.layout_temp1961);
			regist_memo4.setText(R.string.layout_temp1971);
		}
		
		
	}

	@Override
	protected void initValue()
	{
		// TODO Auto-generated method stub

	}

	
}
