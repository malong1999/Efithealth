package com.maxiaobu.healthclub.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.google.gson.Gson;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.adapter.AdapterInviteGroup;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanInviteGroup;
import com.maxiaobu.healthclub.ui.weiget.refresh.LoadMoreFooterView;
import com.maxiaobu.healthclub.ui.weiget.refresh.RefreshHeaderView;
import com.maxiaobu.healthclub.utils.storage.SPUtils;
import com.maxiaobu.volleykit.RequestListener;
import com.maxiaobu.volleykit.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 莫小婷 on 2017/1/19.
 * introduction：我长得真他娘的磕碜，单身未娶，求包养
 * email：maxiaobu1216@gmail.com
 * 功能：加入群
 * 伪码：
 * 待完成：
 */
public class InviteGroupActivity extends BaseAty implements OnLoadMoreListener, OnRefreshListener {

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

    private AdapterInviteGroup mAdapterInviteGroup;
    private List<BeanInviteGroup.GroupListBean> mGroupListBeen;

    /**
     * 刷新是0 ，加载是1
     */
    private int type = 0;
    private int pageIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_group);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    public void initView() {

        setCommonBackToolBar(mToolbarCommon,mTvTitleCommon,"加入群");

        mSwipeToLoadLayout.setOnRefreshListener(this);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
        mSwipeTarget.setLayoutManager(new LinearLayoutManager(this));

        mGroupListBeen = new ArrayList<>();
        mAdapterInviteGroup = new AdapterInviteGroup(mGroupListBeen,InviteGroupActivity.this);
        mSwipeTarget.setAdapter(mAdapterInviteGroup);

        //为什么接口没有作用????
        mAdapterInviteGroup.setEnterListener(new AdapterInviteGroup.IntoListener() {
            @Override
            public void into(int position, String groupIid, String Imid) {
                Intent intent = new Intent(InviteGroupActivity.this,GroupDetailsActivity.class);
                intent.putExtra("groupIid",groupIid);
                intent.putExtra("groupId",Imid);
                startActivity(intent);
            }
        });

        mSwipeToLoadLayout.setRefreshing(true);
    }

    @Override
    public void initData() {
        RequestParams params = new RequestParams();
        params.put("memid",SPUtils.getString(Constant.MEMID));
        params.put("longitude",SPUtils.getString(Constant.LONGITUDE));
        params.put("latitude",SPUtils.getString(Constant.LATITUDE));
        params.put("pageIndex",String.valueOf(pageIndex));
        Log.d("++", UrlPath.URL_GROUP_DISCOVERY + "?memid="+ SPUtils.getString(Constant.MEMID) + "&longitude=" + SPUtils.getString(com.maxiaobu.healthclub.common.Constant.LONGITUDE) + "&latitude=" + SPUtils.getString(com.maxiaobu.healthclub.common.Constant.LATITUDE));
        App.getRequestInstance().post(UrlPath.URL_GROUP_DISCOVERY, this, params, new RequestListener() {
            @Override
            public void requestSuccess(String s) {

                BeanInviteGroup beanInviteGroup = new Gson().fromJson(s,BeanInviteGroup.class);
                if(type == 0){
                    mSwipeToLoadLayout.setRefreshing(false);
                    mGroupListBeen.clear();
                    mGroupListBeen.addAll(beanInviteGroup.getGroupList());
                    mAdapterInviteGroup.notifyDataSetChanged();
                }else {
                    mSwipeToLoadLayout.setLoadingMore(false);
                    if (beanInviteGroup.getGroupList().size() != 0) {
                        int position = mAdapterInviteGroup.getItemCount();
                        mGroupListBeen.addAll(beanInviteGroup.getGroupList());
                        mAdapterInviteGroup.notifyItemRangeInserted(position, beanInviteGroup.getGroupList().size());
                    } else {
                        Toast.makeText(mActivity, "没有更多教练了", Toast.LENGTH_SHORT).show();
                        pageIndex--;
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
    public void onLoadMore() {

        type = 1;
        pageIndex++;
        if(mSwipeToLoadLayout != null){
            mSwipeToLoadLayout.post(new Runnable() {
                @Override
                public void run() {
                  initData();
                }
            });
        }

    }

    @Override
    public void onRefresh() {
        type = 0;
        if(mSwipeToLoadLayout != null){
            mSwipeToLoadLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                 initData();
                }
            },2);
        }
    }
}
