package com.dm.yx.view.order;

import java.util.Map;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alipay.android.app.sdk.AliPay;
import com.dm.yx.BaseActivity;
import com.dm.yx.MainPageActivity;
import com.dm.yx.R;
import com.dm.yx.tools.HealthConstant;
import com.dm.yx.tools.HealthUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
/**
 * 
 * 挂号预约
 *
 */
public class ConfirmOrderActivity extends BaseActivity
{

	@ViewInject(R.id.title)
	private TextView title;

	@ViewInject(R.id.mark)  
	private LinearLayout mark;
	
	@ViewInject(R.id.faculty_name)  
	private TextView falcultyNameT;
	
	@ViewInject(R.id.doctor_name) 
	private TextView doctorNameT;
	
	@ViewInject(R.id.date)     
	private TextView dateT;
	
	@ViewInject(R.id.date_time)   
	private TextView dateTimeT;
	
	@ViewInject(R.id.register_num)
	private TextView registerNumT;
	
	@ViewInject(R.id.user_name)   
	private TextView userNameT;
	
	@ViewInject(R.id.sex)         
	private TextView sexT;
	
	@ViewInject(R.id.confirm_num) 
	private TextView comfirmNumT;
	
	@ViewInject(R.id.confirm_price)
	private TextView confirmPriceT;
	
	@ViewInject(R.id.sex)
	private TextView sexTV;
	
	@ViewInject(R.id.confirm_num)
	private TextView confirmNumTV;
	
	@ViewInject(R.id.userOrderNum)
	private LinearLayout linearLayout;
	
	@ViewInject(R.id.teamDoctorId)
	private LinearLayout linearLayout1;
	
	@ViewInject(R.id.feelayout)
	private LinearLayout linearLayout2;
	
	@ViewInject(R.id.order_cancel)
	private Button orderCancel;
	
	@ViewInject(R.id.taobao)
	private Button taobao;
	
	@ViewInject(R.id.order_cancel_line)
	private LinearLayout order_cancel_line;
	
	@ViewInject(R.id.cancel_single)
	private LinearLayout cancel_single;
	
	private String orderId="";
	private String orderState="";
	private String payState="100";/*初始未支付*/
	private String handState="00A";
	private String orderHospitalId="";
	
	private static final int RQF_PAY = 1;   //支付宝支付
	private static final int GET_ORDER_INFO = 2;   //加密
	private static final int RECHARGE = 3;   //充值
	private Map<String,String> map;  //解析支付宝返回结果后的map
	String msg="";
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.expert_register_proof);
		ViewUtils.inject(this);
		addActivity(this);
		initView();
		initValue();
	}
	
	@OnClick(R.id.back)
	public void toHome(View v)
	{
		Intent intent = new Intent(ConfirmOrderActivity.this,MainPageActivity.class);
		startActivity(intent);
		exit();
	}
	
	@OnClick(R.id.order_cancel)
	public void cancel(View v)
	{		
		dialog.setMessage("正在取消,请稍后...");
		dialog.show();
		handState="00X";
		RequestParams param = webInterface.orderPay(orderId, handState);
		invokeWebServer(param, PAY_STATE);
	}
	
	@OnClick(R.id.order_cancel_single)
	public void cancelSingle(View v)
	{		
//		dialog.setMessage("正在取消,请稍后...");
//		dialog.show();
//		handState="00X";
//		RequestParams param = webInterface.orderPay(orderId, "00X");
//		invokeWebServer(param, PAY_STATE);
		
		Uri uri = Uri.parse("smsto://");
		Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
		intent.putExtra("sms_body",msg);
		startActivity(intent);
		
	}
	
	@OnClick(R.id.taobao)
	public void pay(View v)
	{		
		dialog.setMessage("正在取消,请稍后...");
		dialog.show();
		RequestParams param = webInterface.getRsaSign(this.orderId);
		invokeWebServer(param, RSA_SIGN);
	}
	
   
	/**
	 * 调用手机支付宝进行支付，支付成功后会返回结果信息
	 * @param orderInfo
	 */
	public void alipay(final String orderInfo) {
		new Thread() {
			public void run() {
				AliPay alipay = new AliPay(ConfirmOrderActivity.this, handler);
				//设置为沙箱模式，不设置默认为线上环境
//				alipay.setSandBox(true);
				String result = alipay.pay(orderInfo);
				Message msg = new Message();
				msg.what = RQF_PAY;
				msg.obj = result;
				handler.sendMessage(msg);
			}
		}.start();
	}
	
	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			map = HealthUtil.parserAliResult(msg.obj.toString());
			String message = map.get("memo");
			if(map.containsKey("resultStatus"))
			{
				if("9000".equals(map.get("resultStatus")))/*支付成功*/
				{
					handState="00A";
					RequestParams param = webInterface.orderPay(orderId, "00A");
					invokeWebServer(param, PAY_STATE);
				}else
				{
//					 System.out.println("2");
				}
			}
			super.handleMessage(msg);
		}
	};

	
	@Override
	protected void initView()
	{
		title.setText("预约详情");
		// TODO Auto-generated method stub
		this.orderState=getIntent().getStringExtra("orderState");
		this.payState= getIntent().getStringExtra("payState");
		this.orderHospitalId=getIntent().getStringExtra("hospitalId");
		this.orderId=getIntent().getStringExtra("orderId");
		String registerNum=getIntent().getStringExtra("userOrderNum");
		String fee=getIntent().getStringExtra("fee");
		String doctorName=getIntent().getStringExtra("doctorName");
		if("0".equals(registerNum) || "".equals(registerNum))
		{
			linearLayout.setVisibility(View.GONE);
		}
		if("0".equals(fee) || "".equals(fee))
		{
			linearLayout1.setVisibility(View.GONE);
		}
		if("0".equals(doctorName) || "".equals(doctorName))
		{
			linearLayout2.setVisibility(View.GONE);
		}
		
		doctorNameT.setText(doctorName); 
		dateT.setText(getIntent().getStringExtra("registerTime"  )); 
		confirmPriceT.setText(fee); 
		registerNumT.setText(registerNum); 
		falcultyNameT.setText(	getIntent().getStringExtra("teamName"      )); 
		userNameT.setText(getIntent().getStringExtra("userName"      )); 
		dateTimeT.setText(getIntent().getStringExtra("userNo"        )); 
		sexTV.setText(getIntent().getStringExtra("sex" )); 
		confirmNumTV.setText(getIntent().getStringExtra("userTelephone" ));

		msg=userNameT.getText().toString()+"您已成功预约"+dateT.getText()+falcultyNameT.getText().toString()+"就诊！";
	}

	@Override
	protected void initValue()
	{
		
		if(!"00X".equals(orderState))
		{
			if("101".equals(payState))
			{
				cancel_single.setVisibility(View.VISIBLE);
			}else
			{
				order_cancel_line.setVisibility(View.VISIBLE);
			}
			
			if("101".equals(orderHospitalId))
			{
				mark.setVisibility(View.GONE);
			}
		}
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
			httpHandler.stop();
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
			HealthUtil.infoAlert(ConfirmOrderActivity.this, "信息加载失败，请检查网络后重试");
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
			case PAY_STATE:
				returnMsg(arg0.result, PAY_STATE);
				break;
			case RSA_SIGN:
				returnMsg(arg0.result, RSA_SIGN);
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
		
		switch (code)
		{
		    case RSA_SIGN:
		    	JsonObject rtn = jsonObject.getAsJsonObject("returnMsg");	
		    	String sign=rtn.get("sign").getAsString();
		    	System.out.println(sign);
		    	alipay(sign);
		    	break;
		    case PAY_STATE:	
		      String payRst=jsonObject.get("returnMsg").getAsString();
		      if("true".equals(payRst))
		      {
		    	  if("00A".equals(handState))
		    	  {
		    		  HealthUtil.infoAlert(ConfirmOrderActivity.this, "支付成功");
		    	  }else
		    	  {
		    		  HealthUtil.infoAlert(ConfirmOrderActivity.this, "取消成功");
		    	  }
		    	 
		    	  orderCancel.setVisibility(View.GONE);
		    	  taobao.setVisibility(View.GONE);
		      }
		      break;
		      default:
		}
		
	}
}
