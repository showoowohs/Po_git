package com.imobile.iq8readserialnumber;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.imobile.iq8_readserialnumber.R;

public class MainActivity extends Activity {

	final String TAG = "Po_debug";
	static {
		System.loadLibrary("iMobileReadSerialNumber");
	}
	
	private String Read_SN() {
		
		String SN_number = "";
		File file = new File("/data/Po_prop.txt");

		// Read text from file
		StringBuilder text = new StringBuilder();

		try {

			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;

			while ((line = br.readLine()) != null) {
				
				text.append(line);
				//Log.i(TAG, "text=" + text);
			}
			br.close();
			
			SN_number = text.toString();
		} catch (IOException e) {
			Log.i(TAG, "e=" + e);
			// You'll need to add proper error handling here
		}

		// not data return "Not SN"
		if (SN_number == null || SN_number == "") {
			SN_number = "Not SN";
			return SN_number;
		}
		return SN_number;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		String str = iMobileReadSerialNumber.ReadSN("/data/Po_prop.txt");
		setTitle(""+str);
		//String SN= Read_SN();
		//setTitle(""+SN);
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
