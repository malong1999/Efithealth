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
import com.maxiaobu.healthclub.common.beangson.BeanMmyBespeak;
import com.maxiaobu.healthclub.ui.activity.ClubDetailActivity;
import com.maxiaobu.healthclub.ui.activity.EvaluateActivity;
import com.maxiaobu.healthclub.ui.activity.PersionalActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 马小布 on 2016/9/10.
 * 我的预约
 */
public class AdapterMyBespeak extends RecyclerView.Adapter {
    private Activity mActivity;
    private List<BeanMmyBespeak.BespeaklistBean> mData;

    public AdapterMyBespeak(Activity activity, List<BeanMmyBespeak.BespeaklistBean> mData) {
        mActivity = activity;
        this.mData = mData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_mybespeak_list_aty, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder viewHolder = (MyViewHolder) holder;
        final BeanMmyBespeak.BespeaklistBean bean = mData.get(position);
        if(bean.getMemid().equals("")&& bean.getOrdtype().equals("gclub")){
            viewHolder.mTvName.setText("驻场教练");
            viewHolder.mIvPhoto.setBackgroundResource(R.mipmap.ic_person_default);
        }else {
            Glide.with(mActivity).load(bean.getImgsfile()).placeholder(R.mipmap.ic_place_holder).into(viewHolder.mIvPhoto);
            viewHolder.mTvName.setText(bean.getNickname());
        }
        viewHolder.mTvOccupation.setText("教练");
        viewHolder.mTvCourse.setText(bean.getCoursename());
        viewHolder.mTvTime.setText(bean.getBegintime());
        viewHolder.mTvAddress.setText(bean.getClubname());
        viewHolder.mIvPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                if(bean.getMemid().equals("")&& bean.getOrdtype().equals("gclub")){
                    intent.setClass(mActivity,ClubDetailActivity.class);
                    intent.putExtra("tarid",bean.getClubmemid());
                }else {
                    intent.setClass(mActivity, PersionalActivity.class);
                    intent.putExtra("memrole","coach");
                    intent.putExtra("tarid",bean.getMemid());
                }
                mActivity.startActivity(intent);
            }
        });
        if (bean.getCoursestatus().equals("0")) {
            viewHolder.mTvEvaluate.setText("等待上课");
            viewHolder.mTvEvaluate.setBackgroundResource(R.drawable.bg_my_bespeak);
        } else {
            viewHolder.mTvEvaluate.setText("评价");
            viewHolder.mTvEvaluate.setBackgroundResource(R.drawable.bg_food_order_select);
            viewHolder.mTvEvaluate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //tarid=M000448&lessonid=L000142&nickname=教练_A2&
                    // imgsfile=http://efithealthresource.img-cn-beijing.aliyuncs.com/image/bmember/M000448_1469429236884_p.jpg@!BMEMBER_S&
                    // sign=我今天不开心&coursename=A2私教课程商品&begintime=2016-09-22 11:00&address=健身俱乐部CLUB_A
                    String s = "";
                    if(bean.getMemid().equals("")&& bean.getOrdtype().equals("gclub")){
                        s = "tarid=" + bean.getClubmemid() + "&lessonid=" + bean.getCorderlessonid() + "&nickname=" +
                                bean.getNickname() + "&imgsfile=" + bean.getImgsfilename() + "&sign=" +
                                bean.getSignature() + "&coursename=" + bean.getCoursename() + "&begintime=" + bean.getBegintime()
                                + "&address=" + bean.getClubname() + "&evascore=" + bean.getEvascore();

                    }else {
                        s = "tarid=" + bean.getMemid() + "&lessonid=" + bean.getCorderlessonid() + "&nickname=" +
                                bean.getNickname() + "&imgsfile=" + bean.getImgsfilename() + "&sign=" +
                                bean.getSignature() + "&coursename=" + bean.getCoursename() + "&begintime=" + bean.getBegintime()
                                + "&address=" + bean.getClubname() + "&evascore=" + bean.getEvascore();
                    }

                    Intent intent = new Intent(mActivity, EvaluateActivity.class);
                    intent.putExtra("url", s);
                    mActivity.startActivityForResult(intent, 1);
                }
            });
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
