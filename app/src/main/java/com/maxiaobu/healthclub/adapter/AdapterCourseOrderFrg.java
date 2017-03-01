package com.maxiaobu.healthclub.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.common.beangson.BeanCorderList;
import com.maxiaobu.healthclub.listener.OnAgainItemClickListener;
import com.maxiaobu.healthclub.listener.OnCancelItemClickListener;
import com.maxiaobu.healthclub.listener.OnDelayItemClickListener;
import com.maxiaobu.healthclub.listener.OnImageItemClickListener;
import com.maxiaobu.healthclub.listener.OnOrderRightItemClickListener;
import com.maxiaobu.healthclub.ui.activity.GcourseActivity;
import com.maxiaobu.healthclub.ui.activity.MyBespeakActivity;
import com.maxiaobu.healthclub.ui.activity.PayActivity;
import com.maxiaobu.healthclub.ui.activity.PersionalActivity;
import com.maxiaobu.healthclub.ui.activity.ReservationActivity;
import com.maxiaobu.healthclub.utils.TimesUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 马小布 on 2016/8/25.
 * 课程订单
 */
public class AdapterCourseOrderFrg extends RecyclerView.Adapter {
    public OnCancelItemClickListener mListener;
    public OnDelayItemClickListener mDelayListener;
    public OnAgainItemClickListener mAgainListener;
    public OnImageItemClickListener mImageListener;
    public OnOrderRightItemClickListener mRightListener;

    /**
     * 取消订单
     * @param listener
     */
    public void setOnCancelItemClickListener(OnCancelItemClickListener listener) {
        mListener = listener;
    }
    public void setOnDelayItemClickListener(OnDelayItemClickListener listener) {
        mDelayListener = listener;
    }

    /**
     * 再来一单
     * @param listener
     */
    public void setOnAgainItemClickListener(OnAgainItemClickListener listener) {
        mAgainListener = listener;
    }

    /**
     * 图片被点击
     * @param listener
     */
    public void setOnImageItemClickListener(OnImageItemClickListener listener) {
        mImageListener = listener;
    }
    public void setOnOrderRightItemClickListener(OnOrderRightItemClickListener listener) {
        mRightListener = listener;
    }


    private Activity mActivity;
    private List<BeanCorderList.CorderListBean> mData;

    public AdapterCourseOrderFrg(Activity activity, List<BeanCorderList.CorderListBean> mData) {
        mActivity = activity;
        this.mData = mData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_course_order_list_frg, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder viewHolder = (MyViewHolder) holder;
        final BeanCorderList.CorderListBean listBean = mData.get(position);
        //0 代付款；1 待收货；2已完成
        Glide.with(mActivity).load(listBean.getCa_imgsfilename()).placeholder(R.mipmap.ic_place_holder).into(viewHolder.mIvPhoto);
        viewHolder.mTvTitle.setText(listBean.getCa_nickname());

        viewHolder.mTvCourseName.setText(listBean.getCoursename());
        viewHolder.mTvClubName.setText(listBean.getClubname());
        viewHolder.mIvPhoto.setOnClickListener(v -> {
            if (mImageListener!=null)
                mImageListener.onItemClick(v,listBean.getCoachid());
        });

        if (listBean.getPaystatus().equals("0")) {
            viewHolder.mTvComplete.setText("待付款");
            viewHolder.mTvAppointment.setVisibility(View.GONE);
            viewHolder.mLyNopay.setVisibility(View.VISIBLE);
            viewHolder.mTvRight.setText("继续支付");
            viewHolder.mTvLeft.setText("删除");
            viewHolder.mTvRight.setOnClickListener(view -> {
                Intent intent = new Intent(new Intent(mActivity, PayActivity.class));
                intent.putExtra(Constant.PAY_TYPE, "course");
                intent.putExtra("totlePrice", String.valueOf(mData.get(position).getOrdamt()));
                intent.putExtra("ordno", String.valueOf(mData.get(position).getOrdno()));
                String page = "file:///android_asset/reservation.html?coachid=" + listBean.getCoachid() + " &nickname=" +
                        listBean.getCa_nickname() + "&clubname=" + listBean.getClubname()
                        + "&address=" + listBean.getAddress();
                page += "&coursename=" + listBean.getCoursename() + "&enddate=" +
                        TimesUtil.timestampToStringS(String.valueOf(listBean.getOrdenddate().getTime()), "yyyy/MM/dd")
                        + "&times=" +(Integer.parseInt(listBean.getOrdcoursetimes()) - Integer.parseInt(listBean.getCoursenum()))+ "&orderid=" + listBean.getOrdno();
                page += "&imgsfile=" + listBean.getCa_imgsfilename();
                intent.putExtra("reservation", page.trim());
//                                Log.d("OrderConfirmActivity", object.getOrdno().toString());
                mActivity.startActivity(intent);
            });
            viewHolder.mTvLeft.setOnClickListener(view -> {
                if (mListener != null) {
                    mListener.onItemClick(view, String.valueOf(mData.get(position).getOrdno()));
                }
            });
        } else if (listBean.getOrdstatus().equals("1")) {
            viewHolder.mTvComplete.setText("已完成");
            viewHolder.mTvAppointment.setVisibility(View.GONE);
            viewHolder.mLyNopay.setVisibility(View.VISIBLE);
            viewHolder.mTvRight.setText("再来一单");
            viewHolder.mTvLeft.setText("删除");
            viewHolder.mTvRight.setOnClickListener(view -> mAgainListener.onItemClick(view, mData.get(position)));
            viewHolder.mTvLeft.setOnClickListener(view -> {
                if (mListener != null) {
                    mListener.onItemClick(view, String.valueOf(mData.get(position).getOrdno()));
                }
            });

        } else if (listBean.getBespeaknum().equals(listBean.getCoursenum())) {
            viewHolder.mTvComplete.setText("待预约");
            viewHolder.mTvAppointment.setVisibility(View.VISIBLE);
            viewHolder.mLyNopay.setVisibility(View.GONE);
            viewHolder.mTvAppointment.setText("现在预约");
            //       String page = "file:///android_asset/reservation.html?+ "&nickname=" + corderList.getCa_nickname() + "&clubname=" + corderList.getClubname() + "&address=" + corderList.getAddress();
//        page += "&coursename=" +corderList.getCoursename() + "&enddate=" + formatdate(corderList.getOrdenddate().getTime()) + "&times=" + corderList.getOrdcoursetimes()+ "&orderid=" + corderList.getOrdno();
//        page +="&imgsfile="+corderList.getCa_imgsfilename();
            viewHolder.mTvAppointment.setOnClickListener(v -> {
                Intent intent = new Intent();
                if ("gclub".equals(listBean.getOrdtype()) || "gcoach".equals(listBean.getOrdtype())) {
                    intent.setClass(mActivity, GcourseActivity.class);
                    String page = "gcourse.html?gcalid=" + listBean.getCourseid() + "&conphone=13400000000";
                    intent.putExtra("page", page);
                    Log.d("AdapterCourseOrderFrg", page);
                    mActivity.startActivity(intent);
                } else {
                    String page = "file:///android_asset/reservation.html?coachid=" + listBean.getCoachid() + " &nickname=" +
                            listBean.getCa_nickname() + "&clubname=" + listBean.getClubname()
                            + "&address=" + listBean.getAddress();
                    page += "&coursename=" + listBean.getCoursename() + "&enddate=" +
                            TimesUtil.timestampToStringS(String.valueOf(listBean.getOrdenddate().getTime()), "yyyy/MM/dd")
                            + "&times=" + listBean.getOrdcoursetimes() + "&orderid=" + listBean.getOrdno();
                    page += "&imgsfile=" + listBean.getCa_imgsfilename();
                    Log.d("AdapterCourseOrderFrg", page);
//                    Log.d("AdapterCourseOrderFrg", page);
                    intent.putExtra("reservation", page.trim());
                    intent.setClass(mActivity, ReservationActivity.class);
                    mActivity.startActivity(intent);
                }

            });
//            corder.querySelector("#orderExe1").href = "javascript:commitExe('bespeak','" + corderData.ordno + "','" + corderData.coachid + "')";
        } else if (Integer.parseInt(listBean.getBespeaknum()) > Integer.parseInt(listBean.getCoursenum())) {
            viewHolder.mTvComplete.setText("已预约");
            viewHolder.mTvAppointment.setVisibility(View.VISIBLE);
            viewHolder.mLyNopay.setVisibility(View.GONE);
            viewHolder.mTvAppointment.setText("查看预约");
            viewHolder.mTvAppointment.setOnClickListener(v -> mActivity.startActivity(new Intent(mActivity, MyBespeakActivity.class)));
        }

        if (listBean.getPaystatus().equals("0")) {
//            viewHolder.mTvResidueTimes.setText("剩余" + listBean.getOrdcoursetimes() + "次");
            viewHolder.mTvResidueTimes.setText(listBean.getCoursedays() + "天/" + listBean.getOrdcoursetimes() + "次");
        } else if (listBean.getOrdstatus().equals("1")) {
            viewHolder.mTvEndTime.setText("截至日期" +
                    TimesUtil.timestampToStringS(
                            String.valueOf(listBean.getOrdenddate().getTime()), "yyyy/MM/dd"));
            viewHolder.mTvResidueTimes.setText(listBean.getCoursedays() + "天/" + listBean.getOrdcoursetimes() + "次");
        } else {
            int residue = Integer.parseInt(listBean.getOrdcoursetimes()) - Integer.parseInt(listBean.getCoursenum());
            viewHolder.mTvResidueTimes.setText("剩余" + residue + "次");
            viewHolder.mTvEndTime.setText("截至日期" +
                    TimesUtil.timestampToStringS(
                            String.valueOf(listBean.getOrdenddate().getTime()), "yyyy/MM/dd"));
        }
        if (listBean.getPaystatus().equals("2")) {
            viewHolder.mTvPrice.setText("凭会员卡免费预约");
        } else {
            viewHolder.mTvPrice.setText("共计：" + listBean.getOrdamt() + "元");
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_photo)
        ImageView mIvPhoto;
        @Bind(R.id.tv_content)
        TextView mTvContent;
        @Bind(R.id.tv_title)
        TextView mTvTitle;
        @Bind(R.id.tv_complete)
        TextView mTvComplete;
        @Bind(R.id.tv_price)
        TextView mTvPrice;
        @Bind(R.id.tv_course_name)
        TextView mTvCourseName;
        @Bind(R.id.tv_club_name)
        TextView mTvClubName;
        @Bind(R.id.tv_end_time)
        TextView mTvEndTime;
        @Bind(R.id.tv_residue_times)
        TextView mTvResidueTimes;
        @Bind(R.id.tv_appointment)
        Button mTvAppointment;
        @Bind(R.id.tv_right)
        TextView mTvRight;
        @Bind(R.id.tv_left)
        TextView mTvLeft;
        @Bind(R.id.ly_nopay)
        LinearLayout mLyNopay;
        @Bind(R.id.ly_root)
        LinearLayout mLyRoot;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
