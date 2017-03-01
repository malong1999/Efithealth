package com.maxiaobu.healthclub.ui.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maxiaobu.healthclub.BaseFrg;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.beangson.BeanClubData;
import com.maxiaobu.healthclub.ui.weiget.toolsbar.WrapContentHeightViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 俱乐部的个人页
 */
public class ClubDataFragment extends BaseFrg {
    @Bind(R.id.tv_club_name)
    TextView mTvClubName;
    @Bind(R.id.tv_user_name)
    TextView mTvUserName;
    @Bind(R.id.tv_user_sign)
    TextView mTvUserSign;
    @Bind(R.id.tv_user_sex)
    TextView mTvUserSex;
    @Bind(R.id.tv_user_local)
    TextView mTvUserLocal;
    @Bind(R.id.tv_user_mem)
    TextView mTvUserMem;
    @Bind(R.id.tv_user_birth)
    TextView mTvUserBirth;
    @Bind(R.id.tv_user_phone)
    TextView mTvUserPhone;
    @Bind(R.id.layout_phone)
    LinearLayout mLayoutPhone;


    public static Fragment getInstance() {
        ClubDataFragment instance = new ClubDataFragment();
        return instance;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_club_data, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void initView() {

    }

    @Subscribe
    public void getValuse(BeanClubData data) {
        if (null != data) {
            mTvUserName.setText(data.getName());
            if(data.getSign().equals("") || data.getSign() != null){
                mTvUserSign.setText("暂无签名");
            }else {
                mTvUserSign.setText(data.getSign());
            }
            mTvUserSex.setText(data.getSex());
            mTvUserLocal.setText(data.getLocal());
            mTvUserMem.setText(data.getMen());
            mTvUserBirth.setText(data.getBirth());
            mTvClubName.setText(data.getClub());
            mTvUserPhone.setText(data.getMobPhone());

            mLayoutPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(null != mTvUserPhone.getText().toString()   && !mTvUserPhone.getText().toString().equals("")){
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+mTvUserPhone.getText().toString()));
                        startActivity(intent);
                    }
                }
            });
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }
}
