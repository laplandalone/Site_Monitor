package com.dm.yx.push;

import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.baidu.frontia.api.FrontiaPushMessageReceiver;
import com.dm.yx.MainPageActivity;
import com.dm.yx.R;
import com.dm.yx.tools.HealthUtil;
public class BDPushBroadcaster extends FrontiaPushMessageReceiver{

	private Context mContext;
	private NotificationManager nm;
	
	/**
	 * bind callback
	 * arg3 channelID
	 * arg4 userID
	 */
	@Override
	public void onBind(Context arg0, int arg1, String arg2, String arg3,
			String arg4, String arg5) {
		mContext = arg0;
		HealthUtil.writePushChannelId(arg3);
		HealthUtil.writePushUserlId(arg4);
		HealthUtil.LOG_D(getClass(), "channelID: " + arg3 + " userID: " + arg4);
		System.out.println("channelID: " + arg3 + " userID: " + arg4);
		if (arg1 == 0) {
			HealthUtil.LOG_D(getClass(), "bind success");
			HealthUtil.writeBindPush(true);
		} else {
			HealthUtil.LOG_D(getClass(), "bind failed");
		}
	}

	@Override
	public void onDelTags(Context arg0, int arg1, List<String> arg2,
			List<String> arg3, String arg4) {
		
	}

	@Override
	public void onListTags(Context arg0, int arg1, List<String> arg2,
			String arg3) {
	}

	@Override
	public void onMessage(Context arg0, String arg1, String arg2) {
		mContext = arg0;
		HealthUtil.LOG_D(getClass(), "message: " + arg1 + "customContent: " + arg2);
		showNotification("透传消息", arg1);
	}

	@Override
	public void onNotificationClicked(Context arg0, String arg1, String arg2,
			String arg3) {
		arg0 = mContext;
		HealthUtil.LOG_D(getClass(), "title: " + arg1 + "content: " + arg2 + "customContent: " + arg3);
		showNotification(arg1, arg2);
	}

	@Override
	public void onSetTags(Context arg0, int arg1, List<String> arg2,
			List<String> arg3, String arg4) {
		
	}
	
	//解除绑定回调
	@Override
	public void onUnbind(Context arg0, int arg1, String arg2) {
		
	}
	
	//将消息显示在通知栏，目前设置的点击跳转到MainPageActivity
	@SuppressWarnings({ "unused", "deprecation" })
	private void showNotification(String title,String message) 
	{
		if (nm == null)
		{
			nm = (NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		}
		Notification notification = new Notification(R.drawable.ic_launcher,
				message, System.currentTimeMillis());
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		Intent intent = null;
		if (intent != null)
		{
			intent.setClass(mContext, MainPageActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("title", title);
			intent.putExtra("message", message);
			PendingIntent contentIntent = PendingIntent.getActivity(mContext, 1014,intent,PendingIntent.FLAG_UPDATE_CURRENT);
			notification.setLatestEventInfo(mContext, title,
					message, contentIntent);
			if (nm != null)
			{
				nm.notify(R.string.app_name, notification);
			}
		}	
	}
}
