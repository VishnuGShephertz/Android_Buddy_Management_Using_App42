Android_Buddy_Management_Using_App42
====================================

# About Application

This application shows how can we do Manage our own Buddies by making an Android application using App42API.You can use various features by managing your Buddies like :

1. You can Add/Reject user as Buddy.
2. You can also block user.
3. You can Create Group of Buddies.
4. You can Add buddies in Group.
4. You can Create your own Avatar and change it accordingly.
5. You can Exchange messages between groups and buddies.
6. You can also share pic to Group and Buddy.
6. You can use this application even you are in offline mode.

# Running Sample

This is a sample Android Buddy Mange app is made by using App42 back-end platform. It uses User,Upload,Buddy,Avatart service of App42 platform.It also uses Offline storage and offline cache management of App42 platform.
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

__1. Initialize Services:__ At first you have to register on App42 using your API_KEY and SECRET_KEY keys. And have to initialize all services first.

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


__2. Offline Cache Management:__ Offline caching enables your application to read data from local cache if network is not available. There are different cache levels that can be set using API and have been explained below.

A. Policy.NETWORK_FIRST – This policy enables data to be fetched from network and then update cache. If network is not available, data is fetched from cache.This type of caching is used in this application.

```
        App42CacheManager.setPolicy(Policy.NETWORK_FIRST);
    
```

B. Policy.CACHE_FIRST – Setting this policy will enable all read data to be first looked in to cache if data is available and not expired then it will return from cache otherwise network request will be made and cache will be updated for the same. You can set cache expiry time by using API as explained below. By default cache expiry is set to an hour.

```
    App42CacheManager.setPolicy(Policy.CACHE_FIRST); ;
    App42CacheManager.setExpiryInMinutes("");
    
```
C. Policy.NOCACHE – By default App42 SDK uses this policy and does not use any cache and always reads data from network only.


__3. Offline Management:__ Offline storage allows you to post data locally in case network is not available and syncs it with server later when network is available. This is useful in many scenarios e.g. if you are useing this application in offline mode as per offline caching you have your application data, If you want to send friend request to a user than to make application more efficient it uses offline caching where request  will be saved locally in case of network unavailability and will be later synced with server when network availability resumes.
```
    App42API.setOfflineStorage(true);
```
__4. Registering User:__ To use application first you have to register for this application by passing your userName, password and email-id.
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
__5. Authenticate User:__ Once you register with this app, next time authenticate yourself by passing username and password.
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
                                callBack.onUserAuthenticated(null);
                            }
                        }
                    });
                }
            }
        }.start();
    }
```

__6. Create Your Avatar:__ You can also create your own Avatar with desired name and desired picture as you look like in this application.
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
                                callBack.onAvatarCreationFailed(ex);
                            }
                        }
                    });
                }
            }
        }.start();
    }
```

__7. Get User List:__ You can easily get the users that are not your buddies and add them as buddies accordingly.
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
        ArrayList<String> users = new ArrayList<String>();
        int size = buddies.size();
        for (int i = 0; i < size; i++) {
            users.add(buddies.get(i).getBuddyName());
        }
        return users;
    }
```

__8. Send Friend Request:__ After getting app users you can easily send friend request using following code.
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

__9. Get Friend Request List:__You have to get all friend request first before accepting friend request using following code.
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
__10. Accept Friend Request :__You can accept friend request using following code.
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

__11. Reject Friend Request:__You can easily reject friend request of a user using following code.
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

__12. Block Friend Request:__You can also block  friend request of a user using following code.
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

__13. Block User:__You can also block a user using following code.
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

__14. Get Your Buddy List:__ To make any communication with your friends first you have to get your friend list , using following code.
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

__15. Send Message To Buddy:__ After getting your friend list you can exchange message in form of image and text using following code..
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

__16. Get Chat Messages with Buddy:__You Can get All messages that are communicate between you and your friend using following code..
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

__17. Creating Group:__ You can easily create a group first and than add your friends in group and exchange messages between group.
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

__18. Getting Your Group List:__You can easily get All groups , in which you are subscribed.
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

__19. Add Friends in Group:__ If you are owner of the group than you can add friends in that group to make messaging more interesting.
```
public void addFriendsInGroup(final String userName, final String grpName,
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
```
__20. Getting All Group Friends:__You can easily get all group members those are subscribe in that group.
```
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

```
__21. Getting All Group Messages:__Whenever you want to communicate in group first load all previous group messages by all group members.

```
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

```

__22. send Message in Group:__You can easily send message to group using following code.

```
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
```

__23. Send Picture Message to Group or Friend:__ If you want to share picture image with your friend and group you can easily send it in form of message using this code.

```
public void sharePictureMessage(final ImageInfo imageInfo,
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

```

__24. Gett All Messages:__You can also get all messages that are belongs to you ir-respective group or friend messages.

```
public void getAllMessages(final String username,
            final MessageEventListener callBack) {
        final Handler callerThreadHandler = new Handler();
        new Thread() {
            @Override
            public void run() {
                try {
                    final ArrayList<Buddy> buddy = buddyService
                            .getAllMessages(username);
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

```

