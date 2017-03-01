package com.maxiaobu.healthclub.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.maxiaobu.healthclub.ui.activity.EnterDoorActivity;

/**
 * 门禁进门广播接受者
 */
public class EnterDoorReceiver extends BroadcastReceiver {
    public EnterDoorReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(
                "android.intent.action.MY_BROADCAST")) {
            Intent intent1 = new Intent(context, EnterDoorActivity.class);
            intent1.putExtra("pretCode", intent.getStringExtra("pretCode"));
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context  .startActivity(intent1);

        }
    }
}
