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
import com.dm.yx.model.OrderExpert;
import com.dm.yx.model.OrderExpertList;

public class ExpertDetailAdapter extends BaseAdapter
{

	private Context mContext;

	private List<OrderExpert> orders;

	public ExpertDetailAdapter(Context context, OrderExpertList expertList)
	{
		this.mContext=context;
		this.orders = expertList.getOrders();
	}

	@Override
	public int getCount()
	{
		if (orders == null)
		{
			return 0;
		}
		return orders.size();
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
			 convertView = LayoutInflater.from(mContext).inflate(R.layout.doctor_schedul_list_item, null);
			 TextView textView =  (TextView)convertView.findViewById( R.id.text1);
			 TextView textView2 = (TextView)convertView.findViewById( R.id.text22);
			 TextView textView3 = (TextView)convertView.findViewById( R.id.text3);
			 TextView textView4 = (TextView)convertView.findViewById( R.id.text4);
			 ImageView localImageView = (ImageView)convertView.findViewById(R.id.icon);
			 OrderExpert expert=orders.get(position);
			 /*已预约标示元*/
			 if("Y".equals(expert.getUserFlag()))
			 {
				 localImageView.setBackgroundResource(R.drawable.order);
				 localImageView.setVisibility(View.VISIBLE);
			 }else
			 if("true".equals(expert.getNumMax()))
			 {
				 localImageView.setVisibility(View.VISIBLE);
				 localImageView.setBackgroundResource(R.drawable.reservation_icon);
			 }
			 textView.setText(expert.getWorkTime());
			 textView2.setText("2");
			 textView3.setText(expert.getUserOrderNum());
			 textView4.setText(expert.getFee()+"元");
		
		return convertView;
	}


}
