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
import com.dm.yx.model.HospitalNewsT;
import com.lidroid.xutils.BitmapUtils;

public class NewsListAdapter extends BaseAdapter
{
	private Context mContext;

	private List<HospitalNewsT> hospitalNewsTs;

	private BitmapUtils bitmapUtils;
	
	public NewsListAdapter(Context context, List<HospitalNewsT> hospitalNewsTs)
	{
		this.mContext = context;
		this.hospitalNewsTs = hospitalNewsTs;
		bitmapUtils = new BitmapUtils(mContext);
		bitmapUtils.closeCache();
	}
	

	public List<HospitalNewsT> getHospitalNewsTs() {
		return hospitalNewsTs;
	}


	public void setHospitalNewsTs(List<HospitalNewsT> hospitalNewsTs) {
		this.hospitalNewsTs = hospitalNewsTs;
	}


	@Override
	public int getCount()
	{
		if (hospitalNewsTs == null)
		{
			return 0;
		}
		return hospitalNewsTs.size();
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
			 HospitalNewsT hospitalNewsT=hospitalNewsTs.get(position);
			 String newsImgs=hospitalNewsT.getNewsImages();
			 if(newsImgs.endsWith("jpg") || newsImgs.endsWith("png"))
			 {
				 bitmapUtils.display(imageView,newsImgs);
			 }
			// localImageView.setVisibility(0);
			 textView.setText(hospitalNewsT.getNewsTitle());
			 textView2.setText(hospitalNewsT.getNewsContent());
			 textView3.setText(hospitalNewsT.getCreateDate());
			 
		
		return convertView;
	}

}
