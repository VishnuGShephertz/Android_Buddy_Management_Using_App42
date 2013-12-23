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

import com.shephertz.app42.buddy.app.MyBuddyList;
import com.shephertz.app42.buddy.app.R;

public class BuddyAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<String> items;
	private static LayoutInflater inflater = null;

	public BuddyAdapter(Context context, ArrayList<String> items) {
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
		protected TextView text1;
		protected Button btnMsg;

	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		ViewHolder holder;
		if (convertView == null) {
			vi = inflater.inflate(R.layout.mybuddy_item, null);
			holder = new ViewHolder();
			holder.text1 = (TextView) vi.findViewById(R.id.buddy_name);
			holder.btnMsg = (Button) vi.findViewById(R.id.btn_message);
			vi.setTag(holder);

		} else

			holder = (ViewHolder) vi.getTag();
		holder.text1.setText("  " + items.get(position));

		holder.btnMsg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				((MyBuddyList) mContext).onGetMessageClicked(position);
			}
		});

		return vi;
	}
}