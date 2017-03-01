package com.maxiaobu.healthclub.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.beangson.BeanInviteGroup;
import com.maxiaobu.healthclub.ui.activity.GroupDetailsActivity;
import com.maxiaobu.healthclub.ui.activity.InviteGroupActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 莫小婷 on 2016/12/9.
 * 申请入群
 */
public class AdapterInviteGroup extends RecyclerView.Adapter<AdapterInviteGroup.ViewHolder> {
    private Activity mContext;
    private List<BeanInviteGroup.GroupListBean> mGroupListBeen;
    public IntoListener mIntoListener;
    public interface IntoListener {
        void into(int position,String groupIid,String Imid);
    }
    public void setEnterListener(IntoListener intoListener) {
        mIntoListener = intoListener;
    }

    public AdapterInviteGroup(List<BeanInviteGroup.GroupListBean> groupListBeen, Activity context) {
        mGroupListBeen = groupListBeen;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.em_row_group, parent, false);
        ViewHolder holder = new ViewHolder(convertView);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mName.setText(mGroupListBeen.get(position).getGname());
        Glide.with(mContext).load(mGroupListBeen.get(position).getImgsfilename()).placeholder(R.mipmap.ic_place_holder).into(holder.mAvatar);
        holder.mSingle.setText(mGroupListBeen.get(position).getSummary());
        if (mGroupListBeen.get(position).getGtype().equals("0")) {
            holder.mClassifyText.setVisibility(View.GONE);
        } else {
            holder.mClassifyText.setVisibility(View.VISIBLE);
            holder.mClassifyText.setText(mGroupListBeen.get(position).getDistancestr());
        }
        holder.mRlLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIntoListener != null)
                    mIntoListener.into(position,mGroupListBeen.get(position).getGroupid(),mGroupListBeen.get(position).getImid());
//                Intent intent = new Intent(mContext,GroupDetailsActivity.class);
//                intent.putExtra("groupIid",mGroupListBeen.get(position).getGroupid());
//                intent.putExtra("groupId",mGroupListBeen.get(position).getImid());
//                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mGroupListBeen.size() ;
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        @Bind(R.id.avatar)
        ImageView mAvatar;
        @Bind(R.id.name)
        TextView mName;
        @Bind(R.id.single)
        TextView mSingle;
        @Bind(R.id.classify_text)
        TextView mClassifyText;
        @Bind(R.id.rl_layout)
        RelativeLayout mRlLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }




}
