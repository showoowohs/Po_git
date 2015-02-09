package com.ooieueioo.pojnireadwriteproc;


import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PoJNIReadWriteProc extends Activity {

	private LinearLayout Po_LLay_read = null;
	private EditText Po_Read_ed = null;
	private TextView Po_Read_tv = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_po_jniread_write_proc);
		
		find_id();
		call_JNI_Read_function();
	}
	
	public void Po_Read_btn (View view){
		this.Po_Read_tv.setText("111");
	}
	
	private void find_id() {
		//Po_LLay_read = (LinearLayout) findViewById(R.id.Po_LLay_read);
		this.Po_Read_ed = (EditText) findViewById(R.id.Po_Read_ED1);
		this.Po_Read_tv = (TextView) findViewById(R.id.Po_Read_TV1);
		
	}
	
	private void call_JNI_Read_function() {
		
		String Read_Path = this.Po_Read_ed.getText().toString();
		this.Po_Read_tv.setText(Read_Path);
//		String Read_Path = "/proc/Po_value";
//		TextView tv = new TextView(this);
//		tv.setText("Read Path = "+Read_Path);
//		tv.setTextSize(24);
//		Po_LLay_read.addView(tv);
		
		// call ReadProc()
//		String str = PoJNITtest.HelloWorld();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.po_jniread_write_proc, menu);
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
