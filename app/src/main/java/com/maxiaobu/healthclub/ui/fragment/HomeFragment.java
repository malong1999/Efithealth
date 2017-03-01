package com.maxiaobu.healthclub.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hyphenate.util.DensityUtil;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseFrg;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.adapter.AdapterHorizontalHomeFrg;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.common.beangson.BeanMe;
import com.maxiaobu.healthclub.listener.MqlOnItemClickListener;
import com.maxiaobu.healthclub.ui.GravitySnapHelper;
import com.maxiaobu.healthclub.ui.activity.ActivityTip;
import com.maxiaobu.healthclub.ui.activity.CateringDetailActivity;
import com.maxiaobu.healthclub.ui.activity.CateringListActivity;
import com.maxiaobu.healthclub.ui.activity.ClubDetailActivity;
import com.maxiaobu.healthclub.ui.activity.CoachesListActivity;
import com.maxiaobu.healthclub.ui.activity.HomeActivity;
import com.maxiaobu.healthclub.ui.activity.PersionalActivity;
import com.maxiaobu.healthclub.ui.activity.ScanCodeActivity;
import com.maxiaobu.healthclub.ui.activity.WebActivity;
import com.maxiaobu.healthclub.ui.weiget.GlideCircleTransform;
import com.maxiaobu.healthclub.ui.weiget.MqlHorRecyclerview;
import com.maxiaobu.healthclub.ui.weiget.observablescrollview.ObservableScrollView;
import com.maxiaobu.healthclub.ui.weiget.observablescrollview.ScrollViewListener;
import com.maxiaobu.healthclub.utils.HealthUtil;
import com.maxiaobu.healthclub.utils.TimesUtil;
import com.maxiaobu.healthclub.utils.rx.RxBus;
import com.maxiaobu.healthclub.utils.storage.SPUtils;
import com.maxiaobu.healthclub.utils.web.BaseJsToAndroid;
import com.nineoldandroids.view.ViewHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import at.markushi.ui.CircleButton;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.observables.ConnectableObservable;
import rx.subscriptions.CompositeSubscription;

import static android.R.attr.animation;
import static com.maxiaobu.healthclub.R.id.member;

/**
 * Created by 马小布 on 2016/12/20.
 * introduction: 真他娘的不知道说点啥
 * email:maxiaobu1999@163.com
 * 功能：首页
 * 伪码：通过rxbus获取的数据，来自于homeactivity
 * 待完成：
 */
public class HomeFragment extends BaseFrg implements View.OnClickListener {
    @Bind(R.id.ly_content)
    LinearLayout mLyContent;
    @Bind(R.id.rv_horizontal)
    RecyclerView mRvHorizontal;
    @Bind(R.id.ly_Recommend)
    LinearLayout mLyRecommend;
    @Bind(R.id.web_view)
    WebView mWebView;
    @Bind(R.id.sv_recommend)
    MqlHorRecyclerview mSvRecommend;
    @Bind(R.id.view_pager)
    ViewPager mViewPager;
    @Bind(R.id.page_indicator)
    CircleIndicator mPageIndicator;
    @Bind(R.id.ly_head)
    RelativeLayout mLyHead;
    @Bind(R.id.view_bar_place_holder)
    View mViewBarPlaceHolder;
    @Bind(R.id.tv_club_name)
    TextView mTvClubName;
    @Bind(R.id.iv_club_avatar)
    ImageView mIvClubAvatar;
    @Bind(R.id.tv_club_time_hide)
    TextView mTvClubTimeHide;
    @Bind(R.id.ly_profile)
    RelativeLayout mLyProfile;
    @Bind(R.id.iv_search)
    ImageView mIvSearch;
    @Bind(R.id.iv_title)
    ImageView mIvTitle;
    @Bind(R.id.iv_two_code)
    ImageView mIvTwoCode;
    @Bind(R.id.toolbar)
    RelativeLayout mToolbar;
    @Bind(R.id.fab_right)
    CircleButton mFabRight;
    @Bind(R.id.fab_left)
    CircleButton mFabLeft;
    @Bind(R.id.ly_root)
    FrameLayout mLyRoot;
    private int mMemRole;
    private BeanMe mBeanMine;
    private CompositeSubscription mSubscriptions;
    private View mRootView;
    Boolean flag = false;
    int minHeadHeight = 510;
    private Animation mAnimation;

    public HomeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_home, container, false);
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float density = metrics.density;
        minHeadHeight = (int) (510 / 3 * density);
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
                                try {
                                    Snackbar.make(mRootView, "mme.do在homeActivity中：" + e.toString(), 10000)
                                            .setActionTextColor(Color.parseColor("#Fb8435"))
                                            .setAction("确定", v -> {
                                            }).show();
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }

                            }

                            @Override
                            public void onNext(Object event) {
                                try {
                                    if (event instanceof HomeActivity.TapEvent) {
                                        //Log.d("HomeFragment", ((HomeActivity.TapEvent) event).getBeanMine().getMsgContent());
                                        mBeanMine = ((HomeActivity.TapEvent) event).getBeanMine();
                                        initView();
                                        initData();
                                    } else if (event instanceof HomeActivity.ChangeEvent) {
                                        BeanMe mChangeBeanMine = ((HomeActivity.ChangeEvent) event).getBeanMine();
                                        BeanMe.MemberBean member = mChangeBeanMine.getMember();
                                        HealthUtil.setAvator(getActivity(), mIvClubAvatar,mChangeBeanMine.getMember().getImgsfilename(),true);
                                        if (!member.getNickname().equals(mBeanMine.getMember().getNickname()))
                                            mTvClubName.setText(member.getNickname());
                                        String s = member.getMemrole();
                                        int memRole = mMemRole;
                                        if (s.equals("coach")) {
                                            memRole = 1;
                                        } else if (s.equals("clubadmin")) {
                                            memRole = 2;
                                        } else if (s.equals("mem")) {
                                            if (null == mChangeBeanMine.getCard()) {
                                                memRole = 3;
                                            } else {
                                                memRole = 4;
                                            }
                                        }
                                        mBeanMine = mChangeBeanMine;
                                        if (mMemRole != memRole) {
                                            mMemRole = memRole;
                                            flag = true;

                                            initHeadWeb();
                                        } else {
                                            String changeTimes= mChangeBeanMine.getCard()==null?"-1":mChangeBeanMine.getCard().getCoursetimes();
                                            String times=mBeanMine.getCard()==null?"-1":mBeanMine.getCard().getCoursetimes();
                                            if (!changeTimes.equals(times)) {
                                                initHeadWeb();
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }));
        mSubscriptions.add(tapEventEmitter.connect());

        return mRootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mSubscriptions.clear();
        ButterKnife.unbind(this);
    }

    @Override
    public void initView() {
        ViewGroup.LayoutParams layoutParams = mViewBarPlaceHolder.getLayoutParams();
        layoutParams.height = DensityUtil.dip2px(getActivity(), 250) - minHeadHeight;
        mViewBarPlaceHolder.setLayoutParams(layoutParams);
        mAnimation = new AlphaAnimation(0,1);
        mAnimation.setDuration(1000);

        //初始化横向三个模块
        mRvHorizontal.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mRvHorizontal.setLayoutManager(layoutManager);
        mRvHorizontal.setItemAnimator(new DefaultItemAnimator());
        AdapterHorizontalHomeFrg adapterHorizontalHomeFrg = new AdapterHorizontalHomeFrg(getActivity());
        adapterHorizontalHomeFrg.setOnItemClickListener((view, position, string) -> {
            switch (position) {
                case 0:
                    //教练列表
                    startActivity(new Intent(getActivity(), CoachesListActivity.class));
                    break;
                case 1:
                    //配餐列表
                    startActivity(new Intent(getActivity(), CateringListActivity.class));
                    break;
                case 2:
                    //跳转发现页面
                    startActivity(new Intent(getActivity(), ActivityTip.class));
                    HomeActivity activity = (HomeActivity) getActivity();
                    activity.getfragment();
                    activity.mBottomNavigationBar.selectTab(2);
                    break;
                default:
                    break;
            }

        });
        mRvHorizontal.setAdapter(adapterHorizontalHomeFrg);
        GravitySnapHelper mySnapHelper = new GravitySnapHelper(Gravity.START);
        mySnapHelper.attachToRecyclerView(mRvHorizontal);

        String s = mBeanMine.getMember().getMemrole();
        if (s.equals("coach")) {
            mMemRole = 1;
        } else if (s.equals("clubadmin")) {
            mMemRole = 2;
        } else if (s.equals("mem")) {
            if (null == mBeanMine.getCard()) {
                mMemRole = 3;
            } else {
                mMemRole = 4;
            }
        }
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
//         在js中调用本地java方法
        mWebView.addJavascriptInterface(new WebAppInterface(getActivity()), "mobile");
        mWebView.getSettings().setDefaultTextEncodingName("utf-8");
        mWebView.loadUrl("file:///android_asset/adsList.html");
        mSvRecommend.setScrollViewListener(new ScrollViewListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
//                Log.d("HomeTestFragment", "minHeadHeight:" + minHeadHeight);
                float a = (minHeadHeight + 30 - y / 2);
                float b = (minHeadHeight + 30);
                float scale = a / b;
                ViewHelper.setTranslationY(mLyHead, Math.max(-minHeadHeight, -y));
                ViewHelper.setTranslationY(mLyProfile, Math.max(-minHeadHeight - 30, -y));
                ViewHelper.setTranslationY(mFabRight, Math.max(-minHeadHeight, -y));
                ViewHelper.setTranslationY(mFabLeft, Math.max(-minHeadHeight, -y));
                //比例
//                １－０．５ 　0---minHeadHeight＋60  y/(minHeadHeight+60)
//                ViewHelper.setRotation(mIvClubAvatar,y);
                if (scale > 0.5f) {
//                    ViewHelper.setScaleY(mIvClubAvatar,scale);
                    ViewHelper.setScaleX(mIvClubAvatar, scale);
                    ViewHelper.setScaleY(mIvClubAvatar, scale);
//                    Log.d("HomeTestFragment", "Math.min(1,scale2):" + Math.min(1, scale2 + 0.003f));
                }
                if (y >= minHeadHeight) {
                    //折叠
                    mViewBarPlaceHolder.setAlpha(1);
                    mTvClubTimeHide.setVisibility(View.VISIBLE);
                    if (mMemRole == 4) {
                        mFabRight.setVisibility(View.GONE);
                        mFabLeft.setVisibility(View.GONE);
                    }
                } else {
                    //展开
                    mViewBarPlaceHolder.setAlpha(0);
                    mTvClubTimeHide.setVisibility(View.INVISIBLE);
                    if (mMemRole == 4) {
                        mFabRight.setVisibility(View.VISIBLE);
                        mFabLeft.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        initHeadWeb();


    }

    private void initHeadWeb() {
        mRootView.post(() -> {
            mViewPager.setOffscreenPageLimit(3);
            Adapter adapter = new Adapter(getChildFragmentManager());
            if (flag) {
                adapter.setFlag(true);
            }
            int mWebViewHeight = DensityUtil.px2dip(getActivity(), mLyHead.getMeasuredHeight());
            int mWebTop = 58;
            int mWebBottom = DensityUtil.px2dip(getActivity(), mLyHead.getMeasuredHeight() / 2);
            HealthUtil.setAvator(getActivity(), mIvClubAvatar,mBeanMine.getMember().getImgsfilename(),true);
            mTvClubName.setText(mBeanMine.getMember().getNickname());
            if (mMemRole == 1) {
                //如果是教练
                mFabLeft.setVisibility(View.GONE);
                mFabRight.setVisibility(View.GONE);
                WebFragment webFragmentCoach = new WebFragment();
                Bundle bundleCoach = new Bundle();
                bundleCoach.putString("url", "file:///android_asset/indexView_coach.html?viewHeight="
                        + mWebViewHeight + "&top=" + mWebTop + "&bottom=" + mWebBottom);
                webFragmentCoach.setArguments(bundleCoach);
                adapter.addFragment(webFragmentCoach, "");
                mViewPager.setAdapter(adapter);
                mPageIndicator.setViewPager(mViewPager);
                mPageIndicator.setVisibility(View.GONE);
            } else if (mMemRole == 2) {
                //俱乐部管理员
                mFabLeft.setVisibility(View.GONE);
                mFabRight.setVisibility(View.GONE);
                WebFragment webFragmentClub = new WebFragment();
                Bundle bundleClub = new Bundle();
                bundleClub.putString("url", "file:///android_asset/indexView_club.html?viewHeight="
                        + mWebViewHeight + "&top=" + mWebTop + "&bottom=" + mWebBottom);
                webFragmentClub.setArguments(bundleClub);
                adapter.addFragment(webFragmentClub, "");
                mViewPager.setAdapter(adapter);
                mPageIndicator.setViewPager(mViewPager);
                mPageIndicator.setVisibility(View.GONE);
            } else if (mMemRole == 3) {
                //会员无会籍
                mFabLeft.setVisibility(View.GONE);
                mFabRight.setVisibility(View.GONE);

                WebFragment webFragmentPoor = new WebFragment();
                Bundle bundlePoor = new Bundle();
                bundlePoor.putString("url", "file:///android_asset/indexView_mem.html?viewHeight="
                        + mWebViewHeight + "&top=" + mWebTop + "&bottom=" + mWebBottom);
                webFragmentPoor.setArguments(bundlePoor);
                adapter.addFragment(webFragmentPoor, "");
                mViewPager.setAdapter(adapter);
                mPageIndicator.setViewPager(mViewPager);
                mPageIndicator.setVisibility(View.GONE);


            } else if (mMemRole == 4) {
                //会员有会籍
                //第一篇
                mFabLeft.setVisibility(View.VISIBLE);
                mFabRight.setVisibility(View.VISIBLE);
                SPUtils.getString(Constant.MEMBERSHIP_INFORMATION);
                BeanMe.CardBean cardBean = mBeanMine.getCard();
                mTvClubTimeHide.setText(cardBean.getClubname());
                WebFragment webFragmentRichOne = new WebFragment();
                Bundle bundleRichOne = new Bundle();
//                    ordenddate=截止日期&clubname=俱乐部名称&
//                            coursenum=已上课时&gcoursetimes=总次数
                bundleRichOne.putString("url", "file:///android_asset/indexView_mem_card.html?viewHeight="
                        + mWebViewHeight + "&top=" + mWebTop + "&bottom=" + mWebBottom + "&ordenddate="
                        + TimesUtil.timestampToStringS(String.valueOf(cardBean.getOrdenddate().getTime()), "yyyy-MM-dd")
                        + "&clubname=" + cardBean.getClubname() + "&coursenum=" + cardBean.getCoursenum()
                        + "&gcoursetimes=" + cardBean.getGcoursetimes() + "&clubmem=" + cardBean.getClubmem());
                bundleRichOne.putBoolean("software", true);

                webFragmentRichOne.setArguments(bundleRichOne);
                adapter.addFragment(webFragmentRichOne, "1");

                //第二篇
                WebFragment webFragmentRichTwo = new WebFragment();
                Bundle bundleRichTwo = new Bundle();
                bundleRichTwo.putString("url", "file:///android_asset/indexView_mem_bespeak.html?viewHeight="
                        + mWebViewHeight + "&top=" + mWebTop + "&bottom=" + mWebBottom);
                webFragmentRichTwo.setArguments(bundleRichTwo);
                bundleRichTwo.putBoolean("software", true);
                adapter.addFragment(webFragmentRichTwo, "2");
                mViewPager.setAdapter(adapter);
                mPageIndicator.setViewPager(mViewPager);
//                mViewBarPlaceHolder.setBackgroundColor(0);
            }
        });
    }

    @Override
    public void initData() {
        Glide.with(getActivity()).load(mBeanMine.getMember().getImgsfilename())
                    .transform(new GlideCircleTransform(getActivity()))
                    .placeholder(R.mipmap.ic_place_holder)
                    .into(mIvClubAvatar);
    }

    @OnClick({R.id.fab_left, R.id.fab_right,
            R.id.iv_search, R.id.iv_two_code})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //向左箭头
            case R.id.fab_left:
                int currentItemLeft = mViewPager.getCurrentItem();
                if (currentItemLeft > 0) {
                    mViewPager.setCurrentItem(currentItemLeft - 1);
                }
                break;
            //向右箭头
            case R.id.fab_right:
                int currentItemRight = mViewPager.getCurrentItem();
                if (currentItemRight < mViewPager.getChildCount()) {
                    mViewPager.setCurrentItem(currentItemRight + 1);
                }
                break;
            //搜索
            case R.id.iv_search:
                break;
            //二维码-》现为扫一扫
            case R.id.iv_two_code:
                startActivityForResult(new Intent(getActivity(), ScanCodeActivity.class), 1);
                break;
            default:
                break;
        }
    }

    public static Fragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    static class Adapter extends FragmentPagerAdapter {
        private List<Fragment> mFragments = new ArrayList<>();
        private List<String> mFragmentTitles = new ArrayList<>();
        private FragmentManager mFragmentManager;
        Boolean flag = false;

        public Adapter(FragmentManager fm) {
            super(fm);
            mFragments = new ArrayList<>();
            mFragmentTitles = new ArrayList<>();
            mFragmentManager = fm;
        }

        public void setFlag(Boolean flag) {
            this.flag = flag;
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

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if (flag) {
                Fragment fragment = (Fragment) super.instantiateItem(container, position);
                String fragmentTag = fragment.getTag();
                FragmentTransaction ft = mFragmentManager.beginTransaction();
                if (fragment != null) {
                    ft.remove(fragment);
                }
                fragment = mFragments.get(position);
                ft.add(container.getId(), fragment, fragmentTag);
                ft.attach(fragment);
                ft.commit();
                return fragment;
            } else {
                return super.instantiateItem(container, position);
            }

        }
    }


    public class WebAppInterface extends BaseJsToAndroid {
        Context mContext;

        WebAppInterface(Context c) {
            super(c);
            mContext = c;
        }

        //parmType: 1:个人主页, 2:配餐单页, 3:webview;
//        parm:个人主页所需参数, 配餐单页所需参数, url;
        //1:memrole   tarid  (俱乐部)tarid
        //2:merid
        //3:url 一半
        @JavascriptInterface
        public void gotoNewWindow(String parmType, String parm) {
            Intent intent = new Intent();
            if (parmType.equals("1")) {
                try {
                    JSONObject object = new JSONObject(parm);
                    String memrole = object.getString("memrole");
                    String tarid = object.getString("tarid");
                    intent.putExtra("memrole", memrole);
                    intent.putExtra("tarid", tarid);
                    if (memrole.equals("clubadmin ")) {
                        intent.setClass(getActivity(), ClubDetailActivity.class);
                    } else {
                        intent.setClass(getActivity(), PersionalActivity.class);
                    }
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "gotoNewWindow的parm参数json解析失败", Toast.LENGTH_SHORT).show();
                }
            }
            if (parmType.equals("2")) {
                intent.putExtra("merid", parm);
                intent.setClass(getActivity(), CateringDetailActivity.class);
                startActivity(intent);
            }
            if (parmType.equals("3")) {
                intent.putExtra("url", "file:///android_asset/" + parm);
                intent.setClass(getActivity(), WebActivity.class);
                startActivity(intent);
            }
        }
    }
}
