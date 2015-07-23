package com.site.upload;

import java.util.HashMap;
import java.util.Map;

import android.os.Handler;
import android.os.Message;

import com.site.tools.Constant;

public class UploadThread implements Runnable {

	private FormFile[] formFile;
	private Handler mHandler;
	private String cityId;
	private String linelist;
	private String carNo;
	private String stopName;
	private String stopId;
	private String jingdu;
	private String weidu;

	public UploadThread(FormFile[] formFile, Handler mHandler, String cityId,
			String linelist, String carNo, String stopName, String stopId,
			String jingdu, String weidu) {
		super();
		this.formFile = formFile;
		this.mHandler = mHandler;
		this.cityId = cityId;
		this.linelist = linelist;
		this.carNo = carNo;
		this.stopName = stopName;
		this.stopId = stopId;
		this.jingdu = jingdu;
		this.weidu = weidu;
	}

	@Override
	public void run() {

		String result = uploadFile(formFile);
		Message msg = mHandler.obtainMessage();
		if (msg != null) {
			msg.obj = result;
			msg.arg1 = 1001;
			mHandler.sendMessage(msg);
		}
	}

	/**
	 * 上传图片到服务器
	 * 
	 * @param imageFile
	 *            包含路径
	 */
	public String uploadFile(FormFile[] formFiles) {
		try {

			// 请求普通信息
			Map<String, String> params = new HashMap<String, String>();
			params.put("cityId", cityId);
			params.put("linelist", linelist);
			params.put("stopName", stopName);
			params.put("stopId", stopId);
			params.put("jingdu", jingdu);
			params.put("weidu", weidu);

			String result = SocketHttpRequester.post(Constant.UPLOAD_URL,
					params, formFiles);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
