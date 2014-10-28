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

public class UserOrderListAdapter extends BaseAdapter
{
	private Context mContext;

	private List<RegisterOrderT> registerOrderTs;

	public UserOrderListAdapter(Context context, List<RegisterOrderT> registerOrderTs)
	{
		this.mContext = context;
		this.registerOrderTs = registerOrderTs;
	}

	@Override
	public int getCount()
	{
		if (registerOrderTs == null)
		{
			return 0;
		}
		return registerOrderTs.size();
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
			 convertView = LayoutInflater.from(mContext).inflate(R.layout.user_order_list_item, null);
			 TextView textView =  (TextView)convertView.findViewById( R.id.text1);
			 TextView textView2 = (TextView)convertView.findViewById( R.id.text2);
			 TextView textView3 = (TextView)convertView.findViewById( R.id.text3);
			 TextView textView4 = (TextView)convertView.findViewById( R.id.text4);
			 TextView state = (TextView)convertView.findViewById( R.id.state);
			 ImageView localImageView = (ImageView)convertView.findViewById(R.id.icon);
			 RegisterOrderT registerOrderT=registerOrderTs.get(position);
			 
			// localImageView.setVisibility(0);
			 String registerTime= registerOrderT.getRegisterTime();
			 String dateTime=registerTime.substring(0,12);
			 String weekTime=registerTime.substring(12,registerTime.length());
			 textView2.setText(weekTime);
			 textView.setText(dateTime);
			 textView3.setText(registerOrderT.getTeamName());
			 textView4.setText(registerOrderT.getOrderNum());
			 String stateT=registerOrderT.getPayState();
			 if("101".equals(stateT))
			 {
				 stateT="已支付";
			 }else
			 if("102".equals(stateT))
			 {
				 stateT="已取消";
			 }else
			 if("100".equals(stateT))
			 {
				 stateT="未支付";
			 }
			 state.setText(stateT);
		return convertView;
	}

}
