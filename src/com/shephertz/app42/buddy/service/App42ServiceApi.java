package com.shephertz.app42.buddy.service;

import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.os.Handler;

import com.shephertz.app42.buddy.app.AppContext;
import com.shephertz.app42.buddy.app.ImageInfo;
import com.shephertz.app42.buddy.listener.AvatarEventListener;
import com.shephertz.app42.buddy.listener.BuddyEventListener;
import com.shephertz.app42.buddy.listener.BuddyGroupEventListener;
import com.shephertz.app42.buddy.listener.MessageEventListener;
import com.shephertz.app42.buddy.listener.UserEventListener;
import com.shephertz.app42.buddy.util.Constants;
import com.shephertz.app42.paas.sdk.android.App42API;
import com.shephertz.app42.paas.sdk.android.App42CacheManager;
import com.shephertz.app42.paas.sdk.android.App42CacheManager.Policy;
import com.shephertz.app42.paas.sdk.android.App42Exception;
import com.shephertz.app42.paas.sdk.android.App42Response;
import com.shephertz.app42.paas.sdk.android.avatar.Avatar;
import com.shephertz.app42.paas.sdk.android.avatar.AvatarService;
import com.shephertz.app42.paas.sdk.android.buddy.Buddy;
import com.shephertz.app42.paas.sdk.android.buddy.BuddyService;
import com.shephertz.app42.paas.sdk.android.upload.Upload;
import com.shephertz.app42.paas.sdk.android.upload.UploadFileType;
import com.shephertz.app42.paas.sdk.android.upload.UploadService;
import com.shephertz.app42.paas.sdk.android.user.User;
import com.shephertz.app42.paas.sdk.android.user.UserService;

public class App42ServiceApi {

	private UserService userService;
	private static App42ServiceApi mInstance = null;
	private BuddyService buddyService;
	private AvatarService avatarService;
	private UploadService uploadService;

	private App42ServiceApi(Context context) {
		
		App42API.initialize(context, Constants.App42ApiKey,
				Constants.App42ApiSecret);
		App42CacheManager.setPolicy(Policy.NETWORK_FIRST);
		App42API.setOfflineStorage(true);
		this.userService = App42API.buildUserService();
		this.buddyService = new BuddyService(Constants.App42ApiKey,
				Constants.App42ApiSecret, "");
		this.avatarService = App42API.buildAvatarService();
		this.uploadService=App42API.buildUploadService();
		
		
	}

	/*
	 * instance of class
	 */
	public static App42ServiceApi instance(Context context) {

		if (mInstance == null) {
			mInstance = new App42ServiceApi(context);
		}

		return mInstance;
	}

	/*
	 * This function validate user's authentication with APP42
	 */
	public void createAvatar(final String userName, final String avatarName,
			final String imagePath, final String description,
			final AvatarEventListener callBack) {
		final Handler callerThreadHandler = new Handler();
		new Thread() {
			@Override
			public void run() {
				try {
					final Avatar avatar = avatarService.createAvatar(
							avatarName, userName, imagePath, description);
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							callBack.onAvatarCreated(avatar);
						}
					});
				} catch (final App42Exception ex) {
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							if (callBack != null) {
								System.out.println(ex.toString());
								callBack.onAvatarCreationFailed(ex);
							}
						}
					});
				}
			}
		}.start();
	}
	/*
	 * This function allows user to share photo with face-book friend.
	 * 
	 * @param json data contains information of photo user and friend
	 * 
	 * @param callback instance of class on which we have to return
	 */
	public void sharePic(final ImageInfo imageInfo,
			final MessageEventListener callback) {
		final Handler callingThreadHandler = new Handler();
		new Thread() {
			@Override
			public void run() {
				try {
					final boolean uploadStatus = uploadPhoto(imageInfo);
					callingThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							if (callback != null) {
								callback.onMesssageSentToBuddy();
							}
						}
					});
				} catch (final App42Exception ex) {
					callingThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							if (callback != null) {
								callback.onMessageSendingFailed(ex);
							}
						}
					});
				}
			}
		}.start();

	}

	/*
	 * This function allows user to upload photo
	 * 
	 * @param jsonData contains information of photo,user and friend
	 */
	private boolean uploadPhoto(ImageInfo imageInfo) throws App42Exception {
		String photoID = "Id" + new Date().getTime();
		Upload uploadObj = uploadService.uploadFileForUser(photoID,
				AppContext.myUserName,
				imageInfo.getImagePath(), UploadFileType.IMAGE,"Hey");
		if (uploadObj.isResponseSuccess()) {
			String url=uploadObj.getFileList().get(0)
					.getUrl();
			if(imageInfo.isBuudy()){
				buddyService.sendMessageToFriend(AppContext.myUserName, imageInfo.getBuddyName(), url);
			}
			else {
				buddyService.sendMessageToGroup(AppContext.myUserName, imageInfo.getOwnerName(), imageInfo.getGroupname(), url);
			}
			return true;
		} else {
			return false;
		}
		

	}
	/*
	 * This function validate user's authentication with APP42
	 */
	public void authenticateUser(final String name, final String pswd,
			final UserEventListener callBack) {
		final Handler callerThreadHandler = new Handler();
		new Thread() {
			@Override
			public void run() {
				try {
					final App42Response response = userService.authenticate(
							name, pswd);
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							callBack.onUserAuthenticated(response);
						}
					});
				} catch (final App42Exception ex) {
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							if (callBack != null) {
								System.out.println(ex.toString());
								callBack.onUserAuthenticated(null);
							}
						}
					});
				}
			}
		}.start();
	}

	/*
	 * This function allows to create user using APP42 service
	 */
	public void createUser(final String name, final String pswd,
			final String email, final UserEventListener callBack) {
		final Handler callerThreadHandler = new Handler();
		new Thread() {
			@Override
			public void run() {
				try {
					final User user = userService.createUser(name, pswd, email);
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							callBack.onUserCreated(user);
						}
					});
				} catch (final App42Exception ex) {
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							if (callBack != null) {
								System.out.println(ex.toString());
								callBack.onUserCreated(null);
							}
						}
					});

				}
			}
		}.start();
	}

	public void sendFriendRequest(final String username,
			final String buddyName, final String message,
			final BuddyEventListener callBack) {
		final Handler callerThreadHandler = new Handler();
		new Thread() {
			@Override
			public void run() {
				try {
					buddyService
							.sendFriendRequest(username, buddyName, message);
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							callBack.onFriendRequestSent(true);
						}
					});
				} catch (final App42Exception ex) {
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							if (callBack != null) {
								callBack.onFriendRequestSent(false);
							}
						}
					});
				}
			}
		}.start();
	}

	public void blockBuddy(final String userName, final String buddyName,
			final BuddyEventListener callBack) {
		final Handler callerThreadHandler = new Handler();
		new Thread() {
			@Override
			public void run() {
				try {
					buddyService.blockUser(userName, buddyName);
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							callBack.onBuddyBlocked(true);
						}
					});
				} catch (final App42Exception ex) {
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							if (callBack != null) {
								callBack.onBuddyBlocked(false);
							}
						}
					});
				}
			}
		}.start();
	}

	public void blockFriendRequest(final String userName,
			final String buddyName, final BuddyEventListener callBack) {
		final Handler callerThreadHandler = new Handler();
		new Thread() {
			@Override
			public void run() {
				try {
					buddyService.blockFriendRequest(userName, buddyName);
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							callBack.onBlockBuddyRequest(true, buddyName);
						}
					});
				} catch (final App42Exception ex) {
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							if (callBack != null) {
								callBack.onBlockBuddyRequest(false, buddyName);
							}
						}
					});
				}
			}
		}.start();
	}

	public void loadInvitationList(final String userName,
			final BuddyEventListener callBack) {
		final Handler callerThreadHandler = new Handler();
		new Thread() {
			@Override
			public void run() {
				try {

					final ArrayList<String> requestList = getRequestedList(userName);
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							callBack.onGetInvitationList(requestList);
						}
					});
				} catch (final App42Exception ex) {
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							if (callBack != null) {
								callBack.onError(ex);
							}
						}
					});
				}
			}
		}.start();
	}

	private ArrayList<String> getRequestedList(String userName) {
		return getRequestList(buddyService.getFriendRequest(userName));
	}

	private ArrayList<String> getFriendList(String userName) {
		return getRequestList(buddyService.getAllFriends(userName));
	}

	public void loadMyBuddyList(final String userName,
			final BuddyEventListener callBack) {

		final Handler callerThreadHandler = new Handler();
		new Thread() {
			@Override
			public void run() {
				try {
					final ArrayList<String> buddyList = getFriendList(userName);
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							callBack.onGetBuddyList(buddyList);
						}
					});
				} catch (final App42Exception ex) {
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							if (callBack != null) {
								callBack.onError(ex);
							}
						}
					});
				}
			}
		}.start();
	}

	public void acceptFriendRequest(final String username,
			final String buddyName, final BuddyEventListener callBack) {
		final Handler callerThreadHandler = new Handler();
		new Thread() {
			@Override
			public void run() {
				try {
					buddyService.acceptFriendRequest(username, buddyName);
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							callBack.onAcceptRequest(true, buddyName);
						}
					});
				} catch (final App42Exception ex) {
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							if (callBack != null) {
								callBack.onAcceptRequest(false, buddyName);
							}
						}
					});
				}
			}
		}.start();
	}

	public void rejectFriendRequest(final String username,
			final String buddyName, final BuddyEventListener callBack) {
		final Handler callerThreadHandler = new Handler();
		new Thread() {
			@Override
			public void run() {
				try {
					buddyService.rejectFriendRequest(username, buddyName);
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							callBack.onRejectRequest(true, buddyName);
						}
					});
				} catch (final App42Exception ex) {
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							if (callBack != null) {
								callBack.onRejectRequest(false, buddyName);
							}
						}
					});
				}
			}
		}.start();
	}

	public void addFriednToGroup(final String username, final String groupname,
			final ArrayList<String> friends,
			final BuddyGroupEventListener callBack) {
		final Handler callerThreadHandler = new Handler();
		new Thread() {
			@Override
			public void run() {
				try {
					buddyService.addFriendToGroup(username, groupname, friends);
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							callBack.onFriendAddedInGroup();
						}
					});
				} catch (final App42Exception ex) {
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							if (callBack != null) {
								callBack.onError(ex);
							}
						}
					});
				}
			}
		}.start();
	}

	public void createGroup(final String username, final String groupname,
			final BuddyGroupEventListener callBack) {
		final Handler callerThreadHandler = new Handler();
		new Thread() {
			@Override
			public void run() {
				try {
					final Buddy buddy = buddyService.createGroupByUser(
							username, groupname);
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							callBack.onGroupCreated(true, buddy);
						}
					});
				} catch (final App42Exception ex) {
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							if (callBack != null) {
								callBack.onGroupCreated(false, null);
							}
						}
					});
				}
			}
		}.start();
	}

	public void sendMessageToBuddy(final String username,
			final String buddyName, final String message,
			final MessageEventListener callBack) {
		final Handler callerThreadHandler = new Handler();
		new Thread() {
			@Override
			public void run() {
				try {
					buddyService.sendMessageToFriend(username, buddyName,
							message);
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							callBack.onMesssageSentToBuddy();
						}
					});
				} catch (final App42Exception ex) {
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							if (callBack != null) {
								callBack.onMessageSendingFailed(ex);
							}
						}
					});
				}
			}
		}.start();
	}

	public void sendMessageToGroup(final String username, final String owner,
			final String groupname, final String message,
			final MessageEventListener callBack) {
		final Handler callerThreadHandler = new Handler();
		new Thread() {
			@Override
			public void run() {
				try {
					buddyService.sendMessageToGroup(username, owner, groupname,
							message);
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							callBack.onMessageSentToGroup();
						}
					});
				} catch (final App42Exception ex) {
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							if (callBack != null) {
								callBack.onMessageSendingFailed(ex);
							}
						}
					});
				}
			}
		}.start();
	}

	public void getAllMessagesFromGroup(final String username,
			final String owner, final String groupname,
			final MessageEventListener callBack) {
		final Handler callerThreadHandler = new Handler();
		new Thread() {
			@Override
			public void run() {
				try {

					final ArrayList<Buddy> buddy = buddyService
							.getAllMessagesFromGroup(username, owner, groupname);
					System.out.println("Getting datat" + buddy.toString());

					for (int i = 0; i < buddy.size(); i++) {
						System.out.println("owner is : "
								+ buddy.get(i).getOwnerName());
						System.out.println("userName is : "
								+ buddy.get(i).getUserName());
						System.out.println("message is : "
								+ buddy.get(i).getMessage());
						System.out.println("messageId is : "
								+ buddy.get(i).getMessageId());
						System.out.println("SendedOn is : "
								+ buddy.get(i).getSendedOn());
					}
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							callBack.onGetAllMessages(buddy);
						}
					});
				} catch (final App42Exception ex) {
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							if (callBack != null) {
								callBack.onError(ex);
							}
						}
					});
				}
			}
		}.start();
	}

	public void getAllMessagesFromBuddy(final String username,
			final String buddyName, final MessageEventListener callBack) {
		final Handler callerThreadHandler = new Handler();
		new Thread() {
			@Override
			public void run() {
				try {

					final ArrayList<Buddy> buddy = getBuddyChat(username,
							buddyName);
					System.out.println("Getting datat" + buddy.toString());

					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							callBack.onGetAllMessages(buddy);
						}
					});
				} catch (final App42Exception ex) {
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							if (callBack != null) {
								callBack.onError(ex);
							}
						}
					});
				}
			}
		}.start();
	}

	private ArrayList<Buddy> getBuddyChat(String userName, String buddyName) {
		ArrayList<Buddy> buddy = new ArrayList<Buddy>();
		try {
			buddy = buddyService.getAllMessagesFromBuddy(userName, buddyName);
		} catch (App42Exception e) {
			e.printStackTrace();
		}
		try {
			buddy.addAll(buddyService.getAllMessagesFromBuddy(buddyName,
					userName));
		} catch (App42Exception e) {
			e.printStackTrace();
		}

		return buddy;
	}

	public void getAllMessages(final String username,
			final MessageEventListener callBack) {
		final Handler callerThreadHandler = new Handler();
		new Thread() {
			@Override
			public void run() {
				try {
					final ArrayList<Buddy> buddy = buddyService
							.getAllMessages(username);

					System.out.println("Getting datat" + buddy.toString());

					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							callBack.onGetAllMessages(buddy);
						}
					});
				} catch (final App42Exception ex) {
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							if (callBack != null) {
								callBack.onError(ex);
							}
						}
					});
				}
			}
		}.start();
	}

	/*
	 * This function validate user's authentication with APP42
	 */
	public void loaduserList(final BuddyEventListener callBack) {
		final Handler callerThreadHandler = new Handler();
		new Thread() {
			@Override
			public void run() {
				try {
					ArrayList<User> userList = userService.getAllUsers();
					final ArrayList<String> users = getUserList(userList);
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							callBack.onGetAllUsers(users);
						}
					});
				} catch (final App42Exception ex) {
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							if (callBack != null) {
								System.out.println(ex.toString());
								callBack.onError(ex);
							}
						}
					});
				}
			}
		}.start();
	}

	private ArrayList<String> getUserList(ArrayList<User> userList) {
		ArrayList<String> users = new ArrayList<String>();
		
		for (int i = 0; i < userList.size(); i++) {
			users.add(userList.get(i).getUserName().toString());
		}
		try{
		ArrayList<String> friends=getFriendList(AppContext.myUserName);
		if(friends!=null)
			users.removeAll(friends);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return users;
	}

	public void loadFriednsByGroup(final String userName,
			final String ownerName, final String groupName,
			final boolean isOwner, final BuddyGroupEventListener callBack) {
		final Handler callerThreadHandler = new Handler();
		new Thread() {
			@Override
			public void run() {
				try {

					final ArrayList<String> grpBuddy = getMyBuddyList(userName,
							ownerName, groupName);
					final ArrayList<String> friends = getAllFriends(isOwner,
							userName);
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							callBack.onGetGroupFriends(grpBuddy, friends);
						}
					});
				} catch (final App42Exception ex) {
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							if (callBack != null) {
								callBack.onError(ex);
							}
						}
					});
				}
			}
		}.start();
	}

	private ArrayList<String> getRequestList(ArrayList<Buddy> friendRequest) {
		// TODO Auto-generated method stub
		ArrayList<String> users = new ArrayList<String>();
		int size = friendRequest.size();
		for (int i = 0; i < size; i++) {
			users.add(friendRequest.get(i).getBuddyName());
		}
		return users;
	}

	private ArrayList<String> getMyBuddyList(String userName, String ownerName,
			String groupName) {

		try {
			return getRequestList(buddyService.getAllFriendsInGroup(userName,
					ownerName, groupName));

		} catch (App42Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public ArrayList<String> getAllFriends(boolean isowner, String userName) {
		if (isowner) {
			return getFriendList(userName);
		} else {
			return null;
		}
	}

	/*
	 * This function validate user's authentication with APP42
	 */
	public void loadGroupList(final String userName,
			final BuddyGroupEventListener callBack) {
		final Handler callerThreadHandler = new Handler();
		new Thread() {
			@Override
			public void run() {
				try {

					final ArrayList<Buddy> grpBuddy = buddyService
							.getAllGroups(userName);
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							callBack.onBuddyGroupList(grpBuddy);
						}
					});
				} catch (final App42Exception ex) {
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							if (callBack != null) {
								callBack.onError(ex);
							}
						}
					});
				}
			}
		}.start();
	}

	/*
	 * This function validate user's authentication with APP42
	 */
	public void addFriends(final String userName, final String grpName,
			final ArrayList<String> friends,
			final BuddyGroupEventListener callBack) {
		final Handler callerThreadHandler = new Handler();
		new Thread() {
			@Override
			public void run() {
				try {

					buddyService.addFriendToGroup(userName, grpName, friends);
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							callBack.onFriendAddedInGroup();
						}
					});
				} catch (final App42Exception ex) {
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							if (callBack != null) {
								callBack.onError(ex);
							}
						}
					});
				}
			}
		}.start();
	}

}
