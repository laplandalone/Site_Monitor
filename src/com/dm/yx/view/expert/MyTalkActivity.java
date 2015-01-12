package com.dm.yx.view.expert;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dm.yx.BaseActivity;
import com.dm.yx.MainPageActivity;
import com.dm.yx.R;
import com.dm.yx.adapter.TalkAdapter;
import com.dm.yx.model.UserQuestionT;
import com.dm.yx.tools.HealthConstant;
import com.dm.yx.tools.HealthUtil;
import com.dm.yx.upload.FormFile;
import com.dm.yx.upload.UploadThread;
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
	
	@ViewInject(R.id.img1)
	private ImageView img1;

	@ViewInject(R.id.img2)
	private ImageView img2;

	@ViewInject(R.id.img3)
	private ImageView img3;

	@ViewInject(R.id.frm1)
	private FrameLayout frm1;

	@ViewInject(R.id.frm2)
	private FrameLayout frm2;

	@ViewInject(R.id.frm3)
	private FrameLayout frm3;
	
	@ViewInject(R.id.layout1)
	private LinearLayout imagesLayout;
	
	private ListView list;
	private String userId;
	private String questionType;
	private String phone;
    private String doctorId;
    private String questionId;
    private UserQuestionT questionT;
    private List<UserQuestionT> questionTs;
    private TalkAdapter adapter;
	ArrayList<String> data = new ArrayList<String>();
	private ArrayList<String> imagesUrl = new ArrayList<String>();
	private String mPicName = "";
	private BitmapUtils bitmapUtils;
	private boolean restartBool = true;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.question_detail_talk);
		this.list=(ListView) findViewById(R.id.talklist);
		ViewUtils.inject(this);
		addActivity(this);
		if (savedInstanceState != null)
		{
			restartBool = savedInstanceState.getBoolean("is_restart", true);
			imagesUrl = savedInstanceState.getStringArrayList("image_url");
			mPicName = savedInstanceState.getString("pic_name");
		}
		initView();
		initValue();
		bitmapUtils = new BitmapUtils(this);
		bitmapUtils.configDefaultBitmapMaxSize(50, 50);
		bitmapUtils.clearCache();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		HealthUtil.LOG_D(getClass(), "onSaveInstanceState---->");
		outState.putBoolean("is_restart", false);
		outState.putStringArrayList("image_url", imagesUrl);
		outState.putString("pic_name", mPicName);
		super.onSaveInstanceState(outState);
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
		inputImg.setBackgroundResource(R.drawable.add_img);
//		inputImg.setVisibility(View.GONE);
		askAgain.setVisibility(View.VISIBLE);
		data.add("拍照");
		data.add("从相册选择");
	}
	
	@OnClick(R.id.input_img)
	public void addImage(View v)
	{
		new AlertDialog.Builder(MyTalkActivity.this).setTitle("提示").setIcon(android.R.drawable.ic_dialog_map)
				.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data), new DialogInterface.OnClickListener()
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
								intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(HealthConstant.IMG_PATH, mPicName)));
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
						case R.id.btn_cancel:
							finish();
							break;
						default:
							break;
						}
					}
				}).create().show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, intent);
		switch (requestCode)
		{
		case 0:
//			this.user = HealthUtil.getUserInfo();
//			if (this.user != null)
//			{
//				this.userTelephone = user.getTelephone();
//				this.userId = user.getUserId();
//			}
			break;
		case 1:
			File imageFile = new File(HealthConstant.IMG_PATH, mPicName);
			addImage(imageFile.getAbsolutePath());
			imagesLayout.setVisibility(View.VISIBLE);
			if (frm1.getVisibility() == 8)
			{
				frm1.setVisibility(View.VISIBLE);
				bitmapUtils.display(img1, imageFile.getAbsolutePath());
				break;
			} else if (frm2.getVisibility() == 8)
			{
				frm2.setVisibility(View.VISIBLE);
				bitmapUtils.display(img2, imageFile.getAbsolutePath());
				break;
			} else if (frm3.getVisibility() == 8)
			{
				frm3.setVisibility(View.VISIBLE);
				bitmapUtils.display(img3, imageFile.getAbsolutePath());
				break;
			} else
			{
				HealthUtil.infoAlert(MyTalkActivity.this, "最多可添加三张图片");
				break;
			}
		case 2:
			if (intent != null)
			{
				// 取得返回的Uri,基本上选择照片的时候返回的是以Uri形式，但是在拍照中有得机子呢Uri是空的，所以要特别注意
				Uri mImageCaptureUri = intent.getData();
				// 返回的Uri不为空时，那么图片信息数据都会在Uri中获得。如果为空，那么我们就进行下面的方式获取
				if (mImageCaptureUri != null)
				{
					Bitmap image;
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
						image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageCaptureUri);
						addImage(path);
						if (image != null)
						{
							imagesLayout.setVisibility(View.VISIBLE);
							if (frm1.getVisibility() == 8)
							{
								frm1.setVisibility(View.VISIBLE);
								img1.setImageBitmap(image);
								break;
							} else if (frm2.getVisibility() == 8)
							{
								frm2.setVisibility(View.VISIBLE);
								img2.setImageBitmap(image);
								break;
							} else if (frm3.getVisibility() == 8)
							{
								frm3.setVisibility(View.VISIBLE);
								img3.setImageBitmap(image);
								break;
							} else
							{
								HealthUtil.infoAlert(MyTalkActivity.this, "最多可添加三张图片");
								break;
							}
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

	private void addImage(String imagePath)
	{
		try
		{
			if (imagesUrl == null)
			{
				return;
			} else if (imagesUrl.size() < 4)
			{
				imagesUrl.add(imagePath);
			} else
			{
				HealthUtil.infoAlert(this, "照片个数已经达到上限，请删除之后新增");
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@OnClick(R.id.delete1)
	public void delete1(View v)
	{
		frm1.setVisibility(View.GONE);
		img1.setImageBitmap(null);
		imagesUrl.remove(0);
	}

	@OnClick(R.id.delete2)
	public void delete2(View v)
	{
		frm2.setVisibility(View.GONE);
		img2.setImageBitmap(null);
		imagesUrl.remove(1);
	}

	@OnClick(R.id.delete3)
	public void delete3(View v)
	{
		frm3.setVisibility(View.GONE);
		img3.setImageBitmap(null);
		imagesUrl.remove(2);
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
		 int imageSize = imagesUrl.size();
			FormFile[] formFiles = new FormFile[imageSize];
			for (int i = 0; i < imageSize; i++)
			{
				File imageFile = new File(imagesUrl.get(i));
				FormFile formFile = new FormFile(String.valueOf(new Date().getTime()) + i + ".jpg", imageFile, "image", "application/octet-stream");
				formFiles[i] = formFile;
			}
//		 RequestParams param = webInterface.addUserQuestion(questionStr);
//		 invokeWebServer(param, ADD_QUESTION);
			UploadThread uploadThread = new UploadThread(formFiles, mHandler, questionStr,HealthUtil.readHospitalId(),"ASK_IMG_PATH");
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
					showFailureDialog("提交失败，请核对信息之后重新提交");
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
	

	private void showSuccessDialog()
	{
		AlertDialog alertDialog = new AlertDialog.Builder(this).setPositiveButton("确定", new OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// TODO Auto-generated method stub
				finish();
			}
		}).setTitle("提示").setMessage("提交成功").create();
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.show();
	}
	
	private void submitResult(String result)
	{
		try
		{
			JSONObject jsonObject = new JSONObject(result);
			String executeType = jsonObject.get("executeType").toString();
			if ("success".equals(executeType))
			{
				deleteFile();
				showSuccessDialog();
			} else
			{
				showFailureDialog("提交失败，请核对信息之后重新提交");
				return;
			}
		} catch (JSONException e)
		{
			HealthUtil.infoAlert(this, "返回结果解析失败");
			e.printStackTrace();
		} catch (Exception e)
		{
			HealthUtil.infoAlert(this, "提交失败");
			e.printStackTrace();
		}
	}
	
	private void deleteFile()
	{
		try
		{
			if (restartBool)
			{
				File file = new File(HealthConstant.IMG_PATH);
				File[] files = file.listFiles();
				if (files == null)
				{
					return;
				}
				for (File tempFile : files)
				{
					tempFile.delete();
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();

		}
	}
	
	private void showFailureDialog(String msg)
	{

		AlertDialog alertDialog = new AlertDialog.Builder(this).setPositiveButton("确定", null).setTitle("失败提醒").setMessage(msg).create();
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.show();
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
			httpHandler.cancel();
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
