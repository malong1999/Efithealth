package com.maxiaobu.healthclub.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.chat.DemoHelper;
import com.maxiaobu.healthclub.chat.db.UserDao;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanEventBas;
import com.maxiaobu.healthclub.common.beangson.BeanmDynamicList;
import com.maxiaobu.healthclub.ui.fragment.CoachCourseFragment;
import com.maxiaobu.healthclub.ui.fragment.TrainerDataFragment;
import com.maxiaobu.healthclub.ui.fragment.TrainerDynamicFragment;
import com.maxiaobu.healthclub.ui.weiget.GlideCircleTransform;
import com.maxiaobu.healthclub.utils.storage.SPUtils;
import com.maxiaobu.volleykit.RequestListener;
import com.maxiaobu.volleykit.RequestParams;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import maxiaobu.easeui.domain.EaseUser;
import maxiaobu.mqldialoglibrary.dialog.MQLLoadingFragment;
import maxiaobu.mqldialoglibrary.materialdialogs.core.MaterialDialog;
import maxiaobu.mxbutilscodelibrary.NetworkUtils;
import maxiaobu.mxbutilscodelibrary.StringUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 马小布 on 2016/12/27.
 * email：maxiaobu1216@gmail.com
 * 功能：教练+会员个人页
 * 伪码：
 * 待完成：
 */
public class PersionalActivity extends BaseAty implements AppBarLayout.OnOffsetChangedListener, View.OnClickListener {
    @Bind(R.id.viewpager)
    ViewPager mViewpager;
    @Bind(R.id.iv_header)
    ImageView mIvHeader;
    @Bind(R.id.iv_flag)
    ImageView mIvFlag;
    @Bind(R.id.rl_layout_head)
    RelativeLayout mRlLayoutHead;
    @Bind(R.id.tv_name)
    TextView mTvName;
    @Bind(R.id.tv_fans)
    TextView mTvFans;
    @Bind(R.id.tv_follow)
    TextView mTvFollow;
    @Bind(R.id.item_mutual_image)
    ImageView mItemMutualImage;
    @Bind(R.id.item_mutual_text)
    TextView mItemMutualText;
    @Bind(R.id.item_mutual)
    LinearLayout mItemMutual;
    @Bind(R.id.tv_signature)
    TextView mTvSignature;
    @Bind(R.id.ly_bar)
    RelativeLayout mLyBar;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.ctl_name)
    CollapsingToolbarLayout mCtlName;
    @Bind(R.id.tabs)
    TabLayout mTabs;
    @Bind(R.id.app_bar)
    AppBarLayout mAppBar;
    @Bind(R.id.fab_pull_black)
    FloatingActionButton mFabPullBlack;
    @Bind(R.id.fab_subscribe)
    FloatingActionButton mFabSubscribe;
    @Bind(R.id.fab_talk)
    FloatingActionButton mFabTalk;
    @Bind(R.id.fab_menu)
    FloatingActionMenu mFabMenu;
    @Bind(R.id.fab_release)
    FloatingActionButton mFabRelease;
    @Bind(R.id.iv_detail)
    ImageView mIvDetail;
    @Bind(R.id.container)
    CoordinatorLayout mContainer;
    private int mShortAnimationDuration;
    private Handler mUiHandler = new Handler();
    private String mNickname = "";
    private String mMemrole;
    private BeanmDynamicList.BBMemberBean mData;
    private Animator mCurrentAnimator;
    private EventBus mEventBus;
    private String fans = new String();
    /**
     * 分清两种关注，是他关注我了，还是我关注他了
     * 0 ： 互相都没有关注
     * 1 ： 对方关注我了
     */
    private int tag = 0;
    public FloatingActionButton getFabRelease() {
        return mFabRelease;
    }
    public FloatingActionMenu getFabMenu() {
        return mFabMenu;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("PersionalActivity", "persionalActivty创建了");
        setContentView(R.layout.activity_persional);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("PersionalActivity", "persionalActivty销毁了");
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initView() {
        mShortAnimationDuration = 300;
        tag = 0;
        mEventBus = EventBus.getDefault();
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //appbar的滑动监听
        mAppBar.addOnOffsetChangedListener(this);
        mCtlName.setTitle("");

        String userId = getIntent().getStringExtra("tarid");
        //如果看的人是自己
        if (userId.equals(SPUtils.getString(Constant.MEMID))) {
            mFabMenu.setVisibility(View.GONE);
            mFabRelease.setVisibility(View.VISIBLE);
            mItemMutual.setVisibility(View.GONE);
            //看别人资料
        } else {
            mItemMutual.setVisibility(View.VISIBLE);
            mFabRelease.setVisibility(View.GONE);
            mFabMenu.hideMenuButton(false);
            mFabMenu.setVisibility(View.VISIBLE);
            mUiHandler.postDelayed(() -> mFabMenu.showMenuButton(true), 400);
        }
        mNickname = getIntent().getStringExtra("name");
        if (!StringUtils.isEmpty(mNickname)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Glide.with(PersionalActivity.this).load(getIntent().getStringExtra("picurl"))
                        .placeholder(App.getInstance().getDrawableHolder())
                        .skipMemoryCache(false)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .transform(new GlideCircleTransform(mActivity))
                        .into(mIvHeader);
            } else {
                try {
                    String picurl = getIntent().getStringExtra("picurl").replace("@!BMEMBER_P", "@!BMEMBER_S");
                    Glide.with(PersionalActivity.this).load(picurl)
                            .placeholder(R.mipmap.ic_person_default)
                            .transform(new GlideCircleTransform(mActivity))
                            .into(mIvHeader);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


            String msign = getIntent().getStringExtra("sign");
            mCtlName.setTitle(mNickname);
            mTvName.setText(mNickname);
            if (msign.equals("") || null == msign) {
                mTvSignature.setText("暂无签名");
            } else {
                mTvSignature.setText(msign);
            }
        }

        //判断此人员的身份
        String str = getIntent().getStringExtra("memrole");
        if (str == null) {
            // 判断身份的 当前用户
            mMemrole = SPUtils.getString(Constant.MEMROLE, "-1");
        } else {
//            其他用户
            mMemrole = str;
        }

        if (mMemrole.equals("coach")
                || getIntent().getBooleanExtra("tag", false)) {//教练 tag是指从教练列表跳过来的
            mViewpager.setVisibility(View.VISIBLE);
            mTabs.setVisibility(View.VISIBLE);
            if (mViewpager != null) {
                setupViewPager(mViewpager);
                mViewpager.setOffscreenPageLimit(2);
            }
            mIvFlag.setVisibility(View.VISIBLE);
            mTabs.setupWithViewPager(mViewpager);

        } else if (mMemrole.equals("mem")) {//普通人员
            mViewpager.setVisibility(View.VISIBLE);
            mTabs.setVisibility(View.VISIBLE);
            if (mViewpager != null) {
                setViewpager(mViewpager);
                mViewpager.setOffscreenPageLimit(1);
            }
            mIvFlag.setVisibility(View.GONE);
            mTabs.setupWithViewPager(mViewpager);

        }
        mTabs.setBackgroundResource(R.color.white);
        mTabs.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorOrange));

        mTvFans.setOnClickListener(view -> {
            Intent intent = new Intent(PersionalActivity.this, MyFansActivity.class);
            intent.putExtra("fans", getIntent().getStringExtra("tarid"));
            startActivity(intent);
        });

        mTvFollow.setOnClickListener(view -> {
            Intent intent = new Intent(PersionalActivity.this, MyAttentionActivity.class);
            intent.putExtra("attent", getIntent().getStringExtra("tarid"));
            startActivity(intent);
        });

        mItemMutual.setOnClickListener(this);
        mFabRelease.setOnClickListener(view -> {
            Intent intent = new Intent(PersionalActivity.this, SendDynamicActivity.class);
            startActivityForResult(intent, 101);
        });
    }

    /**
     * 公共数据请求
     */
    @Override
    public void initData() {
        Observable<JsonObject> objectObservable = App.getRetrofitUtil().getRetrofit()
                .getJsonDynamicList(getIntent().getStringExtra("tarid"),
                        SPUtils.getString(Constant.MEMID));
        objectObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<JsonObject>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        try {
                            new MaterialDialog.Builder(mActivity)
                                    .title("网络加载失败")
                                    .negativeColor(Color.parseColor("#Fb8435"))
                                    .positiveColor(Color.parseColor("#Fb8435"))
                                    .content("请检查网络设置")
                                    .negativeText("刷新")
                                    .positiveText("设置")
                                    .onPositive((dialog, which) -> NetworkUtils.openWirelessSettings(mActivity))
                                    .onNegative((dialog, which) -> {
                                        dialog.dismiss();
                                        initData();
                                    }).show();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(JsonObject jsonObject) {
                        BeanmDynamicList object = new Gson().fromJson(jsonObject, BeanmDynamicList.class);
                        mData = object.getBBMember();
                        mTvFans.setText("粉丝：" + mData.getFollowernum());
                        mTvFollow.setText("关注：" + mData.getFollownum());
                        fans = String.valueOf(mData.getFollowernum());
                        //两者都没有关注对方
                        if (mData.getSocialrel().equals("none")) {
                            mItemMutual.setBackgroundResource(R.drawable.bg_person);
                            mItemMutualImage.setImageResource(R.mipmap.ic_add_org);
                            mItemMutualText.setText("关注");
                            mItemMutualText.setTextColor(0xffFb8435);
                            tag = 0;
                            //我关注别人
                        } else if (mData.getSocialrel().equals("follow")) {
                            mItemMutual.setBackgroundResource(R.drawable.bg_person_press);
                            mItemMutualImage.setImageResource(R.mipmap.ic_add_add);
                            mItemMutualText.setText("已关注");
                            mItemMutualText.setTextColor(Color.WHITE);
                            //对方关注我了
                        } else if (mData.getSocialrel().equals("follower")) {
                            mItemMutual.setBackgroundResource(R.drawable.bg_person_press);
                            mItemMutualImage.setImageResource(R.mipmap.ic_mutualedd);
                            mItemMutualText.setText("关注");
                            tag = 1;
                            mItemMutualText.setTextColor(Color.WHITE);
                            //相互关注了
                        } else {
                            mItemMutual.setBackgroundResource(R.drawable.bg_person_press);
                            mItemMutualImage.setImageResource(R.mipmap.ic_mutual_white);
                            mItemMutualText.setTextColor(Color.WHITE);
                            mItemMutualText.setText("互相关注");
                        }

                        if (StringUtils.isEmpty(mNickname)) {
                            mNickname = mData.getNickname();
                            Glide.with(PersionalActivity.this).load(mData.getImgsfilename())
                                    .placeholder(R.mipmap.ic_place_holder)
                                    .transform(new GlideCircleTransform(mActivity)).into(mIvHeader);
                            String msign = mData.getSignature();
                            mCtlName.setTitle(mNickname);
                            mTvName.setText(mNickname);
                            if (msign.equals("") || null == msign) {
                                mTvSignature.setText("暂无签名");
                            } else {
                                mTvSignature.setText(msign);
                            }
                        }
                        mEventBus.post(mData);
                    }
                });
    }

    @OnClick({R.id.fab_talk, R.id.iv_header, R.id.fab_release, R.id.item_mutual})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_talk:
                clickTalk();
                break;
            case R.id.iv_header:
                clickHeader();
                break;
            case R.id.item_mutual:
                clickMutual();
                break;
            default:
                break;
        }
    }

    /**
     * toolbar滑动监听
     *
     * @param appBarLayout
     * @param verticalOffset
     */
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

        if (verticalOffset <= -mCtlName.getHeight() + mToolbar.getHeight() + 180) {
            mCtlName.setTitle(mNickname);
        } else {
            mCtlName.setTitle("");
        }
    }



    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(TrainerDataFragment.getInstance(), "资料");
        adapter.addFragment(TrainerDynamicFragment.getInstance(getIntent()
                .getStringExtra("tarid"), getIntent().getStringExtra("name"), getIntent()
                .getStringExtra("picurl")), "动态");
        adapter.addFragment(CoachCourseFragment.getInstance(), "课程");
        viewPager.setAdapter(adapter);
    }

    private void setViewpager(ViewPager viewpager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(TrainerDataFragment.getInstance(), "资料");
        adapter.addFragment(TrainerDynamicFragment.getInstance(getIntent()
                        .getStringExtra("tarid"), getIntent().getStringExtra("name"),
                getIntent().getStringExtra("picurl")), "动态");
        viewpager.setAdapter(adapter);
    }

    /**
     * 聊一聊点击事件
     */
    private void clickTalk() {
        try {
            String userId = getIntent().getStringExtra("tarid").toLowerCase();
            SPUtils.getString(Constant.MEMID).toLowerCase();
            if (userId.equals(SPUtils.getString(Constant.MEMID).toLowerCase())) {
                Toast.makeText(this, "自己不能和自己聊天", Toast.LENGTH_SHORT).show();
            } else if (getIntent().getBooleanExtra("fromChat", false)) {
                if (getIntent().getBooleanExtra("group_chat", false)) {
                    String nickName = mNickname;
                    String avatar = mData.getImgsfilename();
                    String hxIdFrom = userId;
                    EaseUser easeUser = new EaseUser(hxIdFrom);
                    easeUser.setAvatar(avatar);
                    easeUser.setNick(nickName);
                    // 存入内存
                    DemoHelper.getInstance().getContactList();
                    DemoHelper.getInstance().getContactList().put(hxIdFrom, easeUser);
                    // 存入db
                    UserDao dao = new UserDao(App.getInstance());
                    List<EaseUser> users = new ArrayList<EaseUser>();
                    users.add(easeUser);
                    dao.saveContactList(users);
                    DemoHelper.getInstance().getModel().setContactSynced(true);
                    // 通知listeners联系人同步完毕
                    DemoHelper.getInstance().notifyContactsSyncListener(true);
                    Intent intent = new Intent();
                    intent.setClass(this, ChatActivity.class);
                    intent.putExtra(com.maxiaobu.healthclub.chat.Constant.EXTRA_USER_ID, userId);
                    startActivity(intent);
                } else {
                    this.finish();
                }
            } else {
                String nickName = mNickname;
                String avatar = mData.getImgsfilename();
                String hxIdFrom = userId;
                EaseUser easeUser = new EaseUser(hxIdFrom);
                easeUser.setAvatar(avatar);
                easeUser.setNick(nickName);
                // 存入内存
                DemoHelper.getInstance().getContactList();
                DemoHelper.getInstance().getContactList().put(hxIdFrom, easeUser);
                // 存入db
                UserDao dao = new UserDao(App.getInstance());
                List<EaseUser> users = new ArrayList<EaseUser>();
                users.add(easeUser);
                dao.saveContactList(users);
                DemoHelper.getInstance().getModel().setContactSynced(true);
                // 通知listeners联系人同步完毕
                DemoHelper.getInstance().notifyContactsSyncListener(true);
                Intent intent = new Intent();
                intent.setClass(this, ChatActivity.class);
                intent.putExtra(com.maxiaobu.healthclub.chat.Constant.EXTRA_USER_ID, userId);
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 头像点击事件
     */
    private void clickHeader() {
        if (!StringUtils.isEmpty(getIntent().getStringExtra("picurl"))) {
            zoomImageFromThumb(mIvHeader, getIntent().getStringExtra("picurl"));
        } else if (!StringUtils.isEmpty(mData.getImgpfilename())) {
            zoomImageFromThumb(mIvHeader, mData.getImgpfilename());
        }
    }

    /**
     * 关注点击事件
     */
    private void clickMutual() {
        if (!fans.equals(""))
            if (mItemMutualText.getText().toString().equals("关注")) {
                if (tag == 0) {
                    String num = String.valueOf(Integer.valueOf(fans) + 1);
                    fans = num;
                    setMutualInfo(UrlPath.URL_FOLLOW, R.mipmap.ic_add_add, "已关注", Color.WHITE, R.drawable.bg_person_press, num);
                } else if (tag == 1) {
                    String num = String.valueOf(Integer.valueOf(fans) + 1);
                    fans = num;
                    setMutualInfo(UrlPath.URL_FOLLOW, R.mipmap.ic_mutual, "互相关注", Color.WHITE, R.drawable.bg_person_press, num);
                }
            } else if (mItemMutualText.getText().toString().equals("已关注")) {
                tag = 0;
                String num = String.valueOf(Integer.valueOf(fans) - 1);
                fans = num;
                setMutualInfo(UrlPath.URL_UNFOLLOW, R.mipmap.ic_add_org, "关注", 0xffFb8435, R.drawable.bg_person, num);
            } else if (mItemMutualText.getText().toString().equals("互相关注")) {
                tag = 1;
                String num = String.valueOf(Integer.valueOf(fans) - 1);
                fans = num;
                setMutualInfo(UrlPath.URL_UNFOLLOW, R.mipmap.ic_mutualedd, "关注", Color.WHITE, R.drawable.bg_person_press, num);
            }
    }


    @Override
    public void onBackPressed() {
        if (mIvDetail.getVisibility() == View.VISIBLE) {
            mIvDetail.performClick();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 查看详图
     * @param thumbView
     * @param url
     */
    private void zoomImageFromThumb(final View thumbView, String url) {
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }
        ImageView imageView = (ImageView) thumbView;
        Drawable drawable = imageView.getDrawable();
        Glide.with(mActivity)
                .load(url)
                .placeholder(drawable).into(mIvDetail);
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.container).getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        thumbView.setAlpha(0f);
        mIvDetail.setVisibility(View.VISIBLE);
        mIvDetail.setPivotX(0f);
        mIvDetail.setPivotY(0f);
        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(mIvDetail, View.X, startBounds.left,
                finalBounds.left))
                .with(ObjectAnimator.ofFloat(mIvDetail, View.Y, startBounds.top,
                        finalBounds.top))
                .with(ObjectAnimator.ofFloat(mIvDetail, View.SCALE_X, startScale, 1f))
                .with(ObjectAnimator.ofFloat(mIvDetail, View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;

            }
        });
        set.start();
        mCurrentAnimator = set;
        final float startScaleFinal = startScale;
        mIvDetail.setOnClickListener(view -> {
            if (mCurrentAnimator != null) {
                mCurrentAnimator.cancel();
            }

            AnimatorSet set1 = new AnimatorSet();
            set1.play(ObjectAnimator.ofFloat(mIvDetail, View.X, startBounds.left))
                    .with(ObjectAnimator.ofFloat(mIvDetail, View.Y, startBounds.top))
                    .with(ObjectAnimator
                            .ofFloat(mIvDetail, View.SCALE_X, startScaleFinal))
                    .with(ObjectAnimator
                            .ofFloat(mIvDetail, View.SCALE_Y, startScaleFinal));
            set1.setDuration(mShortAnimationDuration);
            set1.setInterpolator(new DecelerateInterpolator());
            set1.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    thumbView.setAlpha(1f);
                    mIvDetail.setVisibility(View.GONE);
                    mCurrentAnimator = null;
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    thumbView.setAlpha(1f);
                    mIvDetail.setVisibility(View.GONE);
                    mCurrentAnimator = null;
                }
            });
            set1.start();
            mCurrentAnimator = set1;
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (mFabMenu.isOpened())
                mFabMenu.close(true);
        }
        return super.dispatchTouchEvent(ev);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 101:
                BeanEventBas beanEventBas = new BeanEventBas();
                beanEventBas.setRefresh(true);
                mEventBus.post(beanEventBas);
                break;
        }
    }

    /**
     * 设置关注信息
     * @param url
     * @param ImageResouceId
     * @param text
     * @param TextColor
     * @param BaseResouceId
     * @param fansNum
     */
    public void setMutualInfo(String url, int ImageResouceId, String text,
                              int TextColor, int BaseResouceId, String fansNum) {
        RequestParams params = new RequestParams();
        params.put("followid", getIntent().getStringExtra("tarid"));
        params.put("followerid", SPUtils.getString(Constant.MEMID));
        App.getRequestInstance().post(url, this, params, new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                try {
                    JSONObject object = new JSONObject(json);
                    String msgFlag = object.getString("msgFlag");
//                    String msgContent = object.getString("msgContent");
//                    Toast.makeText(PersionalActivity.this, msgContent, Toast.LENGTH_SHORT).show();
                    if (msgFlag.equals("1")) {
                        mItemMutualImage.setImageResource(ImageResouceId);
                        mItemMutualText.setText(text);
                        mItemMutualText.setTextColor(TextColor);
                        mItemMutual.setBackgroundResource(BaseResouceId);
                        mTvFans.setText("粉丝：" + fansNum);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestError(VolleyError e, String error) {

            }

            @Override
            public void noInternet(VolleyError e, String error) {

            }
        });
    }

    //接EventBus的值，从MyAttenttionActivity与MyFansActivity传过来
    //当个人页是自己的时候，在粉丝列表与关注列表，取消关注和添加关注得刷新个人页的关注数量
    @Subscribe
    public void setValue(BeanEventBas beanEventBas){

        if(beanEventBas.getAddAttention() != null){
            mTvFollow.setText("关注：" + (mData.getFollownum() + 1 ));
            mData.setFollownum((mData.getFollownum() + 1 ));
        }

        if(beanEventBas.getCancleAttention() != null){
            mTvFollow.setText("关注：" + (mData.getFollownum() - 1 ));
            mData.setFollownum((mData.getFollownum() - 1 ));
        }
    }
}
