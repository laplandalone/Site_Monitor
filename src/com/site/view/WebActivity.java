package com.site.view;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.site.BaseActivity;
import com.site.R;
import com.site.tools.Constant;
import com.site.tools.SiteUtil;
import com.site.upload.FormFile;
import com.site.upload.UploadThread;

public class WebActivity extends BaseActivity
{
	
	@ViewInject(R.id.title)
	private TextView title;
	WebView web;
	
	ArrayList<String> data = new ArrayList<String>();

	private ArrayList<String> imagesUrl = new ArrayList<String>();
	private String mPicName = "";
	private BitmapUtils bitmapUtils;
	
	@ViewInject(R.id.layout1)
	private LinearLayout imagesLayout;

	@ViewInject(R.id.img1)
	private ImageView img1;

	@ViewInject(R.id.img2)
	private ImageView img2;

	@ViewInject(R.id.img3)
	private ImageView img3;

	@ViewInject(R.id.img4)
	private ImageView img4;

	@ViewInject(R.id.img5)
	private ImageView img5;

	@ViewInject(R.id.img6)
	private ImageView img6;

			
	@ViewInject(R.id.frm1)
	private FrameLayout frm1;

	@ViewInject(R.id.frm2)
	private FrameLayout frm2;

	@ViewInject(R.id.frm3)
	private FrameLayout frm3;
	
	@ViewInject(R.id.frm4)
	private FrameLayout frm4;
	
	@ViewInject(R.id.frm5)
	private FrameLayout frm5;
	
	@ViewInject(R.id.frm6)
	private FrameLayout frm6;
	
	@ViewInject(R.id.delete1)
	private ImageView delete1;

	@ViewInject(R.id.delete2)
	private ImageView delete2;
	
	@ViewInject(R.id.delete3)
	private ImageView delete3;
	
	@ViewInject(R.id.delete4)
	private ImageView delete4;
	
	@ViewInject(R.id.delete5)
	private ImageView delete5;
	
	@ViewInject(R.id.delete6)
	private ImageView delete6;
	
	
	private String currentImg="frm1";
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.visit_detail_webview);
		web = (WebView) findViewById(R.id.webview);  
		web.getSettings().setJavaScriptEnabled(true);     
		web.getSettings().setAllowFileAccess(true);  
		web.getSettings().setDomStorageEnabled(true);//
		ViewUtils.inject(this);
		addActivity(this);
		initView();
		initValue();
	}

	
	@Override
	protected void initView()
	{
//		String titleT = getIntent().getStringExtra("title");
//		// TODO Auto-generated method stub
//		title.setText(titleT);
//		String url = getIntent().getStringExtra("url");
		String l=SiteUtil.getLongitude();
		String lat=SiteUtil.getLatitude();
		String url="http://api.map.baidu.com/marker?location="+lat+","+l+"&title=我的位置&content=百度奎科大厦&output=html";
		System.out.println("url:"+url);
		 if(web != null) 
	        { 
//			    web.addJavascriptInterface(this, "javatojs");

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
	            		super.onPageStarted(view, url, favicon);
	            	}
	                @Override 
	                public void onPageFinished(WebView view,String url) 
	                { 
	                	
	                }

					@Override
					public void onReceivedError(WebView view, int errorCode,
							String description, String failingUrl) 
					{
						SiteUtil.infoAlert(WebActivity.this, "加载失败,请重试...");
						web.setVisibility(View.GONE);
						super.onReceivedError(view, errorCode, description, failingUrl);
					} 
	                
	            }); 
	             
	            loadUrl(url); 
	        } 
		 frm1.setOnClickListener(new Li());
		 frm2.setOnClickListener(new Li());
		 frm3.setOnClickListener(new Li());
		 frm4.setOnClickListener(new Li());
		 frm5.setOnClickListener(new Li());
		 frm6.setOnClickListener(new Li());
	}

	class Li implements OnClickListener
	{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			new AlertDialog.Builder(WebActivity.this).setTitle("提示").setIcon(android.R.drawable.ic_dialog_map)
			.setAdapter(new ArrayAdapter<String>(WebActivity.this, android.R.layout.simple_list_item_1, data), new DialogInterface.OnClickListener()
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
							intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Constant.IMG_PATH, mPicName)));
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
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, intent);
		switch (requestCode)
		{
		 
		case 1:
			File imageFile = new File(Constant.IMG_PATH, mPicName);
			File file = new File(imageFile.getAbsolutePath());
			if(file.exists())
			{
				addImage(imageFile.getAbsolutePath());
				imagesLayout.setVisibility(View.VISIBLE);
				if ("frm1".equals(currentImg))
				{
					bitmapUtils.display(img1, imageFile.getAbsolutePath());
					currentImg="frm2";
					frm2.setVisibility(View.VISIBLE);
					delete1.setVisibility(View.VISIBLE);
					break;
				} else if ("frm2".equals(currentImg))
				{
					frm2.setVisibility(View.VISIBLE);
					bitmapUtils.display(img2, imageFile.getAbsolutePath());
					currentImg="frm3";
					frm3.setVisibility(View.VISIBLE);
					delete2.setVisibility(View.VISIBLE);
					break;
				} else if ("frm3".equals(currentImg))
				{
					frm3.setVisibility(View.VISIBLE);
					bitmapUtils.display(img3, imageFile.getAbsolutePath());
					currentImg="frm4";
					frm4.setVisibility(View.VISIBLE);
					delete3.setVisibility(View.VISIBLE);
					break;
				}if ("frm4".equals(currentImg))
				{
					frm4.setVisibility(View.VISIBLE);
					bitmapUtils.display(img4, imageFile.getAbsolutePath());
					currentImg="frm5";
					frm5.setVisibility(View.VISIBLE);
					delete4.setVisibility(View.VISIBLE);
					break;
					
				} else if ("frm5".equals(currentImg))
				{
					frm5.setVisibility(View.VISIBLE);
					bitmapUtils.display(img5, imageFile.getAbsolutePath());
					currentImg="frm6";
					frm6.setVisibility(View.VISIBLE);
					delete5.setVisibility(View.VISIBLE);
					break;
				} else if ("frm6".equals(currentImg))
				{
					frm6.setVisibility(View.VISIBLE);
					bitmapUtils.display(img6, imageFile.getAbsolutePath());
					delete6.setVisibility(View.VISIBLE);
					break;
				}  else
				{
					SiteUtil.infoAlert(WebActivity.this, "最多可添加6张图片");
					break;
				}
			}
		case 2:
			if (intent != null)
			{
				// 取得返回的Uri,基本上选择照片的时候返回的是以Uri形式，但是在拍照中有得机子呢Uri是空的，所以要特别注意
				Uri mImageCaptureUri = intent.getData();
				// 返回的Uri不为空时，那么图片信息数据都会在Uri中获得。如果为空，那么我们就进行下面的方式获取
				if (mImageCaptureUri != null)
				{
//					Bitmap image = null;
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
//						image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageCaptureUri);
						File image = new File(path);
						addImage(path);
						
						if (image != null)
						{
							imagesLayout.setVisibility(View.VISIBLE);
							if (frm1.getVisibility() == 8)
							{
								frm1.setVisibility(View.VISIBLE);
//								img1.setImageBitmap(image);
								bitmapUtils.display(img1, image.getAbsolutePath());
								break;
							} else if (frm2.getVisibility() == 8)
							{
								frm2.setVisibility(View.VISIBLE);
//								img2.setImageBitmap(image);
								bitmapUtils.display(img2, image.getAbsolutePath());
								break;
							} else if (frm3.getVisibility() == 8)
							{
								frm3.setVisibility(View.VISIBLE);
//								img3.setImageBitmap(image);
								bitmapUtils.display(img3, image.getAbsolutePath());
								break;
							} else
							{
								SiteUtil.infoAlert(WebActivity.this, "最多可添加三张图片");
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

	private void addImage(String imagePath)
	{
		try
		{
			if (imagesUrl == null)
			{
				return;
			} else if (imagesUrl.size() < 7)
			{
				imagesUrl.add(imagePath);
			} else
			{
				SiteUtil.infoAlert(this, "照片个数已经达到上限，请删除之后新增");
			}
		} catch (Exception e)
		{
			e.printStackTrace();
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
		bitmapUtils = new BitmapUtils(this);
		bitmapUtils.configDefaultBitmapMaxSize(50, 50);
		bitmapUtils.clearCache();
		// TODO Auto-generated method stub
		data.add("拍照");
		data.add("从相册选择");
	}
	
	@OnClick(R.id.input_img)
	public void submitQuestion(View v)
	{


		Gson gson = new Gson();
		String questionStr = "";
		int imageSize = imagesUrl.size();
		FormFile[] formFiles = new FormFile[imageSize];
		for (int i = 0; i < imageSize; i++)
		{
			File imageFile = new File(imagesUrl.get(i));
			FormFile formFile = new FormFile(String.valueOf(new Date().getTime()) + i + ".jpg", imageFile, "image", "application/octet-stream");
			formFiles[i] = formFile;
		}
		UploadThread uploadThread = new UploadThread(formFiles, mHandler, "004", "0571-458qj-1", "t5555555555", "何家边", "0571-4805", "30.5490875", "119.7676665");
		new Thread(uploadThread).start();
	}
	
	private Handler mHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			try
			{
				if (dialog != null)
				{
					dialog.cancel();
				}
				if (msg.obj == null)
				{
					showFailureDialog("提交失败，请核对信息之后重新提交");
					return;
				}
				switch (msg.arg1)
				{
				case 1001:
//					submitResult(msg.obj.toString());
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
	
	private void showFailureDialog(String msg)
	{

		AlertDialog alertDialog = new AlertDialog.Builder(this).setPositiveButton("确定", null).setTitle("失败提醒").setMessage(msg).create();
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.show();
	}
}
