package com.example.hello_world;

import android.app.Application; 
import android.util.Log;

import com.parse.Parse; 
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.SaveCallback;

public class App extends Application { 

	@Override public void onCreate() { 
		super.onCreate();
		Parse.setLogLevel(Parse.LOG_LEVEL_VERBOSE);
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
		ParseInstallation.getCurrentInstallation().saveInBackground();
	}
} 