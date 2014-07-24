package com.avmurzin.smshandler;

import android.app.Application;
import android.widget.TextView;

public class SMShandler extends Application {
	  private static boolean activityVisible;
	  private static TextView smsText;
	  
	  public static boolean isActivityVisible() {
		  return activityVisible;
	  }  

	  public static void activityResumed(TextView sms) {
		  activityVisible = true;
		  smsText = sms;
	  }

	  public static void activityPaused(TextView sms) {
		  activityVisible = false;
		  smsText = sms;
	  }
	  
	  public static TextView getTextView() {
		  return smsText;
	  }


}
