package com.imobile.mt8382readkeyevent;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

	final String TAG = "Po_ReadKey";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.d(TAG, "start key");
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.d(TAG, "start onKeyDown ok");
		Log.d(TAG, "event"+ event.toString());
		switch (keyCode) {
			case KeyEvent.KEYCODE_F1: 
				// your Action code
				Log.d(TAG, "F1");
				return true;
			case KeyEvent.KEYCODE_F2: 
				// your Action code
				Log.d(TAG, "F2");
				return true;
			case KeyEvent.KEYCODE_F3: 
				// your Action code
				Log.d(TAG, "F3");
				return true;
			case KeyEvent.KEYCODE_F4: 
				// your Action code
				Log.d(TAG, "F4");
				return true;
			case KeyEvent.KEYCODE_HOME: 
				// your Action code
				Log.d(TAG, "Home");
				return true;				
			case KeyEvent.KEYCODE_BACK: 
				// your Action code
				Log.d(TAG, "Back");
				return true;
			case KeyEvent.KEYCODE_VOLUME_DOWN: 
				// your Action code
				Log.d(TAG, "volume down");
				return true;
			case KeyEvent.KEYCODE_VOLUME_UP: 
				// your Action code
				Log.d(TAG, "volume up");
				return true;
			case KeyEvent.KEYCODE_F9: 
				// your Action code
				Log.d(TAG, "F9");
				return true;
			case KeyEvent.KEYCODE_F10: 
				// your Action code
				Log.d(TAG, "F10");
				return true;
			case KeyEvent.KEYCODE_F11: 
				// your Action code
				Log.d(TAG, "F11");
				return true;
			case KeyEvent.KEYCODE_F12: 
				// your Action code
				Log.d(TAG, "F12");
				return true;
			default:
				Log.d(TAG, "keycode ->"+keyCode);
		}
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
