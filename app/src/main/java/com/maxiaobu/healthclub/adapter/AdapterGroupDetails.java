package com.maxiaobu.healthclub.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.beangson.BeanGroupDetails;
import com.maxiaobu.healthclub.common.beangson.BeanGroupList;

import java.io.PipedOutputStream;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 莫小婷 on 2016/12/6.
 * 群详情
 */

public class AdapterGroupDetails extends BaseAdapter {
    private List<BeanGroupDetails.GroupBean.MemListBean> mMemListBean;
    private Context mContext;

    public AdapterGroupDetails(Context context, List<BeanGroupDetails.GroupBean.MemListBean> memListBean) {
        mContext = context;
        mMemListBean = memListBean;
    }

    @Override
    public int getCount() {
        return mMemListBean.size() > 8 ? 8 : mMemListBean.size();
    }

    @Override
    public Object getItem(int position) {
        return mMemListBean.get(position) == null ? null : mMemListBean.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_gv_group_member, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(mContext).load(mMemListBean.get(position).getImgsfilename()).placeholder(R.mipmap.ic_person_default).into(holder.mMemberImage);
        holder.mMemberName.setText(mMemListBean.get(position).getNickname());
        return convertView;
    }
    class ViewHolder {
        @Bind(R.id.member_image)
        ImageView mMemberImage;
        @Bind(R.id.member_name)
        TextView mMemberName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
