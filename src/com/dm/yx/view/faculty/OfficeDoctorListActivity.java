package com.dm.yx.view.faculty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.dm.yx.BaseActivity;
import com.dm.yx.MainPageActivity;
import com.dm.yx.R;
import com.dm.yx.adapter.CommonListAdapter;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 科室医生
 * 
 */
public class OfficeDoctorListActivity extends BaseActivity implements OnItemClickListener
{
	@ViewInject(R.id.title)
	private TextView title;

	private ListView list;

	private List<Map<String, Object>> unhandList = new ArrayList<Map<String, Object>>();

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_list);
		this.list=(ListView) findViewById(R.id.comlist);
		ViewUtils.inject(this);
		addActivity(this);
		initView();
		initValue();
	}

	@OnClick(R.id.back)
	public void toHome(View v)
	{
		Intent intent = new Intent(OfficeDoctorListActivity.this,MainPageActivity.class);
		startActivity(intent);
		exit();
	}
	@Override
	protected void initView()
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void initValue()
	{
		// TODO Auto-generated method stub
		title.setText(R.string.btn_hospital_facult);
		getListRst();
	}

	public void getListRst()
	{
		
		HashMap localHashMap = new HashMap();
		localHashMap.put("text", "zhangjia");
		
		 unhandList.add(localHashMap);
		 CommonListAdapter adapter = new CommonListAdapter(OfficeDoctorListActivity.this, unhandList);
		 this.list.setAdapter(adapter);
		this.list.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		// TODO Auto-generated method stub
		Intent intent = new Intent(OfficeDoctorListActivity.this,DoctorListActivity.class);
		startActivity(intent);
	}
}
