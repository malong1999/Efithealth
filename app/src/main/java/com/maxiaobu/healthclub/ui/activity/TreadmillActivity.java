package com.maxiaobu.healthclub.ui.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.adapter.AdapterTreadmilAty;
import com.maxiaobu.healthclub.utils.excel.MyHorizontalScrollView;
import com.maxiaobu.healthclub.utils.excel.OtherScroll;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 马小布 on 2017/2/18.
 * email：maxiaobu1216@gmail.com
 * 功能：跑步机
 * 伪码：
 * 待完成：没写完
 */
public class TreadmillActivity extends BaseAty implements OtherScroll {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.ctl_name)
    CollapsingToolbarLayout mCtlName;
    @Bind(R.id.title_lin)
    LinearLayout mTitleLin;
    @Bind(R.id.app_bar)
    AppBarLayout mAppBar;
    private LinearLayout title_lin;
    private ListView ver_list;
    private ArrayList<String> datas;
    private static ArrayList<MyHorizontalScrollView> mScrollLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treadmill);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    public void initView() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mCtlName.setTitle("sdlkfjlskdjf");


        mScrollLists = new ArrayList<>();
        title_lin = (LinearLayout) findViewById(R.id.title_lin);
        View v = (View) LayoutInflater.from(this).inflate(R.layout.hor_scroll_item, null);
//        v.setBackgroundColor(Color.RED);
        MyHorizontalScrollView mHorScrollView = (MyHorizontalScrollView) v.findViewById(R.id.hor_scrollview);
        mScrollLists.add(mHorScrollView);
        title_lin.addView(v);
        ver_list = (ListView) findViewById(R.id.ver_list);
        datas = new ArrayList<>();
        //假数据
        for (int i = 1; i < 32; i++) {
            datas.add(i + "");
        }
        AdapterTreadmilAty adapter = new AdapterTreadmilAty(this, datas, mScrollLists);
        ver_list.setAdapter(adapter);
    }

    @Override
    public void initData() {

    }

    //监听自定义MyHorizontalScrollView的滑动，对每一个MyHorizontalScrollView滑动都使其它联动
    @Override
    public void otherItemScroll(int l, int t, int oldl, int oldt) {
        for (MyHorizontalScrollView item : mScrollLists) {
            item.scrollTo(l, t);
        }
    }

}
