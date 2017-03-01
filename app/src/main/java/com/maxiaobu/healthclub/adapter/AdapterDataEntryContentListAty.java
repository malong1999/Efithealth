package com.maxiaobu.healthclub.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.beangson.BeanItems;
import com.maxiaobu.healthclub.ui.activity.DataEntryActivity;
import com.maxiaobu.healthclub.utils.VibratorUtil;
import com.maxiaobu.healthclub.utils.recyeler.ItemTouchHelperAdapter;
import com.maxiaobu.healthclub.utils.recyeler.ItemTouchHelperViewHolder;
import com.maxiaobu.healthclub.utils.recyeler.OnStartDragListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 马小布 on 2016/8/31.
 * 数据录入
 */
public class AdapterDataEntryContentListAty extends RecyclerView.Adapter implements ItemTouchHelperAdapter, ItemTouchHelperViewHolder {
    private final OnStartDragListener mDragStartListener;
    private int fromPosition = -1;
    private int toPosition;

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        try {
            if (this.fromPosition == -1)
                this.fromPosition = fromPosition;
            this.toPosition = toPosition;
            VibratorUtil.Vibrate(mActivity, 30);
            Collections.swap(mData, fromPosition, toPosition);
            notifyItemMoved(fromPosition, toPosition);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void onItemDismiss(int position) {

    }

    @Override
    public void onItemSelected() {

    }

    @Override
    public void onItemClear() {
//        Log.d("AdapterDataEntryContent", "from" + fromPosition + "-----to:" + toPosition);
//        Toast.makeText(mActivity, "dskjfhsdkjhfkjh", Toast.LENGTH_SHORT).show();
        if (fromPosition != -1) {
            mActivity.upData(String.valueOf(fromPosition + 1), String.valueOf(toPosition + 1), mData.get(0).get(0));

            Cursor c = mActivity.query(mData.get(0).get(0));
            c.moveToFirst();
            mData.clear();
            List<String> data = new ArrayList<>();
            String itemID = c.getString(1);
            String group = c.getString(2);
            String strength = c.getString(3);
            String time = c.getString(4);
//            Log.d("AdapterDataEntryRightLi", strength);
            data.add(itemID);
            data.add(group);
            data.add(strength);
            data.add(time);
            mData.add(data);
            while (c.moveToNext()) {
                data = new ArrayList<>();
                itemID = c.getString(1);
                group = c.getString(2);
                strength = c.getString(3);
                time = c.getString(4);
                Log.d("data", strength);
                data.add(itemID);
                data.add(group);
                data.add(strength);
                data.add(time);
                mData.add(data);
            }
            c.close();
            fromPosition = -1;
            this.notifyDataSetChanged();
        }
    }


    public interface OnItemClickListener {
        public void onItemClick(View view, int position, List<List<String>> mData);
    }

    public OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public interface OnItemLongClickListener {
        public void onItemClick(View view, int position, List<List<String>> mData);
    }

    public OnItemLongClickListener mLongListener;

    /**
     * 长按弹出popuwindow
     * @param listener
     */
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mLongListener = listener;
    }

    public interface OnItemAddClickListener {
        void onItemClick(View view, int position, List<List<String>> mData);
    }

    public OnItemAddClickListener mAddListener;

    public void setOnItemAddClickListener(OnItemAddClickListener listener) {
        mAddListener = listener;
    }

    private DataEntryActivity mActivity;
    private List<List<String>> mData;
    private int selectPosition = 0;

    public AdapterDataEntryContentListAty(Activity activity, List<List<String>> mData, OnStartDragListener dragStartListener) {
        mDragStartListener = dragStartListener;
        mActivity = (DataEntryActivity) activity;
        this.mData = mData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_data_entry_content, parent, false);
            return new MyViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_data_entry_content_foot, parent, false);
            return new FootViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (position != mData.size()) {
            final MyViewHolder viewHolder = (MyViewHolder) holder;
            viewHolder.setIsRecyclable(false);
            List<String> list = mData.get(position);
            viewHolder.mTvGroup.setText(list.get(1));
            viewHolder.mTvStrength.setText(list.get(2) + "kg");
            viewHolder.mTvTimes.setText(list.get(3) + "次，");
            //修改
            viewHolder.mLyRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.onItemClick(view, position, mData);
                    }
                }
            });

            viewHolder.mLyRoot.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mLongListener != null) {
                        mLongListener.onItemClick(v, position, mData);
                    }
                    return true;
                }
            });


            viewHolder.mIvRight.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                        VibratorUtil.Vibrate(mActivity, 30);
                        mDragStartListener.onStartDrag(holder);
                    }
                    return false;
                }
            });
        } else {
            FootViewHolder viewHolder = (FootViewHolder) holder;
            viewHolder.mLyfoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mAddListener != null) {
                        mAddListener.onItemClick(v, position, mData);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mData.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position != mData.size()) {
            return 0;
        } else {
            return 1;
        }
    }


    static class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_group)
        TextView mTvGroup;
        @Bind(R.id.tv_times)
        TextView mTvTimes;
        @Bind(R.id.tv_strength)
        TextView mTvStrength;
        @Bind(R.id.iv_right)
        ImageView mIvRight;
        @Bind(R.id.ly_root)
        RelativeLayout mLyRoot;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class FootViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.ly_foot)
        FrameLayout mLyfoot;

        public FootViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
