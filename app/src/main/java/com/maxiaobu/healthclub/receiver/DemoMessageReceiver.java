package com.maxiaobu.healthclub.receiver;

import android.content.Context;

import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

/**
 * Created by 莫小婷 on 2016/11/15.
 */

public class DemoMessageReceiver extends PushMessageReceiver {

    /**
     * 用来接收服务器发送的透传消息
     * @param context
     * @param message
     */
    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage message) {

    }

    /**
     * 用来接收服务器发来的通知栏消息（用户点击通知栏时触发）
     * @param context
     * @param message
     */
    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage message) {

    }


    /**
     * 用来接收服务器发来的通知栏消息（消息到达客户端时触发，并且可以接收应用在前台时不弹出通知的通知消息）
     * @param context
     * @param message
     */
    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage message) {

    }

    /**
     * 用来接收客户端向服务器发送命令消息后返回的响应
     * @param context
     * @param message
     */
    @Override
    public void onCommandResult(Context context, MiPushCommandMessage message) {

    }

    /**
     * 用来接受客户端向服务器发送注册命令消息后返回的响应。
     * @param context
     * @param message
     */
    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage message) {

    }

}
