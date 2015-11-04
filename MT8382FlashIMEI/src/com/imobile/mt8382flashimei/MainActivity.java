package com.imobile.mt8382flashimei;

import java.io.File;
import java.io.FileOutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.imobile.mt8382flashimei.externalcode.TelephonyInfo;

public class MainActivity extends Activity {

	int not_exit_APP = 0;
	int exit_APP = 1;
	String identifier = null;
	TextView Po_show_imei_number, Po_show_imei;
	EditText Po_et_show_imei_number;
	String Po_IMEI;
	private String TAG = "Po_dbg";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// CheckSIM();
		show_imei();
		Po_findViewById();
	}

	public void CheckSIM() {
		TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(this);

		String imsiSIM1 = telephonyInfo.getImsiSIM1();
		String imsiSIM2 = telephonyInfo.getImsiSIM2();

		boolean isSIM1Ready = telephonyInfo.isSIM1Ready();
		boolean isSIM2Ready = telephonyInfo.isSIM2Ready();

		boolean isDualSIM = telephonyInfo.isDualSIM();

		Log.i(TAG, " IME1 : " + imsiSIM1 + "\n" + " IME2 : " + imsiSIM2 + "\n"
				+ " IS DUAL SIM : " + isDualSIM + "\n" + " IS SIM1 READY : "
				+ isSIM1Ready + "\n" + " IS SIM2 READY : " + isSIM2Ready + "\n");
	}

	private void FlashIMEI1(String imei1) {
		String sdcard_path = Environment.getExternalStorageDirectory()
				.toString();
		File imei_script = new File(sdcard_path, "imei.sh");
		String msg = "echo SIM1 " + imei1 + "+ >  /proc/MTKIMEI\n"+
		/** fix factory imie bug**/
		"rm /system/local_script/first_imei.sh\n" +
		"echo \"sleep 5\" >> /system/local_script/first_imei.sh\n"+
		"echo \"echo SIM1 " + imei1 + "+ >  /proc/MTKIMEI\" >> /system/local_script/first_imei.sh\n";
		try {
			FileOutputStream out = new FileOutputStream(imei_script);
			out.write(msg.getBytes());
		} catch (Exception e) {
			// TODO: handle exception
			Toast("write FlashIMEI1 error");
		}

	}

	public void Po_Btn_flash_imei_number(View view) {

		this.Po_IMEI = Po_et_show_imei_number.getText().toString();
		if (this.Po_IMEI.length() == 15) {
			// flash code
			FlashIMEI1(this.Po_IMEI);

			Intent intent = new Intent(this, process_dialog.class);
			Bundle bundle = new Bundle();
			intent.putExtras(bundle);
			this.startActivityForResult(intent, 1);

		} else {

			show_dialog("Format Error", "Please enter 15 characters",
					not_exit_APP);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			Bundle bundleResult = data.getExtras();
			Log.d(TAG, "RESULT_OK");
			show_dialog("Successfully", "Please reboot device", exit_APP);
		}
	}

	/**
	 * onDestroy()
	 */
	@Override
	protected void onDestroy() {
		int pid = android.os.Process.myPid();
		android.os.Process.killProcess(pid);
		super.onDestroy();
	}

	public void show_dialog(String Title, String Msg, int exit_status) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle(Title);
		dialog.setMessage(Msg);
		dialog.setIcon(android.R.drawable.ic_dialog_alert);
		dialog.setCancelable(false);
		if (exit_status == not_exit_APP) {
			dialog.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							Toast("Format Error");
						}
					});

		} else {// exit_status =exit_APP
			dialog.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// 按下PositiveButton要做的事
							Toast("APP is exit");
							onDestroy();
						}
					});
		}

		dialog.show();

	}

	/**
	 * 
	 * @param call
	 *            Toast
	 */
	public void Toast(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}

	private void Po_findViewById() {

		Po_show_imei = (TextView) findViewById(R.id.Po_show_imei);
		Po_show_imei_number = (TextView) findViewById(R.id.Po_show_imei_number);

		int text_size = 30;
		Po_show_imei.setTextSize(text_size);
		Po_show_imei_number.setTextSize(text_size);

		Po_show_imei.setText("Your IMEI :");
		if (this.identifier.toString().equals("Invalid")) {
			Po_show_imei_number.setTextColor(android.graphics.Color.RED);
		}
		Po_show_imei_number.setText(this.identifier.toString());

		TextView Po_show_input_imei;

		Po_show_input_imei = (TextView) findViewById(R.id.Po_show_input_imei);
		Po_show_input_imei.setTextSize(text_size);
		Po_show_input_imei.setText("Input you imei :");

		Po_et_show_imei_number = (EditText) findViewById(R.id.Po_et_show_imei_number);

	}

	private void show_imei() {
		TelephonyManager tm = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);
		if (tm != null) {
			identifier = tm.getDeviceId();
		}
		if (identifier == null || identifier.length() == 0) {
			identifier = Secure.getString(this.getContentResolver(),
					Secure.ANDROID_ID);
			identifier = "Invalid";

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
