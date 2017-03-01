package com.maxiaobu.healthclub.ui.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.google.gson.JsonObject;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.EMLog;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.chat.Constant;
import com.maxiaobu.healthclub.chat.DemoHelper;
import com.maxiaobu.healthclub.chat.db.InviteMessgeDao;
import com.maxiaobu.healthclub.chat.db.UserDao;
import com.maxiaobu.healthclub.chat.runtimepermissions.PermissionsManager;
import com.maxiaobu.healthclub.chat.runtimepermissions.PermissionsResultAction;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanGroupList;
import com.maxiaobu.healthclub.common.beangson.BeanMe;
import com.maxiaobu.healthclub.common.beangson.BeanUpdata;
import com.maxiaobu.healthclub.service.UpdataService;
import com.maxiaobu.healthclub.ui.fragment.ConversationListFragment;
import com.maxiaobu.healthclub.ui.fragment.DiscoveryFragment;
import com.maxiaobu.healthclub.ui.fragment.GroupListFragment;
import com.maxiaobu.healthclub.ui.fragment.HomeFragment;
import com.maxiaobu.healthclub.ui.fragment.MineFragment;
import com.maxiaobu.healthclub.ui.fragment.TalkFragment;
import com.maxiaobu.healthclub.utils.HealthUtil;
import com.maxiaobu.healthclub.utils.TimesUtil;
import com.maxiaobu.healthclub.utils.rx.RxBus;
import com.maxiaobu.healthclub.utils.storage.SPUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import maxiaobu.easeui.domain.EaseUser;
import maxiaobu.mqldialoglibrary.dialog.MQLLoadingFragment;
import maxiaobu.mqldialoglibrary.materialdialogs.core.MaterialDialog;
import maxiaobu.mxbutilscodelibrary.NetworkUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 马小布 on 2016/12/13.
 * introduction：我长得真他娘的磕碜，单身未娶，求包养
 * email：maxiaobu1216@gmail.com
 * 功能：应用主界面
 * 伪码：
 * 待完成：
 *
 */
public class HomeActivity extends BaseAty {
    @Bind(R.id.bottom_navigation_bar)
    public BottomNavigationBar mBottomNavigationBar;
    @Bind(R.id.fragment_container)
    FrameLayout mFragmentContainer;
    @Bind(R.id.tv_point)
    TextView mTvPoint;
    @Bind(R.id.ly_root)
    RelativeLayout mLyRoot;
    private Fragment nowFragment = new Fragment();
    public Fragment mHomeFragment;
    private Fragment mTalkFragment;
    private Fragment mDiscoveryFragment;
    private Fragment mMineFragment;

    /**
     * 用户在其他设备登录
     */
    public boolean isConflict = false;
    /**
     * 用户账户被移除
     */
    private boolean isCurrentAccountRemoved = false;
    /**
     * 在其他设备登录 dialog
     */
    private AlertDialog.Builder conflictBuilder;
    /**
     * 账户移除 dialog
     */
    private AlertDialog.Builder accountRemovedBuilder;
    /**
     * 在其他设备登录 dialog显示中
     */
    private boolean isConflictDialogShow = false;
    /**
     * 如果账户移除 dialog显示中
     */
    private boolean isAccountRemovedDialogShow;
    /**
     * false：当前为首页 true:当前非首页
     */
    private boolean isHomePage = false;
    /**
     * 是否可以退出应用
     */
    private boolean isExit = true;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = true;
        }
    };
    /**
     * 消息数据库
     */
    private InviteMessgeDao inviteMessgeDao;
    /**
     * 会话列表frg
     */
    private ConversationListFragment conversationListFragment;

    /**
     * 群列表fragment
     */
    private GroupListFragment mGroupListFragment;
    /**
     * 应用内广播管理者
     */
    private LocalBroadcastManager broadcastManager;
    private BroadcastReceiver broadcastReceiver;
    private RxBus mRxBus;
    public LocationClient mLocationClient;
    public MyLocationListener myListener;
    private BeanMe mBeanMe;
    private MQLLoadingFragment mLoadingFragment;

    public static class TapEvent {
        BeanMe beanMine;
        public void setBeanMine(BeanMe beanMine) {
            this.beanMine = beanMine;
        }
        public BeanMe getBeanMine() {
            return beanMine;
        }
    }

    public static class ChangeEvent {
        BeanMe beanMine;

        public void setBeanMine(BeanMe beanMine) {
            this.beanMine = beanMine;
        }

        public BeanMe getBeanMine() {
            return beanMine;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.getBoolean(Constant.ACCOUNT_REMOVED, false)) {
            //账户移除
            DemoHelper.getInstance().logout(false, null);
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        } else if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false)) {
            //在其他设备登录
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        // 全部运行权限
        requestPermissions();
        try {
            initView();
            if (NetworkUtils.isConnected(mActivity)) {
                initData();
            } else {
                HealthUtil.showNONetworkDialog(mActivity);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        group();
    }

    private void group() {
        //屏蔽群代码
        new Thread(() -> {
            List<EMGroup> grouplist = new ArrayList<>();
            try {
                //从服务器获取自己加入的和创建的群组列表，此api获取的群组sdk会自动保存到内存和db。
                grouplist = EMClient.getInstance().groupManager().getJoinedGroupsFromServer();//需异步处理
            } catch (HyphenateException e) {
                e.printStackTrace();
            }
            if(grouplist.size() == 0){
                //从本地加载群组列表
                grouplist = EMClient.getInstance().groupManager().getAllGroups();
            }
            for (EMGroup emGroup : grouplist) {
                for (int j = 0; j < App.getGroupIdList().size(); j++) {
                    if(emGroup.getGroupId().equals(App.getGroupIdList().get(j))){
                        try {
                            //不屏蔽群，解除群屏蔽情况
                            EMClient.getInstance().groupManager().unblockGroupMessage(emGroup.getGroupId());//需异步处理
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                        }
                        break;
                    }else if (j == App.getGroupIdList().size() -1){
                        try {
//                                Log.d("GroupListFragment", emGroup.getGroupId());
                            EMClient.getInstance().groupManager().blockGroupMessage(emGroup.getGroupId());
                        } catch (HyphenateException e) {
                            e.printStackTrace();
//                                Log.d("GroupListFragment", "群主");
                        }
                    }

                }
            }
        }).start();
    }

    @Override
    public void initView() {
        if (getIntent().getBooleanExtra(Constant.ACCOUNT_CONFLICT, false) && !isConflictDialogShow) {
            //处理在其他设备登录
            showConflictDialog();
        } else if (getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false) && !isAccountRemovedDialogShow) {
            //如果账户移除
            showAccountRemovedDialog();
        }
        mBottomNavigationBar
                .setMode(BottomNavigationBar.MODE_FIXED)
                .setActiveColor(R.color.colorOrange)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        mBottomNavigationBar.addItem(new BottomNavigationItem(R.mipmap.ic_home_home, "首页"))
                .addItem(new BottomNavigationItem(R.mipmap.ic_home_message, "Talk"))
                .addItem(new BottomNavigationItem(R.mipmap.ic_home_discover, "发现"))
                .addItem(new BottomNavigationItem(R.mipmap.ic_home_mine, "我的"))
                .initialise();

        mBottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                switchFragment(position);
            }

            @Override
            public void onTabUnselected(int position) {
            }

            @Override
            public void onTabReselected(int position) {
            }
        });
        mHomeFragment = HomeFragment.newInstance();
        switchContent(nowFragment, mHomeFragment);
        isHomePage = true;

        inviteMessgeDao = new InviteMessgeDao(this);
        registerBroadcastReceiver();
    }

    @Override
    public void initData() {
        DemoHelper.isFistLaunch = false;
        mRxBus = App.getRxBusSingleton();
        checkVersion();
        Observable<BeanMe> beanMineObservable = App.getRetrofitUtil().getRetrofit().getBeanMe(SPUtils.getString(com.maxiaobu.healthclub.common.Constant.MEMID));
        beanMineObservable.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Subscriber<BeanMe>() {
                    @Override
                    public void onCompleted() {
                        Log.d("HomeActivity", "lfdkjg.log");
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(BeanMe beanMine) {
                        BeanMe.MemberBean member = beanMine.getMember();
                        SPUtils.putString(com.maxiaobu.healthclub.common.Constant.REC_ADDRESS, member.getRecaddress());
                        SPUtils.putString(com.maxiaobu.healthclub.common.Constant.REC_NAME, member.getRecname());
                        SPUtils.putString(com.maxiaobu.healthclub.common.Constant.REC_PHONE, member.getRecphone());
                        SPUtils.putString(com.maxiaobu.healthclub.common.Constant.NICK_NAME, member.getNickname());//用户名
                        SPUtils.putString(com.maxiaobu.healthclub.common.Constant.AVATAR, member.getImgsfilename());//用户头像
                        SPUtils.putString(com.maxiaobu.healthclub.common.Constant.SIGNSTATUS, beanMine.getSignstatus());//签到状态
                        SPUtils.putString(com.maxiaobu.healthclub.common.Constant.MEMROLE, member.getMemrole());//用户身份
                        String birthday = String.valueOf(beanMine.getMember().getBirthday().getTime());
                        SPUtils.putString(com.maxiaobu.healthclub.common.Constant.BRITHDAY, TimesUtil.timestampToStringS(birthday, "yyyy-MM-dd"));
                        SPUtils.putString(com.maxiaobu.healthclub.common.Constant.SIGNSTATUS, beanMine.getSignstatus());//签到状态
                        DemoHelper.getInstance().getUserProfileManager().updateCurrentUserNickName(member.getNickname());
                        DemoHelper.getInstance().getUserProfileManager().setCurrentUserAvatar(member.getImgsfilename());
                    }
                });
        beanMineObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BeanMe>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        mLoadingFragment = MQLLoadingFragment.getInstance(6,
                                4.0f,
                                false,
                                false, false, false);
                        mLoadingFragment.show(getSupportFragmentManager(), "1");
                    }

                    @Override
                    public void onCompleted() {
                        if (mLoadingFragment != null)
                            mLoadingFragment.dismiss();

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mLoadingFragment != null)
                            mLoadingFragment.dismiss();
                        Snackbar.make(mLyRoot, "mme.do在homeActivity中：" + e.toString(), 10000)
                                .setActionTextColor(Color.parseColor("#Fb8435"))
                                .setAction("确定", v -> {
                                }).show();
                    }

                    @Override
                    public void onNext(BeanMe beanMe) {
                        mBeanMe = beanMe;
                        TapEvent tapEvent = new TapEvent();
                        tapEvent.setBeanMine(beanMe);
                        if (mRxBus.hasObservers()) {
                            mRxBus.send(tapEvent);
                        }
                    }
                });
        refreshGroupId(false);
        mLocationClient = new LocationClient(App.getInstance());
        myListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myListener);
        mLocationClient.setLocOption(App.getInstance().getLocationOption());
        mLocationClient.start();
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        synchronized (conversations) {
            UserDao dao = new UserDao(App.getInstance());
            for (final EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    String hxIdFrom = conversation.getUserName();
                    if (hxIdFrom.substring(0, 1).equals("m")) {
                        Observable<BeanMe> beanMeObservable = App.getRetrofitUtil().getRetrofit().getBeanMe(hxIdFrom.toUpperCase());
                        beanMeObservable
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<BeanMe>() {
                                    @Override
                                    public void onCompleted() {
                                        Log.d("HomeActivity", "onCompleted");
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Log.d("HomeActivity", "mme.do在homeActivity中：" + e.toString());
                                        Snackbar.make(mLyRoot, "mme.do在homeActivity中：" + e.toString(), 10000)
                                                .setAction("确定", v -> {
                                                }).show();
                                    }

                                    @Override
                                    public void onNext(BeanMe result) {
                                        String avatar = result.getMember().getImgsfilename();
                                        String userName = result.getMember().getNickname();
                                        EaseUser easeUser = new EaseUser(hxIdFrom);
                                        easeUser.setAvatar(avatar);
                                        easeUser.setNick(userName);
                                        // 存入内存
                                        Map<String, EaseUser> contactList = DemoHelper.getInstance().getContactList();
                                        contactList.put(hxIdFrom, easeUser);
                                        // 存入db
                                        List<EaseUser> users = new ArrayList<>();
                                        users.add(easeUser);
                                        dao.saveContactList(users);
                                        DemoHelper.getInstance().getModel().setContactSynced(true);
                                        DemoHelper.getInstance().notifyContactsSyncListener(true);
                                    }
                                });
                    }
                }
            }
        }
    }

    //返回主页面是刷新了mme。do接口，然后通过rxbus刷新其他页面，meObservable在发射后需要注销懒了，就交给年兄你了
    //个人建议替rx及retrofit，不采用mvp，不写测试，二者没用明显优势，反而蛋疼
    @Override
    protected void onRestart() {
        super.onRestart();
        Observable<BeanMe> meObservable = App.getRetrofitUtil().getRetrofit()
                .getBeanMe(SPUtils.getString(com.maxiaobu.healthclub.common.Constant.MEMID));
        meObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BeanMe>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(BeanMe beanMe) {
                        ChangeEvent changeEvent = new ChangeEvent();
                        changeEvent.setBeanMine(beanMe);
                        mBeanMe = beanMe;
                        if (mRxBus.hasObservers()) {
                            mRxBus.send(changeEvent);
                        }
                    }
                });
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (mHomeFragment == null && fragment instanceof HomeFragment) {
            mHomeFragment =fragment;
        }else if (mTalkFragment == null && fragment instanceof TalkFragment) {
            mTalkFragment = fragment;
        }else if (mDiscoveryFragment == null && fragment instanceof DiscoveryFragment) {
            mDiscoveryFragment = fragment;
        }else if (mMineFragment == null && fragment instanceof MineFragment) {
            mMineFragment = fragment;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int jumpKey = intent.getIntExtra(com.maxiaobu.healthclub.common.Constant.JUMP_KEY, -1);
        if (jumpKey != -1) {
            Intent jumpIntent = new Intent();
            switch (jumpKey) {
                case com.maxiaobu.healthclub.common.Constant.PCOURSE_TO_PCORDER:
                    jumpIntent.setClass(this, OrderListActivity.class);
                    jumpIntent.putExtra(com.maxiaobu.healthclub.common.Constant.JUMP_KEY,
                            com.maxiaobu.healthclub.common.Constant.PCOURSE_TO_PCORDER);
                    mBottomNavigationBar.selectTab(3);
                    startActivity(jumpIntent);
                    break;
                case com.maxiaobu.healthclub.common.Constant.CATERINGDETAIL_TO_PCORDER:
                    jumpIntent.setClass(this, OrderListActivity.class);
                    jumpIntent.putExtra(com.maxiaobu.healthclub.common.Constant.JUMP_KEY,
                            com.maxiaobu.healthclub.common.Constant.CATERINGDETAIL_TO_PCORDER);
                    mBottomNavigationBar.selectTab(3);
                    startActivity(jumpIntent);
                    break;
                case com.maxiaobu.healthclub.common.Constant.PAY_TO_RESERVATION:
                    mBottomNavigationBar.selectTab(3);
                    jumpIntent.setClass(HomeActivity.this, MyBespeakActivity.class);
                    startActivity(jumpIntent);
                    break;
                //退群刷新
                case com.maxiaobu.healthclub.common.Constant.GROUPCANCLE_TO_TALK:
                    mBottomNavigationBar.selectTab(1);
                    refreshGroupId(true);
                default:
                    break;
            }
        }

        if ((Intent.FLAG_ACTIVITY_CLEAR_TOP & intent.getFlags()) != 0) {
            Intent orderIntent = new Intent();
            //去订单
            if (intent.getIntExtra(com.maxiaobu.healthclub.common.Constant.PAY_RESULT, -1) == 1) {
                orderIntent.putExtra("orderFlag", 2);
                orderIntent.setClass(HomeActivity.this, OrderListActivity.class);
                startActivity(orderIntent);
            } else if (intent.getIntExtra(com.maxiaobu.healthclub.common.Constant.PAY_RESULT, -1) == 0) {
                orderIntent.putExtra("orderFlag", 1);
                orderIntent.setClass(HomeActivity.this, OrderListActivity.class);
                startActivity(orderIntent);
            }
        }
        if (jumpKey != -1) {
            switch (jumpKey) {
                case com.maxiaobu.healthclub.common.Constant.GCOURSE_TO_BESPEAKLIST:
                    startActivity(new Intent(this, MyBespeakActivity.class));
                    break;
                default:
                    break;
            }
        }
        if (intent.getBooleanExtra(Constant.ACCOUNT_CONFLICT, false)
                && !isConflictDialogShow) {
            showConflictDialog();
        } else if (intent.getBooleanExtra(Constant.ACCOUNT_REMOVED, false)
                && !isAccountRemovedDialogShow) {
            showAccountRemovedDialog();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isConflict && !isCurrentAccountRemoved)
            updateUnreadLabel();

        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    @Override
    protected void onDestroy() {
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.popActivity(this);
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onBackPressed() {
        if (isHomePage) {
            exit();
        } else {
            mBottomNavigationBar.selectTab(0);
        }
    }

    /**
     * 退出应用
     */
    private void exit() {
        if (isExit) {
            isExit = false;
            Toast.makeText(getApplicationContext(), "再按一次退出",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
        }
    }

    /**
     * 切换布局
     *
     * @param position
     */
    public void switchFragment(int position) {
        switch (position) {
            case 0:
                switchContent(nowFragment, mHomeFragment);
                isHomePage = true;
                break;
            case 1:
                if (mTalkFragment == null)
                    mTalkFragment = TalkFragment.newInstance();
                switchContent(nowFragment, mTalkFragment);
                isHomePage = false;
                break;
            case 2:
                if (mDiscoveryFragment == null)
                    mDiscoveryFragment = DiscoveryFragment.newInstance();
                switchContent(nowFragment, mDiscoveryFragment);
                isHomePage = false;
                break;
            case 3:
                if (mMineFragment == null)
                    mMineFragment = MineFragment.newInstance();
                switchContent(nowFragment, mMineFragment);
                isHomePage = false;
                break;
            default:
                break;
        }
    }

    /**
     * 切换布局
     * @param from
     * @param to
     */
    private void switchContent(Fragment from, Fragment to) {
        if (nowFragment != to) {
            nowFragment = to;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            if (!to.isAdded()) {
                transaction.hide(from).add(R.id.fragment_container, to).commit();
            } else {
                transaction.hide(from).show(to).commit();
            }
        }
    }

    /**
     * 全部运行权限
     */
    @TargetApi(23)
    private void requestPermissions() {
        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
            @Override
            public void onGranted() {
                //权限被允许的回调
            }

            @Override
            public void onDenied(String permission) {
                //权限被拒绝的回调
            }
        });
    }

    /**
     * 当用户在其他设备登录显示dialog
     */
    private void showConflictDialog() {
        isConflictDialogShow = true;
        DemoHelper.getInstance().logout(false, null);
        String st = getResources().getString(R.string.Logoff_notification);
        if (!HomeActivity.this.isFinishing()) {
            // clear up global variables
            try {
                if (conflictBuilder == null)
                    conflictBuilder = new AlertDialog.Builder(HomeActivity.this);
                conflictBuilder.setTitle(st);
                conflictBuilder.setMessage("账户在其他设备登录");
                conflictBuilder.setPositiveButton(R.string.ok, (dialog, which) -> {
                    dialog.dismiss();
                    conflictBuilder = null;
                    finish();
                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                });
                conflictBuilder.setCancelable(false);
                conflictBuilder.create().show();
                isConflict = true;
            } catch (Exception e) {
                EMLog.e("MainActivity", "---------用户在其他设备登录显示dialog创建失败" + e.getMessage());
            }
        }
    }

    /**
     * 当用户账户被移除 显示dialog
     */
    private void showAccountRemovedDialog() {
        isAccountRemovedDialogShow = true;
        DemoHelper.getInstance().logout(false, null);
        String st5 = getResources().getString(R.string.Remove_the_notification);
        if (!HomeActivity.this.isFinishing()) {
            // clear up global variables
            try {
                if (accountRemovedBuilder == null)
                    accountRemovedBuilder = new AlertDialog.Builder(HomeActivity.this);
                accountRemovedBuilder.setTitle(st5);
                accountRemovedBuilder.setMessage("账户被移除");
                accountRemovedBuilder.setPositiveButton(R.string.ok, (dialog, which) -> {
                    dialog.dismiss();
                    accountRemovedBuilder = null;
                    finish();
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                });
                accountRemovedBuilder.setCancelable(false);
                accountRemovedBuilder.create().show();
                isCurrentAccountRemoved = true;
            } catch (Exception e) {
                EMLog.e("MainActivity", "---------当用户账户被移除 显示dialog创建失败" + e.getMessage());
            }
        }
    }

    /**
     * update unread message count
     * 跟新未读消息数
     */
    public void updateUnreadLabel() {
        int count = getUnreadMsgCountTotal();
        if (count > 99) {
            mTvPoint.setText("99+");
            mTvPoint.setVisibility(View.VISIBLE);
        } else if (count > 0) {
            if (mTvPoint != null) {
                mTvPoint.setText("" + count);
                mTvPoint.setVisibility(View.VISIBLE);
            }
        } else {
            if (mTvPoint != null) {
                mTvPoint.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * 获取未读消息总数
     * @return
     */
    public int getUnreadMsgCountTotal() {
        int unreadMsgCountTotal = 0;
        int chatroomUnreadMsgCount = 0;
        int times = 0;
        int grouptimes = 0;
        int chatTimes = 0;
        unreadMsgCountTotal = EMClient.getInstance().chatManager().getUnreadMsgsCount();
        for (EMConversation conversation : EMClient.getInstance().chatManager().getAllConversations().values()) {
            if (conversation.getType() == EMConversation.EMConversationType.ChatRoom)
                chatroomUnreadMsgCount = chatroomUnreadMsgCount + conversation.getUnreadMsgCount();
            if (conversation.getType() == EMConversation.EMConversationType.GroupChat) {
                for (int i = 0; i < App.getInstance().getGroupIdList().size(); i++) {
                    if (!conversation.getUserName().equals(App.getInstance().getGroupIdList().get(i))) {
                        if (i == App.getGroupIdList().size() - 1) {
                            times = times + conversation.getUnreadMsgCount();
                        }
                    } else {
                        grouptimes = grouptimes + conversation.getUnreadMsgCount();
                        break;
                    }
                }
            }
            if (conversation.getType() == EMConversation.EMConversationType.Chat) {
                chatTimes = chatTimes + conversation.getUnreadMsgCount();
            }
        }

        if (grouptimes == (unreadMsgCountTotal - chatroomUnreadMsgCount - times) && grouptimes != 0) {
            if (mTalkFragment == null)
                mTalkFragment = TalkFragment.newInstance();
            TalkFragment talkFragment = (TalkFragment) mTalkFragment;
            talkFragment.showUnreadMsg("GroupChat");
        } else if (chatTimes == (unreadMsgCountTotal - chatroomUnreadMsgCount - times) && chatTimes != 0) {
            if (mTalkFragment == null)
                mTalkFragment = TalkFragment.newInstance();
            TalkFragment talkFragment = (TalkFragment) mTalkFragment;
            talkFragment.showUnreadMsg("Chat");
        } else {
            if (mTalkFragment == null)
                mTalkFragment = TalkFragment.newInstance();
            TalkFragment talkFragment = (TalkFragment) mTalkFragment;
            talkFragment.showUnreadMsg("");
        }
        return unreadMsgCountTotal - chatroomUnreadMsgCount - times;
    }

    /**
     * 接收到消息时刷新刷新界面
     */
    private void refreshUIWithMessage() {
        runOnUiThread(() -> {
            updateUnreadLabel();
            if (mTalkFragment == null)
                mTalkFragment = TalkFragment.newInstance();
            TalkFragment talkFragment = (TalkFragment) mTalkFragment;
            conversationListFragment = talkFragment.getConversationListFragment();
            mGroupListFragment = talkFragment.getGroupListFragment();
            if (conversationListFragment != null && mGroupListFragment != null) {
                conversationListFragment.refresh();
                mGroupListFragment.refreshUI();
            }
        });
    }

    /**
     * 注册环信相关广播
     */
    private void registerBroadcastReceiver() {
        broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_CONTACT_CHANAGED);
        intentFilter.addAction(Constant.ACTION_GROUP_CHANAGED);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateUnreadLabel();
                refreshGroupId(true);

            }
        };
        broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    public BeanMe getBeanMe() {
        return mBeanMe;
    }

    /**
     * 检查版本
     */
    private void checkVersion() {
        Observable<BeanUpdata> BeanUpdataObservable = App.getRetrofitUtil().getRetrofit().getBeanUpdata();
        BeanUpdataObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BeanUpdata>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(mActivity, "检查版本更新接口访问失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(BeanUpdata bean) {
                        String versionName = getAppVersionName(HomeActivity.this);
                        if (UrlPath.URL_BASE.equals("http://123.56.195.32:18080/efithealth/") && !versionName.equals(bean.getLatest())) {
                            //版本需要更新
                            showUpdateDialog(bean.getMsgContent(), bean.getUrl());
                        }
                    }
                });
    }

    /**
     * 显示更新提示框
     * @param msg
     * @param apkUrl
     */
    public void showUpdateDialog(String msg, final String apkUrl) {
        new MaterialDialog.Builder(this)
                .title("有新版本")
                .content(msg)
                .positiveColor(getResources().getColor(R.color.colorTextPrimary))
                .positiveText("确认")
                .onPositive((dialog, which) -> {
                    Intent intent = new Intent(HomeActivity.this, UpdataService.class);
                    intent.putExtra("url", apkUrl);
                    HomeActivity.this.startService(intent);
                    dialog.dismiss();
                })
                .negativeColor(getResources().getColor(R.color.colorTextPrimary))
                .negativeText("取消")
                .onNegative((dialog, which) -> dialog.dismiss())
                .show();
    }

    /**
     * get版本名称
     *
     * @param context
     * @return
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
        } catch (Exception e) {
            Toast.makeText(context, "获取versionName失败", Toast.LENGTH_SHORT).show();
        }
        return versionName;
    }

    EMMessageListener messageListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            refreshUIWithMessage();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
        }

        @Override
        public void onMessageReadAckReceived(List<EMMessage> messages) {
        }

        @Override
        public void onMessageDeliveryAckReceived(List<EMMessage> message) {
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
        }
    };
                        

    /**
     * Description:百度MAP 定位成功回调接口方法
     *
     * @author Xushd
     * @since 2016年2月20日上午11:14:36
     */
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            SPUtils.putString(com.maxiaobu.healthclub.common.Constant.LATITUDE, location.getLatitude() + "");
            SPUtils.putString(com.maxiaobu.healthclub.common.Constant.LONGITUDE, location.getLongitude() + "");
            String local_city = location.getCity();
            Log.i("myapp", String.valueOf(location.getLatitude()));
            Log.i("myapp", location.getLongitude() + "");

            if (local_city != null) {
                SPUtils.putString(com.maxiaobu.healthclub.common.Constant.CITY, location.getCity());
            } else {
                SPUtils.putString(com.maxiaobu.healthclub.common.Constant.CITY, "沈阳");
            }
            mLocationClient.stop();
        }

        public void onReceivePoi(BDLocation location) {

        }
    }

    /****************************************
     * 退群：刷新groupid
     ************************************/
    public void refreshGroupId(Boolean b) {
        Observable<BeanGroupList> groupListObservable = App.getRetrofitUtil().getRetrofit()
                .getBeanMygrouplist(SPUtils.getString(com.maxiaobu.healthclub.common.Constant.MEMID));
        groupListObservable.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Subscriber<BeanGroupList>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        try {
                            Snackbar.make(mLyRoot, "mgroup!mygrouplist.do在homeActivity中：" + e.toString(), 10000)
                                    .setActionTextColor(Color.parseColor("#Fb8435"))
                                    .setAction("确定", v -> {
                                    }).show();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(BeanGroupList beanGroupList) {
                        List<BeanGroupList.GroupListBean> groupList = beanGroupList.getGroupList();
                        HashSet<String> hashSet = new HashSet<>();
                        List<String> AvarList = new ArrayList<String>();
                        List<String> GroupIdList = new ArrayList<String>();
                        List<String> GroupIidList = new ArrayList<String>();

                        for (int i = 0; i < groupList.size(); i++) {
                            hashSet.add(groupList.get(i).getImid());
                            AvarList.add(groupList.get(i).getImgsfilename());
                            GroupIdList.add(groupList.get(i).getImid());
                            GroupIidList.add(groupList.get(i).getGroupid());
                        }
                        SPUtils.putStringSet(com.maxiaobu.healthclub.common.Constant.GROUP_ID, hashSet);
                        App.getInstance().setAvarList(AvarList);
                        App.getInstance().setGroupIdList(GroupIdList);
                        App.getInstance().setGroupIidList(GroupIidList);
                        if (b) {
                            refreshUIWithMessage();
                        }
                    }
                });
    }

    /**
     * 此处容易出现bug，请注意，不是我写的
     * @return
     */
    public Fragment getfragment() {
        if (mDiscoveryFragment == null) {
            mDiscoveryFragment = DiscoveryFragment.newInstanceArticle();
        }
        return mDiscoveryFragment;
    }
}
