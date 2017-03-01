package com.maxiaobu.healthclub.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.beangson.BeanLoveList;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 莫小婷 on 2016/12/23.
 * 动态详情
 */
public class AdapterCommentDetailGridView extends BaseAdapter {
    private List<BeanLoveList> mList;
    private Context mContext;
    private onClickItem mOnClickItem;
    private onClickHeadImageItem mOnClickHeadImageItem;

    public void setOnClickHeadImageItem(onClickHeadImageItem onClickHeadImageItem) {
        mOnClickHeadImageItem = onClickHeadImageItem;
    }

    public void setOnClickItem(onClickItem onClickItem) {
        mOnClickItem = onClickItem;
    }

    public AdapterCommentDetailGridView(Context context) {
        mContext = context;
    }

    public void setList(List<BeanLoveList> list) {
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size() >= 8 ? 8 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_gv_dynamic_detail, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (parent.getChildCount() == 7) {
            viewHolder.mUserImage.setImageResource(R.drawable.bg_comment_user_image_list);
            viewHolder.mUserImage.setOnClickListener(v -> mOnClickItem.onClikc());
        } else {
//            Log.d("+++", "lalala" + position+"---"+parent.getChildCount());
            if(parent.getChildCount() == position){
                Glide.with(mContext).load(mList.get(position).getImage()).placeholder(R.mipmap.ic_person_default).into(viewHolder.mUserImage);
                viewHolder.mUserImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mOnClickHeadImageItem.onClickHeadImage(position,mList.get(position).getAuthid(),mList.get(position).getMemrole());
                    }
                });
            }
        }
        return convertView;
    }
    class ViewHolder {
        @Bind(R.id.user_image)
        ImageView mUserImage;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface onClickItem{
        void onClikc();
    }

    public interface onClickHeadImageItem{
        void onClickHeadImage(int pos , String authId,String authMemrole);
    }
}
