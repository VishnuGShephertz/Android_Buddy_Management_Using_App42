package com.shephertz.app42.buddy.app;


import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.shephertz.app42.buddy.adapter.InvitationAdapter;
import com.shephertz.app42.buddy.listener.BuddyEventListener;
import com.shephertz.app42.buddy.service.App42ServiceApi;
import com.shephertz.app42.paas.sdk.android.App42Exception;

public class InvitationList extends Activity implements BuddyEventListener {
	ListView list;
	ProgressDialog progressDialog;
	private App42ServiceApi app42Service;
	private ArrayList<String> requestList;
	private InvitationAdapter adapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_list);
		progressDialog = new ProgressDialog(this);
		list = (ListView) findViewById(R.id.list);
		((Button)findViewById(R.id.create)).setVisibility(View.GONE);
		app42Service = App42ServiceApi.instance(this);
		showLoadingPopup("Loading data.....");
		app42Service.loadInvitationList(AppContext.myUserName, this);
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
	private void showLoadingPopup(String message) {
		progressDialog.setMessage(message);
		progressDialog.show();
	}
	
	public void acceptRequest(String buddyName){
		 showLoadingPopup("Please wait....");
         app42Service.acceptFriendRequest(AppContext.myUserName, buddyName, InvitationList.this);
	}
	public void rejectRequest(String buddyName){
		 showLoadingPopup("Please wait....");
		 app42Service.rejectFriendRequest(AppContext.myUserName, buddyName, InvitationList.this);
	}
	
	public void blockRequest(String buddyName){
		 showLoadingPopup("Please wait....");
		  app42Service.blockFriendRequest(AppContext.myUserName, buddyName, InvitationList.this);
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
	public void onBlockBuddyRequest(boolean status,String buddyName) {
		// TODO Auto-generated method stub
		progressDialog.dismiss();
		if(status){
			Toast.makeText(this, R.string.blocked_buddy, Toast.LENGTH_SHORT).show();
			removeFromList(buddyName);
		}
		else{
			Toast.makeText(this, R.string.request_failed, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onRejectRequest(boolean status,String buddyName) {
		// TODO Auto-generated method stub
		progressDialog.dismiss();
		if(status){
			Toast.makeText(this, R.string.rejected_request, Toast.LENGTH_SHORT).show();
			removeFromList(buddyName);
		}
		else{
			Toast.makeText(this, R.string.request_failed, Toast.LENGTH_SHORT).show();
		}
		
	}
	
	private void removeFromList(String buddyName){
		requestList.remove(buddyName);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onAcceptRequest(boolean status,String buddyName) {
		// TODO Auto-generated method stub
		progressDialog.dismiss();
		if(status){
			Toast.makeText(this, R.string.accepted_request_success, Toast.LENGTH_SHORT).show();
			removeFromList(buddyName);
		}
		else{
			Toast.makeText(this, R.string.request_failed, Toast.LENGTH_SHORT).show();
		}
		
	}

	@Override
	public void onGetBuddyList(ArrayList<String> myBuddies) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGetInvitationList(ArrayList<String> invitations) {
		  progressDialog.dismiss();
	     this.requestList=invitations;
	     adapter=new InvitationAdapter(this, this.requestList);
	     list.setAdapter(adapter);
		
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

}