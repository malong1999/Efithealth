package com.maxiaobu.healthclub.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.beangson.BeanMcourseList;
import com.maxiaobu.healthclub.common.beangson.BeanMunbindList;
import com.maxiaobu.healthclub.ui.weiget.GlideCircleTransform;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 马小布 on 2016/8/31.
 * 教练历史课程
 */
public class AdapterCoachesHistoryListfrg extends RecyclerView.Adapter {
    public interface OnItemClickListener {
        void onItemClick(View view, BeanMcourseList.CourseListBean courseListBean);
    }

    public OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    private Context mContext;
    private List<BeanMcourseList.CourseListBean> mData;

    public AdapterCoachesHistoryListfrg(Context context, List<BeanMcourseList.CourseListBean> mData) {
        mContext = context;
        this.mData = mData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_coaches_history_list_frg, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MyViewHolder viewHolder = (MyViewHolder) holder;
        final BeanMcourseList.CourseListBean courseListBean = mData.get(position);
        Glide.with(mContext).load(courseListBean.getImgsfile()).placeholder(R.mipmap.ic_place_holder).into(viewHolder.mIvHead);
        viewHolder.mTvName.setText(courseListBean.getPcoursename());
        viewHolder.mTvDistance.setText(courseListBean.getPcourseprice()+ "元");
        viewHolder.mTvAddress.setText(courseListBean.getPcoursetimes()+"次/"+courseListBean.getPcoursedays()+"天");
        viewHolder.mTvContent.setText(courseListBean.getClubname());
        viewHolder.mLyRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onItemClick(viewHolder.mIvHead, courseListBean );//mData.get(position).getMemid()
                }
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
        @Bind(R.id.tv_name)
        TextView mTvName;
        @Bind(R.id.tv_distance)
        TextView mTvDistance;
        @Bind(R.id.tv_address)
        TextView mTvAddress;
        @Bind(R.id.tv_content)
        TextView mTvContent;
        @Bind(R.id.ly_root)
        MaterialRippleLayout mLyRoot;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
