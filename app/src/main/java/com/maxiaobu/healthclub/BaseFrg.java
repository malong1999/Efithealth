package com.maxiaobu.healthclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by 马小布 on 2016/7/29.
 */
public abstract class BaseFrg extends android.support.v4.app.Fragment {
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        App.getRequestInstance().getRequestManager().cancelAll(this);
    }

    abstract public void initView();

    abstract public void initData();
}
