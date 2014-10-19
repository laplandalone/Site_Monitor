package com.dm.yx.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dm.yx.R;
import com.dm.yx.model.UserQuestionT;

public class QuestionDoctorListAdapter extends BaseAdapter
{
	private Context mContext;

	private List<UserQuestionT> questionTs;

	public QuestionDoctorListAdapter(Context context, List<UserQuestionT> questionTs)
	{
		this.mContext = context;
		this.questionTs = questionTs;
	}

	@Override
	public int getCount()
	{
		if (questionTs == null)
		{
			return 0;
		}
		return questionTs.size();
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
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		
			convertView = LayoutInflater.from(mContext).inflate(R.layout.online_doctor_question_item, null);
			TextView textView = (TextView) convertView.findViewById(R.id.telephone);
			TextView createTime = (TextView) convertView.findViewById(R.id.create_time);
			TextView content = (TextView) convertView.findViewById(R.id.contentItem);
			UserQuestionT questionT = questionTs.get(position);
			String phone = questionT.getUserTelephone()+"";
			String phone1 = "";
			if(phone.length()==11)
			{
				phone1 = phone.substring(0, 3);
				phone = phone.substring(7, 11);
			}
			textView.setText(phone1 + " **** " + phone);
			createTime.setText(questionT.getCreateDate());
			content.setText(questionT.getContent());
			// doctorPosition.setText(doctors.get(position).getPost());
			// facultyName.setText(doctors.get(position).getSkill());
	
		return convertView;
	}

}
