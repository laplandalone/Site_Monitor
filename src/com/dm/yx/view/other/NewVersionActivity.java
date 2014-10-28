package com.dm.yx.view.other;

import java.io.File;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.dm.yx.BaseActivity;
import com.dm.yx.R;
import com.dm.yx.tools.HealthConstant;
import com.dm.yx.tools.HealthUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
/**
 * 
* @ClassName: NewVersionActivity 
* @Description: 发现新版本后，显示下载更新界面
* @author yang.jingwen
* @date 2014-6-11 下午2:31:53 
*
*modified by yang.jingwen
*解决当点击home键之后，此界面消失的问题
*注意：当人为的退出此界面时，请设置HealthUtil.isNewVersionFlag = false;
*
 */
public class NewVersionActivity extends BaseActivity implements OnClickListener{
	private Button updateBtn;
	private Button cancelBtn;
	private TextView remarkTxt;
	
	private Bundle mBundle;
	private String mApplicationUrl;
	private String mForceUpdateFlag;
	private String mApplicationVersionCode;
	private HttpUtils mHttpUtils;
	private HttpHandler<File> mHttpHandler;
	
	private boolean isDownloadCompleted = false;

	protected void initView() {
		updateBtn = (Button) findViewById(R.id.new_version_upload);
		cancelBtn = (Button) findViewById(R.id.new_version_cancel);
		remarkTxt = (TextView) findViewById(R.id.new_version_remark);
		updateBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
	}

	protected void initValue() {
		mApplicationVersionCode = mBundle.getString("applicationVersionCode");
		mApplicationUrl = mBundle.getString("applicationUrl");
		mForceUpdateFlag = mBundle.getString("forceUpdateFlag");
		remarkTxt.setText("版本：" + mApplicationVersionCode + "\n更新内容：\n" + mBundle.getString("remark"));
		if (!"N".equalsIgnoreCase(mForceUpdateFlag)) {
			cancelBtn.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_version);
		mHttpUtils = new HttpUtils();
		mBundle = this.getIntent().getExtras();
		HealthUtil.isNewVersionFlag = true; //当此界面显示时，设置为true
		initView();
		initValue();
	}
	
	public void updateBtn() {
		updateBtn.setEnabled(false);
		cancelBtn.setText("取消");
		mHttpHandler = mHttpUtils.download(mApplicationUrl, HealthConstant.Download_path+"DigitalMedical" + mApplicationVersionCode + ".apk", true, new RequestCallBack<File>() {
			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				
				float size = current / 1024F /1024F;
				float totalSize = total / 1024F /1024F;
				int finish=Math.round(size/totalSize*100);
				updateBtn.setText("正在下载(已下载" + finish + "%)");
				super.onLoading(total, current, isUploading);
			}

			@Override
			public void onSuccess(ResponseInfo<File> arg0)
			{
				updateBtn.setText("下载完成,点击安装");
				isDownloadCompleted = true;
				installApkByGuide(HealthConstant.Download_path+"DigitalMedical" + mApplicationVersionCode + ".apk");
				updateBtn.setEnabled(true);
				cancelBtn.setText("下次安装");
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				HealthUtil.infoAlert(NewVersionActivity.this, arg1);
				//code 416 表示下载已经完成，无需再下
				if (arg0 != null && arg0.getExceptionCode() == 416) {
					isDownloadCompleted = true;
					updateBtn.setText("点击安装");
					updateBtn.setEnabled(true);
					installApkByGuide(HealthConstant.Download_path+"DigitalMedical" + mApplicationVersionCode + ".apk");
				} else
				{
					updateBtn.setText("点击重试");
					updateBtn.setEnabled(true);
				}
			}
		});
	}
	
	 private void installApkByGuide(String localFilePath) {
		    Intent intent = new Intent(Intent.ACTION_VIEW);
	        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        intent.setDataAndType(Uri.parse("file://" + localFilePath),"application/vnd.android.package-archive");
	        startActivity(intent);
	  }
	
	public void cancelBtn() {
		HealthUtil.LOG_D(getClass(), ">>>>>>>>>>>>>>>>>>>cancelBtn");
		if (mHttpHandler != null) {
			if(cancelBtn.getText().toString().equals("取消")){
				mHttpHandler.stop();
				mHttpHandler = null;
			}
		}
		HealthUtil.isNewVersionFlag = false; //当此界面销毁，设置为false
		finish();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return false;
	}

	@Override
	public void onBackPressed()
	{
			super.onBackPressed();
	}
	
	@Override
	protected void onDestroy() {
		HealthUtil.LOG_D(getClass(), "--------------------onDestroy");
		if (mHttpHandler != null) {
			mHttpHandler.stop();
			mHttpHandler = null;
		}
		HealthUtil.isNewVersionFlag = false; //当此界面销毁，设置为false
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.new_version_upload:
			if (isDownloadCompleted) {
				installApkByGuide(HealthConstant.Download_path+"DigitalMedical" + mApplicationVersionCode + ".apk");
			} else {
				updateBtn();
			}
			break;
		case R.id.new_version_cancel:
			cancelBtn();
			break;
		}
		
	}

	

}
