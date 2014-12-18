package com.imobile.widget_carriers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class MyWidgetProvider extends AppWidgetProvider {

	private static final String CLICK_ACTION = "myCustomAction";

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);

		String action = intent.getAction();

		System.out.println("onReveice action: " + action);

		if (CLICK_ACTION.equals(action)) {
			System.out.println("clicked");
			Toast.makeText(context, Read_Carrier(), Toast.LENGTH_LONG).show();
		}

	}

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);

		System.out.println("onEnabled");

	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

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

			// Po add
			remoteViews.setTextViewText(R.id.Po_TV, Read_Carrier());
			appWidgetManager.updateAppWidget(widgetId, remoteViews);
		}
	}

	private String Read_Carrier() {
		System.out.println(" Read_Carrier");
		String Carrier = "";
		try {
			// ���oSD�d�x�s���|

			File sdcard = Environment.getExternalStorageDirectory();

			// Get the text file
//			File file = new File(sdcard, "111.txt");
			File file = new File(sdcard, "ublox_SIM_info.txt");
			Log.d("Po_add", "file="+file);

			// Read text from file
			StringBuilder text = new StringBuilder();
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;

			while ((line = br.readLine()) != null) {
				text.append(line);
				text.append('\n');
			}
			// get �q�H�Ӹ��
			Carrier += Split_Carrier(text.toString());
			Log.d("Po_add", "Carrier="+Carrier);
			// System.out.println("Po...text=" + text);
		} catch (IOException e) {
			Log.d("Po_add", "IOException"+e);
			// You'll need to add proper error handling here
		}

		// �p�G���S���ƭȴN�^�� �S�q�H�A��
		if (Carrier == null || Carrier == "") {
			Carrier = "No Service";
			return Carrier;
		}
		return Carrier;
	}

	/****
	 * ��X�q�H�~�̸��� �æ^��
	 * 
	 * @param str
	 * @return
	 */
	private String Split_Carrier(String str) {
		System.out.println("Split_Carrier()");
		// +COPS: 0,0,Chunghwa Telecom,2
		String mustSplitString = str;

		String[] AfterSplit = mustSplitString.split(",");

		for (int i = 0; i < AfterSplit.length; i++) {

			// ��3�Ѽ��¦��q�H�~�̸��
			if (i == 2) {
				System.out.println("Po_split[2]=" + AfterSplit[i]);
				// �^�ǹq�H�~�̸��
				return AfterSplit[i];
			}
		}

		return "";
	}

}
