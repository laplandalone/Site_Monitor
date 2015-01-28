package com.dm.yx.view.visit;

import java.util.Date;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dm.yx.BaseActivity;
import com.dm.yx.MainPageActivity;
import com.dm.yx.R;
import com.dm.yx.adapter.ConfigListAdapter;
import com.dm.yx.model.HisUser;
import com.dm.yx.model.HospitalConfigT;
import com.dm.yx.model.User;
import com.dm.yx.tools.DateUtils;
import com.dm.yx.tools.HealthConstant;
import com.dm.yx.tools.HealthUtil;
import com.dm.yx.tools.StringUtil;
import com.dm.yx.view.user.CheckRstListActivity;
import com.dm.yx.view.user.LoginActivity;
import com.dm.yx.view.user.UserCheckActivity;
import com.dm.yx.view.user.UserUpdateActivity;
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

public class PatientVisitListActivity extends BaseActivity implements OnItemClickListener
{
	
	@ViewInject(R.id.title)
	private TextView title;
	private String userId="";
	private User user;
	private List<HospitalConfigT> hospitalConfigTs;
	private ConfigListAdapter adapter;
	private ListView list;
	@ViewInject(R.id.contentnull)
	private RelativeLayout layout;
	private String patientId;
	private int age;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_list);
		list = (ListView) findViewById(R.id.comlist);
		ViewUtils.inject(this);
		addActivity(this);
		initView();
		initValue();
	}

	@OnClick(R.id.back)
	public void toBack(View v)
	{
		Intent intent = new Intent(PatientVisitListActivity.this, MainPageActivity.class);
		startActivity(intent);
		exit();
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
				userId=user.getUserId();
				String patientId=user.getCardNo();
				if(patientId==null || "".equals(patientId))
				{
					HealthUtil.infoAlert(PatientVisitListActivity.this, "病案号不存在,请到个人中心完善");
					Intent intentU = new Intent(PatientVisitListActivity.this, UserUpdateActivity.class);
					startActivityForResult(intentU, 1);
				}
			}
			else
			{
				finish();
			}
			break;
		case 1:
			this.user = HealthUtil.getUserInfo();
			if (this.user != null)
			{
				userId=user.getUserId();
				String patientId=user.getCardNo();
				if(patientId==null || "".equals(patientId))
				{
					HealthUtil.infoAlert(PatientVisitListActivity.this, "病案号不存在,请到个人中心完善");
					Intent intentU = new Intent(PatientVisitListActivity.this, UserUpdateActivity.class);
					startActivityForResult(intentU, 1);
				}
			}
			else
			{
				finish();
			}
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void initView()
	{
		// TODO Auto-generated method stub
		
	}
	

	@Override
	protected void initValue()
	{
		// TODO Auto-generated method stub
		title.setText("患者随访");
		
		user=HealthUtil.getUserInfo();
		if (this.user == null)
		{
			Intent intent = new Intent(PatientVisitListActivity.this, LoginActivity.class);
			startActivityForResult(intent, 0);
		}else if( user.getCardNo()==null || "".equals(user.getCardNo()))
		{
			HealthUtil.infoAlert(PatientVisitListActivity.this, "姓名或病案号校验失败，请到个人中心完善...");
			finish();
		}
		else{
			userId=user.getUserId();
			patientId=user.getCardNo();
			if(patientId.length()==6)
			{
				patientId="PID000"+patientId;
			}
			String hisParam="select  * from mzbrxx where patient_id='"+patientId+"'";
			MineRequestCallBack requestCallBack = new MineRequestCallBack(HIS);
			RequestParams requestParams = new RequestParams("UTF-8");
			BasicNameValuePair nameValuePair = new BasicNameValuePair("param",hisParam);
			requestParams.addBodyParameter(nameValuePair);
			mHttpUtils.send(HttpMethod.POST,  HealthConstant.HIS_URL, requestParams, requestCallBack);
		}
	
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

			HealthUtil.infoAlert(PatientVisitListActivity.this, "信息加载失败，请检查网络后重试");
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
			case HIS:
				returnMsg(arg0.result, HIS);
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
		Gson gson = new Gson();
		switch (code) 
		{
		case HIS:
			JsonArray jsonArray = jsonElement.getAsJsonArray();
			String patientNameT=user.getUserName();
			List<HisUser> hisUsers  = gson.fromJson(jsonArray, new TypeToken<List<HisUser>>()
			{
			}.getType());
			if(hisUsers!=null && hisUsers.size()>0)
			{
				HisUser hisUser = hisUsers.get(0);
				String name=hisUser.getPatient_name();
			 
				if(name!=null && patientNameT.equals(name.trim()))
				{
					dialog.setMessage("正在加载,请稍后...");
					dialog.show();
					RequestParams param = webInterface.getHospitalConfig("102", "VISIT_TYPE",userId);
					invokeWebServer(param, GET_LIST);
				}else
				{
					HealthUtil.infoAlert(PatientVisitListActivity.this, "姓名或病案号校验失败，请到个人中心完善...");
					finish();
					return;
				}
			}else
			{
				HealthUtil.infoAlert(PatientVisitListActivity.this, "姓名或病案号校验失败，请到个人中心完善...");
				finish();
				return;
			}
			break;
		case GET_LIST:
			JsonObject jsonObject = jsonElement.getAsJsonObject();
			String executeType = jsonObject.get("executeType").getAsString();
			if (!"success".equals(executeType))
			{
				HealthUtil.infoAlert(PatientVisitListActivity.this, "加载失败请重试.");
				return;
			}
			JsonArray jsonArrayA = jsonObject.getAsJsonArray("returnMsg");
			 
			this.hospitalConfigTs = gson.fromJson(jsonArrayA, new TypeToken<List<HospitalConfigT>>()
			{
			}.getType());
			 
			adapter = new ConfigListAdapter(PatientVisitListActivity.this, hospitalConfigTs);
			this.list.setAdapter(adapter);
			this.list.setOnItemClickListener(this);
			if(this.hospitalConfigTs.size()==0)
			{
				layout.setVisibility(View.VISIBLE);
				list.setVisibility(View.GONE);
			}
			break;
		default:
			break;
		}
		
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		// TODO Auto-generated method stub
		Intent intent = new Intent(PatientVisitListActivity.this, VisitDetailActivity.class);
		HospitalConfigT hospitalConfigT = hospitalConfigTs.get(position);
		String url=hospitalConfigT.getRemark();
		String name=hospitalConfigT.getConfigVal();
		User user=HealthUtil.getUserInfo();
		String patientId=user.getCardNo();
		String operType="无";
//		url="http://192.168.137.1:7001/visit/asdChild.jsp";
		intent.putExtra("url", url+"?name="+user.getUserName()+"&patientId="+patientId+"&operType="+operType);
		intent.putExtra("title", name);
		intent.putExtra("display","Y");
		startActivity(intent);
		 
	}
}
