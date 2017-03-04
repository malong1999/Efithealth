package com.maxiaobu.healthclub.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.maxiaobu.healthclub.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by maqinglong on 2017/3/4.
 */

public class AdapterGoodsPayAty extends RecyclerView.Adapter {


    private Context mContext;
    private ArrayList<Object> mData;

    public AdapterGoodsPayAty(Context context, ArrayList<Object> mData) {
        mContext = context;
        this.mData = mData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if (viewType == 1) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_goods_pay_list_aty, parent, false);
            return new ListViewHolder(v);
        } else if (viewType == 2) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_goods_pay_price_aty, parent, false);
            return new PriceViewHolder(v);
        } else  {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_goods_pay_way_aty, parent, false);
            return new WayViewHolder(v);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //商品列表
        if (holder instanceof ListViewHolder) {
            ListViewHolder listViewHolder= (ListViewHolder) holder;
            listViewHolder.mIvAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int num =Integer.parseInt(String.valueOf(listViewHolder.mTvFoodNum.getText()))+1;
                    listViewHolder.mTvFoodNum.setText(num+"");
                }
            });
            listViewHolder.mIvReduce.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int num =Integer.parseInt(String.valueOf(listViewHolder.mTvFoodNum.getText()))-1;
                    listViewHolder.mTvFoodNum.setText(num+"");
                }
            });

        } else if (holder instanceof PriceViewHolder) {
            //价格

        } else if (holder instanceof WayViewHolder) {
            //支付方式
        }


    }

    @Override
    public int getItemCount() {
        return 4;
    }

    @Override
    public int getItemViewType(int position) {
        if (position <= 1) {
            return 1;
        } else if (position == 2) {
            return 2;
        } else {
            return 3;
        }
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_title)
        TextView mTvTitle;
        @Bind(R.id.tv_club)
        TextView mTvClub;
        @Bind(R.id.iv_add)
        ImageView mIvAdd;
        @Bind(R.id.tv_food_num)
        TextView mTvFoodNum;
        @Bind(R.id.iv_reduce)
        ImageView mIvReduce;

        public ListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class PriceViewHolder extends RecyclerView.ViewHolder {


        public PriceViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class WayViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_arrow)
        ImageView mIvArrow;

        public WayViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
