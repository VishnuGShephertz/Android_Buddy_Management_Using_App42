package com.shephertz.app42.buddy.listener;

import java.util.ArrayList;

import com.shephertz.app42.paas.sdk.android.App42Exception;
import com.shephertz.app42.paas.sdk.android.buddy.Buddy;

public interface BuddyGroupEventListener {
	public void onBuddyGroupList(ArrayList<Buddy> groupList);

	public void onError(App42Exception ex);

	public void onGetGroupFriends(ArrayList<String> groupFrnds,
			ArrayList<String> friends);

	public void onFriendAddedInGroup();
	
	public void onGroupCreated(boolean status,Buddy buddy);
}
