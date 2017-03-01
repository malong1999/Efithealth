package maxiaobu.easeui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMChatRoom;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMConversation.EMConversationType;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import maxiaobu.easeui.R;
import maxiaobu.easeui.domain.EaseUser;
import maxiaobu.easeui.model.EaseAtMessageHelper;
import maxiaobu.easeui.ui.EConversationListFragment;
import maxiaobu.easeui.ui.EaseConversationListFragment;
import maxiaobu.easeui.utils.EaseCommonUtils;
import maxiaobu.easeui.utils.EaseSmileUtils;
import maxiaobu.easeui.utils.EaseUserUtils;
import maxiaobu.easeui.widget.EaseConversationList;

import static com.baidu.location.d.a.i;

/**
 * conversation list adapter
 */
public class EaseConversationAdapter extends ArrayAdapter<EMConversation> {
    private static final String TAG = "ChatAllHistoryAdapter";
    private List<EMConversation> conversationList;
    private List<EMConversation> copyConversationList;
    private ConversationFilter conversationFilter;
    private boolean notiyfyByFilter;

    protected int primaryColor;
    protected int secondaryColor;
    protected int timeColor;
    protected int primarySize;
    protected int secondarySize;
    protected float timeSize;

    protected int type;
    protected  List<String> groupAvar;
    private List<String> groupid;
    private Context mContext;

    /**
     * myt添加的
     */
    private EaseConversationListFragment.EaseConversationListItemClickListener listItemClickListener;

    private EConversationListFragment.EaseConversationListItemClickListener mClickListener;
    private EConversationListFragment.OnItemLongListener mOnItemLongListener;

    int size = 0 ;
    /**
     * myt添加的
     */
    public void setConversationListItemClickListener(EaseConversationListFragment.EaseConversationListItemClickListener listItemClickListener) {
        this.listItemClickListener = listItemClickListener;
    }

    /**
     * 用来设置群头像
     * @return
     */
    public void setGroupAvar(List<String> groupAvar) {
        this.groupAvar = groupAvar;
    }

    /**
     * 用来判断是否是后台群
     * @param
     * @return
     */
    public void setGroupid(List<String> groupid,int size) {
        this.groupid = groupid;

    }

    public void setSize(int size) {
        this.size = size;
    }


    public EaseConversationAdapter setClickListener(EConversationListFragment.EaseConversationListItemClickListener clickListener) {
        mClickListener = clickListener;
        return this;
    }

    public EaseConversationAdapter setOnItemLongListener(EConversationListFragment.OnItemLongListener onItemLongListener) {
        mOnItemLongListener = onItemLongListener;
        return this;
    }

    private EConversationListFragment.EntoListener mListener;

    public void setListener(EConversationListFragment.EntoListener listener) {
//        Log.d("+++", "...");
        mListener = listener;
//        Log.d("+++", "mListener:" + mListener);
    }

    public EaseConversationAdapter(Context context, int resource,
                                   List<EMConversation> objects) {
        super(context, resource, objects);
        mContext =context;
        conversationList = objects;
        copyConversationList = new ArrayList<EMConversation>();
        copyConversationList.addAll(objects);
    }

    @Override
    public int getCount() {
        return conversationList.size();
    }

    @Override
    public EMConversation getItem(int arg0) {
        if (arg0 < conversationList.size()) {
            return conversationList.get(arg0);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.ease_row_chat_history, parent, false);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.unreadLabel = (TextView) convertView.findViewById(R.id.unread_msg_number);
            holder.message = (TextView) convertView.findViewById(R.id.message);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
            holder.msgState = convertView.findViewById(R.id.msg_state);
            holder.list_itease_layout = (RelativeLayout) convertView.findViewById(R.id.list_itease_layout);
            holder.motioned = (TextView) convertView.findViewById(R.id.mentioned);

            holder.headerLayout = (MaterialRippleLayout) convertView.findViewById(R.id.group_layout);
            holder.ItemLayout = (MaterialRippleLayout) convertView.findViewById(R.id.item_layout);
            holder.header = (RelativeLayout) convertView.findViewById(R.id.group_rl);

            convertView.setTag(holder);
        }
//        holder.list_itease_layout.setBackgroundResource(R.drawable.ease_mm_listitem);

        /**
         * myt添加的
         */
        holder.list_itease_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EMConversation conversation = getItem(position);
                listItemClickListener.onListItemClicked(conversation);
//                mClickListener.onListItemClicked(conversation);
//                Toast.makeText(getContext(), "点到了", Toast.LENGTH_SHORT).show();
            }
        });

        holder.list_itease_layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemLongListener != null)
                    mOnItemLongListener.onItemLongListener(position);
                return true;
            }
        });

        // get conversation
        EMConversation conversation = getItem(position);
        // get username or group id
        String username = conversation.getUserName();

        //聊天内容为单聊时
        if (conversation.getType() == EMConversationType.Chat) {
            holder.headerLayout.setVisibility(View.GONE);
            holder.ItemLayout.setVisibility(View.VISIBLE);
            //聊天内容为群聊时
        } else if (conversation.getType() == EMConversationType.GroupChat && position == 0) {
            holder.headerLayout.setVisibility(View.VISIBLE);
            holder.ItemLayout.setVisibility(View.VISIBLE);

            holder.header.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        EMConversation conversation = getItem(position);
                        mListener.ento();
                    }
//                Toast.makeText(getContext(), "点到了", Toast.LENGTH_SHORT).show();
                }
            });
            //聊天内容为群聊时
        } else if (conversation.getType() == EMConversationType.GroupChat && position != 0) {
            holder.headerLayout.setVisibility(View.GONE);
            holder.ItemLayout.setVisibility(View.VISIBLE);
        }

        if (conversation.getType() == EMConversationType.GroupChat) {
            String groupId = conversation.getUserName();
            if(position == 0){
                size = 0;
            }
            Log.d("++", groupId);
            if(groupid != null && groupid.size() !=0 ){
//                holder.ItemLayout.setVisibility(View.VISIBLE);
                Log.d("++", "groupid.size():" + groupid.size());
                for (int i = 0; i < groupid.size(); i++) {
                    if(groupid.get(i).equals(groupId)){
                        holder.ItemLayout.setVisibility(View.VISIBLE);
                        if (EaseAtMessageHelper.get().hasAtMeMsg(groupId)) {
                            holder.motioned.setVisibility(View.VISIBLE);
                        } else {
                            holder.motioned.setVisibility(View.GONE);
                        }
                        // group message, show group avatar
                        if(groupAvar != null && groupAvar.size() != 0){
                            Glide.with(mContext).load(groupAvar.get(i)).placeholder(R.drawable.ease_default_avatar).into(holder.avatar);
                        }else {
                            holder.avatar.setImageResource(R.drawable.ease_group_icon);
                        }

                        EMGroup group = EMClient.getInstance().groupManager().getGroup(username);
                        holder.name.setText(group != null ? group.getGroupName() : username);
                        break;
                    }else if((groupid.size() - 1 )== i ){
                        holder.ItemLayout.setVisibility(View.GONE);
                    }
                }

            }else {
                holder.ItemLayout.setVisibility(View.GONE);
            }

        } else if (conversation.getType() == EMConversationType.ChatRoom) {
            holder.avatar.setImageResource(R.drawable.ease_group_icon);
            EMChatRoom room = EMClient.getInstance().chatroomManager().getChatRoom(username);
            holder.name.setText(room != null && !TextUtils.isEmpty(room.getName()) ? room.getName() : username);
            holder.motioned.setVisibility(View.GONE);
        } else {
            EaseUserUtils.setUserAvatar(getContext(), username, holder.avatar);
            EaseUserUtils.setUserNick(username, holder.name);
            holder.motioned.setVisibility(View.GONE);
        }

        if (conversation.getUnreadMsgCount() > 0) {
            // show unread message count
            holder.unreadLabel.setText(String.valueOf(conversation.getUnreadMsgCount()));
            holder.unreadLabel.setVisibility(View.VISIBLE);
        } else {
            holder.unreadLabel.setVisibility(View.INVISIBLE);
        }

        if (conversation.getAllMsgCount() != 0) {
            // show the content of latest message
            EMMessage lastMessage = conversation.getLastMessage();
            String content = null;
            if (cvsListHelper != null) {
                content = cvsListHelper.onSetItemSecondaryText(lastMessage);
            }
            holder.message.setText(EaseSmileUtils.getSmiledText(getContext(), EaseCommonUtils.getMessageDigest(lastMessage, (this.getContext()))),
                    BufferType.SPANNABLE);
            if (content != null) {
                holder.message.setText(content);
            }
            holder.time.setText(DateUtils.getTimestampString(new Date(lastMessage.getMsgTime())));
            if (lastMessage.direct() == EMMessage.Direct.SEND && lastMessage.status() == EMMessage.Status.FAIL) {
                holder.msgState.setVisibility(View.VISIBLE);
            } else {
                holder.msgState.setVisibility(View.GONE);
            }
        }

        //set property
//        holder.name.setTextColor(primaryColor);
//        holder.message.setTextColor(secondaryColor);
//        holder.time.setTextColor(timeColor);
        holder.name.setTextColor(Color.BLACK);
        holder.message.setTextColor(0xffaeaeae);
        holder.time.setTextColor(0xffD9D9D9);
        if (primarySize != 0)
            holder.name.setTextSize(TypedValue.COMPLEX_UNIT_PX, primarySize);
        if (secondarySize != 0)
            holder.message.setTextSize(TypedValue.COMPLEX_UNIT_PX, secondarySize);
        if (timeSize != 0)
            holder.time.setTextSize(TypedValue.COMPLEX_UNIT_PX, timeSize);

        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        if (!notiyfyByFilter) {
            copyConversationList.clear();
            copyConversationList.addAll(conversationList);
            notiyfyByFilter = false;
        }
    }

    @Override
    public Filter getFilter() {
        if (conversationFilter == null) {
            conversationFilter = new ConversationFilter(conversationList);
        }
        return conversationFilter;
    }

    public EaseConversationAdapter setType(int type) {
        this.type = type;
        return this;
    }

    public void setPrimaryColor(int primaryColor) {
        this.primaryColor = primaryColor;
    }

    public void setSecondaryColor(int secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public void setTimeColor(int timeColor) {
        this.timeColor = timeColor;
    }

    public void setPrimarySize(int primarySize) {
        this.primarySize = primarySize;
    }

    public void setSecondarySize(int secondarySize) {
        this.secondarySize = secondarySize;
    }

    public void setTimeSize(float timeSize) {
        this.timeSize = timeSize;
    }


    private class ConversationFilter extends Filter {
        List<EMConversation> mOriginalValues = null;

        public ConversationFilter(List<EMConversation> mList) {
            mOriginalValues = mList;
        }

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (mOriginalValues == null) {
                mOriginalValues = new ArrayList<EMConversation>();
            }
            if (prefix == null || prefix.length() == 0) {
                results.values = copyConversationList;
                results.count = copyConversationList.size();
            } else {
                String prefixString = prefix.toString();
                final int count = mOriginalValues.size();
                final ArrayList<EMConversation> newValues = new ArrayList<EMConversation>();

                for (int i = 0; i < count; i++) {
                    final EMConversation value = mOriginalValues.get(i);
                    String username = value.getUserName();

                    EMGroup group = EMClient.getInstance().groupManager().getGroup(username);
                    if (group != null) {
                        username = group.getGroupName();
                    } else {
                        EaseUser user = EaseUserUtils.getUserInfo(username);
                        // TODO: not support Nick anymore
//                        if(user != null && user.getNick() != null)
//                            username = user.getNick();
                    }

                    // First match against the whole ,non-splitted value
                    if (username.startsWith(prefixString)) {
                        newValues.add(value);
                    } else {
                        final String[] words = username.split(" ");
                        final int wordCount = words.length;

                        // Start at index 0, in case valueText starts with space(s)
                        for (String word : words) {
                            if (word.startsWith(prefixString)) {
                                newValues.add(value);
                                break;
                            }
                        }
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            conversationList.clear();
            if (results.values != null) {
                conversationList.addAll((List<EMConversation>) results.values);
            }
            if (results.count > 0) {
                notiyfyByFilter = true;
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }

    private EaseConversationList.EaseConversationListHelper cvsListHelper;

    public void setCvsListHelper(EaseConversationList.EaseConversationListHelper cvsListHelper) {
        this.cvsListHelper = cvsListHelper;
    }

    private static class ViewHolder {
        /**
         * who you chat with
         */
        TextView name;
        /**
         * unread message count
         */
        TextView unreadLabel;
        /**
         * content of last message
         */
        TextView message;
        /**
         * time of last message
         */
        TextView time;
        /**
         * avatar
         */
        ImageView avatar;
        /**
         * status of last message
         */
        View msgState;
        /**
         * layout
         */
        RelativeLayout list_itease_layout;
        TextView motioned;

        /**
         * 2016.11.3
         */
        MaterialRippleLayout headerLayout, ItemLayout;
        RelativeLayout header;
    }


}

