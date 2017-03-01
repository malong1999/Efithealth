package com.maxiaobu.healthclub.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by 莫小婷 on 2016/12/9.
 * 发现页面
 */
public class AdapterDiscovery extends FragmentPagerAdapter{
    private List<Fragment> mFragments;
    private String[] title = {"附近的人","好友动态","广场"};

    public void setFragments(List<Fragment> fragments) {
        mFragments = fragments;
    }

    public AdapterDiscovery(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position) == null ? null : mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size() == 0 ? 0 : mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }
}
