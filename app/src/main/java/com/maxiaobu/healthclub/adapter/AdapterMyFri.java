package com.maxiaobu.healthclub.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.beangson.BeanMmanager;
import com.maxiaobu.healthclub.common.beangson.BeanMyFriend;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.maxiaobu.healthclub.R.id.view;

/**
 * Created by 莫小婷 on 2016/12/26.
 * 我的好友
 */
public class AdapterMyFri extends BaseAdapter {
    private List<BeanMyFriend.FriendListBean> mBeanMyFriend;
    private Context mContext;
    private List<Boolean> mBoolean;
    private OnClickButton mOnClickButton;
    private OnClickItem mOnClickItem;
    public void setOnClickItem(OnClickItem onClickItem) {
        mOnClickItem = onClickItem;
    }

    public void setOnClickButton(OnClickButton onClickButton) {
        mOnClickButton = onClickButton;
    }

    public AdapterMyFri(List<BeanMyFriend.FriendListBean> beanMyFriend, Context context) {
        mBeanMyFriend = beanMyFriend;
        mContext = context;
    }

    public void setBoolean(List<Boolean> aBoolean) {
        mBoolean = aBoolean;
    }

    @Override
    public int getCount() {
        return mBeanMyFriend.size();
    }

    @Override
    public Object getItem(int i) {
        return mBeanMyFriend.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_my_friend, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        ViewHolder finalHolder = holder;
        if(!mBoolean.get(i)){
            holder.mItemMutual.setBackgroundResource(R.drawable.bg_my_friend_defult);
            holder.mItemMutualText.setTextColor(0xff939393);
            holder.mItemMutualText.setText("互相关注");
            holder.mItemMutualImage.setImageResource(R.mipmap.ic_mutual);
        }else {
            holder.mItemMutual.setBackgroundResource(R.drawable.bg_my_friend_press);
            holder.mItemMutualText.setTextColor(Color.WHITE);
            holder.mItemMutualText.setText("关注");
            holder.mItemMutualImage.setImageResource(R.mipmap.ic_mutualedd);
        }

        holder.mItemMutual.setOnClickListener(view1 -> mOnClickButton.onClickLiseten(finalHolder.mItemMutual,
                finalHolder.mItemMutualText, finalHolder.mItemMutualImage , i , mBeanMyFriend.get(i).getMemid()));
        Glide.with(mContext).load(mBeanMyFriend.get(i).getImgsfilename())
                .placeholder(R.mipmap.ic_place_holder).into(holder.mItemImage);
        holder.mItemName.setText(mBeanMyFriend.get(i).getNickname());
        if(mBeanMyFriend.get(i).getSignature().equals("")){
            holder.mItemSingle.setText("暂无签名");
        }else {
            holder.mItemSingle.setText(mBeanMyFriend.get(i).getSignature());
        }

        holder.mLlLayout.setOnClickListener(view12 -> {
            if(mOnClickItem != null){
                mOnClickItem.onClickItem(mBeanMyFriend.get(i).getMemid(),mBeanMyFriend.get(i).getCurMemrole());
            }
        });
        return view;
    }

    class ViewHolder {
        @Bind(R.id.item_image)
        ImageView mItemImage;
        @Bind(R.id.item_name)
        TextView mItemName;
        @Bind(R.id.item_single)
        TextView mItemSingle;
        @Bind(R.id.item_mutual_image)
        ImageView mItemMutualImage;
        @Bind(R.id.item_mutual_text)
        TextView mItemMutualText;
        @Bind(R.id.item_mutual)
        LinearLayout mItemMutual;
        @Bind(R.id.ll_layout)
        RelativeLayout mLlLayout;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface OnClickButton{
        void onClickLiseten(LinearLayout layout,TextView textView,ImageView imageView,int pos ,String memid);
    }

    public interface OnClickItem{
        void onClickItem(String memid , String memrole);
    }
}
