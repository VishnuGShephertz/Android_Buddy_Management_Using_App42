package com.shephertz.app42.buddy.app;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.shephertz.app42.buddy.adapter.BuddyAdapter;
import com.shephertz.app42.buddy.listener.BuddyEventListener;
import com.shephertz.app42.buddy.service.App42ServiceApi;
import com.shephertz.app42.buddy.util.Constants;
import com.shephertz.app42.paas.sdk.android.App42Exception;

public class MyBuddyList extends Activity implements BuddyEventListener{
	ListView list;
	ProgressDialog progressDialog;

	private ArrayList<String> myBuddies;
	private App42ServiceApi app42Service;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_list);
		progressDialog = new ProgressDialog(this);
		list = (ListView) findViewById(R.id.list);
		((Button)findViewById(R.id.create)).setVisibility(View.GONE);
		app42Service = App42ServiceApi.instance(this);
		showLoadingPopup();
		app42Service.loadMyBuddyList(AppContext.myUserName, this);
		buildProfileView();
	}
	private void buildProfileView(){
		ImageView myPic = (ImageView) findViewById(R.id.user_pic);
		TextView myName= (TextView) findViewById(R.id.username);
		ApplicationState appState=ApplicationState.instance();
		if(appState.getAvatarName()==null){
			myName.setText(AppContext.myUserName);
		}
		else{
			myName.setText(appState.getAvatarName());
		}
		if(appState.getAvatarPic()!=null){
		myPic.setImageBitmap(appState.getAvatarPic());
		}
		else{
			myPic.setImageResource(R.drawable.default_pic);
		}
	}
	private void showLoadingPopup() {
		progressDialog.setMessage("Loading data....");
		progressDialog.show();
	}
	public void onGetMessageClicked(int pos) {

		Intent intent = new Intent(this, MessageActivity.class);
		intent.putExtra(Constants.MsgType, Constants.BuddyMsg);
		intent.putExtra(Constants.Buddy, myBuddies.get(pos));
		startActivity(intent);
	}

	@Override
	public void onFriendRequestSent(boolean status) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBuddyBlocked(boolean status) {
		// TODO Auto-generated method stub
		
	}

	

	@Override
	public void onGetBuddyList(ArrayList<String> myBuddies) {
		// TODO Auto-generated method stub
		progressDialog.dismiss();
		for (int i = 0; i < myBuddies.size(); i++) {
			System.out.println(myBuddies.get(i));
		}
		this.myBuddies = myBuddies;
		list.setAdapter(new BuddyAdapter(this, this.myBuddies));
		
	}

	@Override
	public void onGetInvitationList(ArrayList<String> invitations) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(App42Exception ex) {
		// TODO Auto-generated method stub
		progressDialog.dismiss();
		
	}

	@Override
	public void onGetAllUsers(ArrayList<String> users) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onBlockBuddyRequest(boolean status, String buddyName) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onRejectRequest(boolean status, String buddyName) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onAcceptRequest(boolean status, String buddyName) {
		// TODO Auto-generated method stub
		
	}
}