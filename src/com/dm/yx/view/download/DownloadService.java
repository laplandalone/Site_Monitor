package com.dm.yx.view.download;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.dm.yx.tools.HealthUtil;
import com.lurencun.android.resource.SDCard;

public class DownloadService extends Service{
	private DownloadManager downloadManager=null;
	private long lastDownload = -1L;
	private String localFileName = "";
	private final String DOWNLOAD_STATE_SUCCESS = "SUCCESS";
	private final String DOWNLOAD_STATE_DELETE = "DELETE";
	private final String DOWNLOAD_STATE_FAIL = "FAILURE";
	private final String DOWNLOAD_STATE_PAUSED = "PAUSED";
	private List<DownloadsChangeObserver> mDownloadObserverList;
	private OnCompleteReceiver onComplete;
	
	private final Uri CONTENT_URI = Uri.parse("content://downloads/my_downloads");
	private final String filePath = "hbgz/file";
	private int startFlag = 0;
	private StringBuffer reasonBuffer;

	private List<String> downloadPromptList = new ArrayList<String>(); 
	
	
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		HealthUtil.removeView();
		HealthUtil.downloading = false;
		unregisterReceiver(onComplete);
		unregisterReceiver(onNotificationClick);
		if (mDownloadObserverList != null && !mDownloadObserverList.isEmpty()) {
			for (DownloadsChangeObserver mDownloadObserver:mDownloadObserverList) {
				this.getContentResolver().unregisterContentObserver(mDownloadObserver);
			}
		}
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		downloadManager=(DownloadManager)getSystemService(DOWNLOAD_SERVICE);
		onComplete = new OnCompleteReceiver();
		if (mDownloadObserverList == null) {
			mDownloadObserverList = new ArrayList<DownloadService.DownloadsChangeObserver>();
		}
		registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
	    registerReceiver(onNotificationClick, new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED));
	    
	    HealthUtil.createFloatView();

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent != null) {
			final String uri = intent.getStringExtra("download_uri");
			localFileName = intent.getStringExtra("file_name");
			  if (!SDCard.IS_MOUNTED) {
				  Toast.makeText(this, "请检查SD卡是否安装", Toast.LENGTH_LONG).show();
			  } else {
//				Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdirs();
				Environment.getExternalStoragePublicDirectory(filePath).mkdirs();
				startDownload(uri);
//				showDownloadManagerView();
			  }
			
		}
		return super.onStartCommand(intent, flags, startId);
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void startDownload(String downloadPath) {
		HealthUtil.LOG_D(getClass(), "startDownload") ;
		HealthUtil.LOG_D(getClass(), "--------downloadPath=" + downloadPath);
//		int startIndex = downloadPath.lastIndexOf("/");
//		remoteFileName = downloadPath.substring(startIndex + 1);
//		HealthUtil.LOG_D(getClass(), "--------localFileName=" + localFileName);
//		if (localFileName == null || "".equals(localFileName)) {
//			localFileName = remoteFileName;
//		}
		/*空格、{}对下载链接有影响，需转义*/
		downloadPath = downloadPath.replace(" ", "%20").replace("{", "%7B").replace("}", "%7D");
	    Uri uri = Uri.parse(downloadPath);
	    HealthUtil.LOG_D(getClass(), " uri=" + uri) ;
	    boolean isDownloading = queryStatus(downloadPath);
	    if (isDownloading) {
	    	HealthUtil.LOG_D(getClass(), "fileName ,download task is running...");
	    	return;
	    }
	    try {
		    DownloadManager.Request request = new DownloadManager.Request(uri);
		    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
	                                DownloadManager.Request.NETWORK_MOBILE);
	        request.setAllowedOverRoaming(true);
	        //根据文件后缀设置mime
	        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
	        int startIndex = localFileName.lastIndexOf(".");
	        String tmpMimeString = localFileName.substring(startIndex + 1).toLowerCase();
//	        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(downloadPath));
	        String mimeString = mimeTypeMap.getMimeTypeFromExtension(tmpMimeString);
	        HealthUtil.LOG_D(getClass(), "--------mimeTypeMap =" + mimeString);
	        request.setMimeType(mimeString);  
	        
	        HealthUtil.LOG_D(getClass(), "--------localFileName=" + localFileName);
	        request.setTitle(localFileName);
	        request.addRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=utf-8");
	        request.setDescription("");
//	        File folder = Environment.getExternalStoragePublicDirectory(filePath);
//	        if (!folder.exists() || !folder.isDirectory()) {
//	        	HealthUtil.LOG_D(getClass(), "folder null");
//	        	folder.mkdirs();
//			}
	        request.setDestinationInExternalPublicDir(filePath,localFileName);
	        request.setVisibleInDownloadsUi(true);
//	        request.setShowRunningNotification(true);
	        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
	        lastDownload = downloadManager.enqueue(request);
	        DownloadsChangeObserver mDownloadObserver = new DownloadsChangeObserver(CONTENT_URI);
	        mDownloadObserver.setLastDownloadId(lastDownload);
	        mDownloadObserverList.add(mDownloadObserver);
		    this.getContentResolver().registerContentObserver(CONTENT_URI, true, mDownloadObserver);
		    HealthUtil.LOG_D(getClass(), "localFileName & lastDownload" + localFileName + "_" + lastDownload) ;
		    startFlag ++;
		    if (!HealthUtil.downloading) {
		    	HealthUtil.downloading = true;
			    HealthUtil.refresh();
			}
		    
	    } catch (IllegalArgumentException e) {
//	    	HealthUtil.worningAlert(this, "下载路径无效");
	    	HealthUtil.LOG_D(getClass(), "IllegalArgumentException , uri=" + uri);
	    }
	  }
	  
	  public boolean queryStatus(String downloadPath) {
	    Cursor c=downloadManager.query(new DownloadManager.Query().setFilterByStatus(DownloadManager.STATUS_RUNNING 
	    		| DownloadManager.STATUS_PAUSED | DownloadManager.STATUS_PENDING));
	    if (c==null) {
//	    	HealthUtil.LOG_D(getClass(), "Download not found!");
	      Toast.makeText(this, "Download not found!", Toast.LENGTH_LONG).show();
	    }
	    else {
	    	if (c.getCount()>0) {
	    		while (c.moveToNext()) {
		    		String uri = c.getString(c.getColumnIndex(DownloadManager.COLUMN_URI));
		    		if (uri != null && uri.equals(downloadPath)) {
		    			 c.close();
		    			 
//		    			 HealthUtil.worningAlert(this, "任务已经在下载列表");
		    			 Toast.makeText(this, "任务已经在下载列表", Toast.LENGTH_LONG).show();
		    			 return true;
		    		}
		    	}
			}
	    }
	    if (c != null && !c.isClosed()) {
	    	c.close();
		}
	    return false;
	  }
	  
	  private void showDownloadManagerView() {
		try {
			 Intent intent = new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS);
			 intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
			 startActivity(intent);
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
		}
		 
	  }
	  
	  private String statusMessage(Cursor c) {
	    String msg = DOWNLOAD_STATE_FAIL;
	    HealthUtil.LOG_D(getClass(), "------statusMessage");
	    try {
			switch(c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
			  case DownloadManager.STATUS_FAILED:
			    msg = DOWNLOAD_STATE_FAIL;
			    break;
			  
			  case DownloadManager.STATUS_PAUSED:
			    msg = DOWNLOAD_STATE_PAUSED;
			    break;
			  
			  case DownloadManager.STATUS_PENDING:
			    msg = "Download pending!";
			    break;
			  
			  case DownloadManager.STATUS_RUNNING:
			    msg = "Download in progress!";
			    break;
			  
			  case DownloadManager.STATUS_SUCCESSFUL:
			    msg = DOWNLOAD_STATE_SUCCESS;
			    break;
			  default:
			    msg="Download is nowhere in sight";
			    break;
			}
			
		} catch (CursorIndexOutOfBoundsException e) {
//			e.printStackTrace();
			Log.e(HealthUtil.LOG_TAG, "CursorIndexOutOfBoundsException");
			msg = DOWNLOAD_STATE_DELETE; 
		}
	    
	    return(msg);
	  }
	  
	  private void installApkByGuide(String localFilePath) {
		    Intent intent = new Intent(Intent.ACTION_VIEW);
	        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        HealthUtil.LOG_D(getClass(), "localFilePath=" + localFilePath);
	        intent.setDataAndType(Uri.parse("file://" + localFilePath),"application/vnd.android.package-archive");
	        startActivity(intent);
	  }
	
	  
	//下载通知点击的监听器
	BroadcastReceiver onNotificationClick = new BroadcastReceiver() {
	    @Override
		public void onReceive(Context ctxt, Intent intent) {
	      showDownloadManagerView();
	    }
	};

	//下载完成的接收器
	private class OnCompleteReceiver extends BroadcastReceiver {
		
		private int stopFlag = 0;
		
//		private long lastDownloadId;
//		
//		 public void setLastDownloadId(long lastDownloadId) {
//			this.lastDownloadId = lastDownloadId;
//		}
		 
		@Override
		public void onReceive(Context ctxt, Intent intent) {
//	    	windowManager.removeView(downloadToast);
			long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);//通过intent获取发广播的id
			HealthUtil.LOG_D(getClass(), "--->intent & lastDownloadId = " + downloadId);
	    	Cursor c = downloadManager.query(new DownloadManager.Query().setFilterById(downloadId));
	    	
	    	if (c != null && downloadId != -1) 
	    	{
				if (c.getCount() <= 0) 
				{
					HealthUtil.LOG_D(getClass(), "DOWNLOAD_STATE_DELETE ");
					stopFlag ++;
				}else 
				{
					c.moveToFirst();
			    	String downloadStatus = statusMessage(c);
			    	HealthUtil.LOG_D(getClass(), "--->OnCompleteReceiver,downloadStatus=" + downloadStatus);
//			    	IAccessWebServcieCtrl awsc = new AccessWebServiceCtrlImpl();

			    	String downloadFileName = c.getString(c.getColumnIndex(DownloadManager.COLUMN_TITLE));
			    	if (DOWNLOAD_STATE_FAIL.equals(downloadStatus)) {
						int reason = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_REASON));
						String toastStr = reasonForFailed(downloadFileName, reason);
//						HealthUtil.worningAlert(ctxt, toastStr);
						stopFlag ++;
						HealthUtil.LOG_D(getClass(), "reasonForFailed = " + toastStr);
					}else if (DOWNLOAD_STATE_PAUSED.equals(downloadStatus)) {
			    		int reason = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_REASON));
			    		String toastStr = reasonForPaused(downloadFileName, reason);
//						HealthUtil.worningAlert(ctxt, toastStr);
						HealthUtil.LOG_D(getClass(), "reasonForPaused = " + toastStr);
					}else if (DOWNLOAD_STATE_SUCCESS.equals(downloadStatus))
					{		
						downloadPromptList.add(downloadFileName);
						
			    		String localFilePath = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
			    		HealthUtil.LOG_D(getClass(), "DOWNLOAD_STATE_SUCCESS = " + downloadFileName);
			    		if (downloadFileName.toLowerCase().endsWith(".apk")) 
			    		{
			    			HealthUtil.LOG_D(getClass(), "localFilePath=" + localFilePath);
				    		installApkByGuide(localFilePath);
						}else
						{
//							Toast.makeText(this,  "\"" + downloadFileName + "\"下载完成", Toast.LENGTH_LONG).show();
						}
			    		stopFlag ++;
					}
				}
				
				c.close();
			}
	    	
	    	if (stopFlag == startFlag) {
	    		HealthUtil.LOG_D(getClass(), "service stop");
	    		stopSelf();
	    		HealthUtil.downloading = false;
	    		HealthUtil.removeView();
			}
	    	
	    }
	}
	
	private String reasonForPaused(String fileName, int reasonCode){
		reasonBuffer = new StringBuffer("\"" + fileName + "\"下载暂停");
		switch (reasonCode) {
		case DownloadManager.PAUSED_QUEUED_FOR_WIFI:
			reasonBuffer.append("：WIFI不可用");
			break;
		case DownloadManager.PAUSED_UNKNOWN:
			
			break;
		case DownloadManager.PAUSED_WAITING_FOR_NETWORK:
			reasonBuffer.append("：手机网络不可用");
			break;
		case DownloadManager.PAUSED_WAITING_TO_RETRY:
			reasonBuffer.append("：等待重试");
			break;
		default:
			
			break;
		}
		return reasonBuffer.toString();
	}
	
	private String reasonForFailed(String fileName, int reasonCode){
		reasonBuffer = new StringBuffer("\"" + fileName + "\"下载失败");
		switch (reasonCode) {
		case DownloadManager.ERROR_CANNOT_RESUME:
			reasonBuffer.append("：请检查网络");
			break;
		case DownloadManager.ERROR_DEVICE_NOT_FOUND:
			reasonBuffer.append("：请检查SD卡是否安装");
			break;
		case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
			reasonBuffer.append("：文件已存在,请删除后再下载");
			break;
		case DownloadManager.ERROR_HTTP_DATA_ERROR:
			reasonBuffer.append("：Http传输出错");
			break;
		case DownloadManager.ERROR_INSUFFICIENT_SPACE:
			reasonBuffer.append("：SD卡空间不足");
			break;
		case DownloadManager.ERROR_TOO_MANY_REDIRECTS:
			reasonBuffer.append("：Http重定向过多");
			break;
		case DownloadManager.ERROR_UNHANDLED_HTTP_CODE:
			reasonBuffer.append("：Http出错");
			break;
		case DownloadManager.ERROR_UNKNOWN:
			
			break;
		default:
			
			break;
		}
		return reasonBuffer.toString();
	}
	
//	private void play(){
//		HealthUtil.LOG_D(getClass(), "flashPlay");
//		Intent callIntent = new Intent(Intent.ACTION_CALL); 
//		callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
//		callIntent.setClass(DownloadService.this, IndustryPlayerActivity.class);
//		callIntent.putExtra("fileName", localFileName);
//		startActivity(callIntent);
//	}
	
	private class DownloadsChangeObserver extends ContentObserver {
		
		private long lastDownloadId;
		
        public void setLastDownloadId(long lastDownloadId) {
			this.lastDownloadId = lastDownloadId;
		}

		public DownloadsChangeObserver(Uri uri) {
            super(new Handler());
        }

		@Override
		public void onChange(boolean selfChange) {
			HealthUtil.LOG_D(getClass(), "selfChange=" + selfChange);
			super.onChange(selfChange);
			Cursor c = downloadManager.query(new DownloadManager.Query().setFilterById(lastDownloadId));
			if (c != null && c.getCount() > 0) {
				c.moveToFirst();
				int totalSize = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
				int size = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
				HealthUtil.LOG_D(getClass(), size + "/" + totalSize + "----------" + lastDownloadId);
		    	c.close();
			}
	    	if (c != null && !c.isClosed()) {
				c.close();
			}
		}

	}

}
