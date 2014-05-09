package com.shephertz.app42.buddy.adapter;



import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.shephertz.app42.buddy.app.InvitationList;
import com.shephertz.app42.buddy.app.R;

public class InvitationAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<String> items;
	private static LayoutInflater inflater = null;

	public InvitationAdapter(Context context,ArrayList<String>items) {
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
		protected TextView buddy;
		protected Button accept;
		protected Button reject;
		protected Button block;

		// public ImageView image;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		ViewHolder holder;
		if (convertView == null) {
			vi = inflater.inflate(
					R.layout.invitation_item, null);
			holder = new ViewHolder();
			holder.buddy = (TextView) vi.findViewById(R.id.buddy_name);
			holder.accept = (Button) vi.findViewById(R.id.btn_accept);
			holder.reject = (Button) vi.findViewById(R.id.btn_reject);
			holder.block = (Button) vi.findViewById(R.id.btn_block);
			vi.setTag(holder);

		} else

			holder = (ViewHolder) vi.getTag();
		final String name=" "+items.get(position).trim();

		holder.buddy.setText(name);
		
		 holder.reject.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					((InvitationList)mContext).rejectRequest(name);
				}
			});
		 
		 holder.accept.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					((InvitationList)mContext).acceptRequest(name);
				}
			});
		 
		 holder.block.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					((InvitationList)mContext).blockRequest(name);
				}
			});

		return vi;
	}
}
