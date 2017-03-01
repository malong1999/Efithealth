package com.maxiaobu.healthclub.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.BaseFrg;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.ui.activity.ScanCodeActivity;
import com.maxiaobu.healthclub.utils.storage.SPUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 首页talk
 * maxiaobu 2016-9-5
 */
public class TalkFragment extends BaseFrg {


    @Bind(R.id.tv_title_common)
    TextView mTvTitleCommon;
    @Bind(R.id.toolbar_common)
    Toolbar mToolbarCommon;
    @Bind(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @Bind(R.id.tab_layout)
    TabLayout mTabLayout;
    @Bind(R.id.view_pager)
    ViewPager mViewPager;
    private View mRootView;
    /**
     * 单聊
     */
    private ConversationListFragment mConversationListFragment;
    /**
     * 群聊
     */
    private GroupListFragment mGroupListFragment;
    private ImageView mUnReadMsgSingle;
    private ImageView mUnReadMsgGroup;

    private Boolean mBoolean = false;

    public TalkFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_talk, container, false);
        ButterKnife.bind(this, mRootView);
        initView();
        initData();
        return mRootView;
    }

    @Override
    public void initView() {
        BaseAty activity = (BaseAty) getActivity();
        activity.setSupportActionBar(mToolbarCommon);
        mToolbarCommon.setTitle("");
        setHasOptionsMenu(true);
        if (mViewPager != null) {
            setupViewPager(mViewPager);
            mViewPager.setOffscreenPageLimit(1);
        }
        mTabLayout.setupWithViewPager(mViewPager);
        View singlerView = LayoutInflater.from(getContext()).inflate(R.layout.tab_singler, null);
        mUnReadMsgSingle = (ImageView) singlerView.findViewById(R.id.unread_msg_number);
        mTabLayout.getTabAt(0).setCustomView(singlerView);
        View groupView = LayoutInflater.from(getContext()).inflate(R.layout.tab_group, null);
        mUnReadMsgGroup = (ImageView) groupView.findViewById(R.id.unread_msg_number);
        mTabLayout.getTabAt(1).setCustomView(groupView);
        if (SPUtils.getBoolean(Constant.GROUP_CHAT, false) && mViewPager.getCurrentItem() != 1) {
            mUnReadMsgGroup.setVisibility(View.VISIBLE);
            SPUtils.putBoolean(Constant.GROUP_CHAT, false);
        } else {
            mUnReadMsgGroup.setVisibility(View.GONE);
        }

        if (SPUtils.getBoolean(Constant.CHAT, false) && mViewPager.getCurrentItem() != 0) {
            mUnReadMsgSingle.setVisibility(View.VISIBLE);
            SPUtils.putBoolean(Constant.CHAT, false);
        } else {
            mUnReadMsgSingle.setVisibility(View.GONE);
        }

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (position == 0) {

                    int conut = 0;
                    int times = 0;
                    mUnReadMsgSingle.setVisibility(View.GONE);
                    int unreadMsgCountTotal = EMClient.getInstance().chatManager().getUnreadMsgsCount();
                    if (unreadMsgCountTotal != 0) {
                        for (EMConversation conversation : EMClient.getInstance().chatManager().getAllConversations().values()) {
                            if (conversation.getType() == EMConversation.EMConversationType.GroupChat) {
                                for (int i = 0; i < App.getInstance().getGroupIdList().size(); i++) {
                                    if (!conversation.getUserName().equals(App.getInstance().getGroupIdList().get(i))) {
                                        if (i == App.getGroupIdList().size() - 1) {
                                            times = times + conversation.getUnreadMsgCount();
                                        }
                                    } else {
                                        conut = conut + conversation.getUnreadMsgCount();
                                        break;
                                    }
                                }
                            }
                        }
                        if (conut == (unreadMsgCountTotal - times)) {
                            mUnReadMsgGroup.setVisibility(View.VISIBLE);
                        }
                    }

                } else if (position == 1) {

                    mUnReadMsgGroup.setVisibility(View.GONE);
                    int conut = 0;
                    int unreadMsgCountTotal = EMClient.getInstance().chatManager().getUnreadMsgsCount();
                    if (unreadMsgCountTotal != 0) {
                        for (EMConversation conversation : EMClient.getInstance().chatManager().getAllConversations().values()) {
                            if (conversation.getType() == EMConversation.EMConversationType.Chat) {
                                conut = conut + conversation.getUnreadMsgCount();
                            }
                        }
                        if (conut == unreadMsgCountTotal) {
                            mUnReadMsgSingle.setVisibility(View.VISIBLE);
                        }
                    }

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void initData() {

    }

    public void showUnreadMsg(String type) {
        try {
            if (type.equals("Chat")) {
                if (mUnReadMsgSingle != null) {

                    if (mViewPager.getCurrentItem() != 0) {
                        mUnReadMsgSingle.setVisibility(View.VISIBLE);
                    }else {
                        mUnReadMsgSingle.setVisibility(View.GONE);
                    }

                } else {
                    SPUtils.putBoolean(Constant.CHAT, true);
                }
            } else if (type.equals("GroupChat")) {
                if (mUnReadMsgGroup != null) {
                    if (mViewPager.getCurrentItem() != 1) {
                        mUnReadMsgGroup.setVisibility(View.VISIBLE);
                    }else {
                        mUnReadMsgGroup.setVisibility(View.GONE);
                    }
                } else {
                    SPUtils.putBoolean(Constant.GROUP_CHAT, true);
                }
            } else if (mUnReadMsgGroup != null && mUnReadMsgSingle != null) {
                mUnReadMsgGroup.setVisibility(View.GONE);
                mUnReadMsgSingle.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * 创建菜单
     *
     * @param menu
     * @return
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_talk, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    /**
     * 创建菜单item点击事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.ic_search) {
//            saveImageToGallery(this,mBitmap);
            Toast.makeText(getActivity(), "待开发", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.scan_imageCode) {
            Intent intent = new Intent(getActivity(),
                    ScanCodeActivity.class);
            startActivityForResult(intent, 1);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 显示item中的图片；
     *
     * @param menu
     * @return
     */
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (menu != null) {
            if (menu.getClass() == MenuBuilder.class) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public static Fragment newInstance() {
        TalkFragment fragment = new TalkFragment();
        return fragment;
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getChildFragmentManager());
        mConversationListFragment = new ConversationListFragment();
        mGroupListFragment = new GroupListFragment();
        adapter.addFragment(mConversationListFragment);
        adapter.addFragment(mGroupListFragment);
        viewPager.setAdapter(adapter);
    }

    public ConversationListFragment getConversationListFragment() {
        return mConversationListFragment;
    }

    public GroupListFragment getGroupListFragment() {
        return mGroupListFragment;
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment) {
            mFragments.add(fragment);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

    }
}