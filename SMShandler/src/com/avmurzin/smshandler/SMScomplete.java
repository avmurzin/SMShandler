package com.avmurzin.smshandler;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.widget.Toast;


public class SMScomplete extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String message = "";
		  switch(getResultCode()) {
		  case Activity.RESULT_OK:
		   message = "Сообщение отправлено";
		   break;
		  case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
			  message = "Что-то пошло не так";
		   break;
		  case SmsManager.RESULT_ERROR_RADIO_OFF:
			  message = "Нет связи";
		   break;
		  case SmsManager.RESULT_ERROR_NULL_PDU:
			  message = "Что-то пошло не так";
		   break;
		  }
		  
		  Toast.makeText(context, 
					message, 
                  Toast.LENGTH_SHORT).show();

	}

}
