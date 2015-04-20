package com.imobile.iq8settingethernetip;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

	private final String TAG = "Po_ETH";
	private int Default_config = 9;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		try {
			// Path ==> /mnt/shell/emulated/0/
			FileWriter fw = new FileWriter("/sdcard/IQ8_EthernetIP.sh", false);
			BufferedWriter bw = new BufferedWriter(fw); // 將BufferedWeiter與FileWrite物件做連結
			bw.write("#!/system/bin/sh\n\n");
			bw.write("echo 192.168.1.210\n");
			bw.write("busybox ifconfig eth0 192.168.1.210");
			bw.newLine();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onDestroy() {
		Log.d(TAG, "exit APP");
		int pid = android.os.Process.myPid();
		android.os.Process.killProcess(pid);
		super.onDestroy();
	}

	/***
	 * 顯示diglog
	 * 
	 * @param Title
	 * @param Msg
	 * @param event
	 */
	public void show_dialog(String Title, String Msg, int Po_event) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle(Title);
		dialog.setMessage(Msg);
		dialog.setIcon(android.R.drawable.ic_dialog_alert);
		dialog.setCancelable(false);
		
		if(Po_event == this.Default_config){
			dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					
					onDestroy();
				}
			});
		}else{
			dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					
					// 按下PositiveButton要做的事
					Toast.makeText(MainActivity.this, "APP is exit",
							Toast.LENGTH_SHORT).show();
					onDestroy();
				}
			});
		}
		dialog.show();

	}
	
	/***
	 * Po_set_default_config is a button, can delete /sdcard/IQ8_EthernetIP.sh
	 * @param view
	 */
	public void Po_set_default_config(View view) {
		Log.d(TAG, "Po_set_default_config()");
		File delete_file = new File("/sdcard/IQ8_EthernetIP.sh");
		// delete file
		delete_file.delete();
		show_dialog("Restore config", "success", this.Default_config);
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
