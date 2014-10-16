package com.dm.yx.view.expert;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dm.yx.BaseActivity;
import com.dm.yx.MainPageActivity;
import com.dm.yx.R;
import com.dm.yx.adapter.MyQuestionListAdapter;
import com.dm.yx.model.Doctor;
import com.dm.yx.model.User;
import com.dm.yx.model.UserQuestionT;
import com.dm.yx.tools.HealthConstant;
import com.dm.yx.tools.HealthUtil;
import com.dm.yx.view.user.LoginActivity;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class QuestionActivity extends BaseActivity implements OnItemClickListener
{
	@ViewInject(R.id.title)
	private TextView title;
	@ViewInject(R.id.contentnull)
	private RelativeLayout layout;
	@ViewInject(R.id.submit)
	private Button submitBtn;
	private ListView list;
	String doctorId;
	String userId;
	String questionType = "";
	List<UserQuestionT> questionTs;
	User user;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.online_question_list);
		this.list = (ListView) findViewById(R.id.list);
		// TODO Auto-generated method stub 
		ViewUtils.inject(this);
		addActivity(this);
		initValue();
		initView();
	}

	@OnClick(R.id.submit)
	public void submitQestion(View v)
	{
		Intent intent = new Intent(QuestionActivity.this, AskQuestionMsgActivity.class);
		intent.putExtra("doctorId", doctorId);
		startActivity(intent);

	}

	@OnClick(R.id.back)
	public void toHome(View v)
	{
		Intent intent = new Intent(QuestionActivity.this, MainPageActivity.class);
		startActivity(intent);
		exit();
	}

	@Override
	protected void initView()
	{
		title.setText("我的提问");
	}

	@Override
	protected void initValue()
	{
		// TODO Auto-generated method stub
		dialog.setMessage("正在加载,请稍后...");
		dialog.show();
		
		this.questionType = getIntent().getStringExtra("questionType");
		if ("expert".equals(questionType))
		{
			Doctor doctor = (Doctor) getIntent().getSerializableExtra("doctor");
			this.doctorId = doctor.getDoctorId();
			RequestParams param = webInterface.getUserQuestionsByDoctorId(doctorId);
			invokeWebServer(param, ADD_QUESTION);
		} else if ("user".equals(questionType))
		{
			this.user = HealthUtil.getUserInfo();
			if (this.user == null)
			{
				Intent intent = new Intent(QuestionActivity.this, LoginActivity.class);
				startActivityForResult(intent, 0);
			} else
			{
				this.userId = user.getUserId();
				submitBtn.setVisibility(View.GONE);
				RequestParams param = webInterface.getUserQuestionsByUserId(userId,HealthUtil.readHospitalId());
				invokeWebServer(param, ADD_QUESTION);
			}

		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, intent);
		switch (requestCode)
		{
		case 0:
			this.user = HealthUtil.getUserInfo();
			if (this.user != null)
			{
				this.userId = user.getUserId();
				submitBtn.setVisibility(View.GONE);
				RequestParams param = webInterface.getUserQuestionsByUserId(userId,HealthUtil.readHospitalId());
				invokeWebServer(param, ADD_QUESTION);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 链接web服务  核对信息
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

			HealthUtil.infoAlert(QuestionActivity.this, "信息加载失败，请检查网络后重试");
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
		String executeType = jsonObject.get("executeType").getAsString();
		if (!"success".equals(executeType))
		{
			HealthUtil.infoAlert(QuestionActivity.this, "加载失败请重试.");
			return;
		}
		JsonArray jsonArray = jsonObject.getAsJsonArray("returnMsg");
		Gson gson = new Gson();
		this.questionTs = gson.fromJson(jsonArray, new TypeToken<List<UserQuestionT>>()
		{
		}.getType());
		MyQuestionListAdapter adapter = new MyQuestionListAdapter(QuestionActivity.this, questionTs);
		this.list.setAdapter(adapter);
		this.list.setOnItemClickListener(this);
		if(this.questionTs.size()==0)
		{
			layout.setVisibility(View.VISIBLE);
			list.setVisibility(View.GONE);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		// TODO Auto-generated method stub
		Intent intent = new Intent(QuestionActivity.this, MyTalkActivity.class);
		UserQuestionT questionT = this.questionTs.get(position);
		Bundle bundle = new Bundle();
		bundle.putSerializable("questioin", questionT);
		intent.putExtras(bundle);
		intent.putExtra("questionType", questionType);
		startActivity(intent);
	}

}
