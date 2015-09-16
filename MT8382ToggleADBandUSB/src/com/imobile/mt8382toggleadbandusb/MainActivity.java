package com.imobile.mt8382toggleadbandusb;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class MainActivity extends Activity {
	private TextView POTV1, POTV2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		PO_findID();
	}
	
	private void PO_findID(){
		this.POTV1 = (TextView)findViewById(R.id.POTV1);
		this.POTV1.setTextSize(26);
		
		this.POTV2 = (TextView)findViewById(R.id.POTV2);
		this.POTV2.setTextSize(26);
	}
}
