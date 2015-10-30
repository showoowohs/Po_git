package com.imobile.mt8382flashimei;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;

public class process_dialog extends Activity {
	ProgressBar myProgressBar;
	int myProgress = 0;
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.process_dialog);

		intent = this.getIntent();
		Bundle bundle = intent.getExtras();

		this.setTitle("Please wait!");
		myProgressBar = (ProgressBar) findViewById(R.id.progressbar);
		myProgressBar.setProgress(myProgress);

		new Thread(new Runnable() {
			@Override
			public void run() {
				while (myProgress < 100) {
					try {
						myHandle.sendMessage(myHandle.obtainMessage());
						Thread.sleep(1000);
					} catch (Throwable t) {
					}
				}
				setResult(RESULT_OK, intent);
				finish();
			}
		}).start();

	}

	Handler myHandle = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			myProgress += 20;
			myProgressBar.setProgress(myProgress);
		}
	};
}