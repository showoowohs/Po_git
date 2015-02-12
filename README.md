# This is my README
1. Flash_Ethernet_Mac:          
可以燒錄Ethernet的Android APP(需Root)

2.MainMenu:
Android RS232 的APP範例

3. Test_NFC
Android 的NFC寫入範例

4. install_del_APP
可以刪除/system/下面某某資料夾的APP(需Root)

5. install_recovery
可以拷貝USB碟或SD卡中的update.zip到/cash/recovery裡面的APP
目的是要做OTA還原

6. IQ8_install_recovery
可以拷貝USB碟或SD卡中的IQ8_update.zip到data/recovery_IQ8/下, 接著透過recovery mode就可以去選取此檔案, 達到還原image的效果

7. nfc-reader2
讀取NFC資料的APP

8. RS232Beeper
讀取RS232的APP, 讀到RS232資料就會Beep一聲

9. test_serial_BT_Symbol與test_serial_BT2_Symbol
這隻APP是是RS2322的Bluetooth程式, 此模組傳輸距離可以傳1KM(經過測試超過一公里), 主要是負責當連線方, AP端如果有啟動, client按下link按鈕就能夠連線並傳送文字了 

10. 增加IMT8_CarriersWidget程式, 由於MTK code base中沒有rild的code, 因此無法透過正規方式將電信業者與3G/4G符號更新到System UI, 所以在此透過此widget來讀取ublox 3G/4G 模組的電信商家and 3G/4G符號, 目前SIM Card info都會存在/storage/sdcard0下, 未來有時間在將此資料強至顯示到system UI上

11. increase GPS_example project, this is a GPS example, 跟一般example 不同的是 this APP can read NMEA data, 除此以外當然也有包含 Google API 提供的 function

12. increase Ctrl_Power_widget project, this APP is a Android widget, can through button turn on/off WiFi/Bluetooth/GPS/3G, P.S not custom use

13. increase IMT8_delete_Root project, 由於 customers want delete Root function on IMT8, so this APP can remove Root, Mainly because remove /system/app/superuser.apk

14. increase PoTestJNI project, this APP is a JNI example,(not custom use), Mainly for me use 

15. increase PoJNIReadWriteProc project, this APP is a JNI read/write /proc/XXX example( R & W /proc/Po_value),(not custom use), mainly for me use

16. increase ThermalPrinterWiFiSwitch project, this APP integrate Thermal Printer sample code, can turn on/off WiFi/BT/GPS/Thermal and use Thermal Printer 
