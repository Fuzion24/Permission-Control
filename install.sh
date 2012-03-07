echo "Remount"
adb -d remount
echo "adb uninstall"
adb -d uninstall com.fuzionsoftware.appstats
echo "rm system app"
adb -d shell rm /system/app/appstats_signed.apk
echo "Sign Apk"
java -Xmx512M -jar ./signapk.jar  ./platform.x509.pem ./platform.pk8 ./PermissionControl.apk ./appstats_signed.apk
echo "Push sys app"
adb -d push ./appstats_signed.apk /system/app/