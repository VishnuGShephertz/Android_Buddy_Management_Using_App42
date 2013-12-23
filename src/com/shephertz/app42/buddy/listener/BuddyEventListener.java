package com.shephertz.app42.buddy.listener;

import java.util.ArrayList;

import com.shephertz.app42.paas.sdk.android.App42Exception;

public interface BuddyEventListener {

	public void onFriendRequestSent(boolean status);
	public void onBuddyBlocked(boolean status);
	public void onBlockBuddyRequest(boolean status,String buddyName);
	public void onRejectRequest(boolean status,String buddyName);
	public void onAcceptRequest(boolean status,String buddyName);
	
	public void onGetBuddyList(ArrayList<String> myBuddies);
	public void onGetInvitationList(ArrayList<String> invitations);
	public void onError(App42Exception ex);
	public void onGetAllUsers(ArrayList<String> users);
	
}
