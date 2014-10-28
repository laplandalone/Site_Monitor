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

public class ContactListAdapter extends BaseAdapter
{
	private Context mContext;

	private List<UserContactT> userContactTs;

	public ContactListAdapter(Context context, List<UserContactT> contactTs)
	{
		this.mContext = context;
		this.userContactTs = contactTs;
	}

	@Override
	public int getCount()
	{
		if (userContactTs == null)
		{
			return 0;
		}
		return userContactTs.size();
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
			 UserContactT userContactT=userContactTs.get(position);
			 
			 String name= userContactT.getContactName();
			
			 textView.setText(name);
		
		return convertView;
	}

}
