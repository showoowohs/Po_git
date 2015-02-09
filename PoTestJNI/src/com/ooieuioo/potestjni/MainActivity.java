package com.ooieuioo.potestjni;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	LinearLayout Po_LLay1 = null;
	static {
		System.loadLibrary("PoTestJNI");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		find_id();
		call_JNI_function();
		
	}
	
	private void find_id(){
		Po_LLay1 = (LinearLayout) findViewById(R.id.Po_LLay1);
	}
	
	private void call_JNI_function(){
		//call HelloWorld()
		String str = PoJNITtest.HelloWorld();  
        TextView tv = new TextView(this);  
        tv.setText(str);
        tv.setTextSize(24);
        Po_LLay1.addView(tv);
        
        //call TransportStringToC()
        String str2 = PoJNITtest.TransportStringToC("Java_Send");  
        TextView tv2 = new TextView(this);  
        tv2.setText(str2);  
        tv2.setTextSize(24);
        Po_LLay1.addView(tv2);
        
        //call TransportIntToC()
        int Po_Java_int = PoJNITtest.TransportIntToC(20);  
        TextView tv3 = new TextView(this);  
        tv3.setText("JNI return number = " + Po_Java_int);  
        tv3.setTextSize(24);
        Po_LLay1.addView(tv3);
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
