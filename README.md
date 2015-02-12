# This is my README
#### 1. Flash_Ethernet_Mac<br /> 
#### description:<br />
this APP can flash Ethernet(need Root permission)

2. MainMenu<br />
description: this APP is a Android RS232 example

3. Test_NFC<br />
description: this APP is a Android NFC, how to write example

4. install_del_APP<br />
description: this APP can remove /system/ 下面某某folder(need Root permission)

5. install_recovery<br />
description: this APP can copy update.zip on USB disk 或 SD Card, last copy update.zip to /cash/recovery/ , 達成 recovery function(IMT8)

6. 
name: IQ8_install_recovery
description: 可以拷貝USB碟或SD卡中的IQ8_update.zip到data/recovery_IQ8/下, 接著透過recovery mode就可以去選取此檔案, 達到還原image的效果

7. 
name: nfc-reader2
description: 讀取NFC資料的APP

8. 
name: RS232Beeper
description: this APP can read RS232, if read 到 RS232 data 就會Beep一聲

9. test_serial_BT_Symbol與test_serial_BT2_Symbol
這隻APP是是RS2322的Bluetooth程式, 此模組傳輸距離可以傳1KM(經過測試超過一公里), 主要是負責當連線方, AP端如果有啟動, client按下link按鈕就能夠連線並傳送文字了 

10. 
name: IMT8_CarriersWidget
description: 由於MTK code base中沒有rild的code, 因此無法透過正規方式將電信業者與3G/4G符號更新到System UI, 所以在此透過此widget來讀取ublox 3G/4G 模組的電信商家and 3G/4G符號, 目前SIM Card info都會存在/storage/sdcard0下, 未來有時間在將此資料強至顯示到system UI上

11. 
name: GPS_example
description: this is a GPS example, 跟一般example 不同的是 this APP can read NMEA data, 除此以外當然也有包含 Google API 提供的 function

12. 
name: Ctrl_Power_widget
description: this APP is a Android widget, can through button turn on/off WiFi/Bluetooth/GPS/3G, P.S not custom use

13. 
name: IMT8_delete_Root
description: 由於 customers want delete Root function on IMT8, so this APP can remove Root, Mainly because remove /system/app/superuser.apk

14. 
name: PoTestJNI
description: this APP is a JNI example,(not custom use), Mainly for me use 

15. 
name: PoJNIReadWriteProc
description: this APP is a JNI read/write /proc/XXX example( R & W /proc/Po_value),(not custom use), mainly for me use

16. 
name: ThermalPrinterWiFiSwitch
description: this APP integrate Thermal Printer sample code, can turn on/off WiFi/BT/GPS/Thermal and use Thermal Printer 
