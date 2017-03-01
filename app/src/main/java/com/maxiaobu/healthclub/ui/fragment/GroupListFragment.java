package com.maxiaobu.healthclub.ui.fragment;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.NetUtils;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.chat.Constant;
import com.maxiaobu.healthclub.chat.DemoHelper;
import com.maxiaobu.healthclub.chat.db.InviteMessgeDao;
import com.maxiaobu.healthclub.chat.db.UserDao;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanGroupDetails;
import com.maxiaobu.healthclub.ui.activity.ChatActivity;
import com.maxiaobu.healthclub.ui.activity.GroupActivity;
import com.maxiaobu.healthclub.ui.activity.HomeActivity;
import com.maxiaobu.healthclub.utils.storage.SPUtils;
import com.maxiaobu.volleykit.RequestListener;
import com.maxiaobu.volleykit.RequestParams;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import maxiaobu.easeui.domain.EaseUser;
import maxiaobu.easeui.model.EaseAtMessageHelper;
import maxiaobu.easeui.ui.EConversationListFragment;
import maxiaobu.easeui.ui.EaseConversationListFragment;

/**
 * Created by 马小布 on 2017/2/20.
 * email：maxiaobu1216@gmail.com
 * 功能：群列表
 * 伪码：
 * 待完成：
 */
public class GroupListFragment extends EConversationListFragment
        implements EConversationListFragment.EntoListener {
    private TextView errorText;
    private AlertDialog mDialog;
    private boolean mDeleteMessage;
    /**
     * 后台id
     */
    private List<String> groupIidList;

    public GroupListFragment() {
    }

    @Override
    protected void initView() {
        super.initView();
        View errorView = (LinearLayout) View.inflate(getActivity(), R.layout.em_chat_neterror_item, null);
        errorItemContainer.addView(errorView);
        errorText = (TextView) errorView.findViewById(R.id.tv_connect_errormsg);

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean updatenick = DemoHelper.getInstance().getUserProfileManager()
                        .updateCurrentUserNickName(SPUtils.getString
                                (com.maxiaobu.healthclub.common.Constant.NICK_NAME));
                DemoHelper.getInstance().getUserProfileManager().setCurrentUserAvatar
                        (SPUtils.getString(com.maxiaobu.healthclub.common.Constant.AVATAR));
                DemoHelper.getInstance().setCurrentUserName(SPUtils.getString
                        (com.maxiaobu.healthclub.common.Constant.MEMID.toLowerCase())); // 环信Id
                if (!updatenick) {
                    Log.e("LoginActivity", "更新用户昵称失败");
                }
            }
        }).start();

    }


    @Override
    protected void setUpView() {
        type = 1;
        super.setUpView();

        groupIidList = new ArrayList<>();

        List<String> groupIdList = App.getInstance().getGroupIdList();
        List<String> groupiidList = App.getInstance().getGroupIidList();
        List<String> AvarList = App.getInstance().getAvarList();

        for (String s : groupIdList) {
            groupid.add(s);
        }

        for (String s : groupiidList) {
            groupIidList.add(s);
        }

        for (String s : AvarList) {
            groupAvar.add(s);
        }

        conversationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EMConversation conversation = conversationListView.getItem(position);
                String username = conversation.getUserName();
                if (username.equals(EMClient.getInstance().getCurrentUser()))
                    Toast.makeText(getActivity(), R.string.Cant_chat_with_yourself, Toast.LENGTH_SHORT).show();
                else {
                    // start chat acitivity
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    if (conversation.isGroup()) {
                        if (conversation.getType() == EMConversation.EMConversationType.ChatRoom) {
                            // it's group chat
                            intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_CHATROOM);
                        } else {
                            intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_GROUP);
                            for (int i = 0; i < groupid.size(); i++) {
                                if (username.equals(groupid.get(i))) {

                                    intent.putExtra("groupId", groupIidList.get(i));
                                }
                            }
                        }

                    }
                    // it's single chat
                    intent.putExtra(Constant.EXTRA_USER_ID, username);
                    startActivity(intent);
                }
            }
        });

        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        synchronized (conversations) {
            for (final EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    String groupId = conversation.getUserName();

                    for (int i = 0; i < groupid.size(); i++) {
                        if (groupId.equals(groupid.get(i))) {
                            RequestParams params = new RequestParams();
                            params.put("groupmember.memid", SPUtils.getString(com.maxiaobu.healthclub.common.Constant.MEMID));
                            params.put("groupmember.groupid", groupIidList.get(i));
                            App.getRequestInstance().post(UrlPath.URL_GROUP_DETAIL, getActivity(), params, new RequestListener() {
                                @Override
                                public void requestSuccess(String s) {
                                    BeanGroupDetails beanGroupList = new Gson().fromJson(s, BeanGroupDetails.class);

                                    for (int i = 0; i < beanGroupList.getGroup().getMemList().size(); i++) {
                                        String avatar = beanGroupList.getGroup().getMemList().get(i).getImgsfilename();
                                        String userName = beanGroupList.getGroup().getMemList().get(i).getNickname();
                                        String hxIdFrom = beanGroupList.getGroup().getMemList().get(i).getMemid();
                                        EaseUser easeUser = new EaseUser(hxIdFrom.toLowerCase());
                                        easeUser.setAvatar(avatar);
                                        easeUser.setNick(userName);
                                        // 存入内存
                                        Map<String, EaseUser> contactList = DemoHelper.getInstance().getContactList();
                                        contactList.put(hxIdFrom.toLowerCase(), easeUser);
                                        // 存入db
                                        UserDao dao = new UserDao(App.getInstance());
                                        List<EaseUser> users = new ArrayList<>();
                                        users.add(easeUser);
                                        dao.saveContactList(users);
                                        DemoHelper.getInstance().getModel().setContactSynced(true);
                                        DemoHelper.getInstance().notifyContactsSyncListener(true);
                                    }

                                    conversationListView.getAdapter().setGroupAvar(groupAvar);
                                    conversationListView.getAdapter().setGroupid(groupid, groupid.size());
                                    conversationListView.getAdapter().setSize(0);
                                    conversationListView.refresh();

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

                    sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
                }
            }
        }

        conversationListView.getAdapter().setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {
            @Override
            public void onListItemClicked(EMConversation conversation) {
                String username = conversation.getUserName();
                // start chat acitivity
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                if (conversation.isGroup()) {
                    if (conversation.getType() == EMConversation.EMConversationType.ChatRoom) {
                        // it's group chat
                        intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_CHATROOM);
                    } else {
                        intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_GROUP);
                        for (int i = 0; i < groupid.size(); i++) {
                            if (username.equals(groupid.get(i))) {

                                intent.putExtra("groupId", groupIidList.get(i));
                            }
                        }
                    }

                }
                // it's single chat
                intent.putExtra(Constant.EXTRA_USER_ID, username);
                startActivity(intent);
            }
        });

        conversationListView.getAdapter().setOnItemLongListener(new OnItemLongListener() {
            @Override
            public void onItemLongListener(int position) {
                mDialog = onCreatDilaog(position);
                mDialog.show();
            }
        });

//        setListener(this);

        conversationListView.getAdapter().setListener(this);
        setEntoListener(this);

    }

    public AlertDialog onCreatDilaog(final int pos) {
        mDeleteMessage = false;
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog, null);
        RelativeLayout delectMessage = (RelativeLayout) view.findViewById(R.id.delete_message);
        RelativeLayout delectConversation = (RelativeLayout) view.findViewById(R.id.delete_conversation);
        delectMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDeleteMessage = false;
                fun(mDeleteMessage, pos);

            }
        });

        delectConversation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDeleteMessage = true;
                fun(mDeleteMessage, pos);
            }
        });
        builder.setView(view);

        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    public void fun(boolean deleteMessage, int pos) {
        mDialog.dismiss();
        EMConversation tobeDeleteCons = conversationListView.getItem(pos);
        if (tobeDeleteCons == null) {
            return;
        }
        if (tobeDeleteCons.getType() == EMConversation.EMConversationType.GroupChat) {
            EaseAtMessageHelper.get().removeAtMeGroup(tobeDeleteCons.getUserName());
        }
        try {
            // delete conversation
            EMClient.getInstance().chatManager().deleteConversation(tobeDeleteCons.getUserName(), deleteMessage);
            InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(getActivity());
            inviteMessgeDao.deleteMessage(tobeDeleteCons.getUserName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        refresh();

        // update unread count
        ((HomeActivity) getActivity()).updateUnreadLabel();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            type = 1;
        }
    }

    @Override
    protected void onConnectionDisconnected() {
        super.onConnectionDisconnected();
        if (NetUtils.hasNetwork(getActivity())) {
            errorText.setText(R.string.can_not_connect_chat_server_connection);
        } else {
            errorText.setText(R.string.the_current_network);
        }
    }

    public static Fragment newInstance() {
        GroupListFragment fragment = new GroupListFragment();
        return fragment;
    }


    @Override
    public void ento() {
        startActivity(new Intent(getActivity(), GroupActivity.class));
    }


    public void refreshUI() {
        List<String> groupIdList = App.getInstance().getGroupIdList();
        List<String> groupiidList = App.getInstance().getGroupIidList();
        List<String> AvarList = App.getInstance().getAvarList();

        //刷新一边数据，先清空以前数据
        if (groupid.size() != 0) {
            groupid.clear();
        }
        if (groupIidList.size() != 0) {
            groupIidList.clear();
        }
        if (groupAvar.size() != 0) {
            groupAvar.clear();
        }

        //重新加载数据
        for (String s : groupIdList) {
            groupid.add(s);
        }

        for (String s : groupiidList) {
            groupIidList.add(s);
        }

        for (String s : AvarList) {
            groupAvar.add(s);
        }

        refresh();
    }
}
