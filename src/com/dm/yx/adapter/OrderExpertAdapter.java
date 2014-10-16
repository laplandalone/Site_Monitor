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

public class OrderExpertAdapter  extends BaseAdapter
{
	private Context mContext;

	private List<OrderExpert> orders;

	private String day="";
	public OrderExpertAdapter(Context context, OrderExpertList expertList)
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
		 View view = convertView;
		 ViewHolder viewHolder = null;

		 viewHolder = new ViewHolder();
		 view = LayoutInflater.from(mContext).inflate(R.layout.expert_doctor_list_item, null);		
		 viewHolder.textView = (TextView)view.findViewById( R.id.name);
		 viewHolder.dateView = (TextView)view.findViewById( R.id.date);
		 viewHolder.weekView = (TextView)view.findViewById( R.id.week);
		 ImageView img = (ImageView) view.findViewById(R.id.time_circle);
		 OrderExpert  expert=orders.get(position);
		 String value=expert.getDoctorName();
		 String weekDay=expert.getDay();
		 String week=expert.getWeek();
		 String display=expert.getDisplay();
		 viewHolder.textView.setText(value);
	
		 if("Y".equals(display))
		 {
			 img.setBackgroundResource(R.drawable.time_item_circle);
			 viewHolder.weekView.setVisibility(View.VISIBLE);
			 viewHolder.dateView.setVisibility(View.VISIBLE);
			 viewHolder.dateView.setText(weekDay);
			 viewHolder.weekView.setText("星期"+week);
		 }
		return view;
	}
	

	private class ViewHolder
	{
		public TextView textView;
		public TextView dateView;
		public TextView weekView;
	}
}
