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
import com.site.model.Cancel;
import com.site.model.Line;

public class CancelAdapter extends BaseAdapter {
	private Context mContext;

	private List<Cancel> cancels;

	private BitmapUtils bitmapUtils;

	public CancelAdapter(Context context, List<Cancel> cancels) {
		this.mContext = context;
		this.cancels = cancels;
		bitmapUtils = new BitmapUtils(mContext);
		bitmapUtils.closeCache();
	}

	public List<Cancel> getCancels() {
		return cancels;
	}

	public void setCancels(List<Cancel> cancels) {
		this.cancels = cancels;
	}

	@Override
	public int getCount() {
		if (cancels == null) {
			return 0;
		}
		return cancels.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		convertView = LayoutInflater.from(mContext).inflate(
				R.layout.cancel_item, null);
		TextView textView = (TextView) convertView.findViewById(R.id.text1);
		TextView textView2 = (TextView) convertView.findViewById(R.id.text2);
		TextView textView3 = (TextView) convertView.findViewById(R.id.text3);
		TextView textView4 = (TextView) convertView.findViewById(R.id.text4);
		Cancel cancel = cancels.get(position);
		// localImageView.setVisibility(0);
		textView.setText(cancel.getLineName());
		textView2.setText(cancel.getDate());
		textView3.setText(cancel.getCarNo());
		textView4.setText(cancel.getStopName());

		return convertView;
	}

}
