package com.imobile.mt8382toggleadbandusb;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity {
	private TextView POTV1, POTV2;

	private final String TAG = "Po_dbg";

	static {
		System.loadLibrary("imobileJNI");
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		PO_findID();
		ReadUSBStatus();
	}
	
	private void PO_findID(){
		this.POTV1 = (TextView)findViewById(R.id.POTV1);
		this.POTV1.setTextSize(26);
		
		this.POTV2 = (TextView)findViewById(R.id.POTV2);
		this.POTV2.setTextSize(26);
	}
	
	private String ReadUSBStatus(){
		String status ="";
		try {
			status = imobileJNI.ReadProc("/proc/USBStatus");
		} catch (Exception e) {
			// TODO: handle exception
			return "Read USBStatus error!";
		}
		Log.d(TAG, "status="+ status.toString());
		
		return status;
	}
}
