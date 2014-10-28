package com.dm.yx.view.expert;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.dm.yx.BaseActivity;
import com.dm.yx.MainPageActivity;
import com.dm.yx.R;
import com.dm.yx.adapter.ImgViewPager;
import com.dm.yx.model.Team;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 科室介绍
 *
 */
public class OnLineFacultyDescActivity extends BaseActivity
{

	@ViewInject(R.id.title)
	private TextView title;
	
	@ViewInject(R.id.all_doctor)
	private Button expertListBtn;
	
	@ViewInject(R.id.all_doctor)
	private Button askLineBtn;

	@ViewInject(R.id.content)
	private TextView content;
	
	@ViewInject(R.id.imgViewPager)
	ImgViewPager myPager; // 图片容器
	
	private Team team;
	
	private List<View> listViews; // 图片组
	
	private BitmapUtils bitmapUtils;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.faculty_desc);
		ViewUtils.inject(this);
		this.team=(Team) getIntent().getSerializableExtra("team");
		this.content.setText(team.getIntroduce());
		addActivity(this);
		initView();
		initValue();
		
	}
	
	@OnClick(R.id.all_doctor)
	public void getExpertList(View v)
	{
		Intent intent = new Intent(OnLineFacultyDescActivity.this,OnLineExpertListActivity.class);
		intent.putExtra("teamId",this.team.getTeamId());
		startActivity(intent);
	}
	
	@OnClick(R.id.online_doctor)
	public void askLineExpert(View v)
	{
		Intent intent = new Intent(OnLineFacultyDescActivity.this,AskExpertListActivity.class);
		intent.putExtra("teamId",this.team.getTeamId());
		startActivity(intent);
	}
	
	@OnClick(R.id.back)
	public void toHome(View v)
	{
		Intent intent = new Intent(OnLineFacultyDescActivity.this,MainPageActivity.class);
		startActivity(intent);
		exit();
	}
	
	/**
	 * 初始化图片
	 */
	private void InitViewPager()
	{
		bitmapUtils = new BitmapUtils(this);
		bitmapUtils.closeCache();
		
		listViews = new ArrayList<View>();
		String imgUrls=team.getImgUrl();
		String[] imgUrlsT=imgUrls.split(",");
		
//		int[] imageResId = new int[]
//		{ R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e };
		if(imgUrlsT!=null && imgUrlsT.length>0)
		{
			for (int i = 0; i < imgUrlsT.length; i++)
			{
				ImageView imageView = new ImageView(this);
				bitmapUtils.display(imageView, imgUrlsT[i]);
				imageView.setScaleType(ScaleType.CENTER_CROP);
				listViews.add(imageView);
			}
		}
		
	}
	@Override
	protected void initView()
	{
		title.setText(this.team.getTeamName()+"介绍");
		// TODO Auto-generated method stub
		InitViewPager();// 初始化图片
		myPager.start(this, listViews, 4000, null, R.layout.ad_bottom_item, R.id.ad_item_v,
				R.drawable.dot_focused, R.drawable.dot_normal);
	}
	@Override
	protected void initValue()
	{
		// TODO Auto-generated method stub

	}

}
