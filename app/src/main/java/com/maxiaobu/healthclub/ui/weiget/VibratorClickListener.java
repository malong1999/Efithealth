package com.maxiaobu.healthclub.ui.weiget;

import android.app.Activity;
import android.view.View;

import com.maxiaobu.healthclub.utils.VibratorUtil;

/**
 * Created by 马小布 on 2016/10/7.
 */

public  class VibratorClickListener implements View.OnClickListener {

    @Override
    public void onClick(View v) {
        VibratorUtil.Vibrate((Activity) v.getContext(),30);
    }
}
