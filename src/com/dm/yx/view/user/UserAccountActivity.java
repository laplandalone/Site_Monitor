package com.dm.yx.view.user;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dm.yx.BaseActivity;
import com.dm.yx.MainPageActivity;
import com.dm.yx.R;
import com.dm.yx.adapter.RelateListAdapter;
import com.dm.yx.adapter.UsersListAdapter;
import com.dm.yx.model.User;
import com.dm.yx.model.UserContactT;
import com.dm.yx.model.UserRelateT;
import com.dm.yx.tools.HealthConstant;
import com.dm.yx.tools.HealthUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lurencun.android.utils.ParamUtil;

public class UserAccountActivity extends BaseActivity  
{
	@ViewInject(R.id.title)
	private TextView title;
	
	@ViewInject(R.id.editUser)
	private TextView edit;
	
	@ViewInject(R.id.contentnull)
	private RelativeLayout layout;
	
	private User user;
	private ListView list;
	private ListView userslist;
	private List<UserRelateT> userRelateTs;
	private UserRelateT relateT;
	private RelateListAdapter adapter;
	private UsersListAdapter userAdapter; 
	private Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.relate_list);
		this.list=(ListView) findViewById(R.id.comlist);
		this.userslist=(ListView) findViewById(R.id.userslist);
		ViewUtils.inject(this);
		addActivity(this);
		initView();
		initValue();
		mContext=this;
	}

	@Override
	protected void initView()
	{
		// TODO Auto-generated method stub
		title.setText("帐号管理");
		edit.setVisibility(View.VISIBLE);
		
		userslist.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) 
			{
				List<User> users = HealthUtil.readChooseUsers();
				User s =users.get(position);
				dialog.setMessage("正在登录,请稍后...");
				dialog.show();
				RequestParams param = webInterface.queryUser( s.getTelephone(), s.getPassword(),HealthUtil.readHospitalId());
				invokeWebServer(param, CHOOSE_USER_LOGIN);
			}
			
		});
		

		list.setOnItemClickListener(new OnItemClickListener()
		{
			 @Override  
			 public void onItemClick(AdapterView<?> parent, View view, int position,long id)  
			 {
				 relateT  = userRelateTs.get(position);
					AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
					builder.setTitle(R.string.checkUpdate_title);
					builder.setMessage("是否解除关联用户？");
					builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							RequestParams param = webInterface.deleteUserRelate(relateT.getRelateId(),user.getTelephone());
							invokeWebServer(param, DELETE);
						}
					});
					
					builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
							

							}
						});
					
					Dialog dialog = builder.create();
					
					dialog.setCanceledOnTouchOutside(false);
					dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
						
						@Override
						public void onCancel(DialogInterface dialog) {
							
						}
					});
					dialog.show();
	            }  

		});
	}


	@OnClick(R.id.editUser)
	public void toEdit(View v)
	{
		String editT= edit.getText().toString();
		if(editT.equals("编辑"))
		{
			userAdapter.setDelete(true);
			userAdapter.notifyDataSetChanged();
			edit.setText("完成");
		}else
		{
			userAdapter.setDelete(false);
			userAdapter.notifyDataSetChanged();
			edit.setText("编辑");
		}
		
		
	}
	
	@OnClick(R.id.back)
	public void toHome(View v)
	{
		Intent intent = new Intent(UserAccountActivity.this, MainPageActivity.class);
		startActivity(intent);
		exit();
	}
	
	@OnClick(R.id.addContact)
	public void addContact(View v)
	{
		Intent intent = new Intent(UserAccountActivity.this, UserRelateActivity.class);
		startActivity(intent);
	}
	
	@OnClick(R.id.addUser)
	public void addUser(View v)
	{
		Intent intent = new Intent(UserAccountActivity.this, LoginActivity.class);
		intent.putExtra("choose_user","true");
		startActivityForResult(intent, 0);
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
				HealthUtil.writeChooseUsers(user);
				userAdapter.notifyDataSetChanged();
			}
			break;
	
		default:
			break;
		}
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		initValue();
		super.onResume();
	}
	@Override
	protected void initValue()
	{
		// TODO Auto-generated method stub
		dialog.setMessage("正在加载,请稍后...");
		dialog.show();
		this.user=HealthUtil.getUserInfo();
		String userId=user.getUserId();
		
		RequestParams param = webInterface.getUserRelate(userId);
		invokeWebServer(param, GET_LIST);
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
			httpHandler.stop();
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
			if (list != null)
			{
				// list.stopLoadMore();
			}
			HealthUtil.infoAlert(UserAccountActivity.this, "信息加载失败，请检查网络后重试");
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
			case GET_LIST:
				returnMsg(arg0.result, GET_LIST);
				break;
			case DELETE:
				returnMsg(arg0.result, DELETE);
				break;
		
			case CHOOSE_USER_LOGIN:
				returnMsg(arg0.result, CHOOSE_USER_LOGIN);
				break;
			}
		}

	}

	/*
	 * 处理返回结果数据
	 */
	private void returnMsg(String json, int code)
	{
		JsonParser jsonParser = new JsonParser();
		JsonElement jsonElement = jsonParser.parse(json);
		switch(code)
		{
			case CHOOSE_USER_LOGIN:
				JsonObject jsonObjectChoose = jsonElement.getAsJsonObject();
			    String returnMsgChoose = jsonObjectChoose.get("returnMsg").toString();
			    if(returnMsgChoose==null || "null".equals(returnMsgChoose))
			    {
			    	HealthUtil.infoAlert(UserAccountActivity.this, "登录失败,请重试...");
			    	return;
			    }
				this.user = HealthUtil.json2Object(returnMsgChoose.toString(), User.class);
				if (this.user != null)
				{
					HealthUtil.writeUserInfo(returnMsgChoose.toString());
					User user = HealthUtil.getUserInfo();
					HealthUtil.writeUserId(user.getUserId());
					HealthUtil.writeUserPhone(user.getTelephone());
					ParamUtil.setUserId(user.getUserId());	
					HealthUtil.writeChooseUsers(user);
					userAdapter.setChoosePhone(user.getTelephone());
					userAdapter.notifyDataSetChanged();
					
					String userId=this.user.getUserId();
					RequestParams param = webInterface.getUserRelate(userId);
					invokeWebServer(param, GET_LIST);
					break;
				}
			case GET_LIST:
				JsonObject jsonObjectT = jsonElement.getAsJsonObject();
				JsonArray jsonArray = jsonObjectT.getAsJsonArray("returnMsg");
				Gson gson = new Gson();
				this.userRelateTs =gson.fromJson(jsonArray, new TypeToken<List<UserRelateT>>(){}.getType());
				
				List<User> usersT=HealthUtil.readChooseUsers();
				if(usersT!=null && userRelateTs!=null)
				{
					for(UserRelateT relateUser:userRelateTs)
					{
						for(User u:usersT)
						{
							if(u.getTelephone().equals(relateUser.getRelatePhone()))
							{
								u.setUserName(relateUser.getRelateName());
							}
						}
					}
				}
				adapter = new RelateListAdapter(UserAccountActivity.this, userRelateTs);
				this.list.setAdapter(adapter);
				userAdapter = new UsersListAdapter(UserAccountActivity.this,user.getTelephone(),true);
				this.userslist.setAdapter(userAdapter);
				break;
			case DELETE:
			JSONObject jsonObj;
			try
			{
				jsonObj = new JSONObject(json);
				String executeTypeT = jsonObj.getString("executeType");
				if("success".equals(executeTypeT))
				{
					userRelateTs.remove(relateT);
					adapter.setRelateTs(userRelateTs);
					adapter.notifyDataSetChanged();
					HealthUtil.infoAlert(UserAccountActivity.this, "解除成功");
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				break;
		}
	}
}
