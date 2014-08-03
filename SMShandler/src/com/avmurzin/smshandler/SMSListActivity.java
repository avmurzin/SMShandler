package com.avmurzin.smshandler;

import com.avmurzin.smshandler.contentprovider.MySMSContentProvider;
import com.avmurzin.smshandler.database.SMSTable;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * Display SMS messages list. Get data from Content Provider (MySMSContentProvider).
 * @author Andrei V. Murzin
 *
 */
public class SMSListActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {
	
	public static final String SETTINGS_NAME = "settings";
	public static final String SETTINGS_ABORT = "abort";
	
	private SimpleCursorAdapter adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sms_list);
		this.getListView().setDividerHeight(0);
		fillData();
		//registerForContextMenu(getListView());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.smsmenu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.clear:
			clearSMSList();
			return true;
		case R.id.exit:
			exitApp();
			return true;
		case R.id.new_sms:
			newSMSMessage();
			return true;
		case R.id.settings:
			doSettings();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		String[] projection = { SMSTable.COLUMN_ID, SMSTable.COLUMN_SENDER, SMSTable.COLUMN_TEXT, SMSTable.COLUMN_TYPE};
		CursorLoader cursorLoader = new CursorLoader(this, MySMSContentProvider.CONTENT_URI, projection, null, null, SMSTable.COLUMN_TIME);
		return cursorLoader;
	}

	// Opens the second activity if an entry is clicked
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		newSMSMessage();
		//Intent intent = new Intent(this, SendSMSActivity.class);
		//Uri smsUri = Uri.parse(MySMSContentProvider.CONTENT_URI + "/" + id);
		//i.putExtra(MySMSContentProvider.CONTENT_ITEM_TYPE, smsUri);

		//startActivity(intent);
	}	
	
	protected void newSMSMessage() {
		Intent intent = new Intent(this, SendSMSActivity.class);
		startActivity(intent);
	}
	
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		adapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		adapter.swapCursor(null);
	}

	private void fillData() {

		// Fields from the database (projection)
		// Must include the _id column for the adapter to work
		//String[] fields = new String[] { SMSTable.COLUMN_SENDER, SMSTable.COLUMN_TEXT, SMSTable.COLUMN_TYPE};
		String[] fields = new String[] { SMSTable.COLUMN_SENDER, SMSTable.COLUMN_TEXT};
		// Fields on the UI to which we map
		int[] row = new int[] { R.id.sms_sender, R.id.sms_message};

		getLoaderManager().initLoader(0, null, this);
		adapter = new SimpleCursorAdapter(this, R.layout.sms_row, null, fields, row, 0);

		setListAdapter(adapter);
	}
	
	//Clear SQL database by Content Provider
	private void clearSMSList() {
		getContentResolver().delete(MySMSContentProvider.CONTENT_URI, null, null);

	}
	
	private void exitApp() {
		finish();
	}
	
	private void doSettings() {
		Intent intent = new Intent(this, SMSSetting.class);
		startActivity(intent);
	}
}
