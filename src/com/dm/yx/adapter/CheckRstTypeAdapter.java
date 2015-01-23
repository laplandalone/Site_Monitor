package com.dm.yx.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dm.yx.R;
import com.dm.yx.model.CheckRstT;
import com.lidroid.xutils.BitmapUtils;

public class CheckRstTypeAdapter extends BaseAdapter 
{
	private Context mContext;

	private List<CheckRstT> checkRstTs;

	
	public CheckRstTypeAdapter(Context context, List<CheckRstT> checkRstTs)
	{
		this.mContext = context;
		this.checkRstTs = checkRstTs;
	}
	



	@Override
	public int getCount()
	{
		if (checkRstTs == null)
		{
			return 0;
		}
		return checkRstTs.size();
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
		
			 convertView = LayoutInflater.from(mContext).inflate(R.layout.check_rst_type_item, null);
			 TextView textView =  (TextView)convertView.findViewById( R.id.text1);
			 CheckRstT rstT=checkRstTs.get(position);
			// localImageView.setVisibility(0);
			 textView.setText(rstT.getCheck_type());
			 
		
		return convertView;
	}

}
