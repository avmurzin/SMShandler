package com.avmurzin.smshandler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.view.View;
/**
 * Бродкаст-ресивер для приема СМС. В данной версии имеет приоритет выше, чем у
 * штатного обработчика и не передает ему полученное сообщение!
 * Если окно приложения активно, то немедленно показывает СМС, если не активно, то запускает
 * сервис SMSprocessor.
 * @author Andrei V. Murzin
 *
 */
public class SMSreceiver extends BroadcastReceiver {
	
	private static final String ACTION_NAME = "android.provider.Telephony.SMS_RECEIVED";
	private String SMSmessage;
	private String SMSsender;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle content;
		Object[] pdus;
		
		if ((intent != null) 
				&& (intent.getAction() != null)
				&& (intent.getAction().compareToIgnoreCase(ACTION_NAME) == 0)) {
			content = intent.getExtras();
			if (content != null) {
				SMSmessage = "";
				SMSsender = "";
				pdus = (Object[]) content.get("pdus");
				SmsMessage[] partSMS = new SmsMessage[pdus.length]; 
				
				for (int i = 0; i < partSMS.length; i++){
					partSMS[i] = SmsMessage.createFromPdu((byte[])pdus[i]);                
					if (i == 0) { 
						SMSsender += "От: " + partSMS[i].getOriginatingAddress() 
								+ System.getProperty("line.separator");
					}
					SMSmessage += partSMS[i].getMessageBody().toString();
	            }
				SMSmessage = "Текст: " + SMSmessage + System.getProperty("line.separator");
			}
   
		    if (SMShandler.isActivityVisible()) {
		    	SMShandler.getTextView().setText("Было получено сообщение" 
		    			+ System.getProperty("line.separator") 
		    			+ SMSsender + SMSmessage);
		    	SMShandler.getTextView().setVisibility(View.VISIBLE);
		    } else {
			    Intent processorIntent = new Intent(context, SMSprocessor.class);
			    processorIntent.putExtra("sms_message", SMSmessage);
			    processorIntent.putExtra("sms_sender", SMSsender);
			    context.startService(processorIntent);
		    }
		    
		    abortBroadcast();
		}

	}
	

}
