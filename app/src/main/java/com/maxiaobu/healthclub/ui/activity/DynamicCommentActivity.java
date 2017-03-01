package com.maxiaobu.healthclub.ui.activity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.google.gson.Gson;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.MainActivity;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.adapter.AdapterCommentDetailGridView;
import com.maxiaobu.healthclub.adapter.AdapterDynamicCommentDetail;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanCommentOnly;
import com.maxiaobu.healthclub.common.beangson.BeanDynamicCommentDetail;
import com.maxiaobu.healthclub.common.beangson.BeanEventBas;
import com.maxiaobu.healthclub.common.beangson.BeanLoveList;
import com.maxiaobu.healthclub.common.beangson.BeanMineDynamic;
import com.maxiaobu.healthclub.ui.weiget.RecyclerViewScrollDetector;
import com.maxiaobu.healthclub.ui.weiget.refresh.LoadMoreFooterView;
import com.maxiaobu.healthclub.ui.weiget.refresh.RefreshHeaderView;
import com.maxiaobu.healthclub.utils.storage.SPUtils;
import com.maxiaobu.volleykit.RequestListener;
import com.maxiaobu.volleykit.RequestParams;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import maxiaobu.mxbutilscodelibrary.ConstUtils;

import static android.R.string.no;

/**
 * Created by 莫小婷 on 2016/12/21.
 * 动态详情页
 *
 */
public class DynamicCommentActivity extends BaseAty implements OnRefreshListener, OnLoadMoreListener, View.OnClickListener, TextWatcher {
    @Bind(R.id.tv_title_common)
    TextView mTvTitleCommon;
    @Bind(R.id.toolbar_common)
    Toolbar mToolbarCommon;
    @Bind(R.id.swipe_refresh_header)
    RefreshHeaderView mSwipeRefreshHeader;
    @Bind(R.id.swipe_target)
    RecyclerView mSwipeTarget;
    @Bind(R.id.swipe_load_more_footer)
    LoadMoreFooterView mSwipeLoadMoreFooter;
    @Bind(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;
    @Bind(R.id.send_text)
    TextView mSendText;
    @Bind(R.id.image_text)
    ImageView mImageText;
    @Bind(R.id.edit_text)
    EditText mEditText;
    @Bind(R.id.rl_layout)
    RelativeLayout mRlLayout;
    private AdapterDynamicCommentDetail mAdapterDynamicCommentDetail;
    /**
     * 评论信息列表
     */
    private List<BeanDynamicCommentDetail.PostBean.CommentlistBean> mCommentDetails;
    /**
     * 动态详情的头部信息（除评论以外的）
     */
    private List<BeanMineDynamic> headInfoList;
    /**
     * 当前页面，默认为1
     * 用来加载更多评论的
     */
    private int currentPage;
    /**
     * 0:刷新  1：加载  2：只更新评论信息
     */
    private int type;
    /**
     * 系统软件盘对象
     */
    private InputMethodManager mImm;

    private ClipboardManager myClipboard;
    private ClipData myClip;
    /**
     * 点击动态详情的更多（头部里的三个点）出现的popWindow
     */
    private PopupWindow mPopupWindow;
    /**
     * 点击每一条评论出现的popWindow
     */
    private PopupWindow mPop;
    /**
     * 标志该动态是否被点赞
     * false 未点赞 true 点赞了
     */
    private Boolean mBooleen=false;

    /**
     * 整个动态列表的所有信息 包括所有评论信息和头部信息
     */
    private BeanDynamicCommentDetail mBean;
    /**
     * 该评论的点赞数
     */
    private List<BeanLoveList> LikeList;

    /**
     * EventBus对象
     * 用来更新其他页动态的数据
     * 例如：用来给HotDynamicFragment传递点赞数信息和评论数信息，更新HotDynamicFragment
     */
    private EventBus mEventBus;
    /**
     * 点赞数
     * 用来更新TrainerDynamicFragment、DynamicFragment、HotDynamicFragment的点赞数
     */
    private int number;

    /**
     * 评论数
     * 用来更新TrainerDynamicFragment、DynamicFragment、HotDynamicFragment的评论数
     */
    private int commentNum;
    private String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_comment_detail);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    public void initView() {
        setCommonBackToolBar(mToolbarCommon, mTvTitleCommon, "动态详情");
        /**
         * 0:刷新  1：加载  2：只更新评论信息
         */
        type = 0;
        currentPage = 1;
        mEventBus = EventBus.getDefault();
        number = getIntent().getIntExtra("loveNum",0);
        commentNum = getIntent().getIntExtra("commentNum",0);
        mSwipeTarget.setLayoutManager(new LinearLayoutManager(this));
        mCommentDetails = new ArrayList<>();
        headInfoList = new ArrayList<>();
        LikeList = new ArrayList<>();
        mAdapterDynamicCommentDetail = new AdapterDynamicCommentDetail(mCommentDetails, this);
        mAdapterDynamicCommentDetail.setHeadInfoList(headInfoList);
        mAdapterDynamicCommentDetail.setLikeList(LikeList);
        mAdapterDynamicCommentDetail.setLikeNumber(number);
        mSwipeToLoadLayout.setOnRefreshListener(this);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
        mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        mSwipeTarget.setOnScrollListener(new RecyclerViewScrollDetector() {
            @Override
            public void onHide() {
                hideLayout();
            }

            @Override
            public void onShow() {
                showLayout();
            }

            @Override
            public void onScroll() {

            }

        });

        mAdapterDynamicCommentDetail.setBooleen(mBooleen);
        //点击头布局的评论图标，弹出或隐藏软键盘
        mAdapterDynamicCommentDetail.setOnClickCommentLayout(view1 -> {
            showLayout();
            if (mEditText.hasFocus() && mImm.isActive()) {
                mEditText.clearFocus();
                mImm.hideSoftInputFromInputMethod(view1.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            } else {
                mEditText.setFocusable(true);
                mEditText.requestFocus();
                mEditText.setFocusableInTouchMode(true);
                mImm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
            }

        });
        //点击头布局的分享图标，弹出分享界面
        mAdapterDynamicCommentDetail.setOnClickShareLayout(() -> {
            ShareBoardConfig config = new ShareBoardConfig();
            config.setIndicatorColor(0x00E9EFF2, 0xffE9EFF2);

            new ShareAction(DynamicCommentActivity.this)
                    .setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE
                            , SHARE_MEDIA.SMS, SHARE_MEDIA.EMAIL)
                    .addButton("umeng_sharebutton_custom", "umeng_sharebutton_custom", "info_icon_1", "info_icon_1")
                    .setShareboardclickCallback(shareBoardlistener)
                    .open(config);
        });
        //点击头布局的更多图标，弹出pop
        mAdapterDynamicCommentDetail.setOnClickMoreLayout((postId) -> {
            mPopupWindow = creatPop(postId);
            mPopupWindow.showAtLocation(findViewById(R.id.ly_root), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        });
        //点击头布局里的点赞列表，跳转到点赞列表
        mAdapterDynamicCommentDetail.setOnClickGoodList(() -> {
            // TODO: 2016/12/24 到点赞列表需要有什么参数吗？
            Intent intent = new Intent(DynamicCommentActivity.this, PointsListActivity.class);
            intent.putExtra("PointsContent",str);
            startActivity(intent);
        });
        //点击点赞，设置点赞状态：是红心还是灰心
        mAdapterDynamicCommentDetail.setOnClickGoodLayout((imageView, list, adapter, textView) -> {
            if (!mBooleen) {
                clickLike(true, mBean.getPost().getPostid(), mBean.getPost().getVisiable(), SPUtils.getString(Constant.MEMID), imageView, list, adapter, textView);

            } else {
                clickLike(false, mBean.getPost().getPostid(), mBean.getPost().getVisiable(), SPUtils.getString(Constant.MEMID), imageView, list, adapter, textView);
            }

        });
        //点击动态图片，跳转到图片全屏展示页
        mAdapterDynamicCommentDetail.setOnClickImageLayout(image -> {
            Intent intent = new Intent(DynamicCommentActivity.this, ImageCheckActivity.class);
            intent.putExtra("image", image);
            startActivity(intent);
        });
        //点击Item评论，弹出pop
        mAdapterDynamicCommentDetail.setOnClickLayout((commentId, authId, pos) -> {

            mPop = creatPopWindow(commentId, authId, pos);
            mPop.showAtLocation(findViewById(R.id.ly_root), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

        });
        //点击头布局的动态作者头像，跳转到个人页
        mAdapterDynamicCommentDetail.setOnListenHeadImage((pos, authId, authMemrole) -> {
            Intent intent = new Intent(DynamicCommentActivity.this, PersionalActivity.class);
            intent.putExtra("tarid", authId);
            intent.putExtra("memrole", authMemrole);
            startActivity(intent);
        });
        //点击评论者的头像，跳转到个人页
        mAdapterDynamicCommentDetail.setOnListenCommentHeadImage((pos, authId, authMemrole) -> {
            Intent intent = new Intent(DynamicCommentActivity.this, PersionalActivity.class);
            intent.putExtra("tarid", authId);
            intent.putExtra("memrole", authMemrole);
            startActivity(intent);
        });
        mSwipeTarget.setAdapter(mAdapterDynamicCommentDetail);

        mEditText.addTextChangedListener(this);
        mSendText.setOnClickListener(this);
    }

    private void showLayout() {
        if (mRlLayout != null && mRlLayout.getVisibility() == View.GONE) {
            mRlLayout.setVisibility(View.INVISIBLE);
            Animation translateAnimation = new TranslateAnimation(mRlLayout.getLeft(), mRlLayout.getLeft(), 30, 0);
            translateAnimation.setDuration(300);
            translateAnimation.setInterpolator(new OvershootInterpolator(0.6f));
            mRlLayout.startAnimation(translateAnimation);
            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                    mRlLayout.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    private void hideLayout() {

        if (mRlLayout != null && mRlLayout.getVisibility() == View.VISIBLE) {
            Animation translateAnimation = new TranslateAnimation(mRlLayout.getLeft(), mRlLayout.getLeft(), 0, mRlLayout.getHeight());
            translateAnimation.setDuration(300);
            translateAnimation.setInterpolator(new OvershootInterpolator(0.6f));
            mRlLayout.startAnimation(translateAnimation);
            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                    mRlLayout.setVisibility(View.GONE);
                }
            });
        }

    }

    @Override
    public void initData() {
//        Log.d("mytt", "" + getIntent().getIntExtra("postindex",0));
//        Log.d("mytt", getIntent().getStringExtra("postid"));
//        Log.d("mytt", getIntent().getStringExtra("visiable"));
        RequestParams params = new RequestParams();
        params.put("memid", SPUtils.getString(Constant.MEMID));
        params.put("postid", getIntent().getStringExtra("postid"));
        params.put("visiable", getIntent().getStringExtra("visiable"));
        params.put("postindex", String.valueOf(getIntent().getIntExtra("postindex", 0)));
        App.getRequestInstance().post(UrlPath.URL_COMMEND, this, params, new RequestListener() {
            @Override
            public void requestSuccess(String json) {
//                Log.d("DynamicCommentActivity", json);
                str = json;
                mBean = new Gson().fromJson(json, BeanDynamicCommentDetail.class);
                //刷新（全部刷新）
                if (type == 0) {
                    if (mSwipeToLoadLayout != null)
                        mSwipeToLoadLayout.setRefreshing(false);
                    mCommentDetails.clear();
                    headInfoList.clear();
                    LikeList.clear();
                    BeanMineDynamic b = new BeanMineDynamic();
                    b.setImage(mBean.getPost().getAuthimgsfilename());
                    b.setName(mBean.getPost().getAuthnickname());
                    b.setCreateTime(getFriendlyTimeSpanByNow(mBean.getPost().getCreatetime().getTime()));
                    b.setContentText(mBean.getPost().getContent());
                    b.setContentImage(mBean.getPost().getImgsfilename());
                    b.setBigImage(mBean.getPost().getImgpfilename());
                    b.setIslike(mBean.getPost().getIslike());
                    b.setAuthId(mBean.getPost().getAuthid());
                    b.setPostId(mBean.getPost().getPostid());
                    b.setMemrole(mBean.getPost().getMemrole());
                    if (mBean.getPost().getIslike() == 1) {
                        mBooleen = true;
                    } else {
                        mBooleen = false;
                    }
                    for (int i = 0; i < mBean.getPost().getLikelist().size(); i++) {
                        BeanLoveList beanLoveList = new BeanLoveList();
                        beanLoveList.setImage(mBean.getPost().getLikelist().get(i).getAuthimgsfilename());
                        beanLoveList.setAuthid(mBean.getPost().getLikelist().get(i).getMemid());
                        beanLoveList.setMemrole(mBean.getPost().getLikelist().get(i).getMemrole());
                        LikeList.add(beanLoveList);
                    }
                    number = LikeList.size();
                    headInfoList.add(b);
                    mCommentDetails.addAll(mBean.getPost().getCommentlist());
                    mAdapterDynamicCommentDetail.setBooleen(mBooleen);
                    mAdapterDynamicCommentDetail.setLikeNumber(number);
                    mAdapterDynamicCommentDetail.notifyDataSetChanged();

                    //刷新（只刷新评论）
                } else if (type == 2) {
                    mCommentDetails.add(0, mBean.getPost().getCommentlist().get(0));
                    commentNum = mCommentDetails.size();
                    mAdapterDynamicCommentDetail.notifyDataSetChanged();
                }
                //加载
                else {
                    if (mSwipeToLoadLayout != null)
                        mSwipeToLoadLayout.setLoadingMore(false);
                    Toast.makeText(DynamicCommentActivity.this, "没有更多数据了", Toast.LENGTH_SHORT).show();
                    currentPage--;
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

    @Override
    public void onRefresh() {
        currentPage = 1;
        type = 0;
        if (mSwipeToLoadLayout != null) {
            mSwipeToLoadLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    initData();
                }
            }, 2);
        }
    }

    @Override
    public void onLoadMore() {
        currentPage++;
        type = 1;
        mSwipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                initData();
            }
        });
    }

    /**
     * 点击事件来判断是否隐藏软键盘
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View view = getCurrentFocus();
            if (isHideInput(view, ev)) {
                hideSoftInput(view);
            }
        }
        return super.dispatchTouchEvent(ev);

    }


    // 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
    private boolean isHideInput(View v, MotionEvent ev) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (ev.getX() > left && ev.getX() < right && ev.getY() > top
                    && ev.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    // 隐藏软键盘
    private void hideSoftInput(View view) {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager != null) {
            manager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    /***
     * 分享面板点击监听
     */
    private ShareBoardlistener shareBoardlistener = new ShareBoardlistener() {

        @Override
        public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
            if (share_media == null) {
                if (snsPlatform.mKeyword.equals("umeng_sharebutton_custom")) {
                    myClip = ClipData.newPlainText("text", "https://www.baidu.com/");
                    myClipboard.setPrimaryClip(myClip);
                    Toast.makeText(DynamicCommentActivity.this, "复制成功", Toast.LENGTH_LONG).show();
                }

            } else {
                new ShareAction(DynamicCommentActivity.this).setPlatform(share_media)
                        .withTitle("efitHeath分享")
                        //必须写，否者出不来
                        /**
                         * 打包成功的友盟分享的微信出不来：
                         *         1、必须写出withText()
                         *
                         */
                        .withText("百度一下，你就知道")
                        .withTargetUrl("https://www.baidu.com/")  //分享http://或者https://
                        .setCallback(umShareListener)
                        .share();
            }
        }
    };

    /**
     * 分享监听
     */
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {

            Toast.makeText(DynamicCommentActivity.this, "分享成功", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(DynamicCommentActivity.this, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if (t != null) {
                com.umeng.socialize.utils.Log.d("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(DynamicCommentActivity.this, "分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(DynamicCommentActivity.this).onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 点击动态详情的更多（头部里的三个点）出现的popWindow
     */
    private PopupWindow creatPop(String postId) {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pop_layout, null);

        Button send = (Button) view.findViewById(R.id.popmenu_send);
        Button cancle = (Button) view.findViewById(R.id.popmenu_cancel);
        Button delete = (Button) view.findViewById(R.id.popmenu_delete);

        if (headInfoList.get(0).getAuthId().equals(SPUtils.getString(Constant.MEMID))) {
            delete.setVisibility(View.VISIBLE);
        } else {
            delete.setVisibility(View.GONE);
        }
        send.setOnClickListener(this);
        cancle.setOnClickListener(this);
        delete.setOnClickListener(view1 -> {
            deleteDynamic(postId,mBean.getPost().getVisiable());
        });

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        PopupWindow popupWindow = new PopupWindow(view, display.getWidth(), display.getHeight(), true);

        popupWindow.setFocusable(true);
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        Drawable drawable = new ColorDrawable(0x99000000);
        popupWindow.setBackgroundDrawable(drawable);

        view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                popupWindow.dismiss();
                return true;
            }
        });

        return popupWindow;
    }
    private void deleteDynamic(String postid,String visiable){
        RequestParams requestParams = new RequestParams();
        requestParams.put("post.postid",postid);
        requestParams.put("post.visiable",visiable);
        App.getRequestInstance().post(this, UrlPath.URL_DEL_POST, requestParams, new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                try {
                    android.util.Log.d("TrainerDynamicFragment", json);
                    JSONObject object = new JSONObject(json);
                    String msgFlag = object.getString("msgFlag");
                    if(msgFlag.equals("1")){
                        BeanEventBas beanEventBas = new BeanEventBas();
                        beanEventBas.setCommentDelete(true);
                        beanEventBas.setCommentPostId(postid);
                        beanEventBas.setCommentFlag(true);
                        mEventBus.post(beanEventBas);
                        mPopupWindow.dismiss();
                        finish();
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

    /**
     * 点击每一条评论出现的popWindow
     */
    private PopupWindow creatPopWindow(String commentId, String authId, int pos) {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popwindow_layout, null);

        Button send = (Button) view.findViewById(R.id.pop_send);
        Button cancle = (Button) view.findViewById(R.id.pop_cancel);
        Button delete = (Button) view.findViewById(R.id.pop_delete);

        if (headInfoList.get(0).getAuthId().equals(SPUtils.getString(Constant.MEMID))) {
            delete.setVisibility(View.VISIBLE);
            send.setVisibility(View.VISIBLE);
        } else {
            if (authId.equals(SPUtils.getString(Constant.MEMID))) {
                delete.setVisibility(View.VISIBLE);
                send.setVisibility(View.GONE);
            } else {
                delete.setVisibility(View.GONE);
                send.setVisibility(View.VISIBLE);
            }
        }

        send.setOnClickListener(this);
        cancle.setOnClickListener(this);
        delete.setOnClickListener(view1 -> {
            mPop.dismiss();
            mCommentDetails.remove(pos);
            mAdapterDynamicCommentDetail.notifyDataSetChanged();
        });

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        PopupWindow popupWindow = new PopupWindow(view, display.getWidth(), display.getHeight(), true);

        popupWindow.setFocusable(true);
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        Drawable drawable = new ColorDrawable(0x99000000);
        popupWindow.setBackgroundDrawable(drawable);

        view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                popupWindow.dismiss();
                return true;
            }
        });
        return popupWindow;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.popmenu_send:
                // TODO: 2016/12/23 举报他
                mPopupWindow.dismiss();
                break;

            case R.id.popmenu_cancel:
                mPopupWindow.dismiss();
                break;
            case R.id.pop_cancel:
                mPop.dismiss();
                break;
            case R.id.pop_send:
                // TODO: 2017/1/19 举报他
                mPop.dismiss();
                break;
            //发送评论
            case R.id.send_text:
                if (mEditText.getText().toString().equals("")) {

                    Toast.makeText(this, "说点什么吧", Toast.LENGTH_SHORT).show();

                } else {
                    RequestParams params = new RequestParams();
                    params.put("post.authid", SPUtils.getString(Constant.MEMID));
                    params.put("post.content", mEditText.getText().toString());
                    params.put("postid", getIntent().getStringExtra("postid"));
                    params.put("visiable", getIntent().getStringExtra("visiable"));
                    params.put("tarmemid", getIntent().getStringExtra("authid"));
                    params.put("postindex", String.valueOf(getIntent().getIntExtra("postindex", 0)));
//                    Log.d("myt--->authid:", getIntent().getStringExtra("authid"));
//                    Log.d("myt", UrlPath.URL_SEND_COMMEND + "?post.authid=" + SPUtils.getString(Constant.MEMID) + "&tarmemid=" + getIntent().getStringExtra("authid") + "&post.content=" + mEditText.getText().toString() + "&postid=" + getIntent().getStringExtra("postid") + "&visiable=" + getIntent().getStringExtra("visiable"));
                    App.getRequestInstance().post(UrlPath.URL_SEND_COMMEND, this, params, new RequestListener() {
                        @Override
                        public void requestSuccess(String json) {
                            try {
                                JSONObject object = new JSONObject(json);
                                String msgFlag = object.getString("msgFlag");
                                if (msgFlag.equals("1")) {
                                    mEditText.setText("");
                                    type = 2;
                                    initData();
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

                break;
        }
    }

    /**
     * 返回更新点赞数和评论数
     */
    @Override
    public void onBackPressed() {
        BeanEventBas beanEventBas = new BeanEventBas();
        beanEventBas.setPos(getIntent().getIntExtra("postindex", 0));
        beanEventBas.setBoolean(mBooleen);
        beanEventBas.setNum(String.valueOf(number));
        beanEventBas.setPostId(getIntent().getStringExtra("postid"));
        beanEventBas.setF(true);
        beanEventBas.setCommentNum(String.valueOf(commentNum));
        mEventBus.post(beanEventBas);
        super.onBackPressed();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!s.toString().equals("")) {
            mSendText.setTextColor(0xffFb8435);
        } else {
            mSendText.setTextColor(0xff585858);
        }
    }

    /**
     * 获取友好型与当前时间的差
     *
     * @param millis 毫秒时间戳
     * @return 友好型与当前时间的差
     * <ul>
     * <li>如果小于1秒钟内，显示刚刚</li>
     * <li>如果在1分钟内，显示XXX秒前</li>
     * <li>如果在1小时内，显示XXX分钟前</li>
     * <li>如果在1小时外的今天内，显示今天15:32</li>
     * <li>如果是昨天的，显示昨天15:32</li>
     * <li>其余显示，2016-10-15</li>
     * <li>时间不合法的情况全部日期和时间信息，如星期六 十月 27 14:21:20 CST 2007</li>
     * </ul>
     */
    @SuppressLint("DefaultLocale")
    public String getFriendlyTimeSpanByNow(long millis) {
        long now = System.currentTimeMillis();
        long span = now - millis;
        if (span < 0)
            return String.format("%tc", millis);// U can read http://www.apihome.cn/api/java/Formatter.html to understand it.
        if (span < 1000) {
            return "刚刚";
        } else if (span < ConstUtils.MIN) {
            return String.format("%d秒前", span / ConstUtils.SEC);
        } else if (span < ConstUtils.HOUR) {
            return String.format("%d分钟前", span / ConstUtils.MIN);
        }

        // 获取当天00:00
        long wee = (now / ConstUtils.DAY) * ConstUtils.DAY;
        if (millis >= wee) {
            long day = span / (24 * 60 * 60 * 1000);
            long hour = (span / (60 * 60 * 1000) - day * 24);
            return hour + "小时前";
        } else if (millis >= wee - ConstUtils.DAY) {
            return "昨天";
        } else {
            long day = span / (24 * 60 * 60 * 1000);
            if (day >= 30) {
                long mon = day / 30;
                return mon + "个月前";
            } else {
                return day + "天前";
            }
        }

    }

    /**
     * 点赞
     *
     * @param doLike   ture点赞  flase 取消点赞
     * @param postid
     * @param visiable
     * @param memid
     */
    private void clickLike(boolean doLike, String postid, String visiable, String memid, ImageView imageView, List<BeanLoveList> list, AdapterCommentDetailGridView adapter, TextView textView) {
        String url = doLike ? UrlPath.URL_SEND_LIKE : UrlPath.URL_UNLIKE;
        RequestParams params = new RequestParams();
        params.put("postid", postid);
        params.put("visiable", visiable);
        params.put("memid", memid);
        App.getRequestInstance().post(url, this, params, new RequestListener() {
            @Override
            public void requestSuccess(String json) {
//                    Toast.makeText(mActivity, json, Toast.LENGTH_LONG).show();
                try {
                    JSONObject object = new JSONObject(json);
                    String msgFlag = object.getString("msgFlag");
                    if (msgFlag.equals("1")) {
                        if (doLike) {
                            if(list.size() != 0){
                                for (int i = 0; i < list.size(); i++) {
                                    if(list.get(i).getImage().equals(SPUtils.getString(Constant.AVATAR))){
                                        break;
                                    }else if(i == list.size() -1){
                                        imageView.setImageResource(R.mipmap.ic_dynamic_good_dw);
                                        BeanLoveList beanLoveList  = new BeanLoveList();
                                        beanLoveList.setImage(SPUtils.getString(Constant.AVATAR));
                                        beanLoveList.setAuthid(SPUtils.getString(Constant.MEMID));
                                        beanLoveList.setMemrole(SPUtils.getString(Constant.MEMROLE));
                                        list.add(0,beanLoveList);
                                        textView.setText(list.size() + "点赞");
                                        number = list.size();
                                        adapter.notifyDataSetChanged();
//                                      mAdapterDynamicCommentDetail.notifyItemRangeInserted(0,1);
                                        mBooleen = true;
                                        mAdapterDynamicCommentDetail.setBooleen(mBooleen);
                                        mAdapterDynamicCommentDetail.setLikeNumber(number);
                                        break;
                                    }
                                }
                            }else {
                                imageView.setImageResource(R.mipmap.ic_dynamic_good_dw);
                                BeanLoveList beanLoveList  = new BeanLoveList();
                                beanLoveList.setImage(SPUtils.getString(Constant.AVATAR));
                                beanLoveList.setAuthid(SPUtils.getString(Constant.MEMID));
                                beanLoveList.setMemrole(SPUtils.getString(Constant.MEMROLE));
                                list.add(0,beanLoveList);
                                textView.setText(list.size() + "点赞");
                                number = list.size();
                                adapter.notifyDataSetChanged();
//                            mAdapterDynamicCommentDetail.notifyItemRangeInserted(0,1);
                                mBooleen = true;
                                mAdapterDynamicCommentDetail.setBooleen(mBooleen);
                                mAdapterDynamicCommentDetail.setLikeNumber(number);
                            }


                        } else {

                            for (int i = 0; i < list.size(); i++) {
                                if (list.get(i).getImage().equals(SPUtils.getString(Constant.AVATAR))) {
                                    imageView.setImageResource(R.mipmap.ic_dynamic_good_df);
                                    list.remove(i);
                                    textView.setText(list.size() + "点赞");
                                    number = list.size();
                                    adapter.notifyDataSetChanged();
//                            mAdapterDynamicCommentDetail.notifyItemRangeInserted(0,1);
                                    mBooleen = false;
                                    mAdapterDynamicCommentDetail.setBooleen(mBooleen);
                                    mAdapterDynamicCommentDetail.setLikeNumber(number);
                                    break;
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestError(VolleyError e, String error) {
                Toast.makeText(mActivity, error, Toast.LENGTH_LONG).show();
            }

            @Override
            public void noInternet(VolleyError e, String error) {

            }
        });

    }

}
