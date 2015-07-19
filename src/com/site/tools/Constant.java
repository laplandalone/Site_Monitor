package com.site.tools;

import android.os.Environment;

import com.umeng.socialize.bean.SHARE_MEDIA;

public class Constant
{
	//hiseemedical.com 
 	public static final String URL ="http://op.yg84.com:7000/universe/car/nearby?";
 	
 	public static final String URL_citylist ="http://op.yg84.com:7000/universe/car/citylist?";
 	
 	public static final String URL_lines =   "http://op.yg84.com:7000/universe/car/lines?";
 	
 	public static final String URL_lineName ="http://op.yg84.com:7000/universe/car/linename?";
 	
 	public static final String URL_query ="http://op.yg84.com:7000/universe/car/query?";
 	
	public static final String URL_record ="http://op.yg84.com:7000/universe/car/record?";
 	
	public static final String UPLOAD_URL ="http://op.yg84.com:7000/universe/car2/upload?";
	
	public static final String IMG_PATH = Environment.getExternalStorageDirectory().getPath()+"/hbgzocr/";
	
	public static final String Download_path = Environment.getExternalStorageDirectory().getPath() + "/health/download/";
	
	
	public static Boolean isNewMessage = false;
}
