package com.dm.yx.view.other;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.dm.yx.tools.HealthConstant;
import com.dm.yx.tools.HealthUtil;
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
		jsonObject.put("applicationVersionCode", versionName);
		jsonObject.put("applicationType", "CUST");
		jsonObject.put("deviceType", "ANDROID");
		jsonObject.put("hospitalId", HealthUtil.readHospitalId());
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
				String remark = returnJson.getString("remark");
				String applicationUrl = returnJson.getString("applicationUrl");
				HealthUtil.writeAppUrl(applicationUrl);
				String forceUpdateFlag = returnJson.getString("forceUpdateFlag");
				String applicationVersionCode = returnJson.getString("applicationVersionCode");
				String versionName = HealthUtil.getVersionName();
				if(!versionName.equals(applicationVersionCode))
				{   
					deleteFile(file);
					Bundle bundle = new Bundle();
					bundle.putString("remark", remark);
					bundle.putString("applicationUrl", applicationUrl);
					bundle.putString("forceUpdateFlag", forceUpdateFlag);
					bundle.putString("applicationVersionCode", applicationVersionCode);
					Intent intent = new Intent(this, NewVersionActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtras(bundle);
					startActivity(intent);
				}else
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
	
	
}
