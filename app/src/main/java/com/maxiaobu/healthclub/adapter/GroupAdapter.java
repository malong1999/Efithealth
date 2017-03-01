package com.maxiaobu.healthclub.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.beangson.BeanGroupList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/3.
 * 群列表
 */
public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private BeanGroupList grouplist;
    private Context mContext;

    public interface EnterListener {
        void enter(int position);
    }

    public EnterListener mEnterListener;

    public void setEnterListener(EnterListener enterListener) {
        mEnterListener = enterListener;
    }

    public GroupAdapter(Context context, BeanGroupList grouplist) {
        this.inflater = LayoutInflater.from(context);
        mContext = context;
        this.grouplist = grouplist;
    }

    public void setGrouplist(BeanGroupList grouplist) {
        this.grouplist = grouplist;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.em_row_group, parent, false);
        ViewHolder holder = new ViewHolder(convertView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mName.setText(grouplist.getGroupList().get(position).getGname());
        Glide.with(mContext).load(grouplist.getGroupList().get(position).getImgsfilename()).placeholder(R.mipmap.ic_place_holder).into(holder.mAvatar);
        holder.mSingle.setText(grouplist.getGroupList().get(position).getSummary());
        if (grouplist.getGroupList().get(position).getGtype().equals("0")) {
            holder.mClassifyText.setVisibility(View.GONE);
        } else {
            holder.mClassifyText.setVisibility(View.VISIBLE);
        }
        holder.mRlLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEnterListener != null)
                    mEnterListener.enter(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return grouplist.getGroupList().size() == 0 ? 0 : grouplist.getGroupList().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.avatar)
        ImageView mAvatar;
        @Bind(R.id.name)
        TextView mName;
        @Bind(R.id.single)
        TextView mSingle;
        @Bind(R.id.rl_layout)
        RelativeLayout mRlLayout;
        @Bind(R.id.classify_text)
        TextView mClassifyText;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
