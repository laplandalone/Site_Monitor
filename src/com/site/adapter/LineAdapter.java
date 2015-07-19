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
import com.site.model.Line;

public class LineAdapter extends BaseAdapter
{
	private Context mContext;

	private List<Line> lines;

	private BitmapUtils bitmapUtils;
	
	public LineAdapter(Context context, List<Line> lines)
	{
		this.mContext = context;
		this.lines = lines;
		bitmapUtils = new BitmapUtils(mContext);
		bitmapUtils.closeCache();
	}
	


	public List<Line> getLines() {
		return lines;
	}

	public void setLines(List<Line> lines) {
		this.lines = lines;
	}




	@Override
	public int getCount()
	{
		if (lines == null)
		{
			return 0;
		}
		return lines.size();
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
		
			 convertView = LayoutInflater.from(mContext).inflate(R.layout.line_list_item, null);
			 TextView textView =  (TextView)convertView.findViewById( R.id.lineName);
			 TextView textView2 = (TextView)convertView.findViewById( R.id.endStopName);
			 Line line=lines.get(position);
			// localImageView.setVisibility(0);
			 textView.setText(line.getLineName());
			 textView2.setText("开往:"+line.getNextStop());
		
		return convertView;
	}

}
