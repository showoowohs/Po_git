package com.imobile.z;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void Po_close(View view) {
		Log.d("PO", "111");
		onDestroy();
		Log.d("PO", "222");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d("PO", "exit this APP");
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(1);
	}
}
