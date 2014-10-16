package com.dm.yx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dm.yx.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class NewsTypeAdapter  extends BaseAdapter
{
	private Context mContext;

	private JsonArray jsonArray;

	private int[] imgArray  = new int[]
			{
			R.drawable.bg_article_title_logo1, 
			R.drawable.bg_article_title_logo2, 
			R.drawable.bg_article_title_logo3
			};
	private int flag=0;
	public NewsTypeAdapter(Context context,JsonArray jsonArray)
	{
		this.mContext = context;
		this.jsonArray = jsonArray;
	}

	@Override
	public int getCount()
	{
		if (jsonArray == null)
		{
			return 0;
		}
		return jsonArray.size();
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
		if(flag>2)
		{
			flag=0;
		}
		
			 convertView = LayoutInflater.from(mContext).inflate(R.layout.common_list_two_item, null);
			 TextView textView =  (TextView)convertView.findViewById( R.id.text1);
			 ImageView imageView = (ImageView)convertView.findViewById( R.id.msg_icon);
			 JsonElement element= jsonArray.get(position);
			 JsonObject jsonObject = element.getAsJsonObject();
			 
			 imageView.setImageResource(imgArray[flag]);
			 imageView.setVisibility(View.VISIBLE);
			 textView.setText(jsonObject.get("configVal").getAsString());
			 flag++;
		
		return convertView;
	}

}
