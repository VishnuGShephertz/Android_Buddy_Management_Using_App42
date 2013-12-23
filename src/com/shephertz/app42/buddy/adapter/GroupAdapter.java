package com.shephertz.app42.buddy.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.shephertz.app42.buddy.app.GroupList;
import com.shephertz.app42.buddy.app.R;
import com.shephertz.app42.paas.sdk.android.buddy.Buddy;

public class GroupAdapter extends BaseAdapter {

	private Activity activity;
	private ArrayList<Buddy> items;
	private static LayoutInflater inflater = null;

	public GroupAdapter(Activity a, ArrayList<Buddy> items) {
		activity = a;
		this.items = items;
		inflater = (LayoutInflater) activity
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
		protected TextView text1;
		protected Button btnEdit;
		protected Button btnMsg;
		

		// public ImageView image;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		ViewHolder holder;
		if (convertView == null) {
			vi = inflater.inflate(R.layout.grp_list_item, null);
			holder = new ViewHolder();
			holder.text1 = (TextView) vi.findViewById(R.id.group_name);
			holder.text1.setTextColor(Color.BLACK);
			holder.btnMsg=(Button)vi.findViewById(R.id.btn_message);
			holder.btnEdit=(Button)vi.findViewById(R.id.btn_edit);
			vi.setTag(holder);

		} else

		holder = (ViewHolder) vi.getTag();
		holder.text1.setText("  " + items.get(position).getGroupName());
        holder.btnMsg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				((GroupList)activity).onGetMessageClicked(position);
			}
		});
     
     holder.btnEdit.setOnClickListener(new OnClickListener() {
			
 			@Override
 			public void onClick(View arg0) {
 				// TODO Auto-generated method stub
 				((GroupList)activity).onEditGroupClicked(position);
 			}
 		});
		return vi;
	}
}