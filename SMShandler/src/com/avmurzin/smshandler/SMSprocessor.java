package com.avmurzin.smshandler;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
/**
 * Сервис для обработки СМС-сообщения. Используется, если окно приложения не активно.
 * В данной версии запускается в основном потоке и формирует уведомление о 
 * поступившем сообщении.
 * @author Andrei V. Murzin
 *
 */
public class SMSprocessor extends Service {

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
	    makeNotification(intent.getExtras().getString("sms_sender"), 
	    		intent.getExtras().getString("sms_message"));
	    return START_STICKY;
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	private void makeNotification(String sms_sender, String sms_message) {
		Intent mainActivityIntent = new Intent(this, MainActivity.class);
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
	
}
