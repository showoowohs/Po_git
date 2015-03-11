package com.imobile.iq8_fixwifi;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	private final String TAG = "Po_debug";
	private EditText Po_Mac1, Po_Mac2, Po_Mac3, Po_Mac4, Po_Mac5, Po_Mac6; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Po_find_id();
		// 1. copy file to /sdcard
		// CopyAssets();
	}
	
	private String Po_get_Mac(){
		String My_Mac = this.Po_Mac1.getText().toString()+this.Po_Mac2.getText().toString()+this.Po_Mac3.getText().toString()+this.Po_Mac4.getText().toString()+this.Po_Mac5.getText().toString()+this.Po_Mac6.getText().toString(); 
		Log.i(TAG, "My_Mac="+My_Mac);
		return My_Mac;
	}
	
	public void Po_find_id(){
		this.Po_Mac1 = (EditText) findViewById(R.id.Po_MAC_ET1);
		this.Po_Mac2 = (EditText) findViewById(R.id.Po_MAC_ET2);
		this.Po_Mac3 = (EditText) findViewById(R.id.Po_MAC_ET3);
		this.Po_Mac4 = (EditText) findViewById(R.id.Po_MAC_ET4);
		this.Po_Mac5 = (EditText) findViewById(R.id.Po_MAC_ET5);
		this.Po_Mac6 = (EditText) findViewById(R.id.Po_MAC_ET6);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "onDestroy()");

		// delete /mnt/shell/emulated/0/*.ini & disable_WMM && call reboot()
		Run_su("busybox rm /mnt/shell/emulated/0/disable_WMM; busybox rm /mnt/shell/emulated/0/WCNSS_qcom_cfg_Jason1.ini; busybox rm /mnt/shell/emulated/0/WCNSS_qcom_cfg_org.ini; /system/bin/reboot");
		// Kill myself
		// android.os.Process.killProcess(android.os.Process.myPid());
	}

	/***
	 * 顯示diglog
	 * 
	 * @param Title
	 * @param Msg
	 */
	public void show_dialog(String Title, String Msg) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle(Title);
		dialog.setMessage(Msg);
		dialog.setIcon(android.R.drawable.ic_dialog_alert);
		dialog.setCancelable(false);
		dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// 按下PositiveButton要做的事
				Toast.makeText(MainActivity.this, "APP is exit",
						Toast.LENGTH_SHORT).show();
				onDestroy();
			}
		});

		dialog.show();

	}

	/**
	 * check_copy_dialog
	 * 
	 * @param Title
	 * @param Msg
	 * @param config_num
	 */
	public void check_copy_dialog(String Title, String Msg, int config_num) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle(Title);
		dialog.setMessage(Msg);
		dialog.setIcon(android.R.drawable.ic_dialog_alert);
		dialog.setCancelable(false);
		if (config_num == 1) {
			// user select config1
			Log.i(TAG, "config_num = 1");

			dialog.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// 按下PositiveButton要做的事
							// 1. creat a cfg (disable_WMM)
							
							Run_su("echo '# This file allows user to override the factory\n\n# defaults for the WLAN Driver\n\n\n# Enable IMPS or not\ngEnableImps=1\n\n# Enable/Disable Idle Scan\n\ngEnableIdleScan=0\n\n\n# Increase sleep duration (seconds) during IMPS\n# 0 implies no periodic wake up from IMPS. Periodic wakeup is \n# unnecessary if Idle Scan is disabled.\ngImpsModSleepTime=0\n\n\n# Enable BMPS or not\ngEnableBmps=1\n\n# Enable suspend or not\n\n# 1: Enable standby, 2: Enable Deep sleep, 3: Enable Mcast/Bcast Filter\n\ngEnableSuspend=3\n\n\n# Phy Mode (auto, b, g, n, etc)\n# Valid values are 0-9, with 0 = Auto, 4 = 11n, 9 = 11ac\ngDot11Mode=0\n\n\n# Handoff Enable(1) Disable(0)\n\ngEnableHandoff=0\n\n\n# CSR Roaming Enable(1) Disable(0)\n\ngRoamingTime=0\n\n\n# Assigned MAC Addresses - This will be used until NV items are in place\n\n# Each byte of MAC address is represented in Hex format as XX\n\nIntf0MacAddress="+Po_get_Mac()+"\nIntf1MacAddress=000AF58989FE\nIntf2MacAddress=000AF58989FD\n\nIntf3MacAddress=000AF58989FC\n\n\n# UAPSD service interval for VO,VI, BE, BK traffic\n\nInfraUapsdVoSrvIntv=0\n\nInfraUapsdViSrvIntv=0\n\nInfraUapsdBeSrvIntv=0\n\nInfraUapsdBkSrvIntv=0\n\n\n# Make 1x1 the default antenna configuration\n\ngNumRxAnt=1\n\n\n# Beacon filtering frequency (unit in beacon intervals)\n\ngNthBeaconFilter=50\n\n\n# Enable WAPI or not\n\n# WAPIIsEnabled=0\n\n\n# Flags to filter Mcast abd Bcast RX packets.\n\n# Value 0: No filtering, 1: Filter all Multicast.\n\n# 2: Filter all Broadcast. 3: Filter all Mcast abd Bcast\n\nMcastBcastFilter=3\n\n\n#Flag to enable HostARPOffload feature or not\n\nhostArpOffload=1\n\n\n#SoftAP Related Parameters\n\n# AP MAc addr\n\ngAPMacAddr=000AF589dcab\n\n\n# 802.11n Protection flag\n\ngEnableApProt=1\n\n\n#Enable OBSS protection\n\ngEnableApOBSSProt=1\n\n\n#Enable/Disable UAPSD for SoftAP\n\ngEnableApUapsd=0\n\n\n# Fixed Rate\n\ngFixedRate=0\n\n\n# Maximum Tx power\n\n# gTxPowerCap=30\n\n\n# Fragmentation Threshold\n\n# gFragmentationThreshold=2346\n\n\n# RTS threshold\n\nRTSThreshold=2347\n\n\n# Intra-BSS forward\n\ngDisableIntraBssFwd=0\n\n\n# WMM Enable/Disable\n\nWmmIsEnabled=0\n\n\n# 802.11d support\n\ng11dSupportEnabled=1\n\n# 802.11h support\n\ng11hSupportEnabled=1\n\n# CCX Support and fast transition\nCcxEnabled=0\nFastTransitionEnabled=1\nImplicitQosIsEnabled=1\ngNeighborScanTimerPeriod=200\n\ngNeighborLookupThreshold=76\ngNeighborReassocThreshold=81\n\ngNeighborScanChannelMinTime=20\ngNeighborScanChannelMaxTime=30\ngMaxNeighborReqTries=3\n\n# Legacy (non-CCX, non-802.11r) Fast Roaming Support\n# To enable, set FastRoamEnabled=1\n# To disable, set FastRoamEnabled=0\nFastRoamEnabled=1\n\n#Check if the AP to which we are roaming is better than current AP in terms of RSSI.\n#Checking is disabled if set to Zero.Otherwise it will use this value as to how better \n#the RSSI of the new/roamable AP should be for roaming\nRoamRssiDiff=3\n\n# If the RSSI of any available candidate is better than currently associated\n# AP by at least gImmediateRoamRssiDiff, then being to roam immediately (without\n# registering for reassoc threshold).\n# NOTE: Value of 0 means that we would register for reassoc threshold.\ngImmediateRoamRssiDiff=10\n\n# To enable, set gRoamIntraBand=1 (Roaming within band)\n# To disable, set gRoamIntraBand=0 (Roaming across band)\ngRoamIntraBand=0\n\n# SAP Country code\n\n# Default Country Code is 2 bytes, 3rd byte is optional indoor or out door.\n\n# Example\n\n#   US Indoor, USI\n\n#   Korea Outdoor, KRO\n\n#   Japan without optional byte, JP\n\n#   France without optional byte, FR\n\n#gAPCntryCode=USI\n\n\n#Short Guard Interval Enable/disable\n\ngShortGI20Mhz=1\n\ngShortGI40Mhz=1\n\n\n#Auto Shutdown  Value in seconds. A value of 0 means Auto shutoff is disabled\n\ngAPAutoShutOff=0\n\n\n# SAP auto channel selection configuration\n\n# 0 = disable auto channel selection\n\n# 1 = enable auto channel selection, channel provided by supplicant will be ignored\n\ngApAutoChannelSelection=0\n\n\n# Listen Energy Detect Mode Configuration\n\n# Valid values 0-128\n\n# 128 means disable Energy Detect feature\n\n# 0-9 are threshold code and 7 is recommended value from system if feature is to be enabled.\n\n# 10-128 are reserved.\n\n# The EDET threshold mapping is as follows in 3dB step:\n\n# 0 = -60 dBm\n\n# 1 = -63 dBm\n\n# 2 = -66 dBm\n\n# ...\n\n# 7 = -81 dBm\n\n# 8 = -84 dBm\n\n# 9 = -87 dBm\n\n# Note: Any of these settings are valid. Setting 0 would yield the highest power saving (in a noisy environment) at the cost of more range. The range impact is approximately #calculated as:\n\n#\n\n#  Range Loss  (dB)  =  EDET threshold level (dBm) + 97 dBm.\n\n#\n\ngEnablePhyAgcListenMode=128\n\n\n#Preferred channel to start BT AMP AP mode (0 means, any channel)\n\nBtAmpPreferredChannel=0\n\n\n#Preferred band (both or 2.4 only or 5 only)\n\nBandCapability=0\n\n\n#Beacon Early Termination (1 = enable the BET feature, 0 = disable)\n\nenableBeaconEarlyTermination=1\n\ngTelescopicBeaconWakeupEn=1\ntelescopicBeaconTransListenInterval=3\ntelescopicBeaconTransListenIntervalNumIdleBcns=10\ntelescopicBeaconMaxListenInterval=5\ntelescopicBeaconMaxListenIntervalNumIdleBcns=15\n\nbeaconEarlyTerminationWakeInterval=10\n\n\n#Bluetooth Alternate Mac Phy (1 = enable the BT AMP feature, 0 = disable)\n\ngEnableBtAmp=0\n\n\n#SOFTAP Channel Range selection \n\ngAPChannelSelectStartChannel=1\n\ngAPChannelSelectEndChannel=11\n\n\n#SOFTAP Channel Range selection Operating band\n\n# 0:2.4GHZ 1: LOW-5GHZ 2:MID-5GHZ 3:HIGH-5GHZ 4: 4.9HZ BAND\n\ngAPChannelSelectOperatingBand=0\n\n\n#Channel Bonding\ngChannelBondingMode5GHz=1\n\n\n#Enable Keep alive with non-zero period value\n\n#gStaKeepAlivePeriod = 30\n\n#AP LINK MONITOR TIMEOUT is used for both SAP and GO mode.\n#It is used to change the frequency of keep alive packets in the AP Link Monitor period which is by\n#default 20s. Currently the keep alive packets are sent as an interval of 3s but after this change\n#the keep alive packet frequency can be changed.\n\n#gApLinkMonitorPeriod = 3\n\n\n#If set will start with active scan after driver load, otherwise will start with\n\n#passive scan to find out the domain\n\ngEnableBypass11d=1\n\n\n#If set to 0, will not scan DFS channels\n\ngEnableDFSChnlScan=1\n\n\ngVhtChannelWidth=2\ngEnableLogp=1\n\n\n# Enable Automatic Tx Power control\n\ngEnableAutomaticTxPowerControl=1\n\n# 0 for OLPC 1 for CLPC and SCPC\ngEnableCloseLoop=1\n\n#Data Inactivity Timeout when in powersave (in ms)\ngDataInactivityTimeout=200\n\n# VHT Tx/Rx MCS values\n# Valid values are 0,1,2. If commented out, the default value is 0.\n# 0=MCS0-7, 1=MCS0-8, 2=MCS0-9\ngVhtRxMCS=2\ngVhtTxMCS=2\n\n# Enable CRDA regulatory support by settings default country code\n#gCrdaDefaultCountryCode=TW\n\n# Scan Timing Parameters\n# gPassiveMaxChannelTime=110\n# gPassiveMinChannelTime=60\n# gActiveMaxChannelTime=40\n# gActiveMinChannelTime=20\n\n#If set to 0, MCC is not allowed.\ngEnableMCCMode=1\n\n# 1=enable STBC; 0=disable STBC \ngEnableRXSTBC=1\n\n# Enable Active mode offload\ngEnableActiveModeOffload=1\n\n#Enable Scan Results Aging based on timer \n#Timer value is in seconds\n#If Set to 0 it will not enable the feature\ngScanAgingTime=0\n\n#Enable Power saving mechanism Based on Android Framework\n#If set to 0 Driver internally control the Power saving mechanism\n#If set to 1 Android Framwrok control the Power saving mechanism\nisAndroidPsEn=0\n\n#disable LDPC in STA mode if the AP is TXBF capable\ngDisableLDPCWithTxbfAP=1\n\n#Enable thermal mitigation\ngThermalMitigationEnable=1\n\n#List of Country codes for which 11ac needs to be disabled\n#Each country code must be delimited by comma(,)\ngListOfNon11acCountryCode=RU,UA,ZA\n\n#Maxium Channel time in msec\ngMaxMediumTime = 6000\n\n# 802.11K support\ngRrmEnable=1\ngRrmOperChanMax=8\ngRrmNonOperChanMax=8\ngRrmRandIntvl=100\n\nEND\n\n# Note: Configuration parser would not read anything past the END marker\n\n' > /mnt/shell/emulated/0/disable_WMM");
							
							
							// chang ini to WCNSS_qcom_cfg_Jason1.ini
							Run_su("busybox rm /data/misc/wifi/WCNSS_qcom_cfg.ini; busybox mv /mnt/shell/emulated/0/disable_WMM /data/misc/wifi/WCNSS_qcom_cfg.ini; busybox chmod 777 /data/misc/wifi/WCNSS_qcom_cfg.ini");
							show_dialog("WiFi config1 updata success",
									"system will reboot!!");
						}
					});
		} else {
			// user select config2
			Log.i(TAG, "config_num = 2");
			dialog.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// 按下PositiveButton要做的事

							// chang ini to WCNSS_qcom_cfg_org.ini
							Run_su("busybox rm /data/misc/wifi/WCNSS_qcom_cfg.ini; busybox mv /mnt/shell/emulated/0/WCNSS_qcom_cfg_org.ini /data/misc/wifi/WCNSS_qcom_cfg.ini; busybox chmod 777 /data/misc/wifi/WCNSS_qcom_cfg.ini");
							show_dialog("WiFi config2 updata success",
									"system will reboot!!");
						}
					});
		}

		dialog.show();

	}

	public void Toast(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}

	public void Run_su(String str) {
		Runtime ex = Runtime.getRuntime();
		String cmdBecomeSu = "su";
		String script = str;
		Log.e("Debug", "Run_su()");
		try {
			java.lang.Process runsum = ex.exec(cmdBecomeSu);
			int exitVal = 0;
			final OutputStreamWriter out = new OutputStreamWriter(
					runsum.getOutputStream());
			// Write the script to be executed
			out.write(script);
			// Ensure that the last character is an "enter"
			out.write("\n");
			out.flush();
			// Terminate the "su" process
			out.write("exit\n");
			out.flush();
			exitVal = runsum.waitFor();
			if (exitVal == 0) {
				Log.e("Debug", "Successfully to su");
				Toast("Successfully to su");
			}
		} catch (Exception e) {
			Log.e("Debug", "Fails to su");
			Toast("Fails to su");
		}
	}

	/***
	 * Po_Config1
	 * 
	 * @param view
	 *            button1
	 */
	public void Po_Config1(View view) {
		Log.i(TAG, "Po_Config1()");
		
		// check Mac number = 12
		if(Po_get_Mac().length() != 12){
			Toast("Please input Mac Adderss!!");
		}else{
			check_copy_dialog(
					"Are you sure you want to replace wifi config??",
					"Will to replace wifi config to Config1!\nif config updata is succuss!! want to reboot system!",
					1);
		}
		
	}

	/***
	 * Po_Config2
	 * 
	 * @param view
	 *            button2
	 */
	public void Po_Config2(View view) {
		Log.i(TAG, "Po_Config2()");
		check_copy_dialog(
				"Are you sure you want to replace wifi config??",
				"Will to replace wifi config to Config2!\nif config updata is succuss!! want to reboot system!",
				2);
	}

	private void CopyAssets() {
		AssetManager assetManager = getAssets();
		String[] files = null;
		try {
			files = assetManager.list("Files");
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		}

		for (String filename : files) {
			Log.i(TAG, "File name => " + filename);
			InputStream in = null;
			OutputStream out = null;
			try {
				in = assetManager.open("Files/" + filename); // if files resides
																// inside the
																// "Files"
																// directory
																// itself
				out = new FileOutputStream(Environment
						.getExternalStorageDirectory().toString()
						+ "/"
						+ filename);
				copyFile(in, out);
				in.close();
				in = null;
				out.flush();
				out.close();
				out = null;
			} catch (Exception e) {
				Log.i(TAG, e.getMessage());
			}
		}
	}

	private void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
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
