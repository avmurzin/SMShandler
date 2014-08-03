package com.avmurzin.smshandler.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
/**
 * Table for SMS message storing - text and time stamp.
 * @author Andrei V. Murzin
 *
 */
public class SMSTable {
	// Database table
	public static final String TABLE_SMS = "sms";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_SENDER = "smssender"; //type text
	public static final String COLUMN_TEXT = "smstext"; //type text
	public static final String COLUMN_TYPE = "smstype"; //type integer: IN/OUT
	public static final String COLUMN_TIME = "smstime"; //type integer (epoch time)
	public static final int IN = 0;
	public static final int OUT = 1;
	
	// Database creation SQL statement
	private static final String DATABASE_CREATE = "create table " 
			+ TABLE_SMS
			+ "(" 
			+ COLUMN_ID + " integer primary key autoincrement, " 
			+ COLUMN_SENDER + " text not null, " 
			+ COLUMN_TEXT + " text not null, " 
			+ COLUMN_TYPE + " integer not null, "
			+ COLUMN_TIME + " integer not null" 
			+ ");";

	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		Log.w(SMSTable.class.getName(), "Upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_SMS);
		onCreate(database);
	}	  
}
