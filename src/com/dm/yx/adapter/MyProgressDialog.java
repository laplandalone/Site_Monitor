package com.dm.yx.adapter;

import android.app.ProgressDialog;
import android.content.Context;

import com.dm.yx.tools.HealthUtil;

public class MyProgressDialog extends ProgressDialog{
	
//	private TimerTask task;
	
	public MyProgressDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MyProgressDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void show() {
		super.show();
//		Timer timer = new Timer(true);
//		final Handler mHandler = new Handler() {
//
//			@Override
//			public void handleMessage(Message msg) {
//				super.handleMessage(msg);
//				switch(msg.arg1) {
//					case 1:
//					setMessage("����ʱ��������ĵȴ�");
//				}
//			}
//		};
//		task = new TimerTask() {
//			
//			@Override
//			public void run() {
//				Message msg = new Message();
//				msg.arg1 = 1;
//				mHandler.sendMessage(msg);
//				
//			}
//		};
//		timer.schedule(task, 10000);
	}
	
	@Override
	public void cancel() {
//		cancelTimer();
		super.cancel();
	}

	@Override
	public void dismiss() {
//		cancelTimer();
		HealthUtil.LOG_D(getClass(), "dialog cancel , and stop connected webservice");
//		AccessWebServiceCtrlImpl.stopAccessWebService();
		if (isShowing()) {
			super.dismiss();
		}
	}

	@Override
	public void onBackPressed() {
//		cancelTimer();
		super.onBackPressed();
		
	}
	
//	private void cancelTimer() {
//		TelecomEPUtility.LOG_D(getClass(), "cancelTimer");
//		if (task != null) {
//			task.cancel();
//		}
//	}
	

}
