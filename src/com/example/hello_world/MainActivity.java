package com.example.hello_world;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.SaveCallback;


public class MainActivity extends ActionBarActivity {
	public final static String EXTRA_MESSAGE = "com.mycompany.myfirstapp.MESSAGE";

	private EditText username;
	private EditText password;
	private Button login;
	private TextView loginLockedTV;
	private TextView attemptsLeftTV;
	private TextView numberOfRemainingLoginAttemptsTV;
	int numberOfRemainingLoginAttempts = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ParseInstallation.getCurrentInstallation().saveInBackground();
		Parse.initialize(this, "aTsSm7syFBbL2LKygXedkiRClOBmPpHv2loE1f00", "aAA8N4K0XfDpKHCpYL54Y7ilZLyU7hYe7l2b7keB");
		ParsePush.subscribeInBackground("", new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if (e == null) {
					Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
				} else {
					Log.e("com.parse.push", "failed to subscribe for push", e);
				}
			}
		});
		setupVariables();
	}


	public void authenticateLogin(View view) {
		if (username.getText().toString().equals("admin") && 
				password.getText().toString().equals("admin")) {
			Toast.makeText(getApplicationContext(), "Hello admin!", 
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(getApplicationContext(), "Seems like you 're not admin!", 
					Toast.LENGTH_SHORT).show();
			numberOfRemainingLoginAttempts--;
			attemptsLeftTV.setVisibility(View.VISIBLE);
			numberOfRemainingLoginAttemptsTV.setVisibility(View.VISIBLE);
			numberOfRemainingLoginAttemptsTV.setText(Integer.toString(numberOfRemainingLoginAttempts));

			if (numberOfRemainingLoginAttempts == 0) {
				login.setEnabled(false);
				loginLockedTV.setVisibility(View.VISIBLE);
				loginLockedTV.setBackgroundColor(Color.RED);
				loginLockedTV.setText("LOGIN LOCKED!!!");
			}
		}
	}

	private void setupVariables() {
		username = (EditText) findViewById(R.id.usernameET);
		password = (EditText) findViewById(R.id.passwordET);
		login = (Button) findViewById(R.id.loginBtn);
		loginLockedTV = (TextView) findViewById(R.id.loginLockedTV);
		attemptsLeftTV = (TextView) findViewById(R.id.attemptsLeftTV);
		numberOfRemainingLoginAttemptsTV = (TextView) findViewById(R.id.numberOfRemainingLoginAttemptsTV);
		numberOfRemainingLoginAttemptsTV.setText(Integer.toString(numberOfRemainingLoginAttempts));
	}


}
