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
import com.maxiaobu.healthclub.common.beangson.BeanMineStudent;
import com.maxiaobu.healthclub.ui.weiget.GlideCircleTransform;
import com.maxiaobu.healthclub.ui.weiget.NoDoubleClickListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/12.
 * 学员列表
 */
public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.ViewHolder> {
    private List<BeanMineStudent.CoachmemberlistBean> mList;
    private Context mContext;
    private OnItemClickListener mListener;
    public void setListener(OnItemClickListener listener) {
        mListener = listener;
    }
    public StudentListAdapter(Context context) {
        mContext = context;
    }
    public void setList(List<BeanMineStudent.CoachmemberlistBean> list) {
        mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_rv_student_list_aty, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final BeanMineStudent.CoachmemberlistBean mbean = mList.get(position);
//        Log.d("StudentListAdapter", "position:" + position);
        Glide.with(mContext).load(mbean.getImgsfilename())
                .transform(new GlideCircleTransform(mContext))
                .placeholder(R.mipmap.ic_place_holder)
                .into(holder.mIvStudentImage);
        holder.mTvStudentName.setText(mbean.getNickname());
        holder.mTvStudentSign.setText(mbean.getSignature());
        holder.mMrlayout.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                mListener.onItemClick(v, mbean.getMemrole(), mbean.getMemid());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.iv_student_image)
        ImageView mIvStudentImage;
        @Bind(R.id.tv_student_name)
        TextView mTvStudentName;
        @Bind(R.id.tv_student_sign)
        TextView mTvStudentSign;
        @Bind(R.id.mrlayout)
        MaterialRippleLayout mMrlayout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    public interface OnItemClickListener {
        void onItemClick(View view, String memrole, String memid);
    }

}
