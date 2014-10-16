package com.dm.yx.view.expert;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.dm.yx.BaseActivity;
import com.dm.yx.MainPageActivity;
import com.dm.yx.R;
import com.dm.yx.adapter.TalkAdapter;
import com.dm.yx.model.UserQuestionT;
import com.dm.yx.tools.HealthConstant;
import com.dm.yx.tools.HealthUtil;
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

/**
 * 我的提问：可回复
 * @author Lapland_Alone
 *
 */
public class MyTalkActivity extends BaseActivity
{

	@ViewInject(R.id.title)
	private TextView title;
	
	@ViewInject(R.id.input_img)
	private  Button inputImg;
	
	@ViewInject(R.id.ask_again_text)
	private EditText askAgain;
	
	private ListView list;
	private String userId;
	private String questionType;
	private String phone;
    private String doctorId;
    private String questionId;
    private UserQuestionT questionT;
    private List<UserQuestionT> questionTs;
    private TalkAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.question_detail_talk);
		this.list=(ListView) findViewById(R.id.talklist);
		ViewUtils.inject(this);
		addActivity(this);
		initView();
		initValue();
	}

	@OnClick(R.id.back)
	public void toHome(View v)
	{
		Intent intent = new Intent(MyTalkActivity.this,MainPageActivity.class);
		startActivity(intent);
		exit();
	}
	
	@Override
	protected void initView()
	{
		// TODO Auto-generated method stub
		this.title.setText(R.string.ask_online_temp45);
		inputImg.setVisibility(View.GONE);
		askAgain.setVisibility(View.VISIBLE);
	}

	@OnClick(R.id.submit_question)
	public void submitQuestion(View v)
	{
		String content = askAgain.getText().toString();
		
		if("".equals(content))
		{
			HealthUtil.infoAlert(this, "内容为空");
			return;
		}
		
		 dialog.setMessage("正在提交,请稍后...");
		 dialog.show();

		 this.questionT = new UserQuestionT();

		 this.questionT.setQuestionId(this.questionId);
		 this.questionT.setUserId(this.userId);
		 this.questionT.setDoctorId(this.doctorId);
		 this.questionT.setUserTelephone(this.phone);
		 this.questionT.setAuthType("public");
		 this.questionT.setRecordType("copy");
		 this.questionT.setContent(content);
		Gson gson = new Gson();
		String questionStr = gson.toJson(this.questionT);
		RequestParams param = webInterface.addUserQuestion(questionStr);
		invokeWebServer(param, ADD_QUESTION);
	}
	@Override
	protected void initValue()
	{
		// TODO Auto-generated method stub
		dialog.setMessage("正在加载,请稍后...");
		dialog.show();
		this.questionType=getIntent().getStringExtra("questionType");
		UserQuestionT questionT = (UserQuestionT) getIntent().getSerializableExtra("questioin");
		this.userId=questionT.getUserId();
		this.doctorId=questionT.getDoctorId();
		this.phone=questionT.getUserTelephone();
		this.questionId=questionT.getQuestionId();
		RequestParams param = webInterface.getUserQuestionsByIds(questionId);
		invokeWebServer(param, GET_LIST);
		
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
			
			HealthUtil.infoAlert(MyTalkActivity.this, "信息加载失败，请检查网络后重试");
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
			case GET_LIST:
				returnMsg(arg0.result, GET_LIST);
				break;
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
		switch (code)
		{
		    case GET_LIST:
		    	
				JsonArray jsonArray = jsonObject.getAsJsonArray("returnMsg");
				Gson gson = new Gson();  
				this.questionTs = gson.fromJson(jsonArray, new TypeToken<List<UserQuestionT>>(){}.getType());  
				 adapter = new TalkAdapter(MyTalkActivity.this, this.questionTs);
				list.setAdapter(adapter);
				break;
		    case ADD_QUESTION:
		    	String rstFlag = jsonObject.get("returnMsg").toString();
		    	if("true".equals(rstFlag))
		    	{
		    		askAgain.setText("");
		    		this.questionTs.add(this.questionT);
		    		adapter.notifyDataSetChanged();
		    	}else
		    	{
		    		HealthUtil.infoAlert(MyTalkActivity.this,"提交失败,请重试...");
		    	}
		    	break;
		}
		
	}


}
