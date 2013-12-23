package com.shephertz.app42.buddy.listener;

import java.util.ArrayList;

import com.shephertz.app42.paas.sdk.android.App42Exception;
import com.shephertz.app42.paas.sdk.android.buddy.Buddy;

public interface MessageEventListener {
	public void onMessageSentToGroup();
	public void onError(App42Exception e);
	public void onMessageSendingFailed(App42Exception e);
	public void onMesssageSentToBuddy();
	public void onGetAllMessages(ArrayList<Buddy> messages);

}
