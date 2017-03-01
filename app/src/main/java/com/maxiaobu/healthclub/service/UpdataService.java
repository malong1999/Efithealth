package com.maxiaobu.healthclub.service;

import android.app.DownloadManager;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;

import com.maxiaobu.healthclub.receiver.DownloadCompleteReceiver;


/**
 * Created by 马小布 on 2017/1/18.
 * email：maxiaobu1216@gmail.com
 * 功能：版本更新服务
 * 伪码：
 * 待完成：
 */
public class UpdataService extends Service {
    /**
     * 安卓系统下载类
     **/
    DownloadManager manager;

    /**
     * 接收下载完的广播
     **/
    DownloadCompleteReceiver receiver;

    public UpdataService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 调用下载
        initDownManager(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if (receiver != null)
//            unregisterReceiver(receiver);
        super.onDestroy();
    }

    /**
     * 初始化下载器
     **/
    private void initDownManager(Intent intent) {
        manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        receiver = new DownloadCompleteReceiver();

        //设置下载地址
        DownloadManager.Request down = new DownloadManager.Request(
                Uri.parse(intent.getStringExtra("url")));
        down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
                | DownloadManager.Request.NETWORK_WIFI);
        // 下载时，通知栏显示途中
        /**
         *  request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
         *  表示下载进行中和下载完成的通知栏是否显示。
         *  默认只显示下载中通知。VISIBILITY_VISIBLE_NOTIFY_COMPLETED表示下载完成后显示通知栏提示。
         *  VISIBILITY_HIDDEN表示不显示任何通知栏提示，
         *  这个需要在AndroidMainfest中添加权限android.permission.DOWNLOAD_WITHOUT_NOTIFICATION.
         */
        down.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        /**
         * 显示下载界面
         */
        down.setVisibleInDownloadsUi(true);
//        设置下载后文件存放的位置,第三个参数设置下载后文件存放的路劲
        down.setDestinationInExternalFilesDir(this,
                Environment.DIRECTORY_DOWNLOADS, "efithealth.apk");
        // 将下载请求放入队列
        manager.enqueue(down);
        //注册下载广播
        registerReceiver(receiver, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        //停止服务并关闭广播
        UpdataService.this.stopSelf();
    }
}
