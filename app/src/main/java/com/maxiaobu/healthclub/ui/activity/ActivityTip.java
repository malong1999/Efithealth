package com.maxiaobu.healthclub.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.Bean_MessayTypeList;
import com.maxiaobu.healthclub.ui.fragment.WebFragment;
import com.maxiaobu.volleykit.RequestJsonListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 莫小婷 on 2017/1/6.
 * 文章
 */

public class ActivityTip extends BaseAty {


    @Bind(R.id.tab_layout)
    TabLayout mTabLayout;
    @Bind(R.id.view_pager)
    ViewPager mViewPager;
    @Bind(R.id.tv_title_common)
    TextView mTvTitleCommon;
    @Bind(R.id.toolbar_common)
    Toolbar mToolbarCommon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    public void initView() {
        setCommonBackToolBar(mToolbarCommon,mTvTitleCommon,"文章");
    }

    @Override
    public void initData() {
        App.getRequestInstance().post(this, UrlPath.URL_MESSAYTYPELIST,
                Bean_MessayTypeList.class, null, new RequestJsonListener<Bean_MessayTypeList>() {
                    @Override
                    public void requestSuccess(Bean_MessayTypeList result) {
                        if (mViewPager != null) {
                            Adapter adapter = new Adapter(getSupportFragmentManager());
                            for (int i = 0; i < result.getTagList().size(); i++) {
                                WebFragment webFragment = new WebFragment();
                                Bundle bundle = new Bundle();
                                bundle.putString("url", "file:///android_asset/essayList.html?tagid=" + result.getTagList().get(i).getEtid());
                                webFragment.setArguments(bundle);
                                adapter.addFragment(webFragment, result.getTagList().get(i).getEtname());
                            }

                            mViewPager.setAdapter(adapter);
                            mViewPager.setOffscreenPageLimit(1);

                        }
                        mTabLayout.setupWithViewPager(mViewPager);
                        mTabLayout.setTabTextColors(Color.WHITE, 0xffFb8435);
                    }

                    @Override
                    public void requestError(VolleyError volleyError, String s) {

                    }

                    @Override
                    public void noInternet(VolleyError volleyError, String s) {

                    }

                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
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
            return mFragmentTitles.get(position);
        }
    }
}
