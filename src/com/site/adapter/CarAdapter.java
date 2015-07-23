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
import com.site.model.Car;
import com.site.tools.StringUtil;

public class CarAdapter extends BaseAdapter {
	private Context mContext;

	private List<Car> cars;

	private BitmapUtils bitmapUtils;

	public CarAdapter(Context context, List<Car> cars) {
		this.mContext = context;
		this.cars = cars;
		bitmapUtils = new BitmapUtils(mContext);
		bitmapUtils.closeCache();
	}

	public List<Car> getCars() {
		return cars;
	}

	public void setCars(List<Car> cars) {
		this.cars = cars;
	}

	@Override
	public int getCount() {
		if (cars == null) {
			return 0;
		}
		return cars.size();
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
				R.layout.common_article_list_item, null);
		TextView textView = (TextView) convertView.findViewById(R.id.title);

		Car car = cars.get(position);
		textView.setText(car.getCarNo());
		int stop = 0;
		String stopFlag = car.getDeltStops();

		if (StringUtil.checkStringIsNum(stopFlag)) {
			stop = Integer.parseInt(stopFlag);
		}
		if (stop == 0)// 到站
		{
			convertView.setBackgroundResource(R.drawable.red_bg);
		} else if (stop >= 1 && stop <= 3)// 到站
		{
			convertView.setBackgroundResource(R.drawable.bg);
		} else if (stop < 0)// 到站
		{
			convertView.setBackgroundResource(R.drawable.arrived);
		}

		return convertView;
	}

}
