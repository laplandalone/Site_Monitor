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

public class MyQuestionListAdapter extends BaseAdapter
{
	private Context mContext;

	private List<UserQuestionT> questionTs;

	public MyQuestionListAdapter(Context context, List<UserQuestionT> questionTs)
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
		
			convertView = LayoutInflater.from(mContext).inflate(R.layout.online_user_question_item, null);
			TextView doctorName = (TextView) convertView.findViewById(R.id.doctorName);
			TextView question = (TextView) convertView.findViewById(R.id.question);
			TextView createDate = (TextView) convertView.findViewById(R.id.createDate);
			UserQuestionT questionT = questionTs.get(position);
			doctorName.setText(questionT.getName());
			question.setText(questionT.getContent());
			createDate.setText(questionT.getCreateDate());
		
		return convertView;
	}

}
