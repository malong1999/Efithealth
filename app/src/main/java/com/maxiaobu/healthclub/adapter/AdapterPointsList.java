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
import com.maxiaobu.healthclub.common.beangson.BeanDynamicCommentDetail;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 莫小婷 on 2017/2/15.
 * 点赞列表
 */

public class AdapterPointsList extends RecyclerView.Adapter<AdapterPointsList.ViewHolder> {
    private Context mContext;
    private List<BeanDynamicCommentDetail.PostBean.LikelistBean> mList;
    private OnClickItem mOnClickItem;

    public void setOnClickItem(OnClickItem onClickItem) {
        mOnClickItem = onClickItem;
    }

    public AdapterPointsList(Context context, List<BeanDynamicCommentDetail.PostBean.LikelistBean> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_near_list, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(mContext).load(mList.get(position).getAuthimgsfilename()).placeholder(R.mipmap.ic_place_holder).into(holder.mItemImage);
        holder.mItemName.setText(mList.get(position).getAuthnickname());
//        holder.mItemDistance.setText("距您" + mList.get(position).getDistancestr());
//        if(mList.get(position).getSignature().equals("") || mList.get(position).getSignature() == null){
//            holder.mItemSingle.setText("暂无签名");
//        }else {
//            holder.mItemSingle.setText(mList.get(position).getSignature());
//        }
        holder.mItemSingle.setVisibility(View.GONE);

        holder.mLlLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClickItem.onClickItemListen(mList.get(position).getMemid(), mList.get(position).getMemrole());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.item_image)
        ImageView mItemImage;
        @Bind(R.id.item_name)
        TextView mItemName;
        @Bind(R.id.item_distance)
        TextView mItemDistance;
        @Bind(R.id.ll_layout)
        RelativeLayout mLlLayout;
        @Bind(R.id.item_single)
        TextView mItemSingle;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnClickItem {
        void onClickItemListen(String memid, String memrole);
    }
}

