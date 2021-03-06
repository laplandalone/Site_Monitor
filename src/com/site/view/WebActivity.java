package com.site.view;

import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup.OnCheckedChangeListener;
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
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.site.BaseActivity;
import com.site.R;
import com.site.tools.Constant;
import com.site.tools.SiteUtil;
import com.site.upload.FormFile;
import com.site.upload.SocketHttpRequester;
import com.site.upload.UploadThread;

public class WebActivity extends BaseActivity {
	private ProgressBar mPgBar;
	private TextView mTvProgress;

	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	BitmapDescriptor mCurrentMarker;
	MapView mMapView;
	private BaiduMap baiduMap;

	// UI相关
	OnCheckedChangeListener radioButtonListener;
	Button requestLocButton;
	boolean isFirstLoc = true;// 是否首次定位

	@ViewInject(R.id.title)
	private TextView title;
	WebView web;

	@ViewInject(R.id.editUser)
	private TextView editUser;

	@ViewInject(R.id.site)
	private TextView site;

	ArrayList<String> data = new ArrayList<String>();
	private Map<String, String> imagesUrl = new HashMap<String, String>();
	private String mPicName = "";
	private BitmapUtils bitmapUtils;

	private FormFile[] formFiles;
	private MyTask mTask;
	private View upView;

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

	private String currentImg = "frm1";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.visit_detail_webview);

		ViewUtils.inject(this);
		addActivity(this);
		initView();
		initValue();

		upView = getLayoutInflater().inflate(R.layout.filebrowser_uploading,
				null);
		mPgBar = (ProgressBar) upView
				.findViewById(R.id.pb_filebrowser_uploading);
		mTvProgress = (TextView) upView
				.findViewById(R.id.tv_filebrowser_uploading);

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

	@Override
	protected void initView() {
		title.setText("上报站点信息");
		site.setText("线路列表");
		editUser.setText("");

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

		File imagepath = new File(Constant.IMG_PATH);

		if (!imagepath.exists()) {
			Log.i("zou", "imagepath.mkdir()");
			imagepath.mkdir();
		}
	}

	class Li implements OnClickListener {
		@Override
		public void onClick(View v) {
			currentImg = v.getTag() + "";
			// TODO Auto-generated method stub
			new AlertDialog.Builder(WebActivity.this)
					.setTitle("提示")
					.setIcon(android.R.drawable.ic_dialog_map)
					.setAdapter(
							new ArrayAdapter<String>(WebActivity.this,
									android.R.layout.simple_list_item_1, data),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									getPicName();
									switch (which) {
									case 0:
										try {
											Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
											intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(new File(Constant.IMG_PATH,mPicName)));

											startActivityForResult(intent, 1);
										} catch (Exception e) {
											e.printStackTrace();
										}
										break;
									case 1:
										try {
											Intent intent = new Intent();
											intent.setType("image/*");
											intent.setAction(Intent.ACTION_GET_CONTENT);
											startActivityForResult(intent, 2);
										} catch (ActivityNotFoundException e) {

										}
										break;
									// case R.id.btn_cancel:
									// finish();
									// break;
									default:
										break;
									}
								}
							}).create().show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		// TODO Auto-generated method stub
		Bitmap image = null;
		super.onActivityResult(requestCode, resultCode, intent);
		switch (requestCode) {
		case 1:
			File imageFile = new File(Constant.IMG_PATH, mPicName);
			File file = new File(imageFile.getAbsolutePath());
			// SiteUtil.compressBitmapT(imageFile.getAbsolutePath(), mPicName);
			if (file.exists()) {
				addImage(currentImg, imageFile.getAbsolutePath());
				imagesLayout.setVisibility(View.VISIBLE);
				if ("frm1".equals(currentImg)) {
					bitmapUtils.display(img1, imageFile.getAbsolutePath());
					frm2.setVisibility(View.VISIBLE);
					delete1.setVisibility(View.VISIBLE);
					frm1.setOnClickListener(null);
					img1.setOnClickListener(new deleteImg());
					break;
				} else if ("frm2".equals(currentImg)) {
					frm2.setVisibility(View.VISIBLE);
					bitmapUtils.display(img2, imageFile.getAbsolutePath());
					frm3.setVisibility(View.VISIBLE);
					delete2.setVisibility(View.VISIBLE);
					img2.setOnClickListener(new deleteImg());
					break;
				} else if ("frm3".equals(currentImg)) {
					frm3.setVisibility(View.VISIBLE);
					bitmapUtils.display(img3, imageFile.getAbsolutePath());
					frm4.setVisibility(View.VISIBLE);
					delete3.setVisibility(View.VISIBLE);
					img3.setOnClickListener(new deleteImg());
					break;
				}
				if ("frm4".equals(currentImg)) {
					frm4.setVisibility(View.VISIBLE);
					bitmapUtils.display(img4, imageFile.getAbsolutePath());
					frm5.setVisibility(View.VISIBLE);
					delete4.setVisibility(View.VISIBLE);
					img4.setOnClickListener(new deleteImg());
					break;

				} else if ("frm5".equals(currentImg)) {
					frm5.setVisibility(View.VISIBLE);
					bitmapUtils.display(img5, imageFile.getAbsolutePath());
					frm6.setVisibility(View.VISIBLE);
					delete5.setVisibility(View.VISIBLE);
					img5.setOnClickListener(new deleteImg());
					break;
				} else if ("frm6".equals(currentImg)) {
					frm6.setVisibility(View.VISIBLE);
					bitmapUtils.display(img6, imageFile.getAbsolutePath());
					delete6.setVisibility(View.VISIBLE);
					img6.setOnClickListener(new deleteImg());
					break;
				} else {
					SiteUtil.infoAlert(WebActivity.this, "最多可添加6张图片");
					break;
				}
			}
		case 2:
			if (intent != null) {
				// 取得返回的Uri,基本上选择照片的时候返回的是以Uri形式，但是在拍照中有得机子呢Uri是空的，所以要特别注意
				Uri mImageCaptureUri = intent.getData();
				// 返回的Uri不为空时，那么图片信息数据都会在Uri中获得。如果为空，那么我们就进行下面的方式获取
				if (mImageCaptureUri != null) {
					try {
						String path = getPath(this, mImageCaptureUri);

						if (path == null || "".equals(path)) {
							String[] proj = { MediaStore.Images.Media.DATA };
							Cursor cursor = managedQuery(mImageCaptureUri,
									proj, null, null, null);
							// 按我个人理解 这个是获得用户选择的图片的索引值
							int column_index = cursor
									.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
							// 将光标移至开头 ，这个很重要，不小心很容易引起越界
							cursor.moveToFirst();
							// 最后根据索引值获取图片路径
							path = cursor.getString(column_index);
							// 这个方法是根据Uri获取Bitmap图片的静态方法

							image = SiteUtil.compressBitmapT(path, mPicName);
						}

						else {
							image = SiteUtil.compressBitmapT(path, mPicName);
							// SiteUtil.compressBitmapT(image,mPicName);
							// selectImage(this, intent);
						}
						// SiteUtil.compressBitmapT(path, mPicName);
						addImage(currentImg, path);
						if ("frm1".equals(currentImg)) {
							img1.setImageBitmap(image);
							img1.setOnClickListener(null);
							frm2.setVisibility(View.VISIBLE);
							delete1.setVisibility(View.VISIBLE);
							img1.setOnClickListener(new deleteImg());
							break;
						} else if ("frm2".equals(currentImg)) {
							frm2.setVisibility(View.VISIBLE);
							img2.setImageBitmap(image);
							frm3.setVisibility(View.VISIBLE);
							delete2.setVisibility(View.VISIBLE);
							img2.setOnClickListener(new deleteImg());
							break;
						} else if ("frm3".equals(currentImg)) {
							frm3.setVisibility(View.VISIBLE);
							img3.setImageBitmap(image);
							currentImg = "frm4";
							frm4.setVisibility(View.VISIBLE);
							delete3.setVisibility(View.VISIBLE);
							delete3.setTag("frm3");
							img3.setTag("frm3");
							img3.setOnClickListener(new deleteImg());
							break;
						}
						if ("frm4".equals(currentImg)) {
							frm4.setVisibility(View.VISIBLE);
							img4.setImageBitmap(image);
							frm5.setVisibility(View.VISIBLE);
							delete4.setVisibility(View.VISIBLE);
							img4.setOnClickListener(new deleteImg());
							break;

						} else if ("frm5".equals(currentImg)) {
							frm5.setVisibility(View.VISIBLE);
							img5.setImageBitmap(image);
							frm6.setVisibility(View.VISIBLE);
							delete5.setVisibility(View.VISIBLE);
							img5.setOnClickListener(new deleteImg());
							break;
						} else if ("frm6".equals(currentImg)) {
							frm6.setVisibility(View.VISIBLE);
							img6.setImageBitmap(image);
							delete6.setVisibility(View.VISIBLE);
							img6.setOnClickListener(new deleteImg());
							break;
						} else {
							SiteUtil.infoAlert(WebActivity.this, "最多可添加6张图片");
							break;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			break;
		default:
			break;
		}
	}

	@SuppressLint("NewApi")
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

				final String selection = "_id=?";
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
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
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
		final String column = "_data";
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

	private String getPicName() {
		mPicName = new Date().getTime() + ".jpg";
		return mPicName;
	}

	class deleteImg implements OnClickListener {
		@Override
		public void onClick(View v) {
			String tag = v.getTag() + "";
			imagesUrl.remove(tag);
			if ("frm1".equals(tag)) {
				delete1.setVisibility(View.GONE);
				img1.setImageBitmap(null);
				img1.setOnClickListener(new Li());
			} else if ("frm2".equals(tag)) {
				delete2.setVisibility(View.GONE);
				img2.setImageBitmap(null);
				img2.setOnClickListener(new Li());
			} else if ("frm3".equals(tag)) {
				delete3.setVisibility(View.GONE);
				img3.setImageBitmap(null);
				img3.setOnClickListener(new Li());
			} else if ("frm4".equals(tag)) {
				delete4.setVisibility(View.GONE);
				img4.setImageBitmap(null);
				img4.setOnClickListener(new Li());
			} else if ("frm5".equals(tag)) {
				delete5.setVisibility(View.GONE);
				img5.setImageBitmap(null);
				img5.setOnClickListener(new Li());
			} else if ("frm6".equals(tag)) {
				delete6.setVisibility(View.GONE);
				img6.setImageBitmap(null);
				img6.setOnClickListener(new Li());
			}
		}
	}

	private void addImage(String key, String imagePath) {
		try {
			if (imagesUrl == null) {
				return;
			} else if (imagesUrl.size() < 7) {
				imagesUrl.put(key, imagePath);
			} else {
				SiteUtil.infoAlert(this, "照片个数已经达到上限，请删除之后新增");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadUrl(String url) {
		if (web != null) {
			web.loadUrl(url);
		}
	}

	@Override
	protected void initValue() {
		bitmapUtils = new BitmapUtils(this);
		bitmapUtils.configDefaultBitmapMaxSize(50, 50);
		bitmapUtils.clearCache();
		// TODO Auto-generated method stub
		data.add("拍照");
		data.add("从相册选择");

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

	@OnClick(R.id.input_img)
	public void submitQuestion(View v) {
		int imageSize = imagesUrl.size();
		formFiles = new FormFile[imageSize];
		int i = 0;
		for (String key : imagesUrl.keySet()) {
			File imageFile = new File(imagesUrl.get(key));
			FormFile formFile = new FormFile(String.valueOf(new Date()
					.getTime()) + i + ".jpg", imageFile, "image",
					"application/octet-stream");
			formFiles[i] = formFile;
			i++;
		}
		new AlertDialog.Builder(WebActivity.this).setTitle("上传进度")
				.setView(upView).create().show();
		mTask = new MyTask();
		mTask.execute();
		// UploadThread uploadThread = new UploadThread(formFiles, mHandler,
		// SiteUtil.getCity(), lineIds, "555", SiteUtil.getStopName(),
		// SiteUtil.getStopId(),
		// SiteUtil.getLongitude(),SiteUtil.getLatitude());
		// new Thread(uploadThread).start();
	}

	public void submitResult(String json) {
		JsonParser jsonParser = new JsonParser();
		JsonElement jsonElement = jsonParser.parse(json);
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		String status = jsonObject.get("status").getAsString();
		if ("00".equals(status)) {
			SiteUtil.infoAlert(WebActivity.this, "上传成功");
			finish();
		} else {
			SiteUtil.infoAlert(WebActivity.this, "上传失败,请重试");
		}
	}

	@OnClick(R.id.site)
	public void site(View v) {
		finish();
	}

	@OnClick(R.id.cancel)
	public void cancel(View v) {
		finish();
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// 退出时销毁定位
		mLocClient.stop();
		// 关闭定位图层
		baiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
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
			System.out.println(values[0]);
			mTvProgress.setText("loading..." + values[0] + "%");
		}

		@Override
		protected String doInBackground(String... param) {
			String lineIds = getIntent().getStringExtra("lineIds");
			Map<String, String> params = new HashMap<String, String>();
			params.put("cityId", SiteUtil.getCity());
			params.put("linelist", lineIds);
			params.put("stopName", SiteUtil.getStopName());
			params.put("stopId", SiteUtil.getStopId());
			params.put("jingdu", SiteUtil.getLongitude());
			params.put("weidu", SiteUtil.getLatitude());

			try {
				return post(Constant.UPLOAD_URL, params, formFiles);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		public String post(String path, Map<String, String> params,
				FormFile[] files) throws Exception {
			final String BOUNDARY = "---------------------------7da2137580612"; // 数据分隔线
			final String endline = "--" + BOUNDARY + "--\r\n";// 数据结束标志
			SiteUtil.LOG_D(SocketHttpRequester.class, "upload--->post");
			int fileDataLength = 0;
			for (FormFile uploadFile : files) {// 得到文件类型数据的总长度
				StringBuilder fileExplain = new StringBuilder();
				fileExplain.append("--");
				fileExplain.append(BOUNDARY);
				fileExplain.append("\r\n");
				fileExplain.append("Content-Disposition: form-data;name=\""
						+ uploadFile.getParameterName() + "\";filename=\""
						+ uploadFile.getFilname() + "\"\r\n");
				fileExplain.append("Content-Type: "
						+ uploadFile.getContentType() + "\r\n\r\n");
				fileExplain.append("\r\n");
				fileDataLength += fileExplain.length();
				if (uploadFile.getInStream() != null) {
					long len = uploadFile.getFile().length();
					SiteUtil.LOG_D(SocketHttpRequester.class, "File size:"
							+ len);
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
				textEntity.append("Content-Disposition: form-data; name=\""
						+ entry.getKey() + "\"\r\n\r\n");
				textEntity.append(entry.getValue());
				textEntity.append("\r\n");
			}
			// 计算传输给服务器的实体数据总长度
			int dataLength = textEntity.toString().getBytes().length
					+ fileDataLength + endline.getBytes().length;

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
			String contenttype = "Content-Type: multipart/form-data; boundary="
					+ BOUNDARY + "\r\n";
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

			for (FormFile uploadFile : files) {
				StringBuilder fileEntity = new StringBuilder();
				fileEntity.append("--");
				fileEntity.append(BOUNDARY);
				fileEntity.append("\r\n");
				fileEntity.append("Content-Disposition: form-data;name=\""
						+ uploadFile.getParameterName() + "\";filename=\""
						+ uploadFile.getFilname() + "\"\r\n");
				fileEntity.append("Content-Type: "
						+ uploadFile.getContentType() + "\r\n\r\n");
				outStream.write(fileEntity.toString().getBytes());
				if (uploadFile.getInStream() != null) {
					byte[] buffer = new byte[1024];
					int len = 0;
					int length = 0;
					while ((len = uploadFile.getInStream()
							.read(buffer, 0, 1024)) != -1) {
						outStream.write(buffer, 0, len);
						length += len;
						publishProgress((int) ((length / (float) dataLength) * 100));
					}
					uploadFile.getInStream().close();
				} else {
					outStream.write(uploadFile.getData(), 0,
							uploadFile.getData().length);
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

}
