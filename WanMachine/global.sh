#!/system/bin/sh
echo "Internationalization in progress..."
echo -e '\x35''\x31''\x30''\x30''\x31' | dd of=/dev/block/mmcblk0 bs=1 seek=797443075 count=5
sleep 10
reboot recovery