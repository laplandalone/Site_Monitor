package com.site.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.site.R;
import com.site.model.NearBy;

public class NearBysListAdapter extends BaseAdapter
{
	private Context mContext;

	private List<NearBy> nearBys;

	private BitmapUtils bitmapUtils;
	
	public NearBysListAdapter(Context context, List<NearBy> nearBys)
	{
		this.mContext = context;
		this.nearBys = nearBys;
		bitmapUtils = new BitmapUtils(mContext);
		bitmapUtils.closeCache();
	}
	


	public List<NearBy> getNearyBys() {
		return nearBys;
	}



	public void setNearyBys(List<NearBy> nearyBys) {
		this.nearBys = nearyBys;
	}



	@Override
	public int getCount()
	{
		if (nearBys == null)
		{
			return 0;
		}
		return nearBys.size();
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
		
			 convertView = LayoutInflater.from(mContext).inflate(R.layout.common_article_list_item, null);
			 TextView textView =  (TextView)convertView.findViewById( R.id.newsTitle);
			 TextView textView2 = (TextView)convertView.findViewById( R.id.newsContent);
			 TextView textView3 = (TextView)convertView.findViewById( R.id.newsDate);
			 NearBy nearyBy=nearBys.get(position);
			// localImageView.setVisibility(0);
			 textView.setText(nearyBy.getStopName());
			 
		
		return convertView;
	}

}
