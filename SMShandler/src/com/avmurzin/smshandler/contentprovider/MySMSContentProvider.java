package com.avmurzin.smshandler.contentprovider;

import com.avmurzin.smshandler.database.SMSDatabaseHelper;
import com.avmurzin.smshandler.database.SMSTable;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class MySMSContentProvider extends ContentProvider {
	
	// database
	private SMSDatabaseHelper database;

	// used for the UriMacher
	private static final int SMS_ALL = 10;
	private static final int SMS_ID = 20;
	
	private static final String AUTHORITY = "com.avmurzin.smshandler.contentprovider";
	private static final String BASE_PATH = "sms";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
	
	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	  static {
	    sURIMatcher.addURI(AUTHORITY, BASE_PATH, SMS_ALL);
	    sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", SMS_ID);
	  }

	  @Override
	  public boolean onCreate() {
		  database = new SMSDatabaseHelper(getContext());
		  return false;
	  }

	  @Override
	  synchronized public Cursor query(Uri uri, String[] projection, String selection,
		      String[] selectionArgs, String sortOrder) {
		 
		  
		  SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		  queryBuilder.setTables(SMSTable.TABLE_SMS);
		  
		  int uriType = sURIMatcher.match(uri);
		  switch (uriType) {
		  case SMS_ALL:
			  break;
		  case SMS_ID:
			  queryBuilder.appendWhere(SMSTable.COLUMN_ID + "="
					  + uri.getLastPathSegment());
			  break;
		  default:
			  throw new IllegalArgumentException("Unknown URI: " + uri);
		  }

		  SQLiteDatabase db = database.getWritableDatabase();
		  Cursor cursor = queryBuilder.query(db, projection, selection,
				  selectionArgs, null, null, sortOrder);

		  cursor.setNotificationUri(getContext().getContentResolver(), uri);

		  return cursor;
	  }
		
		
	  @Override
	  synchronized public int delete(Uri uri, String selection, String[] selectionArgs) {
		  int uriType = sURIMatcher.match(uri);
		  SQLiteDatabase sqlDB = database.getWritableDatabase();
		  int rowsDeleted = 0;
		  switch (uriType) {
		  case SMS_ALL:
			  rowsDeleted = sqlDB.delete(SMSTable.TABLE_SMS, selection,
					  selectionArgs);
			  break;
		  case SMS_ID:
			  String id = uri.getLastPathSegment();
			  if (TextUtils.isEmpty(selection)) {
				  rowsDeleted = sqlDB.delete(SMSTable.TABLE_SMS,
						  SMSTable.COLUMN_ID + "=" + id, 
						  null);
			  } else {
				  rowsDeleted = sqlDB.delete(SMSTable.TABLE_SMS,
						  SMSTable.COLUMN_ID + "=" + id 
						  + " and " + selection,
						  selectionArgs);
			  }
			  break;
		  default:
			  throw new IllegalArgumentException("Unknown URI: " + uri);
		  }
		  getContext().getContentResolver().notifyChange(uri, null);
		  return rowsDeleted;
	  }

	  @Override
	  synchronized public String getType(Uri uri) {
		  return null;
	  }

	@Override
	synchronized public Uri insert(Uri uri, ContentValues values) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		long id = 0;
		switch (uriType) {
		case SMS_ALL:
			id = sqlDB.insert(SMSTable.TABLE_SMS, null, values);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return Uri.parse(BASE_PATH + "/" + id);
	}


	@Override
	synchronized public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		int rowsUpdated = 0;
		switch (uriType) {
		case SMS_ALL:
			rowsUpdated = sqlDB.update(SMSTable.TABLE_SMS, 
					values, 
					selection,
					selectionArgs);
			break;
		case SMS_ID:
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsUpdated = sqlDB.update(SMSTable.TABLE_SMS, 
						values,
						SMSTable.COLUMN_ID + "=" + id, 
						null);
			} else {
				rowsUpdated = sqlDB.update(SMSTable.TABLE_SMS, 
						values,
						SMSTable.COLUMN_ID + "=" + id 
						+ " and " 
						+ selection,
						selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsUpdated;
	}

}
