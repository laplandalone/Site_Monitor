package com.site.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

import android.R.string;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.RequestParams;
import com.site.application.RegApplication;
import com.site.model.Cancel;
import com.site.model.Line;

/**
 * 
 * 工具类
 * 
 */
public class SiteUtil {

	public static final boolean DEBUG = true;
	private static final int LOG_SIZE_LIMIT = 3500;
	public static final String LOG_TAG = "Site_Monitor";
	private static SharedPreferences userPreferences;

	private static WindowManager appWindowManager;
	private static View downloadFloatView;
	private static boolean viewAdded = false;// 透明窗体是否已经显示
	private static WindowManager.LayoutParams appLayoutParams;
	private static Context mContext = RegApplication.getInstance();
	public static boolean isNewVersionFlag = false;

	public static void worningAlert(Activity activity, String msg) {
		Toast toast = Toast.makeText(activity, msg, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	/**
	 * 下载悬浮窗显示标志，是否有下载在进行
	 */
	public static boolean downloading = false;// 是否有下载在进行
	static {
		if (userPreferences == null) {
			userPreferences = PreferenceManager
					.getDefaultSharedPreferences(mContext);
		}
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

	public static void LOG_D(Class<?> paramClass, String paramString) {
		if (DEBUG) {
			String str = paramClass.getName();
			if (str != null) {
				str = str.substring(1 + str.lastIndexOf("."));
			}
			int i = paramString.length();
			if (i > LOG_SIZE_LIMIT) {
				int j = 0;
				int k = 1 + i / LOG_SIZE_LIMIT;
				while (j < k + -1) {
					Log.d(LOG_TAG, paramString.substring(j * LOG_SIZE_LIMIT,
							LOG_SIZE_LIMIT * (j + 1)));
					j++;
				}
				Log.d(LOG_TAG, paramString.substring(j * LOG_SIZE_LIMIT, i));
			} else {
				Log.d(LOG_TAG, str + " -> " + paramString);
			}
		}

	}

	public static void LOG_E(Class<?> paramClass, String paramString) {
		String str = paramClass.getName();
		Log.e(LOG_TAG, str + " -> " + paramString);
	}

	/**
	 * 调用web服务器的入参，转换成json
	 * 
	 * @param methodName
	 *            web服务的接口名称
	 * @param keys
	 *            参数的名称
	 * @param values
	 *            参数值
	 * @return
	 */
	public static String convert2Json(String methodName, String[] keys,
			Object[] values) {
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		builder.append("channel:'Q',");
		builder.append("channelType:'ANDROID',");
		builder.append("securityCode:'0000000000',");
		builder.append("serviceType:" + "'" + methodName + "',");
		builder.append("params:{");
		if (keys != null && values != null) {
			int keyLen = keys.length;
			int valueLen = values.length;
			if (keyLen == valueLen) {
				for (int i = 0; i < keyLen; i++) {
					if (i == keyLen - 1) {
						if (values[i] instanceof String) {
							builder.append("'" + keys[i] + "':'" + values[i]
									+ "'");
						} else {
							builder.append("'" + keys[i] + "':" + values[i]
									+ "");
						}
						break;
					}
					if (values[i] instanceof String) {
						builder.append("'" + keys[i] + "':'" + values[i] + "',");
					} else {
						builder.append("'" + keys[i] + "':" + values[i] + ",");
					}
				}
			} else {
				SiteUtil.LOG_D(SiteUtil.class,
						"convert2Json--http--->keys.lenght != values.lenght");
			}
		}
		builder.append("},");
		builder.append("rtnDataFormatType:'json'");
		builder.append("}");
		Log.d("convert2Json", builder.toString());
		return builder.toString();
	}

	public static RequestParams getRequestParams(String methodName,
			String[] keys, Object[] values) {
		String paramValue = convert2Json(methodName, keys, values);
		RequestParams requestParams = new RequestParams("UTF-8");
		BasicNameValuePair nameValuePair = new BasicNameValuePair("param",
				paramValue);
		requestParams.addBodyParameter(nameValuePair);
		SiteUtil.LOG_D(SiteUtil.class, "url param=" + paramValue);
		return requestParams;
	}

	/* JSON解析 */
	public static <T> T json2Object(String json, Class<T> clazz) {
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

	// 根据jsonobject的key获取value ，返回类型为String
	public static String getJSONObjectKeyVal(JSONObject object, String key) {
		if (object == null) {
			return "";
		}
		if (key == null) {
			return "";
		}
		String result = null;
		Object obj;
		try {
			obj = object.get(key);
			if (obj == null) {
				result = "";
			} else {
				result = obj.toString();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 验证手机号码
	 * 
	 * @param mobiles
	 *            手机号
	 * @return [0-9]{5,9}
	 */
	public static boolean isMobileNum(String phoneNum) {
		boolean flag = false;
		try {
			Pattern p = Pattern
					.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
			Matcher m = p.matcher(phoneNum);
			flag = m.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	/**
	 * 自定义Toast
	 * 
	 * @param activity
	 * @param msg
	 */
	public static void infoAlert(Activity activity, String msg) {
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
			LOG_D(SiteUtil.class, "----b=" + b);
		}

		return tessdata.getAbsolutePath().endsWith(File.separator) ? tessdata
				.getAbsolutePath() : tessdata.getAbsolutePath()
				+ File.separator;
	}

	/**
	 * 获取网落图片资源
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap getHttpBitmap(String url) {
		URL myFileURL;
		Bitmap bitmap = null;
		try {
			myFileURL = new URL(url);
			// 获得连接
			HttpURLConnection conn = (HttpURLConnection) myFileURL
					.openConnection();
			// 设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
			conn.setConnectTimeout(6000);
			// 连接设置获得数据流
			conn.setDoInput(true);
			// 不使用缓存
			conn.setUseCaches(false);
			// 这句可有可无，没有影响
			// conn.connect();
			// 得到数据流
			InputStream is = conn.getInputStream();
			// 解析得到图片
			bitmap = BitmapFactory.decodeStream(is);
			// 关闭数据流
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	// 解析支付宝支付以后返回的结果
	public static Map<String, String> parserAliResult(String result) {
		String[] array = result.split(";");
		Map<String, String> map = new HashMap<String, String>();
		for (String s : array) {
			int index = s.indexOf("=");
			String key = s.substring(0, index);
			String value = "";
			if (!s.substring(index + 1).equals("{}")) {
				value = s.substring(index + 2, s.length() - 1);
			}
			map.put(key, value);
		}
		return map;
	}

	// 解析支付宝支付以后返回的结果中result的部分
	public static Map<String, String> parserAliPartner(String result) {
		String[] array = result.split("&");
		Map<String, String> map = new HashMap<String, String>();
		for (String s : array) {
			int index = s.indexOf("=");
			String key = s.substring(0, index);
			String value = "";
			if (!s.substring(index + 1).equals("''")) {
				value = s.substring(index + 2, s.length() - 1);
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

		appWindowManager = (WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE);

		appLayoutParams = new LayoutParams(100, 100, LayoutParams.TYPE_PHONE,
				LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSPARENT);
		appLayoutParams.gravity = Gravity.RIGHT | Gravity.BOTTOM;

		downloadFloatView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(
						DownloadManager.ACTION_VIEW_DOWNLOADS);
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
		} else if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			appLayoutParams.y = 120;
			appLayoutParams.x = 10;
		}
		// 如果已经添加了就只更新view
		if (viewAdded) {
			appWindowManager.updateViewLayout(downloadFloatView,
					appLayoutParams);
		} else {
			appWindowManager.addView(downloadFloatView, appLayoutParams);
			viewAdded = true;
		}
	}

	public static Float getFileSize(String fileSize) {
		fileSize = fileSize.toLowerCase().trim();
		String size = "0";
		try {
			if (fileSize.endsWith("kb")) {
				size = fileSize.substring(0, fileSize.length() - 2);
				return Float.parseFloat(size) * 1024;
			} else if (fileSize.endsWith("mb")) {
				size = fileSize.substring(0, fileSize.length() - 2);
				return Float.parseFloat(size) * 1024 * 1024;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0F;
	}

	/* 判断Sdcard是否存在 */
	public static boolean detectSdcardIsExist() {
		String extStorageState = Environment.getExternalStorageState();
		File file = Environment.getExternalStorageDirectory();
		if (!Environment.MEDIA_MOUNTED.equals(extStorageState)
				|| !file.exists() || !file.canWrite()
				|| file.getFreeSpace() <= 0) {
			return false;
		}
		return true;
	}

	/* 判断存储空间大小是否满足条件 */
	public static boolean isAvaiableSpace(float sizeByte) {
		boolean ishasSpace = false;
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			String sdcard = Environment.getExternalStorageDirectory().getPath();
			StatFs statFs = new StatFs(sdcard);
			long blockSize = statFs.getBlockSize();
			long blocks = statFs.getAvailableBlocks();
			float availableSpare = blocks * blockSize;
			if (availableSpare > (sizeByte + 1024 * 1024)) {
				ishasSpace = true;
			}
		}
		return ishasSpace;
	}

	public static Bitmap compressBitmapT(String path, String name) {
		if (path != null) {
			File file = new File(path);
			if (file.exists()) {
				BitmapFactory.Options opts = new BitmapFactory.Options();
				// opts.inJustDecodeBounds = true;
				opts.inSampleSize = 1;
				long size = file.length() / 1024;
				if (size > 3000) {
					opts.inSampleSize = 10;
				} else if (size > 1000 && size < 3000) {
					opts.inSampleSize = 6;
				} else if (size > 500 && size < 1000) {
					opts.inSampleSize = 5;
				}
				Bitmap bitmap = BitmapFactory.decodeFile(file.getPath(), opts);

				if (bitmap == null) // 若获取图片失败就取消压缩
				{
					return null;
				}

				saveFileBitmap(name, bitmap, 100);
				return bitmap;
			}
		}
		return null;
	}

	public static Bitmap compressBitmap(String path, String name) {
		if (path != null) {
			File file = new File(path);
			if (file.exists()) {
				BitmapFactory.Options opts = new BitmapFactory.Options();

				Bitmap image = BitmapFactory.decodeFile(file.getPath(), opts);

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
				int options = 100;
				while (baos.toByteArray().length / 1024 > 700) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
					baos.reset();// 重置baos即清空baos
					image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
					options -= 10;// 每次都减少10
				}

				ByteArrayInputStream isBm = new ByteArrayInputStream(
						baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
				Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
				saveFileBitmap(name, bitmap, 100);
				return image;
			}
		}
		return null;
	}

	public static void saveFileBitmap(String fileName, Bitmap bitmap,
			int compressFactor) {
		if (bitmap == null) {
			return;
		}

		File destDir = new File(Constant.IMG_PATH);
		if (!destDir.exists()) {
			destDir.mkdirs();
		}

		FileOutputStream outputStream;
		try {
			outputStream = new FileOutputStream(new File(Constant.IMG_PATH
					+ fileName));
			bitmap.compress(Bitmap.CompressFormat.PNG, compressFactor,
					outputStream);
			outputStream.flush();
			outputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void saveBitmap(Bitmap bitmap, String fileName) {
		if (bitmap == null) {
			return;
		}

		File destDir = new File(Constant.IMG_PATH);
		if (!destDir.exists()) {
			destDir.mkdirs();
		}

		FileOutputStream outputStream;
		try {
			outputStream = new FileOutputStream(new File(Constant.IMG_PATH
					+ fileName));
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
			outputStream.flush();
			outputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 纬度
	 * 
	 * @return
	 */
	public static String getLatitude() {
		return userPreferences.getString("lbs_location_Latitude", "30.275079");
	}

	public static void writeLatitude(String location) {
		userPreferences.edit().putString("lbs_location_Latitude", location)
				.commit();
	}

	/**
	 * 经度
	 * 
	 * @return
	 */
	public static String getLongitude() {
		return userPreferences
				.getString("lbs_location_Longitude", "120.154724");
	}

	public static void writeLongitude(String location) {
		userPreferences.edit().putString("lbs_location_Longitude", location)
				.commit();
	}

	public static String getStopId() {
		return userPreferences.getString("stopId", "");
	}

	public static void writeStopId(String stopId) {
		userPreferences.edit().putString("stopId", stopId).commit();
	}

	public static String getCity() {
		return userPreferences.getString("city", "");
	}

	public static void writeCity(String city) {
		userPreferences.edit().putString("city", city).commit();
	}

	public static String getCityName() {
		return userPreferences.getString("cityName", "");
	}

	public static void writeCityName(String city) {
		userPreferences.edit().putString("cityName", city).commit();
	}

	public static String getAddress() {
		return userPreferences.getString("address", "");
	}

	public static void writeAddress(String address) {
		userPreferences.edit().putString("address", address).commit();
	}

	public static String getLineIds() {
		return userPreferences.getString("lineIds", "");
	}

	public static void writeLineIds(String lineIds) {
		userPreferences.edit().putString("lineIds", lineIds).commit();
	}

	public static String getStopName() {
		return userPreferences.getString("stopName", "");
	}

	public static void writeStopName(String stopName) {
		userPreferences.edit().putString("stopName", stopName).commit();
	}

	public static void writeCancels(Cancel cancelT, String flag) {
		String cancelStr = userPreferences.getString("cancels", "");
		Gson gson = new Gson();
		List<Cancel> cancelsT = new ArrayList<Cancel>();
		List<Cancel> cancels = gson.fromJson(cancelStr,
				new TypeToken<List<Cancel>>() {
				}.getType());
		if (cancels != null) {
			if ("delete".equals(flag)) {
				for (Cancel cancel : cancels) {
					if (!cancelT.getCancelId().equals(cancel.getCancelId())) {
						cancelsT.add(cancel);
					}
				}
				cancels = cancelsT;
			}
		} else {
			cancels = new ArrayList<Cancel>();
		}
		if ("add".equals(flag)) {
			cancels.add(cancelT);
		}
		userPreferences.edit().putString("cancels", gson.toJson(cancels))
				.commit();
	}

	public static List<Cancel> readCancels() {
		String cancelStr = userPreferences.getString("cancels", "");
		Gson gson = new Gson();
		List<Cancel> cancels = gson.fromJson(cancelStr,
				new TypeToken<List<Cancel>>() {
				}.getType());
		return cancels;
	}

	/**
	 * 压缩图片质量,返回图片名字
	 * 
	 * @param activity
	 * @param uri
	 * @return author
	 */
	public static String compressBitmap(Activity activity, String path) {
		File file = new File(path);
		if (!file.exists()) {
			return null;
		}
		if (file.length() / 1024 <= 200) {
			return file.getName();
		}
		Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
		if (bitmap == null) { // 若获取图片失败就取消压缩
			return file.getName();
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		// 压缩质量因数
		// 越低代表图片质量越差，但图片质量会减小
		// 为100时不减少图片质量
		// 经测试，从60开始比较不错
		int options = 60;
		// 这里的200只是一个参考数
		// 压缩后的图片质量在此值左右浮动，浮动值很小
		while (baos.toByteArray().length / 1024 > 200) {
			baos.reset();
			options -= 1;
			if (options == 1) {
				break;
			}
			bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
		}
		// 注意：
		// 这里千万别用BitmapFactory.decodeStream(is)去生成Bitmap后再去保存
		// 图片稍微大一点就会OOM
		try {
			// 压缩后的图片覆盖掉之前的图片
			FileOutputStream fos = new FileOutputStream(path);
			fos.write(baos.toByteArray());
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file.getName();
	}

	/**
	 * 压缩图片质量,返回图片名字
	 * 
	 * @param activity
	 * @param uri
	 * @return author
	 */
	public static void compressBitmap(Bitmap bitmap, String path) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		// 压缩质量因数
		// 越低代表图片质量越差，但图片质量会减小
		// 为100时不减少图片质量
		// 经测试，从60开始比较不错
		int options = 60;
		// 这里的200只是一个参考数
		// 压缩后的图片质量在此值左右浮动，浮动值很小
		while (baos.toByteArray().length / 1024 > 200) {
			baos.reset();
			options -= 1;
			if (options == 1) {
				break;
			}
			bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
		}
		// 注意：
		// 这里千万别用BitmapFactory.decodeStream(is)去生成Bitmap后再去保存
		// 图片稍微大一点就会OOM
		try {
			// 压缩后的图片覆盖掉之前的图片
			FileOutputStream fos = new FileOutputStream(path);
			fos.write(baos.toByteArray());
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
