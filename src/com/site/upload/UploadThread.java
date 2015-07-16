package com.site.upload;

import java.util.HashMap;
import java.util.Map;

import android.os.Handler;
import android.os.Message;

import com.site.tools.HealthConstant;
import com.site.tools.HealthUtil;

public class UploadThread implements Runnable{

	
	private FormFile[] formFile;
	private Handler mHandler;
	private String questionT;
	private String hospitalId;
	private String uploadType;
	private String userId;
	private String visitType;
	

	public UploadThread(FormFile[] formFile, Handler mHandler,
			String questionT,String hospitalId,String uploadType,String userId,String visitType) {
		super();
		this.formFile = formFile;
		this.mHandler = mHandler;
		this.questionT = questionT;
		this.hospitalId=hospitalId;
		this.uploadType=uploadType;
		this.userId=userId;
		this.visitType=visitType;
	}

	@Override
	public void run() {

			String result = uploadFile(formFile);
			Message msg= mHandler.obtainMessage();
			if (msg != null) {
				msg.obj = result;
				msg.arg1 = 1001;
				mHandler.sendMessage(msg);
			}
	}
	
	/**
     * 上传图片到服务器
     * 
     * @param imageFile 包含路径
     */
    public String uploadFile(FormFile[] formFiles) {
        try {
            

        	 
            //请求普通信息
            Map<String, String> params = new HashMap<String, String>();
            params.put("method", "uploadFile");
            params.put("questionT", questionT);
            params.put("hospitalId", hospitalId);
            params.put("uploadType", uploadType);
            params.put("userId", userId);
            params.put("visitType", visitType);
            HealthUtil.LOG_D(getClass(), "url uploadType=" + uploadType);

            String result = SocketHttpRequester.post(HealthConstant.UPLOAD_URL, params, formFiles);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
