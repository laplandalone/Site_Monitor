package com.dm.yx.view.order;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dm.yx.BaseActivity;
import com.dm.yx.MainPageActivity;
import com.dm.yx.R;
import com.dm.yx.adapter.UserOrderListAdapter;
import com.dm.yx.model.RegisterOrderT;
import com.dm.yx.model.User;
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

public class UserOrderActivity extends BaseActivity implements OnItemClickListener
{
	@ViewInject(R.id.title)
	private TextView title;
	@ViewInject(R.id.contentnull)
	private RelativeLayout layout;
	
	@ViewInject(R.id.orderTitle)
	private LinearLayout orderTitle;
	private User user;
	private ListView list;
	private List<RegisterOrderT> registerOrderTs;
	private Context mContext;
	private RegisterOrderT orderT ;
	private UserOrderListAdapter adapter ;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext=this;
		setContentView(R.layout.user_register_order);
		this.list=(ListView) findViewById(R.id.list_view);
		ViewUtils.inject(this);
		addActivity(this);
		initView();
		initValue();
	}

	@Override
	protected void initView()
	{
		// TODO Auto-generated method stub
		title.setText("我的预约");
		list.setOnItemLongClickListener( new OnItemLongClickListener()
		{

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				 orderT =  registerOrderTs.get(position);
				
				AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
				builder.setTitle("删除提示");
				if("101".equals(orderT.getPayState()) || "100".equals(orderT.getPayState()))
				{
					builder.setMessage("是否删除此预约？");
					builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							RequestParams param = webInterface.deleteRegisterOrder(orderT.getOrderId());
							invokeWebServer(param, DELETE);
						}
					});
					
					builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
							

							}
						});
				}else
				{
					builder.setMessage("此预约不能删除");
					builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							
						}
					});
					
					builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
							

							}
						});
				}
				
				
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
		Intent intent = new Intent(UserOrderActivity.this, MainPageActivity.class);
		startActivity(intent);
		exit();
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
		RequestParams param = webInterface.getUserOrderById(userId,HealthUtil.readHospitalId());
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
			HealthUtil.infoAlert(UserOrderActivity.this, "信息加载失败，请检查网络后重试");
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
			case GET_LIST_MORE:
				returnMsg(arg0.result, GET_LIST_MORE);
			case DELETE:
				returnMsg(arg0.result, DELETE);
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
			this.registerOrderTs =gson.fromJson(jsonArray, new TypeToken<List<RegisterOrderT>>(){}.getType());
			if(this.registerOrderTs.size()==0)
			{
				layout.setVisibility(View.VISIBLE);
				list.setVisibility(View.GONE);
			}else
			{
				orderTitle.setVisibility(View.VISIBLE);
			}
			adapter = new UserOrderListAdapter(UserOrderActivity.this, registerOrderTs);
			this.list.setAdapter(adapter);
			this.list.setOnItemClickListener(this);
			break;
		case DELETE:
			String rsn= jsonObject.get("returnMsg").toString();
			if("true".equals(rsn))
			{
				HealthUtil.infoAlert(UserOrderActivity.this, "删除成功...");
				registerOrderTs.remove(orderT);
				adapter.notifyDataSetChanged();
			}else
			{
				HealthUtil.infoAlert(UserOrderActivity.this, "删除失败...");
			}
		break;
		}
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		// TODO Auto-generated method stub
		Intent intent = new Intent(UserOrderActivity.this,ConfirmOrderActivity.class);
		RegisterOrderT registerOrderT =registerOrderTs.get(position);
		intent.putExtra("orderType", "old");
		intent.putExtra("hospitalId", registerOrderT.getHospitalId());
		intent.putExtra("payState", registerOrderT.getPayState());
		intent.putExtra("orderId", registerOrderT.getOrderId());
		intent.putExtra("orderState", registerOrderT.getOrderState());
		intent.putExtra("doctorName", registerOrderT.getDoctorName());
		intent.putExtra("registerTime", registerOrderT.getRegisterTime());
		intent.putExtra("fee", registerOrderT.getOrderFee());
		intent.putExtra("registerId", registerOrderT.getRegisterId());
		intent.putExtra("userOrderNum", registerOrderT.getOrderNum());
		
		intent.putExtra("doctorId", registerOrderT.getDoctorId());
		intent.putExtra("teamId", registerOrderT.getTeamId());
		intent.putExtra("teamName", registerOrderT.getTeamName());
		intent.putExtra("userName", registerOrderT.getUserName());
		intent.putExtra("userNo", registerOrderT.getUserNo());
		intent.putExtra("userTelephone", registerOrderT.getUserTelephone());
		intent.putExtra("sex", registerOrderT.getSex());
		startActivity(intent);
	}
}
