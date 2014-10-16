package com.dm.yx.view.faculty;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dm.yx.BaseActivity;
import com.dm.yx.MainPageActivity;
import com.dm.yx.R;
import com.dm.yx.model.Doctor;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class DoctorDetailActivity extends BaseActivity
{
	@ViewInject(R.id.title)
	private TextView title;
	@ViewInject(R.id.doctor_name)
	private TextView doctorName;
	@ViewInject(R.id.doctot_introduction)
	private TextView doctotIntroduction;
	@ViewInject(R.id.doctor_position)
	private TextView doctorPosition;
	@ViewInject(R.id.out_patient_time)
	private TextView outPatientTime;
	@ViewInject(R.id.out_patient_place)
	private TextView outPatientPlace;
	@ViewInject(R.id.guahao_fee)
	private TextView guahaoFee;
	@ViewInject(R.id.skill)
	private TextView skill;
	@ViewInject(R.id.doctor_photo)
	private ImageView photo;
	
	private BitmapUtils bitmapUtils;
	private Doctor  doctor;
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_doctor_detail);
		this.doctor=(Doctor) getIntent().getSerializableExtra("doctor");
		ViewUtils.inject(this);
		addActivity(this);
		initView();
		initValue();
	}

	@OnClick(R.id.back)
	public void toHome(View v)
	{
		Intent intent = new Intent(DoctorDetailActivity.this,MainPageActivity.class);
		startActivity(intent);
		exit();
	}
	
	@Override
	protected void initView()
	{
		// TODO Auto-generated method stub
		title.setText("医生详情");
	}

	@Override
	protected void initValue()
	{
		bitmapUtils = new BitmapUtils(this);
		bitmapUtils.closeCache();
		// TODO Auto-generated method stub
		this.doctorName.setText(this.doctor.getName());
		this.doctotIntroduction.setText(this.doctor.getIntroduce());
		this.skill.setText(this.doctor.getSkill());
		this.doctorPosition.setText(this.doctor.getPost());
		this.outPatientTime.setText(this.doctor.getWorkTime());
		String fee = this.doctor.getRegisterFee();
		if(null==fee ||  "".equals( fee ) || "null".equals( fee ))
		{
			fee="无";
		}else
		{
			fee+="元";
		}
		this.guahaoFee.setText(fee);
		this.outPatientPlace.setText(this.doctor.getWorkAddress());
		String photoUrl=this.doctor.getPhotoUrl();
		 if(photoUrl.endsWith("jpg") || photoUrl.endsWith("png"))
		 {
			 bitmapUtils.display(this.photo,photoUrl);
		 }
	}
	
	

}
