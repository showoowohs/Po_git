package com.ooieueioo.ctrl_power_widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.RemoteViews;

public class MyWidgetProvider extends AppWidgetProvider {

	private static final String CLICK_ACTION = "myCustomAction";
	private static final String Po_btn1_wifi = "Po_WiFi";
	private static final String Po_btn2_BT = "Po_BT";
	private static boolean wifi_status = false;
	private static boolean BT_status = false;

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);

		String action = intent.getAction();

		System.out.println("onReveice action: " + action);
		Log.d("Po", "action=" + action);

		if (CLICK_ACTION.equals(action)) {
			System.out.println("clicked");
			// Toast.makeText(context, Read_Carrier(),
			// Toast.LENGTH_LONG).show();

		}

		if (Po_btn1_wifi.equals(action)) {
			toggle_WiFi(context);
		}

		// if (Po_btn1_wifi.equals(intent.getAction())) {
		// toggle_WiFi(context);
		// }

		if (Po_btn2_BT.equals(action)) {
			toggle_BT(context);
		}

	}

	/*****
	 * toggle_WiFi
	 * 
	 * @param context
	 *            on/off WiFi
	 */
	public void toggle_WiFi(Context context) {
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		if (!wifiManager.isWifiEnabled()) {
			wifiManager.setWifiEnabled(true);
			this.wifi_status = true;
		} else {
			wifiManager.setWifiEnabled(false);
			this.wifi_status = false;
		}
		Log.d("Po", "toggleWiFi() wifi_status=" + this.wifi_status);
		// System.out.println("toggleWiFi() wifi_status=" + this.wifi_status);
	}

	/***
	 * toggle_BT
	 * 
	 * @param context
	 *            on/off BuleTooth
	 */
	public void toggle_BT(Context context) {
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();
		if (!mBluetoothAdapter.isEnabled()) {
			mBluetoothAdapter.enable();
			this.BT_status = true;
		} else {
			mBluetoothAdapter.disable();
			this.BT_status = false;
		}
		Log.d("Po", "toggle_BT() BT_status=" + this.BT_status);
	}

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);

		System.out.println("onEnabled");

	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		Log.d("Po_add", "onUpdate");
		System.out.println("on-update widget");

		for (int widgetId : appWidgetIds) {
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
					R.layout.widget_layout);

			Intent intent = new Intent(context, MyWidgetProvider.class);

			intent.setAction(CLICK_ACTION);
			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

			PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
					0, intent, 0);

			remoteViews.setOnClickPendingIntent(R.id.WidgetLayout,
					pendingIntent);
			// Po add button
			remoteViews.setOnClickPendingIntent(R.id.Po_bt1,
					getPendingSelfIntent(context, Po_btn1_wifi));
			remoteViews.setOnClickPendingIntent(R.id.Po_bt2,
					getPendingSelfIntent(context, Po_btn2_BT));

			appWidgetManager.updateAppWidget(widgetId, remoteViews);
		}
		// ±Ò°Êthread
		// Thread thread = new Thread(new update_thread());
		// thread.start();
	}

	private PendingIntent getPendingSelfIntent(Context context, String action) {
		// An explicit intent directed at the current class (the "self").
		Intent intent = new Intent(context, getClass());
		intent.setAction(action);
		return PendingIntent.getBroadcast(context, 0, intent, 0);
	}

	public class update_thread implements Runnable {
		@Override
		public void run() {
			int i = 0;
			int Max_num = 61;
			while (true && i++ < Max_num) {

				try {
					Thread.sleep(1000);

					// if ((i == 2) || (i == 5) || (i==10) || (i == 20) || (i ==
					// 30)){
					// RemoteViews views = new RemoteViews(
					// Po_context.getPackageName(), R.layout.widget_layout);
					// views.setTextViewText(R.id.TextView01,
					// String.valueOf(Math.random()) );
					// Chang_Signal_Icon(Po_context, Po_Signal, Read_Carrier());
					// Po_app_manager.updateAppWidget(Po_appWidgetId, views);
					// }

					if (i == 60) {
						i = 0;
					}
					Log.d("Po_add", "i=" + i);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.d("Po_add", "InterruptedException");
				}

			}
		}
	}

}
