package com.dm.yx.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dm.yx.R;
import com.dm.yx.model.UserQuestionT;
import com.dm.yx.tools.HealthConstant;
import com.lidroid.xutils.BitmapUtils;

public class TalkAdapter extends BaseAdapter
{

	private Context mContext;

	private List<UserQuestionT> questionTs;
	private BitmapUtils bitmapUtils;
	
	public TalkAdapter(Context context, List<UserQuestionT> questionTs)
	{
		this.mContext = context;
		this.questionTs = questionTs;
		bitmapUtils = new BitmapUtils(mContext);
		bitmapUtils.closeCache();
	}

	@Override
	public int getCount()
	{
		if (questionTs == null)
		{
			return 0;
		}
		return questionTs.size();
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
		UserQuestionT questionT = questionTs.get(position);
		String recordType=questionT.getRecordType();
		if("ask".equals(recordType) || "copy".equals(recordType))
		{
			convertView = LayoutInflater.from(mContext).inflate(R.layout.ask_text_item, null);
			TextView textView = (TextView) convertView.findViewById(R.id.content);
			LinearLayout imgLine = (LinearLayout) convertView.findViewById(R.id.imgline);
			textView.setText(questionT.getContent());
			String img = questionT.getImgUrl();
			if(img!=null)
			{
				
			String[] imgs=img.split(",");
			if(imgs.length>0 && imgs[0]!=null && !"".equals(imgs[0]))
			{
				 imgLine.setVisibility(View.VISIBLE);
				 ImageView img1=(ImageView) convertView.findViewById(R.id.img1);
				 bitmapUtils.display(img1,HealthConstant.question_img_Url+imgs[0]);
				 Uri mUri = Uri.parse(HealthConstant.question_img_Url+imgs[0]);
				 img1.setTag(mUri);
				 img1.setOnClickListener(new OnClickListener()
				 {
					@Override
					public void onClick(View v) 
					{
						Intent it = new Intent(Intent.ACTION_VIEW);
						it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				        Uri mUri = (Uri) v.getTag();
				        it.setDataAndType(mUri, "image/*");
						mContext.startActivity(it);
					}
				});
			}
			if(imgs.length>1 && imgs[1]!=null && !"".equals(imgs[1]))
			{
				 ImageView img2=(ImageView) convertView.findViewById(R.id.img2);
				 bitmapUtils.display(img2,HealthConstant.question_img_Url+imgs[1]);
				 Uri mUri = Uri.parse(HealthConstant.question_img_Url+imgs[1]);
				 img2.setTag(mUri);
				 img2.setOnClickListener(new OnClickListener()
				 {
					@Override
					public void onClick(View v) 
					{
						Intent it = new Intent(Intent.ACTION_VIEW);
						it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				        Uri mUri = (Uri) v.getTag();
				        it.setDataAndType(mUri, "image/*");
						mContext.startActivity(it);
					}
				});
			}
			
			if(imgs.length>2 && imgs[2]!=null && !"".equals(imgs[2]))
			{
				 ImageView img3=(ImageView) convertView.findViewById(R.id.img3);
				 bitmapUtils.display(img3,HealthConstant.question_img_Url+imgs[2]);
				 Uri mUri = Uri.parse(HealthConstant.question_img_Url+imgs[2]);
				 img3.setTag(mUri);
				 img3.setOnClickListener(new OnClickListener()
				 {
					@Override
					public void onClick(View v) 
					{
						Intent it = new Intent(Intent.ACTION_VIEW);
						it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				        Uri mUri = (Uri) v.getTag();
				        it.setDataAndType(mUri, "image/*");
						mContext.startActivity(it);
					}
				});
			}
			}
			
		}else if("ans".equals(recordType) )
		{
			convertView = LayoutInflater.from(mContext).inflate(R.layout.answer_text_item, null);
			TextView textView = (TextView) convertView.findViewById(R.id.anscontent);
			textView.setText(questionT.getContent());
		}
			
		return convertView;
	}

	

}
