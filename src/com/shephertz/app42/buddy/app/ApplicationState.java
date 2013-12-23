package com.shephertz.app42.buddy.app;

import android.graphics.Bitmap;


public class ApplicationState {
	private static ApplicationState mInstance=null;
	private boolean isAuthenticated;
	private String userName;
	private String avatarName;
	private String avatarPicUrl;
	private Bitmap avatarPic;
	
	public Bitmap getAvatarPic() {
		return avatarPic;
	}
	public void setAvatarPic(Bitmap avatarPic) {
		this.avatarPic = avatarPic;
	}
	private String avatarUserName;
	public String getAvatarUserName() {
		return avatarUserName;
	}
	public void setAvatarUserName(String avatarUserName) {
		this.avatarUserName = avatarUserName;
	}
	public String getAvatarName() {
		return avatarName;
	}
	public void setAvatarName(String avatarName) {
		this.avatarName = avatarName;
	}
	public String getAvatarPicUrl() {
		return avatarPicUrl;
	}
	public void setAvatarPicUrl(String avatarPicUrl) {
		this.avatarPicUrl = avatarPicUrl;
	}
	
	public static ApplicationState getmInstance() {
		return mInstance;
	}
	public static void setmInstance(ApplicationState mInstance) {
		ApplicationState.mInstance = mInstance;
	}
	public boolean isAuthenticated() {
		return isAuthenticated;
	}
	public void setAuthenticated(boolean isAuthenticated) {
		this.isAuthenticated = isAuthenticated;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getMailId() {
		return mailId;
	}
	public void setMailId(String mailId) {
		this.mailId = mailId;
	}
	private String password;
	private String mailId;
	/*
	 * instance of class
	 */
	public static ApplicationState instance() {
		if (mInstance == null) {
			mInstance = new ApplicationState();
		}
		return mInstance;
	}
	
	
}
