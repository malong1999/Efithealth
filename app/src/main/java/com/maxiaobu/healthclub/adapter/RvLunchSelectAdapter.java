package com.maxiaobu.healthclub.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.maxiaobu.healthclub.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 马小布 on 2016/12/22.
 * introduction: 真他娘的不知道说点啥
 * email:maxiaobu1999@163.com
 * 功能：列表筛选适配器
 * 伪码：
 * 待完成：
 */
public class RvLunchSelectAdapter extends RecyclerView.Adapter {
    public interface LunchSelectItemClickListener {
        void onItemClick(View view, int postion);
    }

    public LunchSelectItemClickListener mListener;

    public void setLunchSelectItemClickListener(LunchSelectItemClickListener listener) {
        mListener = listener;
    }

    private Context mContext;
    private int[] menuIcons;
    private String[] menuTitle;

    public RvLunchSelectAdapter(Context context, int[] menuIcons, String[] menuTitle) {
        mContext = context;
        this.menuIcons = menuIcons;
        this.menuTitle = menuTitle;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_lunch_select, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        MyViewHolder viewHolder = (MyViewHolder) holder;
        if (position == menuTitle.length - 1) {
            viewHolder.mView1.setVisibility(View.GONE);
            viewHolder.mView2.setVisibility(View.GONE);
            viewHolder.mView3.setVisibility(View.GONE);
        }
        viewHolder.mTextView.setText(menuTitle[position]);
        viewHolder.mImageView.setImageResource(menuIcons[position]);
        viewHolder.mMrLayout.setOnClickListener(view -> {
            if (mListener != null) {
                mListener.onItemClick(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuIcons.length;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.image_view)
        ImageView mImageView;
        @Bind(R.id.text_view)
        TextView mTextView;
        @Bind(R.id.mr_layout)
        LinearLayout mMrLayout;
        @Bind(R.id.v_view1)
        View mView1;
        @Bind(R.id.v_view2)
        View mView2;
        @Bind(R.id.v_view3)
        View mView3;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
