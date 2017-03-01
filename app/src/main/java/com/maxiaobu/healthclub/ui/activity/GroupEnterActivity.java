package com.maxiaobu.healthclub.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.adapter.AdapterGroupEnter;
import com.maxiaobu.healthclub.chat.db.InviteMessgeDao;
import com.maxiaobu.healthclub.chat.domain.InviteMessage;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanEventBas;
import com.maxiaobu.healthclub.common.beangson.BeanGroupEnter;
import com.maxiaobu.healthclub.ui.weiget.dialog.CustomDialog;
import com.maxiaobu.healthclub.ui.weiget.refresh.LoadMoreFooterView;
import com.maxiaobu.healthclub.ui.weiget.refresh.RefreshHeaderView;
import com.maxiaobu.volleykit.RequestListener;
import com.maxiaobu.volleykit.RequestParams;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 马小布 on 2017/1/19.
 * email：maxiaobu1216@gmail.com
 * 功能：群申请列表
 * 伪码：
 * 待完成：
 */
public class GroupEnterActivity extends BaseAty implements OnRefreshListener,
        OnLoadMoreListener, AdapterGroupEnter.OnClickPassButton, AdapterGroupEnter.OnLongClickRemove {
    @Bind(R.id.tv_title_common)
    TextView mTvTitleCommon;
    @Bind(R.id.toolbar_common)
    Toolbar mToolbarCommon;
    @Bind(R.id.swipe_refresh_header)
    RefreshHeaderView mSwipeRefreshHeader;
    @Bind(R.id.swipe_target)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipe_load_more_footer)
    LoadMoreFooterView mSwipeLoadMoreFooter;
    @Bind(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;

    /**
     * 0刷新  1加载
     */
    private int dataType;
    /**
     * 当前页数
     */
    private int currentPage;

    private AdapterGroupEnter mAdapterGroupEnter;
    private List<BeanGroupEnter.MemberlistBean> mMemberlistBeen;

    private EventBus mEventBus;

    private CustomDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_enter);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    public void initView() {
        setCommonBackToolBar(mToolbarCommon, mTvTitleCommon, "入群申请");
        mEventBus = EventBus.getDefault();
        mSwipeToLoadLayout.setOnRefreshListener(this);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapterGroupEnter = new AdapterGroupEnter(this);
        currentPage = 1;
        dataType = 0;
        mMemberlistBeen = new ArrayList<>();
        mAdapterGroupEnter.setMemberlistBeen(mMemberlistBeen);
        mRecyclerView.setAdapter(mAdapterGroupEnter);

        mAdapterGroupEnter.setOnClickPassButton(this);
        mAdapterGroupEnter.setLongClickRemove(this);

        mSwipeToLoadLayout.setRefreshing(true);
    }

    @Override
    public void initData() {
        RequestParams params = new RequestParams();
        params.put("groupmember.groupid", getIntent().getStringExtra("groupIid"));
        params.put("pageIndex", String.valueOf(currentPage));
        App.getRequestInstance().post(UrlPath.URL_GROUP_APPLY, this, params, new RequestListener() {
            @Override
            public void requestSuccess(String s) {
                BeanGroupEnter beanGroupEnter = new Gson().fromJson(s, BeanGroupEnter.class);
                if (dataType == 0) {

                    mSwipeToLoadLayout.setRefreshing(false);
                    mMemberlistBeen.clear();
                    mMemberlistBeen.addAll(beanGroupEnter.getMemberlist());
                    mAdapterGroupEnter.notifyDataSetChanged();

                } else if (dataType == 1) {
                    mSwipeToLoadLayout.setLoadingMore(false);
                    if (beanGroupEnter.getMemberlist().size() != 0) {
                        int position = mAdapterGroupEnter.getItemCount();
                        mMemberlistBeen.addAll(beanGroupEnter.getMemberlist());
                        mAdapterGroupEnter.notifyItemRangeChanged(position, beanGroupEnter.getMemberlist().size());
                    } else {
                        Toast.makeText(GroupEnterActivity.this, "没有更多的数据了", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onRefresh() {
        currentPage = 1;
        dataType = 0;
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
        dataType = 1;
        mSwipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                initData();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    /**
     * 进群处理
     *
     * @param v
     */
    @Override
    public void onPass(View v, String memid) {

        boolean f = ApplyIntoGroup(memid);
        if (f) {
            aa(memid, "1");
        }

    }


    public void aa(String memid, String type) {
        RequestParams params = new RequestParams();
        params.put("groupmember.groupid", getIntent().getStringExtra("groupIid"));
        params.put("groupmember.memid", memid);
        params.put("groupmember.checkstatus", type);
        App.getRequestInstance().post(UrlPath.URL_GROUP_INTO, this, params, new RequestListener() {
            @Override
            public void requestSuccess(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    String msgContent = object.getString("msgContent");
                    String msgFlag = object.getString("msgFlag");
                    Toast.makeText(GroupEnterActivity.this, msgContent, Toast.LENGTH_SHORT).show();

                    if (msgFlag.equals("1")) {

                        //所有的刷新处理
                        BeanEventBas beanEventBas = new BeanEventBas();
                        beanEventBas.setRefresh(true);
                        mEventBus.post(beanEventBas);
                        mSwipeToLoadLayout.setRefreshing(true);
//                        onRefresh();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
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

    public boolean ApplyIntoGroup(String memid) {

        final Boolean[] flag = {true};
        InviteMessgeDao dao = new InviteMessgeDao(this);
        new Thread(() -> {
            try {

                EMClient.getInstance().groupManager().acceptApplication(memid.toLowerCase(), getIntent().getStringExtra("id"));
                flag[0] = true;

            } catch (HyphenateException e) {
                flag[0] = false;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(GroupEnterActivity.this, "进群同意操作失败，请重新操作", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
        dao.saveUnreadMessageCount(0);
        return flag[0];
    }


    /**
     * 拒绝入群
     *
     * @param v
     */
    @Override
    public void onRemove(View v, String memid) {

        mDialog = new CustomDialog(this, new CustomDialog.SetOnClick() {
            @Override
            public void onPosition(View view, AlertDialog dialog) {
                aa(memid, "2");
            }

            @Override
            public void onCancle(View view, AlertDialog dialog) {

            }
        });
        mDialog.setTitle("拒绝入群");
        mDialog.setContent("您确定要拒绝该用户进入吗？");
        mDialog.setTitleColor(Color.RED);
        mDialog.create();
        mDialog.show();

    }


}
