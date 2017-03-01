package com.maxiaobu.healthclub.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.beangson.BeanMgettrainingitem;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 马小布 on 2016/8/31.
 * 数据录入左侧列表
 */
public class AdapterDataEntryLeftListAty extends RecyclerView.Adapter {
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    private Context mContext;
    private List<BeanMgettrainingitem.ListBean.ItemsBean> mData;
    private int selectPosition = 0;

    public AdapterDataEntryLeftListAty(Context context, List<BeanMgettrainingitem.ListBean.ItemsBean> mData) {
        mContext = context;
        this.mData = mData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_data_entry_left, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MyViewHolder viewHolder = (MyViewHolder) holder;
        if (selectPosition == position){
            viewHolder.mLyRoot.setBackgroundColor(mContext.getResources().getColor(R.color.gray262626));
        }else {
            viewHolder.mLyRoot.setBackgroundColor(mContext.getResources().getColor(R.color.gray444444));
        }
        BeanMgettrainingitem.ListBean.ItemsBean bean = mData.get(position);
        Glide.with(mContext).load(bean.getImgsfilename()).into(viewHolder.mImageView);
        viewHolder.mTextView.setText(bean.getItemname());

        viewHolder.mLyRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    selectPosition = position;
                    mListener.onItemClick(view, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.image_view)
        ImageView mImageView;
        @Bind(R.id.text_view)
        TextView mTextView;
        @Bind(R.id.ly_root)
        LinearLayout mLyRoot;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
