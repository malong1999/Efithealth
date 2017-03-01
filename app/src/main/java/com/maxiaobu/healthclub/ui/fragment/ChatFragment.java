package com.maxiaobu.healthclub.ui.fragment;


import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.EasyUtils;
import com.hyphenate.util.PathUtil;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.chat.Constant;
import com.maxiaobu.healthclub.chat.DemoHelper;
import com.maxiaobu.healthclub.chat.domain.EmojiconExampleGroupData;
import com.maxiaobu.healthclub.chat.ui.VideoCallActivity;
import com.maxiaobu.healthclub.chat.ui.VoiceCallActivity;
import com.maxiaobu.healthclub.chat.widget.ChatRowVoiceCall;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanMe;
import com.maxiaobu.healthclub.ui.activity.ChatRoomDetailsActivity;
import com.maxiaobu.healthclub.ui.activity.ClubDetailActivity;
import com.maxiaobu.healthclub.ui.activity.ContextMenuActivity;
import com.maxiaobu.healthclub.ui.activity.GroupDetailsActivity;
import com.maxiaobu.healthclub.ui.activity.HomeActivity;
import com.maxiaobu.healthclub.ui.activity.OrderListActivity;
import com.maxiaobu.healthclub.ui.activity.PersionalActivity;
import com.maxiaobu.healthclub.ui.activity.WebActivity;
import com.maxiaobu.healthclub.utils.storage.SPUtils;
import com.maxiaobu.volleykit.RequestJsonListener;
import com.maxiaobu.volleykit.RequestListener;
import com.maxiaobu.volleykit.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import cn.bingoogolapple.qrcode.zxing.QRCodeDecoder;
import maxiaobu.easeui.EaseConstant;
import maxiaobu.easeui.domain.EaseUser;
import maxiaobu.easeui.model.EaseImageCache;
import maxiaobu.easeui.ui.EaseChatFragment;
import maxiaobu.easeui.utils.EaseImageUtils;
import maxiaobu.easeui.utils.EaseUserUtils;
import maxiaobu.easeui.widget.chatrow.EaseChatRow;
import maxiaobu.easeui.widget.chatrow.EaseCustomChatRowProvider;
import maxiaobu.easeui.widget.emojicon.EaseEmojiconMenu;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 聊天主界面
 */
public class ChatFragment extends EaseChatFragment implements EaseChatFragment.EaseChatFragmentHelper {
    // constant start from 11 to avoid conflict with constant in base class
    private static final int ITEM_VIDEO = 11;
    private static final int ITEM_FILE = 12;
    private static final int ITEM_VOICE_CALL = 13;
    private static final int ITEM_VIDEO_CALL = 14;

    private static final int REQUEST_CODE_SELECT_VIDEO = 11;
    private static final int REQUEST_CODE_SELECT_FILE = 12;
    private static final int REQUEST_CODE_GROUP_DETAIL = 13;
    private static final int REQUEST_CODE_CONTEXT_MENU = 14;
    private static final int REQUEST_CODE_SELECT_AT_USER = 15;


    private static final int MESSAGE_TYPE_SENT_VOICE_CALL = 1;
    private static final int MESSAGE_TYPE_RECV_VOICE_CALL = 2;
    private static final int MESSAGE_TYPE_SENT_VIDEO_CALL = 3;
    private static final int MESSAGE_TYPE_RECV_VIDEO_CALL = 4;
    /**
     * if it is chatBot
     */
    private boolean isRobot;
    private String groupId;

    private Boolean group_chat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void setUpView() {
        setChatFragmentListener(this);
        if (chatType == Constant.CHATTYPE_GROUP) {
//            Map<String, RobotUser> robotMap = DemoHelper.getInstance().getRobotList();
//            if (robotMap != null && robotMap.containsKey(toChatUsername)) {
//                isRobot = true;
//            }
            group_chat = true;

        }else {
            group_chat = false;
        }

        super.setUpView();
        if (EaseUserUtils.getUserInfo(toChatUsername) != null) {
            EaseUser user = EaseUserUtils.getUserInfo(toChatUsername);
            if (user != null && user.getNickname() != null && user.getAvatar() != null) {
                titleBar.setTitle(user.getNick());
            } else {
            }
        }
        // set click listener
        titleBar.setLeftLayoutClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (EasyUtils.isSingleActivity(getActivity())) {
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    startActivity(intent);
                }
                onBackPressed();
            }
        });
        ((EaseEmojiconMenu) inputMenu.getEmojiconMenu()).addEmojiconGroup(EmojiconExampleGroupData.getData());
        if (chatType == EaseConstant.CHATTYPE_GROUP) {
            //在聊天群里监听输入“@”字符，跳转到联系人列表 // TODO: 2016/10/31  
            inputMenu.getPrimaryMenu().getEditText().addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

        //更新头像：
        DemoHelper.getInstance().getUserProfileManager().setCurrentUserAvatar(SPUtils.getString(com.maxiaobu.healthclub.common.Constant.AVATAR));
    }

    @Override
    protected void registerExtendMenuItem() {
        super.registerExtendMenuItem();
        inputMenu.registerExtendMenuItem(R.string.attach_video, R.drawable.em_chat_video_selector, ITEM_VIDEO, extendMenuItemClickListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SELECT_VIDEO: //send the video
                    if (data != null) {
                        int duration = data.getIntExtra("dur", 0);
                        String videoPath = data.getStringExtra("path");
                        File file = new File(PathUtil.getInstance().getImagePath(), "thvideo" + System.currentTimeMillis());
                        try {
                            FileOutputStream fos = new FileOutputStream(file);
                            Bitmap ThumbBitmap = ThumbnailUtils.createVideoThumbnail(videoPath, 3);
                            ThumbBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                            fos.close();
                            sendVideoMessage(videoPath, file.getAbsolutePath(), duration);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case REQUEST_CODE_SELECT_FILE: //send the file
                    if (data != null) {
                        Uri uri = data.getData();
                        if (uri != null) {
                            sendFileByUri(uri);
                        }
                    }
                    break;
                case REQUEST_CODE_SELECT_AT_USER:
                    if (data != null) {
                        String username = data.getStringExtra("username");
                        inputAtUsername(username, false);
                    }
                    break;
                default:
                    break;
            }
        }
        if (requestCode == REQUEST_CODE_CONTEXT_MENU) {
            switch (resultCode) {
                case ContextMenuActivity.RESULT_CODE_COPY: // copy
                    clipboard.setPrimaryClip(ClipData.newPlainText(null,
                            ((EMTextMessageBody) contextMenuMessage.getBody()).getMessage()));
                    break;
                case ContextMenuActivity.RESULT_CODE_DELETE: // delete
                    conversation.removeMessage(contextMenuMessage.getMsgId());
                    messageList.refresh();
                    break;

                case ContextMenuActivity.RESULT_CODE_FORWARD: // forward
//                    Intent intent = new Intent(getActivity(), ForwardMessageActivity.class);
//                    intent.putExtra("forward_msg_id", contextMenuMessage.getMsgId());
//                    startActivity(intent);
                    Toast.makeText(getActivity(), "正在开发", Toast.LENGTH_SHORT).show();
                    break;
                case ContextMenuActivity.RESULT_CODE_RECODE:
                        EMMessage message = data.getParcelableExtra("message");
                        EMImageMessageBody imgBody = (EMImageMessageBody) message.getBody();
                        String thumbPath = imgBody.thumbnailLocalPath();

                        Observable.just(thumbPath)
                                .subscribeOn(Schedulers.computation())
                                .observeOn(AndroidSchedulers.mainThread())
                                .map(thumbPath1 -> {
                                    Bitmap bitmap1 = BitmapFactory.decodeFile(thumbPath1);
                                    if (!new File(thumbPath1).exists()) {
                                        thumbPath1 = EaseImageUtils.getThumbnailImagePath(imgBody.getLocalUrl());
                                        bitmap1 = EaseImageCache.getInstance().get(thumbPath1);
                                    }
                                    QRCodeDecoder.syncDecodeQRCode(bitmap1);
                                    return QRCodeDecoder.syncDecodeQRCode(bitmap1);
                                })
                                .subscribe(new Subscriber<String>() {
                                    @Override
                                    public void onCompleted() {
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Toast.makeText(getActivity(), "解析失败", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onNext(String s) {
                                        if (TextUtils.isEmpty(s)) {
                                            Toast.makeText(getActivity(), "没解析出来", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onSetMessageAttributes(EMMessage message) {
        if (isRobot) {
            // 设置消息扩展属性
            message.setAttribute("em_robot_message", isRobot);
        }
        // 通过扩展属性，将userPic和userName发送出去。
        String avatar = SPUtils.getString(com.maxiaobu.healthclub.common.Constant.AVATAR);
        if (!TextUtils.isEmpty(avatar)) {
            message.setAttribute("avatar", avatar);
        }
        String nickName = SPUtils.getString(com.maxiaobu.healthclub.common.Constant.NICK_NAME);
        if (!TextUtils.isEmpty(nickName)) {
            message.setAttribute("nickName", nickName);
        }
        String to = message.getTo();
        EaseUser userInfo = DemoHelper.getInstance().getUserInfo(to);
        String nickNameTo = userInfo.getNickname();
        String avatarTo = userInfo.getAvatar();
        message.setAttribute("avatarTo",avatarTo);
        message.setAttribute("nickNameTo",nickNameTo);
        Log.d(TAG, nickNameTo+"-----"+avatarTo);
    }

    @Override
    public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
        return new CustomChatRowProvider();
    }

    @Override
    public void onEnterToChatDetails() {
        if (chatType == Constant.CHATTYPE_GROUP) {
            EMGroup group = EMClient.getInstance().groupManager().getGroup(toChatUsername);
            if (group == null) {
                Toast.makeText(getActivity(), R.string.gorup_not_found, Toast.LENGTH_SHORT).show();
                return;
            }
            startActivityForResult(
                    (new Intent(getActivity(), GroupDetailsActivity.class).putExtra("groupId", toChatUsername).putExtra("groupIid",getArguments().getString("groupId"))),
                    REQUEST_CODE_GROUP_DETAIL);
        } else if (chatType == Constant.CHATTYPE_CHATROOM) {
            startActivityForResult(new Intent(getActivity(), ChatRoomDetailsActivity.class).putExtra("roomId", toChatUsername), REQUEST_CODE_GROUP_DETAIL);
        }
    }

    @Override
    public void onAvatarClick(final String username) {
        String userna = username.substring(0,1);
        Log.d("++", userna);
        if(userna.equals("m")){
            App.getRequestInstance().post(getActivity(), UrlPath.URL_ME, BeanMe.class,
                    new RequestParams("memid", username.toUpperCase()),
                    new RequestJsonListener<BeanMe>() {
                        @Override
                        public void requestSuccess(BeanMe result) {
                            Intent intent;
                            String memrole = result.getMember().getMemrole();
                            if ("clubadmin".equals(memrole)) {
                                intent = new Intent(getActivity(), ClubDetailActivity.class);
                            } else {
                                intent = new Intent(getActivity(), PersionalActivity.class);
                            }
                            intent.putExtra("memrole", memrole);
                            intent.putExtra("tarid", username.toUpperCase());
                            intent.putExtra("fromChat", true);
                            intent.putExtra("group_chat",group_chat);
                            startActivity(intent);
                        }

                        @Override
                        public void requestError(VolleyError volleyError, String s) {

                        }

                        @Override
                        public void noInternet(VolleyError volleyError, String s) {

                        }

                    });
        }else {
            Toast.makeText(getActivity(), "这是系统人物", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onAvatarLongClick(String username) {
        inputAtUsername(username);
    }


    @Override
    public boolean onMessageBubbleClick(EMMessage message) {
        return false;
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> messages) {
        super.onCmdMessageReceived(messages);
    }

    @Override
    public void onMessageBubbleLongClick(EMMessage message) {
        startActivityForResult((new Intent(getActivity(), ContextMenuActivity.class)).putExtra("message", message)
                        .putExtra("ischatroom", chatType == EaseConstant.CHATTYPE_CHATROOM),
                REQUEST_CODE_CONTEXT_MENU);
    }


    /**
     * 结果处理
     *
     * @param resultString
     */
    private void onResultHandler(String resultString) {
        if (TextUtils.isEmpty(resultString)) {
            Toast.makeText(getContext(), "扫描失败！", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            String[] str = resultString.split("//");
//            Log.d("MyCodeActivity", str[0]);
            if (str[0].equals("interface:")) {
//                Log.d("----", "mmmm");
                String url = str[1];
//                Log.d("++++", UrlPath.URL_BASE + url + "&memid=" + SPUtils.getString(com.maxiaobu.healthclub.common.Constant.MEMID));
                App.getRequestInstance().post(UrlPath.URL_BASE + url + "&memid=" +
                                SPUtils.getString(com.maxiaobu.healthclub.common.Constant.MEMID),
                        getContext(), null, new RequestListener() {
                            @Override
                            public void requestSuccess(String json) {
                                try {
                                    JSONObject object = null;
                                    object = new JSONObject(json);
                                    Object msgFlag = object.get("msgFlag");
                                    Object msgContent = object.get("msgContent");
                                    if (msgFlag.equals("1")) {
                                        getContext().startActivity(new Intent(getContext(), OrderListActivity.class));
                                    } else if (msgFlag.equals("0")) {
                                        if (msgContent != null) {
                                            Toast.makeText(getContext(), msgContent.toString(), Toast.LENGTH_SHORT).show();
                                        }
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
            } else if (str[0].equals("http:")) {

                Intent intent = new Intent(getContext(), WebActivity.class);
                intent.putExtra("url", resultString);
                startActivity(intent);

            } else if (str[0].equals("member:")) {
                Toast.makeText(getContext(), "社交功能即将开放", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), resultString, Toast.LENGTH_SHORT).show();
            }

        } catch (NullPointerException e) {
            Toast.makeText(getContext(), "二维码错误", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public boolean onExtendMenuItemClick(int itemId, View view) {
        switch (itemId) {
            case ITEM_VIDEO:
//                Intent intent = new Intent(getActivity(), ImageGridActivity.class);
//                startActivityForResult(intent, REQUEST_CODE_SELECT_VIDEO);
                Toast.makeText(getContext(), "该功能正在开发，请敬请期待", Toast.LENGTH_SHORT).show();
                break;
            case ITEM_FILE: //file
                selectFileFromLocal();
                break;
            case ITEM_VOICE_CALL:
                startVoiceCall();
                break;
            case ITEM_VIDEO_CALL:
                startVideoCall();
                break;
            default:
                break;
        }
        //keep exist extend menu
        return false;
    }

    /**
     * select file
     */
    protected void selectFileFromLocal() {
        Intent intent = null;
        if (Build.VERSION.SDK_INT < 19) { //api 19 and later, we can't use this way, demo just select from images
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);

        } else {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_SELECT_FILE);
    }

    /**
     * make a voice call
     */
    protected void startVoiceCall() {
        if (!EMClient.getInstance().isConnected()) {
            Toast.makeText(getActivity(), R.string.not_connect_to_server, Toast.LENGTH_SHORT).show();
        } else {
            startActivity(new Intent(getActivity(), VoiceCallActivity.class).putExtra("username", toChatUsername)
                    .putExtra("isComingCall", false));
            // voiceCallBtn.setEnabled(false);
            inputMenu.hideExtendMenuContainer();
        }
    }

    /**
     * make a video call
     */
    protected void startVideoCall() {
        if (!EMClient.getInstance().isConnected())
            Toast.makeText(getActivity(), R.string.not_connect_to_server, Toast.LENGTH_SHORT).show();
        else {
            startActivity(new Intent(getActivity(), VideoCallActivity.class).putExtra("username", toChatUsername)
                    .putExtra("isComingCall", false));
            // videoCallBtn.setEnabled(false);
            inputMenu.hideExtendMenuContainer();
        }
    }

    /**
     * chat row provider
     */
    private final class CustomChatRowProvider implements EaseCustomChatRowProvider {
        @Override
        public int getCustomChatRowTypeCount() {
            //here the number is the message type in EMMessage::Type
            //which is used to count the number of different chat row
            return 8;
        }

        @Override
        public int getCustomChatRowType(EMMessage message) {
            if (message.getType() == EMMessage.Type.TXT) {
                //voice call
                if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VOICE_CALL : MESSAGE_TYPE_SENT_VOICE_CALL;
                } else if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VIDEO_CALL, false)) {
                    //video call
                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VIDEO_CALL : MESSAGE_TYPE_SENT_VIDEO_CALL;
                }
            }
            return 0;
        }

        @Override
        public EaseChatRow getCustomChatRow(EMMessage message, int position, BaseAdapter adapter) {
            if (message.getType() == EMMessage.Type.TXT) {
                // voice call or video call
                if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false) ||
                        message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VIDEO_CALL, false)) {
                    return new ChatRowVoiceCall(getActivity(), message, position, adapter);
                }
            }
            return null;
        }

    }


}
