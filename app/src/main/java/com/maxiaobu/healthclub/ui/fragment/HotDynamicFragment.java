package com.maxiaobu.healthclub.ui.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseFrg;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.adapter.AdapterHotDynamicFrg;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanEventBas;
import com.maxiaobu.healthclub.common.beangson.BeanHotDynamic;
import com.maxiaobu.healthclub.ui.activity.ActivityTip;
import com.maxiaobu.healthclub.ui.activity.DynamicCommentActivity;
import com.maxiaobu.healthclub.ui.activity.ImageCheckActivity;
import com.maxiaobu.healthclub.ui.activity.PersionalActivity;
import com.maxiaobu.healthclub.ui.weiget.refresh.LoadMoreFooterView;
import com.maxiaobu.healthclub.ui.weiget.refresh.RefreshHeaderView;
import com.maxiaobu.healthclub.utils.storage.SPUtils;
import com.maxiaobu.volleykit.RequestJsonListener;
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
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.content.Context.CLIPBOARD_SERVICE;
/**
 * Created by 马小布 on 2017/2/20.
 * email：maxiaobu1216@gmail.com
 * 功能：广场
 * 伪码：
 * 待完成：
 */
public class HotDynamicFragment extends BaseFrg implements OnLoadMoreListener, OnRefreshListener,
        AdapterHotDynamicFrg.OnListenItemClick, View.OnClickListener {
    @Bind(R.id.swipe_refresh_header)
    RefreshHeaderView mSwipeRefreshHeader;
    @Bind(R.id.swipe_target)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipe_load_more_footer)
    LoadMoreFooterView mSwipeLoadMoreFooter;
    @Bind(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;

    private AdapterHotDynamicFrg mAdapter;
    private List<BeanHotDynamic.ListBean> mData;

    private int currentPage;

    private int type;

    private ClipboardManager myClipboard;

    private ClipData myClip;

    private PopupWindow mPopupWindow;

    private EventBus mEventBus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discovery_hot_dynamic, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        initView();
        initData();
        return view;
    }

    @Override
    public void initView() {
        type = 0;
        currentPage = 1;
        mEventBus = EventBus.getDefault();
        mData = new ArrayList<>();
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
        mSwipeToLoadLayout.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new AdapterHotDynamicFrg(getActivity(), mData);
        myClipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
        mAdapter.setWebLinstener(() -> {
            Intent intent = new Intent(getActivity(), ActivityTip.class);
            startActivity(intent);
        });
        mAdapter.setOnListenItemClick(this);
        mAdapter.setOnListenGoodClickLayout((goodCheckBox, textView, pos) -> {
            if (mData.get(pos).getIslike() == 0) {
                //点赞
                clickLike(true, mData.get(pos).getPostid(), mData.get(pos).getVisiable(), SPUtils.getString(Constant.MEMID), goodCheckBox, textView, pos);

            } else if (mData.get(pos).getIslike() == 1) {
                //取消点赞
                clickLike(false, mData.get(pos).getPostid(), mData.get(pos).getVisiable(), SPUtils.getString(Constant.MEMID), goodCheckBox, textView, pos);
            }
        });
        mAdapter.setOnListenCommentClickLayout(new AdapterHotDynamicFrg.OnListenCommentClickLayout() {
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
        mAdapter.setOnListenShareClickLayout((shareImageButton, pos) -> share());
        //点击三个点图标，出现popWindow
        mAdapter.setOnListenMoreClickLayout((moreImageButton, pos, memid, postId) -> {
            mPopupWindow = creatPop(pos, memid, postId);
            mPopupWindow.showAtLocation(getActivity().findViewById(R.id.ly_root), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        });
        //点击动态图片，跳转到图片全屏展示页
        mAdapter.setOnListenItemImageClick((view, imageView, pos, image) -> {
            Intent intent = new Intent(getActivity(), ImageCheckActivity.class);
            intent.putExtra("image", image);
            getContext().startActivity(intent);
        });
        //点击动态作者的头像调转到该作者的个人页
        mAdapter.setOnListenHeadImage((pos, authId, authMemrole) -> {
            Intent intent = new Intent(getActivity(), PersionalActivity.class);
            intent.putExtra("tarid", authId);
            intent.putExtra("memrole", authMemrole);
            startActivity(intent);
        });

        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initData() {
        RequestParams params = new RequestParams();
        params.put("memid", SPUtils.getString(Constant.MEMID));
        params.put("pageIndex", String.valueOf(currentPage));
        App.getRequestInstance().post(UrlPath.URL_PUBLIC_DYNAMIC, getActivity(), BeanHotDynamic.class, params, new RequestJsonListener<BeanHotDynamic>() {
            @Override
            public void requestSuccess(BeanHotDynamic beanHotDynamic) {
                //刷新
                if (type == 0) {
                    if (mSwipeToLoadLayout != null)
                        mSwipeToLoadLayout.setRefreshing(false);
                    mData.clear();
                    mData.addAll(beanHotDynamic.getList());
                    mAdapter.notifyDataSetChanged();
                    //加载
                } else if (type == 1) {
                    if (mSwipeToLoadLayout != null)
                        mSwipeToLoadLayout.setLoadingMore(false);
                    if (beanHotDynamic.getList() != null && beanHotDynamic.getList().size() != 0) {
                        int pos = mAdapter.getItemCount();
                        mData.addAll(beanHotDynamic.getList());
                        mAdapter.notifyItemRangeInserted(pos, beanHotDynamic.getList().size());
                    } else {
                        Toast.makeText(getActivity(), "没有更多数据了", Toast.LENGTH_SHORT).show();
                        currentPage--;
                    }
                }
            }

            @Override
            public void requestError(VolleyError volleyError, String s) {
                Log.d("ClubListActivity", s);
                Log.d("ClubListActivity", "volleyError:" + volleyError);
            }

            @Override
            public void noInternet(VolleyError volleyError, String s) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
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
                            mData.get(pos).setIslike(1);
                            mAdapter.notifyItemChanged(pos);
                        } else {
                            checkBox.setBackgroundResource(R.mipmap.ic_dynamic_good_df);
                            int num = Integer.valueOf(textView.getText().toString()) - 1;
                            textView.setText(num + "");
                            mData.get(pos).setIslike(0);
                            mAdapter.notifyItemChanged(pos);
                        }
                        BeanEventBas beanEventBas = new BeanEventBas();
                        if(mData.get(pos).getIslike() == 1){
                            beanEventBas.setSquareRefresh(true);
                        }else {
                            beanEventBas.setSquareRefresh(false);
                        }
                        beanEventBas.setLoveNum(Integer.valueOf(textView.getText().toString()));


                        beanEventBas.setPostid(postid);
                        beanEventBas.setSquare2friendFalg(true);
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
//            Log.d("HotDynamicFragment", "mData.size():" + mData.size());
//            Log.d("HotDynamicFragment", "postion:" + postion);
            deleteDynamic(postion,postId,mData.get(postion).getVisiable());
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
    private void deleteDynamic(int postion,String postid,String visiable){
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
                        beanEventBas.setCommentFlag(true);
                        mEventBus.post(beanEventBas);
                        mData.remove(postion);
                        mAdapter.notifyDataSetChanged();
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
                com.umeng.socialize.utils.Log.d("throw", "throw:" + t.getMessage());
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

    @Subscribe
    public void getValue(BeanEventBas beanEventBas) {
        //从trainerDyanmicFragment与DynamicCommentActivity带来的更新评论数和点赞数
        if(null != beanEventBas.getBoolean() && null != beanEventBas.getPos()){
            try{
                int isLike;
                if(beanEventBas.getBoolean()){
                    isLike = 1;
                }else {
                    isLike = 0;
                }
                for (int k = 0; k < mData.size(); k++) {
                    if (mData.get(k).getPostid().equals(beanEventBas.getPostId())) {
                        if(!String.valueOf(mData.get(k).getLikecount()).equals(beanEventBas.getNum())|| !String.valueOf(mData.get(k).getCommentcount()).equals(beanEventBas.getCommentNum())){
                            mData.get(k).setIslike(isLike);
                            mData.get(k).setLikecount(Integer.valueOf(beanEventBas.getNum()));
                            if(beanEventBas.getCommentNum() != null){
                                mData.get(k).setCommentcount(Integer.valueOf(beanEventBas.getCommentNum()));
                            }
                            mAdapter.notifyItemChanged(k+1);
                        }
                        break;
                    }
                }
            }catch (Exception e){
                android.util.Log.d("DynamicFragment", e.toString());
            }
        }

        //接收来自TrainerDynamicActivity、DynamicFragment、DynamicCommentActivity的删除动态的通知
        if(beanEventBas.getCommentDelete() != null && beanEventBas.getCommentDelete() && beanEventBas.getCommentPostId() != null){
            for (int i = 0; i < mData.size(); i++) {
                if(beanEventBas.getCommentPostId().equals(mData.get(i).getPostid())){
                    mData.remove(i);
                    mAdapter.notifyDataSetChanged();
                    break;
                }
            }
        }

        //从个人页发表动态刷新该页
        if(beanEventBas.getSquareDynamicFalg() != null){
            mSwipeToLoadLayout.setRefreshing(true);
        }

        //刷新从好友动态来的点赞信息
        if(beanEventBas.getSquareRefresh() != null && beanEventBas.getFriend2squareFalg() != null){
            for (int i = 0; i < mData.size(); i++) {
                if(mData.get(i).getPostid().equals(beanEventBas.getPostid())){
                    if (beanEventBas.getSquareRefresh()) {
                        mData.get(i).setIslike(1);
                    }else {
                        mData.get(i).setIslike(0);
                    }
                    mData.get(i).setLikecount(beanEventBas.getLoveNum());
                    mAdapter.notifyItemChanged(i + 1);
                    break;
                }
            }
        }

        //好友动态发表动态刷新该页
        if(beanEventBas.getAddDynamicFalg() != null){
            mSwipeToLoadLayout.setRefreshing(true);
        }
    }
}
