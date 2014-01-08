Android_Buddy_Management_Using_App42
====================================

# About Application

This application shows how can we do Manage our own Buddies by making an Android application using App42API.You can use various features by managing your Buddies like :

1. You can Add/Reject user as Buddy.
2. You can also block user.
3. You can Cretae a Group of Buddies.
4. You can Add buddies in Group.
4. You can Create your own Avatar and change it accordingly.
5. You can Exchange messages beteen groups and buddies.
6. You can also share pic to Group and Buddy.
6. You can use this application even you are in offline mode.

# Running Sample

This is a sample Android Buddy Mange app is made by using App42 back-end platform. It uses User,Upload,Buddy,Avatart service of App42 platform.It also uses Offline storage of App42 platform.
Here are the few easy steps to run this sample app.

1. [Register] (https://apphq.shephertz.com/register) with App42 platform.
2. Create an app once you are on Quick start page after registration.
3. If you are already registered, login to [AppHQ] (http://apphq.shephertz.com) console and create an app from App Manager Tab.
3. Download project from [here] (https://github.com/shephertz/Android_Buddy_Management_Using_App42/archive/master.zip) and import it in your Eclipse.
4. Open Constants.java file in sample app and make these changes.

```
A. Change App42ApiKey and App42ApiSecret that you have received in step 2 or 3 at line no 4 and 4.
```
11.Build your android apk.


# Design Details:

All source code communication with App42 API is written in  App42ServiceApi.java file.Here are few coding steps that are required to do your own buddy Management in Android Application.

__1. Initialize Services:__ At first you have to register on App42 using your APIKEY and SECRETKEY keys. And have to intialiaze all services first.

```
   
		App42API.initialize(context, Constants.App42ApiKey,
				Constants.App42ApiSecret);
		this.userService = App42API.buildUserService();
		this.buddyService = App42API.buildBuddyService();
		this.avatarService = App42API.buildAvatarService();
		this.uploadService=App42API.buildUploadService();
		App42CacheManager.setPolicy(Policy.NETWORK_FIRST);
		App42API.setOfflineStorage(true);
	
```
__2. Registering User:__ To use application first you have to register for this application by passing your userName, password and email-id.
```
   
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
								callBack.onUserCreated(null);
							}
						}
					});

				}
			}
		}.start();
	}
	
```
__3. Authenticate User:__ Once you register with this app, next time authenticate yourself by passing userName and password.
```
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
```

__4. Create Your Avatar:__ You can also create your own Avatar.
```

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
```

__5. Get User List:__ You can easily get the users that are not your buddies and add them as buddies accordingly.
```
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
		ArrayList<String> friends=getRequestList(buddyService.getAllFriends(AppContext.myUserName));
		if(friends!=null)
			users.removeAll(friends);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return users;
	}
	private ArrayList<String> getRequestList(ArrayList<Buddy> buddies) {
		// TODO Auto-generated method stub
		ArrayList<String> users = new ArrayList<String>();
		int size = buddies.size();
		for (int i = 0; i < size; i++) {
			users.add(buddies.get(i).getBuddyName());
		}
		return users;
	}
```

__6. Sending Friend Request:__ After getting app users you can easily send friend request using following code.
```
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
```

__7. Getting Friend Request List:__You have to  get  all friend request first before accepting friend request using following code.
```
public void loadInvitationList(final String userName,
			final BuddyEventListener callBack) {
		final Handler callerThreadHandler = new Handler();
		new Thread() {
			@Override
			public void run() {
				try {

					final ArrayList<String> requestList = getRequestList(buddyService.getFriendRequest(userName));
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

```
__8. Accept Friend Request :__You can accept friend request using following code.
```
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
```

__7. Reject Friend Request:__You can easily reject friend request of a user using following code.
```
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
```

__7. Block Friend Request:__You have to  get  all friend request first before accepting friend request using following code.
```
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
```

__7. Block User:__You have to  get  all friend request first before accepting friend request using following code.
```
public void blockUser(final String userName, final String buddyName,
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
```

__7. Get Your Buddy List:__You have to  get  all friend request first before accepting friend request using following code.
```
public void loadMyBuddyList(final String userName,
			final BuddyEventListener callBack) {
		final Handler callerThreadHandler = new Handler();
		new Thread() {
			@Override
			public void run() {
				try {
					final ArrayList<String> buddyList = getRequestList(buddyService.getAllFriends(userName));
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
```

__7. Send Message To Buddy:__You have to  get  all friend request first before accepting friend request using following code.
```
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
```

__7. Get Chat Messages with Buddy:__You have to  get  all friend request first before accepting friend request using following code.
```
public void getAllMessagesFromBuddy(final String username,
			final String buddyName, final MessageEventListener callBack) {
		final Handler callerThreadHandler = new Handler();
		new Thread() {
			@Override
			public void run() {
				try {

					final ArrayList<Buddy> buddy = getBuddyChat(username,
							buddyName);
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
```

__7. Creating Group:__You have to  get  all friend request first before accepting friend request using following code.
```
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
```

__7. Getting Your Group List:__You have to  get  all friend request first before accepting friend request using following code.
```
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
```

__7. Getting Your Group List:__You have to  get  all friend request first before accepting friend request using following code.
```
```
__7. Getting Your Group List:__You have to  get  all friend request first before accepting friend request using following code.
```
```
