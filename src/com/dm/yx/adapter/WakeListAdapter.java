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
import com.dm.yx.model.WakeT;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class WakeListAdapter  extends BaseAdapter
{
	private Context mContext;

	private List<WakeT> wakeTs;
	public WakeListAdapter(Context context,List<WakeT> wakeTs)
	{
		this.mContext = context;
		this.wakeTs = wakeTs;
	}

	@Override
	public int getCount()
	{
		if (wakeTs == null)
		{
			return 0;
		}
		return wakeTs.size();
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
		 convertView = LayoutInflater.from(mContext).inflate(R.layout.common_list_two_item, null);
		 TextView textView =  (TextView)convertView.findViewById( R.id.text1);
		 WakeT wakeT = wakeTs.get(position);
		 textView.setText(wakeT.getWakeName());
		 return convertView;
	}

}
