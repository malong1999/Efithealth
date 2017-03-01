package com.maxiaobu.healthclub.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.beangson.BeanGoodsList;
import com.maxiaobu.healthclub.ui.weiget.TouchHighlightImageButton;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 马小布 on 2016/9/10.
 * 没用
 */
public class ClubListAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<BeanGoodsList.ListBean> mData;

    public ClubListAdapter(Context context, List<BeanGoodsList.ListBean> mData) {
        mContext = context;
        this.mData = mData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_lunch_list, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_goods)
        TouchHighlightImageButton mIvGoods;
        @Bind(R.id.tv_title)
        TextView mTvTitle;
        @Bind(R.id.iv_icon)
        ImageView mIvIcon;
        @Bind(R.id.tv_food_type)
        TextView mTvFoodType;
        @Bind(R.id.tv_energy)
        TextView mTvEnergy;
        @Bind(R.id.tv_protein)
        TextView mTvProtein;
        @Bind(R.id.tv_time)
        TextView mTvTime;
        @Bind(R.id.linearLayout)
        LinearLayout mLinearLayout;
        @Bind(R.id.imageView2)
        ImageView mImageView2;
        @Bind(R.id.tv_price)
        TextView mTvPrice;
        @Bind(R.id.mrlayout)
        MaterialRippleLayout mMrlayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
