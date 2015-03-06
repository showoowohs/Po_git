package com.imobile.iq8_fixwifi;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {
	final String TAG = "Po_debug";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// 1. copy file to /sdcard
		CopyAssets();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "onDestroy()");
		
		// call reboot
		Run_su("/system/bin/reboot");
		// Kill myself
		// android.os.Process.killProcess(android.os.Process.myPid());
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
		dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// 按下PositiveButton要做的事
				Toast.makeText(MainActivity.this, "APP is exit",
						Toast.LENGTH_SHORT).show();
				onDestroy();
			}
		});

		dialog.show();

	}

	/**
	 * check_copy_dialog
	 * 
	 * @param Title
	 * @param Msg
	 * @param config_num
	 */
	public void check_copy_dialog(String Title, String Msg, int config_num) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle(Title);
		dialog.setMessage(Msg);
		dialog.setIcon(android.R.drawable.ic_dialog_alert);
		dialog.setCancelable(false);
		if (config_num == 1) {
			// user select config1
			Log.i(TAG, "config_num = 1");

			dialog.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// 按下PositiveButton要做的事

							// chang ini to WCNSS_qcom_cfg_Jason1.ini
							Run_su("busybox rm /data/misc/wifi/WCNSS_qcom_cfg.ini; busybox mv /mnt/shell/emulated/0/WCNSS_qcom_cfg_Jason1.ini /data/misc/wifi/WCNSS_qcom_cfg.ini; busybox chmod 777 /data/misc/wifi/WCNSS_qcom_cfg.ini");
							show_dialog("WiFi config1 updata success", "system will reboot!!");
						}
					});
		} else {
			// user select config2
			Log.i(TAG, "config_num = 2");
			dialog.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// 按下PositiveButton要做的事

							// chang ini to WCNSS_qcom_cfg_org.ini
							Run_su("busybox rm /data/misc/wifi/WCNSS_qcom_cfg.ini; busybox mv /mnt/shell/emulated/0/WCNSS_qcom_cfg_org.ini /data/misc/wifi/WCNSS_qcom_cfg.ini; busybox chmod 777 /data/misc/wifi/WCNSS_qcom_cfg.ini");
							show_dialog("WiFi config2 updata success", "system will reboot!!");
						}
					});
		}

		dialog.show();

	}

	public void Toast(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}

	public void Run_su(String str) {
		Runtime ex = Runtime.getRuntime();
		String cmdBecomeSu = "su";
		String script = str;
		Log.e("Debug", "Run_su()");
		try {
			java.lang.Process runsum = ex.exec(cmdBecomeSu);
			int exitVal = 0;
			final OutputStreamWriter out = new OutputStreamWriter(
					runsum.getOutputStream());
			// Write the script to be executed
			out.write(script);
			// Ensure that the last character is an "enter"
			out.write("\n");
			out.flush();
			// Terminate the "su" process
			out.write("exit\n");
			out.flush();
			exitVal = runsum.waitFor();
			if (exitVal == 0) {
				Log.e("Debug", "Successfully to su");
				Toast("Successfully to su");
			}
		} catch (Exception e) {
			Log.e("Debug", "Fails to su");
			Toast("Fails to su");
		}
	}

	/***
	 * Po_Config1
	 * 
	 * @param view
	 *            button1
	 */
	public void Po_Config1(View view) {
		Log.i(TAG, "Po_Config1()");
		check_copy_dialog(
				"Are you sure you want to replace wifi config??",
				"Will to replace wifi config to Config1!\nif config updata is succuss!! want to reboot system!",
				1);
	}

	/***
	 * Po_Config2
	 * 
	 * @param view
	 *            button2
	 */
	public void Po_Config2(View view) {
		Log.i(TAG, "Po_Config2()");
		check_copy_dialog(
				"Are you sure you want to replace wifi config??",
				"Will to replace wifi config to Config2!\nif config updata is succuss!! want to reboot system!",
				2);
	}

	private void CopyAssets() {
		AssetManager assetManager = getAssets();
		String[] files = null;
		try {
			files = assetManager.list("Files");
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		}

		for (String filename : files) {
			Log.i(TAG, "File name => " + filename);
			InputStream in = null;
			OutputStream out = null;
			try {
				in = assetManager.open("Files/" + filename); // if files resides
																// inside the
																// "Files"
																// directory
																// itself
				out = new FileOutputStream(Environment
						.getExternalStorageDirectory().toString()
						+ "/"
						+ filename);
				copyFile(in, out);
				in.close();
				in = null;
				out.flush();
				out.close();
				out = null;
			} catch (Exception e) {
				Log.i(TAG, e.getMessage());
			}
		}
	}

	private void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
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
