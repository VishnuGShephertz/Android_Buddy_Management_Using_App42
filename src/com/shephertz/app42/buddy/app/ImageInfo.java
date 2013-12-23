package com.shephertz.app42.buddy.app;

public class ImageInfo {

	private boolean isGroup;
	public boolean isGroup() {
		return isGroup;
	}
	public void setGroup(boolean isGroup) {
		this.isGroup = isGroup;
	}
	private boolean isBuudy;
	public ImageInfo(boolean isGroup, boolean isBuudy, String buddyName,
			String groupname, String imagePath, String ownerName) {
		super();
		this.isGroup = isGroup;
		this.isBuudy = isBuudy;
		this.buddyName = buddyName;
		this.groupname = groupname;
		this.imagePath = imagePath;
		this.ownerName = ownerName;
	}
	
	public static ImageInfo getGroupImage(String groupName,String imagePath,String owner){
		return new ImageInfo(true, false, null, groupName, imagePath,owner);
	}
	public static ImageInfo getUserImage(String buddyName,String imagePath,String owner){
		return new ImageInfo(false, true, buddyName, null, imagePath,owner);
	}
	public boolean isBuudy() {
		return isBuudy;
	}
	public void setBuudy(boolean isBuudy) {
		this.isBuudy = isBuudy;
	}
	public String getBuddyName() {
		return buddyName;
	}
	public void setBuddyName(String buddyName) {
		this.buddyName = buddyName;
	}
	public String getGroupname() {
		return groupname;
	}
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
	public String getImagePath() {
		return imagePath;
	}
	
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	private String buddyName;
	private String groupname;
	private String imagePath;
	private String ownerName;
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
}
