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
import com.maxiaobu.healthclub.common.beangson.BeanMyAttention;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 莫小婷 on 2016/12/27.
 *
 */

public class AdapterMyAttention extends BaseAdapter {
    private Context mContext;
    private List<BeanMyAttention.FollowlistBean> mBeanMyAttention;
    private HashMap<Integer,Boolean> mList;
    private HashMap<Integer,Boolean> mBothBoolean;
    private OnClickLisente mOnClickLisente;
    private OnClickItem mOnClickItem;

    public void setBothBoolean(HashMap<Integer,Boolean> bothBoolean) {
        mBothBoolean = bothBoolean;
    }

    public void setOnClickItem(OnClickItem onClickItem) {
        mOnClickItem = onClickItem;
    }

    public void setOnClickLisente(OnClickLisente onClickLisente) {
        mOnClickLisente = onClickLisente;
    }

    public void setList(HashMap<Integer,Boolean> list) {
        mList = list;
    }

    public AdapterMyAttention(List<BeanMyAttention.FollowlistBean> beanMyAttention, Context context) {
        mBeanMyAttention = beanMyAttention;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mBeanMyAttention.size();
    }

    @Override
    public Object getItem(int i) {
        return mBeanMyAttention.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_lv_my_attention, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if(mList.containsKey(i)){
            if (!mList.get(i)) {
                holder.mItemMutualText.setTextColor(0xff939393);
                holder.mItemMutualText.setText("已关注");
                holder.mItemMutualImage.setImageResource(R.mipmap.ic_mutualed);
                holder.mItemMutual.setBackgroundResource(R.drawable.bg_my_friend_defult);
            } else {
                holder.mItemMutual.setBackgroundResource(R.drawable.bg_my_friend_press);
                holder.mItemMutualText.setText("关注");
                holder.mItemMutualText.setTextColor(Color.WHITE);
                holder.mItemMutualImage.setImageResource(R.mipmap.ic_add);
            }
        }

        if(mBothBoolean.containsKey(i)){
            if (!mBothBoolean.get(i)) {
                holder.mItemMutualText.setTextColor(0xff939393);
                holder.mItemMutualText.setText("相互关注");
                holder.mItemMutualImage.setImageResource(R.mipmap.ic_mutual);
                holder.mItemMutual.setBackgroundResource(R.drawable.bg_my_friend_defult);
            }else {
                holder.mItemMutualText.setTextColor(Color.WHITE);
                holder.mItemMutualText.setText("关注");
                holder.mItemMutualImage.setImageResource(R.mipmap.ic_mutualedd);
                holder.mItemMutual.setBackgroundResource(R.drawable.bg_my_friend_press);
            }
        }

        ViewHolder finalHolder = holder;
        holder.mItemMutual.setOnClickListener(view1 -> mOnClickLisente.onClickLiseten(finalHolder.mItemMutual, finalHolder.mItemMutualText, finalHolder.mItemMutualImage, i,mBeanMyAttention.get(i).getMemid()));
        holder.mLlLayout.setOnClickListener(view12 -> mOnClickItem.onClickItem(i,mBeanMyAttention.get(i).getMemid(),mBeanMyAttention.get(i).getCurMemrole()));
        Glide.with(mContext).load(mBeanMyAttention.get(i).getImgsfilename()).placeholder(R.mipmap.ic_place_holder).into(holder.mItemImage);
        holder.mItemName.setText(mBeanMyAttention.get(i).getNickname());
        if(mBeanMyAttention.get(i).getSignature().equals("")){
            holder.mItemSingle.setText("暂无签名");
        }else {
            holder.mItemSingle.setText(mBeanMyAttention.get(i).getSignature());
        }
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

    public interface OnClickLisente {
        void onClickLiseten(LinearLayout layout, TextView textView, ImageView imageView, int pos,String memid);
    }

    public interface OnClickItem {
        void onClickItem(int pos, String memid,String memrole);
    }
}
