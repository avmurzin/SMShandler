package com.avmurzin.smshandler;

import com.avmurzin.smshandler.contentprovider.MySMSContentProvider;
import com.avmurzin.smshandler.database.SMSTable;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.view.ViewDebug.FlagToString;
/**
 * Сервис для обработки СМС-сообщения. Используется, если окно приложения не активно.
 * В данной версии запускается в основном потоке и формирует уведомление о 
 * поступившем сообщении.
 * @author Andrei V. Murzin
 *
 */
public class SMSprocessor extends IntentService {

	public SMSprocessor() {
		super("SMSprocessor");
	}

//	@Override
//	public int onStartCommand(Intent intent, int flags, int startId) {
//		
//		String smsSender = intent.getExtras().getString("sms_sender");
//		String smsMessage = intent.getExtras().getString("sms_message");
//		ContentValues values = new ContentValues();
//		
//	    makeNotification(smsSender, smsMessage);
//	   	    
//	    values.put(SMSTable.COLUMN_SENDER, smsSender);
//	    values.put(SMSTable.COLUMN_TEXT, smsMessage);
//	    values.put(SMSTable.COLUMN_TYPE, SMSTable.IN);
//	    values.put(SMSTable.COLUMN_TIME, System.currentTimeMillis() / 1000);
//	    getContentResolver().insert(MySMSContentProvider.CONTENT_URI, values);
//	    
//	    return START_STICKY;
//	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	private void makeNotification(String sms_sender, String sms_message) {
		Intent mainActivityIntent = new Intent(this, SMSListActivity.class);
		mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		mainActivityIntent.putExtra("sms_sender", sms_sender);
		mainActivityIntent.putExtra("sms_message", sms_message);
	    PendingIntent contentIntent = PendingIntent.getActivity(this, 0, mainActivityIntent, 0);
	    Context context = getApplicationContext();
	    Notification.Builder builder = new Notification.Builder(context)
	        .setContentTitle("Поступило сообщение от: ")
	        .setContentText(sms_sender)
	        .setContentIntent(contentIntent)
	        .setSmallIcon(R.drawable.ic_launcher)
	        .setAutoCancel(true);
	    NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = builder.build();
	    notificationManager.notify(R.drawable.ic_launcher, notification);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		String smsSender = intent.getExtras().getString("sms_sender");
		String smsMessage = intent.getExtras().getString("sms_message");
		ContentValues values = new ContentValues();

		makeNotification(smsSender, smsMessage);

		values.put(SMSTable.COLUMN_SENDER, smsSender);
		values.put(SMSTable.COLUMN_TEXT, smsMessage);
		values.put(SMSTable.COLUMN_TYPE, SMSTable.IN);
		values.put(SMSTable.COLUMN_TIME, System.currentTimeMillis() / 1000);
		getContentResolver().insert(MySMSContentProvider.CONTENT_URI, values);
		
	}
	
}
