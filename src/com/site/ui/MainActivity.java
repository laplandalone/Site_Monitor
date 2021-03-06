package com.site.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapPoi;
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
import com.site.tools.ImageItem;
import com.site.tools.PublicWay;
import com.site.tools.Res;
import com.site.tools.SiteUtil;
import com.site.upload.FileUploadAsyncTask;
import com.site.upload.FormFile;

/**
 * 首页面activity
 * 
 * @version 2014年10月18日 下午11:48:34
 */
public class MainActivity extends BaseActivity {

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
	private File[] files;
	private View upView;
	private String mPicName;
	/* 定位相关 */
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private BitmapDescriptor mCurrentMarker;
	private MapView mMapView;
	private BaiduMap baiduMap;
	boolean isFirstLoc = true;// 是否首次定位
	private String lineIds;
	private ProgressDialog pd;
	
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
//		baiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(72000);
		mLocClient.setLocOption(option);
		String l=SiteUtil.getLongitude();
		String ln=SiteUtil.getLatitude();
		double lng = Double.parseDouble(l);
		double lan =Double.parseDouble(SiteUtil.getLatitude());
		LatLng cenpt = new LatLng(lan, lng);

		// 普通地图
		baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		// 开启交通图线
//		baiduMap.setTrafficEnabled(true);
		// 设置比例尺
		
		baiduMap.setOnMapClickListener(listener);
		mLocClient.start();

		baiduMap.setOnMapStatusChangeListener( new BaiduMap.OnMapStatusChangeListener() {
            /**
             * 手势操作地图，设置地图状态等操作导致地图状态开始改变。
             * @param status 地图状态改变开始时的地图状态
             */
            public void onMapStatusChangeStart(MapStatus status){
            }
            /**
             * 地图状态变化中
             * @param status 当前地图状态
             */
            public void onMapStatusChange(MapStatus status){
            }
            /**
             * 地图状态改变结束
             * @param status 地图状态改变结束后的地图状态
             */
            public void onMapStatusChangeFinish(MapStatus status)
            {
                LatLng ll=status.target;
                SiteUtil.writeLongitude(ll.longitude+"");
                SiteUtil.writeLatitude(ll.latitude+"");
                Log.d("map change","sts ch fs:"+ll.latitude+","+ll.longitude+"");
            }
        });
	}

	@Override
	protected void initValue() {
		// TODO Auto-generated method stub

	}

	@OnClick(R.id.input_img)
	public void submit(View v)
	{
		pd = new ProgressDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("上传中....");
		pd.setCancelable(false);
		pd.show();
		ArrayList<ImageItem> imageItems = Bimp.tempSelectBitmap;
		formFiles = new FormFile[imageItems.size()];
		files=new File[imageItems.size()];
		for (int i=0;i<imageItems.size();i++) 
		{
			ImageItem item=imageItems.get(i);
			Bitmap bitmap = item.getBitmap();
			String path = item.getImagePath();
			SiteUtil.compressBitmap(bitmap, path);
			File imageFile = new File(path);
			files[i]=imageFile;
		}
		FileUploadAsyncTask task = new FileUploadAsyncTask(this,pd);
		task.execute(files);
	}

	private String getPicName() {
		return new Date().getTime() + ".jpg";
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

		pop = new PopupWindow(MainActivity.this);

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
				Intent intent = new Intent(MainActivity.this,AlbumActivity.class);
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
							MainActivity.this, R.anim.activity_translate_in));
					pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
				} else {
					Intent intent = new Intent(MainActivity.this,
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
		mPicName=getPicName();
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(new File(Constant.IMG_PATH+mPicName)));
		startActivityForResult(intent, TAKE_PICTURE);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PICTURE:
			if (Bimp.tempSelectBitmap.size() < 6 && resultCode == RESULT_OK) {

				String fileName = String.valueOf(System.currentTimeMillis());
//				Bitmap bm = (Bitmap) data.getExtras().get("data");
//				FileUtils.saveBitmap(bm, fileName);
				File imageFile = new File(Constant.IMG_PATH, mPicName);
				File file = new File(imageFile.getAbsolutePath());
				// SiteUtil.compressBitmapT(imageFile.getAbsolutePath(), mPicName);
				if (file.exists())
				{
					Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
					ImageItem takePhoto = new ImageItem();
					takePhoto.setBitmap(bitmap);
					takePhoto.setImagePath(imageFile.getAbsolutePath());
					Bimp.tempSelectBitmap.add(takePhoto);
				}
				
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
//				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
//				baiduMap.animateMapStatus(u);
				baiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().target(ll).zoom(16).build()));
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}
	
	 OnMapClickListener listener = new OnMapClickListener()  {

		@Override
		public void onMapClick(LatLng arg0)
		{
			// TODO Auto-generated method stub
			System.out.println(arg0.latitude);
			System.out.println(arg0.longitude);
//			mLocClient.setLocOption(LatLng);
		}

		@Override
		public boolean onMapPoiClick(MapPoi arg0) {
			// TODO Auto-generated method stub
			return false;
		}  
		    
		};
}
