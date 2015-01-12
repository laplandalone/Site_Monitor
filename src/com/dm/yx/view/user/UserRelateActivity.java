package com.dm.yx.view.user;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.dm.yx.BaseActivity;
import com.dm.yx.MainPageActivity;
import com.dm.yx.R;
import com.dm.yx.adapter.UsersListAdapter;
import com.dm.yx.model.User;
import com.dm.yx.tools.HealthConstant;
import com.dm.yx.tools.HealthUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class UserRelateActivity extends BaseActivity
{
	@ViewInject(R.id.title)
	private TextView title;
	
	@ViewInject(R.id.userName)
	private EditText userName;

	@ViewInject(R.id.password)
	private EditText password;
	private ListView userslist;
    private String userId;
    private UsersListAdapter userAdapter; 
    private List<User> users =new ArrayList<User>();
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_relate);
		this.userslist=(ListView) findViewById(R.id.userslist);
		ViewUtils.inject(this);
		addActivity(this);
		initView();
		initValue();
	}

	@OnClick(R.id.back)
	public void toHome(View v)
	{
		Intent intent = new Intent(UserRelateActivity.this, MainPageActivity.class);
		startActivity(intent);
		exit();
	}

	@Override
	protected void initView()
	{
		// TODO Auto-generated method stub
		title.setText("用户关联");

	}

	@Override
	protected void initValue()
	{
		User user = HealthUtil.getUserInfo();
		userAdapter = new UsersListAdapter(UserRelateActivity.this,user.getTelephone(),false);
		this.userslist.setAdapter(userAdapter);
		userslist.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) 
			{
				User user = HealthUtil.getUserInfo();
				List<User> usersT=HealthUtil.readChooseUsers();
				if(usersT!=null)
				{
					for(User u:usersT)
					{
						if(u.getTelephone().equals(user.getTelephone()))
						{
							continue;
						}
						users.add(u);
					}
				}
				User s =users.get(position);
				
				dialog.setMessage("关联中,请稍后...");
				dialog.show();
				
				RequestParams param = webInterface.addUserRelate(user.getUserId(),user.getTelephone(),user.getUserName(),user.getPassword(), s.getTelephone(), s.getPassword());
				invokeWebServer(param, ADD);
			}
			
		});
	}

	@OnClick(R.id.addRelate)
	public void addRelate(View v)
	{
	
	}
	/**
	 * 链接web服务
	 * 
	 * @param param
	 */
	private void invokeWebServer(RequestParams param, int responseCode)
	{
		HealthUtil.LOG_D(getClass(), "connect to web server");
		MineRequestCallBack requestCallBack = new MineRequestCallBack(responseCode);
		if (httpHandler != null)
		{
			httpHandler.cancel();
		}
		httpHandler = mHttpUtils.send(HttpMethod.POST, HealthConstant.URL, param, requestCallBack);
	}

	/**
	 * 获取后台返回的数据
	 */
	class MineRequestCallBack extends RequestCallBack<String>
	{

		private int responseCode;

		public MineRequestCallBack(int responseCode)
		{
			super();
			this.responseCode = responseCode;
		}

		@Override
		public void onFailure(HttpException error, String msg)
		{
			HealthUtil.LOG_D(getClass(), "onFailure-->msg=" + msg);
			if (dialog.isShowing())
			{
				dialog.cancel();
			}

			HealthUtil.infoAlert(UserRelateActivity.this, "信息加载失败，请检查网络后重试");
		}

		@Override
		public void onSuccess(ResponseInfo<String> arg0)
		{
			// TODO Auto-generated method stub
			HealthUtil.LOG_D(getClass(), "result=" + arg0.result);
			if (dialog.isShowing())
			{
				dialog.cancel();
			}
			switch (responseCode)
			{
			case ADD:
				returnMsg(arg0.result, ADD);
				break;
			}
		}

	}

	/*
	 * 处理返回结果数据
	 */
	private void returnMsg(String json, int responseCode)
	{
		try
		{
			JSONObject jsonObject = new JSONObject(json);
			String executeType = jsonObject.getString("executeType");
			String returnJson = jsonObject.getString("returnMsg").toString();
			if("success".equals(executeType))
			{
				if("1".equals(returnJson))
				{
					HealthUtil.infoAlert(UserRelateActivity.this, "关联账号已存在");
				}else if("0".equals(returnJson))
				{
					HealthUtil.infoAlert(UserRelateActivity.this, "关联账号不存在");
				}else if("2".equals(returnJson))
				{
					HealthUtil.infoAlert(UserRelateActivity.this, "关联账号成功");
					finish();
				}
			}else
			{
				HealthUtil.infoAlert(UserRelateActivity.this, "处理失败，请重试...");
			}
			
			
		} catch (Exception e)
		{
			HealthUtil.infoAlert(UserRelateActivity.this, "处理失败，请重试...");
		}

	}

	private void showSuccessDialog()
	{
		AlertDialog alertDialog = new AlertDialog.Builder(this)
				.setPositiveButton("确定", new OnClickListener()
				{

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						// TODO Auto-generated method stub

					}
				}).setTitle("提示").setMessage("密码已重置请查收").create();
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.show();
	}
}
