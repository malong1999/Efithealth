package com.maxiaobu.healthclub.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.adapter.AdapterGroupDetails;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanEventBas;
import com.maxiaobu.healthclub.common.beangson.BeanGroupDetails;
import com.maxiaobu.healthclub.ui.weiget.gridview.MyGridView;
import com.maxiaobu.healthclub.ui.weiget.dialog.CustomDialog;
import com.maxiaobu.healthclub.ui.weiget.observablescrollview.ObservableScrollView;
import com.maxiaobu.healthclub.utils.storage.SPUtils;
import com.maxiaobu.volleykit.RequestListener;
import com.maxiaobu.volleykit.RequestParams;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import maxiaobu.easeui.widget.EaseSwitchButton;

/**
 * Created by 莫小亭 on 2017/2/20.
 * email：maxiaobu1216@gmail.com
 * 功能：群详情页
 * 伪码：
 * 待完成：
 */
public class GroupDetailsActivity extends BaseAty implements CustomDialog.SetOnClick, SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.tv_title_common)
    TextView mTvTitleCommon;
    @Bind(R.id.toolbar_common)
    Toolbar mToolbarCommon;
    @Bind(R.id.group_image)
    ImageView mGroupImage;
    @Bind(R.id.group_name)
    TextView mGroupName;
    @Bind(R.id.group_classify)
    TextView mGroupClassify;
    @Bind(R.id.group_code)
    ImageView mGroupCode;
    @Bind(R.id.group_intoUser_avatar)
    ImageView mGroupIntoUserAvatar;
    @Bind(R.id.group_message)
    RelativeLayout mGroupMessage;
    @Bind(R.id.group_summary)
    TextView mGroupSummary;
    @Bind(R.id.group_address)
    TextView mGroupAddress;
    @Bind(R.id.group_notice)
    TextView mGroupNotice;
    @Bind(R.id.group_gridView)
    MyGridView mGroupGridView;
    @Bind(R.id.group_members)
    TextView mGroupMembers;
    @Bind(R.id.group_members_num)
    TextView mGroupMembersNum;
    @Bind(R.id.group_members_layout)
    RelativeLayout mGroupMembersLayout;
    @Bind(R.id.group_cancle)
    Button mGroupCancle;
    @Bind(R.id.group_message_con)
    MaterialRippleLayout mLayout;
    @Bind(R.id.group_code_rl)
    RelativeLayout mGroupCodeLayout;
    @Bind(R.id.button_layout)
    RelativeLayout mButtonLayout;
    @Bind(R.id.switch_button)
    EaseSwitchButton mSwitchButton;
    @Bind(R.id.group_notifi_layout)
    RelativeLayout mGroupNotifiLayout;
    @Bind(R.id.group_classify_text)
    TextView mGroupClassifyText;
    @Bind(R.id.scroll_view)
    ObservableScrollView mScrollView;
    @Bind(R.id.swipe_layout)
    SwipeRefreshLayout mSwipeLayout;
    @Bind(R.id.unread_msg_number)
    ImageView mUnReadmsgImageView;
    /**
     * 本群id
     */
    private String groupId;
    /**
     * 本群对象
     */
    private String groupManager;

    private EMGroup group;
    private Menu mMenu;
    private boolean optionMenu = false;
    private CustomDialog mDialog;
    private List<BeanGroupDetails.GroupBean.MemListBean> mMemListBeen;
    private AdapterGroupDetails mAdapterGroupDetails;
    private String group_member_str;
    private String Group_Image;
    private String group_member_size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        groupId = getIntent().getStringExtra("groupId");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    group = EMClient.getInstance().groupManager().getGroupFromServer(groupId);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        if (group == null) {
            group = EMClient.getInstance().groupManager().getGroup(groupId);
        }
        setContentView(R.layout.activity_group_details);
        ButterKnife.bind(this);
        initView();
        initData();
        EventBus.getDefault().register(this);
    }

    @Override
    public void initView() {
        setCommonBackToolBar(mToolbarCommon, mTvTitleCommon, "群详情");
        mGroupCode.setColorFilter(Color.BLACK);
        mLayout.setClickable(true);
        mMemListBeen = new ArrayList<>();
        mAdapterGroupDetails = new AdapterGroupDetails(this, mMemListBeen);
        mGroupGridView.setAdapter(mAdapterGroupDetails);
        if (-1 != SPUtils.getInt(Constant.NOTIFY)) {
            if (SPUtils.getInt(Constant.NOTIFY) == 1) {
                mSwitchButton.openSwitch();
            } else {
                mSwitchButton.closeSwitch();
            }
        } else {
            mSwitchButton.openSwitch();
        }
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setRefreshing(true);
        mSwipeLayout.setColorSchemeResources(R.color.colorOrange);
        mSwipeLayout.setEnabled(true);
        mScrollView.setScrollViewListener((scrollView, x, y, oldx, oldy) -> {
            if (y == 0) {
                mSwipeLayout.setEnabled(true);
            } else {
                mSwipeLayout.setEnabled(false);
            }
        });
    }


    @Override
    public void initData() {
        RequestParams params = new RequestParams();
        params.put("groupmember.memid", SPUtils.getString(Constant.MEMID));
        params.put("groupmember.groupid", getIntent().getStringExtra("groupIid"));
        App.getRequestInstance().post(UrlPath.URL_GROUP_DETAIL, this, params, new RequestListener() {
            @Override
            public void requestSuccess(String s) {
                try {
                    mSwipeLayout.setRefreshing(false);
                    BeanGroupDetails beanGroupDetails = new Gson().fromJson(s, BeanGroupDetails.class);
                    Glide.with(GroupDetailsActivity.this).load(beanGroupDetails.getGroup().getImgsfilename()).placeholder(R.mipmap.ic_place_holder).into(mGroupImage);
                    mGroupName.setText(beanGroupDetails.getGroup().getGname());
                    mGroupAddress.setText(beanGroupDetails.getGroup().getAddress());
                    mGroupNotice.setText(beanGroupDetails.getGroup().getBulletin());
                    mGroupSummary.setText(beanGroupDetails.getGroup().getSummary());
                    groupManager = beanGroupDetails.getGroup().getManagerid();
                    mMemListBeen.clear();
                    mMemListBeen.addAll(beanGroupDetails.getGroup().getMemList());
                    mAdapterGroupDetails.notifyDataSetChanged();

                    if (beanGroupDetails.getGroup().getIsnotice().equals("1")) {
                        mSwitchButton.openSwitch();
                    } else if (beanGroupDetails.getGroup().getIsnotice().equals("0")) {
                        mSwitchButton.closeSwitch();
                    }

                    //默认群
                    if (beanGroupDetails.getGroup().getGtype().equals("0")) {
                        mGroupClassify.setVisibility(View.GONE);
                        mGroupClassifyText.setVisibility(View.GONE);
                        mLayout.setVisibility(View.GONE);
                        mButtonLayout.setVisibility(View.GONE);
                    }
                    //主题群
                    else {
                        mGroupClassify.setVisibility(View.VISIBLE);
                        mGroupClassifyText.setVisibility(View.VISIBLE);
                        mGroupClassifyText.setText(beanGroupDetails.getGroup().getGtypename());
                        mButtonLayout.setVisibility(View.VISIBLE);

                        //在主题群的群主
                        if (beanGroupDetails.getGroup().getManagerid().equals(SPUtils.getString(Constant.MEMID))) {
                            mLayout.setVisibility(View.VISIBLE);
                            mGroupCancle.setText("退出该群");

                        }
                        //群成员或者是陌生人
                        else {
                            mLayout.setVisibility(View.GONE);

                            //群成员
                            if (beanGroupDetails.getGroup().getMemcheckstatus().equals("1")) {
                                mGroupCancle.setText("退出该群");
                            } else {
                                //陌生人
                                mGroupCancle.setText("申请加入");
                            }
                        }
                    }
                    if (beanGroupDetails.getGroup().getMemcheckstatus().equals("0")) {
                        mGroupNotifiLayout.setVisibility(View.GONE);
                    } else {
                        mGroupNotifiLayout.setVisibility(View.VISIBLE);
                    }
                    group_member_str = s;
                    Group_Image = beanGroupDetails.getGroup().getImgsfilename();
                    group_member_size = String.valueOf(beanGroupDetails.getGroup().getMemList().size());
                }catch (Exception e){
                    Toast.makeText(GroupDetailsActivity.this, "获取不到数据", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void requestError(VolleyError volleyError, String s) {
                mSwipeLayout.setRefreshing(false);
            }

            @Override
            public void noInternet(VolleyError volleyError, String s) {
                mSwipeLayout.setRefreshing(false);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_group, menu);
        return true;
    }

    /**
     * 最先执行这个消息
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        String memid = SPUtils.getString(Constant.MEMID).toLowerCase();
        mMenu = menu;
        //如果是群主，那么就应该显示该显示的。
        if (group != null) {
            if (group.getOwner().equals(memid)) {

                if (menu != null) {
                    if (menu.getClass() == MenuBuilder.class) {
                        try {
                            Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                            m.setAccessible(true);
                            m.invoke(menu, true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
                optionMenu = true;
            }
            //如果不是群主，则显示不该显示的。
        } else {
            optionMenu = false;
        }
        checkOptionMenu();
        return super.onPrepareOptionsMenu(menu);
    }

    public void checkOptionMenu() {
        if (null != mMenu) {
            if (optionMenu) {
                for (int i = 0; i < mMenu.size(); i++) {
                    mMenu.getItem(i).setVisible(true);
                    mMenu.getItem(i).setEnabled(true);
                }
            } else {
                for (int i = 0; i < mMenu.size(); i++) {
                    mMenu.getItem(i).setVisible(false);
                    mMenu.getItem(i).setEnabled(false);
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.information) {
            Intent intent = new Intent(this, GroupInformation.class);
            intent.putExtra("summary", mGroupSummary.getText().toString());
            intent.putExtra("notice", mGroupNotice.getText().toString());
            intent.putExtra("id",getIntent().getStringExtra("groupIid"));
            startActivity(intent);
            return true;
        } else if (id == R.id.member) {
            Intent intent = new Intent(this, GroupMemberActivity.class);
            intent.putExtra("groupSize", group_member_size);
            intent.putExtra("groupMember", group_member_str);
            intent.putExtra("id", groupId);
            intent.putExtra("groupIid", getIntent().getStringExtra("groupIid"));
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.group_code_rl, R.id.group_message, R.id.group_members_layout, R.id.group_cancle, R.id.switch_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.group_code_rl:
                Intent intent = new Intent(this, GroupCodeActivity.class);
                intent.putExtra("groupid", groupId);
                intent.putExtra("groupname", mGroupName.getText().toString());
                intent.putExtra("image", Group_Image);
                startActivity(intent);
                break;
            case R.id.group_message:
                Intent groupEnterIntent = new Intent(this, GroupEnterActivity.class);
                groupEnterIntent.putExtra("groupIid", getIntent().getStringExtra("groupIid"));
                groupEnterIntent.putExtra("id",groupId);
                startActivity(groupEnterIntent);
                break;
            case R.id.group_members_layout:
                Intent groupMemberIntent = new Intent(this, GroupAllMemberActivity.class);
                groupMemberIntent.putExtra("group_member_str", group_member_str);
                groupMemberIntent.putExtra("id", groupId);
                startActivity(groupMemberIntent);
                break;
            case R.id.group_cancle:

                if (mGroupCancle.getText().toString().equals("退出该群")) {

                    if(group.getOwner().equals(SPUtils.getString(Constant.MEMID).toLowerCase())){
                        Toast.makeText(this, "您是群主，不能退出", Toast.LENGTH_SHORT).show();
                    }else {
                        mDialog = new CustomDialog(this, new CustomDialog.SetOnClick() {
                            @Override
                            public void onPosition(View view, AlertDialog dialog) {
                                mHandler.sendEmptyMessage(1);

                            }

                            @Override
                            public void onCancle(View view, AlertDialog dialog) {

                            }
                        });
                        mDialog.setTitle("退出该群");
                        mDialog.setContent("您确定要退出该群？");
                        mDialog.setTitleColor(Color.RED);
                        mDialog.create();
                        mDialog.show();
                    }

                } else if (mGroupCancle.getText().toString().equals("申请加入")) {

                    mDialog = new CustomDialog(this, this);
                    mDialog.setTitle("申请进入");
                    mDialog.setContent("您确定要申请进入该群？");
                    mDialog.setTitleColor(Color.BLACK);
                    mDialog.create();
                    mDialog.show();
                }
                break;
            case R.id.switch_button:

                final int falg;

                if (mSwitchButton.isSwitchOpen()) {
                    mSwitchButton.closeSwitch();
                    falg = 0;
                } else {
                    mSwitchButton.openSwitch();
                    falg = 1;
                }

                RequestParams params = new RequestParams();
                params.put("memid", SPUtils.getString(Constant.MEMID));
                params.put("groupid", getIntent().getStringExtra("groupIid"));
                params.put("isnotice", String.valueOf(falg));


                App.getRequestInstance().post(UrlPath.URL_NOTIFY, this, params, new RequestListener() {
                    @Override
                    public void requestSuccess(String s) {
                        JSONObject object = null;
                        try {
                            object = new JSONObject(s);
                            String msgFlag = (String) object.get("msgFlag");
                            String msgContent = (String) object.get("msgContent");
                            if (msgFlag.equals("1")) {
                                Toast.makeText(GroupDetailsActivity.this, msgContent, Toast.LENGTH_SHORT).show();
                                SPUtils.putInt(Constant.NOTIFY, falg);
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

                break;
        }
    }

    @Override
    public void onPosition(View view, AlertDialog dialog) {
        new Thread(new Runnable() {
            @Override
            public void run()  {
                try {
                    EMClient.getInstance().groupManager().applyJoinToGroup(getIntent().getStringExtra("groupId"), "求加入");
                    mHandler.sendEmptyMessage(2);
                } catch (HyphenateException e) {
                    Log.e("myt", "e:" + e);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(GroupDetailsActivity.this, "入群申请失败，请重新操作", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }


    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            if (msg.what == 1) {

                RequestParams params = new RequestParams();
                params.put("groupmember.memid", SPUtils.getString(Constant.MEMID));
                params.put("groupmember.groupid", getIntent().getStringExtra("groupIid"));
                App.getRequestInstance().post(UrlPath.URL_TEXT, GroupDetailsActivity.this, params, new RequestListener() {
                    @Override
                    public void requestSuccess(String s) {
                        try {
                            JSONObject object = new JSONObject(s);
                            String msgFlag = object.getString("msgFlag");
                            String msgContent = object.getString("msgContent");
                            if (msgFlag.equals("1")) {
                                Toast.makeText(GroupDetailsActivity.this, "退出成功", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent();
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra(Constant.JUMP_KEY, Constant.GROUPCANCLE_TO_TALK);
                                intent.setClass(GroupDetailsActivity.this, HomeActivity.class);
                                startActivity(intent);
                                new Thread(() -> {
                                    try {
                                        EMClient.getInstance().groupManager().leaveGroup(groupId);//需异步处理
                                    } catch (HyphenateException e) {
                                        e.printStackTrace();
                                    }
                                }).start();

                            } else {
                                Toast.makeText(GroupDetailsActivity.this, msgContent, Toast.LENGTH_SHORT).show();
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

            } else if (msg.what == 2) {

                RequestParams params = new RequestParams();
                params.put("groupmember.memid", SPUtils.getString(Constant.MEMID));
                params.put("groupmember.groupid", getIntent().getStringExtra("groupIid"));
                App.getRequestInstance().post(UrlPath.URL_GROPU_APPLY_INTO, GroupDetailsActivity.this, params, new RequestListener() {
                    @Override
                    public void requestSuccess(String s) {

                        try {
                            JSONObject object = new JSONObject(s);
                            String msgFlag = object.getString("msgFlag");
                            String msgContent = object.getString("msgContent");
                            Toast.makeText(GroupDetailsActivity.this, msgContent, Toast.LENGTH_SHORT).show();

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
            return false;
        }
    });

    @Override
    public void onCancle(View view, AlertDialog dialog) {
    }

    @Override
    public void onRefresh() {
        initData();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Refresh(BeanEventBas beanEventBas){

        if (beanEventBas.getRefresh() != null){
            mSwipeLayout.setRefreshing(true);
            initData();
        }

    }

}
