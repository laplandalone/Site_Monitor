package com.site.tools;

import android.os.Environment;

import com.umeng.socialize.bean.SHARE_MEDIA;

public class HealthConstant
{
	//hiseemedical.com 
 	public static final String URL ="http://op.yg84.com:7000/universe/car/nearby?";
 	
 	public static final String URL_citylist ="http://op.yg84.com:7000/universe/car/citylist?";
 	
 	public static final String URL_lines =   "http://op.yg84.com:7000/universe/car/lines?";
 	
 	public static final String URL_lineName ="http://op.yg84.com:7000/universe/car/linename?";
 	
 	public static final String URL_query ="http://op.yg84.com:7000/universe/car/query?";
 	
//	public static final String URL ="http://192.168.1.106:7001/mobile.htm?method=axis";
//	public static final String URL ="http://www.hiseemedical.com:10821/mobile.htm?method=axis";
	
//	public static final String HIS_URL="http://27.17.0.42:10821/his/mobile.htm?method=axis";
	public static final String HIS_URL="http://mobilemedical.net.cn:10821/his/mobile.htm?method=axis";
//	public static final String UPLOAD_URL = "http://192.168.137.1:7001/fileUpload";
	
//	public static final String UPLOAD_URL = "http://123.57.78.38:10841/fileUpload";
//	public static final String UPLOAD_URL = "http://58.53.209.120:9100/fileUpload";
	public static final String UPLOAD_URL = "http://www.hiseemedical.com:10821/fileUpload";
	
//	public static final String LAUGUAGE_PACKAGE = "http://61.183.0.37:7170/apkpackages/chi_sim.traineddata";
	public static final String IMG_PATH = Environment.getExternalStorageDirectory().getPath()+"/hbgzocr/";
	
	public static final String Download_path = Environment.getExternalStorageDirectory().getPath() + "/health/download/";
	
	public static final String imgUrl = "http://www.hiseemedical.com:10821/ImgWeb/photo/";
	
	public static final String question_img_Url = "http://www.hiseemedical.com:10821";
	
	public static final String Download_Url = "http://www.hiseemedical.com:10821/file.htm?method=downloadFile";

	@SuppressWarnings("rawtypes")
	public static final Enum WECHAT = SHARE_MEDIA.WEIXIN;  //微信分享名称
	
	public static  final String PUSH_KEY = "EHKN6qSTGpmEWN0uXk85LWGO"; //push key
	
	public static Boolean isNewMessage = false;
}
