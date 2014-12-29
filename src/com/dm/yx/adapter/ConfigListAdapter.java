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
import com.dm.yx.model.HospitalConfigT;
import com.dm.yx.model.HospitalNewsT;
import com.lidroid.xutils.BitmapUtils;

public class ConfigListAdapter extends BaseAdapter
{
	private Context mContext;

	private List<HospitalConfigT> hospitalConfigTs;

	private BitmapUtils bitmapUtils;
	
	public ConfigListAdapter(Context context, List<HospitalConfigT> hospitalConfigTs)
	{
		this.mContext = context;
		this.hospitalConfigTs = hospitalConfigTs;
		bitmapUtils = new BitmapUtils(mContext);
		bitmapUtils.closeCache();
	}
	
	public List<HospitalConfigT> getHospitalConfigTs() {
		return hospitalConfigTs;
	}

	public void setHospitalConfigTs(List<HospitalConfigT> hospitalConfigTs) {
		this.hospitalConfigTs = hospitalConfigTs;
	}


	@Override
	public int getCount()
	{
		if (hospitalConfigTs == null)
		{
			return 0;
		}
		return hospitalConfigTs.size();
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
		 HospitalConfigT hospitalConfigT=hospitalConfigTs.get(position);
		 String visitName=hospitalConfigT.getConfigVal();
		 textView.setText(visitName);
	     return convertView;
	}

}
