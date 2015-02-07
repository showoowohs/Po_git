package com.ooieuioo.potestjni;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends Activity {

	static {
		System.loadLibrary("PoTestJNI");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		call_JNI_function();
	}
	
	private void call_JNI_function(){
		//call HelloWorld()
//		String str = PoJNITtest.HelloWorld();  
//        TextView tv = new TextView(this);  
//        tv.setText(str);  
//        setContentView(tv);
        
        //call 
        String str2 = PoJNITtest.TransportStringToC("Java_Send");  
        TextView tv2 = new TextView(this);  
        tv2.setText(str2);  
        setContentView(tv2);
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
