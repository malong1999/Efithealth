package com.maxiaobu.healthclub.ui.fragment;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.github.clans.fab.FloatingActionButton;
import com.google.gson.Gson;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseFrg;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.adapter.AdapterCommentDetailGridView;
import com.maxiaobu.healthclub.adapter.AdapterDynamicList;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanDynamic;
import com.maxiaobu.healthclub.common.beangson.BeanEventBas;
import com.maxiaobu.healthclub.common.beangson.BeanMineDynamic;
import com.maxiaobu.healthclub.ui.activity.DynamicCommentActivity;
import com.maxiaobu.healthclub.ui.activity.ImageCheckActivity;
import com.maxiaobu.healthclub.ui.activity.MyFansActivity;
import com.maxiaobu.healthclub.ui.activity.PersionalActivity;
import com.maxiaobu.healthclub.ui.activity.SendDynamicActivity;
import com.maxiaobu.healthclub.ui.weiget.NoDoubleClickListener;
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
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.ShareBoardlistener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import maxiaobu.mxbutilscodelibrary.ConstUtils;

import static android.R.attr.breadCrumbShortTitle;
import static android.R.attr.data;
import static android.content.Context.CLIPBOARD_SERVICE;


/**
 * Created by 莫小婷 on 2016/12/9.
 * 发现---好友动态
 */

public class DynamicFragment extends BaseFrg implements OnLoadMoreListener, OnRefreshListener, View.OnClickListener, AdapterDynamicList.OnListenItemClick {
    @Bind(R.id.swipe_refresh_header)
    RefreshHeaderView mSwipeRefreshHeader;
    @Bind(R.id.swipe_target)
    RecyclerView mSwipeTarget;
    @Bind(R.id.swipe_load_more_footer)
    LoadMoreFooterView mSwipeLoadMoreFooter;
    @Bind(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;
    @Bind(R.id.fab_send_discovery)
    FloatingActionButton mFabSendDiscovery;

    /**
     * 当前页面，默认为1
     */
    private int currentPage;
    /**
     * 0：刷新
     * 1：加载
     */
    private int type;
    private AdapterDynamicList mAdapterDynamicList;
    /**
     * 动态列表的数据List集合
     */
    private List<BeanMineDynamic> mLists;
    /**
     * 每个item的点赞标志
     */
    private List<Boolean> mBooleen;
    /**
     * 点击三个点，弹出的pop
     */
    private PopupWindow mPopupWindow;
    /**
     * 复制粘贴类  分享里的按钮
     */
    private ClipboardManager myClipboard;
    /**
     * 复制粘贴类  分享里的按钮
     */
    private ClipData myClip;
//    private Animator mCurrentAnimator;
//    private int mShortAnimationDuration = 300;
    /**
     * 每个item的点赞数量标志（没有使用）
     */
    private List<Integer> num;
    /**
     * 权限，是否只看私密动态的权限，
     * 从发现弹出的Dialog中设置。 传到本Fragment来设置动态列表信息
     * 默认为 1
     */
    private String visiable;
    /**
     * EventBus对象
     * 用来给HotDynamicFragment传递点赞信息，更新HotDynamicFragment
     */
    private EventBus mEventBus;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_discovery_dynamic, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        initView();
        initData();
        return view;
    }

    @Override
    public void initView() {
        /**
         * 0:刷新  1：加载
         */
        type = 0;
        currentPage = 1;
        visiable = "1";
        mEventBus = EventBus.getDefault();
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
        mSwipeToLoadLayout.setOnRefreshListener(this);
        mSwipeTarget.setLayoutManager(new LinearLayoutManager(getActivity()));
        mLists = new ArrayList<>();
        mBooleen = new ArrayList<>();
        num = new ArrayList<>();
        myClipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
        mAdapterDynamicList = new AdapterDynamicList(getActivity(), mLists);
        mAdapterDynamicList.setBooleen(mBooleen);
        //点动态整个布局跳转到动态详情
        mAdapterDynamicList.setOnListenItemClick(this);
        mAdapterDynamicList.setNum(num);
        //点击点赞，设置点赞状态：是红心还是灰心
        mAdapterDynamicList.setOnListenGoodClickLayout((goodCheckBox, textView, pos) -> {
            if (mBooleen != null && mBooleen.get(pos) != null && !mBooleen.get(pos)) {
                //点赞
                clickLike(true, mLists.get(pos).getPostId(), mLists.get(pos).getVisiable(), SPUtils.getString(Constant.MEMID), goodCheckBox, textView, pos);

            } else if (mBooleen != null && mBooleen.get(pos) != null && mBooleen.get(pos)) {
                //取消点赞
                clickLike(false, mLists.get(pos).getPostId(), mLists.get(pos).getVisiable(), SPUtils.getString(Constant.MEMID), goodCheckBox, textView, pos);
            }
        });
        //点击动态的评论图标，跳转到动态详情
        mAdapterDynamicList.setOnListenCommentClickLayout(new AdapterDynamicList.OnListenCommentClickLayout() {
            @Override
            public void onClickCommentListenDown(ImageButton commentImageButton) {
                commentImageButton.setBackgroundResource(R.mipmap.ic_dynamic_comment_dw);
            }

            @Override
            public void onClickCommentListenUp(ImageButton commentImageButton, int pos, String postid, String visiable, String authId, Integer likeNum, Integer commentNum) {
                commentImageButton.setBackgroundResource(R.mipmap.ic_dynamic_comment_df);
                Intent intent = new Intent(getActivity(), DynamicCommentActivity.class);
                intent.putExtra("postid", postid);
                intent.putExtra("visiable", visiable);
                intent.putExtra("postindex", pos);
                intent.putExtra("authid", authId);
                intent.putExtra("loveNum", likeNum);
                intent.putExtra("commentNum", commentNum);
                startActivity(intent);
            }
        });
        //点击分享图标，出现分享页面
        mAdapterDynamicList.setOnListenShareClickLayout((shareImageButton, pos) -> share());
        //点击三个点图标，出现popWindow
        mAdapterDynamicList.setOnListenMoreClickLayout((moreImageButton, pos, memid, postId) -> {
            mPopupWindow = creatPop(pos, memid, postId);
            mPopupWindow.showAtLocation(getActivity().findViewById(R.id.ly_root), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        });
        //点击动态图片，跳转到图片全屏展示页
        mAdapterDynamicList.setOnListenItemImageClick((view, imageView, pos, image) -> {
            Intent intent = new Intent(getActivity(), ImageCheckActivity.class);
            intent.putExtra("image", image);
            getContext().startActivity(intent);
        });
        //点击动态作者的头像调转到该作者的个人页
        mAdapterDynamicList.setOnListenHeadImage((pos, authId, authMemrole) -> {
            Intent intent = new Intent(getActivity(), PersionalActivity.class);
            intent.putExtra("tarid", authId);
            intent.putExtra("memrole", authMemrole);
            startActivity(intent);
        });

        mSwipeTarget.setAdapter(mAdapterDynamicList);
        //点击FAB跳转到发送动态页面
        mFabSendDiscovery.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                Intent intent = new Intent(getActivity(), SendDynamicActivity.class);
                startActivityForResult(intent, 101);
            }
        });
        //监听RecyclerView的滚动状态，隐藏或者显示FAB
        mSwipeTarget.setOnScrollListener(new RecyclerViewScrollDetector() {
            @Override
            public void onHide() {
                mFabSendDiscovery.hide(true);
            }

            @Override
            public void onShow() {
                mFabSendDiscovery.show(true);
            }

            @Override
            public void onScroll() {

            }
        });
    }

    private void share() {
        ShareBoardConfig config = new ShareBoardConfig();
        config.setIndicatorColor(0x00E9EFF2, 0xffE9EFF2);

        new ShareAction(getActivity())
                .setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE
                        , SHARE_MEDIA.SMS, SHARE_MEDIA.EMAIL)
                .addButton("umeng_sharebutton_custom", "umeng_sharebutton_custom", "info_icon_1", "info_icon_1")
                .setShareboardclickCallback(shareBoardlistener)
                .open(config);
    }

    private PopupWindow creatPop(int postion, String memid, String postId) {

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pop_layout, null);

        Button send = (Button) view.findViewById(R.id.popmenu_send);
        Button cancle = (Button) view.findViewById(R.id.popmenu_cancel);
        Button delete = (Button) view.findViewById(R.id.popmenu_delete);

        if (!memid.equals(SPUtils.getString(Constant.MEMID))) {
            delete.setVisibility(View.GONE);
        } else {
            delete.setVisibility(View.VISIBLE);
        }

        send.setOnClickListener(this);
        cancle.setOnClickListener(this);
        delete.setOnClickListener(view1 -> {
//            Toast.makeText(getActivity(), "postion:" + postion, Toast.LENGTH_SHORT).show();
            deleteDynamic(postion, postId, mLists.get(postion).getVisiable());
        });

        WindowManager windowManager = getActivity().getWindowManager();
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

    private void deleteDynamic(int postion, String postid, String visiable) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("post.postid", postid);
        requestParams.put("post.visiable", visiable);
        App.getRequestInstance().post(getActivity(), UrlPath.URL_DEL_POST, requestParams, new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                try {
                    android.util.Log.d("TrainerDynamicFragment", json);
                    JSONObject object = new JSONObject(json);
                    String msgFlag = object.getString("msgFlag");
                    if (msgFlag.equals("1")) {
                        BeanEventBas beanEventBas = new BeanEventBas();
                        beanEventBas.setCommentDelete(true);
                        beanEventBas.setCommentPostId(postid);
                        beanEventBas.setCommentFlag(true);
                        mEventBus.post(beanEventBas);
                        mLists.remove(postion);
                        num.remove(postion);
                        mBooleen.remove(postion);
                        mAdapterDynamicList.notifyItemChanged(postion);
                        mPopupWindow.dismiss();
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

    @Override
    public void initData() {
        RequestParams params = new RequestParams();
        params.put("memid", SPUtils.getString(Constant.MEMID));
        params.put("visiable", visiable);
        params.put("pageIndex", String.valueOf(currentPage));
        App.getRequestInstance().post(UrlPath.URL_DYNAMIC, getActivity(), params, new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                BeanDynamic beanDynamic = new Gson().fromJson(json, BeanDynamic.class);
                //刷新
                if (type == 0) {
                    mSwipeToLoadLayout.setRefreshing(false);
                    mBooleen.clear();
                    mLists.clear();
                    num.clear();
                    if (beanDynamic.getList() != null && beanDynamic.getList().size() != 0) {
                        for (int i = 0; i < beanDynamic.getList().size(); i++) {
                            if (beanDynamic.getList().get(i).getIslike() == 1) {
                                mBooleen.add(true);
                            } else {
                                mBooleen.add(false);
                            }
                            num.add(0);
                            BeanMineDynamic bean = new BeanMineDynamic();
                            bean.setImage(beanDynamic.getList().get(i).getAuthimgsfilename());
                            bean.setName(beanDynamic.getList().get(i).getAuthnickname());
                            bean.setContentText(beanDynamic.getList().get(i).getContent());
                            bean.setContentImage(beanDynamic.getList().get(i).getImgsfilename());
                            bean.setCreateTime(getFriendlyTimeSpanByNow(beanDynamic.getList().get(i).getCreatetime().getTime()));
                            bean.setBigImage(beanDynamic.getList().get(i).getImgpfilename());
                            bean.setLoveNum(String.valueOf(beanDynamic.getList().get(i).getLikecount()));
                            bean.setCommentNum(String.valueOf(beanDynamic.getList().get(i).getCommentcount()));
                            bean.setPostId(beanDynamic.getList().get(i).getPostid());
                            bean.setVisiable(beanDynamic.getList().get(i).getVisiable());
                            bean.setAuthId(beanDynamic.getList().get(i).getAuthid());
                            bean.setMemrole(beanDynamic.getList().get(i).getMemrole());
                            mLists.add(bean);
                        }
                    }
                    mAdapterDynamicList.notifyDataSetChanged();

                } else { //加载
                    mSwipeToLoadLayout.setLoadingMore(false);
                    if (beanDynamic.getList() != null && beanDynamic.getList().size() != 0) {
                        int pos = mAdapterDynamicList.getItemCount();
                        for (int i = 0; i < beanDynamic.getList().size(); i++) {
                            if (beanDynamic.getList().get(i).getIslike() == 1) {
                                mBooleen.add(true);
                            } else {
                                mBooleen.add(false);
                            }
                            num.add(0);
                            BeanMineDynamic bean = new BeanMineDynamic();
                            bean.setImage(beanDynamic.getList().get(i).getAuthimgsfilename());
                            bean.setName(beanDynamic.getList().get(i).getAuthnickname());
                            bean.setContentText(beanDynamic.getList().get(i).getContent());
                            bean.setContentImage(beanDynamic.getList().get(i).getImgsfilename());
                            bean.setCreateTime(getFriendlyTimeSpanByNow(beanDynamic.getList().get(i).getCreatetime().getTime()));
                            bean.setBigImage(beanDynamic.getList().get(i).getImgpfilename());
                            bean.setLoveNum(String.valueOf(beanDynamic.getList().get(i).getLikecount()));
                            bean.setCommentNum(String.valueOf(beanDynamic.getList().get(i).getCommentcount()));
                            bean.setPostId(beanDynamic.getList().get(i).getPostid());
                            bean.setVisiable(beanDynamic.getList().get(i).getVisiable());
                            bean.setAuthId(beanDynamic.getList().get(i).getAuthid());
                            mLists.add(bean);
                        }
                        mAdapterDynamicList.notifyItemRangeInserted(pos, beanDynamic.getList().size());
                    } else {
                        Toast.makeText(getContext(), "没有更多信息了", Toast.LENGTH_SHORT).show();
                        currentPage--;
                    }
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
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.popmenu_send:

                // TODO: 2016/12/21 举报代码
                Toast.makeText(getContext(), "举报他", Toast.LENGTH_SHORT).show();
                mPopupWindow.dismiss();
                break;

            case R.id.popmenu_cancel:
                mPopupWindow.dismiss();
                break;
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
                    Toast.makeText(getContext(), "复制成功", Toast.LENGTH_LONG).show();
                }

            } else {
                new ShareAction(getActivity()).setPlatform(share_media)
                        .withTitle("efitHeath分享")
                        //必须写，否者出不来
                        /**
                         * 打包成功的友盟分享的微信出不来：
                         *         1、必须写出withText()
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

            Toast.makeText(getContext(), "分享成功", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(getContext(), platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if (t != null) {
                Log.d("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(getContext(), "分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 101:
                //刷新广场动态
                BeanEventBas beanEventBas = new BeanEventBas();
                beanEventBas.setAddDynamicFalg(true);
                mEventBus.post(beanEventBas);
                //刷新该页动态
                onRefresh();
                break;
            default:
                UMShareAPI.get(getContext()).onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    /**
     * 点动态整个布局跳转到动态详情
     *
     * @param postid
     * @param visiable
     * @param i
     * @param authId
     * @param likeNum
     * @param commentNum
     */
    @Override
    public void onItemListen(String postid, String visiable, int i, String authId, Integer likeNum, Integer commentNum) {
        Intent intent = new Intent(getActivity(), DynamicCommentActivity.class);
        intent.putExtra("postid", postid);
        intent.putExtra("visiable", visiable);
        intent.putExtra("postindex", i);
        intent.putExtra("authid", authId);
        intent.putExtra("loveNum", likeNum);
        intent.putExtra("commentNum", commentNum);
        startActivity(intent);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * EvnetBus接收类，用来更新动态信息
     * 例如：从SendDynamicActivity页回来更新点赞数量和评论数量 以及 添加第一个动态
     *
     * @param beanEventBas
     */
    @Subscribe
    public void getValue(BeanEventBas beanEventBas) {
        //接收来自发现页面，是否看私密来刷新动态列表
        if (null != beanEventBas.getFriendRefresh()) {
            visiable = String.valueOf(beanEventBas.getFriendRefresh());
            currentPage = 1;
            initData();
        }
        //接收来自TrainerDynamicActivity、HotDynamicFragment、DynamicCommentActivity的动态点赞、评论刷新
        if (null != beanEventBas.getBoolean() && null != beanEventBas.getPos()) {
            try {
                for (int k = 0; k < mLists.size(); k++) {
                    if (mLists.get(k).getPostId().equals(beanEventBas.getPostId())) {
                        if (!mLists.get(k).getLoveNum().equals(beanEventBas.getNum()) || !mLists.get(k).getCommentNum().equals(beanEventBas.getCommentNum())) {
                            mBooleen.remove(k);
                            mBooleen.add(k, beanEventBas.getBoolean());
                            mLists.get(k).setLoveNum(beanEventBas.getNum());
                            if (beanEventBas.getCommentNum() != null) {
                                mLists.get(k).setCommentNum(beanEventBas.getCommentNum());
                            }
                            mAdapterDynamicList.notifyItemChanged(k);
                        }
                        break;
                    }
                }
            } catch (Exception e) {
                android.util.Log.d("DynamicFragment", e.toString());
            }
        }
        //接收来自TrainerDynamicActivity、HotDynamicFragment、DynamicCommentActivity的删除动态的通知
        if (beanEventBas.getCommentDelete() != null && beanEventBas.getCommentDelete() && beanEventBas.getCommentPostId() != null) {
            for (int i = 0; i < mLists.size(); i++) {
                if (beanEventBas.getCommentPostId().equals(mLists.get(i).getPostId())) {
                    mLists.remove(i);
                    mBooleen.remove(i);
                    num.remove(i);
                    mAdapterDynamicList.notifyDataSetChanged();
                }
            }
        }
        //接收来着TrainerDynamicActivity（从个人页动态）的发表动态刷新该页
        if (beanEventBas.getSquareDynamicFalg() != null) {
            mSwipeToLoadLayout.setRefreshing(true);
        }

        //刷新从广场来的点赞信息
        if (beanEventBas.getSquareRefresh() != null && beanEventBas.getSquare2friendFalg() != null) {
            for (int i = 0; i < mLists.size(); i++) {
                if (mLists.get(i).getPostId().equals(beanEventBas.getPostid())) {
                    mBooleen.remove(i);
                    mBooleen.add(i, beanEventBas.getSquareRefresh());
                    mLists.get(i).setLoveNum(String.valueOf(beanEventBas.getLoveNum()));
                    mAdapterDynamicList.notifyItemChanged(i);
                    break;
                }
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
    private void clickLike(boolean doLike, String postid, String visiable, String memid, CheckBox checkBox, TextView textView, int pos) {
        String url = doLike ? UrlPath.URL_SEND_LIKE : UrlPath.URL_UNLIKE;
        RequestParams params = new RequestParams();
        params.put("postid", postid);
        params.put("visiable", visiable);
        params.put("memid", memid);
        App.getRequestInstance().post(url, getActivity(), params, new RequestListener() {
            @Override
            public void requestSuccess(String json) {
//                    Toast.makeText(mActivity, json, Toast.LENGTH_LONG).show();
                try {
                    JSONObject object = new JSONObject(json);
                    String msgFlag = object.getString("msgFlag");
                    if (msgFlag.equals("1")) {
                        if (doLike) {
                            checkBox.setBackgroundResource(R.mipmap.ic_dynamic_good_dw);
                            int num = Integer.valueOf(textView.getText().toString()) + 1;
                            textView.setText(num + "");
                            mBooleen.remove(pos);
                            mBooleen.add(pos, true);
                        } else {
                            checkBox.setBackgroundResource(R.mipmap.ic_dynamic_good_df);
                            int num = Integer.valueOf(textView.getText().toString()) - 1;
                            textView.setText(num + "");
                            mBooleen.remove(pos);
                            mBooleen.add(pos, false);
                        }
                        BeanEventBas beanEventBas = new BeanEventBas();
                        beanEventBas.setSquareRefresh(mBooleen.get(pos));
                        beanEventBas.setLoveNum(Integer.valueOf(textView.getText().toString()));
                        beanEventBas.setPostid(postid);
                        beanEventBas.setFriend2squareFalg(true);
                        mEventBus.post(beanEventBas);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestError(VolleyError e, String error) {
                Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
            }

            @Override
            public void noInternet(VolleyError e, String error) {

            }
        });

    }

}
