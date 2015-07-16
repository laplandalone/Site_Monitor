package com.site.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dm.yx.R;
import com.lidroid.xutils.BitmapUtils;
import com.site.model.City;

public class CityAdapter extends BaseAdapter
{
	private Context mContext;

	private List<City> cities;

	private BitmapUtils bitmapUtils;
	
	public CityAdapter(Context context, List<City> cities)
	{
		this.mContext = context;
		this.cities = cities;
		bitmapUtils = new BitmapUtils(mContext);
		bitmapUtils.closeCache();
	}
	

	public List<City> getCities() {
		return cities;
	}


	public void setCities(List<City> cities) {
		this.cities = cities;
	}







	@Override
	public int getCount()
	{
		if (cities == null)
		{
			return 0;
		}
		return cities.size();
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
			 ImageView imageView = (ImageView) convertView.findViewById( R.id.news_photo);
			 City city=cities.get(position);
			// localImageView.setVisibility(0);
			 textView.setText(city.getCityName());
			 
		
		return convertView;
	}

}
