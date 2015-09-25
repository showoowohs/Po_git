package com.imobile.mt8382toggleadbandusb;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.ooieueioo.externallib.ProcessInfo.ProcessInfo;

public class MainActivity extends Activity {
	private TextView POTV1, POTV2;
	private ImageView POIV1;
	private ToggleButton tButton;
	private final int Default_config = 1;
	private Thread RmmodNFC, ChmodNFC, ChmodGPS, InsmodNFC;

	private final String TAG = "Po_dbg";

	static {
		System.loadLibrary("imobileJNI");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		PO_findID();
		init();

		// toggle on/off event
		this.tButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {

				if (isChecked) {
					Log.d(TAG, "button on");
					show_dialog(
							"Warning",
							"USB host power will be disable!! (USB 3G function may not work)",
							Default_config);

				} else {

					// on USB power
					USBON();

					// show usb mode(USB/OTG)
					String SetUIText = ReadUSBStatus();
					POTV1.setText(SetUIText);

					// show picture
					POIV1.setImageResource(R.drawable.usb_hub);

					// setting nfc
					NFCON();

					// setting gps
					USBGPSON();

					Log.d(TAG, "button off");
				}

			}
		});
	}

	/**
	 * this is a exit button, can close APP
	 * 
	 * @param view
	 */
	public void Po_close(View view) {
		Log.d(TAG, "Po_close()");

		boolean status = this.tButton.isChecked();

		if (status == true) {
			Log.d(TAG, "Po_close RmmodNFC.isAlive()" + RmmodNFC.isAlive());
			if (this.RmmodNFC.isAlive() == true) {
				debug_toast("Please exit one more time!");
			} else {
				onDestroy();
			}
		} else {

			Log.d(TAG, "Po_close ChmodGPS.isAlive()" + ChmodGPS.isAlive());
			Log.d(TAG, "Po_close ChmodNFC.isAlive()" + ChmodNFC.isAlive());
			Log.d(TAG, "Po_close ChmodNFC.isAlive()" + InsmodNFC.isAlive());

			if ((this.ChmodGPS.isAlive() == true)
					&& (this.ChmodNFC.isAlive() == true)
					&& (this.InsmodNFC.isAlive() == true)) {
				debug_toast("Please exit one more time!");
			} else {
				onDestroy();
			}
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "exit this APP");
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(1);
	}

	private void PO_findID() {
		this.POTV1 = (TextView) findViewById(R.id.POTV1);
		this.POTV2 = (TextView) findViewById(R.id.POTV2);
		this.tButton = (ToggleButton) findViewById(R.id.toggleButton1);
		this.POIV1 = (ImageView) findViewById(R.id.POIV1);
	}

	/**
	 * can read USB status(OTG/USB), through cat /proc/USBStatus
	 * 
	 * @return USB current status
	 */
	private String ReadUSBStatus() {
		String status = "";
		String ReturnUI = "";
		try {
			status = imobileJNI.ReadProc("/proc/USBStatus");
			Log.d(TAG, "status=" + status.toString());

			if (status.equals("USB") || status.equals("OTG")) {
				ReturnUI = status + " Model is currently";
			} else {
				ReturnUI = "Read USBStatus error!";
				return ReturnUI;
			}
		} catch (Exception e) {
			// TODO: handle exception
			ReturnUI = "Read USBStatus error!";
			return ReturnUI;
		}

		return ReturnUI;
	}

	/**
	 * turn on USB GPS, via setting permission
	 */
	private void USBGPSON() {
		ChmodGPS = new Thread(new GPSChmodThread());
		ChmodGPS.start();
	}

	/**
	 * turn off nfc, if not nfc driver this function is not work
	 */
	private void NFCOFF() {
		// read nfc pid
		String NFCPID = getRunningAppProcessInfo_NFCPID();
		if (!NFCPID.equals("0")) {
			KILLNFCPID(NFCPID);
			// delay_via_thread(500);
			RmmodNFC = new Thread(new NFCRmmodThread());
			RmmodNFC.start();
		}
	}

	private void NFCON() {
		// read nfc pid
		String NFCPID = getRunningAppProcessInfo_NFCPID();
		if (!NFCPID.equals("0")) {
			InsmodNFC = new Thread(new NFCInmodThread());
			InsmodNFC.start();
			// delay_via_thread(3000);
			ChmodNFC = new Thread(new NFCPremissionThread());
			ChmodNFC.start();
		}
	}

	/**
	 * can disable USB power(through echo USBOFF > "/proc/USBStatus") P.S if
	 * function fail will show "write USBOFF error" dialog
	 */
	private void USBOFF() {
		String status = "";
		try {
			status = imobileJNI.WriteProc("/proc/USBStatus", "USBOFF");
			Log.d(TAG, "status=" + status.toString());
			if (status.equals("xx")) {
				debug_toast("write USBOFF error");
			}
		} catch (Exception e) {
			// TODO: handle exception
			debug_toast("write USBOFF error");
		}
	}

	/**
	 * can enable USB power(through echo USBON > "/proc/USBStatus") if function
	 * fail will show "write USBOFF error" dialog
	 */
	private void USBON() {
		String status = "";
		try {
			status = imobileJNI.WriteProc("/proc/USBStatus", "USBON");
			Log.d(TAG, "status=" + status.toString());
			if (status.equals("xx")) {
				debug_toast("write USBON error");
			}
		} catch (Exception e) {
			// TODO: handle exception
			debug_toast("write USBON error");
		}
	}

	/**
	 * can kill nfc PID (through echo KILLPID MyPID > "/proc/USBStatus")
	 */
	private void KILLNFCPID(String NFCPID) {
		String status = "";
		String command = "KILLPID " + NFCPID;
		try {
			status = imobileJNI.WriteProc("/proc/USBStatus", command);
			Log.d(TAG, "status=" + status.toString());
			if (status.equals("xx")) {
				debug_toast("write KILLPID error");
			}
		} catch (Exception e) {
			// TODO: handle exception
			debug_toast("write KILLPID error");
		}
	}

	/**
	 * can rmmod nfc module (through echo RMMOD cp210x+ > "/proc/USBStatus")
	 */
	private void RmmodNFCModule() {
		String status = "";
		try {
			status = imobileJNI.WriteProc("/proc/USBStatus", "RMMOD cp210x+");
			Log.d(TAG, "status=" + status.toString());
			if (status.equals("xx")) {
				debug_toast("write rmmod error");
			}
		} catch (Exception e) {
			// TODO: handle exception
			debug_toast("write rmmod error");
		}
	}

	/**
	 * can insmod nfc module (through echo INSMOD /system/lib/modules/cp210x.ko+
	 * > "/proc/USBStatus")
	 */
	private void InsmodNFCModule() {
		String status = "";
		try {
			status = imobileJNI.WriteProc("/proc/USBStatus",
					"INSMOD /system/lib/modules/cp210x.ko+");
			Log.d(TAG, "status=" + status.toString());
			if (status.equals("xx")) {
				debug_toast("write insmod error");
			}
		} catch (Exception e) {
			// TODO: handle exception
			debug_toast("write insmod error");
		}
	}

	/**
	 * can set /dev/ttyUSB1 permission (through echo CHMOD 0666 /dev/ttyUSB1+ >
	 * "/proc/USBStatus") can set /dev/ttyACM0 permission (through echo CHMOD
	 * 0666 /dev/ttyACM0+ > "/proc/USBStatus")
	 */
	private void ChmodDevNote(String device_note) {
		String status = "";
		String command = "CHMOD 0777 " + device_note + "+";
		try {
			status = imobileJNI.WriteProc("/proc/USBStatus", command);
			Log.d(TAG, "status=" + status.toString());
			if (status.equals("xx")) {
				debug_toast("write insmod error");
			}
		} catch (Exception e) {
			// TODO: handle exception
			debug_toast("write insmod error");
		}
	}

	private void init() {
		// setting text size
		this.POTV1.setTextSize(36);
		this.POTV1.setTextColor(Color.BLACK);
		this.POTV2.setTextSize(30);
		this.POTV2.setTextColor(Color.BLACK);

		// show usb mode(USB/OTG)
		String SetUIText = ReadUSBStatus();
		this.POTV1.setText(SetUIText);
		this.POTV2.setText("OTG mode");

		// check status
		// Log.d(TAG, "status = "+SetUIText.substring(0, 3));
		if (SetUIText.substring(0, 3).equals("USB")) {

			this.tButton.setChecked(false);

			// show picture
			this.POIV1.setImageResource(R.drawable.usb_hub);

			// setting nfc
			NFCON();

			// setting GPS
			USBGPSON();

		} else if (SetUIText.substring(0, 3).equals("OTG")) {

			this.tButton.setChecked(true);
			// show picture
			this.POIV1.setImageResource(R.drawable.otg_600web);

			// setting nfc
			NFCOFF();

		}

	}

	/***
	 * ≈„•‹diglog
	 * 
	 * @param Title
	 * @param Msg
	 * @param event
	 */
	public void show_dialog(String Title, String Msg, int Po_event) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle(Title);
		dialog.setMessage(Msg);
		dialog.setIcon(android.R.drawable.ic_dialog_alert);
		dialog.setCancelable(false);

		if (Po_event == this.Default_config) {
			dialog.setIcon(R.drawable.warning_wevere);
			dialog.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							// on USB power
							USBOFF();

							// show usb mode(USB/OTG)
							String SetUIText = ReadUSBStatus();
							POTV1.setText(SetUIText);

							// show picture
							POIV1.setImageResource(R.drawable.otg_600web);

							// setting nfc
							NFCOFF();

						}
					});
			dialog.setNegativeButton("close",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							tButton.setChecked(false);

							// show usb mode(USB/OTG)
							String SetUIText = ReadUSBStatus();
							POTV1.setText(SetUIText);

							// show picture
							POIV1.setImageResource(R.drawable.usb_hub);

						}
					});

		}

		dialog.show();

	}

	private void debug_toast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	/*****
	 * can delay example: Thread.sleep(1000);
	 * 
	 * @param delay_time
	 */
	private void delay_via_thread(int delay_time) {
		int DelatTime = delay_time;
		try {
			// delay 1 second
			Thread.sleep(DelatTime);

		} catch (InterruptedException e) {
			e.printStackTrace();

		}
	}

	/**
	 * get NFC PID, busybox ps | grep nfc return NFC_PID
	 */
	private String getRunningAppProcessInfo_NFCPID() {

		ActivityManager mActivityManager = null;

		List<ProcessInfo> processInfoList = null;

		mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

		processInfoList = new ArrayList<ProcessInfo>();

		List<ActivityManager.RunningAppProcessInfo> appProcessList = mActivityManager
				.getRunningAppProcesses();

		for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessList) {

			int pid = appProcessInfo.pid;

			int uid = appProcessInfo.uid;

			String processName = appProcessInfo.processName;

			int[] myMempid = new int[] { pid };

			Debug.MemoryInfo[] memoryInfo = mActivityManager
					.getProcessMemoryInfo(myMempid);

			int memSize = memoryInfo[0].dalvikPrivateDirty;

			// Log.i(TAG, "processName: " + processName + "  pid: " + pid
			// + " uid:" + uid + " memorySize is -->" + memSize + "kb");

			// find nfc package name
			if (processName.equals("com.android.nfc")) {
				// if (processName.equals("com.imobile.packagemanagertest")) {

				ProcessInfo processInfo = new ProcessInfo();
				processInfo.setPid(pid);
				processInfo.setUid(uid);
				processInfo.setMemSize(memSize);
				processInfo.setPocessName(processName);
				processInfoList.add(processInfo);

				String[] packageList = appProcessInfo.pkgList;
				// Log.i(TAG, "process id is " + pid + " has "
				// + packageList.length);

				String PID = Integer.toString(pid);
				return PID;
			}

		}
		return "0";
	}

	public class NFCPremissionThread implements Runnable {
		@Override
		public void run() {
			try {
				// delay 3 second
				int i = 0;
				while (true) {
					i++;
					Thread.sleep(1000);
					Log.d(TAG, "NFCPremissionThread() i=" + i);
					if (i > 3) {
						ChmodDevNote("/dev/ttyUSB1");
						break;
					}
				}

			} catch (InterruptedException e) {
				e.printStackTrace();

			}
		}
	}

	public class NFCRmmodThread implements Runnable {
		@Override
		public void run() {
			try {
				// delay 2 second
				int i = 0;
				while (true) {
					i++;
					Thread.sleep(1000);
					Log.d(TAG, "NFCRmmodThread() i=" + i);
					if (i > 2) {
						RmmodNFCModule();
						break;
					}
				}

			} catch (InterruptedException e) {
				e.printStackTrace();

			}
		}
	}

	public class GPSChmodThread implements Runnable {
		@Override
		public void run() {
			try {
				// delay 2 second
				int i = 0;
				while (true) {
					i++;
					Thread.sleep(1000);
					Log.d(TAG, "GPSChmodThread() i=" + i);
					if (i > 3) {
						ChmodDevNote("/dev/ttyACM0");
						break;
					}
				}

			} catch (InterruptedException e) {
				e.printStackTrace();

			}
		}
	}

	public class NFCInmodThread implements Runnable {
		@Override
		public void run() {
			try {
				// delay 2 second
				int i = 0;
				while (true) {
					i++;
					Thread.sleep(1000);
					Log.d(TAG, "NFCInmodThread() i=" + i);
					if (i > 2) {
						InsmodNFCModule();
						break;
					}
				}

			} catch (InterruptedException e) {
				e.printStackTrace();

			}
		}
	}
}
