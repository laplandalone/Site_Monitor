package com.dm.yx.view.expert;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dm.yx.BaseActivity;
import com.dm.yx.MainPageActivity;
import com.dm.yx.R;
import com.dm.yx.adapter.QuestionDoctorListAdapter;
import com.dm.yx.adapter.ViewPagerAdapter;
import com.dm.yx.model.Doctor;
import com.dm.yx.model.UserQuestionT;
import com.dm.yx.tools.HealthConstant;
import com.dm.yx.tools.HealthUtil;
import com.dm.yx.tools.ObjectCensor;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

@SuppressLint({ "ResourceAsColor", "NewApi" })
public class TabQuestionActivity extends BaseActivity implements OnItemClickListener
{
	@ViewInject(R.id.title)
	private TextView title;
	@ViewInject(R.id.submit)
	private Button submitBtn;
	@ViewInject(R.id.vPager)
	private ViewPager viewPager;
	@ViewInject(R.id.settings)
	private ImageView settings;
	private ListView list;
	private RelativeLayout contentnull;
	String doctorId;
	String userId;
	String questionType = "";
	List<UserQuestionT> questionTs;
	private Doctor doctor;
	private View view1, view2;// 需要滑动的页卡
	private List<View> viewList;//把需要滑动的页卡添加到这个list中  

	@ViewInject(R.id.tv_tab_hot)
	TextView tabHot;
	
	@ViewInject(R.id.tv_tab_Major)
	TextView tabMajor;
	
	private BitmapUtils bitmapUtils;
	
	@OnClick(R.id.submit)
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.online_doctor_detail);
		ViewUtils.inject(this);
		addActivity(this);
		bitmapUtils = new BitmapUtils(this);
		bitmapUtils.closeCache();
		initView();
		initValue();
	
		
	}

	@OnClick(R.id.settings)
	public void toMyQuestion(View v)
	{
		Intent intent = new Intent(TabQuestionActivity.this,QuestionActivity.class);
		intent.putExtra("questionType", "user");
		startActivity(intent);
	}
	
	@OnClick(R.id.submit)
	public void submitQestion(View v)
	{
		Intent intent = new Intent(TabQuestionActivity.this, AskQuestionMsgActivity.class);
		intent.putExtra("doctorId", doctorId);
		startActivity(intent);

	}

	@OnClick(R.id.back)
	public void toHome(View v)
	{
		Intent intent = new Intent(TabQuestionActivity.this, MainPageActivity.class);
		startActivity(intent);
		exit();
	}


	@OnClick(R.id.tv_tab_hot)
	public void toDoctor(View v)
	{
		  viewPager.setCurrentItem(0); 
		//  tabHot.setTextColor(R.color.white);
		  tabHot.setBackgroundResource(R.drawable.btn_main);
		//  tabMajor.setTextColor(R.color.tab_menu);
		  tabMajor.setBackground(null);
	}

	@OnClick(R.id.tv_tab_Major)
	public void toQuestion(View v)
	{
		  viewPager.setCurrentItem(1); 
	//	  tabMajor.setTextColor(R.color.white);
		//  tabHot.setTextColor(R.color.tab_menu);
		  tabHot.setBackground(null);
		  tabMajor.setBackgroundResource(R.drawable.btn_main);
	}
	
	@OnClick(R.id.tv_tab_my)
	public void toTabmy(View v)
	{
		HealthUtil.infoAlert(TabQuestionActivity.this, "正在建设中");
	}
	
	
	@Override
	protected void initView()
	{
		// TODO Auto-generated method stub
		title.setText("在线提问");
		settings.setVisibility(View.VISIBLE);
		settings.setBackgroundResource(R.drawable.btn_my_online);
		tabMajor.setBackgroundResource(R.drawable.btn_main);
		this.doctor=(Doctor) getIntent().getSerializableExtra("doctor");
		LayoutInflater lf = getLayoutInflater().from(this);
		
		view1 = lf.inflate(R.layout.tab_doctor_detail, null);
		view2 = lf.inflate(R.layout.tab_online_question_list, null);
		
		TextView doctorName=(TextView) view1.findViewById(R.id.doctor_name);
		doctorName.setText(doctor.getName());
		TextView introddoction=(TextView) view1.findViewById(R.id.doctot_introduction);
		introddoction.setText(doctor.getIntroduce()); 
		TextView skill=(TextView) view1.findViewById(R.id.skill);
		skill.setText(doctor.getSkill()); 
		TextView position=(TextView) view1.findViewById(R.id.doctor_position);
		position.setText(doctor.getPost()); 
		TextView outPatientTime=(TextView) view1.findViewById(R.id.out_patient_time);
		outPatientTime.setText(doctor.getWorkTime());
		TextView outPatientPlace=(TextView) view1.findViewById(R.id.out_patient_place);
		outPatientPlace.setText(doctor.getWorkAddress());
		TextView guahaoFee=(TextView) view1.findViewById(R.id.guahao_fee);
		String fee=doctor.getRegisterFee();
		if(ObjectCensor.isStrRegular(fee))
		{
			guahaoFee.setText(doctor.getRegisterFee()+"元");
		}else
		{
			guahaoFee.setText("无");
		}
		
		ImageView imageView = (ImageView) view1.findViewById( R.id.doctor_photo);
		String photoUrl=doctor.getPhotoUrl();
		if(photoUrl.endsWith("jpg") || photoUrl.endsWith("png"))
		 {
			 bitmapUtils.display(imageView,photoUrl);
		 }
		this.list = (ListView) view2.findViewById(R.id.list);
		contentnull = (RelativeLayout) view2.findViewById(R.id.contentnull);
		Button questionBtn = (Button) view2.findViewById(R.id.submit);
		
		viewList = new ArrayList<View>();// 将要分页显示的View装入数组中
		viewList.add(view1);
		viewList.add(view2);
		viewPager.setAdapter(new ViewPagerAdapter(viewList));  
        viewPager.setCurrentItem(1); 
        viewPager.setOnPageChangeListener(new OnPageChangeListener()
		{
			@Override
			public void onPageSelected(int arg0)
			{
				// TODO Auto-generated method stub
				if(arg0==0)
				{
					  tabHot.setBackgroundResource(R.drawable.btn_main);
					  tabMajor.setBackground(null);
				}else if(arg0==1)
				{
					tabHot.setBackground(null);
				    tabMajor.setBackgroundResource(R.drawable.btn_main);
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2)
			{
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0)
			{
				// TODO Auto-generated method stub
			}
		});
        
        questionBtn.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(TabQuestionActivity.this, AskQuestionMsgActivity.class);
				intent.putExtra("doctorId", doctor.getDoctorId());
				intent.putExtra("teamId", doctor.getTeamId());
				startActivity(intent);
			}
		});
	}

	@Override
	protected void initValue()
	{
		// TODO Auto-generated method stub
		dialog.setMessage("正在加载,请稍后...");
		dialog.show();
		
		Doctor doctor = (Doctor) getIntent().getSerializableExtra("doctor");
		this.doctorId = doctor.getDoctorId();
		RequestParams param = webInterface.getUserQuestionsByDoctorId(doctorId);
		invokeWebServer(param, ADD_QUESTION);

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

			HealthUtil.infoAlert(TabQuestionActivity.this, "信息加载失败，请检查网络后重试");
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
			case ADD_QUESTION:
				returnMsg(arg0.result, ADD_QUESTION);
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
		JsonArray jsonArray = jsonObject.getAsJsonArray("returnMsg");
		Gson gson = new Gson();
		this.questionTs = gson.fromJson(jsonArray, new TypeToken<List<UserQuestionT>>()
		{
		}.getType());
		QuestionDoctorListAdapter adapter = new QuestionDoctorListAdapter(TabQuestionActivity.this, questionTs);
		this.list.setAdapter(adapter);
		this.list.setOnItemClickListener(this);
		if (this.questionTs.size() == 0) {
			list.setVisibility(View.GONE);
			contentnull.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		// TODO Auto-generated method stub
		Intent intent = new Intent(TabQuestionActivity.this, TalkActivity.class);
		UserQuestionT questionT = this.questionTs.get(position);
		Bundle bundle = new Bundle();
		bundle.putSerializable("questioin", questionT);
		intent.putExtras(bundle);
		intent.putExtra("questionType", "expert");
		startActivity(intent);
	}

}
