package com.maxiaobu.healthclub.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by 莫小婷 on 2016/12/26.
 * 我的好友
 */
public class AdapterMyFriend extends FragmentPagerAdapter{
    private List<Fragment> mFragments;
    private String[] title = {"好友","关注","粉丝"};
    public AdapterMyFriend(FragmentManager fm) {
        super(fm);
    }
    public void setFragments(List<Fragment> fragments) {
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }
}
