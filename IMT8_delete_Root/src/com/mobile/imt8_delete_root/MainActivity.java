package com.mobile.imt8_delete_root;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViewId();
		show_dialog("Are you sure want to delete Root??", "Warning! will lose root!");
	}
	
	@Override
	protected void onDestroy() {
		int pid = android.os.Process.myPid();
		android.os.Process.killProcess(pid);
		super.onDestroy();
	}
	
	/***
	 * 顯示diglog
	 * 
	 * @param Title
	 * @param Msg
	 */
	public void show_dialog(String Title, String Msg) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle(Title);
		dialog.setMessage(Msg);
		dialog.setIcon(android.R.drawable.ic_dialog_alert);
		dialog.setCancelable(false);
		dialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// 按下PositiveButton要做的事
//				Run_su("busybox rm /data/recovery_IQ8.sh; busybox mv /sdcard/recovery_IQ8.sh /data/recovery_IQ8.sh; busybox chmod 777 /data/recovery_IQ8.sh ; ./data/recovery_IQ8.sh");
				// Move_File("/sdcard/command", "/system/command");

				Toast.makeText(MainActivity.this, "APP will exit",
						Toast.LENGTH_SHORT).show();
				onDestroy();
			}
		});
		
		dialog.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// 按下NegativeButton要做的事
				String Title = "Please shutdown!";
				String Msg = "Recovery setting is OK\nPlease turn off the machine and then into recovery mode";
				show_dialog(Title, Msg);
			}
		});

		dialog.show();
	}

	public void findViewId(){
		
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
