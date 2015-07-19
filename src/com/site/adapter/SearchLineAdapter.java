package com.site.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.site.R;
import com.site.model.SearchLine;

public class SearchLineAdapter extends BaseAdapter
{
	private Context mContext;

	private List<SearchLine> searchLines;

	private BitmapUtils bitmapUtils;
	
	public SearchLineAdapter(Context context, List<SearchLine> searchLines)
	{
		this.mContext = context;
		this.searchLines = searchLines;
		bitmapUtils = new BitmapUtils(mContext);
		bitmapUtils.closeCache();
	}
	


	 



	public List<SearchLine> getSearchLines() {
		return searchLines;
	}







	public void setSearchLines(List<SearchLine> searchLines) {
		this.searchLines = searchLines;
	}







	@Override
	public int getCount()
	{
		if (searchLines == null)
		{
			return 0;
		}
		return searchLines.size();
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
			 SearchLine line=searchLines.get(position);
			// localImageView.setVisibility(0);
			 textView.setText(line.getStopName());
		
		return convertView;
	}

}
