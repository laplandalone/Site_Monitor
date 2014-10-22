package com.dm.yx.tools;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.dm.yx.application.RegApplication;
import com.dm.yx.model.HospitalT;
import com.dm.yx.model.User;
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

	  private static Context mContext = RegApplication.getInstance();
	  public static boolean isNewVersionFlag = false ;
		static 
		{
			if (userPreferences == null)
			{
			  userPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
			}
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
}
