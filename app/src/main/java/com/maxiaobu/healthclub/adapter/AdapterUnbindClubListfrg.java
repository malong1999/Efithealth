package com.maxiaobu.healthclub.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanCoachesListAty;
import com.maxiaobu.healthclub.common.beangson.BeanMunbindList;
import com.maxiaobu.healthclub.ui.weiget.GlideCircleTransform;
import com.maxiaobu.healthclub.ui.weiget.NoDoubleClickListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.internal.DebouncingOnClickListener;

/**
 * Created by 马小布 on 2016/8/31.
 * 未绑定俱乐部
 */
public class AdapterUnbindClubListfrg extends RecyclerView.Adapter {
    public interface OnItemClickListener {
        void onItemClick(View view, String clubId);
    }
    public OnItemClickListener mListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    private Context mContext;
    private List<BeanMunbindList.UnbindListBean> mData;
    public AdapterUnbindClubListfrg(Context context,List<BeanMunbindList.UnbindListBean> mData) {
        mContext = context;
        this.mData = mData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_bind_club_list_frg, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MyViewHolder viewHolder = (MyViewHolder) holder;
        final BeanMunbindList.UnbindListBean unbindListBean = mData.get(position);
        Glide.with(mContext).load(unbindListBean.getClubmemimgsfile()).transform(new GlideCircleTransform(mContext)).placeholder(R.mipmap.ic_place_holder).into(viewHolder.mIvHead);
        viewHolder.mTvName.setText(unbindListBean.getClubname());
        viewHolder.mTvDistance.setText("距您 " +unbindListBean.getDistance()+ "km");
        viewHolder.mTvAddress.setText(unbindListBean.getAddress());
        viewHolder.mTvContent.setText("场地参考价格:"+unbindListBean.getClubprice()+"元/次");
        viewHolder.mLyRoot.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                if (mListener != null) {
                    mListener.onItemClick(viewHolder.mIvHead, unbindListBean.getClubmemid());//mData.get(position).getMemid()
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
