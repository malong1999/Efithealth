package com.maxiaobu.healthclub.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.beangson.BeanMyFans;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 莫小婷 on 2016/12/27.
 * 我的粉丝
 */
public class AdapterMyFans extends BaseAdapter {
    private Context mContext;
    private List<BeanMyFans.FollowerlistBean> mBeanMyFans;
    private HashMap<Integer,Boolean> mBooleen;
    private HashMap<Integer,Boolean> mBothBoolean;
    private OnClickLayout mOnClickLayout;
    private OnClickItem mOnClickIteml;

    public void setBothBoolean(HashMap<Integer, Boolean> bothBoolean) {
        mBothBoolean = bothBoolean;
    }

    public void setOnClickItem(OnClickItem onClickIteml) {
        mOnClickIteml = onClickIteml;
    }

    public void setOnClickLayout(OnClickLayout onClickLayout) {
        mOnClickLayout = onClickLayout;
    }

    public void setBooleen(HashMap<Integer,Boolean> booleen) {
        mBooleen = booleen;
    }

    public AdapterMyFans(List<BeanMyFans.FollowerlistBean> beanMyFans, Context context) {
        mBeanMyFans = beanMyFans;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mBeanMyFans.size();
    }

    @Override
    public Object getItem(int i) {
        return mBeanMyFans.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder = null;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_lv_my_fans, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if(mBooleen.containsKey(i)){
            if(!mBooleen.get(i)){
                holder.mItemMutualText.setTextColor(Color.WHITE);
                holder.mItemMutualText.setText("关注");
                holder.mItemMutualImage.setImageResource(R.mipmap.ic_add);
                holder.mItemMutual.setBackgroundResource(R.drawable.bg_my_friend_press);
            }else {
                holder.mItemMutualText.setTextColor(0xff939393);
                holder.mItemMutual.setBackgroundResource(R.drawable.bg_my_friend_defult);
                holder.mItemMutualText.setText("已关注");
                holder.mItemMutualImage.setImageResource(R.mipmap.ic_mutualed);
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
        holder.mItemMutual.setOnClickListener(view1 -> mOnClickLayout.onClickLayout(finalHolder.mItemMutual, finalHolder.mItemMutualText, finalHolder.mItemMutualImage,i,mBeanMyFans.get(i).getMemid()));
        holder.mLlLayout.setOnClickListener(view12 -> mOnClickIteml.onClickItem(i,mBeanMyFans.get(i).getMemid(),mBeanMyFans.get(i).getCurMemrole()));
        Glide.with(mContext).load(mBeanMyFans.get(i).getImgsfilename()).placeholder(R.mipmap.ic_place_holder).into(holder.mItemImage);
        holder.mItemName.setText(mBeanMyFans.get(i).getNickname());
        if(mBeanMyFans.get(i).getSignature().equals("")){
            holder.mItemSingle.setText("暂无签名");
        }else {
            holder.mItemSingle.setText(mBeanMyFans.get(i).getSignature());
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

    public interface OnClickLayout{
        void onClickLayout(LinearLayout layout,TextView textView,ImageView imageView,int pos,String memid);
    }

    public interface OnClickItem{
        void onClickItem(int pos,String memid,String memrole);
    }
}
