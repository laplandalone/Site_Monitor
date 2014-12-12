package com.dm.yx.view.user;

import java.util.List;

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
import com.dm.yx.adapter.ContactListAdapter;
import com.dm.yx.model.User;
import com.dm.yx.model.UserContactT;
import com.dm.yx.tools.HealthConstant;
import com.dm.yx.tools.HealthUtil;
import com.dm.yx.view.order.UserOrderActivity;
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

public class ContactListActivity extends BaseActivity implements OnItemClickListener
{
	@ViewInject(R.id.title)
	private TextView title;
	@ViewInject(R.id.contentnull)
	private RelativeLayout layout;
	
	private User user;
	private ListView list;
	private List<UserContactT> contactTs;
	private UserContactT  contactT;
	private Context context;
	private ContactListAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_list);
		this.list=(ListView) findViewById(R.id.comlist);
		ViewUtils.inject(this);
		addActivity(this);
		context=this;
		initView();
		initValue();
	}

	@Override
	protected void initView()
	{
		// TODO Auto-generated method stub
		title.setText("我的联系人");
		list.setOnItemLongClickListener( new OnItemLongClickListener()
		{

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				contactT =  contactTs.get(position);
				
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("删除提示");
				
					builder.setMessage("是否删除此就诊人？");
					builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							RequestParams param = webInterface.deleteUserContactT(contactT.getContactId());
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
				return true;
			}
		});
	}


	@OnClick(R.id.back)
	public void toHome(View v)
	{
		Intent intent = new Intent(ContactListActivity.this, MainPageActivity.class);
		startActivity(intent);
		exit();
	}
	
	@OnClick(R.id.addContact)
	public void addContact(View v)
	{
		Intent intent = new Intent(ContactListActivity.this, ContactActivity.class);
		startActivity(intent);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initView();
		initValue();
	}
	@Override
	protected void initValue()
	{
		// TODO Auto-generated method stub
		dialog.setMessage("正在加载,请稍后...");
		dialog.show();
		this.user=HealthUtil.getUserInfo();
		String userId=user.getUserId();
		RequestParams param = webInterface.getUserContact (userId);
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
			HealthUtil.infoAlert(ContactListActivity.this, "信息加载失败，请检查网络后重试");
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
			case GET_LIST_MORE:
				returnMsg(arg0.result, GET_LIST_MORE);
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
		
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		switch (code)
		{
			case GET_LIST:
			JsonArray jsonArray = jsonObject.getAsJsonArray("returnMsg");
			Gson gson = new Gson();
			this.contactTs =gson.fromJson(jsonArray, new TypeToken<List<UserContactT>>(){}.getType());
			if(this.contactTs.size()==0)
			{
				layout.setVisibility(View.VISIBLE);
				list.setVisibility(View.GONE);
			}else
			{
				layout.setVisibility(View.GONE);
				list.setVisibility(View.VISIBLE);
				adapter = new ContactListAdapter(ContactListActivity.this, contactTs);
				this.list.setAdapter(adapter);
				this.list.setOnItemClickListener(this);
			}
			break;
			case DELETE:
				String rsn= jsonObject.get("returnMsg").toString();
				if("true".equals(rsn))
				{
					HealthUtil.infoAlert(ContactListActivity.this, "删除成功...");
					contactTs.remove(contactT);
					adapter.notifyDataSetChanged();
					if(this.contactTs==null || this.contactTs.size()==0)
					{
						layout.setVisibility(View.VISIBLE);
						list.setVisibility(View.GONE);
					}
				}else
				{
					HealthUtil.infoAlert(ContactListActivity.this, "删除失败...");
				}
			break;
		}
		
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		// TODO Auto-generated method stub
		Intent intent = new Intent(ContactListActivity.this, UpdateContactActivity.class);
		UserContactT contactT  = contactTs.get(position);
		Bundle bundle = new Bundle();
		bundle.putSerializable("contactT", contactT);
		intent.putExtras(bundle);
		startActivity(intent);
	}
}
