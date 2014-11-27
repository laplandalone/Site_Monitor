package com.dm.yx.application;

import java.util.LinkedList;
import java.util.List;

import com.baidu.frontia.FrontiaApplication;

import android.app.Activity;
import android.app.Application;

public class RegApplication extends Application
{

	public static RegApplication applicationContext;
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
}
