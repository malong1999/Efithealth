package com.maxiaobu.healthclub.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.beangson.BeanGroupDetails;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 莫小婷 on 2016/12/2.
 * 群成员管理
 */
public class AdapterGroupMemberManage extends RecyclerView.Adapter<AdapterGroupMemberManage.ViewHolder> {
    private Context mContext;
    private List<BeanGroupDetails.GroupBean.MemListBean> mListBeen;
    private OnRemoveClick mOnRemoveClick;
    private String manageMemid;

    public void setManageMemid(String manageMemid) {
        this.manageMemid = manageMemid;
    }

    public interface OnRemoveClick{
        void onRemoveMenber(View view, String memid, int pos);
    }

    public void setOnRemoveClick(OnRemoveClick onRemoveClick) {
        mOnRemoveClick = onRemoveClick;
    }

    public void setListBeen(List<BeanGroupDetails.GroupBean.MemListBean> listBeen) {
        mListBeen = listBeen;
    }

    public AdapterGroupMemberManage(Context context) {
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
        holder.mItemPositiveButton.setText("移除");
        Glide.with(mContext).load(mListBeen.get(position).getImgsfilename()).placeholder(R.mipmap.ic_place_holder).into(holder.mItemImage);
        holder.mItemName.setText(mListBeen.get(position).getNickname());
        holder.mItemSingle.setText(mListBeen.get(position).getSignature());
        if(manageMemid != null){
            if(mListBeen.get(position).getMemid().equals(manageMemid)){
                holder.mItemPositiveButton.setVisibility(View.GONE);
            }else {
                holder.mItemPositiveButton.setVisibility(View.VISIBLE);
            }
        }

        holder.mItemPositiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnRemoveClick != null){
                    mOnRemoveClick.onRemoveMenber(v,mListBeen.get(position).getMemid(),position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListBeen.size();
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

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
