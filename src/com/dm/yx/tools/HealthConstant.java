package com.dm.yx.tools;

import android.os.Environment;

import com.umeng.socialize.bean.SHARE_MEDIA;

public class HealthConstant
{
	//hiseemedical.com
 	public static final String URL ="http://192.168.137.1:7001/mobile.htm?method=axis";
//	public static final String URL ="http://58.53.209.120:9100/mobile.htm?method=axis";
//	public static final String URL ="http://58.53.209.107:10821/mobile.htm?method=axis";
//	public static final String URL ="http://58.53.209.107:10821/mobile.htm?method=axis";
	
//	public static final String UPLOAD_URL = "http://192.168.137.1:7001/fileUpload";
//	public static final String UPLOAD_URL = "http://58.53.209.120:9100/fileUpload";
	public static final String UPLOAD_URL = "http://58.53.209.107:10821/fileUpload";
	
//	public static final String LAUGUAGE_PACKAGE = "http://61.183.0.37:7170/apkpackages/chi_sim.traineddata";
	public static final String IMG_PATH = Environment.getExternalStorageDirectory().getPath()+"/hbgzocr/";
	
	public static final String Download_path = Environment.getExternalStorageDirectory().getPath() + "/health/download/";
	
	public static final String imgUrl = "http://58.53.209.107:10821/ImgWeb/photo/";
	
	public static final String question_img_Url = "http://58.53.209.107:10821";

	@SuppressWarnings("rawtypes")
	public static final Enum WECHAT = SHARE_MEDIA.WEIXIN;  //微信分享名称
	
	public static  final String PUSH_KEY = "EHKN6qSTGpmEWN0uXk85LWGO"; //push key
}
