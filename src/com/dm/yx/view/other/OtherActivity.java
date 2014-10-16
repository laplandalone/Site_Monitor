package com.dm.yx.view.other;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dm.yx.BaseActivity;
import com.dm.yx.MainPageActivity;
import com.dm.yx.R;
import com.dm.yx.tools.HealthUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.socialize.bean.RequestType;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.sso.SmsHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

/**
 * 更多
 * 
 */
public class OtherActivity extends BaseActivity{
	@ViewInject(R.id.title)
	private TextView title;

	@ViewInject(R.id.soft_update)
	private LinearLayout soft_update;

	final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share",RequestType.SOCIAL);

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.other);
		ViewUtils.inject(this);
		addActivity(this);
		initView();
		// 设置分享内容
	}

	@Override
	protected void initView() 
	{
	   title.setText("更多");
	   mController.setShareContent("我正在使用‘掌上亚心’安卓版手机应用，手机预约专家号、专家在线与医生零距离沟通等功能等你体验！http://www.hiseemedical.com:10821/ImgWeb/DM_YX.apk");
		// 设置分享图片, 参数2为图片的url地址
//	   mController.setShareMedia(new UMImage(this,"http://www.umeng.com/images/pic/banner_module_social.png"));
		// 设置分享图片，参数2为本地图片的资源引用
//	   mController.setShareMedia(new UMImage(this, R.drawable.ic_launcher));
	   mController.getConfig().setPlatformOrder(SHARE_MEDIA.SMS,SHARE_MEDIA.QQ,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.SINA);
	   mController.getConfig().removePlatform( SHARE_MEDIA.RENREN, SHARE_MEDIA.DOUBAN,SHARE_MEDIA.TENCENT);
	   
	   //添加QQ平台
	   UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this,
				"100424468", "c7394704798a158208a74ab60104f0ba");
		
	    qqSsoHandler.setTargetUrl("http://www.hiseemedical.com:10821/ImgWeb/DM_YX.apk");
		qqSsoHandler.addToSocialSDK();
		
		//添加短信平台
		SmsHandler smsHandler = new SmsHandler();
		smsHandler.addToSocialSDK();
		
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(this,"wx655d7918ef562780");
		wxHandler.addToSocialSDK();
	}

	@Override
	protected void initValue() {
		// TODO Auto-generated method stub

	}

	@OnClick(R.id.share)
	public void share(View v)
	{
		
		mController.openShare(this, new SnsPostListener()
		{
			@Override
			public void onStart() 
			{
				if(SHARE_MEDIA.QQ==mController.getConfig().getSelectedPlatfrom())
				{
					
				}
			}

			@Override
			public void onComplete(SHARE_MEDIA platform, int eCode,
					SocializeEntity entity)
			{
			System.out.println("eCode:"+eCode);
			}
		});
	}

//	public void share(View v) {
//		Uri uri = Uri.parse("smsto://");
//		Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
//		intent.putExtra("sms_body",
//				this.getResources().getText(R.string.other_temp28));
//		startActivity(intent);
//	}

	@OnClick(R.id.back)
	public void toHome(View v) {
		Intent intent = new Intent(OtherActivity.this, MainPageActivity.class);
		startActivity(intent);
		exit();
	}

	@OnClick(R.id.soft_update)
	public void checkVersion(View v) {
		if (!isNetworkAvailable(this)) {
			HealthUtil.infoAlert(OtherActivity.this, "网络不可用，请检查！");
			return;
		}
		Intent intent = new Intent(this, CheckNewVersion.class);
		intent.putExtra("flag", "hand");
		startService(intent);
	}

	@OnClick(R.id.about_law)
	public void aboutLaw(View v) {
		Intent intent = new Intent(OtherActivity.this, WebActivity.class);
		startActivity(intent);
	}

	@OnClick(R.id.qanda)
	public void qanda(View v) {
		HealthUtil.infoAlert(OtherActivity.this, "正在建设中...");
	}

}
