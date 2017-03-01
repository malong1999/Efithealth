package com.maxiaobu.healthclub.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.beangson.BeanGroupEnter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 莫小婷 on 2016/12/2.
 * 加入群
 */

public class AdapterGroupEnter extends RecyclerView.Adapter<AdapterGroupEnter.ViewHolder> {
    private Context mContext;
    private List<BeanGroupEnter.MemberlistBean> mMemberlistBeen;
    private OnClickPassButton mOnClickPassButton;
    private OnLongClickRemove mLongClickRemove;

    public interface OnClickPassButton {
        void onPass(View v,String memid);
    }

    public interface OnLongClickRemove{
        void onRemove(View v,String memid);
    }

    public void setLongClickRemove(OnLongClickRemove longClickRemove) {
        mLongClickRemove = longClickRemove;
    }

    public void setOnClickPassButton(OnClickPassButton onClickPassButton) {
        mOnClickPassButton = onClickPassButton;
    }

    public void setMemberlistBeen(List<BeanGroupEnter.MemberlistBean> memberlistBeen) {
        mMemberlistBeen = memberlistBeen;
    }

    public AdapterGroupEnter(Context context) {
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_rv_group_enter, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mItemPositiveButton.setText("通过");
        Glide.with(mContext).load(mMemberlistBeen.get(position).getImgsfilename()).placeholder(R.mipmap.ic_place_holder).into(holder.mItemImage);
        holder.mItemName.setText(mMemberlistBeen.get(position).getNickname());
        holder.mItemSingle.setText(mMemberlistBeen.get(position).getSignature());
        holder.mItemPositiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickPassButton != null) {
                    mOnClickPassButton.onPass(v,mMemberlistBeen.get(position).getMemid());
                }
            }
        });

        holder.mRlLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(mLongClickRemove != null){
                    mLongClickRemove.onRemove(v,mMemberlistBeen.get(position).getMemid());
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMemberlistBeen.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.item_image)
        ImageView mItemImage;
        @Bind(R.id.item_name)
        TextView mItemName;
        @Bind(R.id.item_single)
        TextView mItemSingle;
        @Bind(R.id.item_positiveButton)
        TextView mItemPositiveButton;
        @Bind(R.id.rl_layout)
        RelativeLayout mRlLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
