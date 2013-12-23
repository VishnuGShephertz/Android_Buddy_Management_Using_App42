package com.shephertz.app42.buddy.app;


import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.shephertz.app42.buddy.adapter.UserAdapter;
import com.shephertz.app42.buddy.listener.BuddyEventListener;
import com.shephertz.app42.buddy.service.App42ServiceApi;
import com.shephertz.app42.paas.sdk.android.App42Exception;

public class UserList extends Activity implements BuddyEventListener{
	ListView list;
	
	ProgressDialog progressDialog;
	private ArrayList<String> users;

	private App42ServiceApi app42Service;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_list);
		progressDialog = new ProgressDialog(this);
		list = (ListView) findViewById(R.id.list);
		((Button)findViewById(R.id.create)).setVisibility(View.GONE);
		app42Service = App42ServiceApi.instance(this);
		showLoadingPopup("Loading data....");
		app42Service.loaduserList(this);
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

	
public void blockUser(String userName){
	 app42Service.blockBuddy(AppContext.myUserName, userName, UserList.this);
}
	/**
     * Create and return an example alert dialog with an edit text box.
     */
    public void addAsAFriend(final String userName) {
    	
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Send Request to "+userName);
      
       final EditText input = new EditText(this);
       input.setHint("Message");
        builder.setView(input);
        builder.setPositiveButton("Friend ", new DialogInterface.OnClickListener() {
 
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
            String message=input.getText().toString();
            showLoadingPopup("Sending request....");
             app42Service.sendFriendRequest(AppContext.myUserName, userName, message, UserList.this);
            }
        });
 
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
 
            @Override
            public void onClick(DialogInterface dialog, int which) {
         
                
            }
        });
        builder.show();
    }


	@Override
	public void onGetAllUsers(ArrayList<String> users) {
		// TODO Auto-generated method stub
		progressDialog.dismiss();
		users.remove(AppContext.myUserName);
		for(int i=0;i<users.size();i++){
			System.out.println(users.get(i));
		}
		this.users=users;
		
		list.setAdapter(new UserAdapter(this, this.users));
		
	}

	@Override
	public void onError(App42Exception ex) {
		// TODO Auto-generated method stub
		progressDialog.dismiss();
	}

	@Override
	public void onFriendRequestSent(boolean status) {
		// TODO Auto-generated method stub
		progressDialog.dismiss();
		if(status){
			Toast.makeText(this, R.string.friend_request, Toast.LENGTH_SHORT).show();
		}
		else{
			Toast.makeText(this,R.string.request_failed, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onBuddyBlocked(boolean status) {
		// TODO Auto-generated method stub
		progressDialog.dismiss();
		if(status){
			Toast.makeText(this, R.string.blocked_user, Toast.LENGTH_SHORT).show();
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