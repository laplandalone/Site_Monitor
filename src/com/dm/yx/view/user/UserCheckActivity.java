package com.dm.yx.view.user;

import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dm.yx.BaseActivity;
import com.dm.yx.MainPageActivity;
import com.dm.yx.R;
import com.dm.yx.model.HisUser;
import com.dm.yx.model.User;
import com.dm.yx.tools.HealthConstant;
import com.dm.yx.tools.HealthUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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

public class UserCheckActivity extends BaseActivity
{
	
	@ViewInject(R.id.title)
	private TextView title;
	
	@ViewInject(R.id.name)
	private TextView name;
	
	@ViewInject(R.id.userCard)
	private TextView userCard;
	private User user;
	private String patientId;
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
	public void health1(View v) throws Exception
	{
		 patientId=userCard.getText().toString().trim();
		 String patientNameT=name.getText().toString().trim();
		 if("".equals(patientNameT))
			{
				HealthUtil.infoAlert(UserCheckActivity.this, "姓名不能为空");
				return;
			}
		 
		 if("".equals(patientId))
		{
			HealthUtil.infoAlert(UserCheckActivity.this, "病案号不能为空");
			return;
		}
		dialog.setMessage("正在加载,请稍后...");
		dialog.show();
		if(patientId.length()==6)
		{
			patientId="PID000"+patientId;
		}
		String param="select  * from mzbrxx where patient_id='"+patientId+"'";
		MineRequestCallBack requestCallBack = new MineRequestCallBack(GET_LIST);
		RequestParams requestParams = new RequestParams("UTF-8");
		BasicNameValuePair nameValuePair = new BasicNameValuePair("param",param);
		requestParams.addBodyParameter(nameValuePair);
		mHttpUtils.send(HttpMethod.POST,  HealthConstant.HIS_URL, requestParams, requestCallBack);

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
			
			HealthUtil.infoAlert(UserCheckActivity.this, "信息加载失败，请检查网络后重试");
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
		
		JsonArray jsonArray = jsonElement.getAsJsonArray();
		
		String patientNameT=name.getText().toString().trim();
		
		Gson gson = new Gson();
		List<HisUser> hisUsers  = gson.fromJson(jsonArray, new TypeToken<List<HisUser>>()
		{
		}.getType());
		if(hisUsers!=null && hisUsers.size()>0)
		{
			HisUser hisUser = hisUsers.get(0);
			String name=hisUser.getPatient_name();
			if(name!=null && patientNameT.equals(name.trim()))
			{
				HealthUtil.writePatientId(patientId);
				HealthUtil.writePatientName(name);
			    Intent intent = new Intent(UserCheckActivity.this,CheckRstListActivity.class);
			    intent.putExtra("patientId", patientId);
				startActivity(intent);
			}else
			{
				HealthUtil.infoAlert(UserCheckActivity.this, "身份校验失败，请重试...");
				return;
			}
		}else
		{
			HealthUtil.infoAlert(UserCheckActivity.this, "身份校验失败，请重试...");
			return;
		}
	}
	@Override
	protected void initView()
	{
		// TODO Auto-generated method stub
		  String id=HealthUtil.readPatientId();
		  if(!"".equals(id) && id.length()>6)
		  {
			  id=id.substring(id.length()-6,id.length()); 
		  }
		  userCard.setText(id);
		  name.setText(HealthUtil.readpatientName());
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
			}else
			{
//				checkUser();
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
