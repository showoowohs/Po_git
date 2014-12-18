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
	/**
	 * GSM = 0
	 */
	private final int GSM = 0; 
	/**
	 * GSM_COMPACT = 1
	 */
	private final int GSM_COMPACT = 1; 
	/**
	 * UTRAN = 2
	 */
	private final int UTRAN = 2; 
	/**
	 * GSM_with_EDGE_availability = 3
	 */
	private final int GSM_with_EDGE_availability = 3;
	/**
	 * UTRAN_with_HSDPA_availability = 4
	 */
	private final int UTRAN_with_HSDPA_availability = 4; 
	/**
	 * UTRAN_with_HSUPA_availability = 5
	 */
	private final int UTRAN_with_HSUPA_availability = 5; 
	/**
	 * UTRAN_with_HSDPA_and_HSUPA_availability = 6
	 */
	private final int UTRAN_with_HSDPA_and_HSUPA_availability = 6;
	/**
	 * LTE = 7
	 */
	private final int LTE = 7; 
	private String Po_Signal = ""; 
	 
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
			// 取得SD卡儲存路徑

			File sdcard = Environment.getExternalStorageDirectory();

			// Get the text file
			// File file = new File(sdcard, "111.txt");
			File file = new File(sdcard, "ublox_SIM_info.txt");
			Log.d("Po_add", "file=" + file);

			// Read text from file
			StringBuilder text = new StringBuilder();
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;

			while ((line = br.readLine()) != null) {
				text.append(line);
				text.append('\n');
			}
			// get 電信商資料
			Carrier += Split_Carrier(text.toString());
			Log.d("Po_add", "Carrier=" + Carrier);
			// System.out.println("Po...text=" + text);
			this.Po_Signal = Split_radio_access_technology(text.toString());
		} catch (IOException e) {
			Log.d("Po_add", "IOException" + e);
			// You'll need to add proper error handling here
		}

		// 如果都沒抓到數值就回傳 沒電信服務
		if (Carrier == null || Carrier == "") {
			Carrier = "No Service";
			return Carrier;
		}
		return Carrier;
	}

	/****
	 * 找出電信業者資資料 並回傳
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

			// 第3參數舊式電信業者資料
			if (i == 2) {
				// System.out.println("Po_split[2]=" + AfterSplit[i]);
				// 將取得的電信業者資料中的怪怪符號拿除
				String Del_Symbol = "";
				int last = AfterSplit[i].length() - 1;
				Del_Symbol = AfterSplit[i].substring(1, last);
				Log.d("Po_add", "Del_Symbol=" + Del_Symbol);
				// 回傳電信業者資料
				return Del_Symbol;
			}
		}

		return "";
	}

	/****
	 * 找出電信業者資資料的 radio access technology 並回傳 
	 * Indicates the radio access technology
	 *  
	 *  0: GSM 
	 *  1: GSM COMPACT 
	 *  2: UTRAN 
	 *  3: GSM with EDGE availability 
	 *  4: UTRAN with HSDPA availability 
	 *  5: UTRAN with HSUPA availability 
	 *  6: UTRAN with HSDPA and HSUPA availability 
	 *  7: LTE
	 * 
	 * @param str
	 * @return
	 */
	private String Split_radio_access_technology(String str) {
		Log.d("Po_add", "Split_radio_access_technology()");
		// +COPS: 0,0,Chunghwa Telecom,2
		String mustSplitString = str;

		String[] AfterSplit = mustSplitString.split(",");

		for (int i = 0; i < AfterSplit.length; i++) {

			// 找到radio access technology
			if (i == 3) {
				
				String tmp = AfterSplit[i].substring(0, 1);
				int radio_access_technology = Integer.parseInt(tmp);
				//radio_access_technology
				Log.d("Po_add", "radio_access_technology="+ radio_access_technology);
				//reference url  http://gophone.pixnet.net/blog/post/167676570-lte%E3%80%81wcdma%E3%80%81hsupa%E3%80%81gsm%E3%80%80%E5%82%BB%E5%82%BB%E5%88%86%E4%B8%8D%E6%B8%85%EF%BC%9F-%E7%B6%9C%E5%90%88%E8%A7%A3%E6%9E%90
				switch (radio_access_technology) {
					case GSM:
						Log.d("Po_add", "2G");
						return "GSM";
					case GSM_COMPACT:
					case UTRAN:
					case GSM_with_EDGE_availability:
					case UTRAN_with_HSDPA_availability:
					case UTRAN_with_HSUPA_availability:
					case UTRAN_with_HSDPA_and_HSUPA_availability:
						Log.d("Po_add", "3G");
						return "3G";
					case LTE:
						Log.d("Po_add", "4G");
						return "4G";
					default:
						Log.d("Po_add", "default");
				}
								
				return "";
			}
		}

		return "";
	}

}
