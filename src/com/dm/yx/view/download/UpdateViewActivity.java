package com.dm.yx.view.download;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.TypedValue;
import android.widget.TextView;
import com.dm.yx.BaseActivity;
import com.dm.yx.R;
import com.dm.yx.tools.HealthUtil;

public class UpdateViewActivity extends BaseActivity {

	private ProgressDialog pDialog;
	private String downUrl="";
	private String remark="";
	private String appName="";
	private Context mContext=this;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
	
		super.onCreate(savedInstanceState);
		pDialog = getProgressDialog(this);
		pDialog.setCancelable(false);
		pDialog.setCanceledOnTouchOutside(false);
		pDialog.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				 
				UpdateViewActivity.this.finish();
			}
		});
		
		initView();
		initValue();
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initValue() {
		appName=getIntent().getStringExtra("appName");
		downUrl=getIntent().getStringExtra("url");
		remark=getIntent().getStringExtra("remark");
		// TODO Auto-generated method stub
		showCheckNewVersionView("", "");
	}

	
	/*检测到新版本弹窗*/
	@SuppressLint("ResourceAsColor")
	private void showCheckNewVersionView(final String uri,String flag) {
		try {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.checkUpdate_title);
//			builder.setIcon(R.drawable.ic_launcher);
			TextView text = new TextView(this);
			text.setPadding(5, 5, 5, 5);
			text.setTextColor(R.color.TextColorGreen);
			text.setText(remark);
			text.setAutoLinkMask(Linkify.WEB_URLS);
			text.setMovementMethod(LinkMovementMethod.getInstance());
			text.setTextSize(TypedValue.COMPLEX_UNIT_PX,this.getResources().getDimensionPixelSize(R.dimen.large_text_size));
			text.getPaint().setFakeBoldText(true);
			builder.setView(text);
		
			builder.setPositiveButton(getString(R.string.checkUpdate_updateNow), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					short shortFlag=0;
					HealthUtil.startDownload(downUrl,mContext,appName,"10MB",shortFlag);
					UpdateViewActivity.this.finish();
				}
			});
			if (!"Y".equalsIgnoreCase(flag)) {
				builder.setNegativeButton(getString(R.string.checkUpdate_updatelater), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
					 
						UpdateViewActivity.this.finish();
					}
				});
			}
			Dialog dialog = builder.create();
			if ("Y".equalsIgnoreCase(flag)) {
				dialog.setCancelable(false);
			}
			dialog.setCanceledOnTouchOutside(false);
			dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
				
				@Override
				public void onCancel(DialogInterface dialog) {
					UpdateViewActivity.this.finish();
					
				}
			});
			dialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
