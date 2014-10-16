package com.dm.yx.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dm.yx.R;
import com.dm.yx.model.Doctor;
import com.dm.yx.model.DoctorList;
import com.lidroid.xutils.BitmapUtils;

public class ExpertListAdapter  extends BaseAdapter
{
	private Context mContext;

	private List<Doctor> doctors;

	private BitmapUtils bitmapUtils;
	
	public ExpertListAdapter(Context context, DoctorList doctorList)
	{
		this.mContext=context;
		this.doctors = doctorList.getDoctors();
		bitmapUtils = new BitmapUtils(mContext);
		bitmapUtils.closeCache();
	}

	@Override
	public int getCount()
	{
		if (doctors == null)
		{
			return 0;
		}
		return doctors.size();
	}

	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub  my_online_select btn_my_online
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		
			 convertView = LayoutInflater.from(mContext).inflate(R.layout.common_doctor_list_item, null);
			 TextView textView = (TextView)convertView.findViewById( R.id.comdoctor_name);
			 TextView facultyName = (TextView)convertView.findViewById( R.id.good_job);
			 TextView doctorPosition = (TextView)convertView.findViewById( R.id.comdoctor_position);
			 ImageView imageView = (ImageView) convertView.findViewById( R.id.doctor_photo);
			 String value=doctors.get(position).getName();
			 textView.setText(value);
			 doctorPosition.setText(doctors.get(position).getPost());
			 facultyName.setText(doctors.get(position).getSkill());
			 String photoUrl=doctors.get(position).getPhotoUrl();
			 if(photoUrl.endsWith("jpg") || photoUrl.endsWith("png"))
			 {
				 bitmapUtils.display(imageView,photoUrl);
			 }
		
		
		return convertView;
	}

}
