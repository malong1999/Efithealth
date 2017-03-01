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

import com.bumptech.glide.Glide;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.common.beangson.BeanOtherAttention;
import com.maxiaobu.healthclub.utils.storage.SPUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.R.attr.pointerIcon;
import static android.R.attr.spacing;
import static android.R.attr.tag;
import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * Created by 莫小婷 on 2017/1/6.
 * 关注列表
 */
public class AdapterOtherAttention extends BaseAdapter {
    private Context mContext;
    private List<BeanOtherAttention.FollowlistBean> mList;
    private OnClickLisente mOnClickLisente;
    private OnClickItem mOnClickItem;

    public AdapterOtherAttention(List<BeanOtherAttention.FollowlistBean> list,Context context) {
        mContext = context;
        mList = list;
    }

    public void setOnClickItem(OnClickItem onClickItem) {
        mOnClickItem = onClickItem;
    }

    public void setOnClickLisente(OnClickLisente onClickLisente) {
        mOnClickLisente = onClickLisente;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_lv_my_attention, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if(mList.get(position).getMemid().equals(SPUtils.getString(Constant.MEMID))){
            holder.mItemMutual.setVisibility(View.GONE);
        }else {
            holder.mItemMutual.setVisibility(View.VISIBLE);
        }

        //两者都没有关注对方
        if(mList.get(position).getSocialrel().equals("none")){
            holder.mItemMutual.setBackgroundResource(R.drawable.bg_my_friend_press);
            holder.mItemMutualImage.setImageResource(R.mipmap.ic_add);
            holder.mItemMutualText.setText("关注");
            holder.mItemMutualText.setTextColor(Color.WHITE);
            //我关注别人
        }else if(mList.get(position).getSocialrel().equals("follow")){
            holder.mItemMutual.setBackgroundResource(R.drawable.bg_my_friend_defult);
            holder.mItemMutualImage.setImageResource(R.mipmap.ic_mutualed);
            holder.mItemMutualText.setText("已关注");
            holder.mItemMutualText.setTextColor(0xff939393);
            //对方关注我了
        }else if(mList.get(position).getSocialrel().equals("follower")){
            holder.mItemMutual.setBackgroundResource(R.drawable.bg_my_friend_press);
            holder.mItemMutualImage.setImageResource(R.mipmap.ic_mutualedd);
            holder.mItemMutualText.setText("关注");
            holder.mItemMutualText.setTextColor(Color.WHITE);
            //相互关注了
        }else {
            holder.mItemMutual.setBackgroundResource(R.drawable.bg_my_friend_defult);
            holder.mItemMutualImage.setImageResource(R.mipmap.ic_mutual);
            holder.mItemMutualText.setTextColor(0xff939393);
            holder.mItemMutualText.setText("互相关注");
        }
        ViewHolder finalHolder = holder;
        holder.mItemMutual.setOnClickListener(view1 -> mOnClickLisente.onClickLiseten(finalHolder.mItemMutual, finalHolder.mItemMutualText, finalHolder.mItemMutualImage, position,mList.get(position).getMemid(),mList.get(position).getSocialrel()));
        holder.mLlLayout.setOnClickListener(view12 -> mOnClickItem.onClickItem(position,mList.get(position).getMemid(),mList.get(position).getCurMemrole()));
        Glide.with(mContext).load(mList.get(position).getImgsfilename()).placeholder(R.mipmap.ic_place_holder).into(holder.mItemImage);
        holder.mItemName.setText(mList.get(position).getNickname());
        if(mList.get(position).getSignature().equals("")){
            holder.mItemSingle.setText("暂无签名");
        }else {
            holder.mItemSingle.setText(mList.get(position).getSignature());
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
        void onClickLiseten(LinearLayout layout, TextView textView, ImageView imageView, int pos,String memid,String socialrel);
    }

    public interface OnClickItem {
        void onClickItem(int pos, String memid,String memrole);
    }
}
