package com.dm.yx.view.other;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.dm.yx.model.User;
import com.dm.yx.tools.HealthConstant;
import com.dm.yx.tools.HealthUtil;
import com.dm.yx.view.download.UpdateViewActivity;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lurencun.android.webservice.IWebServiceInterface;
import com.lurencun.android.webservice.WebServiceInterfaceImpl;

public class CheckNewVersion extends Service{

	private HttpUtils mHttpUtils = new HttpUtils();
	private final static int CHECK_NEWVERSION = 1;
	protected IWebServiceInterface webInterface = new WebServiceInterfaceImpl();
	private String flag;
	File file = new File(HealthConstant.Download_path); 
	
	private String versionCode="";
	short shortFlag = 0;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.e("health", "version------------>checkVersion");
		this.flag=intent.getStringExtra("flag");
		checkNewVersion();
		return super.onStartCommand(intent, flags, startId);
	}

	private void checkNewVersion() {
	try {
		JSONObject jsonObject = new JSONObject();
		String versionName = HealthUtil.getVersionName();
		this.versionCode=versionName;
		jsonObject.put("applicationVersionCode", versionName);
		jsonObject.put("applicationType", "CUST");
		jsonObject.put("deviceType", "ANDROID");
		jsonObject.put("hospitalId", HealthUtil.readHospitalId());
		jsonObject.put("telephone", HealthUtil.readUserPhone());
		jsonObject.put("", HealthUtil.readUserPhone());
		RequestParams param = webInterface.checkNewVersion(jsonObject.toString());
		connectWebServer(param,CHECK_NEWVERSION);
	} catch (JSONException e) {
		e.printStackTrace();
	}
	
	}
	
	public void deleteFile(File file)
	{ 

        if (file.exists() == false) 
        { 
            return; 
        } else { 
            if (file.isFile()) { 
                file.delete(); 
                return; 
            } 
            if (file.isDirectory()) { 
                File[] childFile = file.listFiles(); 
                if (childFile == null || childFile.length == 0) { 
                    file.delete(); 
                    return; 
                } 
                for (File f : childFile) { 
                    deleteFile(f); 
                } 
                file.delete(); 
            } 
        } 
    } 
 

	/**
	 * 链接服务器
	 * 
	 * @param param
	 */
	private void connectWebServer(RequestParams param, int responseCode) {
		try {
//			RealNameUtil.LOG_D(getClass(), "connect to web server");
			MineRequestCallBack requestCallBack = new MineRequestCallBack(
					responseCode);
			mHttpUtils.send(HttpMethod.POST, HealthConstant.URL, param, requestCallBack);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 返回结果处理
	 */
	class MineRequestCallBack extends RequestCallBack<String> {

		private int responseCode;

		public MineRequestCallBack(int responseCode) {
			super();
			this.responseCode = responseCode;
		}


		@Override
		public void onFailure(HttpException error, String msg) 
		{
			HealthUtil.LOG_D(getClass(), "version-------------------->msg=" + msg);
			stopSelf();
		}

		@Override
		public void onSuccess(ResponseInfo<String> arg0) {
			switch(responseCode) {
			case CHECK_NEWVERSION:
				newVersionResult(arg0.result);
				HealthUtil.LOG_D(getClass(), "version======================-->result=" + arg0.result);
				break;
			}
		}
		
	}
	String appName="";
	String downUrl="";
	private Context mContext=this;
	private void newVersionResult(String result) {
		try {
			JSONObject jsonObject = new JSONObject(result);
			String executeType = jsonObject.getString("executeType");
			JSONObject returnJson = jsonObject.getJSONObject("returnMsg");
			if (!"success".equals(executeType))
			{
				Toast toast = Toast.makeText(this, "加载失败请重试.", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				return;
			}
			/** xjz 2014-05-21 当returnJson为空的时候会报异常 end*/
			if(returnJson.length() > 0)
			{
				deleteFile(file);
				String remark = returnJson.getString("remark");
				String trailRemark = returnJson.getString("trailRemark");
				String applicationUrl = returnJson.getString("applicationUrl");
			
				HealthUtil.writeAppUrl(applicationUrl);
				String forceUpdateFlag = returnJson.getString("forceUpdateFlag");
				String applicationVersionCode = returnJson.getString("applicationVersionCode");
				String trialVersionCode= returnJson.getString("trailVersionCode");
				String trailVersionFlag= returnJson.getString("trailVersionFlag");
				String trailVersionPhone= returnJson.getString("trailVersionPhone");
				String versionName = HealthUtil.getVersionName();
				 
				if(!versionName.equals(applicationVersionCode))
				{
					if( !"".equals(HealthUtil.readUserPhone()) && trailVersionPhone.contains(HealthUtil.readUserPhone()) && versionName.equals(trialVersionCode))
					{
						if("hand".equals(this.flag))
						{
							Toast toast = Toast.makeText(this, "当前已是最新测试版本,版本号:"+versionName, Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
						}
					}else
					{
						if(versionName.equals(trialVersionCode))
						{
							if( "hand".equals(this.flag))
							{
								Toast toast = Toast.makeText(this, "当前已是最新版本,版本号:"+versionName, Toast.LENGTH_SHORT);
								toast.setGravity(Gravity.CENTER, 0, 0);
								toast.show();
							}
							return;
						}
						appName="yaxin"+applicationVersionCode+".apk";
						downUrl=HealthConstant.Download_Url+"&fileName="+appName;
						Intent intent = new Intent(CheckNewVersion.this,UpdateViewActivity.class);
						intent.putExtra("appName", appName);
						intent.putExtra("url", downUrl);
						intent.putExtra("remark",remark);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
					}
 				}else if(!versionName.equals(trialVersionCode) && "Y".equals(trailVersionFlag) && !"".equals(HealthUtil.readUserPhone()) && trailVersionPhone.contains(HealthUtil.readUserPhone()))
				{
 					
					appName="yaxin"+trialVersionCode+".apk";
					downUrl=HealthConstant.Download_Url+"&fileName="+appName;
					Intent intent = new Intent(CheckNewVersion.this,UpdateViewActivity.class);
					intent.putExtra("appName", appName);
					intent.putExtra("url", downUrl);
					intent.putExtra("remark",trailRemark);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				}
				else
				{
					if("hand".equals(this.flag))
					{
						Toast toast = Toast.makeText(this, "当前已是最新版本,版本号:"+versionName, Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
					}
				}
				
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		stopSelf();
	}
	
	private void update()
	{
		HealthUtil.startDownload(downUrl, mContext,appName,"10MB",shortFlag);
	}
}
