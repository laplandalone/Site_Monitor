package com.dm.yx.push;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.frontia.api.FrontiaPushMessageReceiver;
import com.dm.yx.R;
import com.dm.yx.tools.HealthConstant;
import com.dm.yx.tools.HealthUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class MyMessageReceiver extends FrontiaPushMessageReceiver{

	private Context mContext;
	private NotificationManager nm;
	/**
	 * push server发起绑定请求，这个过程是异步的。绑定请求的结果通过onBind返回。
	 * @param arg0
     *            BroadcastReceiver的执行Context
     * @param arg1
     *            绑定接口返回值，0 - 成功
     * @param arg2
     *            应用id。errorCode非0时为null
     * @param arg3
     *            应用user id。errorCode非0时为null
     * @param arg4
     *            应用channel id。errorCode非0时为null
     * @param arg5
     *            向服务端发起的请求id。在追查问题时有用；
     * @return none
	 */
	@Override
	public void onBind(Context arg0, int arg1, String arg2, String arg3,
			String arg4, String arg5) {
		mContext = arg0;
		if (arg1 == 0) {
			System.out.println("bind sercie success,userId=" + arg3);
			System.out.println("bind sercie success,arg4=" + arg4);
			HealthUtil.LOG_D(getClass(), "bind sercie success,userId=" + arg3);
			HealthUtil.writePushUserlId(arg3);
			HealthUtil.writePushChannelId(arg4);
		} else {
			HealthUtil.LOG_D(getClass(), "bind sercie failure,userId=" + arg3);
		}
		
	}

	/**
     * delTags() 的回调函数。
     * 
     * @param context
     *            上下文
     * @param errorCode
     *            错误码。0表示某些tag已经删除成功；非0表示所有tag均删除失败。
     * @param successTags
     *            成功删除的tag
     * @param failTags
     *            删除失败的tag
     * @param requestId
     *            分配给对云推送的请求的id
     */
	@Override
	public void onDelTags(Context arg0, int arg1, List<String> arg2,
			List<String> arg3, String arg4) {
	}

	/**
     * listTags() 的回调函数。
     * 
     * @param context
     *            上下文
     * @param errorCode
     *            错误码。0表示列举tag成功；非0表示失败。
     * @param tags
     *            当前应用设置的所有tag。
     * @param requestId
     *            分配给对云推送的请求的id
     */
	@Override
	public void onListTags(Context arg0, int arg1, List<String> arg2,
			String arg3) {
	}

	/**
     * 接收透传消息的函数。
     * 
     * @param arg0
     *            上下文
     * @param arg1
     *            推送的消息
     * @param arg2
     *            自定义内容,为空或者json字符串
     */
	@Override
	  public void onMessage(Context context, String message,
	            String customContentString){
		mContext = context;
		String messageString = "透传消息 message=\"" + message+ "\" customContentString=" + customContentString;
		controlDisplay(message);
        
	}

     private void controlDisplay(String msg)
     {
    	 JsonParser jsonParser = new JsonParser();
		 JsonElement jsonElement = jsonParser.parse(msg);
		 JsonObject jsonObject = jsonElement.getAsJsonObject();
		 String title=jsonObject.get("title").getAsString();
		 String desc=jsonObject.get("description").getAsString();
		 String userIds=jsonObject.get("user_id").getAsString();
		 String[] ids = userIds.split(",");
		 String userId=HealthUtil.readUserId();
		
		 if(userIds!=null && !"".equals(userIds))
		 {
			 if(ids!=null && !"".equals(userId))
			 {
				 for(int i=0;i<ids.length;i++)
				 {
					 if(ids[i]!=null && ids[i].equals(userId))
					 {
						 showNotification(title, desc);
						 break;
					 }
				 }
			 }
			
		 }else
		 {
			 showNotification(desc,title);
		 }
		
		
     }
	
	/**
     * setTags() 的回调函数。
     * 
     * @param arg0 context
     *            上下文
     * @param arg1 errorCode
     *            错误码。0表示某些tag已经设置成功；非0表示所有tag的设置均失败。
     * @param arg2 successTags
     *            设置成功的tag
     * @param arg3 failTags
     *            设置失败的tag
     * @param arg4 requestId
     *            分配给对云推送的请求的id
     */
	@Override
	public void onSetTags(Context arg0, int arg1, List<String> arg2,
			List<String> arg3, String arg4) {
	}

	/**
     * PushManager.stopWork() 的回调函数。
     * 
     * @param context
     *            上下文
     * @param errorCode
     *            错误码。0表示从云推送解绑定成功；非0表示失败。
     * @param requestId
     *            分配给对云推送的请求的id
     */
	@Override
	public void onUnbind(Context arg0, int arg1, String arg2) {
	}
	

	/**
	 * 显示通知
	 * @param message 推送消息
	 * @param userMessage 自定义消息
	 */
	@SuppressWarnings("deprecation")
	private void showNotification(String message,String title) 
	{
		 System.err.println("showNotification:"+message+" title:"+title);
			if (nm == null)
			{
				nm = (NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
			}
			Notification notification = new Notification(R.drawable.ic_launcher,message, System.currentTimeMillis());
			notification.defaults |= Notification.DEFAULT_SOUND;
			notification.flags = Notification.FLAG_AUTO_CANCEL;
	
			//设置有新的消息
			HealthConstant.isNewMessage = true;
			
			Intent intent = new Intent(mContext, NotificationMessageActivity.class);
			intent.putExtra("title", title);
			intent.putExtra("message", message);
			PendingIntent contentIntent = PendingIntent.getActivity(mContext, 1014,intent,PendingIntent.FLAG_UPDATE_CURRENT);
	
			// must set this for content view, or will throw a exception
	
			notification.setLatestEventInfo(mContext, title,message, contentIntent);
	
			if (nm != null) 
			{
				nm.notify(R.string.app_name, notification);
			}
	}

	
	/**
     * 接收通知点击的函数。注：推送通知被用户点击前，应用无法通过接口获取通知的内容。
     * 
     * @param context
     *            上下文
     * @param title
     *            推送的通知的标题
     * @param description
     *            推送的通知的描述
     * @param customContentString
     *            自定义内容，为空或者json字符串
     */
    @Override
    public void onNotificationClicked(Context context, String title,
            String description, String customContentString) {
        String notifyString = "通知点击 title=\"" + title + "\" description=\""
                + description + "\" customContent=" + customContentString;
        System.err.println(notifyString);

        // 自定义内容获取方式，mykey和myvalue对应通知推送时自定义内容中设置的键和值
        if (!TextUtils.isEmpty(customContentString)) {
            JSONObject customJson = null;
            try {
                customJson = new JSONObject(customContentString);
                String myvalue = null;
                if (customJson.isNull("mykey")) {
                    myvalue = customJson.getString("mykey");
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
        updateContent(context, notifyString);
    }

    private void updateContent(Context context, String content) {
        Log.d(TAG, "updateContent");
        String logText = "" ;

        if (!logText.equals("")) {
            logText += "\n";
        }

        SimpleDateFormat sDateFormat = new SimpleDateFormat("HH-mm-ss");
        logText += sDateFormat.format(new Date()) + ": ";
        logText += content;

//        Utils.logStringCache = logText;

        Intent intent = new Intent();
        intent.setClass(context.getApplicationContext(), NotificationMessageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.getApplicationContext().startActivity(intent);
    }

}
