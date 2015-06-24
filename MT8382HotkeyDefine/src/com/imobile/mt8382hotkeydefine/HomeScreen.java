package com.imobile.mt8382hotkeydefine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class HomeScreen extends FragmentActivity {

	private final String TAG = "Po_debug";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homescreen);
	}

	public void restore_default(View view) {
//		Log.d(TAG, "click");
		CopyAssets();
		String FilePath = "/mnt/sdcard/Hotkey.ini";
		if(isFileExsist(FilePath)){
			Log.d(TAG, "have file");
			show_dialog("Restore Default Config Success!", "Please reboot devices");
		}else{
			Log.d(TAG, "not file");
		}

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
		dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// 按下PositiveButton要做的事
				Toast.makeText(HomeScreen.this, "APP is exit",
						Toast.LENGTH_SHORT).show();
				onDestroy();
			}
		});

		dialog.show();

	}
	
	/**
	 * isFileExsist() : can check file 
	 * @param filepath
	 * @return bool
	 */
	public Boolean isFileExsist(String filepath) {
	    
	        File file = new File(filepath);
	        return file.exists();
	    
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

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.main, menu);
	// return true;
	// }
}
