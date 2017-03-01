package com.maxiaobu.healthclub.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
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
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.chat.DemoHelper;
import com.maxiaobu.healthclub.chat.db.UserDao;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanClubData;
import com.maxiaobu.healthclub.common.beangson.BeanEventBas;
import com.maxiaobu.healthclub.common.beangson.BeanMbclub;
import com.maxiaobu.healthclub.common.beangson.BeanMrelation;
import com.maxiaobu.healthclub.ui.fragment.ClubCourseFragment;
import com.maxiaobu.healthclub.ui.fragment.ClubDataFragment;
import com.maxiaobu.healthclub.ui.fragment.ClubDynamicFragment;
import com.maxiaobu.healthclub.ui.weiget.GlideCircleTransform;
import com.maxiaobu.healthclub.utils.TimesUtil;
import com.maxiaobu.healthclub.utils.storage.SPUtils;
import com.maxiaobu.volleykit.JsonUtils;
import com.maxiaobu.volleykit.RequestJsonListener;
import com.maxiaobu.volleykit.RequestListener;
import com.maxiaobu.volleykit.RequestParams;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import maxiaobu.easeui.domain.EaseUser;

/**
 * Created by 马小布 on 2017/1/19.
 * email：maxiaobu1216@gmail.com
 * 功能：俱乐部详情
 * 伪码：
 * 待完成：
 */
public class ClubDetailActivity extends BaseAty implements AppBarLayout.OnOffsetChangedListener, View.OnClickListener {
    @Bind(R.id.iv_header)
    ImageView mIvHeader;
    @Bind(R.id.iv_flag)
    ImageView mIvFlag;
    @Bind(R.id.rl_layout_head)
    RelativeLayout mRlLayoutHead;
    @Bind(R.id.tv_name)
    TextView mTvName;
    @Bind(R.id.tv_follow)
    TextView mTvFollow;
    @Bind(R.id.tv_fans)
    TextView mTvFans;
    @Bind(R.id.ll_layout_attation)
    LinearLayout mLlLayoutAttation;
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
    @Bind(R.id.view_pager)
    ViewPager mViewPager;
    @Bind(R.id.fab_pull_black)
    FloatingActionButton mFabPullBlack;
    @Bind(R.id.fab_subscribe)
    FloatingActionButton mFabSubscribe;
    @Bind(R.id.fab_bind)
    FloatingActionButton mFabBind;
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
    private Handler mUiHandler = new Handler();
    private int mPreviousVisibleItem;
    private String mNickname = "";
    private String mClubid = "";
    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;
    private EventBus mEventBus;
    private BeanMbclub.BBMemberBean mData;
    private String fans = new String();
    /**
     * 分清两种关注，是他关注我了，还是我关注他了
     * 0 ： 互相都没有关注
     * 1 ： 对方关注我了
     */
    private int tag = 0;

    public FloatingActionMenu getFabMenu() {
        return mFabMenu;
    }

    public FloatingActionButton getFabRelease() {
        return mFabRelease;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_detail);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    public void initView() {
        mShortAnimationDuration = 300;
        tag = 0;
        mEventBus = EventBus.getDefault();
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAppBar.addOnOffsetChangedListener(this);
        mCtlName.setTitle("");
        mFabMenu.hideMenuButton(false);
        mUiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mFabMenu.showMenuButton(true);
            }
        }, 400);
        if (mViewPager != null) {
            setupViewPager(mViewPager);
            mViewPager.setOffscreenPageLimit(2);
        }
        mTabs.setupWithViewPager(mViewPager);
        mTabs.setBackgroundResource(R.color.white);
        mTabs.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorOrange));

        if (getIntent().getStringExtra("tarid").equals(SPUtils.getString(Constant.MEMID))) {
            mFabMenu.setVisibility(View.GONE);
            mFabRelease.setVisibility(View.VISIBLE);
            mItemMutual.setVisibility(View.GONE);
        } else {
            mItemMutual.setVisibility(View.VISIBLE);
            mFabRelease.setVisibility(View.GONE);
            mFabMenu.hideMenuButton(false);
            mFabMenu.setVisibility(View.VISIBLE);
            mUiHandler.postDelayed(() -> mFabMenu.showMenuButton(true), 400);
        }


        mTvFans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClubDetailActivity.this, MyFansActivity.class);
                intent.putExtra("fans", getIntent().getStringExtra("tarid"));
                startActivity(intent);
            }
        });

        mTvFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClubDetailActivity.this, MyAttentionActivity.class);
                intent.putExtra("attent", getIntent().getStringExtra("tarid"));
                startActivity(intent);
            }
        });
        mItemMutual.setOnClickListener(this);
        mFabRelease.setOnClickListener(view -> {
            Intent intent = new Intent(ClubDetailActivity.this, SendDynamicActivity.class);
            startActivityForResult(intent, 101);
        });
    }


    @Override
    public void initData() {
        RequestParams params = new RequestParams();
        params.put("memid", SPUtils.getString(Constant.MEMID));
        params.put("socialrel.memid", getIntent().getStringExtra("tarid"));
//        Log.d("ClubDetailActivity", UrlPath.URL_CLUB_DETAIL + "?clubmemid=" + getIntent().getStringExtra("tarid"));
        App.getRequestInstance().post(this, UrlPath.URL_CLUB_DATA,
                params, new RequestListener() {
                    @Override
                    public void requestSuccess(String s) {
//                        Log.d("ClubDetailActivity", s);
                        BeanMbclub object = JsonUtils.object(s, BeanMbclub.class);
                        mData = object.getBBMember();
                        mClubid = mData.getClubid();
                        Glide.with(ClubDetailActivity.this).load(mData.getImgsfilename())//mData.getImgsfilename()
                                .transform(new GlideCircleTransform(mActivity)).placeholder(R.mipmap.ic_place_holder).into(mIvHeader);
                        mTvFans.setText("粉丝:" + mData.getFollowernum());//mData.getFollownum()
                        mTvFollow.setText("关注：" + mData.getFollownum());//mData.getConcernnum()

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
                        mNickname = mData.getNickname();//mData.getNickname()
                        mCtlName.setTitle(mData.getNickname());//mData.getNickname()
                        mTvName.setText(mData.getNickname());//mData.getNickname()
                        if (mData.getSignature().equals("")) {
                            mTvSignature.setText("暂无签名");
                        } else {
                            mTvSignature.setText(mData.getSignature());//mData.getSignature()
                        }
                        BeanClubData data = new BeanClubData();
                        data.setName(mData.getNickname());
                        data.setSign(mData.getSignature());
                        data.setSex(mData.getGendername());
                        data.setClub(mData.getClubname());
                        data.setMobPhone(mData.getMobphone());
                        data.setLocal(mData.getAddress());
                        data.setMen("俱乐部");
                        data.setBirth(TimesUtil.timestampToStringS(String.valueOf(mData.getBirthday().getTime()), "yyyy-hh-dd"));
                        mEventBus.post(data);
                    }

                    @Override
                    public void requestError(VolleyError volleyError, String s) {

                    }

                    @Override
                    public void noInternet(VolleyError volleyError, String s) {

                    }

                });

        params = new RequestParams();
        params.put("memid", SPUtils.getString(Constant.MEMID));
        params.put("tarid", getIntent().getStringExtra("tarid"));
        params.put("tarrole", "clubadmin");
        App.getRequestInstance().post(this, UrlPath.URL_MRELATION, BeanMrelation.class, params, new RequestJsonListener<BeanMrelation>() {
            @Override
            public void requestSuccess(BeanMrelation result) {
                if (null != result.getIsbind()) {
                    mFabBind.setVisibility(View.VISIBLE);
                    if (!result.getIsbind().equals("0")) {
                        mFabBind.setImageResource(R.mipmap.ic_fab_unbind);
                        mFabBind.setClickable(false);
                    }

                }
            }

            @Override
            public void requestError(VolleyError volleyError, String s) {

            }

            @Override
            public void noInternet(VolleyError volleyError, String s) {

            }

        });
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(ClubDataFragment.getInstance(), "资料");
        adapter.addFragment(ClubDynamicFragment.getInstance(getIntent().getStringExtra("tarid")), "动态");
        adapter.addFragment(ClubCourseFragment.getInstance(), "课程");
        viewPager.setAdapter(adapter);
    }

    @OnClick({R.id.fab_talk, R.id.fab_bind, R.id.iv_header})
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.fab_talk:
                String userId = getIntent().getStringExtra("tarid").toLowerCase();
                if (userId == SPUtils.getString(Constant.MEMID)) {
                    Toast.makeText(this, "自己不能和自己聊天", Toast.LENGTH_SHORT).show();
                } else if (getIntent().getBooleanExtra("fromChat", false)) {
                    this.finish();
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

                    intent.setClass(this, ChatActivity.class);
                    intent.putExtra(com.maxiaobu.healthclub.chat.Constant.EXTRA_USER_ID, userId);
                    startActivity(intent);
                }
                break;
            case R.id.fab_bind:
                intent.setClass(this, ApplyBindClubActivity.class);
                intent.putExtra("coachid", SPUtils.getString(Constant.MEMID));
                intent.putExtra("clubid", mClubid);
                startActivity(intent);
                break;
            case R.id.iv_header:
                zoomImageFromThumb(mIvHeader, mData.getImgpfilename());
                break;
            case R.id.item_mutual:
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

                break;
            default:
                break;
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

    @Override
    public void onBackPressed() {
        if (mIvDetail.getVisibility() == View.VISIBLE) {
            mIvDetail.performClick();
        } else {
            super.onBackPressed();
        }
    }

    private void zoomImageFromThumb(final View thumbView, String url) {
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        Glide.with(mActivity).load(url).placeholder(R.mipmap.ic_place_holder).into(mIvDetail);
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
        set
                .play(ObjectAnimator.ofFloat(mIvDetail, View.X, startBounds.left,
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
        mIvDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator.ofFloat(mIvDetail, View.X, startBounds.left))
                        .with(ObjectAnimator.ofFloat(mIvDetail, View.Y, startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(mIvDetail, View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(mIvDetail, View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
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
                set.start();
                mCurrentAnimator = set;
            }
        });
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

    public void setMutualInfo(String url, int ImageResouceId, String text, int TextColor, int BaseResouceId, String fansNum) {
        RequestParams params = new RequestParams();
        params.put("followid", getIntent().getStringExtra("tarid"));
        params.put("followerid", SPUtils.getString(Constant.MEMID));
        App.getRequestInstance().post(url, this, params, new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                try {
                    JSONObject object = new JSONObject(json);
                    String msgFlag = object.getString("msgFlag");
                    String msgContent = object.getString("msgContent");
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
}
