package com.site.application;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.os.Vibrator;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.pgyersdk.crash.PgyCrashManager;
import com.site.tools.SiteUtil;

public class RegApplication extends Application {
	public LocationClient locationClient;
	public static RegApplication applicationContext;
	public Vibrator mVibrator;
	public List<Activity> activityList = new LinkedList<Activity>();

	public RegApplication() {
		super();
		applicationContext = this;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		SDKInitializer.initialize(this);
		startLocation();
		locationClient.start();
		String cityId=SiteUtil.getCity();
		 
		if(cityId==null || "".equals(cityId))
		{
			SiteUtil.writeCity("000");
			SiteUtil.writeCityName("武汉");
		}
		
	

	}

	public void startLocation() {
		LocationClientOption locationOption = new LocationClientOption();
		locationOption.setIsNeedAddress(true);// 获取文字地理信息
		locationOption.setScanSpan(60000);
		locationClient = new LocationClient(getApplicationContext(),
				locationOption);
		locationClient.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation location) {
				if (location == null) {
					return;
				}
				// Receive Location
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
				sb.append(location.getAddrStr());

				SiteUtil.writeLongitude(location.getLongitude() + "");
				SiteUtil.writeLatitude(location.getLatitude() + "");
				String city = location.getCity();
				sb.append("\n城市:" + city);
				// if(city!=null)
				// {
				// SiteUtil.writeCityName(location.getCity()+"");
				// }
				// SiteUtil.writeAddress(location.getAddrStr());
				if (location.getLocType() == BDLocation.TypeGpsLocation) {
					sb.append("\nspeed : ");
					sb.append(location.getSpeed());
					sb.append("\nsatellite : ");
					sb.append(location.getSatelliteNumber());
					sb.append("\ndirection : ");
					sb.append("\naddr : ");
				} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
					sb.append("\naddr : ");
					sb.append(location.getAddrStr());
					sb.append("\noperationers : ");
					sb.append(location.getOperators());
				}

				Log.i("BaiduLocationApiDem", sb.toString());
			}
		});
	}

	public static RegApplication getInstance() {
		return applicationContext;
	}

	// 添加Activity 到容器中
	public void addActivity(Activity activity) {
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

	public  void finish(Class classs)
	{
		for (Activity activity : activityList) 
		{
			if(classs.equals(activity.getClass()))
			{
				activity.finish();
				System.out.println("name:"+activity.getClass());
			}
			
//			activity.finish();
		}
//		activityList.clear();
	}
}
