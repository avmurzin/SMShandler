package com.avmurzin.smshandler.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * SMS database helper.
 * @author Andrei V. Murzin
 *
 */
public class SMSDatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "smstable.db";
	private static final int DATABASE_VERSION = 1;
	
	  public SMSDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		SMSTable.onCreate(db);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		SMSTable.onUpgrade(db, oldVersion, newVersion);
	}

}
