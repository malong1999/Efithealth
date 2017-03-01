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
import com.maxiaobu.healthclub.common.beangson.BeanClubList;
import com.maxiaobu.healthclub.ui.weiget.GlideCircleTransform;
import com.maxiaobu.healthclub.ui.weiget.NoDoubleClickListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 马小布 on 2016/8/31.
 * 没有用
 */
public class AdapterScanCardListAty extends RecyclerView.Adapter {
    public interface OnItemClickListener {
        void onItemClick(View view, String tarid);
    }
    public OnItemClickListener mListener;
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener =listener;
    }
    private Context mContext;
    private List<BeanClubList.ClubListBean>  mData;
    public AdapterScanCardListAty(Context context, List<BeanClubList.ClubListBean>  mData) {
        mContext = context;
        this.mData = mData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_club_list_aty, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MyViewHolder viewHolder = (MyViewHolder) holder;
        final BeanClubList.ClubListBean bean = mData.get(position);
        Glide.with(mContext).load(bean.getImgsfilename()).transform(new GlideCircleTransform(mContext)).placeholder(R.mipmap.ic_place_holder).into(viewHolder.mIvHead);
        viewHolder.mTvName.setText(bean.getClubname());
        viewHolder.mTvDistance.setText("距您" + bean.getDistancestr());
        viewHolder.mRbGoods.setRating(Float.parseFloat(bean.getEvascore()));
        viewHolder.mTvStar.setText("("+bean.getEvascore()+")");
        viewHolder.mTvContent.setText(bean.getAddress());
        viewHolder.mLyRoot.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                if (mListener!=null){
                    mListener.onItemClick(viewHolder.mIvHead,bean.getMemid());//mData.get(position).getMemid()
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
        @Bind(R.id.rb_goods)
        RatingBar mRbGoods;
        @Bind(R.id.tv_star)
        TextView mTvStar;
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
