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
import com.maxiaobu.healthclub.common.beangson.BeanNearList;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 莫小婷 on 2016/12/19.
 * 附近的人
 */
public class AdapterNearList extends RecyclerView.Adapter<AdapterNearList.ViewHolder> {
    private Context mContext;
    private List<BeanNearList.CoachListBean> mList;
    private OnClickItem mOnClickItem;
    public void setOnClickItem(OnClickItem onClickItem) {
        mOnClickItem = onClickItem;
    }
    public AdapterNearList(Context context, List<BeanNearList.CoachListBean> list) {
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
        Glide.with(mContext).load(mList.get(position).getImgsfilename()).placeholder(R.mipmap.ic_place_holder).into(holder.mItemImage);
        holder.mItemName.setText(mList.get(position).getNickname());
        holder.mItemDistance.setText("距您" + mList.get(position).getDistancestr());
        if(mList.get(position).getSignature().equals("") || mList.get(position).getSignature() == null){
            holder.mItemSingle.setText("暂无签名");
        }else {
            holder.mItemSingle.setText(mList.get(position).getSignature());
        }
        holder.mLlLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClickItem.onClickItemListen(mList.get(position).getMemid(), mList.get(position).getCurMemrole());
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
