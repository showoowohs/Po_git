package com.imobile.thermalprinter_wifiswitch;

import java.util.HashMap;
import java.util.Iterator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lvrenyang.pos.Pos;
import com.lvrenyang.rw.PL2303Driver;
import com.lvrenyang.rw.TTYTermios;
import com.lvrenyang.rw.TTYTermios.FlowControl;
import com.lvrenyang.rw.TTYTermios.Parity;
import com.lvrenyang.rw.TTYTermios.StopBits;
import com.lvrenyang.rw.USBPort;
import com.lvrenyang.rw.USBSerialPort;

public class ThermalPrinter_WiFiSwitch extends Activity implements
		OnClickListener {

	private Button buttonTestText, buttonTestBarCode, buttonTestQrCode,
			buttonTestPic, buttonQueryStatus;
	private Button buttonDisconnect, buttonConnect;
	private TextView textView1;

	private static USBSerialPort serialPort;
	private static PL2303Driver mSerial;
	private static Pos mPos;
	private static Context mContext;

	private BroadcastReceiver broadcastReceiver;

	private boolean debug_main = true;

	private static final String str1 = "abcdefghijklmnopqrstuvwxyz";
	private static final String strBarCode = "123456789012";
	private static final String strQrCode = "Hello, the beautiful world!";
	// Po area Start
	private Context Po_context = null;
	private static boolean wifi_status = false;
	private static boolean BT_status = false;
	private static boolean GPS_status = false;
	private static int Thermal_status = 0;
	private ImageView Po_IV1_top, Po_IV2_top, Po_IV3_top, Po_IV4_top;
	private ImageView Po_IV1_below, Po_IV2_below, Po_IV3_below, Po_IV4_below;
	private final String TAG = "Po_test";

	static {
		System.loadLibrary("imobileJNI");
	}
	// Po area END

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_thermal_printer__wi_fi_switch);

		textView1 = (TextView) findViewById(R.id.textView1);
		buttonDisconnect = (Button) findViewById(R.id.buttonDisconnect);
		buttonDisconnect.setOnClickListener(this);
		buttonDisconnect.setEnabled(false);
		buttonConnect = (Button) findViewById(R.id.buttonConnect);
		buttonConnect.setOnClickListener(this);
		buttonConnect.setEnabled(true);
		buttonTestText = (Button) findViewById(R.id.buttonTestText);
		buttonTestText.setOnClickListener(this);
		buttonTestBarCode = (Button) findViewById(R.id.buttonTestBarCode);
		buttonTestBarCode.setOnClickListener(this);
		buttonTestQrCode = (Button) findViewById(R.id.buttonTestQrCode);
		buttonTestQrCode.setOnClickListener(this);
		buttonTestPic = (Button) findViewById(R.id.buttonTestPic);
		buttonTestPic.setOnClickListener(this);
		buttonQueryStatus = (Button) findViewById(R.id.buttonQueryStatus);
		buttonQueryStatus.setOnClickListener(this);

		mContext = getApplicationContext();
		mSerial = new PL2303Driver();

		serialPort = new USBSerialPort(null, null);
		mPos = new Pos(serialPort, mSerial);

		initBroadcast();
		handleIntent();
		debug_toast("onCreate");

		// Po add Start

		this.Po_context = this;
		Po_find_id();
		Po_init_parameter(this);

		// Po add END
	}

	// Po area Start

	/****
	 * toggle_Thermal
	 * 
	 * @param context
	 *            on/of Thermal Printer
	 */
	public void toggle_Thermal(Context context) {
		// 1. Read Thermal status
		String Thermal_Read_Path = "/proc/tca6416";
		String Tmp_Thermal_status = imobileJNI.ReadProc(Thermal_Read_Path);
		Log.d(TAG, "toggle_Thermal() Tmp_Thermal_status ="+ Tmp_Thermal_status.toString());
		
		if(Tmp_Thermal_status.equals("1")){
			// set Thermal_status --> 1
			this.Thermal_status = 1;
		} else {
			// set Thermal_status --> 0
			this.Thermal_status = 0;
		}

		Log.d(TAG, "toggle_Thermal() Thermal_status ="+ this.Thermal_status);
	}
	
	/***
	 * Po_thermal_area: click can toggle thermal printer on/off
	 * 
	 * @param view
	 */
	public void Po_thermal_area(View view) {
		toggle_Thermal(this);
//		 Log.d(TAG, "Po_thermal_area() click");
	}
	
	/****
	 * toggle_GPS
	 * 
	 * @param context
	 *            on/of GPS
	 */
	public void toggle_GPS(Context context) {
		//open GPS setting dialog
		Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
		        
	}
	
	/***
	 * Po_GPS_area: click can toggle GPS on/off
	 * 
	 * @param view
	 */
	public void Po_GPS_area(View view) {
		toggle_GPS(this);
		// Log.d(TAG, "click");
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

			// set drawable
			this.Po_IV2_top.setImageDrawable(getResources().getDrawable(
					R.drawable.ic_appwidget_settings_bluetooth_on_holo));
			this.Po_IV2_below.setImageDrawable(getResources().getDrawable(
					R.drawable.blue));

		} else {
			mBluetoothAdapter.disable();
			this.BT_status = false;

			// set drawable
			this.Po_IV2_top.setImageDrawable(getResources().getDrawable(
					R.drawable.ic_appwidget_settings_bluetooth_off_holo));
			this.Po_IV2_below.setImageDrawable(getResources().getDrawable(
					R.drawable.white));

		}
		Log.d(TAG, "toggle_BT() BT_status=" + this.BT_status);
	}

	/***
	 * Po_BT_area: click can toggle BT on/off
	 * 
	 * @param view
	 */
	public void Po_BT_area(View view) {
		toggle_BT(this);
		// Log.d(TAG, "click");
	}

	/*****
	 * toggle_WiFi: on/off WiFi
	 * 
	 * @param context
	 */
	public void toggle_WiFi(Context context) {
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		if (!wifiManager.isWifiEnabled()) {
			wifiManager.setWifiEnabled(true);
			this.wifi_status = true;

			// set drawable
			this.Po_IV1_top.setImageDrawable(getResources().getDrawable(
					R.drawable.ic_appwidget_settings_wifi_on_holo));
			this.Po_IV1_below.setImageDrawable(getResources().getDrawable(
					R.drawable.blue));

		} else {
			wifiManager.setWifiEnabled(false);
			this.wifi_status = false;

			// set drawable
			this.Po_IV1_top.setImageDrawable(getResources().getDrawable(
					R.drawable.ic_appwidget_settings_wifi_off_holo));
			this.Po_IV1_below.setImageDrawable(getResources().getDrawable(
					R.drawable.white));

		}
		Log.d(TAG, "toggleWiFi() wifi_status=" + this.wifi_status);
		// System.out.println("toggleWiFi() wifi_status=" + this.wifi_status);
	}

	/***
	 * Po_wifi_area: click can toggle wifi on/off
	 * 
	 * @param view
	 */
	public void Po_wifi_area(View view) {
		toggle_WiFi(this);
		// Log.d(TAG, "click");
	}

	/***
	 * Po_init_parameter: onCreate and onResume call can read wifi status, last
	 * chang wifi/BT icon
	 * 
	 * @param context
	 */
	private void Po_init_parameter(Context context) {

		// read wifi status
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		if (!wifiManager.isWifiEnabled()) {
			this.wifi_status = false;

			// set drawable
			this.Po_IV1_top.setImageDrawable(getResources().getDrawable(
					R.drawable.ic_appwidget_settings_wifi_off_holo));
			this.Po_IV1_below.setImageDrawable(getResources().getDrawable(
					R.drawable.white));

		} else {
			this.wifi_status = true;

			// set drawable
			this.Po_IV1_top.setImageDrawable(getResources().getDrawable(
					R.drawable.ic_appwidget_settings_wifi_on_holo));
			this.Po_IV1_below.setImageDrawable(getResources().getDrawable(
					R.drawable.blue));

		}
		Log.d(TAG, "wifi_status=" + this.wifi_status);

		// read bluetooth status
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();
		if (!mBluetoothAdapter.isEnabled()) {
			this.BT_status = false;

			// set drawable
			this.Po_IV2_top.setImageDrawable(getResources().getDrawable(
					R.drawable.ic_appwidget_settings_bluetooth_off_holo));
			this.Po_IV2_below.setImageDrawable(getResources().getDrawable(
					R.drawable.white));

		} else {
			this.BT_status = true;

			// set drawable
			this.Po_IV2_top.setImageDrawable(getResources().getDrawable(
					R.drawable.ic_appwidget_settings_bluetooth_on_holo));
			this.Po_IV2_below.setImageDrawable(getResources().getDrawable(
					R.drawable.blue));
		}

		Log.d(TAG, "BT_status=" + this.BT_status);

		// read GPS status
		String provider = Settings.Secure.getString(
				context.getContentResolver(),
				Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		this.GPS_status = provider.contains("gps");

		if (this.GPS_status) {

			//true
			// set drawable
			this.Po_IV3_top.setImageDrawable(getResources().getDrawable(
					R.drawable.ic_appwidget_settings_gps_on_holo));
			this.Po_IV3_below.setImageDrawable(getResources().getDrawable(
					R.drawable.blue));
		} else {
			
			//false
			// set drawable
			this.Po_IV3_top.setImageDrawable(getResources().getDrawable(
					R.drawable.ic_appwidget_settings_gps_off_holo));
			this.Po_IV3_below.setImageDrawable(getResources().getDrawable(
					R.drawable.white));

		}

		Log.d(TAG, "GPS_status=" + this.GPS_status);
	}

	/***
	 * Po_find_id: can run findViewById
	 */
	private void Po_find_id() {
		// find id wifi
		this.Po_IV1_top = (ImageView) findViewById(R.id.Po_IV1_top);
		this.Po_IV1_below = (ImageView) findViewById(R.id.Po_IV1_below);

		// find id BT
		this.Po_IV2_top = (ImageView) findViewById(R.id.Po_IV2_top);
		this.Po_IV2_below = (ImageView) findViewById(R.id.Po_IV2_below);

		// find id GPS
		this.Po_IV3_top = (ImageView) findViewById(R.id.Po_IV3_top);
		this.Po_IV3_below = (ImageView) findViewById(R.id.Po_IV3_below);
		
		// find id Thermal Printer
		this.Po_IV4_top = (ImageView) findViewById(R.id.Po_IV4_top);
		this.Po_IV4_below = (ImageView) findViewById(R.id.Po_IV4_below);

	}

	// Po area END

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.thermal_printer__wi_fi_switch, menu);
		return true;
	}

	private void initBroadcast() {
		broadcastReceiver = new BroadcastReceiver() {

			@SuppressLint("NewApi")
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				String action = intent.getAction();
				if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
					textView1.setText("");
					buttonDisconnect.callOnClick();

				}
			}

		};
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
		intentFilter.addAction(UsbManager.ACTION_USB_ACCESSORY_ATTACHED);
		intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
		mContext.registerReceiver(broadcastReceiver, intentFilter);
	}

	@SuppressLint("NewApi")
	private void uninitBroadcast() {
		mContext.unregisterReceiver(broadcastReceiver);
	}

	@SuppressLint("NewApi")
	private void handleIntent() {
		buttonConnect.callOnClick();
	}

	private void debug_toast(String msg) {
		if (debug_main)
			Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		handleIntent();
		debug_toast("onNewIntent()");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Po_init_parameter(this);
		debug_toast("onResume");
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		disconnect();
		uninitBroadcast();
		debug_toast("onDestroy");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.buttonConnect:
			try {
				connect();
			} catch (Exception e) {
				debug_toast(e.toString());
			}
			break;

		case R.id.buttonDisconnect:
			disconnect();
			break;

		case R.id.buttonTestText:
			/**
			 * usb打印机虚拟串口波特率只有象征意义，波特率哪一个都可以。
			 */
			open(115200, Parity.NONE);
			mPos.POS_S_TextOut(str1, 32, 0, 0, 0, 0x00 + 0x400);
			mPos.POS_FeedLine();
			mPos.POS_FeedLine();
			debug_toast("Testing TEXT");
			close();
			break;

		case R.id.buttonTestBarCode:
			open(115200, Parity.NONE);
			mPos.POS_S_SetBarcode(strBarCode, 32, 0x41, 3, 162, 0x00, 0x02);
			mPos.POS_FeedLine();
			mPos.POS_FeedLine();
			debug_toast("Testing BarCode");
			close();
			break;

		case R.id.buttonTestQrCode:
			open(115200, Parity.NONE);
			mPos.POS_S_SetQRcode(strQrCode, 6, 4);
			mPos.POS_FeedLine();
			mPos.POS_FeedLine();
			debug_toast("Testing QRCode");
			close();
			break;

		case R.id.buttonTestPic:
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.iu2);
			if (null == bitmap) {
				debug_toast("Image Decoding ERROR!");
				break;
			}
			open(115200, Parity.NONE);
			/**
			 * 图片的像素宽度必须为8的整数倍
			 */
			mPos.POS_PrintPicture(bitmap, 360, 0);
			mPos.POS_FeedLine();
			mPos.POS_FeedLine();
			debug_toast("Testing Image");
			close();
			break;

		case R.id.buttonQueryStatus:

			open(115200, Parity.NONE);

			int retry = 0;
			int maxRetry = 3;
			byte[] data = { 0x10, 0x04, 0x01 };
			byte[] buf = new byte[1];

			/** 有两次机会 */
			for (retry = 0; retry < maxRetry; retry++) {
				mPos.POS_Write(data, 0, data.length, 500);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				int rec = mPos.POS_Read(buf, 0, buf.length, 1000);
				if (rec == buf.length)
					break;
			}
			if (retry == maxRetry)
				debug_toast("Load Failed");
			else
				debug_toast("" + buf[1]);

			break;
		}
	}

	/**
	 * 打开和关闭按钮，就是用来连接USB的，打开就是probe，关闭就是disconnect
	 * probe会填充port字段，open会根据termios字段来打开串口
	 * 
	 */

	@SuppressLint("NewApi")
	private void connect() {
		final UsbManager mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
		HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
		Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
		if (deviceList.size() > 0) {
			// 初始化选择对话框布局，并添加按钮和事件
			LinearLayout llSelectDevice = new LinearLayout(this);
			llSelectDevice.setOrientation(LinearLayout.VERTICAL);
			llSelectDevice.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Link Device").setView(llSelectDevice);
			final AlertDialog dialog = builder.create();

			while (deviceIterator.hasNext()) { // 这里是if不是while，说明我只想支持一种device
				final UsbDevice device = deviceIterator.next();
				Button btDevice = new Button(llSelectDevice.getContext());
				btDevice.setLayoutParams(new LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				btDevice.setGravity(Gravity.LEFT);
				btDevice.setText("ID: " + device.getDeviceId());
				btDevice.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						PendingIntent mPermissionIntent = PendingIntent
								.getBroadcast(
										ThermalPrinter_WiFiSwitch.this,
										0,
										new Intent(
												ThermalPrinter_WiFiSwitch.this
														.getApplicationInfo().packageName),
										0);
						serialPort.port = new USBPort(mUsbManager,
								ThermalPrinter_WiFiSwitch.this, device,
								mPermissionIntent);
						int ret = mSerial.pl2303_probe(serialPort);
						if (ret == 0) {
							textView1.setText(System.currentTimeMillis() + ": "
									+ " connection successful " + mSerial.type
									+ "\n");
							buttonConnect.setEnabled(false);
							buttonDisconnect.setEnabled(true);
						} else {
							textView1.setText(System.currentTimeMillis() + ": "
									+ " connection fail(" + ret + ")\n");
							buttonConnect.setEnabled(true);
							buttonDisconnect.setEnabled(false);
						}
					}
				});
				llSelectDevice.addView(btDevice);
			}
			if (llSelectDevice.getChildCount() == 1)
				llSelectDevice.getChildAt(0).callOnClick();
			else
				dialog.show();
		}
	}

	private void open(final int baudrate, final Parity parity) {

		TTYTermios termios = serialPort.termios;
		serialPort.termios = new TTYTermios(baudrate, FlowControl.NONE, parity,
				StopBits.ONE, 8);
		int ret = mSerial.pl2303_open(serialPort, termios);
		if (ret == 0)
			textView1.setText(System.currentTimeMillis() + ": "
					+ "open successful" + serialPort.termios.baudrate + " "
					+ serialPort.termios.parity + "\n");
		else
			textView1.setText(System.currentTimeMillis() + ": " + "open fail("
					+ ret + ")\n");

	}

	private void close() {
		mSerial.pl2303_close(serialPort);
	}

	private void disconnect() {
		close();
		mSerial.pl2303_disconnect(serialPort);
		textView1.setText("");
		buttonConnect.setEnabled(true);
		buttonDisconnect.setEnabled(false);

	}
}
