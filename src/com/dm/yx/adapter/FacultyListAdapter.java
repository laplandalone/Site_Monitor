package com.dm.yx.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dm.yx.R;
import com.dm.yx.model.Team;
import com.dm.yx.model.TeamList;

public class FacultyListAdapter extends BaseAdapter
{
	private Context mContext;

	private List<Team> teams ;

	public FacultyListAdapter(Context context,  TeamList unhandList)
	{
		this.mContext = context;
		this.teams = unhandList.getTeams();
	}

	@Override
	public int getCount()
	{
		if (teams == null)
		{
			return 0;
		}
		return teams.size();
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
		
			convertView = LayoutInflater.from(mContext).inflate(R.layout.img_list_item, null);
			TextView textView = (TextView) convertView.findViewById(R.id.comtext1);
			String value = teams.get(position).getTeamName().toString();
			textView.setText(value);
		
		return convertView;
	}

}