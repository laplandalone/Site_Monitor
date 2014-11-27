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
import com.dm.yx.model.RegisterOrderT;
import com.dm.yx.model.UserContactT;
import com.dm.yx.model.UserRelateT;

public class RelateListAdapter extends BaseAdapter
{
	private Context mContext;

	private List<UserRelateT> relateTs;

	public List<UserRelateT> getRelateTs() {
		return relateTs;
	}

	public void setRelateTs(List<UserRelateT> relateTs) {
		this.relateTs = relateTs;
	}

	public RelateListAdapter(Context context, List<UserRelateT> relateTs)
	{
		this.mContext = context;
		this.relateTs = relateTs;
	}

	@Override
	public int getCount()
	{
		if (relateTs == null)
		{
			return 0;
		}
		return relateTs.size();
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
			 convertView = LayoutInflater.from(mContext).inflate(R.layout.contact_list_item, null);
			 TextView textView =  (TextView)convertView.findViewById( R.id.comtext1);
			 UserRelateT relateT=relateTs.get(position);
			 
			 String phone= relateT.getRelatePhone();
			 String name=relateT.getRelateName();
			 if(name==null || "".equals(name))
			 {
				 textView.setText(phone);
			 }else
			 {
				 textView.setText(name);
			 }
		
		return convertView;
	}

}
