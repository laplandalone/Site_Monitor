package com.site.application;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.os.Vibrator;
import android.util.Log;

import com.baidu.frontia.FrontiaApplication;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.site.tools.HealthUtil;

public class RegApplication extends Application
{
	public LocationClient mLocationClient;
	public GeofenceClient mGeofenceClient;
	public MyLocationListener mMyLocationListener;
	
	public static RegApplication applicationContext;
	public Vibrator mVibrator;
	public List<Activity> activityList = new LinkedList<Activity>();
	public RegApplication()
	{
		super();
		applicationContext = this;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		FrontiaApplication.initFrontiaApplication(this);	//初始化百度推送
		mLocationClient = new LocationClient(this.getApplicationContext());
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
		mGeofenceClient = new GeofenceClient(getApplicationContext());
		
		mLocationClient.start();
		mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
	}
	
	public static RegApplication getInstance()
	{
		return applicationContext;
	}
	
	// 添加Activity 到容器中
	public void addActivity(Activity activity)
	{
		activityList.add(activity);
	}

	// 遍历所有Activity 并finish
	public void exit()
	{
		for (Activity activity : activityList)
		{
			activity.finish();
		}
		activityList.clear();
	}
	
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			//Receive Location 
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			
			HealthUtil.writeLongitude(location.getLongitude()+"");
			HealthUtil.writeLatitude(location.getLatitude()+"");
			HealthUtil.writeCity(location.getCity()+"");
			location.getAddrStr();
			if (location.getLocType() == BDLocation.TypeGpsLocation){
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
				sb.append("\ndirection : ");
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				sb.append(location.getDirection());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				sb.append("\noperationers : ");
				sb.append(location.getOperators());
			}
			 
			Log.i("BaiduLocationApiDem", sb.toString());
		}


	}
}
