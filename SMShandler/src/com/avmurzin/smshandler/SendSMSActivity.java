package com.avmurzin.smshandler;

import java.util.ArrayList;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SendSMSActivity extends Activity implements OnClickListener {
	private Button bt_send, bt_clear;
	private EditText phone, text;
	private TextView smsText;
	private static final String SENT = "SENT_ACTION";
	private SMScomplete complete;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		smsText = (TextView) findViewById(R.id.smsText);
		bt_send = (Button) findViewById(R.id.bt_send);
		bt_clear = (Button) findViewById(R.id.bt_clear);
		phone = (EditText) findViewById(R.id.phone);
		text = (EditText) findViewById(R.id.text);
		
		bt_send.setOnClickListener(this);
		bt_clear.setOnClickListener(this);
		smsText.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_send: 
			sendSMS(phone.getText().toString(), text.getText().toString());
			finish();
			break;
		case R.id.bt_clear: 
			phone.setText("");
			text.setText("");
			break;
		case R.id.smsText: 
			smsText.setVisibility(View.GONE);
			break;
		}
		
	}

	private void sendSMS(String phone_string, String text_string) {

		if (phone_string.length() == 0 || text_string.length() == 0) {
			Toast.makeText(getBaseContext(), 
					"Введите номер абонента и текст сообщения", 
                    Toast.LENGTH_SHORT).show();
			return;
		}
		
		SmsManager smsManager = SmsManager.getDefault();
		
		ArrayList<String> arraySMS = smsManager.divideMessage(text_string);
		ArrayList<PendingIntent> arrayIntent = new ArrayList<PendingIntent>();
		
		for (String part : arraySMS) {
			arrayIntent.add(PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0));
		}
	
		smsManager.sendMultipartTextMessage(phone_string, null, arraySMS, arrayIntent, null); 
		Toast.makeText(getBaseContext(), 
				"Отправляем сообщение", 
                Toast.LENGTH_SHORT).show();
		phone.setText("");
		text.setText("");
	}
	
	@Override
	protected void onResume() {
	  super.onResume();
	  SMShandler.activityResumed(smsText);
	  complete = new SMScomplete();
	  registerReceiver(complete, new IntentFilter(SENT));
	}

	@Override
	protected void onPause() {
	  super.onPause();
	  SMShandler.activityPaused(smsText);
	  unregisterReceiver(complete);
	  complete = null;
	}


	
}
