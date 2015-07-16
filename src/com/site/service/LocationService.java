package com.site.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.baidu.location.LocationClient;
import com.site.application.RegApplication;
/**
 * 地图定位功能
 * @author yangjingwen
 *
 */
public class LocationService extends Service{
	private LocationClient mLocClient;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

	@Override
	public void onCreate() {
		//初始化百度地图
		initLBS();
	}



	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		startLocation();
		return super.onStartCommand(intent, flags, startId);
	}

	/*
	 * 初始化百度定位服务
	 */
	private void initLBS() {
		if(mLocClient == null) {
			mLocClient = ((RegApplication) getApplication()).mLocationClient;
		}
	}
	
	/**
	 * 定位请求开始
	 */
	private void startLocation() {
		if (mLocClient != null && !mLocClient.isStarted()) {
//			QueueUtil.LOG_D(getClass(), "---BD location start");
			mLocClient.start();
		}
	}
	
}
