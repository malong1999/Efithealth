package com.maxiaobu.healthclub.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.beangson.BeanCommentOnly;
import com.maxiaobu.healthclub.common.beangson.BeanDynamicCommentDetail;
import com.maxiaobu.healthclub.common.beangson.BeanLoveList;
import com.maxiaobu.healthclub.common.beangson.BeanMineDynamic;
import com.maxiaobu.healthclub.ui.weiget.gridview.MyGridView;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import maxiaobu.mxbutilscodelibrary.ConstUtils;

import static maxiaobu.mxbutilscodelibrary.TimeUtils.getFriendlyTimeSpanByNow;

/**
 * Created by 莫小婷 on 2016/12/21.
 */
public class AdapterDynamicCommentDetail extends RecyclerView.Adapter {
    private Context mContext;
    //评论列表
    private List<BeanDynamicCommentDetail.PostBean.CommentlistBean> mCommentDetails;
    //头列表
    private List<BeanMineDynamic> headInfoList;
    private OnClickCommentLayout mOnClickCommentLayout;
    private OnClickMoreLayout mOnClickMoreLayout;
    private OnClickShareLayout mOnClickShareLayout;
    private OnClickGoodList mOnClickGoodList;
    private OnClickGoodLayout mOnClickGoodLayout;
    private OnClickImageLayout mOnClickImageLayout;
    private OnClickLayout mOnClickLayout;
    private OnListenHeadImage mOnListenHeadImage;
    private OnListenCommentHeadImage mOnListenCommentHeadImage;
    //点赞列表
    private List<BeanLoveList> LikeList;
    private int LikeNumber;

    public void setOnListenCommentHeadImage(OnListenCommentHeadImage onListenCommentHeadImage) {
        mOnListenCommentHeadImage = onListenCommentHeadImage;
    }

    public void setOnListenHeadImage(OnListenHeadImage onListenHeadImage) {
        mOnListenHeadImage = onListenHeadImage;
    }

    public void setLikeNumber(int likeNumber) {
        LikeNumber = likeNumber;
    }

    public void setOnClickLayout(OnClickLayout onClickLayout) {
        mOnClickLayout = onClickLayout;
    }

    public void setLikeList(List<BeanLoveList> likeList) {
        LikeList = likeList;
    }

    public void setHeadInfoList(List<BeanMineDynamic> headInfoList) {
        this.headInfoList = headInfoList;
    }

    public void setOnClickImageLayout(OnClickImageLayout onClickImageLayout) {
        mOnClickImageLayout = onClickImageLayout;
    }

    public void setOnClickGoodLayout(OnClickGoodLayout onClickGoodLayout) {
        mOnClickGoodLayout = onClickGoodLayout;
    }

    //当页评论列表信息
//    private List<BeanCommentOnly> mOnlies;
    private Boolean mBooleen;

    public void setBooleen(Boolean booleen) {
        mBooleen = booleen;
    }

//    public void setOnlies(List<BeanCommentOnly> onlies) {
//        mOnlies = onlies;
//    }

    public void setOnClickGoodList(OnClickGoodList onClickGoodList) {
        mOnClickGoodList = onClickGoodList;
    }

    public void setOnClickMoreLayout(OnClickMoreLayout onClickMoreLayout) {
        mOnClickMoreLayout = onClickMoreLayout;
    }

    public void setOnClickShareLayout(OnClickShareLayout onClickShareLayout) {
        mOnClickShareLayout = onClickShareLayout;
    }

    public void setOnClickCommentLayout(OnClickCommentLayout onClickCommentLayout) {
        mOnClickCommentLayout = onClickCommentLayout;
    }

    public AdapterDynamicCommentDetail(List<BeanDynamicCommentDetail.PostBean.CommentlistBean> commentDetails, Context context) {
        mCommentDetails = commentDetails;
        mContext = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case 0:
                View view = LayoutInflater.from(mContext).inflate(R.layout.item_rv_head_dynamic_comment, parent, false);
                holder = new HeadViewHolder(view);
                break;
            case 1:
                View view1 = LayoutInflater.from(mContext).inflate(R.layout.item_rv_content_dynamic_comment, parent, false);
                holder = new ContentViewHolder(view1);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        int type = getItemViewType(position);
        switch (type) {
            case 0:
                HeadViewHolder headViewHolder = (HeadViewHolder) holder;
                headViewHolder.mAdapterCommentDetailGridView.setOnClickItem(() -> mOnClickGoodList.onClickListener());
                headViewHolder.mAdapterCommentDetailGridView.setOnClickHeadImageItem((pos, authId, authMemrole) -> mOnListenHeadImage.onListenClick(pos,authId,authMemrole));
                headViewHolder.mItemCommentImage.setOnClickListener(v -> mOnClickCommentLayout.onclickComment(v));
                headViewHolder.mItemShare.setOnClickListener(v -> mOnClickShareLayout.onclickShare());
                headViewHolder.mItemMore.setOnClickListener(v -> mOnClickMoreLayout.onclickMore(headInfoList.get(0).getPostId()));
                headViewHolder.mItemGoodLayout.setOnClickListener(v -> {
                    mOnClickGoodLayout.onclickGood(headViewHolder.mItemGoodImage, headViewHolder.mList, headViewHolder.mAdapterCommentDetailGridView, headViewHolder.mDynamicCommentGoodText);
                });
                headViewHolder.mItemContentImage.setOnClickListener(view -> mOnClickImageLayout.onClickImage(headInfoList.get(0).getBigImage()));
                headViewHolder.mItemImage.setOnClickListener(view -> mOnListenHeadImage.onListenClick(position-1,headInfoList.get(0).getAuthId(),headInfoList.get(0).getMemrole()));
                if (!mBooleen) {
                    headViewHolder.mItemGoodImage.setImageResource(R.mipmap.ic_dynamic_good_df);
                } else {
                    headViewHolder.mItemGoodImage.setImageResource(R.mipmap.ic_dynamic_good_dw);
                }
                headViewHolder.mItemImage.setBorderWidth(3);
                headViewHolder.mItemImage.setBorderColor(0xffc4c4c4);
                Glide.with(mContext).load(headInfoList.get(0).getImage()).into(headViewHolder.mItemImage);
                headViewHolder.mItemName.setText(headInfoList.get(0).getName());
                headViewHolder.mItemTime.setText(headInfoList.get(0).getCreateTime());
                headViewHolder.mItemContentText.setText(headInfoList.get(0).getContentText());
                Glide.with(mContext).load(headInfoList.get(0).getContentImage()).placeholder(R.mipmap.ic_place_holder).into(headViewHolder.mItemContentImage);
                headViewHolder.mDynamicCommentTitle.setText("全部评论" + (mCommentDetails.size()));
                headViewHolder.mDynamicCommentGoodText.setText(LikeNumber + "点赞");
                break;
            case 1:
                ContentViewHolder contentViewHolder = (ContentViewHolder) holder;
                Glide.with(mContext).load(mCommentDetails.get(position - 1).getAuthimgsfilename()).into(contentViewHolder.mItemCommentImage);
                contentViewHolder.mItemCommentName.setText(mCommentDetails.get(position - 1).getAuthnickname());
                contentViewHolder.mItemCommentContent.setText(mCommentDetails.get(position - 1).getContent());
                contentViewHolder.mItemCommentTime.setText(getFriendlyTimeSpanByNow(mCommentDetails.get(position - 1).getCreatetime().getTime()));
                contentViewHolder.mContainer.setOnClickListener(view -> mOnClickLayout.onClickLayout(mCommentDetails.get(position - 1).getPostid(), mCommentDetails.get(position - 1).getAuthid(), position - 1));
                contentViewHolder.mItemCommentImage.setOnClickListener(view -> mOnListenCommentHeadImage.onListenCommentImageClick(position -1,mCommentDetails.get(position - 1).getAuthid(),mCommentDetails.get(position - 1).getMemrole()));
                break;
        }
    }

    @Override
    public int getItemCount() {
        //9 ： 留言的条数  1 ： 是固定的，是头布局
        return mCommentDetails.size() + headInfoList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    class HeadViewHolder extends RecyclerView.ViewHolder {
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
        ImageView mItemGoodImage;
        @Bind(R.id.item_good_layout)
        LinearLayout mItemGoodLayout;
        @Bind(R.id.item_comment_image)
        ImageView mItemCommentImage;
        @Bind(R.id.item_share)
        ImageView mItemShare;
        @Bind(R.id.item_more)
        ImageView mItemMore;
        @Bind(R.id.dynamic_comment_good_text)
        TextView mDynamicCommentGoodText;
        @Bind(R.id.dynamic_comment_grid_view)
        MyGridView mDynamicCommentGridView;
        @Bind(R.id.dynamic_comment_title)
        TextView mDynamicCommentTitle;

        private AdapterCommentDetailGridView mAdapterCommentDetailGridView;
        private List<BeanLoveList> mList;

        public HeadViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mList = new ArrayList<>();
            mAdapterCommentDetailGridView = new AdapterCommentDetailGridView(mContext);
            for (int i = 0; i < LikeList.size(); i++) {
                mList.add(LikeList.get(i));
            }
            mAdapterCommentDetailGridView.setList(mList);
            mDynamicCommentGridView.setAdapter(mAdapterCommentDetailGridView);
        }
    }

    class ContentViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.item_comment_image)
        ImageView mItemCommentImage;
        @Bind(R.id.item_comment_name)
        TextView mItemCommentName;
        @Bind(R.id.item_comment_time)
        TextView mItemCommentTime;
        @Bind(R.id.item_comment_content)
        TextView mItemCommentContent;
        @Bind(R.id.container)
        RelativeLayout mContainer;

        public ContentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnClickCommentLayout {
        void onclickComment(View view);
    }

    public interface OnClickShareLayout {
        void onclickShare();
    }

    public interface OnClickMoreLayout {
        void onclickMore(String postId);
    }

    public interface OnClickGoodList {
        void onClickListener();
    }

    public interface OnClickGoodLayout {
        void onclickGood(ImageView imageView, List<BeanLoveList> list, AdapterCommentDetailGridView adapter, TextView textView);
    }

    public interface OnClickImageLayout {
        void onClickImage(String image);
    }

    public interface OnClickLayout {
        void onClickLayout(String commentId, String authId, int pos);
    }

    public interface OnListenHeadImage{
        void onListenClick(int pos , String authId,String authMemrole);
    }

    public interface OnListenCommentHeadImage{
        void onListenCommentImageClick(int pos , String authId,String authMemrole);
    }

    /**
     * 获取友好型与当前时间的差
     *
     * @param millis 毫秒时间戳
     * @return 友好型与当前时间的差
     * <ul>
     * <li>如果小于1秒钟内，显示刚刚</li>
     * <li>如果在1分钟内，显示XXX秒前</li>
     * <li>如果在1小时内，显示XXX分钟前</li>
     * <li>如果在1小时外的今天内，显示今天15:32</li>
     * <li>如果是昨天的，显示昨天15:32</li>
     * <li>其余显示，2016-10-15</li>
     * <li>时间不合法的情况全部日期和时间信息，如星期六 十月 27 14:21:20 CST 2007</li>
     * </ul>
     */
    @SuppressLint("DefaultLocale")
    public String getFriendlyTimeSpanByNow(long millis) {
        long now = System.currentTimeMillis();
        long span = now - millis;
        if (span < 0)
            return String.format("%tc", millis);// U can read http://www.apihome.cn/api/java/Formatter.html to understand it.
        if (span < 1000) {
            return "刚刚";
        } else if (span < ConstUtils.MIN) {
            return String.format("%d秒前", span / ConstUtils.SEC);
        } else if (span < ConstUtils.HOUR) {
            return String.format("%d分钟前", span / ConstUtils.MIN);
        }

        // 获取当天00:00
        long wee = (now / ConstUtils.DAY) * ConstUtils.DAY;
        if (millis >= wee) {
            long day = span / (24 * 60 * 60 * 1000);
            long hour = (span / (60 * 60 * 1000) - day * 24);
            return hour + "小时前";
        } else if (millis >= wee - ConstUtils.DAY) {
            return "昨天";
        } else {
            long day = span / (24 * 60 * 60 * 1000);
            if (day >= 30) {
                long mon = day / 30;
                return mon + "个月前";
            } else {
                return day + "天前";
            }
        }

    }
}
