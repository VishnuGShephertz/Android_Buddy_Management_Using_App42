package com.shephertz.app42.buddy.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.shephertz.app42.buddy.adapter.MessageAdapter;
import com.shephertz.app42.buddy.listener.MessageEventListener;
import com.shephertz.app42.buddy.service.App42ServiceApi;
import com.shephertz.app42.buddy.util.Constants;
import com.shephertz.app42.buddy.util.Utils;
import com.shephertz.app42.paas.sdk.android.App42Exception;
import com.shephertz.app42.paas.sdk.android.buddy.Buddy;


public class MessageActivity extends Activity implements MessageEventListener {
	/** Called when the activity is first created. */

	private ArrayList<MyBuddy> messages;
	private MessageAdapter adapter;
	private App42ServiceApi app42Service;
	private String buddyName;
	private String grpName;
	private String ownerName;
	ProgressDialog progressDialog;
	private int msgFrom;
	private ListView list;
	private EditText text;
	private final int RESULT_LOAD_IMAGE = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_list);

		progressDialog = new ProgressDialog(this);
		text = (EditText) this.findViewById(R.id.text);
		
		parseIntentData();
		this.app42Service = App42ServiceApi.instance(this);
		list = (ListView) findViewById(R.id.list);
		messages=new ArrayList<MyBuddy>();
		adapter = new MessageAdapter(this, messages, AppContext.myUserName);
		list.setAdapter(adapter);
		((TextView) findViewById(R.id.page_header)).setText("Messages");
		if (msgFrom == Constants.AllMsg) {
			LinearLayout linl = (LinearLayout) findViewById(R.id.bottom_write_bar);
			linl.setVisibility(View.GONE);
		}
		loadMessages();
	}

	private void loadMessages() {
		if (msgFrom == Constants.AllMsg) {
			showProgressDialog();
			app42Service.getAllMessages(AppContext.myUserName, this);
		} else if (msgFrom == Constants.BuddyMsg) {
			showProgressDialog();
			app42Service.getAllMessagesFromBuddy(AppContext.myUserName,
					buddyName, this);
		} else if (msgFrom == Constants.GroupMsg) {
			showProgressDialog();
			app42Service.getAllMessagesFromGroup(AppContext.myUserName,
					ownerName, grpName, this);
		}
	}

	private void parseIntentData() {
		Intent intent = getIntent();
		msgFrom = intent.getIntExtra(Constants.MsgType, -1);
		if (msgFrom == Constants.AllMsg) {

		} else if (msgFrom == Constants.BuddyMsg) {
			buddyName = intent.getStringExtra(Constants.Buddy);

		} else if (msgFrom == Constants.GroupMsg) {
			grpName = intent.getStringExtra(Constants.GroupName);
			ownerName = intent.getStringExtra(Constants.Owner);

		}

	}

	private void showProgressDialog() {
		progressDialog.setMessage("Loading messages.......");
		progressDialog.show();
	}


	void onError() {
		progressDialog.dismiss();
	}

	public void onStop() {
		super.onStop();

	}

	public void onUploadImage(View view){
		if(Utils.haveNetworkConnection(this)){
		browsePhoto();
		}
		else{
			Toast.makeText(this, "Turn on Internet connection", Toast.LENGTH_SHORT).show();
		}
	}
	
	
	/*
	 * Call when user clicks on browse photo
	 */
	private void browsePhoto() {
		Intent i = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(i, RESULT_LOAD_IMAGE);
	
	}
	
	 /*
	  * Callback when user select image from gallery for upload
	  * and call previewImagePopup for preview
	  * User autherize with facebook on first time and he has to send autherize callback
	  * 
	  * (non-Javadoc)
	  * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	  */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
				&& null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();
			uploadImage(picturePath);
		} 

	}
	
	private void uploadImage(String imgPath){
		addNewMessage(imgPath);
		if(Utils.haveNetworkConnection(this)){
			ImageInfo imageInfo=null;
			if (msgFrom == Constants.BuddyMsg) {
				imageInfo=ImageInfo.getUserImage(buddyName, imgPath, AppContext.myUserName);
			} else if (msgFrom == Constants.GroupMsg) {
				imageInfo=ImageInfo.getGroupImage(grpName, imgPath, ownerName);
			}
			app42Service.sharePic(imageInfo, this);
		}   
	}
	public void sendMessage(View view) {
		String newMessage = text.getText().toString().trim();
		if (newMessage.length() > 0) {
			text.setText("");
			if (msgFrom == Constants.BuddyMsg) {
				app42Service.sendMessageToBuddy(AppContext.myUserName, buddyName, newMessage, this);
			} else if (msgFrom == Constants.GroupMsg) {
				app42Service.sendMessageToGroup(AppContext.myUserName,
						ownerName, grpName, newMessage, this);
			}
			addNewMessage(newMessage);
		}
	}

	void addNewMessage(String msg) {
		MyBuddy myBuddy=new MyBuddy(AppContext.myUserName, msg, new Date());
		messages.add(myBuddy);
		adapter.notifyDataSetChanged();
		list.setSelection(messages.size() - 1);
	}

	@Override
	public void onMessageSentToGroup() {
		// TODO Auto-generated method stub
		progressDialog.dismiss();
		Toast.makeText(this, R.string.message_sent, Toast.LENGTH_SHORT)
				.show();
	}

	@Override
	public void onError(App42Exception e) {
		// TODO Auto-generated method stub
		progressDialog.dismiss();
	}

	@Override
	public void onMesssageSentToBuddy() {

		progressDialog.dismiss();
		Toast.makeText(this, R.string.message_sent, Toast.LENGTH_SHORT)
				.show();
	}

	
	@Override
	public void onGetAllMessages(ArrayList<Buddy> allMessages) {
		// TODO Auto-generated method stub
		progressDialog.dismiss();
		this.messages=new ArrayList<MyBuddy>();
		for(Buddy buddy:allMessages){
			MyBuddy myBuddy=new MyBuddy(buddy.getOwnerName(), buddy.getMessage(), buddy.getSendedOn());
			messages.add(myBuddy);
		}
		
		Collections.sort(messages, new Comparator<MyBuddy>() {
			   public int compare(MyBuddy o1, MyBuddy o2) {
			       if (o1.getSendDate()== null || o2.getSendDate() == null)
			         return 0;
			       return o1.getSendDate().compareTo(o2.getSendDate());
			   }
			 });
		
		adapter = new MessageAdapter(this, messages, AppContext.myUserName);
		list.setAdapter(adapter);
		//adapter.notifyDataSetChanged();
		//list.setSelection(messages.size() - 1);
	}
	


	@Override
	public void onMessageSendingFailed(App42Exception e) {
		// TODO Auto-generated method stub
		progressDialog.dismiss();
		Toast.makeText(this, R.string.message_failed, Toast.LENGTH_SHORT).show();
		if(messages.size()>0){
		messages.remove(messages.size() - 1);
		adapter.notifyDataSetChanged();
		list.setSelection(messages.size() - 1);
		}
	}


}