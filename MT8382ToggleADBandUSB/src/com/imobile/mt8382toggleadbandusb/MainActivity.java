package com.imobile.mt8382toggleadbandusb;

import android.R.color;
import android.app.Activity;
import android.graphics.Color;
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
		init();
	}

	private void PO_findID() {
		this.POTV1 = (TextView) findViewById(R.id.POTV1);
		this.POTV2 = (TextView) findViewById(R.id.POTV2);
		
	}

	/**
	 * can read USB status(OTG/USB), through cat /proc/USBStatus
	 * @return USB current status 
	 */
	private String ReadUSBStatus(){
		String status ="";
		String ReturnUI ="";
		try {
			status = imobileJNI.ReadProc("/proc/USBStatus");
			Log.d(TAG, "status="+ status.toString());
			
			if(status.equals("USB") || status.equals("OTG")){
				ReturnUI = status + " Model is currently";
			}else{
				ReturnUI = "Read USBStatus error!";
				return ReturnUI;
			}
		} catch (Exception e) {
			// TODO: handle exception
			ReturnUI = "Read USBStatus error!";
			return ReturnUI;
		}
		
		return ReturnUI;
	}
	
	private void init(){
		// setting text size
		this.POTV1.setTextSize(36);
		this.POTV1.setTextColor(Color.BLACK);
		this.POTV2.setTextSize(30);
		this.POTV2.setTextColor(Color.BLACK);
		
		
		// show usb mode(USB/OTG)
		String SetUIText = ReadUSBStatus();
		this.POTV1.setText(SetUIText);
		this.POTV2.setText("OTG mode");
		
		// check status
//		Log.d(TAG, "status = "+SetUIText.substring(0, 3));
//		if(SetUIText.substring(0, 3).equals("USB")){
//			this.POTV1.setText("USB");
//		}else if(SetUIText.substring(0, 3).equals("OTG")){
//			
//		}
		
	}
}
