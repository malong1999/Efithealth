package com.maxiaobu.healthclub.ui.fragment;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.util.NetUtils;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.chat.Constant;
import com.maxiaobu.healthclub.chat.DemoHelper;
import com.maxiaobu.healthclub.chat.db.InviteMessgeDao;
import com.maxiaobu.healthclub.ui.activity.ChatActivity;
import com.maxiaobu.healthclub.ui.activity.HomeActivity;
import com.maxiaobu.healthclub.utils.storage.SPUtils;

import maxiaobu.easeui.model.EaseAtMessageHelper;
import maxiaobu.easeui.ui.EaseConversationListFragment;

/**
 * Created by 马小布 on 2017/2/20.
 * email：maxiaobu1216@gmail.com
 * 功能：会话列表
 * 伪码：
 * 待完成：
 */
public class ConversationListFragment extends EaseConversationListFragment {
    private TextView errorText;
    private ConversationListFragment instance;
    private AlertDialog mDialog;
    private boolean mDeleteMessage;


    @Override
    protected void initView() {
        super.initView();
        View errorView = (LinearLayout) View.inflate(getActivity(), R.layout.em_chat_neterror_item, null);
        errorItemContainer.addView(errorView);
        errorText = (TextView) errorView.findViewById(R.id.tv_connect_errormsg);

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean updatenick = DemoHelper.getInstance().getUserProfileManager().updateCurrentUserNickName(SPUtils.getString(com.maxiaobu.healthclub.common.Constant.NICK_NAME));
                DemoHelper.getInstance().getUserProfileManager().setCurrentUserAvatar(SPUtils.getString(com.maxiaobu.healthclub.common.Constant.AVATAR));
                DemoHelper.getInstance().setCurrentUserName(SPUtils.getString(com.maxiaobu.healthclub.common.Constant.MEMID.toLowerCase())); // 环信Id
                if (!updatenick) {
                    Log.e("LoginActivity", "更新用户昵称失败");
                }
            }
        }).start();

    }

    @Override
    protected void setUpView() {
        type = 0;
        super.setUpView();
        instance = this;
        // register context menu
//        registerForContextMenu(conversationListView);
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
                        }

                    }
                    // it's single chat
                    intent.putExtra(Constant.EXTRA_USER_ID, username);
                    startActivity(intent);
                }
            }
        });
        conversationListView.getAdapter().setConversationListItemClickListener(conversation -> {
            String username = conversation.getUserName();
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            if (conversation.isGroup()) {
                if (conversation.getType() == EMConversation.EMConversationType.ChatRoom) {
                    intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_CHATROOM);
                } else {
                    intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_GROUP);
                }

            }
            // it's single chat
            intent.putExtra(Constant.EXTRA_USER_ID, username);
            startActivity(intent);
        });

        conversationListView.getAdapter().setOnItemLongListener(position -> {
            mDialog = onCreatDilaog(position);
            mDialog.show();
        });

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
//        Toast.makeText(App.getInstance().getApplicationContext(), "..", Toast.LENGTH_SHORT).show();
        if (isVisibleToUser) {
//            Toast.makeText(App.getInstance().getApplicationContext(), "EConversationListFragment type:" + type, Toast.LENGTH_SHORT).show();
            type = 0;
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


    public AlertDialog onCreatDilaog(final int pos) {
        mDeleteMessage = false;
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog, null);
        RelativeLayout delectMessage = (RelativeLayout) view.findViewById(R.id.delete_message);
        RelativeLayout delectConversation = (RelativeLayout) view.findViewById(R.id.delete_conversation);
        delectMessage.setOnClickListener(v -> {
            mDeleteMessage = false;
            fun(mDeleteMessage, pos);

        });

        delectConversation.setOnClickListener(v -> {
            mDeleteMessage = true;
            fun(mDeleteMessage, pos);
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

    /**
     * 上下文菜单，没用上
     *
     * @param menu
     * @param v
     * @param menuInfo
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.em_delete_message, menu);
    }

    /**
     * 上下文菜单，没用上
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        boolean deleteMessage = false;
        if (item.getItemId() == R.id.delete_message) {
            deleteMessage = true;
        } else if (item.getItemId() == R.id.delete_conversation) {
            deleteMessage = false;
        }

        EMConversation tobeDeleteCons = conversationListView.getItem(((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position);
        if (tobeDeleteCons == null) {
            return true;
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
        return true;
    }


}
