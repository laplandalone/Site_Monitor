package com.dm.yx.view.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.lidroid.xutils.http.RequestParams;
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
				name.setText(user.getUserName());
				idcard.setText(user.getUserNo());
			}else
			{
				finish();
			}
			break;
		case 1:
			this.user = HealthUtil.getUserInfo();
			String nameT=user.getUserName();
			String no=user.getUserNo();
			if (this.user != null && nameT!=null && !"".equals(nameT) && no!=null && !"".equals(no))
			{
				name.setText(nameT);
				idcard.setText(no);
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
			String nameT=user.getUserName();
			String no=user.getUserNo();
			if (this.user != null && nameT!=null && !"".equals(nameT) && no!=null && !"".equals(no))
			{
				name.setText(nameT);
				idcard.setText(no);
			}else
			{
				checkUser();
			}
		}
	}

	private void checkUser()
	{
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle("提示");  
		alertDialog.setMessage("姓名和身份证没有完成填写，是否需要到个人中心填写？");  
		alertDialog.setPositiveButton("取消",  
	                new DialogInterface.OnClickListener() {  
	                    public void onClick(DialogInterface dialog, int whichButton) 
	                    {  
	                    	finish();
	                    }  
	                });  

		  
		alertDialog.setNeutralButton("确定",  
	                new DialogInterface.OnClickListener() {  
	                    public void onClick(DialogInterface dialog, int whichButton) 
	                    {  
	                    	Intent intent = new Intent(UserCheckActivity.this, UserUpdateActivity.class);
	            			startActivityForResult(intent,1); 
	                    }  
	                });  
		alertDialog.show();  
	}
}
