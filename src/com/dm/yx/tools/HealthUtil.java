package com.dm.yx.tools;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.os.Environment;
import android.os.StatFs;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Toast;

import com.dm.yx.R;
import com.dm.yx.application.RegApplication;
import com.dm.yx.model.HospitalT;
import com.dm.yx.model.User;
import com.dm.yx.view.download.DownloadService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.RequestParams;

/**
 * 
 * 工具类
 *
 */
public class HealthUtil {
	
	 public static final boolean DEBUG = true;
	  private static final int LOG_SIZE_LIMIT = 3500;
	  public static final String LOG_TAG = "Digital_Medical";
	  private static SharedPreferences userPreferences;

	  private static WindowManager appWindowManager;
	  private static View downloadFloatView;
	  private static boolean viewAdded = false;// 透明窗体是否已经显示
	  private static WindowManager.LayoutParams appLayoutParams;
	  private static Context mContext = RegApplication.getInstance();
	  public static boolean isNewVersionFlag = false ;
	  
	  public static void worningAlert(Activity activity,String msg) {
		  Toast toast = Toast.makeText(activity, msg, Toast.LENGTH_SHORT);
		  toast.setGravity(Gravity.CENTER, 0, 0);
		  toast.show();
	  }
	  
	  /**
		 * 下载悬浮窗显示标志，是否有下载在进行
		 */
		public static boolean downloading = false;// 是否有下载在进行
		static 
		{
			if (userPreferences == null)
			{
			  userPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
			}
		 }
		
		public static void writeChooseUsers(User user)
		{
			boolean flag =true;
			String chooseUserTs= userPreferences.getString("chooseUsers","");
			Gson gson = new Gson();  
			List<User> users= gson.fromJson(chooseUserTs, new TypeToken<List<User>>(){}.getType());   
			if(users!=null)
			{
				for(User u:users)
				{
					if(u.getTelephone().equals(user.getTelephone()))
					{
						u.setPassword(user.getPassword());
						u.setUserName(user.getUserName());
						flag=false;
					}
				}
				if(flag)
				{
					users.add(user);
				}
				userPreferences.edit().putString("chooseUsers",gson.toJson(users)).commit();
			}else
			{
				users=new ArrayList<User>();
				users.add(user);
				userPreferences.edit().putString("chooseUsers",gson.toJson(users)).commit();
			}
		}
		
		public static void deleteChooseUsers(User user)
		{
			List<User> users= readChooseUsers();
			for(User u:users)
			{
				if(u.getTelephone().equals(user.getTelephone()))
				{
					users.remove(u);
					break;
				}
			}
			Gson gson = new Gson();
			userPreferences.edit().putString("chooseUsers",gson.toJson(users)).commit();
		}
		
		public static List<User> readChooseUsers()
		{
			String chooseUserTs= userPreferences.getString("chooseUsers","");
			User currentUser=getUserInfo();
			boolean b = true;
			if(chooseUserTs!=null && !"".equals(chooseUserTs))
			{
				Gson gson = new Gson();  
				List<User> users =  gson.fromJson(chooseUserTs, new TypeToken<List<User>>(){}.getType());   
				if(users!=null)
				{
					for(User u:users)
					{
						if(u.getTelephone().equals(currentUser.getTelephone()))
						{
							b=false;
						}
					}
					if(b)
					{
						users.add(getUserInfo());
					}
					return users;
				}
				
			}
			List<User> users=new ArrayList<User>();
			users.add(getUserInfo());
			return users;
		}
		
		public static void writeHospitalTs(String hospitalTs)
		{
			userPreferences.edit().putString("hospitalTs", hospitalTs).commit();
		}
		
		public static List<HospitalT> readHospitalTs()
		{
			String hospitalTs= userPreferences.getString("hospitalTs","");
			if(hospitalTs!=null && !"".equals(hospitalTs))
			{
				JsonParser jsonParser = new JsonParser();
				JsonElement jsonElement = jsonParser.parse(hospitalTs);
				JsonObject jsonObject = jsonElement.getAsJsonObject();
				JsonArray jsonArray = jsonObject.getAsJsonArray("returnMsg");
				Gson gson = new Gson();  
				List<HospitalT> hospitalTs2 =  gson.fromJson(jsonArray, new TypeToken<List<HospitalT>>(){}.getType());   
				return hospitalTs2;
			}
			return null;
		}
		
		public static HospitalT getHospitalTById(String hospitalId)
		{
			List<HospitalT> hospitalTs = readHospitalTs();
			for(HospitalT hospitalT:hospitalTs)
			{
				if(hospitalT.getHospitalId().equals(hospitalId))
				{
					return hospitalT;
				}
			}
			return null;
		}
		
		public static void writePushChannelId(String flag)
		{
			userPreferences.edit().putString("pushChannelId", flag).commit();
		}
		
		public static String readPushChannelId() 
		{
			return userPreferences.getString("pushChannelId", "");
		}
		
		public static void writePushUserlId(String flag)
		{
			userPreferences.edit().putString("pushUserlId", flag).commit();
		}
		
		public static String readPushUserId() 
		{
			return userPreferences.getString("pushUserlId", "");
		}
		
		public static void writeBindPush(boolean flag)
		{
			userPreferences.edit().putBoolean("isBind", flag).commit();
		}
		
		public static boolean readBindPush() 
		{
			return userPreferences.getBoolean("isBind", false);
		}
		
		public static void writeHospitalId(String hospitalId)
		{
			userPreferences.edit().putString("hospitalId", hospitalId).commit();
		}
		
		public static String readHospitalId() 
		{
			return userPreferences.getString("hospitalId", "102");
		}
		
		public static void writeAppUrl(String url)
		{
			userPreferences.edit().putString("url", url).commit();
		}
		
		public static String readAppUrl() 
		{
			return userPreferences.getString("url", "http://www.hiseemedical.com:10821/dmyx.apk");
		}
		
		public static String readHospitalName() 
		{
				return  "亚洲心脏病医院";
		}
		
		public static void writeLoginAuto(String loginAuto)
		{
			userPreferences.edit().putString("loginAuto", loginAuto).commit();
		}
		
		public static String readLoginAuto() 
		{
			return userPreferences.getString("loginAuto", "");
		}
		
		public static void writeUserPhone(String userPhone)
		{
			userPreferences.edit().putString("userPhone", userPhone).commit();
		}
		public static String readUserPhone() 
		{
			return userPreferences.getString("userPhone", "");
		}
		
		public static void writeUserPassword(String psw)
		{
			userPreferences.edit().putString("userPasssword", psw).commit();
		}
		
		public static String readUserPassword() 
		{
			return userPreferences.getString("userPasssword", "");
		}
		
		public static void writeUserId(String info)
		{
			userPreferences.edit().putString("userId", info).commit();
		}
		
		public static String readUserId() 
		{
			return userPreferences.getString("userId", "");
		}
		
		public static void writeUserInfo(String info)
		{
			userPreferences.edit().putString("userInfo", info).commit();
		}
		
		public static String readUserInfo() 
		{
			return userPreferences.getString("userInfo", "");
		}
		
		public static User getUserInfo() 
		{
			String userJson = readUserInfo();
			User userInfo = json2Object(userJson, User.class);
			if (userInfo == null)
			{
				return null;
			}
			return userInfo;
		}
	
		public static String getVersionName() {
			  PackageManager pm = mContext.getPackageManager();
				try {
					PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), 0);
					return pi.versionName;  
					
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				} 
				return "";
		  }
	  public static void LOG_D(Class<?> paramClass, String paramString)
	  {
		  if (DEBUG) {
			  String str = paramClass.getName();
			    if (str != null)
			    {
			      str = str.substring(1 + str.lastIndexOf("."));
			    }
			    int i = paramString.length();
			    if (i > LOG_SIZE_LIMIT)
			    {
			      int j = 0;
			      int k = 1 + i / LOG_SIZE_LIMIT;
			      while (j < k + -1)
			      {
			        Log.d(LOG_TAG, paramString.substring(j * LOG_SIZE_LIMIT, LOG_SIZE_LIMIT * (j + 1)));
			        j++;
			      }
			      Log.d(LOG_TAG, paramString.substring(j * LOG_SIZE_LIMIT, i));
			    }
			    else {
			      Log.d(LOG_TAG, str + " -> " + paramString);
			    }
		  }
	    
	  }
	  
	  
	  public static void LOG_E(Class<?> paramClass, String paramString)
	  {
			  String str = paramClass.getName();
			  Log.e(LOG_TAG, str + " -> " + paramString);
	  }
	  
	  /**
	   * 调用web服务器的入参，转换成json
	   * @param methodName web服务的接口名称
	   * @param keys 参数的名称
	   * @param values 参数值
	   * @return
	   */
	  	public static String convert2Json (String methodName, String[] keys, Object[] values) {
  		    StringBuilder builder = new StringBuilder();
  			builder.append("{");
  			builder.append("channel:'Q',");
  			builder.append("channelType:'ANDROID',");
  			builder.append("securityCode:'0000000000',");
  			builder.append("serviceType:"+ "'" + methodName + "',");
  			builder.append("params:{");
  			if (keys != null && values != null) {
  		    int keyLen = keys.length ;
  		    int valueLen = values.length;
  		    if (keyLen == valueLen) {
	  			for (int i=0; i < keyLen; i++) 
	  			{
	  				if (i == keyLen-1) {
	  					if (values[i] instanceof String) {
		  					builder.append("'"+keys[i]+"':'" + values[i] + "'");
		  				} else {
		  					builder.append("'"+keys[i]+"':" + values[i] + "");
		  				}
	  					break;
	  				}
	  				if (values[i] instanceof String) {
	  					builder.append("'"+keys[i]+"':'" + values[i] + "',");
	  				} else {
	  					builder.append("'"+keys[i]+"':" + values[i] + ",");
	  				}
	  			}
  		    }
  		    else {
  		    	HealthUtil.LOG_D(HealthUtil.class, "convert2Json--http--->keys.lenght != values.lenght");
  		    }
  			}
  		    builder.append("},");
  			builder.append("rtnDataFormatType:'json'");
  			builder.append("}");
  			Log.d("convert2Json", builder.toString());
		return builder.toString();
	}
	  	
	 public static RequestParams getRequestParams(String methodName, String[] keys, Object[] values) {
		 String paramValue =  convert2Json(methodName, keys, values);
		 RequestParams requestParams = new RequestParams("UTF-8");
		 BasicNameValuePair nameValuePair = new BasicNameValuePair("param", paramValue);
		 requestParams.addBodyParameter(nameValuePair);
		 HealthUtil.LOG_D(HealthUtil.class, "url param=" + paramValue);
		 return requestParams;
	 }
	  	
	  	/*JSON解析*/
	  public static <T> T json2Object(String json,Class<T> clazz) {
		  if (json == null) {
			  return null;
		  }
		 String[] tempStr = json.split("\n");
		 if (tempStr != null && tempStr.length > 0) {
			 json = tempStr[tempStr.length - 1];
		 }
		  GsonBuilder builder = new GsonBuilder();   
        // 不转换没有 @Expose 注解的字段   
        builder.excludeFieldsWithoutExposeAnnotation();  
        Gson gson = builder.create();
        T object = null;
		try {
			object = gson.fromJson(json, clazz);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		}
        return object;

	  }
	  
	  //根据jsonobject的key获取value ，返回类型为String
	  public static String getJSONObjectKeyVal(JSONObject object, String key)
	  {
		if(object == null)
		{
		    return "";
		}
		if(key == null)
		{
		    return "";
		}
		String result = null;
		Object obj;
		try {
			obj = object.get(key);
			if (obj == null)
			{
				result = "";
			}
			else
			{
				result = obj.toString();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	  }
	  
	  /**  
      * 验证手机号码  
      * @param mobiles 手机号
      * @return  [0-9]{5,9}  
      */  
     public static boolean isMobileNum(String phoneNum){
    	 boolean flag = false;
    	 try{
    		 Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");  
    		 Matcher m = p.matcher(phoneNum);
    		 flag = m.matches();
    	 }catch(Exception e){
    		 flag = false;
    	 }
    	 return flag;
     }
    /**
     * 自定义Toast
     * @param activity
     * @param msg
     */
	public static void infoAlert(Activity activity,String msg) {
		Toast toast = Toast.makeText(activity, msg, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
	
	/**
	 * 获取sd卡的路径
	 * 
	 * @return 路径的字符串
	 */
	public static String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取外存目录
		} else {
			sdDir = Environment.getDownloadCacheDirectory();
		}
		if (sdDir == null) {
			return "";
		}
		return sdDir.toString();
	}
	
	public static String getDownloadPath() {
		String sdPath = getSDPath();
		if (!sdPath.endsWith(File.separator)) {
			sdPath += File.separator;
		}
		
		File tessdata = new File(sdPath + "tessdata");
		if (!tessdata.exists() || !tessdata.isDirectory()) {
			boolean b = tessdata.mkdirs();
			LOG_D(HealthUtil.class, "----b=" + b);
		}
		
		return tessdata.getAbsolutePath().endsWith(File.separator)?tessdata.getAbsolutePath():tessdata.getAbsolutePath() + File.separator;
	}
	
	
	/**
     * 获取网落图片资源 
     * @param url
     * @return
     */
    public static Bitmap getHttpBitmap(String url)
    {
        URL myFileURL;
        Bitmap bitmap=null;
        try{
            myFileURL = new URL(url);
            //获得连接
            HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();
            //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            conn.setConnectTimeout(6000);
            //连接设置获得数据流
            conn.setDoInput(true);
            //不使用缓存
            conn.setUseCaches(false);
            //这句可有可无，没有影响
            //conn.connect();
            //得到数据流
            InputStream is = conn.getInputStream();
            //解析得到图片
            bitmap = BitmapFactory.decodeStream(is);
            //关闭数据流
            is.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return bitmap;
    }
    
    
    //解析支付宝支付以后返回的结果
  	public static Map<String,String> parserAliResult(String result){
  		String [] array = result.split(";");
  		Map<String,String> map = new HashMap<String,String>();
  		for(String s : array){
  			int index = s.indexOf("=");
  			String key = s.substring(0, index);
  			String value = "";
  			if(!s.substring(index+1).equals("{}")){
  				value = s.substring(index+2, s.length()-1);
  			}
  			map.put(key, value);
  		}
  		return map;
  	}
  	
  	//解析支付宝支付以后返回的结果中result的部分
  	public static Map<String,String> parserAliPartner(String result){
  		String [] array = result.split("&");
  		Map<String,String> map = new HashMap<String,String>();
  		for(String s : array){
  			int index = s.indexOf("=");
  			String key = s.substring(0, index);
  			String value = "";
  			if(!s.substring(index+1).equals("''")){
  				value = s.substring(index+2, s.length()-1);
  			}
  			map.put(key, value);
  		}
  		return map;
  	}
  	

	/**
	 * 关闭下载悬浮窗
	 */
	public static void removeView() {
		if (viewAdded) {
			appWindowManager.removeView(downloadFloatView);
			viewAdded = false;
		}
	}
	
	/**
	 * 创建下载悬浮窗
	 */
	public static void createFloatView() {
		downloadFloatView = LayoutInflater.from(mContext).inflate(R.layout.download_notification_view, null);
		appWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		
		appLayoutParams = new LayoutParams(100, 100, LayoutParams.TYPE_PHONE,LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSPARENT);
		appLayoutParams.gravity = Gravity.RIGHT|Gravity.BOTTOM;
		
		downloadFloatView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
				mContext.startActivity(intent);
			}
		});
	}
	/**
	 * 刷新下载悬浮窗 
	 */
	public static void refresh() { 
		if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			appLayoutParams.y = 10;
			appLayoutParams.x = 10;
		}else if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			appLayoutParams.y = 120;
			appLayoutParams.x = 10;
		}
        // 如果已经添加了就只更新view  
        if (viewAdded) {  
        	appWindowManager.updateViewLayout(downloadFloatView, appLayoutParams);  
        } else {  
        	appWindowManager.addView(downloadFloatView, appLayoutParams);  
            viewAdded = true;  
        }  
    }
	
	  public static void startDownload(String url,Context activity,String fileName,String fileSize,Short isUpdate) {
		  if (detectSdcardIsExist()) {
			  Float tempFileSize = getFileSize(fileSize);
			  if (tempFileSize <= 0) {
				  tempFileSize = 2f * 1024 * 1024;
			  }
			  if (isAvaiableSpace(tempFileSize)) {
				  Intent intent = new Intent(activity,DownloadService.class);
				  intent.putExtra("download_uri",url);
				  intent.putExtra("is_update",isUpdate);
				  intent.putExtra("file_name",fileName);
				  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				  mContext.startService(intent);
			  } else {
				  Toast.makeText(mContext, "存储卡空间不足", Toast.LENGTH_SHORT).show();
			  }
		  } else {
			  Toast.makeText(mContext, "请检查存储卡是否安装", Toast.LENGTH_SHORT).show();
		  }
	  }
	  
	  public static Float getFileSize(String fileSize) {
		  fileSize = fileSize.toLowerCase().trim();
		  String size = "0";
		  try {
			if (fileSize.endsWith("kb")) {
				size = fileSize.substring(0, fileSize.length()-2);
				return Float.parseFloat(size) * 1024;
			} else if (fileSize.endsWith("mb")) {
				size = fileSize.substring(0, fileSize.length()-2);
				return Float.parseFloat(size) * 1024 * 1024;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0F;
	  }
	  
	  /*判断Sdcard是否存在*/
	  public static boolean detectSdcardIsExist() {
		String extStorageState = Environment.getExternalStorageState();
		 File file = Environment.getExternalStorageDirectory();
		  if (!Environment.MEDIA_MOUNTED.equals(extStorageState) ||
				  !file.exists() || !file.canWrite() || file.getFreeSpace() <= 0) {
			  return false;
		  } 
		return true;
	  }
	  
	  /*判断存储空间大小是否满足条件*/
	  public static boolean isAvaiableSpace(float sizeByte) { 
	        boolean ishasSpace = false; 
	        if (android.os.Environment.getExternalStorageState().equals( 
	                android.os.Environment.MEDIA_MOUNTED)) { 
	            String sdcard = Environment.getExternalStorageDirectory().getPath(); 
	            StatFs statFs = new StatFs(sdcard); 
	            long blockSize = statFs.getBlockSize(); 
	            long blocks = statFs.getAvailableBlocks(); 
	            float availableSpare = blocks * blockSize; 
	            if (availableSpare > (sizeByte + 1024*1024)) { 
	                ishasSpace = true; 
	            } 
	        } 
	        return ishasSpace; 
	   } 
}
