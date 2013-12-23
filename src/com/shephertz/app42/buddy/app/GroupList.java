package com.shephertz.app42.buddy.app;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.shephertz.app42.buddy.adapter.GroupAdapter;
import com.shephertz.app42.buddy.listener.BuddyGroupEventListener;
import com.shephertz.app42.buddy.service.App42ServiceApi;
import com.shephertz.app42.buddy.util.Constants;
import com.shephertz.app42.paas.sdk.android.App42Exception;
import com.shephertz.app42.paas.sdk.android.buddy.Buddy;

public class GroupList extends Activity implements BuddyGroupEventListener {
	ListView list;
	ProgressDialog progressDialog;
	private ArrayList<Buddy> myGroups;;
	private App42ServiceApi app42Service;
	private GroupAdapter adapter;
 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_list);
		progressDialog = new ProgressDialog(this);
		list = (ListView) findViewById(R.id.list);
		showLoadingPopup("fetaching groups...");
		buildProfileView();
		app42Service = App42ServiceApi.instance(this);
		app42Service.loadGroupList(AppContext.myUserName, this);
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
	
	public void createNewGroup(View view){
		sendGroupPopup();
	}
	
	public void onError(Exception ex) {
		progressDialog.dismiss();
	}

	/**
     * Create and return an example alert dialog with an edit text box.
     */
    private void sendGroupPopup() {
    	
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Create Group");
      
       final EditText input = new EditText(this);
       input.setHint("Group name");
        builder.setView(input);
        builder.setPositiveButton("create", new DialogInterface.OnClickListener() {
 
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
            String groupname=input.getText().toString();
            showLoadingPopup("Creating group....");
             app42Service.createGroup(AppContext.myUserName, groupname, GroupList.this);
            }
        });
 
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
         
                
            }
        });
        builder.show();
    }

    
    public void onEditGroupClicked(int pos){
    	Intent intent=new Intent(this,GroupActivity.class);
		Buddy buddy=myGroups.get(pos);
		intent.putExtra(Constants.Owner,buddy.getUserName());
		intent.putExtra(Constants.GroupName,buddy.getGroupName());
		startActivity(intent);
    }

    public void onGetMessageClicked(int pos){
    	Buddy buddy=myGroups.get(pos);
    	Intent intent=new Intent(this,MessageActivity.class);
		intent.putExtra(Constants.MsgType, Constants.GroupMsg);
		intent.putExtra(Constants.GroupName,buddy.getGroupName());
		intent.putExtra(Constants.Owner, buddy.getUserName());
		startActivity(intent);
    }


	@Override
	public void onBuddyGroupList(ArrayList<Buddy> buddydata) {
		progressDialog.dismiss();
		this.myGroups=buddydata;
		adapter=new GroupAdapter(this, myGroups);
		list.setAdapter(adapter);
		
	}

	@Override
	public void onError(App42Exception ex) {
		// TODO Auto-generated method stub
		progressDialog.dismiss();
	}

	@Override
	public void onGetGroupFriends(ArrayList<String> groupFrnds,
			ArrayList<String> friends) {
		
	}

	@Override
	public void onFriendAddedInGroup() {
		// TODO Auto-generated method stub
		Toast.makeText(this, R.string.buddy_added, Toast.LENGTH_SHORT).show();
		
	}
	@Override
	public void onGroupCreated(boolean status, Buddy buddy) {
		// TODO Auto-generated method stub
		progressDialog.dismiss();
		
		if(status){
			Toast.makeText(this, R.string.group_created, Toast.LENGTH_SHORT).show();
			myGroups.add(buddy);
			adapter.notifyDataSetChanged();
		}
		else{
			Toast.makeText(this, R.string.group_creation_failed, Toast.LENGTH_SHORT).show();
		}
	}

}