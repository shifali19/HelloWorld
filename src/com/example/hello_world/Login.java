package com.example.hello_world;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
public class Login extends Activity implements OnClickListener{
	private EditText user, pass;
	private Button bLogin;
	private EditText SignupUser, SignupPass;
	private Button Signup;
	// Progress Dialog
	private ProgressDialog pDialog;
	// JSON parser class
	JSONParser jsonParser = new JSONParser();
	private static final String LOGIN_URL = "http://mysql-anuraj.host56.com/login.php";
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";
	@Override
	protected void onCreate(Bundle savedInstanceState) {       
		super.onCreate(savedInstanceState);
		//ParseInstallation.getCurrentInstallation().saveInBackground();
		/*Parse.initialize(this, "aTsSm7syFBbL2LKygXedkiRClOBmPpHv2loE1f00", "aAA8N4K0XfDpKHCpYL54Y7ilZLyU7hYe7l2b7keB");
		Parse.setLogLevel(Parse.LOG_LEVEL_VERBOSE);
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
		ParseInstallation.getCurrentInstallation().saveInBackground();*/
		setContentView(R.layout.activity_main);      
		user = (EditText)findViewById(R.id.usernameET);
		pass = (EditText)findViewById(R.id.passwordET);          
		bLogin = (Button)findViewById(R.id.loginBtn);
		bLogin.setOnClickListener(this);
		Signup = (Button)findViewById(R.id.signUpBtn);
		Signup.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		 ContentResolver cr = getContentResolver();
		Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
		if (cur.getCount() > 0) {
			   while (cur.moveToNext()){
				   String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				   Log.d("DEBUG","Name : "+name);
				   String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
				   Log.d("DEBUG","Id : "+id);
		//Log.d("DEBUG","Number : "+cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
				   if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
					   Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",  new String[]{id}, null);
					   String phone;
					   while (pCur.moveToNext()) {
						   phone = pCur .getString(pCur .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)); 
						   Log.d("DEBUG","Number : "+phone);
					   }
				   }
			   }
		}
		switch (v.getId()) {
		case R.id.loginBtn:
			new AttemptLogin().execute();
			break;
		case R.id.signUpBtn:
			new AttemptSignup().execute();
			break;
		default:
			break;
		}
	}
	
	class AttemptSignup extends AsyncTask<String, String, String> {
		@Override
		protected String doInBackground(String... args) {
				// TODO Auto-generated method stub
				// here Check for success tag
				int success;
				String username = user.getText().toString();
				String password = pass.getText().toString();
				ParseUser user = new ParseUser();
				user.setUsername(username);
				user.setPassword(password);
				TelephonyManager mTelephonyMgr;  
				mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);   

				int yourNumber = Integer.parseInt(mTelephonyMgr.getLine1Number());  
				Log.d("DEBUG","Contact NUmber : "+yourNumber);
				if(yourNumber<0)
				user.put("contact_no",99999999);
				else
					user.put("contact_no",yourNumber);
				user.signUpInBackground(new SignUpCallback() {
					  public void done(ParseException e) {
						 if (e == null) {
					    	Intent ii = new Intent(Login.this,OtherActivity.class);
							finish();
							// this finish() method is used to tell android os that we are done with current //activity now! Moving to other activity
							startActivity(ii);
							} else {
						Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_LONG).show();
					    }
					  }
					});
				return null;
	   }
	}

	class AttemptLogin extends AsyncTask<String, String, String> {
		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		boolean failure = false;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Login.this);
			pDialog.setMessage("Attempting for login...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
		String username = user.getText().toString();
		String password = pass.getText().toString();
		
		@Override
		protected String doInBackground(String... args) {
		ParseUser.logInInBackground(username,password, new LogInCallback() {
			  public void done(ParseUser user, ParseException e) {
			    if (user != null) {
			    	Intent ii = new Intent(Login.this,OtherActivity.class);
					finish();
					// this finish() method is used to tell android os that we are done with current //activity now! Moving to other activity
					startActivity(ii);
			    } else {
			    	Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_LONG).show();
			    }
			  }
			});
		return null;
		}
/*		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			// here Check for success tag
			int success;
			String username = user.getText().toString();
			String password = pass.getText().toString();
			try {

				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("username", username));
				params.add(new BasicNameValuePair("password", password));

				Log.d("request!", "starting");

				JSONObject json = jsonParser.makeHttpRequest(
						LOGIN_URL, "POST", params);

				// checking  log for json response
				Log.d("Login attempt", json.toString());

				// success tag for json
				success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					Log.d("Successfully Login!", json.toString());

					Intent ii = new Intent(Login.this,OtherActivity.class);
					finish();
					// this finish() method is used to tell android os that we are done with current //activity now! Moving to other activity
					startActivity(ii);
					return json.getString(TAG_MESSAGE);
				}else{

					return json.getString(TAG_MESSAGE);

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}*/
		/**
		 * Once the background process is done we need to  Dismiss the progress dialog asap
		 * **/
		protected void onPostExecute(String message) {

			pDialog.dismiss();
			if (message != null){
				Toast.makeText(Login.this, message, Toast.LENGTH_LONG).show();
			}
		}
	} 
}
