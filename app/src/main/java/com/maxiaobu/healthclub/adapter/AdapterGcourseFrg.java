package com.maxiaobu.healthclub.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.beangson.BeanCoachesDetail;
import com.maxiaobu.healthclub.ui.activity.GcourseActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 马小布 on 2016/9/1.
 * 团操列表
 */
public class AdapterGcourseFrg extends RecyclerView.Adapter {
    private List<BeanCoachesDetail.GcourseListBean> mData;
    private Activity mActivity;

    public AdapterGcourseFrg(Activity activity, List<BeanCoachesDetail.GcourseListBean> data) {
        mActivity = activity;
        mData = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_gcourser_frg, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final BeanCoachesDetail.GcourseListBean bean = mData.get(position);
        MyViewHolder viewHolder = (MyViewHolder) holder;
        Glide.with(mActivity).load(bean.getImgpfilename()).placeholder(R.mipmap.ic_place_holder).into(viewHolder.mIvHead);
        viewHolder.mTvTitle.setText(bean.getGcalname());
        viewHolder.mTvNum.setText(bean.getGcoursetimes()+"次/"+bean.getGcoursedays()+"天");
        viewHolder.mTvClubName.setText(bean.getClubname());
        viewHolder.mTvAddress.setText(bean.getAddress());
        viewHolder.mTvPrice.setText(bean.getGcourseprice()+"元");
        viewHolder.mTvTime.setText("名额："+bean.getGcoursenum()+"人/时长："+bean.getGcourseminutes()+"分钟");
        viewHolder.mLyRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, GcourseActivity.class);
                //file:///android_asset/gcourse.html?gcalid=G000052&conphone=13400001111
                intent.putExtra("page","gcourse.html?gcalid="+bean.getGcalid()+"&conphone=13400000000");
                mActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_head)
        ImageView mIvHead;
        @Bind(R.id.tv_title)
        TextView mTvTitle;
        @Bind(R.id.tv_price)
        TextView mTvPrice;
        @Bind(R.id.tv_num)
        TextView mTvNum;
        @Bind(R.id.tv_time)
        TextView mTvTime;
        @Bind(R.id.tv_more_club)
        TextView mTvMoreClub;
        @Bind(R.id.tv_club_name)
        TextView mTvClubName;
        @Bind(R.id.tv_address)
        TextView mTvAddress;
        @Bind(R.id.ly_root)
        LinearLayout mLyRoot;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
