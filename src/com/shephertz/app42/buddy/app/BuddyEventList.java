package com.shephertz.app42.buddy.app;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.shephertz.app42.buddy.adapter.ListAdapter;
import com.shephertz.app42.buddy.listener.AvatarEventListener;
import com.shephertz.app42.buddy.service.App42ServiceApi;
import com.shephertz.app42.buddy.util.Constants;
import com.shephertz.app42.buddy.util.Utils;
import com.shephertz.app42.paas.sdk.android.App42Exception;
import com.shephertz.app42.paas.sdk.android.avatar.Avatar;

public class BuddyEventList extends Activity implements OnItemClickListener, AvatarEventListener{
	private final int RESULT_LOAD_IMAGE = 1;
	private String avatarName;
	private App42ServiceApi app42Service;
	private ApplicationState appState;
	private ImageView myPic;
	private TextView myName ;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buddy_home);
		myPic = (ImageView) findViewById(R.id.user_pic);
		myName = (TextView) findViewById(R.id.username);;
		ListView list = (ListView) findViewById(R.id.list);
		list.setAdapter(new ListAdapter(this, getMenuList()));
		list.setOnItemClickListener(this);
		app42Service=App42ServiceApi.instance(this);
		appState=ApplicationState.instance();
		buildProfileView();
		
	}

	private void buildProfileView(){
		if(appState.getAvatarName()==null){
			myName.setText(AppContext.myUserName);
		}
		else{
			myName.setText(appState.getAvatarName());
		}
		if(appState.getAvatarPic()!=null){
			myPic.setBackgroundResource(0);
		    myPic.setImageBitmap(appState.getAvatarPic());
		}
		else{
			myPic.setImageResource(R.drawable.default_pic);
		}
	}
	private ArrayList<String> getMenuList() {
		ArrayList<String> menu = new ArrayList<String>();
		for (String item : Constants.MENU) {
			menu.add(item);
		}
		return menu;
	}

	public void onLogoutClicked(View view) {
		ApplicationState.instance().setAuthenticated(false);
		this.finish();
		Intent mainIntent = new Intent(this, LoginActivity.class);
		this.startActivity(mainIntent);
	}

	public void onEditClicked(View view) {
		browsePhotoDialog();
	}
	/*
	 * used to create alert dialog when logout option is selected
	 * @param name of friend whom you want to sahre image
	 */
	public void browsePhotoDialog() {
		AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
		alertbox.setTitle("Create Your Avatar");
	       final EditText input = new EditText(this);
	       input.setHint("Avatar name");
	       alertbox.setView(input);
		   alertbox.setPositiveButton("Browse Pic",
				new DialogInterface.OnClickListener() {
					// do something when the button is clicked
					public void onClick(DialogInterface arg0, int arg1) {
						
						browsePhoto(input.getText().toString());
					}
				});
		alertbox.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {

					}
				});
		alertbox.show();
	}
	
	/*
	 * Call when user clicks on browse photo
	 */
	private void browsePhoto(String avtarName) {
	this.avatarName=avtarName;
	if(this.avatarName!=null&&!this.avatarName.equals("")){
		Intent i = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(i, RESULT_LOAD_IMAGE);
	}
	else{
		Toast.makeText(this, R.string.error_avatar, Toast.LENGTH_SHORT).show();
	}
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
			createAvater(picturePath);
		} 

	}
	
	private void createAvater(String imgPath){
		appState.setAvatarName(avatarName);
		appState.setAvatarPic(BitmapFactory.decodeFile(imgPath));
		  buildProfileView();
		app42Service.createAvatar(AppContext.myUserName, avatarName, imgPath, "My Avatar", this);
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		if (position == Constants.BuddyList) {
			Intent intent = new Intent(this, MyBuddyList.class);
			startActivity(intent);
		} else if (position == Constants.InvitList) {
			Intent intent = new Intent(this, InvitationList.class);
			startActivity(intent);
		} else if (position == Constants.GroupList) {
			Intent intent = new Intent(this, GroupList.class);
			startActivity(intent);
		} else if (position == Constants.AllUsers) {
			Intent intent = new Intent(this, UserList.class);
			startActivity(intent);
		} else if (position == Constants.AllMsges) {
			Intent intent = new Intent(this, MessageActivity.class);
			intent.putExtra(Constants.MsgType, Constants.AllMsg);
			startActivity(intent);
		}
	}

	@Override
	public void onAvatarCreated(Avatar avatar) {
	     // TODO Auto-generated method stub
		appState.setAvatarUserName(avatar.getUserName());
		appState.setAvatarName(avatar.getName());
		appState.setAvatarPicUrl(avatar.getTinyURL());
		Utils.loadImageFromUrl(avatar.getTinyURL(), this);
	    buildProfileView();
	}

	@Override
	public void onAvatarCreationFailed(App42Exception ex) {
	}

	@Override
	public void onLoadImage(Bitmap bitmap) {
		// TODO Auto-generated method stub
		if(bitmap!=null){
			appState.setAvatarPic(bitmap);
			myPic.setImageBitmap(bitmap);
		}
	}

}