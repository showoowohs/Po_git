package com.ooieueioo.ctrl_power_widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.RemoteViews;

public class MyWidgetProvider extends AppWidgetProvider {

	private static final String CLICK_ACTION = "myCustomAction";
	private static final String Po_btn1_wifi = "XX";
	private static boolean wifi_status = false;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);

		String action = intent.getAction();

		System.out.println("onReveice action: " + action);

		if (CLICK_ACTION.equals(action)) {
			System.out.println("clicked");
			// Toast.makeText(context, Read_Carrier(),
			// Toast.LENGTH_LONG).show();

		}
		
		if (Po_btn1_wifi.equals(intent.getAction())) {
//            onUpdate(context);
			this.wifi_status = !this.wifi_status;
			Log.d("Po", "Po_btn1_wifi="+this.wifi_status);
			toggleWiFi(this.wifi_status, context);
        }

	}
	public void toggleWiFi(boolean status , Context context) {
		WifiManager wifiManager = (WifiManager) 
				context.getSystemService(Context.WIFI_SERVICE);
		if (status == true && !wifiManager.isWifiEnabled()) {
			wifiManager.setWifiEnabled(true);
		} else if (status == false && wifiManager.isWifiEnabled()) {
			wifiManager.setWifiEnabled(false);
		}
		Log.d("Po", "toggleWiFi() Po_btn1_wifi="+this.wifi_status);
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

			appWidgetManager.updateAppWidget(widgetId, remoteViews);
		}
		// �Ұ�thread
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
