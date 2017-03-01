package com.maxiaobu.healthclub.ui.activity;


import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.utils.storage.SPUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
/**
 * Created by 马小布 on 2016/12/26.
 * email：maxiaobu1216@gmail.com
 * 功能：进门确认
 * 伪码：
 * 待完成：
 */
public class EnterDoorActivity extends BaseAty {
    @Bind(R.id.iv_enter_door)
    ImageView mIvEnterDoor;
    @Bind(R.id.activity_enter_door)
    RelativeLayout mActivityEnterDoor;
    @Bind(R.id.tv_success)
    TextView mTvSuccess;
    @Bind(R.id.btn_complete)
    Button mBtnComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_door);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    public void initView() {
        String pretCode = getIntent().getStringExtra("pretCode");
        mIvEnterDoor.setImageResource(R.mipmap.ic_enter_door);

       if (pretCode.equals("1")) {
           SPUtils.putBoolean(Constant.foorBOOLEAN, true);
           mIvEnterDoor.setImageResource(R.mipmap.ic_enter_door);

           SPUtils.putLong(Constant.foorTime, System.currentTimeMillis());
           App.getInstance().moxiaoting();
        } else {
            mIvEnterDoor.setImageResource(R.mipmap.ic_unenter_door);
            mTvSuccess.setText("扫码失败");
            mBtnComplete.setBackgroundColor(Color.RED);
        }
        mBtnComplete.setOnClickListener(v -> EnterDoorActivity.this.finish());
    }

    @Override
    public void initData() {

    }
}
