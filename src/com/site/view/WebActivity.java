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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hp.hpl.sparta.xpath.Step;
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
	
	@ViewInject(R.id.editUser)
	private TextView editUser;
	
	@ViewInject(R.id.site)
	private TextView site;
	
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
	
	@OnClick(R.id.site)
	public void site(View v)
	{
		finish();
	}

	@OnClick(R.id.cancel)
	public void cancel(View v)
	{
		finish();
	}
	
	@Override
	protected void initView()
	{
//		String titleT = getIntent().getStringExtra("title");
//		// TODO Auto-generated method stub
		title.setText("上班站点信息");
//		String url = getIntent().getStringExtra("url");
		site.setText("线路列表");
		editUser.setText("");
		String l=SiteUtil.getLongitude();
		String lat=SiteUtil.getLatitude();
		String url="http://api.map.baidu.com/marker?location="+lat+","+l+"&title="+SiteUtil.getAddress()+"&content=&output=html";
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
		 
		 frm1.setTag("frm1");
		 frm2.setTag("frm2");
		 frm3.setTag("frm3");
		 frm4.setTag("frm4");
		 frm5.setTag("frm5");
		 frm6.setTag("frm6");
		 
		 delete1.setTag("frm1");
		 img1.setTag("frm1");
		 delete2.setTag("frm2");
		 img2.setTag("frm2");
		 delete3.setTag("frm3");
		 img3.setTag("frm3");
		 delete4.setTag("frm4");
		 img4.setTag("frm4");
		 delete5.setTag("frm5");
		 img5.setTag("frm5");
		 delete6.setTag("frm6");
		 img6.setTag("frm6");
			
	}

	class Li implements OnClickListener
	{
		@Override
		public void onClick(View v) {
			currentImg=v.getTag()+"";
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
//					case R.id.btn_cancel:
//						finish();
//						break;
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
		Bitmap image = null;
		super.onActivityResult(requestCode, resultCode, intent);
		switch (requestCode)
		{
		 
		case 1:
			File imageFile = new File(Constant.IMG_PATH, mPicName);
			File file = new File(imageFile.getAbsolutePath());
			SiteUtil.compressBitmap(imageFile.getAbsolutePath(), mPicName);
			if(file.exists())
			{
				addImage(Constant.IMG_PATH+mPicName);
				imagesLayout.setVisibility(View.VISIBLE);
				if ("frm1".equals(currentImg))
				{
					bitmapUtils.display(img1, imageFile.getAbsolutePath());
					frm2.setVisibility(View.VISIBLE);
					delete1.setVisibility(View.VISIBLE);
					frm1.setOnClickListener(null);
					img1.setOnClickListener(new deleteImg());
					break;
				} else if ("frm2".equals(currentImg))
				{
					frm2.setVisibility(View.VISIBLE);
					bitmapUtils.display(img2, imageFile.getAbsolutePath());
					frm3.setVisibility(View.VISIBLE);
					delete2.setVisibility(View.VISIBLE);
					img2.setOnClickListener(new deleteImg());
					break;
				} else if ("frm3".equals(currentImg))
				{
					frm3.setVisibility(View.VISIBLE);
					bitmapUtils.display(img3, imageFile.getAbsolutePath());
					frm4.setVisibility(View.VISIBLE);
					delete3.setVisibility(View.VISIBLE);
					img3.setOnClickListener(new deleteImg());
					break;
				}if ("frm4".equals(currentImg))
				{
					frm4.setVisibility(View.VISIBLE);
					bitmapUtils.display(img4, imageFile.getAbsolutePath());
					frm5.setVisibility(View.VISIBLE);
					delete4.setVisibility(View.VISIBLE);
					img4.setOnClickListener(new deleteImg());
					break;
					
					
				} else if ("frm5".equals(currentImg))
				{
					frm5.setVisibility(View.VISIBLE);
					bitmapUtils.display(img5, imageFile.getAbsolutePath());
					frm6.setVisibility(View.VISIBLE);
					delete5.setVisibility(View.VISIBLE);
					img5.setOnClickListener(new deleteImg());
					break;
				} else if ("frm6".equals(currentImg))
				{
					frm6.setVisibility(View.VISIBLE);
					bitmapUtils.display(img6, imageFile.getAbsolutePath());
					delete6.setVisibility(View.VISIBLE);
					img6.setOnClickListener(new deleteImg());
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
						if(path!=null && !"".equals(path))
						{
							image=SiteUtil.compressBitmap(path,mPicName);
						}else
						{
							image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageCaptureUri);
							SiteUtil.compressImage(image,mPicName);
						}
						 
						addImage(Constant.IMG_PATH + mPicName);
						if ("frm1".equals(currentImg))
						{
							img1.setImageBitmap(image);
							img1.setOnClickListener(null);
							frm2.setVisibility(View.VISIBLE);
							delete1.setVisibility(View.VISIBLE);
							img1.setOnClickListener(new deleteImg());
							break;
						}else if ("frm2".equals(currentImg))
						{
							frm2.setVisibility(View.VISIBLE);
							img2.setImageBitmap(image);
							frm3.setVisibility(View.VISIBLE);
							delete2.setVisibility(View.VISIBLE);
							img2.setOnClickListener(new deleteImg());
							break;
						} else if ("frm3".equals(currentImg))
						{
							frm3.setVisibility(View.VISIBLE);
							img3.setImageBitmap(image);
							currentImg="frm4";
							frm4.setVisibility(View.VISIBLE);
							delete3.setVisibility(View.VISIBLE);
							delete3.setTag("frm3");
							img3.setTag("frm3");
							img3.setOnClickListener(new deleteImg());
							break;
						}if ("frm4".equals(currentImg))
						{
							frm4.setVisibility(View.VISIBLE);
							img4.setImageBitmap(image);
							frm5.setVisibility(View.VISIBLE);
							delete4.setVisibility(View.VISIBLE);
							img4.setOnClickListener(new deleteImg());
							break;
							
						} else if ("frm5".equals(currentImg))
						{
							frm5.setVisibility(View.VISIBLE);
							img5.setImageBitmap(image);
							frm6.setVisibility(View.VISIBLE);
							delete5.setVisibility(View.VISIBLE);
							img5.setOnClickListener(new deleteImg());
							break;
						} else if ("frm6".equals(currentImg))
						{
							frm6.setVisibility(View.VISIBLE);
							img6.setImageBitmap(image);
							delete6.setVisibility(View.VISIBLE);
							img6.setOnClickListener(new deleteImg());
							break;
						}  else
						{
							SiteUtil.infoAlert(WebActivity.this, "最多可添加6张图片");
							break;
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

 
	class deleteImg implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			String tag= v.getTag()+"";
			if("frm1".equals(tag))
			{
				delete1.setVisibility(View.GONE);
				img1.setImageBitmap(null);
				img1.setOnClickListener(new Li());
				imagesUrl.remove(0);
			}else if("frm2".equals(tag))
			{
				delete2.setVisibility(View.GONE);
				img2.setImageBitmap(null);
				img2.setOnClickListener(new Li());
				imagesUrl.remove(1);
			}
			else if("frm3".equals(tag))
			{
				delete3.setVisibility(View.GONE);
				img3.setImageBitmap(null);
				img3.setOnClickListener(new Li());
				imagesUrl.remove(2);
			}
			else if("frm4".equals(tag))
			{
				delete4.setVisibility(View.GONE);
				img4.setImageBitmap(null);
				img4.setOnClickListener(new Li());
				imagesUrl.remove(3);
			}
			else if("frm5".equals(tag))
			{
				delete5.setVisibility(View.GONE);
				img5.setImageBitmap(null);
				img5.setOnClickListener(new Li());
				imagesUrl.remove(4);
			}else if("frm6".equals(tag))
			{
				delete6.setVisibility(View.GONE);
				img6.setImageBitmap(null);
				img6.setOnClickListener(new Li());
				imagesUrl.remove(5);
			} 
		}
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

		dialog.setMessage("正在上传,请稍后...");
		dialog.show();
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
		
		UploadThread uploadThread = new UploadThread(formFiles, mHandler, SiteUtil.getCity(), "0571-458qj-1", "t5555555555", SiteUtil.getStopName(), SiteUtil.getStopId(), SiteUtil.getLongitude(),SiteUtil.getLatitude());
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
					SiteUtil.infoAlert(WebActivity.this,"提交失败，请核对信息之后重新提交");
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
	
	public void submitResult(String json)
	{
		JsonParser jsonParser = new JsonParser();
		JsonElement jsonElement = jsonParser.parse(json);
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		String status = jsonObject.get("status").getAsString();
		if("00".equals(status))
		{
			SiteUtil.infoAlert(WebActivity.this,"上传成功");
			finish();
		}else
		{
			SiteUtil.infoAlert(WebActivity.this,"上传失败,请重试");
		}
	}

	 
}
