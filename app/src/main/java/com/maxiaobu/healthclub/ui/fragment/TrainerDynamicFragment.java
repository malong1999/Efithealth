package com.maxiaobu.healthclub.ui.fragment;


import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
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
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseFrg;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.adapter.TextAdapter;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanEventBas;
import com.maxiaobu.healthclub.common.beangson.BeanMeDynamic;
import com.maxiaobu.healthclub.common.beangson.BeanMineDynamic;
import com.maxiaobu.healthclub.ui.activity.DynamicCommentActivity;
import com.maxiaobu.healthclub.ui.activity.ImageCheckActivity;
import com.maxiaobu.healthclub.ui.activity.PersionalActivity;
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
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import maxiaobu.mxbutilscodelibrary.ConstUtils;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * 个人页 --- 动态页面
 */
public class TrainerDynamicFragment extends BaseFrg implements View.OnClickListener, TextAdapter.OnListenItemClick {
    @Bind(R.id.swipe_target)
    RecyclerView mSwipeTarget;
    @Bind(R.id.nest_scroll_view)
    NestedScrollView mNestScrollView;
    /**
     * 页数 默认为1
     */
    private int currentPage;
    /**
     * 0： 刷新列表
     * 1： 加载更多
     * 2： 添加刚发出的第一条动态
     */
    private int type;
    /**
     * 动态列表适配器
     */
    private TextAdapter mAdapterDynamicList;
    /***
     * 动态列表的数据List集合
     */
    private List<BeanMineDynamic> mList;
    /**
     * 每个item的点赞标志
     */
    private List<Boolean> mGoodFalgList;
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
    /**
     * 滑动的最大距离，用来隐藏和显示FAB按钮
     */
    private static final int HIDE_THRESHOLD = 20;
    /**
     * 记录滑动的距离，用来隐藏和显示FAB按钮
     */
    private int scrolledDistance = 0;
    /**
     * 标志是否显示FAB，用来隐藏和显示FAB按钮
     */
    private boolean controlsVisible = true;

    /**
     * 滑动到底部以后，加载动态，页码+1
     * 每次滑动到底部要间隔2秒才会刷新
     */
    public static final int MIN_CLICK_DELAY_TIME = 2000;
    /**
     * 最近一次加载动态的时间，默认为0
     */
    private long lastClickTime = 0;
    /**
     * 每个item的点赞数量标志（没有使用）
     */
    private List<Integer> mGoodNumFalgList;
    /**
     * 用来刷新DynamicFragment与HotDynamicFragment数据的
     * 例如：添加动态，删除动态，刷新点赞和评论数等
     */
    private EventBus mEventBus;

    public static Fragment getInstance(String memid, String name, String image) {
        TrainerDynamicFragment instance = new TrainerDynamicFragment();
        Bundle bundle = new Bundle();
        bundle.putString("memid", memid);
        bundle.putString("name", name);
        bundle.putString("image", image);
        instance.setArguments(bundle);
        return instance;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trainer_dynamic, container, false);
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
        mEventBus = EventBus.getDefault();
        mList = new ArrayList<>();
        mGoodFalgList = new ArrayList<>();
        mGoodNumFalgList = new ArrayList<>();
        myClipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
        mSwipeTarget.setHasFixedSize(true);
        mSwipeTarget.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManagerPersonal = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        layoutManagerPersonal.setSmoothScrollbarEnabled(true);
        layoutManagerPersonal.setAutoMeasureEnabled(true);
        mSwipeTarget.setLayoutManager(layoutManagerPersonal);
        mAdapterDynamicList = new TextAdapter(getActivity(), mList);
        mAdapterDynamicList.setBooleen(mGoodFalgList);
        mAdapterDynamicList.setNum(mGoodNumFalgList);
        //点动态整个布局跳转到动态详情
        mAdapterDynamicList.setOnListenItemClick(this);
        //点击点赞，设置点赞状态：是红心还是灰心
        mAdapterDynamicList.setOnListenGoodClickLayout((goodCheckBox, textView, pos) -> {
            if (mGoodFalgList != null && mGoodFalgList.get(pos) != null && !mGoodFalgList.get(pos)) {
                clickLike(true, mList.get(pos).getPostId(), mList.get(pos).getVisiable(), SPUtils.getString(Constant.MEMID), goodCheckBox, textView, pos);
            } else if (mGoodFalgList != null && mGoodFalgList.get(pos) != null && mGoodFalgList.get(pos)) {
                clickLike(false, mList.get(pos).getPostId(), mList.get(pos).getVisiable(), SPUtils.getString(Constant.MEMID), goodCheckBox, textView, pos);
            }
        });
        //点击动态的评论图标，跳转到动态详情
        mAdapterDynamicList.setOnListenCommentClickLayout(new TextAdapter.OnListenCommentClickLayout() {
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
        mAdapterDynamicList.setOnListenMoreClickLayout((moreImageButton, pos, memid, postid) -> {
            mPopupWindow = creatPop(pos, memid, postid);
            mPopupWindow.showAtLocation(getActivity().findViewById(R.id.container), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        });
        //点击动态图片，跳转到图片全屏展示页
        mAdapterDynamicList.setOnListenItemImageClick((view, imageView, pos, image) -> {
            Intent intent = new Intent(getActivity(), ImageCheckActivity.class);
            intent.putExtra("image", image);
            getContext().startActivity(intent);
        });

        mSwipeTarget.setAdapter(mAdapterDynamicList);
        //设置Fab的显示和隐藏状态
        mNestScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int dy = scrollY - oldScrollY;
                if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
                    if (null != ((PersionalActivity) getActivity()).getFabMenu() && ((PersionalActivity) getActivity()).getFabMenu().getVisibility() == View.VISIBLE)
                        ((PersionalActivity) getActivity()).getFabMenu().hideMenuButton(true);
                    if (null != ((PersionalActivity) getActivity()).getFabRelease() && ((PersionalActivity) getActivity()).getFabRelease().getVisibility() != View.GONE)
                        ((PersionalActivity) getActivity()).getFabRelease().hide(true);
                    controlsVisible = false;
                    scrolledDistance = 0;
                } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
                    if (null != ((PersionalActivity) getActivity()).getFabMenu() && ((PersionalActivity) getActivity()).getFabMenu().getVisibility() == View.VISIBLE)
                        ((PersionalActivity) getActivity()).getFabMenu().showMenuButton(true);
                    if (null != ((PersionalActivity) getActivity()).getFabRelease() && ((PersionalActivity) getActivity()).getFabRelease().getVisibility() != View.GONE)
                        ((PersionalActivity) getActivity()).getFabRelease().show(true);
                    controlsVisible = true;
                    scrolledDistance = 0;
                }
                if ((controlsVisible && dy > 0) || (!controlsVisible && dy < 0)) {
                    scrolledDistance += dy;
                }

            }

        });

        //监听NestScrollView是否滑到底部，再向上滑动则加载动态
        mNestScrollView.setOnTouchListener(new TouchListenerImpl());
    }


    private class TouchListenerImpl implements View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_UP:
                    int scrollY = view.getScrollY();
                    int height = view.getHeight();
                    int scrollViewMeasuredHeight = mNestScrollView.getChildAt(0).getMeasuredHeight();
                    if ((scrollY + height) == scrollViewMeasuredHeight) {
                        long currentTime = Calendar.getInstance().getTimeInMillis();
                        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                            lastClickTime = currentTime;
                            type = 1;
                            currentPage++;
                            initData();
                        }
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
//                    int scrollY = view.getScrollY();
//                    int height = view.getHeight();
//                    int scrollViewMeasuredHeight = mNestScrollView.getChildAt(0).getMeasuredHeight();
//                    if ((scrollY + height) == scrollViewMeasuredHeight) {
//                        long currentTime = Calendar.getInstance().getTimeInMillis();
//                        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
//                            lastClickTime = currentTime;
//                            type = 1;
//                            currentPage++;
//                            initData();
//                        }
//                    }
                    break;

                default:
                    break;
            }
            return false;
        }

    }

    @Override
    public void initData() {
        RequestParams params = new RequestParams();
//        android.util.Log.d("wjnn",UrlPath.URL_ME_DYNAMIC+"?tarmemid="+ getArguments().getString("memid") + "&memid="+SPUtils.getString(Constant.MEMID)+"&pageIndex="+ String.valueOf(currentPage));
        params.put("tarmemid", getArguments().getString("memid"));
        params.put("memid", SPUtils.getString(Constant.MEMID));
        params.put("pageIndex", String.valueOf(currentPage));
        App.getRequestInstance().post(UrlPath.URL_ME_DYNAMIC, getActivity(), params, new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                try {

                    BeanMeDynamic beanMeDynamic = new Gson().fromJson(json, BeanMeDynamic.class);
                    //刷新
                    if (type == 0) {
                        mGoodFalgList.clear();
                        mList.clear();
                        mGoodNumFalgList.clear();
                        for (int i = 0; i < beanMeDynamic.getList().size(); i++) {
                            if (beanMeDynamic.getList().get(i).getIslike() == 1) {
                                mGoodFalgList.add(true);
                            } else {
                                mGoodFalgList.add(false);
                            }
                            mGoodNumFalgList.add(0);
                            BeanMineDynamic bean = new BeanMineDynamic();
                            bean.setName(beanMeDynamic.getList().get(i).getAuthnickname());
                            bean.setImage(beanMeDynamic.getList().get(i).getAuthimgsfilename());
                            bean.setContentText(beanMeDynamic.getList().get(i).getContent());
                            bean.setContentImage(beanMeDynamic.getList().get(i).getImgsfilename());
                            bean.setLoveNum(String.valueOf(beanMeDynamic.getList().get(i).getLikecount()));
                            bean.setCommentNum(String.valueOf(beanMeDynamic.getList().get(i).getCommentcount()));
                            bean.setVisiable(beanMeDynamic.getList().get(i).getVisiable());
                            bean.setCreateTime(getFriendlyTimeSpanByNow(beanMeDynamic.getList().get(i).getCreatetime().getTime()));
                            bean.setBigImage(beanMeDynamic.getList().get(i).getImgpfilename());
//                                android.util.Log.d("mytt-->", beanMeDynamic.getList().get(i).getPostid());
                            bean.setPostId(beanMeDynamic.getList().get(i).getPostid());
                            bean.setAuthId(beanMeDynamic.getList().get(i).getAuthid());
                            mList.add(bean);
                        }
                        mAdapterDynamicList.notifyDataSetChanged();
                        //加载更多
                    } else if (type == 1) {
//                        mSwipeToLoadLayout.setLoadingMore(false);
//                        mSwipeTarget.completeLoad();
                        if (beanMeDynamic.getList().size() != 0) {
                            for (int i = 0; i < beanMeDynamic.getList().size(); i++) {
                                if (beanMeDynamic.getList().get(i).getIslike() == 1) {
                                    mGoodFalgList.add(true);
                                } else {
                                    mGoodFalgList.add(false);
                                }
                                mGoodNumFalgList.add(0);
                                BeanMineDynamic bean = new BeanMineDynamic();
                                bean.setPostId(beanMeDynamic.getList().get(i).getPostid());
                                bean.setName(beanMeDynamic.getList().get(i).getAuthnickname());
                                bean.setImage(beanMeDynamic.getList().get(i).getAuthimgsfilename());
                                bean.setContentText(beanMeDynamic.getList().get(i).getContent());
                                bean.setContentImage(beanMeDynamic.getList().get(i).getImgsfilename());
                                bean.setLoveNum(String.valueOf(beanMeDynamic.getList().get(i).getLikecount()));
                                bean.setCommentNum(String.valueOf(beanMeDynamic.getList().get(i).getCommentcount()));
                                bean.setVisiable(beanMeDynamic.getList().get(i).getVisiable());
                                bean.setCreateTime(getFriendlyTimeSpanByNow(beanMeDynamic.getList().get(i).getCreatetime().getTime()));
                                bean.setBigImage(beanMeDynamic.getList().get(i).getImgpfilename());
                                bean.setAuthId(beanMeDynamic.getList().get(i).getAuthid());
                                mList.add(bean);
                            }
                            mAdapterDynamicList.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getActivity(), "没有更多数据了", Toast.LENGTH_SHORT).show();
                            currentPage--;
                        }
                        //添加一条动态，显示一下
                    } else if (type == 2) {
                        mGoodFalgList.add(0, false);
                        mGoodNumFalgList.add(0, 0);
                        BeanMineDynamic bean = new BeanMineDynamic();
                        bean.setName(beanMeDynamic.getList().get(0).getAuthnickname());
                        bean.setImage(beanMeDynamic.getList().get(0).getAuthimgsfilename());
                        bean.setPostId(beanMeDynamic.getList().get(0).getPostid());
                        bean.setContentText(beanMeDynamic.getList().get(0).getContent());
                        bean.setContentImage(beanMeDynamic.getList().get(0).getImgsfilename());
                        bean.setLoveNum(String.valueOf(beanMeDynamic.getList().get(0).getLikecount()));
                        bean.setCommentNum(String.valueOf(beanMeDynamic.getList().get(0).getCommentcount()));
                        bean.setVisiable(beanMeDynamic.getList().get(0).getVisiable());
                        bean.setBigImage(beanMeDynamic.getList().get(0).getImgpfilename());
                        bean.setAuthId(beanMeDynamic.getList().get(0).getAuthid());
                        bean.setCreateTime(getFriendlyTimeSpanByNow(beanMeDynamic.getList().get(0).getCreatetime().getTime()));
                        mList.add(0, bean);
                        mAdapterDynamicList.notifyDataSetChanged();

                    }
                } catch (Exception e) {
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

    private PopupWindow creatPop(int position, String memid, String postId) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pop_layout, null);

        Button send = (Button) view.findViewById(R.id.popmenu_send);
        Button cancle = (Button) view.findViewById(R.id.popmenu_cancel);
        Button delete = (Button) view.findViewById(R.id.popmenu_delete);

        if (memid.equals(SPUtils.getString(Constant.MEMID))) {
            delete.setVisibility(View.VISIBLE);
        } else {
            delete.setVisibility(View.GONE);
        }

        send.setOnClickListener(this);
        cancle.setOnClickListener(this);
        delete.setOnClickListener(view1 -> {
//            Toast.makeText(getActivity(), "postion:" + position, Toast.LENGTH_SHORT).show();
//            android.util.Log.d("TrainerDynamicFragment", "position:" + position);
//            android.util.Log.d("TrainerDynamicFragment", mList.get(position).getVisiable());
            deleteDynamic(position, postId,mList.get(position).getVisiable());

        });

        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        PopupWindow popupWindow = new PopupWindow(view, display.getWidth(), display.getHeight(), true);

        popupWindow.setFocusable(true);
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        Drawable drawable = new ColorDrawable(0x99000000);
        popupWindow.setBackgroundDrawable(drawable);

        view.setOnTouchListener((v, event) -> {

            popupWindow.dismiss();
            return true;
        });

        return popupWindow;
    }

    private void deleteDynamic(int position, String postid,String visiable){
        RequestParams requestParams = new RequestParams();
        requestParams.put("post.postid",postid);
        requestParams.put("post.visiable",visiable);
        App.getRequestInstance().post(getActivity(), UrlPath.URL_DEL_POST, requestParams, new RequestListener() {
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
                        mEventBus.post(beanEventBas);
                        mList.remove(position);
                        mGoodFalgList.remove(position);
                        mGoodNumFalgList.remove(position);
                        mAdapterDynamicList.notifyDataSetChanged();
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
        UMShareAPI.get(getContext()).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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

    /**点动态整个布局跳转到动态详情
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

    /**
     * EvnetBus接收类，用来更新动态信息
     * 例如：从SendDynamicActivity页回来更新点赞数量和评论数量 以及 添加第一个动态
     * @param beanEventBas
     */
    @Subscribe
    public void getValue(BeanEventBas beanEventBas) {
        //接收来自PersionalActivity的刷新通知。发表动态以后加载第一条数据
        if (beanEventBas.getRefresh() != null && beanEventBas.getRefresh()) {
            type = 0;
            currentPage = 1;
            initData();
        }
        //接收来自SendDynamicActivity的通知，更新点赞信息和评论信息
        if (null != beanEventBas.getBoolean() && null != beanEventBas.getPos() && beanEventBas.getF() != null) {

            try {
                //先判断是否包含这个动态信息
                for (int k = 0; k < mList.size(); k++) {
                    if (mList.get(k).getPostId().equals(beanEventBas.getPostId())) {
                        //判断值是否一致，一致则不刷新，不一致则刷新
                        if (!mList.get(k).getLoveNum().equals(beanEventBas.getNum()) || !mList.get(k).getCommentNum().equals(beanEventBas.getCommentNum())) {

                            //删除旧值
                            mGoodFalgList.remove(k);
                            //添加新值
                            mGoodFalgList.add(k, beanEventBas.getBoolean());
                            mList.get(k).setLoveNum(beanEventBas.getNum());
                            mList.get(k).setCommentNum(beanEventBas.getCommentNum());
//                            android.util.Log.d("wjnn====", mList.get(k).getLoveNum());
                            mAdapterDynamicList.notifyDataSetChanged();
                        }
                        break;
                    }
                }

            } catch (Exception e) {
                android.util.Log.d("DynamicFragment", e.toString());
            }
        }
        //接收来自DynamicCommentActivity（动态详情）的通知，删除动态更新
        if (beanEventBas.getCommentDelete() != null && beanEventBas.getCommentDelete() && beanEventBas.getCommentPostId() != null && beanEventBas.getCommentFlag() != null) {

            for (int i = 0; i < mList.size(); i++) {
                if (beanEventBas.getCommentPostId().equals(mList.get(i).getPostId())) {
                    mList.remove(i);
                    mGoodFalgList.remove(i);
                    mGoodNumFalgList.remove(i);
                    mAdapterDynamicList.notifyDataSetChanged();
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
                            mGoodFalgList.remove(pos);
                            mGoodFalgList.add(pos, true);
                            mList.get(pos).setLoveNum(String.valueOf(Integer.valueOf(mList.get(pos).getLoveNum()) + 1));
                            mAdapterDynamicList.notifyDataSetChanged();
                        } else {
                            checkBox.setBackgroundResource(R.mipmap.ic_dynamic_good_df);
                            mGoodFalgList.remove(pos);
                            mGoodFalgList.add(pos, false);
                            mList.get(pos).setLoveNum(String.valueOf(Integer.valueOf(mList.get(pos).getLoveNum()) - 1));
                            mAdapterDynamicList.notifyDataSetChanged();
                        }
                        //发一个EVentBus，让DynamicFragment 与 HotDynamicFragment点赞数更新
                        BeanEventBas beanEventBas = new BeanEventBas();
                        beanEventBas.setPos(pos);
                        beanEventBas.setPostId(postid);
                        beanEventBas.setBoolean(mGoodFalgList.get(pos));
                        beanEventBas.setNum(mList.get(pos).getLoveNum());
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
