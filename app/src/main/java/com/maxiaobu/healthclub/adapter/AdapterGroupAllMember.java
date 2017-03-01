package com.maxiaobu.healthclub.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.beangson.BeanGroupDetails;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 莫小婷 on 2016/12/2.
 * 群成员
 */

public class AdapterGroupAllMember extends RecyclerView.Adapter<AdapterGroupAllMember.ViewHolder> {
    private Context mContext;
    private List<BeanGroupDetails.GroupBean.MemListBean> mMemListBeen;
    private OnItemClick mOnItemClick;

    public void setOnItemClick(OnItemClick onItemClick) {
        mOnItemClick = onItemClick;
    }

    public AdapterGroupAllMember(Context context, List<BeanGroupDetails.GroupBean.MemListBean> memListBeen) {
        mContext = context;
        mMemListBeen = memListBeen;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_rv_group_all_member, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(mContext).load(mMemListBeen.get(position).getImgsfilename()).placeholder(R.mipmap.ic_person_default).into(holder.mItemImage);
        holder.mItemName.setText(mMemListBeen.get(position).getNickname());
        holder.mLlLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClick.onItemClickListen(mMemListBeen.get(position).getMemid(),position,mMemListBeen.get(position).getCurMemrole());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMemListBeen.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_image)
        ImageView mItemImage;
        @Bind(R.id.item_name)
        TextView mItemName;
        @Bind(R.id.ll_layout)
        LinearLayout mLlLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClick{
        void onItemClickListen(String memid,int pos,String memrole);
    }
}
