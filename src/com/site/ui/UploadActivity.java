package com.site.ui;

import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.HTTP;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.site.BaseActivity;
import com.site.R;
import com.site.tools.Bimp;
import com.site.tools.Constant;
import com.site.tools.FileUtils;
import com.site.tools.ImageItem;
import com.site.tools.PublicWay;
import com.site.tools.Res;
import com.site.tools.SiteUtil;
import com.site.upload.FormFile;
import com.site.upload.SocketHttpRequester;

/**
 * 首页面activity
 * 
 * @version 2014年10月18日 下午11:48:34
 */
public class UploadActivity extends BaseActivity {

	private GridView noScrollgridview;
	private GridAdapter adapter;
	private View parentView;
	private PopupWindow pop = null;
	private LinearLayout ll_popup;
	public static Bitmap bimap;

	private ProgressBar mPgBar;
	private TextView mTvProgress;

	private Map<String,String>paramMap;
	private FormFile[] formFiles;
	private List<File> files;
	private MyTask mTask;
	private View upView;

	/* 定位相关 */
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private BitmapDescriptor mCurrentMarker;
	private MapView mMapView;
	private BaiduMap baiduMap;
	boolean isFirstLoc = true;// 是否首次定位
	private String lineIds;

	@ViewInject(R.id.title)
	private TextView title;

	@ViewInject(R.id.editUser)
	private TextView editUser;

	@ViewInject(R.id.site)
	private TextView site;

	@OnClick(R.id.site)
	public void site(View v) {
		finish();
	}

	@OnClick(R.id.cancel)
	public void cancel(View v) {
		finish();
	}
	private String showMsg="";
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Res.init(this);
		bimap = BitmapFactory.decodeResource(getResources(),
				R.drawable.icon_addpic_unfocused);
		PublicWay.activityList.add(this);
		parentView = getLayoutInflater().inflate(R.layout.activity_selectimg,
				null);
		setContentView(parentView);
		Init();
		addActivity(this);
		ViewUtils.inject(this);

		upView = getLayoutInflater().inflate(R.layout.filebrowser_uploading,
				null);
		mPgBar = (ProgressBar) upView
				.findViewById(R.id.pb_filebrowser_uploading);
		mTvProgress = (TextView) upView
				.findViewById(R.id.tv_filebrowser_uploading);

		initView();
		initValue();

	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		title.setText("上报站点信息");
		site.setText("线路列表");
		editUser.setText("");
		// 地图初始化
		mMapView = (MapView) findViewById(R.id.bmapView);
		baiduMap = mMapView.getMap();
		// 开启定位图层
		baiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(72000);
		mLocClient.setLocOption(option);
		// 普通地图
		baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		// 开启交通图线
		baiduMap.setTrafficEnabled(true);
		// 设置比例尺
		baiduMap.setMapStatus(MapStatusUpdateFactory
				.newMapStatus(new MapStatus.Builder().zoom(16).build()));

		mLocClient.start();
	}

	@Override
	protected void initValue() {
		// TODO Auto-generated method stub

	}

	@OnClick(R.id.input_img)
	public void submit(View v) {

		new AlertDialog.Builder(UploadActivity.this).setTitle("上传进度")
				.setView(upView).create().show();
		ArrayList<ImageItem> imageItems = Bimp.tempSelectBitmap;
		formFiles = new FormFile[imageItems.size()];
		files=new ArrayList<File>();
		for (int i=0;i<imageItems.size();i++) 
		{
			ImageItem item=imageItems.get(i);
			Bitmap bitmap = item.getBitmap();
			String path = item.getImagePath();
			if (path == null || "".equals(path))// 拍照没有路径
			{
				item.setImagePath(path);
			}
			path = Constant.IMG_PATH + getPicName();
			SiteUtil.compressBitmap(bitmap, path);

			File imageFile = new File(path);
			files.add(imageFile);
		}

		mTask = new MyTask();
		mTask.execute();
	}

	private String getPicName() {
		return new Date().getTime() + ".jpg";
	}

	private class MyTask extends AsyncTask<String, Integer, String> {

		@Override
		protected void onPostExecute(String result) {
			submitResult(result);
		}

		@Override
		protected void onPreExecute() {
			mTvProgress.setText("loading...");
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			mPgBar.setProgress(values[0]);
			mTvProgress.setText("loading..." + values[0] + "%");
		}

		@Override
		protected String doInBackground(String... param)
		{
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.setCharset(Charset.forName(HTTP.UTF_8));//设置请求的编码格式
			builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);//设置浏览器兼容模式
			int count=0;
			for (File file:files) 
			{
//				FileBody fileBody = new FileBody(file);//把文件转换成流对象FileBody
//				builder.addPart("file"+count, fileBody);
				builder.addBinaryBody("file"+count, file);
				count++;
			}		
			builder.addTextBody("method", "upload");//设置请求参数
			builder.addTextBody("fileTypes", "image/gif, image/jpeg, image/pjpeg, image/pjpeg");//设置请求参数
			HttpEntity entity = builder.build();// 生成 HTTP POST 实体  	
//			int totalSize = entity.getContentLength();//获取上传文件的大小
//	        
//		     ProgressOutHttpEntity progressHttpEntity = new ProgressOutHttpEntity(
//		        		entity, new ProgressListener() {
//		                    @Override
//		                    public void transferred(long transferedBytes) {
//		                        publishProgress((int) (100 * transferedBytes / totalSize));//更新进度
//		                    }
//		                });
		     
		     return uploadFile(Constant.UPLOAD_URL, entity);

			}

		
		/**
		 * 向服务器上传文件
		 * @param url
		 * @param entity
		 * @return
		 */
		public String uploadFile(String url, HttpEntity entity) {				
			HttpClient httpClient=new DefaultHttpClient();// 开启一个客户端 HTTP 请求 
	        httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
	        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);// 设置连接超时时间
	        HttpPost httpPost = new HttpPost(url);//创建 HTTP POST 请求  
	        httpPost.setEntity(entity);
	        try {
	            HttpResponse httpResponse = httpClient.execute(httpPost);
	            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	                return "文件上传成功";
	            }
	        } catch (ClientProtocolException e) {
	            e.printStackTrace();
	        } catch (ConnectTimeoutException e) {
	            e.printStackTrace();
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            if (httpClient != null && httpClient.getConnectionManager() != null) {
	                httpClient.getConnectionManager().shutdown();
	            }
	        }
	        return "文件上传失败";
	    }

		
		public String post(String path, Map<String, String> params,
				FormFile[] files) throws Exception {
			final String BOUNDARY = "---------------------------7da2137580612"; // 数据分隔线
			final String endline = "--" + BOUNDARY + "--\r\n";// 数据结束标志
			SiteUtil.LOG_D(SocketHttpRequester.class, "upload--->post");
			int fileDataLength = 0;
			for (FormFile uploadFile : files) {// 得到文件类型数据的总长度
				SiteUtil.LOG_D(UploadActivity.class, uploadFile.getFilname());
				StringBuilder fileExplain = new StringBuilder();
				fileExplain.append("--");
				fileExplain.append(BOUNDARY);
				fileExplain.append("\r\n");
				fileExplain.append("Content-Disposition: form-data;name=\""+ uploadFile.getParameterName() + "\";filename=\""+ uploadFile.getFilname() + "\"\r\n");
				fileExplain.append("Content-Type: "+ uploadFile.getContentType() + "\r\n\r\n");
				fileExplain.append("\r\n");
				fileDataLength += fileExplain.length();
				if (uploadFile.getInStream() != null)
				{
					long len = uploadFile.getFile().length();
					SiteUtil.LOG_D(SocketHttpRequester.class, "File size:"+ len);
					if (uploadFile.getFile() != null) {
						fileDataLength += len;
					} else {
						fileDataLength += len;
					}
				} else {
					fileDataLength += uploadFile.getData().length;
				}
			}
			StringBuilder textEntity = new StringBuilder();
			for (Map.Entry<String, String> entry : params.entrySet()) {// 构造文本类型参数的实体数据
				textEntity.append("--");
				textEntity.append(BOUNDARY);
				textEntity.append("\r\n");
				textEntity.append("Content-Disposition: form-data; name=\""+ entry.getKey() + "\"\r\n\r\n");
				textEntity.append(entry.getValue());
				textEntity.append("\r\n");
			}
			// 计算传输给服务器的实体数据总长度
			int dataLength = textEntity.toString().getBytes().length+ fileDataLength + endline.getBytes().length;

			URL url = new URL(path);
			int port = url.getPort() == -1 ? 80 : url.getPort();
			Socket socket = new Socket(InetAddress.getByName(url.getHost()),
					port);
			Log.i("hbgz", "socket connected is " + socket.isConnected());
			OutputStream outStream = socket.getOutputStream();
			// 下面完成HTTP请求头的发送
			String requestmethod = "POST " + url.getPath() + " HTTP/1.1\r\n";
			outStream.write(requestmethod.getBytes());
			String accept = "Accept: image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*\r\n";
			outStream.write(accept.getBytes());
			String language = "Accept-Language: zh-CN\r\n";
			outStream.write(language.getBytes());
			String contenttype = "Content-Type: multipart/form-data; boundary="+ BOUNDARY + "\r\n";
			outStream.write(contenttype.getBytes());
			String contentlength = "Content-Length: " + dataLength + "\r\n";
			outStream.write(contentlength.getBytes());
			String alive = "Connection: Keep-Alive\r\n";
			outStream.write(alive.getBytes());
			String host = "Host: " + url.getHost() + ":" + port + "\r\n";
			outStream.write(host.getBytes());
			// 写完HTTP请求头后根据HTTP协议再写一个回车换行
			outStream.write("\r\n".getBytes());
			// 把所有文本类型的实体数据发送出来
			outStream.write(textEntity.toString().getBytes());
			// 把所有文件类型的实体数据发送出来
			int length = 0;
			for (FormFile uploadFile : files)
			{
				StringBuilder fileEntity = new StringBuilder();
		
				fileEntity.append("--");
				fileEntity.append(BOUNDARY);
				fileEntity.append("\r\n");
				fileEntity.append("Content-Disposition: form-data;name=\"" +uploadFile.getParameterName() + "\";filename=\"" +uploadFile.getFilname() + "\"\r\n");
				fileEntity.append("Content-Type: "+ uploadFile.getContentType() + "\r\n\r\n");
				outStream.write(fileEntity.toString().getBytes());
				System.out.println(fileEntity);
				if (uploadFile.getInStream() != null)
				{
					byte[] buffer = new byte[1024];
					int len = 0;
					
					while ((len = uploadFile.getInStream().read(buffer, 0, 1024)) != -1) 
					{
						outStream.write(buffer, 0, len);
						length += len;
						publishProgress((int) ((length / (float) dataLength) * 100));
					}
					uploadFile.getInStream().close();
				} else
				{
					outStream.write(uploadFile.getData(), 0,uploadFile.getData().length);
				}
				outStream.write("\r\n".getBytes());
			}
			// 下面发送数据结束标志，表示数据已经结束
			outStream.write(endline.getBytes());
			outStream.flush();
			InputStreamReader reader = new InputStreamReader(
					socket.getInputStream());
			int i = 0;
			char[] buffer = new char[1024];
			while ((i = reader.read(buffer)) != -1) {
				boolean requestCodeSuccess = false;
				boolean uploadSuccess = false;
				String str = new String(buffer);

				int start = str.trim().indexOf("{");
				int end = str.trim().indexOf("}");
				if (start > -1 && end > 0) {
					return str.substring(start, end + 1);
				}

			}
			outStream.flush();
			outStream.close();
			reader.close();
			socket.close();
			return null;

		}
	}

	public void submitResult(String json)
	{
		JsonParser jsonParser = new JsonParser();
		JsonElement jsonElement = jsonParser.parse(json);
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		String status = jsonObject.get("status").getAsString();
		if ("00".equals(status))
		{
			showMsg="上传成功";
		} else 
		{
			showMsg="上传失败,请重试";
		}
		showDialog();
	}

	public void showDialog() 
	{
		AlertDialog alertDialog = new AlertDialog.Builder(this).setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// TODO Auto-generated method stub
				for (int i = 0; i < PublicWay.activityList.size(); i++)
				{
					if (null != PublicWay.activityList.get(i)) 
					{
						PublicWay.activityList.get(i).finish();
					}
				}
				System.exit(0);
			}
					 
			}).setTitle("提示").setMessage(showMsg).create();
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.show();
	}
	
	public void Init() {

		pop = new PopupWindow(UploadActivity.this);

		View view = getLayoutInflater().inflate(R.layout.item_popupwindows,
				null);

		ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);

		pop.setWidth(LayoutParams.MATCH_PARENT);
		pop.setHeight(LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		pop.setContentView(view);

		RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
		Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
		Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
		Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
		parent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				photo();
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(UploadActivity.this,AlbumActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.activity_translate_in,
						R.anim.activity_translate_out);
				pop.dismiss();
				ll_popup.clearAnimation();
				finish();
			}
		});
		bt3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});

		noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new GridAdapter(this);
		adapter.update();
		noScrollgridview.setAdapter(adapter);
		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == Bimp.tempSelectBitmap.size()) {
					Log.i("ddddddd", "----------");
					ll_popup.startAnimation(AnimationUtils.loadAnimation(
							UploadActivity.this, R.anim.activity_translate_in));
					pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
				} else {
					Intent intent = new Intent(UploadActivity.this,
							GalleryActivity.class);
					intent.putExtra("position", "1");
					intent.putExtra("ID", arg2);
					startActivity(intent);
				}
			}
		});

	}

	@SuppressLint("HandlerLeak")
	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private int selectedPosition = -1;
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void update() {
			loading();
		}

		public int getCount() {
			if (Bimp.tempSelectBitmap.size() == 6) {
				return 6;
			}
			return (Bimp.tempSelectBitmap.size() + 1);
		}

		public Object getItem(int arg0) {
			return null;
		}

		public long getItemId(int arg0) {
			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_published_grida,
						parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView
						.findViewById(R.id.item_grida_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position == Bimp.tempSelectBitmap.size()) {
				holder.image.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.icon_addpic_unfocused));
				if (position == 6) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position)
						.getBitmap());
			}
			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}

		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					adapter.notifyDataSetChanged();
					break;
				}
				super.handleMessage(msg);
			}
		};

		public void loading() {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						if (Bimp.max == Bimp.tempSelectBitmap.size()) {
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
							break;
						} else {
							Bimp.max += 1;
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
						}
					}
				}
			}).start();
		}
	}

	public String getString(String s) {
		String path = null;
		if (s == null)
			return "";
		for (int i = s.length() - 1; i > 0; i++) {
			s.charAt(i);
		}
		return path;
	}

	protected void onRestart() {
		adapter.update();
		super.onRestart();
	}

	// 完成
	private static final int TAKE_PICTURE = 0x000001;

	public void photo() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PICTURE:
			if (Bimp.tempSelectBitmap.size() < 6 && resultCode == RESULT_OK) {

				String fileName = String.valueOf(System.currentTimeMillis());
				Bitmap bm = (Bitmap) data.getExtras().get("data");
				FileUtils.saveBitmap(bm, fileName);

				ImageItem takePhoto = new ImageItem();
				takePhoto.setBitmap(bm);
				Bimp.tempSelectBitmap.add(takePhoto);
			}
			break;
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			for (int i = 0; i < PublicWay.activityList.size(); i++) {
				if (null != PublicWay.activityList.get(i)) {
					PublicWay.activityList.get(i).finish();
				}
			}
			System.exit(0);
		}
		return true;
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			baiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				baiduMap.animateMapStatus(u);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}
}
