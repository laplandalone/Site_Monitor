package com.dm.yx.view.visit;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dm.yx.BaseActivity;
import com.dm.yx.MainPageActivity;
import com.dm.yx.R;
import com.dm.yx.model.User;
import com.dm.yx.tools.HealthConstant;
import com.dm.yx.tools.HealthUtil;
import com.dm.yx.upload.FormFile;
import com.dm.yx.upload.UploadThread;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class VisitDetailActivity extends BaseActivity
{
	
	@ViewInject(R.id.title)
	private TextView title;
	WebView web;
	
	@ViewInject(R.id.input_img)
	private  Button inputImg;
	
	@ViewInject(R.id.ask_again_text)
	private EditText askAgain;
	
	@ViewInject(R.id.copy_button)
	private LinearLayout layout;
	@ViewInject(R.id.layout1)
	private LinearLayout imagesLayout;
	
	@ViewInject(R.id.img1)
	private ImageView img1;

	@ViewInject(R.id.img2)
	private ImageView img2;

	@ViewInject(R.id.img3)
	private ImageView img3;
	
	 
	@ViewInject(R.id.frm1)
	private FrameLayout frm1;

	@ViewInject(R.id.frm2)
	private FrameLayout frm2;

	@ViewInject(R.id.frm3)
	private FrameLayout frm3;
	 
	ArrayList<String> data = new ArrayList<String>();
	
	private ArrayList<String> imagesUrl = new ArrayList<String>();
	private String mPicName = "";
	private BitmapUtils bitmapUtils;
	private boolean restartBool = true;
	private Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.visit_detail_webview);
		web = (WebView) findViewById(R.id.webview);  
		web.getSettings().setJavaScriptEnabled(true);   
		ViewUtils.inject(this);
		addActivity(this);
		initView();
		initValue();
		if (savedInstanceState != null)
		{
			restartBool = savedInstanceState.getBoolean("is_restart", true);
			imagesUrl = savedInstanceState.getStringArrayList("image_url");
			mPicName = savedInstanceState.getString("pic_name");
		}
		bitmapUtils = new BitmapUtils(this);
		bitmapUtils.configDefaultBitmapMaxSize(50, 50);
		bitmapUtils.clearCache();
		context=this;
	}


	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		HealthUtil.LOG_D(getClass(), "onSaveInstanceState---->");
		outState.putBoolean("is_restart", false);
		outState.putStringArrayList("image_url", imagesUrl);
		outState.putString("pic_name", mPicName);
		super.onSaveInstanceState(outState);
	}
	
	@OnClick(R.id.back)
	public void toBack(View v)
	{
		Intent intent = new Intent(VisitDetailActivity.this, MainPageActivity.class);
		startActivity(intent);
		exit();
	}
	
 
	@JavascriptInterface
	@Override
	protected void initView()
	{
		String titleT = getIntent().getStringExtra("title");
		// TODO Auto-generated method stub
		title.setText(titleT);
		String url = getIntent().getStringExtra("url");
		data.add("拍照");
		data.add("从相册选择");
		frm1.setOnClickListener(clickListener);
		frm2.setOnClickListener(clickListener);
		frm3.setOnClickListener(clickListener);
		 if(web != null) 
	        { 
			    web.addJavascriptInterface(this, "javatojs");

	            web.setWebViewClient(new WebViewClient() 
	            { 
	            	@Override
	                public boolean shouldOverrideUrlLoading(WebView view, String url) { 
	                    view.loadUrl(url); 
	                    return true; 
	                } 
	            	
	            	@Override
	            	public void onPageStarted(WebView view, String url,
	            			Bitmap favicon) {
	            		dialog.setMessage("正在加载,请稍后...");
	      	    		dialog.show();
	            		super.onPageStarted(view, url, favicon);
	            	}
	                @Override 
	                public void onPageFinished(WebView view,String url) 
	                { 
	                	 try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}   
	                    dialog.dismiss(); 
	                }

					@Override
					public void onReceivedError(WebView view, int errorCode,
							String description, String failingUrl) 
					{
						HealthUtil.infoAlert(VisitDetailActivity.this, "加载失败,请重试...");
						web.setVisibility(View.GONE);
						super.onReceivedError(view, errorCode, description, failingUrl);
					} 
	                
	            }); 
	             
	            loadUrl(url); 
	        } 
	}

	  public void loadUrl(String url) 
	    { 
	        if(web != null) 
	        { 
	            web.loadUrl(url); 
	          
	        } 
	    } 

	@Override
	protected void initValue()
	{
		
	}
	
	@JavascriptInterface
	public void addVisit(String josn,String visitType)
	{
		dialog.setMessage("正在加载,请稍后...");
  		dialog.show();
		User user = HealthUtil.getUserInfo();
//		RequestParams param = webInterface.addVisit(josn, user.getUserId(), visitType);
//		invokeWebServer(param, ADD_VISIT);
		
		
		int imageSize = imagesUrl.size();
		FormFile[] formFiles = new FormFile[imageSize];
		for (int i = 0; i < imageSize; i++)
		{
			File imageFile = new File(imagesUrl.get(i));
			FormFile formFile = new FormFile(String.valueOf(new Date().getTime()) + i + ".jpg", imageFile, "image", "application/octet-stream");
			formFiles[i] = formFile;
		}
		UploadThread uploadThread = new UploadThread(formFiles, mHandler, josn,HealthUtil.readHospitalId(),"VISIT_IMG_PATH",user.getUserId(),visitType);
		new Thread(uploadThread).start();
		
	}

	 Handler handler = new Handler();
	
	private Handler mHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			try
			{
				   
				handler.post(new Runnable() {
					
					@Override
					public void run() 
					{
						// TODO Auto-generated method stub
						if (dialog != null)
						{
							dialog.cancel();
						}	
					}
				});
				
				if (msg.obj == null)
				{
					HealthUtil.infoAlert(VisitDetailActivity.this,"提交失败，请核对信息之后重新提交");
					return;
				}
				switch (msg.arg1)
				{
				case 1001:
					submitResult(msg.obj.toString());
					break;
				case 1002:
					// parseData(msg.obj.toString());
					break;
				}
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	};
	
	private void submitResult(String result)
	{
		try
		{
			JSONObject jsonObject = new JSONObject(result);
			String executeType = jsonObject.get("executeType").toString();
			if ("success".equals(executeType))
			{
				HealthUtil.infoAlert(this, "您随访提交成功，请耐心等待医生回复.");
				finish();
			} else
			{
				HealthUtil.infoAlert(this,"提交失败，请核对信息之后重新提交");
				return;
			}
		} catch (JSONException e)
		{
			HealthUtil.infoAlert(this, "返回结果解析失败");
			e.printStackTrace();
		} catch (Exception e)
		{
			HealthUtil.infoAlert(this, "提交失败");
			e.printStackTrace();
		}
	}
	
	@OnClick(R.id.vist_submit)
	public void submit(View v)
	{
		web.loadUrl("javascript:addAsd()");
	}
	
	@OnClick(R.id.vist_back)
	public void back(View v)
	{
		finish();
	}
	
	@JavascriptInterface
	public void alert(String msg)
	{
		HealthUtil.infoAlert(VisitDetailActivity.this,msg);
	}
	
	@JavascriptInterface
	public void cancel()
	{
		finish();
	}

	/**
	 * 链接web服务
	 * 
	 * @param param
	 */
	private void invokeWebServer(RequestParams param, int responseCode)
	{
		HealthUtil.LOG_D(getClass(), "connect to web server");
		MineRequestCallBack requestCallBack = new MineRequestCallBack(responseCode);
		if (httpHandler != null)
		{
			httpHandler.cancel();
		}
		httpHandler = mHttpUtils.send(HttpMethod.POST, HealthConstant.URL, param, requestCallBack);
	}

	/**
	 * 获取后台返回的数据
	 */
	class MineRequestCallBack extends RequestCallBack<String>
	{

		private int responseCode;

		public MineRequestCallBack(int responseCode)
		{
			super();
			this.responseCode = responseCode;
		}

		@Override
		public void onFailure(HttpException error, String msg)
		{
			HealthUtil.LOG_D(getClass(), "onFailure-->msg=" + msg);
			if (dialog.isShowing())
			{
				dialog.cancel();
			}

			HealthUtil.infoAlert(VisitDetailActivity.this, "信息加载失败，请检查网络后重试");
		}

		@Override
		public void onSuccess(ResponseInfo<String> arg0)
		{
			// TODO Auto-generated method stub
			HealthUtil.LOG_D(getClass(), "result=" + arg0.result);
			if (dialog.isShowing())
			{
				dialog.cancel();
			}
			switch (responseCode)
			{
			case ADD_VISIT:
				returnMsg(arg0.result, ADD_VISIT);
				break;
			}
		}
	}
	/*
	 * 处理返回结果数据
	 */
	private void returnMsg(String json, int code)
	{
		JsonParser jsonParser = new JsonParser();
		JsonElement jsonElement = jsonParser.parse(json);
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		String executeType = jsonObject.get("executeType").getAsString();
		if ("success".equals(executeType))
		{
			HealthUtil.infoAlert(VisitDetailActivity.this, "您随访提交成功，请耐心等待医生回复.");
			finish();
		} else
		{
			HealthUtil.infoAlert(VisitDetailActivity.this, "添加随访失败.");
		}

	}
	
	OnClickListener clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View view) 
		{
			new AlertDialog.Builder(VisitDetailActivity.this).setTitle("提示").setIcon(android.R.drawable.ic_dialog_map).setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, data), new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					getPicName();
					switch (which)
					{
					case 0:
						try
						{
							Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
							intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(HealthConstant.IMG_PATH, mPicName)));
							startActivityForResult(intent, 1);
						} catch (Exception e)
						{
							e.printStackTrace();
						}
						break;
					case 1:
						try
						{
							Intent intent = new Intent();
							intent.setType("image/*");
							intent.setAction(Intent.ACTION_GET_CONTENT);
							startActivityForResult(intent, 2);
						} catch (ActivityNotFoundException e)
						{

						}
						break;
					case R.id.btn_cancel:
						finish();
						break;
					default:
						break;
					}
				}
			}).create().show();
			
		}
	};
	
	@OnClick(R.id.frm1)
	public void addImage(View v)
	{
		new AlertDialog.Builder(VisitDetailActivity.this).setTitle("提示").setIcon(android.R.drawable.ic_dialog_map)
				.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data), new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						getPicName();
						switch (which)
						{
						case 0:
							try
							{
								Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
								intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(HealthConstant.IMG_PATH, mPicName)));
								startActivityForResult(intent, 1);
							} catch (Exception e)
							{
								e.printStackTrace();
							}
							break;
						case 1:
							try
							{
								Intent intent = new Intent();
								intent.setType("image/*");
								intent.setAction(Intent.ACTION_GET_CONTENT);
								startActivityForResult(intent, 2);
							} catch (ActivityNotFoundException e)
							{

							}
							break;
						case R.id.btn_cancel:
							finish();
							break;
						default:
							break;
						}
					}
				}).create().show();
	}

	private void addImage(String imagePath)
	{
		try
		{
			if (imagesUrl == null)
			{
				return;
			} else if (imagesUrl.size() < 4)
			{
				imagesUrl.add(imagePath);
			} else
			{
				HealthUtil.infoAlert(this, "照片个数已经达到上限，请删除之后新增");
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, intent);
		int imgSize = imagesUrl.size();
		switch (requestCode)
		{
		case 0:
			break;
		case 1:
			File imageFile = new File(HealthConstant.IMG_PATH, mPicName);
			addImage(imageFile.getAbsolutePath());
			imagesLayout.setVisibility(View.VISIBLE);
			 
			if (imgSize == 0)
			{
				bitmapUtils.display(img1, imageFile.getAbsolutePath());
				break;
			} else if (imgSize == 1)
			{
				bitmapUtils.display(img2, imageFile.getAbsolutePath());
				break;
			} else if (imgSize == 2)
			{
				frm3.setVisibility(View.VISIBLE);
				bitmapUtils.display(img3, imageFile.getAbsolutePath());
				break;
			} else
			{
				HealthUtil.infoAlert(VisitDetailActivity.this, "最多可添加三张图片");
				break;
			}
		case 2:
			if (intent != null)
			{
				// 取得返回的Uri,基本上选择照片的时候返回的是以Uri形式，但是在拍照中有得机子呢Uri是空的，所以要特别注意
				Uri mImageCaptureUri = intent.getData();
				// 返回的Uri不为空时，那么图片信息数据都会在Uri中获得。如果为空，那么我们就进行下面的方式获取
				if (mImageCaptureUri != null)
				{
					Bitmap image;
					try
					{
						String[] proj = { MediaStore.Images.Media.DATA };
						Cursor cursor = managedQuery(mImageCaptureUri, proj, null, null, null);
						// 按我个人理解 这个是获得用户选择的图片的索引值
						int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
						// 将光标移至开头 ，这个很重要，不小心很容易引起越界
						cursor.moveToFirst();
						// 最后根据索引值获取图片路径
						String path = cursor.getString(column_index);
						// 这个方法是根据Uri获取Bitmap图片的静态方法
						image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageCaptureUri);
						if(path==null || "".equals(path))
						{
							path=getPath(context, mImageCaptureUri);
						}
						addImage(path);
						if (image != null)
						{
							if (imgSize == 0)
							{
								img1.setImageBitmap(image);
								break;
							} else if (imgSize == 1)
							{
								img2.setImageBitmap(image);
								break;
							} else if (imgSize == 2)
							{
								frm3.setVisibility(View.VISIBLE);
								img3.setImageBitmap(image);
								break;
							} else
							{
								HealthUtil.infoAlert(VisitDetailActivity.this, "最多可添加三张图片");
								break;
							} 
						}
					} catch (Exception e)
					{
						e.printStackTrace();
					}
				}

			}

			break;
		default:
			break;
		}
	}
	
	private String getPicName()
	{
		mPicName = new Date().getTime() + ".jpg";
		return mPicName;
	}
	 
     @TargetApi(Build.VERSION_CODES.KITKAT)
     public static String getPath(final Context context, final Uri uri) {
         final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
         // DocumentProvider
         if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
             // ExternalStorageProvider
             if (isExternalStorageDocument(uri)) {
                 final String docId = DocumentsContract.getDocumentId(uri);
                 final String[] split = docId.split(":");
                 final String type = split[0];
                 if ("primary".equalsIgnoreCase(type)) {
                     return Environment.getExternalStorageDirectory() + "/"
                             + split[1];
                 }
                 // TODO handle non-primary volumes
             }
             // DownloadsProvider
             else if (isDownloadsDocument(uri)) {
                 final String id = DocumentsContract.getDocumentId(uri);
                 final Uri contentUri = ContentUris.withAppendedId(
                         Uri.parse("content://downloads/public_downloads"),
                         Long.valueOf(id));
                 return getDataColumn(context, contentUri, null, null);
             }
             // MediaProvider
             else if (isMediaDocument(uri)) {
                 final String docId = DocumentsContract.getDocumentId(uri);
                 final String[] split = docId.split(":");
                 final String type = split[0];
                 Uri contentUri = null;
                 if ("image".equals(type)) {
                     contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                 } else if ("video".equals(type)) {
                     contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                 } else if ("audio".equals(type)) {
                     contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                 }
                 final String selection = MediaColumns._ID + "=?";
                 final String[] selectionArgs = new String[] { split[1] };
                 return getDataColumn(context, contentUri, selection,
                         selectionArgs);
             }
         }
         // MediaStore (and general)
         else if ("content".equalsIgnoreCase(uri.getScheme())) {
             // Return the remote address
             if (isGooglePhotosUri(uri))
                 return uri.getLastPathSegment();
             return getDataColumn(context, uri, null, null);
         }
         // File
         else if ("file".equalsIgnoreCase(uri.getScheme())) {
             return uri.getPath();
         }
         return null;
     }
 
     /**
      * Get the value of the data column for this Uri . This is useful for
      * MediaStore Uris , and other file - based ContentProviders.
      * 
      * @param context
      *            The context.
      * @param uri
      *            The Uri to query.
      * @param selection
      *            (Optional) Filter used in the query.
      * @param selectionArgs
      *            (Optional) Selection arguments used in the query.
      * @return The value of the _data column, which is typically a file path.
      */
     public static String getDataColumn(Context context, Uri uri,
             String selection, String[] selectionArgs) {
         Cursor cursor = null;
         final String column = MediaColumns.DATA;
         final String[] projection = { column };
         try {
             cursor = context.getContentResolver().query(uri, projection,
                     selection, selectionArgs, null);
             if (cursor != null && cursor.moveToFirst()) {
                 final int index = cursor.getColumnIndexOrThrow(column);
                 return cursor.getString(index);
             }
         } finally {
             if (cursor != null)
                 cursor.close();
         }
         return null;
     }
 
     /**
      * @param uri
      *            The Uri to check.
      * @return Whether the Uri authority is ExternalStorageProvider.
      */
     public static boolean isExternalStorageDocument(Uri uri) {
         return "com.android.externalstorage.documents".equals(uri
                 .getAuthority());
     }
 
     /**
      * @param uri
      *            The Uri to check.
      * @return Whether the Uri authority is DownloadsProvider.
      */
     public static boolean isDownloadsDocument(Uri uri) {
         return "com.android.providers.downloads.documents".equals(uri
                 .getAuthority());
     }
 
     /**
      * @param uri
      *            The Uri to check.
      * @return Whether the Uri authority is MediaProvider.
      */
     public static boolean isMediaDocument(Uri uri) {
         return "com.android.providers.media.documents".equals(uri
                 .getAuthority());
     }
 
     /**
      * @param uri
      *            The Uri to check.
      * @return Whether the Uri authority is Google Photos.
      */
     public static boolean isGooglePhotosUri(Uri uri) {
         return "com.google.android.apps.photos.content".equals(uri
                 .getAuthority());
     }

}
