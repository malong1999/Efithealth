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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanMcoachBespeak;
import com.maxiaobu.healthclub.common.beangson.BeanMmyBespeak;
import com.maxiaobu.healthclub.dao.DataEntryDbHelper;
import com.maxiaobu.healthclub.ui.activity.DataEntryActivity;
import com.maxiaobu.healthclub.ui.activity.PersionalActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 马小布 on 2016/9/10.
 * 教学预约
 */
public class AdapterTeachingAppointment extends RecyclerView.Adapter {
    public interface OnItemClickListener {
        //status 0无数据1 有数据
        void onItemClick(View view, BeanMcoachBespeak.CoachBespeaklistBean bean);
    }
    public OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    private Activity mActivity;
    private List<BeanMcoachBespeak.CoachBespeaklistBean> mData;
    public AdapterTeachingAppointment(Activity activity, List<BeanMcoachBespeak.CoachBespeaklistBean> mData) {
        mActivity = activity;
        this.mData = mData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_teaching_appointment_aty, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder viewHolder = (MyViewHolder) holder;
        final BeanMcoachBespeak.CoachBespeaklistBean bean = mData.get(position);
        //0 代付款；1 待收货；2已完成
        Glide.with(mActivity).load(bean.getImgsfile()).placeholder(R.mipmap.ic_place_holder).into(viewHolder.mIvPhoto);
        viewHolder.mTvName.setText(bean.getNickname());
        viewHolder.mTvOccupation.setText("学员");
        viewHolder.mTvCourse.setText(bean.getCoursename());
        viewHolder.mTvTime.setText(bean.getBegintime());
        viewHolder.mTvAddress.setText(bean.getClubname());
        viewHolder.mIvPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mActivity, PersionalActivity.class);
                intent.putExtra("memrole",bean.getMemrole());
                intent.putExtra("tarid",bean.getMemid());
                mActivity.startActivity(intent);
            }
        });
        if (bean.getCoursestatus().equals("0")) {
            viewHolder.mTvEvaluate.setText("确认上课");
            viewHolder.mTvEvaluate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener!=null){
                        mListener.onItemClick(v,bean);
                    }
                }
            });
        } else {
            viewHolder.mTvEvaluate.setVisibility(View.INVISIBLE);
        }
    }
    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_photo)
        ImageView mIvPhoto;
        @Bind(R.id.tv_name)
        TextView mTvName;
        @Bind(R.id.tv_occupation)
        TextView mTvOccupation;
        @Bind(R.id.tv_course)
        TextView mTvCourse;
        @Bind(R.id.tv_time)
        TextView mTvTime;
        @Bind(R.id.tv_address)
        TextView mTvAddress;
        @Bind(R.id.tv_evaluate)
        TextView mTvEvaluate;
        @Bind(R.id.ly_root)
        LinearLayout mLyRoot;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
