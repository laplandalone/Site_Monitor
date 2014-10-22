package com.dm.yx;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.dm.yx.adapter.ImgViewPager;
import com.dm.yx.model.HospitalT;
import com.dm.yx.tools.HealthConstant;
import com.dm.yx.tools.HealthUtil;
import com.dm.yx.view.other.CheckNewVersion;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 
 * @author Lapland_Alone
 *
 */
public class WelcomeActivity extends BaseActivity
{
	
	@ViewInject(R.id.hospital)
	private ImageView hospital;

	@ViewInject(R.id.logo)
	private ImageView logo;

	@ViewInject(R.id.line1)
	private LinearLayout layout1;
	
	@ViewInject(R.id.line2)
	private LinearLayout layout2;

	@ViewInject(R.id.hospitalName)
	private TextView hospitalName;
	
	@ViewInject(R.id.remark)
	private TextView remark;
	
	@ViewInject(R.id.description)
	private TextView description;
	
	@ViewInject(R.id.imgViewPager)
	ImgViewPager myPager; // 图片容器

	@ViewInject(R.id.vb)
	LinearLayout ovalLayout; // 圆点容器
	
	private List<View> listViews; // 图片组
	private List <HospitalT> hospitalTs;
	private BitmapUtils bitmapUtils;
	
	int currentNum=0;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_hospital_page);
		bitmapUtils = new BitmapUtils(this);
		bitmapUtils.closeCache();
		ViewUtils.inject(this);
		addActivity(this);
		bindPush();
		initView();
		initValue();
		System.out.println(HealthUtil.readPushChannelId());
		System.out.println(HealthUtil.readPushUserId());
		Intent intent = new Intent(this, CheckNewVersion.class);
		intent.putExtra("flag", "auto");
		startService(intent);
	}
	
	/**
	 * bind push server 
	 * @param 
	 * @return void
	 * author xuchun
	 */
    private void bindPush() {
    	if (!HealthUtil.readBindPush()) {
    		HealthUtil.LOG_D(getClass(), "bind push server ing...");
    		PushManager.startWork(this, PushConstants.LOGIN_TYPE_API_KEY, HealthConstant.PUSH_KEY);
    	}
    }
	
	/**
	 * 初始化图片
	 */
	private void initViewPager()
	{
		
		listViews = new ArrayList<View>();
		
		String [] imgNames= new String[]{"a.jpg"};
		
		for(int i = 0; i < imgNames.length; i++)
		{
			ImageView imageView = new ImageView(this);
//			bitmapUtils.configDefaultLoadingImage(R.drawable.default_loading_img);
//			bitmapUtils.configDefaultLoadFailedImage(R.drawable.load_failure);
			bitmapUtils.display(imageView,HealthConstant.imgUrl+"/welcome/"+imgNames[i]);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			listViews.add(imageView);
			
		}
//		int[] imageResId = new int[]
//		{ R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d};
//		for (int i = 0; i < imageResId.length; i++)
//		{
//			ImageView imageView = new ImageView(this);
//			imageView.setImageResource(imageResId[i]);
//			imageView.setScaleType(ScaleType.CENTER_CROP);
//			listViews.add(imageView);
//		}
	}
	
	
	/**
	 * 清华阳光医院：hospital_id:101
	 * @param v
	 */
	@OnClick(R.id.line1)
	public void toMain(View v)
	{
		Intent intent = new Intent(WelcomeActivity.this, MainPageActivity.class);
		HealthUtil.writeHospitalId("102");
		startActivity(intent);
		finish();
	}
	
	
	@OnClick(R.id.phone114)
	public void toPhone(View v)
	{
		Intent intent = new Intent();
		// 激活源代码,添加intent对象
		intent.setAction("android.intent.action.CALL");
		intent.setData(Uri.parse("tel:114"));
		// 激活Intent
		startActivity(intent);
	}

	@Override
	protected void initView()
	{
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		int screenWidth = display.getWidth();
		int scrrenHeight = display.getHeight();
		
		int  spacedip480=12;
		int  spacedip720=12;
		int  imgPagerHeigth=0;
		int intersectionPoint480=35;//大圆和小圆交集长度
		int intersectionPoint720=22;//大圆和小圆交集长度
		int maxCircle480=30;//大圆比小圆大 诊疗服务
		int maxCircle720=30;//大圆比小圆大 诊疗服务
		int space=0;
		int intersectionPoint=0;//大圆和小圆交集长度
		int maxCircle=0;//大圆比小圆大 诊疗服务
		
		if(screenWidth==480)
		{
			space=dip2px(this, spacedip480);
			maxCircle=dip2px(this, maxCircle480);
			intersectionPoint=dip2px(this, intersectionPoint480);
			imgPagerHeigth=dip2px(this, 40);//40：title高度+间隔高度
		}else
		{
			space=dip2px(this, spacedip720);
			maxCircle=dip2px(this, maxCircle720);
			intersectionPoint=dip2px(this, intersectionPoint720);
			imgPagerHeigth=dip2px(this, 30);//30:title高度+间隔高度
		}
		int spaceX =space*5;//其他服务下，小圆圈间隔
		int minCircleWhith=(screenWidth-spaceX)/4;//其他服务下，小圆圈图片宽度
		int minCircleHeight=minCircleWhith*190/160;// 其他服务下，小圆圈图片高度 ： 190/160图片比例
		int myPagerHight=scrrenHeight-minCircleHeight*5-imgPagerHeigth;
		myPager.setLayoutParams(new LinearLayout.LayoutParams(screenWidth,myPagerHight));
		
		layout2.setLayoutParams(new LinearLayout.LayoutParams(screenWidth,scrrenHeight-screenWidth-intersectionPoint));
		
	
		
		initViewPager();// 初始化图片
		myPager.start(this, listViews, 4000, ovalLayout, R.layout.ad_bottom_item, R.id.ad_item_v,
				R.drawable.pager_select, R.drawable.pager_item);
	}

	public static int dip2px(Context context, float dipValue)
	{
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public static int px2dip(Context context, float pxValue)
	{
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	
	@Override
	protected void initValue()
	{
//
//		dialog.setMessage("正在加载,请稍后...");
//		dialog.show();
//		RequestParams param = webInterface.getHospitals("");
//		invokeWebServer(param, GET_LIST);
	}
	

	
	
}
