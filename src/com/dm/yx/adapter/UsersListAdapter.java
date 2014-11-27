package com.dm.yx.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dm.yx.R;
import com.dm.yx.application.RegApplication;
import com.dm.yx.model.User;
import com.dm.yx.tools.HealthUtil;
import com.dm.yx.view.user.LoginActivity;

public class UsersListAdapter extends BaseAdapter
{
	private Context mContext;

	private List<User> users =new ArrayList<User>();

    private boolean delete = false;
    private String choosePhone="";
    private boolean display=false;
	public boolean isDelete() {
		return delete;
	}

	public void setDelete(boolean delete) {
		this.delete = delete;
	}


	public String getChoosePhone() {
		return choosePhone;
	}

	public void setChoosePhone(String choosePhone) {
		this.choosePhone = choosePhone;
	}

	public UsersListAdapter(Context context,String phone,boolean display)
	{
		this.mContext = context;
		this.choosePhone=phone;
		List<User> usersT=HealthUtil.readChooseUsers();
		if(usersT!=null)
		{
			for(User u:usersT)
			{
				if(u.getTelephone().equals(phone) && !display)
				{
					continue;
				}
				users.add(u);
			}
		}
	}

	@Override
	public int getCount()
	{
		if (users == null)
		{
			return 0;
		}
		return users.size();
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
			 convertView = LayoutInflater.from(mContext).inflate(R.layout.users_list_item, null);
			 TextView textView =  (TextView)convertView.findViewById( R.id.comtext1);
			 ImageView choose = (ImageView) convertView.findViewById( R.id.choose);
			 ImageView deleteImg = (ImageView) convertView.findViewById( R.id.deleteImg);
			 User user=users.get(position);
			 String phone= user.getTelephone();
			 String name=user.getUserName();
			 if(name==null || "".equals(name))
			 {
				 textView.setText(phone);
			 }else
			 {
				 textView.setText(name);
			 }
		
			 
			 if(choosePhone.equals(phone))
			 {
				 choose.setVisibility(View.VISIBLE);
			 }
			 if(delete)
			 {
				 deleteImg.setBackgroundResource(R.drawable.delete);
				 deleteImg.setTag(user);
				 deleteImg.setOnClickListener(new OnClickListener()
				 {
					@Override
					public void onClick(View v) 
					{
						User deleteUser =(User) v.getTag();
						HealthUtil.deleteChooseUsers(deleteUser);
						users.remove(deleteUser);
						UsersListAdapter.this.notifyDataSetChanged();
						if(choosePhone.equals(deleteUser.getTelephone()))
						{
							HealthUtil.writeUserPhone("");
							HealthUtil.writeUserPassword("");
							HealthUtil.writeUserInfo("");
							RegApplication.getInstance().exit();
							Intent intent = new Intent(mContext,LoginActivity.class);
							mContext.startActivity(intent);
							
						}
					}
				});
			 }else
			 {
				 deleteImg.setBackgroundResource(R.drawable.faculty_normal);
			 }
			
		return convertView;
	}


}
