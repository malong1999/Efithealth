package com.maxiaobu.healthclub.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by 马小布 on 2016/12/3.
 */

public class SystemUtils {
    public static String ip2long(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
            if (!wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(true);
            }
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ip = wifiInfo.getIpAddress();
            long iplong = HealthUtil.ipStrToLong(String.valueOf(HealthUtil.int2ip(ip)));
            return String.valueOf(iplong);
        } catch (Exception e) {

        }
        return null;
    }
}
