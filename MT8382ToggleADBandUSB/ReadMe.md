### Test Environment
Devices: MT8382
OS: android 4.4.2
---

### Remark:
>This APP through JIN echo ShowMac/FlashMacSD/ClearMac to /proc/eth0, can flash/clear Ethernet Mac address 
>
> 1. echo help > /proc/USBStatus
> 2. echo USBON > /proc/USBStatus                               (USB Power on, will toggle usb mode, can use usb device)
> 3. echo USBOFF > /proc/USBStatus                              (USB Power off, will toggle otg mode, can use otg usb port)
> 4. echo KILLPID (my_pid) > /proc/USBStatus                    (can kill PID through JNI)
> 5. echo CHMOD (premission) (device_note)+ > /proc/USBStatus   (can chmod device_note through JNI)
> 6. echo RMMOD (module_name)+ > /proc/USBStatus                (can rmmod driver through JNI)
> 7. echo INSMOD (module_path)+ > /proc/USBStatus               (can insmod driver through JNI)

---

### result picture:
> 1. How to enable OTG power
![alt tag](https://github.com/showoowohs/Po_git/blob/master/MT8382ToggleADBandUSB/demo/enable_otg_power_2.gif)

> 2. How to enable USB power(toggle to USB host, can use mouse, keyboard e.t.c devices)
![alt tag](https://github.com/showoowohs/Po_git/blob/master/MT8382ToggleADBandUSB/demo/enable_usb_power.gif)

---

### maintain
> 2015/09/25 fix USB NFC and USB GPS question(toggle otg/usb mode after NFC/GPS can not work) 
