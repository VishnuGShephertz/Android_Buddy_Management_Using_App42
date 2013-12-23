package com.shephertz.app42.buddy.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.shephertz.app42.buddy.app.R;
import com.shephertz.app42.buddy.app.UserList;

public class UserAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<String> items;
	private static LayoutInflater inflater = null;

	public UserAdapter(Context context,ArrayList<String>items) {
		mContext = context;
		this.items = items;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return items.size();
	}

	public Object getItem(int position) {
		return items.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	private static class ViewHolder {
		protected TextView buddyName;
		protected Button add;
		protected Button block;

		// public ImageView image;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		ViewHolder holder;
		if (convertView == null) {
			vi = inflater.inflate(
					R.layout.user_item, null);
			holder = new ViewHolder();
			holder.buddyName = (TextView) vi.findViewById(R.id.buddy_name);
			holder.add = (Button) vi.findViewById(R.id.btn_add);
			holder.block = (Button) vi.findViewById(R.id.btn_block);
			vi.setTag(holder);

		} else

			holder = (ViewHolder) vi.getTag();
       final String name=items.get(position).trim();
		holder.buddyName.setText(" "+name);
		
		 holder.add.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					((UserList)(Activity)mContext).addAsAFriend(name);
				}
			});
		 
		 holder.block.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					((UserList)mContext).blockUser(name);
				}
			});

		return vi;
	}
}