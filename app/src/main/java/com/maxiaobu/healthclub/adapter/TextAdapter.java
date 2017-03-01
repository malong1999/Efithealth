package com.maxiaobu.healthclub.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.common.beangson.BeanMineDynamic;
import com.maxiaobu.healthclub.utils.storage.SPUtils;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.maxiaobu.healthclub.R.id.view;
/**
 * Created by 莫小婷 on 2017/1/24.
 * email：maxiaobu1216@gmail.com
 * 功能：动态列表适配器
 * 伪码：
 * 待完成：
 */
public class TextAdapter extends RecyclerView.Adapter<TextAdapter.ViewHolder> {
    private Context mContext;
    private List<BeanMineDynamic> mLists;
    private OnListenGoodClickLayout mOnListenGoodClickLayout;
    private OnListenCommentClickLayout mOnListenCommentClickLayout;
    private OnListenShareClickLayout mOnListenShareClickLayout;
    private OnListenMoreClickLayout mOnListenMoreClickLayout;
    private OnListenItemClick mOnListenItemClick;
    private OnListenItemImageClick mOnListenItemImageClick;
    private List<Boolean> mBooleen;
    private List<Integer> num;

    public void setNum(List<Integer> num) {
        this.num = num;
    }

    public void setBooleen(List<Boolean> booleen) {
        mBooleen = booleen;
    }

    public void setOnListenItemImageClick(OnListenItemImageClick onListenItemImageClick) {
        mOnListenItemImageClick = onListenItemImageClick;
    }

    public void setOnListenItemClick(OnListenItemClick onListenItemClick) {
        mOnListenItemClick = onListenItemClick;
    }

    public void setOnListenMoreClickLayout(OnListenMoreClickLayout onListenMoreClickLayout) {
        mOnListenMoreClickLayout = onListenMoreClickLayout;
    }

    public void setOnListenShareClickLayout(OnListenShareClickLayout onListenShareClickLayout) {
        mOnListenShareClickLayout = onListenShareClickLayout;
    }

    public void setOnListenCommentClickLayout(OnListenCommentClickLayout onListenCommentClickLayout) {
        mOnListenCommentClickLayout = onListenCommentClickLayout;
    }

    public void setOnListenGoodClickLayout(OnListenGoodClickLayout onListenGoodClickLayout) {
        mOnListenGoodClickLayout = onListenGoodClickLayout;
    }

    public TextAdapter(Context context, List<BeanMineDynamic> lists) {
        mContext = context;
        mLists = lists;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_rv_dynamic_two, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mItemImage.setBorderWidth(3);
        holder.mItemImage.setBorderColor(0xffc4c4c4);
        holder.mItemName.setText(mLists.get(position).getName());
        Glide.with(mContext)
                .load(mLists.get(position).getImage())
                .into(holder.mItemImage);
        holder.mItemTime.setText(mLists.get(position).getCreateTime());
        holder.mItemContentText.setText(mLists.get(position).getContentText());
        Picasso.with(mContext).load(mLists.get(position).getContentImage())
                .into(holder.mItemContentImage);
        holder.mItemGoodNum.setText(mLists.get(position).getLoveNum());
        holder.mItemCommentNum.setText(mLists.get(position).getCommentNum());
        if (mLists.get(position).getImage().equals(SPUtils.getString(Constant.AVATAR))) {
            holder.mItemViewHead.setVisibility(View.VISIBLE);
            holder.mLlLayout.setVisibility(View.VISIBLE);
            if (mLists.get(position).getVisiable().equals("1")) {
                holder.mItemPremissionImage.setImageResource(R.mipmap.bt_public_dynamic1);
                holder.mItemPremissionText.setText("公开");
            } else if (mLists.get(position).getVisiable().equals("2")) {
                holder.mItemPremissionImage.setImageResource(R.mipmap.bt_friend_dynamic1);
                holder.mItemPremissionText.setText("好友圈");
            } else {
                holder.mItemPremissionImage.setImageResource(R.mipmap.bt_mine_dynamic1);
                holder.mItemPremissionText.setText("仅自己可见");
            }
        } else {
            holder.mLlLayout.setVisibility(View.GONE);
            holder.mItemViewHead.setVisibility(View.GONE);
        }

        holder.mItemLayout.setOnClickListener(v -> {
            if (mOnListenItemClick != null) {
                mOnListenItemClick.onItemListen(mLists.get(position).getPostId(), mLists.get(position).getVisiable(), position, mLists.get(position).getAuthId(),Integer.valueOf(mLists.get(position).getLoveNum()),Integer.valueOf(mLists.get(position).getCommentNum()));
            }
        });
        ViewHolder finalHolder = holder;
        holder.mItemGoodLayout.setOnClickListener(v -> {
            if (mOnListenGoodClickLayout != null)
                mOnListenGoodClickLayout.onClickGoodListen(finalHolder.mItemGoodImage, finalHolder.mItemGoodNum, position);
        });
        ViewHolder finalHolder1 = holder;
        holder.mItemGoodImage.setOnClickListener(v -> {
            if (mOnListenGoodClickLayout != null)
                mOnListenGoodClickLayout.onClickGoodListen(finalHolder1.mItemGoodImage, finalHolder1.mItemGoodNum, position);
        });
        ViewHolder finalHolder2 = holder;
        holder.mItemCommentLayout.setOnTouchListener((v, event) -> {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (mOnListenCommentClickLayout != null)
                    mOnListenCommentClickLayout.onClickCommentListenDown(finalHolder2.mItemCommentImage);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                if (mOnListenCommentClickLayout != null)
                    mOnListenCommentClickLayout.onClickCommentListenUp(finalHolder2.mItemCommentImage, position, mLists.get(position).getPostId(), mLists.get(position).getVisiable(), mLists.get(position).getAuthId(),Integer.valueOf(mLists.get(position).getLoveNum()),Integer.valueOf(mLists.get(position).getCommentNum()));
            }

            return true;
        });
        ViewHolder finalHolder3 = holder;
        holder.mItemCommentImage.setOnClickListener(v -> {
            if (mOnListenCommentClickLayout != null)
                mOnListenCommentClickLayout.onClickCommentListenUp(finalHolder3.mItemCommentImage, position, mLists.get(position).getPostId(), mLists.get(position).getVisiable(), mLists.get(position).getAuthId(),Integer.valueOf(mLists.get(position).getLoveNum()),Integer.valueOf(mLists.get(position).getCommentNum()));
        });

        ViewHolder finalHolder4 = holder;
        holder.mItemShare.setOnClickListener(v -> {
            if (mOnListenShareClickLayout != null) {

                mOnListenShareClickLayout.onClickShareListenUp(finalHolder4.mItemShare, position);
            }
        });

        ViewHolder finalHolder5 = holder;
        holder.mItemMore.setOnClickListener(v -> {
            if (mOnListenMoreClickLayout != null) {

                mOnListenMoreClickLayout.onClickMoreListenUp(finalHolder5.mItemMore, position,mLists.get(position).getAuthId(),mLists.get(position).getPostId());
            }
        });
        ViewHolder finalHolder6 = holder;
        holder.mItemContentImage.setOnClickListener(v -> {
            mOnListenItemImageClick.onImageClick(v, finalHolder6.mItemContentImage, position, mLists.get(position).getBigImage());
        });

        if (!mBooleen.get(position)) {
            holder.mItemGoodImage.setBackgroundResource(R.mipmap.ic_dynamic_good_df);
        } else {
            holder.mItemGoodImage.setBackgroundResource(R.mipmap.ic_dynamic_good_dw);
        }

    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return mLists.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_image)
        CircularImageView mItemImage;
        @Bind(R.id.item_name)
        TextView mItemName;
        @Bind(R.id.item_time)
        TextView mItemTime;
        @Bind(R.id.item_content_text)
        TextView mItemContentText;
        @Bind(R.id.item_content_image)
        ImageView mItemContentImage;
        @Bind(R.id.item_good_image)
        CheckBox mItemGoodImage;
        @Bind(R.id.item_good_num)
        TextView mItemGoodNum;
        @Bind(R.id.item_good_layout)
        LinearLayout mItemGoodLayout;
        @Bind(R.id.item_comment_image)
        ImageButton mItemCommentImage;
        @Bind(R.id.item_comment_num)
        TextView mItemCommentNum;
        @Bind(R.id.item_comment_layout)
        LinearLayout mItemCommentLayout;
        @Bind(R.id.item_share)
        ImageView mItemShare;
        @Bind(R.id.item_more)
        ImageView mItemMore;
        @Bind(R.id.item_layout)
        LinearLayout mItemLayout;
        @Bind(R.id.item_view)
        View mItemView;
        @Bind(R.id.item_premission_image)
        ImageView mItemPremissionImage;
        @Bind(R.id.item_premission_text)
        TextView mItemPremissionText;
        @Bind(R.id.ll_layout)
        LinearLayout mLlLayout;
        @Bind(R.id.item_view_head)
        View mItemViewHead;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnListenGoodClickLayout {
        void onClickGoodListen(CheckBox goodCheckBox, TextView textView, int pos);

    }

    public interface OnListenCommentClickLayout {
        void onClickCommentListenDown(ImageButton commentImageButton);

        void onClickCommentListenUp(ImageButton commentImageButton, int pos, String postid, String visiable, String authId,Integer likeNum , Integer commentNum);
    }

    public interface OnListenShareClickLayout {
        void onClickShareListenUp(ImageView shareImageButton, int pos);
    }

    public interface OnListenMoreClickLayout {
        void onClickMoreListenUp(ImageView moreImageButton, int pos,String memid,String postId);
    }

    public interface OnListenItemClick {
        void onItemListen(String postid, String visiable, int i, String authId,Integer likeNum , Integer commentNum);
    }

    public interface OnListenItemImageClick {
        void onImageClick(View view, ImageView imageView, int pos, String image);
    }
}
