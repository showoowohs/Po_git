package com.imobile.mt8382readkeyevent;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

	final String TAG = "Po_ReadKey";
	final int F13 = 183; // 0x00b7
	final int F14 = 184; // 0x00b8
	final int F15 = 185; // 0x00b9
	final int F16 = 186; // 0x00ba

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.d(TAG, "start key");
	}

	public String Po_split_keycode(String keyevent) {
		Log.d(TAG, "Po_split_keycode start");
		// Log.d(TAG, keyevent);
		String last_code;
		// 1. splite ,
		String[] AfterSplit = keyevent.split(",");
		// for (int i = 0; i < AfterSplit.length; i++) {
		// Log.d(TAG, "AfterSplit[" + i + "]=" + AfterSplit[i]);
		// }
		if (AfterSplit.length == 10) {
			// Log.d(TAG, "AfterSplit[2]=" + AfterSplit[2]);
			// 2. split kernel keycode
			last_code = AfterSplit[2];
			AfterSplit = last_code.split("=");
			if (AfterSplit.length == 2) {
				// Log.d(TAG, "AfterSplit[1]=" + AfterSplit[1]);
				last_code = AfterSplit[1];
			} else {
				return "ERROR";
			}
		} else {
			return "ERROR";
		}

		return last_code;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.d(TAG, "start onKeyDown ok");
		// Po_split_keycode(event.toString());
		Log.d(TAG, "Po_split_keycode = " + Po_split_keycode(event.toString()));

		//*** call standard android API ***//
		// switch (keyCode) {
		// case KeyEvent.KEYCODE_F1:
		// // your Action code
		// Log.d(TAG, "F1");
		// return true;
		// default:
		// // Log.d(TAG, "keycode ->"+keyCode);
		// }
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
