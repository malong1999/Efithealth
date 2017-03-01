package com.maxiaobu.healthclub.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.adapter.AdapterMyFriend;
import com.maxiaobu.healthclub.ui.fragment.GoodFriendFragment;
import com.maxiaobu.healthclub.ui.fragment.MyAttentionFragment;
import com.maxiaobu.healthclub.ui.fragment.MyFansFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 马小布 on 2017/2/20.
 * email：maxiaobu1216@gmail.com
 * 功能：好友列表
 * 伪码：
 * 待完成：
 */
public class MyFriendActivity extends BaseAty {

    @Bind(R.id.tv_title_common)
    TextView mTvTitleCommon;
    @Bind(R.id.toolbar_common)
    Toolbar mToolbarCommon;
    @Bind(R.id.tab_layout)
    TabLayout mTabLayout;
    @Bind(R.id.view_pager)
    ViewPager mViewPager;

    private List<Fragment> mFragments;
    private AdapterMyFriend mAdapterMyFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_friend);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    public void initView() {
        setCommonBackToolBar(mToolbarCommon, mTvTitleCommon, "我的好友");
        mAdapterMyFriend = new AdapterMyFriend(getSupportFragmentManager());
        mFragments = new ArrayList<>();
        mFragments.add(new GoodFriendFragment());
        mFragments.add(new MyAttentionFragment());
        mFragments.add(new MyFansFragment());
        mAdapterMyFriend.setFragments(mFragments);
        mViewPager.setAdapter(mAdapterMyFriend);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabTextColors(Color.WHITE,0xffFb8435);
    }

    @Override
    public void initData() {

    }
}
