Android_Buddy_Management_Using_App42
====================================

# About Application

This application shows how can we do Manage our own Buddies by making an Android application using App42API.You can use various features by managing your Buddies like :2. Add User as a Buddy.

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

All source code communication with App42 API is written in  App42ServiceApi.java file.

__Initialize Services:__ At first you have to register on App42 using your APIKEY and SECRETKEY keys. And have to intialiaze all services first.

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
__Registering User:__ To use application first you have to register for this application by passing your userName, password and email-id.
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
__Registering User:__ To use application first you have to register for this application by passing your userName, password and email-id.
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

__Registering User:__ To use application first you have to register for this application by passing your userName, password and email-id.
```
```

__Registering User:__ To use application first you have to register for this application by passing your userName, password and email-id.
```
```

__Registering User:__ To use application first you have to register for this application by passing your userName, password and email-id.
```
```

__Registering User:__ To use application first you have to register for this application by passing your userName, password and email-id.
```
```

