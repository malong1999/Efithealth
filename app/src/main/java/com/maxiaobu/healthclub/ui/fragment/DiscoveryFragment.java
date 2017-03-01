package com.maxiaobu.healthclub.ui.fragment;


import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.maxiaobu.healthclub.BaseFrg;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.adapter.AdapterDiscovery;
import com.maxiaobu.healthclub.common.beangson.BeanEventBas;
import com.maxiaobu.healthclub.ui.weiget.segmentcontrol.SegmentControl;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.R.attr.fragment;
import static com.maxiaobu.healthclub.R.id.dialog_no;
import static com.maxiaobu.healthclub.R.id.dialog_yes;
import static com.maxiaobu.healthclub.R.id.sv_recommend;
import static com.maxiaobu.healthclub.R.id.view;

/**
 * maxiaobu 2016-9-5
 * 发现页面
 */
public class DiscoveryFragment extends BaseFrg implements View.OnClickListener {
    @Bind(R.id.tv_title_common)
    TextView mTvTitleCommon;
    @Bind(R.id.toolbar_common)
    Toolbar mToolbarCommon;
    @Bind(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @Bind(R.id.discovery_tab_layout)
    TabLayout mDiscoveryTabLayout;
    @Bind(R.id.view_pager)
    ViewPager mViewPager;
    @Bind(R.id.discovery_filter)
    LinearLayout mDiscoveryFilter;

    private List<Fragment> mFragments;
    private AdapterDiscovery mAdapterDiscovery;
    private AlertDialog NearmDialog;
    private AlertDialog FriendmDialog;

    /**
     * 附近的人——筛选的身份
     */
    private String near_memrole;
    /**
     * 附近的人——筛选的性别
     */
    private String near_sex;
    private int near_memrole_index = 0;
    private int near_sex_index = 0;

    private String friend_memrole;
    private String friend_sex;
    private int friend_memrole_index = 0;
    private int friend_sex_index = 0 ;
    //只看私密标志
    private Boolean mPrivateFlag = false;

    private String[] memrole_List = new String[]{"全部","会员","教练","俱乐部"};
    private String[] sex_List = new String[]{"男","女"};

    private EventBus mEventBus;

    public DiscoveryFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_discovery, container, false);
        ButterKnife.bind(this, view);
        initView();
        initData();
        return view;
    }


    @Override
    public void initView() {

        mAdapterDiscovery = new AdapterDiscovery(getChildFragmentManager());
        mFragments = new ArrayList<>();
        mEventBus = EventBus.getDefault();
        near_memrole = "全部";
        near_sex = "男";
        friend_memrole = "全部";
        friend_sex = "男";
        mFragments.add(new NearFragment());
        mFragments.add(new DynamicFragment());
        mFragments.add(new HotDynamicFragment());
        mAdapterDiscovery.setFragments(mFragments);
        mViewPager.setAdapter(mAdapterDiscovery);
        mDiscoveryTabLayout.setupWithViewPager(mViewPager);
        mDiscoveryTabLayout.setTabTextColors(Color.WHITE, 0xffFb8435);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 2){
                    mDiscoveryFilter.setVisibility(View.GONE);
                }else {
                    mDiscoveryFilter.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mDiscoveryFilter.setOnClickListener(this);
        if (article == 1)
            mViewPager.setCurrentItem(2);
    }

    private AlertDialog creatNearDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_discovery_near,null);
        TextView dialog_pos = (TextView) view.findViewById(R.id.dialog_pos);
        TextView dialog_cancle = (TextView) view.findViewById(R.id.dialog_cancle);
        SegmentControl segmentControl_memrole = (SegmentControl) view.findViewById(R.id.seg_control_memrole);
        SegmentControl segmentControl_sex = (SegmentControl) view.findViewById(R.id.seg_control_sex);
        segmentControl_memrole.setOnSegmentControlClickListener(index -> {
            near_memrole_index = index;
        });
        segmentControl_sex.setOnSegmentControlClickListener(index -> {
            near_sex_index = index;
        });

        switch (near_memrole){
            case "全部":
                segmentControl_memrole.setSelectedIndex(0);
                near_memrole_index = 0;
                break;
            case "会员":
                segmentControl_memrole.setSelectedIndex(1);
                near_memrole_index = 1;
                break;

            case "教练":
                segmentControl_memrole.setSelectedIndex(2);
                near_memrole_index = 2;
                break;

            case "俱乐部":
                segmentControl_memrole.setSelectedIndex(3);
                near_memrole_index = 3;
                break;
        }

        switch (near_sex){
            case "男":
                segmentControl_sex.setSelectedIndex(0);
                near_sex_index = 0;
                break;

            case "女":
                segmentControl_sex.setSelectedIndex(1);
                near_sex_index = 1;
                break;
        }

        dialog_pos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshUI(near_memrole_index,near_sex_index);
            }
        });
        dialog_cancle.setOnClickListener(this);

        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    private void refreshUI(int near_memrole_index, int near_sex_index) {
        near_memrole = memrole_List[near_memrole_index];
        near_sex = sex_List[near_sex_index];
        // TODO: 2016/12/23 附近的人筛选网络请求
        NearmDialog.dismiss();
    }

    private AlertDialog creatDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_discovery,null);
        TextView dialog_yes = (TextView) view.findViewById(R.id.dialog_yes);
        TextView dialog_no = (TextView) view.findViewById(R.id.dialog_no);
        SegmentControl segmentControl_memrole = (SegmentControl) view.findViewById(R.id.segment_control_memrole);
        SegmentControl segmentControl_sex = (SegmentControl) view.findViewById(R.id.segment_control_sex);
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.dialog_check_box);
        if(mPrivateFlag){
            checkBox.setChecked(true);
        }else {
            checkBox.setChecked(false);
        }
        checkBox.setOnClickListener(view1 -> {
            if(mPrivateFlag){
                checkBox.setChecked(false);
                mPrivateFlag = false;
            }else {
                checkBox.setChecked(true);
                mPrivateFlag = true;
            }
        });

        segmentControl_memrole.setOnSegmentControlClickListener(index -> {
            friend_memrole_index = index;
        });
        segmentControl_sex.setOnSegmentControlClickListener(index -> {

            friend_sex_index = index;
        });

        switch (friend_memrole){
            case "全部":
                segmentControl_memrole.setSelectedIndex(0);
                friend_memrole_index = 0;
                break;

            case "会员":
                segmentControl_memrole.setSelectedIndex(1);
                friend_memrole_index = 1;
                break;

            case "教练":
                segmentControl_memrole.setSelectedIndex(2);
                friend_memrole_index = 2;
                break;

            case "俱乐部":
                segmentControl_memrole.setSelectedIndex(3);
                friend_memrole_index = 3;
                break;
        }

        switch (friend_sex){
            case "男":
                segmentControl_sex.setSelectedIndex(0);
                friend_sex_index = 0;
                break;

            case "女":
                segmentControl_sex.setSelectedIndex(1);
                friend_sex_index = 1;
                break;
        }
        dialog_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshFriendUI(friend_memrole_index,friend_sex_index);
            }
        });
        dialog_no.setOnClickListener(this);

        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    private void refreshFriendUI(int friend_memrole_index, int friend_sex_index) {
        friend_memrole = memrole_List[friend_memrole_index];
        friend_sex = sex_List[friend_sex_index];
        int visiable;
        if(mPrivateFlag){
            visiable = 2;
        }else {
            visiable = 1;
        }
        BeanEventBas beanEventBas = new BeanEventBas();
        beanEventBas.setFriendRefresh(visiable);
        mEventBus.post(beanEventBas);
        FriendmDialog.dismiss();
    }

    @Override
    public void initData() {


    }


    public static Fragment newInstance() {
        DiscoveryFragment fragment = new DiscoveryFragment();
        return fragment;
    }

    /**
     * 跳转到文章页面，并且viewPager滑动
     */
    private static int article ;
    public static Fragment newInstanceArticle() {
        article=1;
        DiscoveryFragment fragment = new DiscoveryFragment();
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.discovery_filter:
                if(mViewPager.getCurrentItem() == 0){
                    NearmDialog = creatNearDialog();
                    NearmDialog.show();
                }else {
                    FriendmDialog = creatDialog();
                    FriendmDialog.show();
                }
                break;

            case dialog_no:
                FriendmDialog.dismiss();
                break;

            case R.id.dialog_cancle:

                NearmDialog.dismiss();
                break;
        }
    }


}
