package com.site.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class CommonListAdapter extends BaseAdapter
{
	private Context mContext;

	private List<Map<String, Object>> unhandList = new ArrayList<Map<String, Object>>();

	public CommonListAdapter(Context context, List<Map<String, Object>> unhandList)
	{
		this.mContext = context;
		this.unhandList = unhandList;
	}

	@Override
	public int getCount()
	{
		if (unhandList == null)
		{
			return 0;
		}
		return unhandList.size();
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
		
//			convertView = LayoutInflater.from(mContext).inflate(R.layout.common_list_item, null);
//			TextView textView = (TextView) convertView.findViewById(R.id.comtext1);
//			String value = unhandList.get(position).get("text").toString();
//			textView.setText(value);
		
		return convertView;
	}

}