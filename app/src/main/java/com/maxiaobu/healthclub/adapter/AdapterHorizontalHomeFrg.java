package com.maxiaobu.healthclub.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.listener.MqlOnItemClickListener;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 马小布 on 2016/12/19：17:30.
 * email：maxiaobu1216@gmail.com
 * 功能：主页面横向列表  教练列表  配餐列表  跳转发现页面
 * 伪码：
 * 待完成：
 */
public class AdapterHorizontalHomeFrg extends RecyclerView.Adapter {
    MqlOnItemClickListener mListener;


    public void setOnItemClickListener(MqlOnItemClickListener listener) {
        mListener = listener;
    }

    private Context mContext;
//    private List<BeanClubList.ClubListBean> mData;

    public AdapterHorizontalHomeFrg(Context context) {
        mContext = context;
//        this.mData = mData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_horizontal_home_frg, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder viewHolder = (MyViewHolder) holder;
        switch (position) {
            case 0:
                //教练列表
                viewHolder.mImageView.setImageResource(R.mipmap.ic_home_coach);
                break;
            case 1:
                //配餐列表
                viewHolder.mImageView.setImageResource(R.mipmap.ic_home_catering);
                break;
            case 2:
                //跳转发现页面
                viewHolder.mImageView.setImageResource(R.mipmap.ic_home_find);
                break;
            default:
                break;
        }
        viewHolder.mImageView.setOnClickListener(view -> {
            if (mListener != null)
                mListener.onItemClick(view, position, null);
        });
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.image_view)
        ImageView mImageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
