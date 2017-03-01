package com.maxiaobu.healthclub.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 莫小婷 on 2017/1/3.
 */

public class SendDyniamicPremissions extends BaseAty implements View.OnClickListener {

    @Bind(R.id.tv_title_common)
    TextView mTvTitleCommon;
    @Bind(R.id.toolbar_common)
    Toolbar mToolbarCommon;
    @Bind(R.id.premissions_public_checkbox)
    CheckBox mPremissionsPublicCheckbox;
    @Bind(R.id.premissions_public)
    RelativeLayout mPremissionsPublic;
    @Bind(R.id.premissions_friend_checkbox)
    CheckBox mPremissionsFriendCheckbox;
    @Bind(R.id.premissions_friend)
    RelativeLayout mPremissionsFriend;
    @Bind(R.id.premissions_mine_checkbox)
    CheckBox mPremissionsMineCheckbox;
    @Bind(R.id.premissions_mine)
    RelativeLayout mPremissionsMine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_premissions);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    public void initView() {
        setCommonBackToolBar(mToolbarCommon,mTvTitleCommon,"选择范围");
        String text = getIntent().getStringExtra("premissions");
        if (text.equals("公开")){
            mPremissionsPublicCheckbox.setChecked(true);
        }else if(text.equals("好友")){
            mPremissionsFriendCheckbox.setChecked(true);
        }else {
            mPremissionsMineCheckbox.setChecked(true);
        }
        mToolbarCommon.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SendDyniamicPremissions.this,SendDynamicActivity.class);
                if(mPremissionsPublicCheckbox.isChecked()){
                    intent.putExtra("text","公开");
                }else if(mPremissionsMineCheckbox.isChecked()){
                    intent.putExtra("text","仅自己可见");
                }else {
                    intent.putExtra("text","好友");
                }
                setResult(SendDynamicActivity.SEND_DYNAMIC_PREMISSIONS,intent);
                finish();
            }
        });
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.premissions_public,R.id.premissions_friend,R.id.premissions_mine})
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.premissions_public:
                mPremissionsPublicCheckbox.setChecked(true);
                mPremissionsMineCheckbox.setChecked(false);
                mPremissionsFriendCheckbox.setChecked(false);
                break;
            case R.id.premissions_mine:
                mPremissionsMineCheckbox.setChecked(true);
                mPremissionsPublicCheckbox.setChecked(false);
                mPremissionsFriendCheckbox.setChecked(false);
                break;
            case R.id.premissions_friend:
                mPremissionsMineCheckbox.setChecked(false);
                mPremissionsPublicCheckbox.setChecked(false);
                mPremissionsFriendCheckbox.setChecked(true);
                break;
        }
    }
}
