package com.maxiaobu.healthclub.ui.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseFrg;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.chat.DemoHelper;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.common.beangson.BeanMe;
import com.maxiaobu.healthclub.common.beangson.BeanMineSign;
import com.maxiaobu.healthclub.ui.activity.BalanceActivity;
import com.maxiaobu.healthclub.ui.activity.BindClubListActivity;
import com.maxiaobu.healthclub.ui.activity.ClubDetailActivity;
import com.maxiaobu.healthclub.ui.activity.CoachcertApplyActivity;
import com.maxiaobu.healthclub.ui.activity.CourseManageActivity;
import com.maxiaobu.healthclub.ui.activity.HomeActivity;
import com.maxiaobu.healthclub.ui.activity.MineTeachingAppointmentActivity;
import com.maxiaobu.healthclub.ui.activity.MyBespeakActivity;
import com.maxiaobu.healthclub.ui.activity.MyCardActivity;
import com.maxiaobu.healthclub.ui.activity.MyCodeActivity;
import com.maxiaobu.healthclub.ui.activity.MyFriendActivity;
import com.maxiaobu.healthclub.ui.activity.MyStudentActivity;
import com.maxiaobu.healthclub.ui.activity.OrderListActivity;
import com.maxiaobu.healthclub.ui.activity.PersionalActivity;
import com.maxiaobu.healthclub.ui.activity.ScheduleManagementActivity;
import com.maxiaobu.healthclub.ui.activity.SplashActivity;
import com.maxiaobu.healthclub.ui.activity.TrainDataActivity;
import com.maxiaobu.healthclub.ui.activity.UserEditActivity;
import com.maxiaobu.healthclub.ui.weiget.pulltozoom.ECPullToZoomScrollViewToolbarEx;
import com.maxiaobu.healthclub.utils.HealthUtil;
import com.maxiaobu.healthclub.utils.TimesUtil;
import com.maxiaobu.healthclub.utils.rx.RxBus;
import com.maxiaobu.healthclub.utils.storage.SPUtils;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import maxiaobu.mqldialoglibrary.materialdialogs.core.MaterialDialog;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.observables.ConnectableObservable;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


/**
 * Created by 马小布 on 2016/12/27.
 * email：maxiaobu1216@gmail.com
 * 功能：我的  一级第四个
 * 伪码：
 * 待完成：
 */
public class MineFragment extends BaseFrg implements ECPullToZoomScrollViewToolbarEx.OnScrollViewChangedOutSizeListener, View.OnClickListener {

    public static final int MIN_CLICK_DELAY_TIME = 1000;
    ECPullToZoomScrollViewToolbarEx mFragmentMinePulltozoomScrollview;
    @Bind(R.id.toolbar_backgroundColor)
    RelativeLayout mToolbarBackgroundColor;
    @Bind(R.id.tv_title_common)
    TextView mTvTitleCommon;
    @Bind(R.id.mine_image)
    ImageView mMineImage;
    @Bind(R.id.fragment_mine_code)
    LinearLayout mFragmentMineCode;
    @Bind(R.id.toolbar_common)
    RelativeLayout mToolbarCommon;
    @Bind(R.id.iv_header)
    CircularImageView mIvHeader;
    @Bind(R.id.tv_header_edit)
    TextView mTvHeaderEdit;
    @Bind(R.id.iv_header_userEdit)
    ImageView mIvHeaderUserEdit;

    @Bind(R.id.tv_cart_num)
    TextView mTvCartNum;
    @Bind(R.id.ly_cart)
    LinearLayout mLyCart;
    @Bind(R.id.tv_order_num)
    TextView mTvOrderNum;
    @Bind(R.id.ly_order)
    LinearLayout mLyOrder;
    @Bind(R.id.tv_appointment_num)
    TextView mTvAppointmentNum;
    @Bind(R.id.ly_appointment)
    LinearLayout mLyAppointment;
    @Bind(R.id.tv_balance_num)
    TextView mTvBalanceNum;
    @Bind(R.id.ly_balance)
    LinearLayout mLyBalance;
    @Bind(R.id.imageView3)
    ImageView mImageView3;
    @Bind(R.id.ll_my_cart)
    LinearLayout mLlMyCart;
    @Bind(R.id.ly_my_cart)
    MaterialRippleLayout mLyMyCart;
    @Bind(R.id.ll_my_courseManage)
    LinearLayout mLlMyCourseManage;
    @Bind(R.id.ly_my_courseManage)
    MaterialRippleLayout mLyMyCourseManage;
    @Bind(R.id.imageView)
    ImageView mImageView;
    @Bind(R.id.ll_my_friend)
    LinearLayout mLlMyFriend;
    @Bind(R.id.ly_my_friend)
    MaterialRippleLayout mLyMyFriend;
    @Bind(R.id.ll_my_courseAppointment)
    LinearLayout mLlMyCourseAppointment;
    @Bind(R.id.ly_my_courseAppointment)
    MaterialRippleLayout mLyMyCourseAppointment;
    @Bind(R.id.tv_sign)
    TextView mTvSign;
    @Bind(R.id.ly_sign)
    LinearLayout mLySign;
    @Bind(R.id.ly_persional_info)
    LinearLayout mLyPersionalInfo;
    @Bind(R.id.textView2)
    TextView mTextView2;
    @Bind(R.id.imageView4)
    ImageView mImageView4;
    @Bind(R.id.ly_authentication)
    LinearLayout mLyAuthentication;
    @Bind(R.id.ly_my_student)
    LinearLayout mLyMyStudent;
    @Bind(R.id.ly_club_list)
    LinearLayout mLyClubList;
    @Bind(R.id.ly_file_management)
    LinearLayout mLyFileManagement;
    @Bind(R.id.ly_trainer_root)
    LinearLayout mLyTrainerRoot;
    @Bind(R.id.ly_set_top)
    LinearLayout mLySetTop;
    @Bind(R.id.ly_login_out)
    LinearLayout mLyLoginOut;
    @Bind(R.id.ly_my_fri)
    LinearLayout myFri;
    @Bind(R.id.imageView5)
    ImageView mImageView5;
    @Bind(R.id.ly_train_data)
    LinearLayout mLyTrainData;


    private long lastClickTime = 0;
    private View mRootView;
    private AlertDialog.Builder mDialog;
    private String mBalance;
    private BeanMe mBeanMine;
    private CompositeSubscription mSubscriptions;

    public MineFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_mine, container, false);
        mFragmentMinePulltozoomScrollview = (ECPullToZoomScrollViewToolbarEx) mRootView
                .findViewById(R.id.fragment_mine_pulltozoom_scrollview);
        loadViewForPullToZoomScrollView(mFragmentMinePulltozoomScrollview);
        ButterKnife.bind(this, mRootView);
        RxBus rxBus = App.getRxBusSingleton();
        mSubscriptions = new CompositeSubscription();
        ConnectableObservable<Object> tapEventEmitter = rxBus.asObservable().publish();
        mSubscriptions
                .add(tapEventEmitter.
                        observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Object>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Snackbar.make(mRootView, "mme.do在homeActivity中：" + e.toString(), 10000)
                                        .setActionTextColor(Color.parseColor("#Fb8435"))
                                        .setAction("确定", v -> {
                                        }).show();
                            }

                            @Override
                            public void onNext(Object event) {
                                if (event instanceof HomeActivity.ChangeEvent) {
                                    BeanMe mChangeBeanMine = ((HomeActivity.ChangeEvent) event).getBeanMine();
                                    BeanMe.MemberBean member = mChangeBeanMine.getMember();
                                    if (!member.getImgsfilename().equals(mBeanMine.getMember().getImgsfilename()))
//                                        Glide.with(getActivity())
//                                                .load(member.getImgsfilename())
//                                                .into(mIvHeader);
                                        HealthUtil.setAvator(getActivity(), mIvHeader, mChangeBeanMine.getMember().getImgsfilename(), true);
                                    if (!member.getNickname().equals(mBeanMine.getMember().getNickname()))
                                        mTvHeaderEdit.setText(member.getNickname());
                                    if (!mChangeBeanMine.getOrdercount().equals(mBeanMine.getOrdercount()))
                                        mTvOrderNum.setText("" + mChangeBeanMine.getOrdercount());
                                    if (!mChangeBeanMine.getLessoncount().equals(mBeanMine.getLessoncount()))
                                        mTvAppointmentNum.setText("" + mChangeBeanMine.getLessoncount());
                                    if (!mChangeBeanMine.getSignstatus().equals(mBeanMine.getSignstatus())) {
                                        if (mChangeBeanMine.getSignstatus().equals("1")) {
                                            mTvSign.setText("已签到");
                                        } else {
                                            mTvSign.setText("签到");
                                        }
                                    }
                                    if (!mChangeBeanMine.getYcoinnum().equals(mBeanMine.getYcoinnum())) {
                                        mBalance = mChangeBeanMine.getYcoinnum();
                                        if (mChangeBeanMine.getYcoinnum().length() >= 5) {
                                            int size = mChangeBeanMine.getYcoinnum().length();
                                            String one = mChangeBeanMine.getYcoinnum().substring(0, size - 4);
                                            String two = mChangeBeanMine.getYcoinnum().substring(size - 4, size - 3);
                                            if (two.equals("0")) {
                                                mTvBalanceNum.setText(one + " 万");
                                            } else {
                                                mTvBalanceNum.setText(one + "." + two + " 万");
                                            }
                                        } else {
                                            mTvBalanceNum.setText(mChangeBeanMine.getYcoinnum());
                                        }
                                    }
                                    mBeanMine = mChangeBeanMine;

                                }
                            }
                        }));
        mSubscriptions.add(tapEventEmitter.connect());
        HomeActivity homeActivity = (HomeActivity) getActivity();
        mBeanMine = homeActivity.getBeanMe();
        if (mBeanMine != null) {
            initView();
            initData();
        }
        return mRootView;
    }

    private void loadViewForPullToZoomScrollView(ECPullToZoomScrollViewToolbarEx scrollView) {
        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_mine_head_view, null);
        View zoomView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_mine_head_zoom_view, null);
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_mine_content_view, null);
        scrollView.setHeaderView(headView);
        scrollView.setZoomView(zoomView);
        scrollView.setScrollContentView(contentView);
    }

    /**
     * 做各种绑定布局
     */
    @Override
    public void initView() {
        //主布局的绑定
        mFragmentMinePulltozoomScrollview.setToolbar(mToolbarCommon);

        setPullToZoomViewLayoutParams(mFragmentMinePulltozoomScrollview);

        mFragmentMinePulltozoomScrollview.setOnScrollViewChangedOutSideListener(this);

        mIvHeader.setOnClickListener(this);
        mIvHeaderUserEdit.setOnClickListener(this);
        mFragmentMineCode.setOnClickListener(this);
    }

    /**
     * 做逻辑代码
     */
    @Override
    public void initData() {
        try {
            // 判断身份的
            String memrole = SPUtils.getString(Constant.MEMROLE, "-1");
            if (memrole.equals("coach")) {
                mLyMyCourseManage.setVisibility(View.VISIBLE);
                mLyMyCourseAppointment.setVisibility(View.VISIBLE);
                mLyTrainerRoot.setVisibility(View.VISIBLE);
                mLyAuthentication.setVisibility(View.GONE);
                mLyMyCart.setVisibility(View.GONE);
                mLyMyFriend.setVisibility(View.GONE);

            } else if (memrole.equals("mem")) {
                mLyMyFriend.setVisibility(View.VISIBLE);
                mLyMyCart.setVisibility(View.VISIBLE);
                mLyAuthentication.setVisibility(View.VISIBLE);
                mLyTrainerRoot.setVisibility(View.GONE);
                mLyMyCourseManage.setVisibility(View.GONE);
                mLyMyCourseAppointment.setVisibility(View.GONE);

            }
            if (mBeanMine != null) {
//            Glide.with(getActivity())
//                    .load(mBeanMine.getMember().getImgsfilename())
//                    .into(mIvHeader);
                HealthUtil.setAvator(getActivity(), mIvHeader, mBeanMine.getMember().getImgsfilename(), true);

                mTvHeaderEdit.setText(mBeanMine.getMember().getNickname());
                mTvOrderNum.setText("" + mBeanMine.getOrdercount());
                mTvAppointmentNum.setText("" + mBeanMine.getLessoncount());

                if (mBeanMine.getSignstatus().equals("1")) {
                    mTvSign.setText("已签到");
                } else {
                    mTvSign.setText("签到");
                }

                mBalance = mBeanMine.getYcoinnum();
                if (mBeanMine.getYcoinnum().length() >= 5) {
                    int size = mBeanMine.getYcoinnum().length();
//                    Log.d("MineFragment", "size:" + size);
                    String one = mBeanMine.getYcoinnum().substring(0, size - 4);
                    String two = mBeanMine.getYcoinnum().substring(size - 4, size - 3);
                    if (two.equals("0")) {
                        mTvBalanceNum.setText(one + " 万");
                    } else {
                        mTvBalanceNum.setText(one + "." + two + " 万");
                    }
                } else {
                    mTvBalanceNum.setText(mBeanMine.getYcoinnum());
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    @OnClick({R.id.ly_cart, R.id.ly_order, R.id.ly_appointment, R.id.ly_balance,
            R.id.ly_persional_info, R.id.ly_sign, R.id.ly_authentication,
            R.id.ly_club_list, R.id.ly_my_student, R.id.ll_my_courseManage, R.id.ll_my_courseAppointment,
            R.id.ly_file_management, R.id.ly_trainer_root, R.id.ly_set_top, R.id.ly_login_out,
            R.id.ll_my_cart, R.id.ll_my_friend, R.id.ly_my_fri,R.id.ly_train_data})
    @Override
    public void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            switch (v.getId()) {
                //订单
                case R.id.ly_order:
                    startActivity(new Intent(getActivity(), OrderListActivity.class));
                    break;
                //退出登录
                case R.id.ly_login_out:
                    logout();
                    break;
                //预约
                case R.id.ly_appointment:
                    startActivity(new Intent(getActivity(), MyBespeakActivity.class));
                    break;
                //申请认证
                case R.id.ly_authentication:
                    startActivity(new Intent(getActivity(), CoachcertApplyActivity.class));
                    break;
                //合作俱乐部
                case R.id.ly_club_list:
                    startActivity(new Intent(getActivity(), BindClubListActivity.class));
                    break;
                //课程管理
                case R.id.ll_my_courseManage:
                    startActivity(new Intent(getActivity(), CourseManageActivity.class));
                    break;
                //教学预约
                case R.id.ll_my_courseAppointment:
                    startActivity(new Intent(getActivity(), MineTeachingAppointmentActivity.class));
                    break;
                case R.id.ly_file_management:
                    startActivity(new Intent(getActivity(), ScheduleManagementActivity.class));
                    break;
                //点击头像跳转到个人详情
                case R.id.iv_header:
                    // 判断身份的
                    String memrole = SPUtils.getString(Constant.MEMROLE, "-1");
                    if (memrole.equals("coach") || memrole.equals("mem")) {
                        Intent intent = new Intent(getContext(), PersionalActivity.class);
                        intent.putExtra("tarid", SPUtils.getString(Constant.MEMID));
                        getContext().startActivity(intent);
                    } else {//如果是俱乐部管理员登录则跳转到俱乐部详情页面
                        Intent intent = new Intent(getActivity(), ClubDetailActivity.class);
                        intent.putExtra("tarid", SPUtils.getString(Constant.MEMID));
                        startActivity(intent);
                    }
                    break;
                case R.id.ly_persional_info:
                case R.id.iv_header_userEdit:
                    String str = new Gson().toJson(mBeanMine);
                    Intent intent_edit = new Intent(getActivity(), UserEditActivity.class);
                    intent_edit.putExtra("values", str);
                    startActivity(intent_edit);
                    break;
                case R.id.ly_balance:
                    Intent intent = new Intent(getActivity(), BalanceActivity.class);
                    intent.putExtra("userImage", SPUtils.getString(Constant.AVATAR));
                    intent.putExtra("name", SPUtils.getString(Constant.NICK_NAME));
                    intent.putExtra("balance", mBalance);
                    startActivity(intent);
                    break;
                case R.id.ly_my_student:
                    Intent myStudentIntent = new Intent(getContext(), MyStudentActivity.class);
                    startActivity(myStudentIntent);
                    break;
                case R.id.ll_my_cart:
                    Intent myCardIntent = new Intent(getContext(), MyCardActivity.class);
                    startActivity(myCardIntent);
                    break;
                case R.id.fragment_mine_code:
                case R.id.ll_my_friend:
                    Intent myCodeIntent = new Intent(getContext(), MyCodeActivity.class);
                    startActivity(myCodeIntent);
                    break;
                case R.id.ly_sign:
                    signAnalysis();
                    break;
                case R.id.ly_my_fri:
                    Intent intentMyFri = new Intent(getContext(), MyFriendActivity.class);
                    startActivity(intentMyFri);
                    break;
                //训练数据
                case R.id.ly_train_data:
                    startActivity(new Intent( getActivity(), TrainDataActivity.class));
                    break;
                default:
                    break;
            }
        }
    }

    // 设置头部的View的宽高。
    private void setPullToZoomViewLayoutParams(ECPullToZoomScrollViewToolbarEx scrollView) {
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int mScreenWidth = localDisplayMetrics.widthPixels;
        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth, (int) (9.8F * (mScreenWidth / 15.0F)));
        scrollView.setHeaderLayoutParams(localObject);

    }

    public static Fragment newInstance() {
        MineFragment fragment = new MineFragment();
        return fragment;
    }

    /**
     * 显示退出的Dialog
     */
    public void logout() {
        new MaterialDialog.Builder(getActivity())
                .title("退出登录")
                .positiveText("确定")
                .negativeText("取消")
                .onPositive((dialog, which) -> DemoHelper.getInstance().logout(true, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        Intent intent = new Intent(getActivity(), SplashActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(int i, String s) {
                        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onProgress(int i, String s) {
                        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();

                    }
                }))
                .onNegative((dialog, which) -> dialog.dismiss()).show();
    }

    @Override
    public void onScrollChanged(int left, int top, int oldLeft, int oldTop) {
        float a = top;
        float b = a / 1000;
        Log.d("MineFragment", "b:" + b);
        float max = (float) Math.min(1, b * 2);
        mToolbarBackgroundColor.setAlpha(max);

    }


    public void Analysis() {
        Observable<BeanMe> beanMineObservable = App.getRetrofitUtil().getRetrofit().getBeanMe(SPUtils.getString(Constant.MEMID));
        beanMineObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BeanMe>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        try {
                            Snackbar.make(mRootView, "mme.do在MineFragment中：" + e.toString(), 10000)
                                    .setActionTextColor(Color.parseColor("#Fb8435"))
                                    .setAction("确定", v -> {
                                    }).show();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(BeanMe beanMine) {
                        SPUtils.putString(Constant.AVATAR, beanMine.getMember().getImgsfilename());
                        SPUtils.putString(Constant.NICK_NAME, beanMine.getMember().getNickname());
                        String time = String.valueOf(beanMine.getMember().getBirthday().getTime());
                        SPUtils.putString(Constant.BRITHDAY, TimesUtil.timestampToStringS(time, "yyyy-MM-dd"));
                        mIvHeader.setBorderWidth(6);
//                        Glide.with(getActivity())
//                                .load(beanMine.getMember().getImgsfilename())
//                                .into(mIvHeader);
                        HealthUtil.setAvator(getActivity(), mIvHeader, mBeanMine.getMember().getImgsfilename(), true);

                        mTvHeaderEdit.setText(beanMine.getMember().getNickname());
                        mTvOrderNum.setText("" + beanMine.getOrdercount());
                        mTvAppointmentNum.setText("" + beanMine.getLessoncount());

                        if (beanMine.getSignstatus().equals("1")) {
                            mTvSign.setText("已签到");
                        } else {
                            mTvSign.setText("签到");
                        }

                        mBalance = beanMine.getYcoinnum();
                        if (beanMine.getYcoinnum().length() >= 5) {
                            int size = beanMine.getYcoinnum().length();
//                    Log.d("MineFragment", "size:" + size);
                            String one = beanMine.getYcoinnum().substring(0, size - 4);
                            String two = beanMine.getYcoinnum().substring(size - 4, size - 3);
                            if (two.equals("0")) {
                                mTvBalanceNum.setText(one + " 万");
                            } else {
                                mTvBalanceNum.setText(one + "." + two + " 万");
                            }
                        } else {
                            mTvBalanceNum.setText(beanMine.getYcoinnum());
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
//        Analysis();
    }

    public void signAnalysis() {
        Observable<BeanMineSign> beanMineSignObservable = App.getRetrofitUtil().getRetrofit().getBeanSinginSave(SPUtils.getString(Constant.MEMID));
        beanMineSignObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BeanMineSign>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        try {
                            Snackbar.make(mRootView, "msinginsave.do在MineFragment中：" + e.toString(), 10000)
                                    .setActionTextColor(Color.parseColor("#Fb8435"))
                                    .setAction("确定", v -> {
                                    }).show();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(BeanMineSign bean) {
                        try {
                            if (bean.getMsgFlag().equals("1")) {
                                if (!bean.getCount().equals("0")) {
                                    mDialog = createOnceSignDialog(bean.getMsgContent(), bean.getCount());
                                    mDialog.show();
                                    Analysis();
                                    mTvSign.setText("已签到");
                                } else {
                                    mDialog = creatSignDialog(bean.getMsgContent());
                                    mDialog.show();
                                }

                            } else {
                                Toast.makeText(getContext(), "签到失败，请检查网络原因", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 当日已经签到完毕，显示内容为：今日已经签到。
     *
     * @param mMsgContent 内容
     * @return
     */
    private AlertDialog.Builder creatSignDialog(String mMsgContent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(mMsgContent);
        builder.setPositiveButton("确定", (dialog, which) -> dialog.dismiss());
        return builder;

    }

    /**
     * 当日的第一次签到显示内容为：签到次数：*
     *
     * @param mMsgContent 内容
     * @param mCount      签到次数
     * @return
     */
    public AlertDialog.Builder createOnceSignDialog(String mMsgContent, String mCount) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(mMsgContent + ": 已签到" + mCount + "天");
        builder.setPositiveButton("确定", (dialog, which) -> dialog.dismiss());

        return builder;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mSubscriptions.clear();
        ButterKnife.unbind(this);
    }
}
