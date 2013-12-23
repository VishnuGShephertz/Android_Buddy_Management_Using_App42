package com.shephertz.app42.buddy.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.shephertz.app42.buddy.listener.UserEventListener;
import com.shephertz.app42.buddy.service.App42ServiceApi;
import com.shephertz.app42.paas.sdk.android.App42Exception;
import com.shephertz.app42.paas.sdk.android.App42Response;
import com.shephertz.app42.paas.sdk.android.user.User;

public class LoginActivity extends Activity implements UserEventListener {

	private App42ServiceApi app42Service;
	private EditText userName;
	private EditText password;
	private EditText emailid;
	private ProgressDialog progressDialog;
	private ApplicationState appState;
	private String regName="Archu";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		progressDialog=new ProgressDialog(this);
		userName = (EditText) this.findViewById(R.id.uname);
		password = (EditText) this.findViewById(R.id.pswd);
		emailid = (EditText) this.findViewById(R.id.email);
	
		app42Service = App42ServiceApi.instance(this);
		appState=ApplicationState.instance();
		userName.setText(regName);
		password.setText("archu");
		emailid.setText(regName+"@gmail.com");
	}

	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	public void onStart() {
		super.onStart();
		userName.setText(appState.getUserName());
		password.setText(appState.getPassword());
		emailid.setText(appState.getMailId());
		if(appState.isAuthenticated()){
			gotoHomeActivity(appState.getUserName());
		}
	}

	public void onSigninClicked(View view) {
		progressDialog = ProgressDialog.show(this, "", "signing in..");
		progressDialog.setCancelable(true);
		app42Service.authenticateUser(userName.getText().toString(), password
				.getText().toString(), this);
	}

	public void onRegisterClicked(View view) {
		progressDialog = ProgressDialog.show(this, "", "registering..");
		progressDialog.setCancelable(true);
		app42Service.createUser(userName.getText().toString(), password
				.getText().toString(), emailid.getText().toString(), this);
	}

	private void saveDetails() {
		appState.setUserName(userName.getText()
				.toString());
		appState.setPassword(password.getText()
				.toString());
		appState.setMailId(emailid.getText()
				.toString());
		appState.setAuthenticated(true);
	}

	private void gotoHomeActivity(String signedInUserName) {
		this.finish();
		AppContext.myUserName=signedInUserName;
		Intent mainIntent = new Intent(this, BuddyEventList.class);
		this.startActivity(mainIntent);
	}
	@Override
	public void onUserCreated(final User user) {
		progressDialog.dismiss();
		if (user != null) {
			saveDetails();
			gotoHomeActivity(userName.getText().toString());
		} else {
			Toast.makeText(this, R.string.user_creation_failed, Toast.LENGTH_SHORT)
					.show();
		}
	}
	
	@Override
	public void onUserAuthenticated(final App42Response response) {
		progressDialog.dismiss();
		if (response != null) {
			saveDetails();
			gotoHomeActivity(userName.getText().toString());
		} else {
			Toast.makeText(this, R.string.user_auth_failed,
					Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void onError(App42Exception ex) {
		// TODO Auto-generated method stub
		
	}

}
